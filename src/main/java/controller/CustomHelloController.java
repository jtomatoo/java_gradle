package controller;

import java.util.Map;

import org.springframework.stereotype.Component;

import app.anno.RequiredParams;
import app.anno.ViewName;

@Component("/hello")
public class CustomHelloController implements SimpleControllerInterface {

	@Override
	@ViewName("/WEB-INF/view/hello.jsp")
	@RequiredParams({"name"})
	public void control(Map<String, String> params, Map<String, Object> model) {
		model.put("message", "Hello " + params.get("name"));
	}
}


