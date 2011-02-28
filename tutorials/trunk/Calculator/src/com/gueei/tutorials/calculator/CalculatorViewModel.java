package com.gueei.tutorials.calculator;

import java.text.DecimalFormat;
import java.util.Stack;

import android.view.View;

import com.gueei.android.binding.Command;
import com.gueei.android.binding.DependentObservable;
import com.gueei.android.binding.Observable;

public class CalculatorViewModel {
	public String current = "0";
	public final Observable<Double> Display = new Observable<Double>(Double.class, 0d);
	public final DependentObservable<String> FormattedDisplay = new DependentObservable<String>(String.class, Display){
		@Override
		public String calculateValue(Object... arg0) throws Exception {
			DecimalFormat format = new DecimalFormat();
			format.applyPattern("#.######");
			String output = format.format(Display.get());
			if (output.length() <= MAXLENGTH) return output;
			format.applyPattern("0.########E00");
			return format.format(Display.get());
		}
	};
	
	public NumberCommand Number9 = new NumberCommand(9);
	public NumberCommand Number8 = new NumberCommand(8);
	public NumberCommand Number7 = new NumberCommand(7);
	public NumberCommand Number6 = new NumberCommand(6);
	public NumberCommand Number5 = new NumberCommand(5);
	public NumberCommand Number4 = new NumberCommand(4);
	public NumberCommand Number3 = new NumberCommand(3);
	public NumberCommand Number2 = new NumberCommand(2);
	public NumberCommand Number1 = new NumberCommand(1);
	public NumberCommand Number0 = new NumberCommand(0);
	public OperatorCommand Plus = new OperatorCommand(new Operator(1, "+", false){
		@Override
		public double calculate(double operandA, double operandB) {
			return operandA + operandB;
		}});
	public OperatorCommand Minus = new OperatorCommand(new Operator(1, "-", false){
		@Override
		public double calculate(double operandA, double operandB) {
			return operandA - operandB;
		}});
	public OperatorCommand Multiply = new OperatorCommand(new Operator(2, "x", false){
		@Override
		public double calculate(double operandA, double operandB) {
			return operandA * operandB;
		}});
	public OperatorCommand Divide = new OperatorCommand(new Operator(2, "/", false){
		@Override
		public double calculate(double operandA, double operandB) {
			return operandA / operandB;
		}});
	public OperatorCommand Equal = new OperatorCommand(new Operator(0, "=", true){
		@Override
		public double calculate(double operandA, double operandB) {
			return operandA;
		}});
	public Command Dot = new Command(){
		public void Invoke(View arg0, Object... arg1) {
			putDot();
		}
	};
	public Command AllClear = new Command(){
		public void Invoke(View arg0, Object... arg1) {
			onAllClear();
		}		
	};
	public Command Back = new Command(){
		public void Invoke(View arg0, Object... arg1) {
			onBack();
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
		Display.set(0d);
	}
	
	private void onBack(){
		if (lastCommandIsOperator) return;
		String temp = current;
		temp = temp.substring(0, temp.length()-1);
		if (temp.length()==0)
			temp = "0";
		current = temp;
		Display.set(Double.parseDouble(current));
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
		Display.set(Double.parseDouble(current));
	}
	
	private void putDot(){
		String temp = current;
		if (temp.equals("0"))
			current = "0.";
		else{
			if (temp.indexOf(".") <0)
				current = temp + ".";
		}
		Display.set(Double.parseDouble(current));
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
				Display.set(result);
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
	
	private class NumberCommand implements Command{
		private int mNumber;
		public NumberCommand(int number){
			mNumber = number;
		}
		public void Invoke(View arg0, Object... arg1) {
			addNumber(mNumber);
		}
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
