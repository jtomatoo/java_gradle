package app.anno;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.springframework.context.annotation.Import;

import app.SqlServiceContext;

@Import(value=SqlServiceContext.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface EnableSqlService {
	
}
