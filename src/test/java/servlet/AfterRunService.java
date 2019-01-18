package servlet;

import java.io.UnsupportedEncodingException;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ModelAndView;

public interface AfterRunService {
	public String getContentAsString() throws UnsupportedEncodingException;
	
	public WebApplicationContext getContext();
	
	public <T> T getBean(Class<T> beanType);
	
	public ModelAndView getModelAndView();
	
	public AfterRunService assertViewName(String viewName);
	
	public AfterRunService assertModel(String name, Object value);
}
