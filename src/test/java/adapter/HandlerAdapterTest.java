package adapter;

import java.io.IOException;

import javax.servlet.ServletException;

import org.junit.Test;

import controller.CustomHelloController;
import servlet.AbstractDispatcherServletTest;

public class HandlerAdapterTest extends AbstractDispatcherServletTest {
	
	@Test
	public void simpleHandlerAdapter() throws ServletException, IOException {
		setClasses(SimpleHandlerAdapter.class, CustomHelloController.class);
		initRequest("/hello").addParameter("name", "Spring").runService();
		
		assertViewName("/WEB-INF/view/hello.jsp");
		assertModel("message", "Hello Spring");
	}

}
