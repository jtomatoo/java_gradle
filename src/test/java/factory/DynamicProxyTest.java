package factory;

import static org.junit.Assert.assertThat;

import java.lang.reflect.Proxy;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.hamcrest.core.Is;
import org.junit.Test;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;

import service.proxy.Hello;
import service.proxy.HelloTarget;
import service.proxy.UppercaseHandler;

public class DynamicProxyTest {

	@Test
	public void simpleProxy() {
		Hello proxiedHello = (Hello) Proxy.newProxyInstance(
				getClass().getClassLoader(),
				new Class[] {Hello.class},
				new UppercaseHandler(new HelloTarget()));
	}
	
	@Test
	public void proxyFactoryBean() {
		ProxyFactoryBean pfBean = new ProxyFactoryBean();
		pfBean.setTarget(new HelloTarget());
		pfBean.addAdvice(new UppercaseAdvice());
		
		Hello proxiedHello = (Hello) pfBean.getObject();
		assertThat(proxiedHello.sayHello("Toby"), Is.is("HELLO TOBY"));
		assertThat(proxiedHello.sayHi("Toby"), Is.is("HI TOBY"));
		assertThat(proxiedHello.sayThankYou("Toby"), Is.is("THANK YOU TOBY"));
	}
	
	public static class UppercaseAdvice implements MethodInterceptor {

		@Override
		public Object invoke(MethodInvocation invocation) throws Throwable {
			String ret = (String)invocation.proceed();
			return ret.toUpperCase();
		}
	}
	
	@Test
	public void pointcutAdvisor() {
		ProxyFactoryBean pfBean = new ProxyFactoryBean();
		pfBean.setTarget(new HelloTarget());
		
		NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
		pointcut.setMappedName("sayH*");
		
		pfBean.addAdvisor(new DefaultPointcutAdvisor(pointcut, new UppercaseAdvice()));
		
		Hello proxiedHello = (Hello) pfBean.getObject();
		
		assertThat(proxiedHello.sayHello("Toby"), Is.is("HELLO TOBY"));
		assertThat(proxiedHello.sayHi("Toby"), Is.is("HI TOBY"));
		assertThat(proxiedHello.sayThankYou("Toby"), Is.is("Thank You Toby"));
	}
	
	@Test
	public void clasNamePointcutAdvisor() {
		// 포인트컷 준비
		NameMatchMethodPointcut classMethodPointcut = new NameMatchMethodPointcut() {
			public ClassFilter getClassFilter() {
				return new ClassFilter() {
					@Override
					public boolean matches(Class<?> clazz) {
						return clazz.getSimpleName().startsWith("HelloT");
					}
				};
			}
		};
		classMethodPointcut.setMappedName("sayH*");
		
		checkAdviced(new HelloTarget(), classMethodPointcut, true);
		
		class HelloWorld extends HelloTarget{};
		checkAdviced(new HelloWorld(), classMethodPointcut, false);
		
		class HelloToby extends HelloTarget{};
		checkAdviced(new HelloToby(), classMethodPointcut, true);
	}

	private void checkAdviced(HelloTarget target, NameMatchMethodPointcut pointcut, boolean adviced) {
		ProxyFactoryBean pfBean = new ProxyFactoryBean();
		pfBean.setTarget(target);
		pfBean.addAdvisor(new DefaultPointcutAdvisor(pointcut, new UppercaseAdvice()));
		Hello proxiedHello = (Hello)pfBean.getObject();
		
		if(adviced) {
			assertThat(proxiedHello.sayHello("Toby"), Is.is("HELLO TOBY"));
			assertThat(proxiedHello.sayHi("Toby"), Is.is("HI TOBY"));
			assertThat(proxiedHello.sayThankYou("Toby"), Is.is("Thank You Toby"));
		} else {
			assertThat(proxiedHello.sayHello("Toby"), Is.is("Hello Toby"));
			assertThat(proxiedHello.sayHi("Toby"), Is.is("Hi Toby"));
			assertThat(proxiedHello.sayThankYou("Toby"), Is.is("Thank You Toby"));
		}
	}
}
