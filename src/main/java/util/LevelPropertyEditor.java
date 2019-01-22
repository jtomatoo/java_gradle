package util;

import java.beans.PropertyEditorSupport;

import domain.Level;

public class LevelPropertyEditor extends PropertyEditorSupport {

	@Override
	public String getAsText() {
		return String.valueOf(((Level)this.getValue()).initValue());
	}
	
	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		this.setValue(Level.valueOf(Integer.parseInt(text.trim())));
	}
}
