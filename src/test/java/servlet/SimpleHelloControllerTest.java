package servlet;

import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;

import org.hamcrest.core.Is;
import org.junit.Test;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import controller.AdvancedHelloController;
import domain.HelloSpring;

public class SimpleHelloControllerTest extends AbstractDispatcherServletTest {

	@Test
	public void helloController() throws ServletException, IOException {
		ModelAndView mav = setLocations("classpath:WEB-INF/spring-servlet.xml", "classpath:WEB-INF/applicationContext.xml")
							.setClasses(HelloSpring.class)
							.initRequest("/hello", RequestMethod.GET)
							.addParameter("name", "Spring")
							.runService()
							.getModelAndView();
		assertThat(mav.getViewName(), Is.is("/WEB-INF/view/hello.jsp"));
		assertThat((String)mav.getModel().get("message"), Is.is("Hello Spring"));
	}
	
	@Test
	public void advancedHelloController() throws Exception {
		Map<String, String> params = new HashMap<String, String>();
		params.put("name", "Spring");
		Map<String, Object> model = new HashMap<String, Object>();
		
		new AdvancedHelloController().control(params, model);
		
		assertThat((String)model.get("message"), Is.is("Hello Spring"));
	}
}
