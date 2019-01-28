package util;

import org.springframework.core.convert.converter.Converter;

import domain.Level;

public class LevelToStringConverter implements Converter<Level, String>{

	@Override
	public String convert(Level level) {
		return String.valueOf(level.initValue());
	}

}
