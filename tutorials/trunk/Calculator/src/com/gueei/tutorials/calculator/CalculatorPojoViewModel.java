package com.gueei.tutorials.calculator;

import java.text.DecimalFormat;
import java.util.Stack;

import android.view.View;

import com.gueei.android.binding.Command;
import com.gueei.android.binding.DependentObservable;
import com.gueei.android.binding.Observable;
import com.gueei.android.binding.pojo.PojoViewModel;
import com.gueei.android.binding.pojo.PojoViewModelHelper;

public class CalculatorPojoViewModel implements PojoViewModel {
	private PojoViewModelHelper mHelper = new PojoViewModelHelper();
	public PojoViewModelHelper getHelper() {
		return mHelper;
	}

	public void notifyPropertyChanged(String propertyName) {
		mHelper.notifyPropertyChanged(propertyName);
	}
	
	public String current = "0";

	private double mDisplay;
	public double getDisplay(){
		return mDisplay;
	}
	
	public void setDisplay(double value){
		mDisplay = value;
		notifyPropertyChanged("Display");
		notifyPropertyChanged("FormattedDisplay");
	}
	
	public String getFormattedDisplay(){
		DecimalFormat format = new DecimalFormat();
		format.applyPattern("#.######");
		String output = format.format(mDisplay);
		if (output.length() <= MAXLENGTH) return output;
		format.applyPattern("0.########E00");
		return format.format(mDisplay);
	}
	
	public void Number9(){
		addNumber(9);
	}
	public void Number8(){
		addNumber(8);
	}
	public void Number7(){
		addNumber(7);
	}
	public void Number6(){
		addNumber(6);
	}
	public void Number5(){
		addNumber(5);
	}
	public void Number4(){
		addNumber(4);
	}
	public void Number3(){
		addNumber(3);
	}
	public void Number2(){
		addNumber(2);
	}
	public void Number1(){
		addNumber(1);
	}
	public void Number0(){
		addNumber(0);
	}
	public void Dot(){
		putDot();
	}
	
	public void Plus(){
		operate(plusOperator);
	}
	public void Minus(){
		operate(minusOperator);
	}
	public void Multiply(){
		operate(multiplyOperator);
	}
	public void Divide(){
		operate(divideOperator);
	}
	public void Equal(){
		operate(equalOperator);
	}
	public void AllClear(){
		onAllClear();
	}
	public void Back(){
		onBack();
	}

	private final Operator plusOperator = new Operator(1, "+", false){
		@Override
		public double calculate(double operandA, double operandB) {
			return operandA + operandB;
		}
	};
	private final Operator minusOperator = new Operator(1, "-", false){
		@Override
		public double calculate(double operandA, double operandB) {
			return operandA - operandB;
		}
	};
	private final Operator multiplyOperator = new Operator(2, "x", false){
		@Override
		public double calculate(double operandA, double operandB) {
			return operandA * operandB;
		}
	};
	private final Operator divideOperator = new Operator(2, "/", false){
		@Override
		public double calculate(double operandA, double operandB) {
			return operandA / operandB;
		}
	};
	private final Operator equalOperator = new Operator(0, "=", false){
		@Override
		public double calculate(double operandA, double operandB) {
			return operandA;
		}
	};

	private Stack<Double> operands = new Stack<Double>();
	private Stack<Operator> operators = new Stack<Operator>();
	
	private final int MAXLENGTH = 10;
	
	private boolean lastCommandIsOperator = false;

	private void onAllClear(){
		current = "0";
		operands.clear();
		operators.clear();
		setDisplay(0d);
	}
	
	private void onBack(){
		if (lastCommandIsOperator) return;
		String temp = current;
		temp = temp.substring(0, temp.length()-1);
		if (temp.length()==0)
			temp = "0";
		current = temp;
		setDisplay(Double.parseDouble(current));
	}
	
	private void addNumber(int number){
		lastCommandIsOperator = false;
		String temp = current;
		if (temp.length() >= MAXLENGTH) return;
		if (temp.equals("0") && (temp.indexOf(".") <0)){
			current = Integer.toString(number);
		}else{
			current = temp + number;
		}
		setDisplay(Double.parseDouble(current));
	}
	
	private void putDot(){
		String temp = current;
		if (temp.equals("0"))
			current = "0.";
		else{
			if (temp.indexOf(".") <0)
				current = temp + ".";
		}
		setDisplay(Double.parseDouble(current));
	}
	
	private void operate(Operator operator){
		if (!lastCommandIsOperator){
			// Push the current number to stack
			operands.push(Double.parseDouble(current));
			// current.set("0");
			lastCommandIsOperator = true;
		}else{
			if (!operators.empty())
				operators.pop();
		}
		
		Operator lastOperator = null;
		while (!operators.empty()){
			lastOperator = operators.peek();
			if (lastOperator.getLevel()>=operator.getLevel()){
				// Calculate
				double result;
				if (lastOperator.isUnary())
					result = lastOperator.calculate(operands.pop(), 0d);
				else{
					double b = operands.pop();
					double a = operands.pop();
					result = lastOperator.calculate(a, b);
				}
				setDisplay(result);
				operands.push(result);
				operators.pop();
			}
			else{
				break;
			}
		}
		// Special case for level 0 (=)
		if (operator.getLevel()>0)
			operators.push(operator);
		
		current = "0";
	}
	
	private class OperatorCommand implements Command{
		private final Operator mOperator;
		public OperatorCommand(Operator operator){
			mOperator = operator;
		}
		public void Invoke(View arg0, Object... arg1) {
			operate(mOperator);
		}
	}
	
	private abstract static class Operator{
		private final int mLevel;
		private final String mSymbol;
		private final boolean mUnary;
		public Operator(int level, String symbol, boolean unary){
			mLevel = level;
			mSymbol = symbol;
			mUnary = unary;
		}
		public boolean isUnary(){
			return mUnary;
		}
		public int getLevel(){
			return mLevel;
		}
		public String getSymbol(){
			return mSymbol;
		}
		
		public abstract double calculate(double operandA, double operandB);
	}

	
}
