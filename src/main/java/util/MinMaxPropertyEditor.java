package util;

import java.beans.PropertyEditorSupport;

public class MinMaxPropertyEditor extends PropertyEditorSupport {

	private int min;
	private int max;

	public MinMaxPropertyEditor(int min, int max) {
		this.min = min;
		this.max = max;
	}
	
	@Override
	public String getAsText() {
		return String.valueOf((Integer)this.getValue());
	}
	
	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		Integer val = Integer.parseInt(text);
		if(val < min) {
			val = min;
		} else if(val > max) {
			val = max;
		}
		setValue(val);
	}
}
