package servlet;

import static org.junit.Assert.assertThat;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hamcrest.core.Is;
import org.junit.Test;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.SimpleServletHandlerAdapter;

public class ServletControllerTest extends AbstractDispatcherServletTest {

	@Test
	public void helloServletController() throws ServletException, IOException {
		setClasses(SimpleServletHandlerAdapter.class, HelloServlet.class);
		initRequest("/hello").addParameter("name", "Spring");
		
		assertThat(runService().getContentAsString(), Is.is("Hello Spring"));
	}
	
	@Component("/hello")
	public static class HelloServlet extends HttpServlet {
		
		 protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
			 String name = req.getParameter("name");
			 resp.getWriter().print("Hello " + name);
	    }
	}
}
