package adapter;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.servlet.HandlerAdapter;
import org.springframework.web.servlet.ModelAndView;

import app.anno.RequiredParams;
import app.anno.ViewName;
import controller.CustomHelloController;

public class SimpleHandlerAdapter implements HandlerAdapter {

	@Override
	public boolean supports(Object handler) {
		return (handler instanceof CustomHelloController);
	}

	@Override
	public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		Method m = ReflectionUtils.findMethod(handler.getClass(), "control", Map.class, Map.class);
		ViewName viewName = AnnotationUtils.getAnnotation(m, ViewName.class);
		RequiredParams requiredParams = AnnotationUtils.getAnnotation(m, RequiredParams.class);
		
		Map<String, String> params = new HashMap<String, String>();
		for (String param : requiredParams.value()) {
			String value = request.getParameter(param);
			if(value == null) {
				throw new IllegalStateException();
			}
			params.put(param, value);
		}
		
		Map<String, Object> model = new HashMap<String, Object>();
		
		((CustomHelloController)handler).control(params, model);
		
		return new ModelAndView(viewName.value(), model);
	}

	@Override
	public long getLastModified(HttpServletRequest request, Object handler) {
		return -1;
	}

}
