package servlet;

import static org.junit.Assert.assertThat;

import javax.servlet.ServletException;

import org.hamcrest.core.Is;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletConfig;
import org.springframework.web.servlet.ModelAndView;

import controller.HelloController;
import domain.HelloSpring;

public class ServletTest {

	
	@Test
	public void simpleGet() throws Exception {
		MockHttpServletRequest req = new MockHttpServletRequest("GET", "/hello");
		req.addParameter("name", "Spring");
		
		MockHttpServletResponse res = new MockHttpServletResponse();
		
		SimpleGetServlet servlet = new SimpleGetServlet();
		servlet.service(req, res);
		assertThat(res.getContentAsString(), Is.is("<HTML><BODY>Hello Spring</BODY></HTML>"));
		assertThat(res.getContentAsString().contains("Hello Spring"), Is.is(true));

	}
	
	@Test
	public void simpleDispatcherServlet() throws Exception {
		ConfigurableDispatcherServlet servlet = new ConfigurableDispatcherServlet();
		servlet.setRelativeLocations(getClass(), "spring-servlet.xml");
		servlet.setClasses(HelloSpring.class);
		servlet.init(new MockServletConfig());
		
		MockHttpServletRequest req = new MockHttpServletRequest("GET", "/hello");
		req.addParameter("name", "Spring");
		
		MockHttpServletResponse res = new MockHttpServletResponse();
		
		servlet.service(req, res);
		
		ModelAndView mav = servlet.getModelAndView();
		assertThat(mav.getViewName(), Is.is("/WEB-INF/view/hello.jsp"));
		assertThat((String)mav.getModel().get("message"), Is.is("Hello Spring"));
	}
	
	@Test
	public void simpleController() throws Exception {
		ApplicationContext ac = new GenericXmlApplicationContext("classpath:WEB-INF/spring-servlet.xml", 
																	"classpath:WEB-INF/applicationContext.xml");
		
		HelloController helloController = ac.getBean(HelloController.class);
		
		MockHttpServletRequest req = new MockHttpServletRequest("GET", "/hello");
		req.addParameter("name", "Spring");
		MockHttpServletResponse res = new MockHttpServletResponse();
		
		ModelAndView mav = helloController.handleRequest(req, res);
	}
}
