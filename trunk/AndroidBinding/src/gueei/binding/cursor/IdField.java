package gueei.binding.cursor;

@SuppressWarnings({"UnusedDeclaration"})
public class IdField extends LongField {

	public IdField(int columnIndex) {
		super(columnIndex);
	}
	
	public IdField(String columnName){
		super(columnName);
	}
}
