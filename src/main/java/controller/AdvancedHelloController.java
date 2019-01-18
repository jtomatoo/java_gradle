package controller;

import java.util.Map;

public class AdvancedHelloController extends SimpleController {

	public AdvancedHelloController() {
		this.setRequiredParams(new String[] {"name"});
		this.setViewName("/WEB-INF/view/hello.jsp");
	}
	
	@Override
	public void control(Map<String, String> params, Map<String, Object> model) throws Exception {
		model.put("message", "Hello " + params.get("name"));
	}

}
