package controller;

import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.nio.charset.Charset;

import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;

import org.hamcrest.CoreMatchers;
import org.hamcrest.core.Is;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CharsetEditor;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import domain.Level;
import domain.shop.Member;
import servlet.AbstractDispatcherServletTest;
import util.LevelPropertyEditor;
import util.MinMaxPropertyEditor;
import util.UserValidator;

public class SessionAttributeTest extends AbstractDispatcherServletTest {

	
	@Test
	public void simpleWebDataBinder() {
		WebDataBinder dataBinder = new WebDataBinder(null);
		dataBinder.registerCustomEditor(Level.class, new LevelPropertyEditor());
		assertThat(dataBinder.convertIfNecessary("1", Level.class), Is.is(Level.BASIC));
	}
	
	
	@Test
	public void charsetEditor() {
		CharsetEditor charsetEditor = new CharsetEditor();
		charsetEditor.setAsText("UTF-8");
		assertThat(charsetEditor.getValue(), Is.is(CoreMatchers.instanceOf(Charset.class)));
		assertThat((Charset)charsetEditor.getValue(), Is.is(Charset.forName("UTF-8")));
	}
	
	@Test
	public void simpleCustomPropertyEditor() {
		LevelPropertyEditor levelEditor = new LevelPropertyEditor();
		levelEditor.setAsText("3");
		assertThat((Level)levelEditor.getValue(), Is.is(Level.GOLD));
		
		levelEditor.setValue(Level.BASIC);
		assertThat(levelEditor.getAsText(), Is.is("1"));
	}
	
	@Test
	public void sessionAttr() throws ServletException, IOException {
		setClasses(UserController.class);
		// GET 요청 - form()
		initRequest("/user/edit").addParameter("id", "1");
		runService();
		
		HttpSession session = request.getSession();
		assertThat(session.getAttribute("user"), Is.is(getModelAndView().getModel().get("user")));
		
		// POST 요청 - submit()
		initRequest("/user/edit", "POST").addParameter("id", "1").addParameter("name", "Spring2");
		request.setSession(session);
		runService();
		
		assertThat(((User)getModelAndView().getModel().get("user")).getEmail(), Is.is("mail@spring.com"));
		assertThat(session.getAttribute("user"), Is.is(CoreMatchers.nullValue()));
	}
	
	@Test
	public void simpleConversion() {
		setLocations("classpath:WEB-INF/applicationContext.xml");
	}
	
	@Controller
	public static class SearchController {
		
		@Autowired
		private ConversionService conversionService;
		
		@InitBinder
		public void initBinder(WebDataBinder dataBinder) {
			dataBinder.setConversionService(conversionService);
		}
	}
	
	
	@Controller
	@SessionAttributes("user")
	public static class UserController {
		
		@Autowired
		private UserValidator validator;
		
		@InitBinder
		public void initBinder(WebDataBinder dataBinder) {
			dataBinder.registerCustomEditor(Level.class, new LevelPropertyEditor());
			dataBinder.registerCustomEditor(int.class, "age", new MinMaxPropertyEditor(0, 200));
			dataBinder.setValidator(this.validator);
		}
		
		@RequestMapping(value="/user/edit", method=RequestMethod.GET)
		public User form(@RequestParam int id) {
			return new User(1, "Spring", "mail@spring.com");
		}
		
		@RequestMapping(value="/user/edit", method=RequestMethod.POST)
		public void submit(@ModelAttribute User user, SessionStatus sessionStatus) {
			sessionStatus.setComplete();
		}
		
		/*
		@RequestMapping("/add")
		public void add(@ModelAttribute User user, BindingResult result) {
			this.validator.validate(user, result);
			if(result.hasErrors()) {
				
			} else {
				
			}
		}
		 */
		@RequestMapping("/add")
		public void add(@ModelAttribute @Validated User user, BindingResult result) {
			
		}
	}
	
	
	public static class User {
		private int id;
		private String name;
		private String email;
		
		
		public User() {
		}
		
		public User(int id, String name, String email) {
			super();
			this.id = id;
			this.name = name;
			this.email = email;
		}

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}
	}
}
