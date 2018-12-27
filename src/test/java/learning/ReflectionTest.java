package learning;

import static org.junit.Assert.assertThat;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.hamcrest.core.Is;
import org.junit.Test;

import service.proxy.Hello;
import service.proxy.HelloTarget;
import service.proxy.HelloUppercase;
import service.proxy.UppercaseHandler;

public class ReflectionTest {

	@Test
	public void invokeMethod() throws Exception {
		String name = "Spring";
		
		// length()
		assertThat(name.length(), Is.is(6));
		
		Method lengthMethod = String.class.getMethod("length");
		assertThat((Integer)lengthMethod.invoke(name), Is.is(6));
		
		// charAt()
		assertThat(name.charAt(0), Is.is('S'));
		
		Method charAtMethod = String.class.getMethod("charAt", int.class);
		assertThat((Character)charAtMethod.invoke(name, 0), Is.is('S'));
	}
	
	@Test
	public void simpleProxy() {
		Hello hello = new HelloTarget();
		assertThat(hello.sayHello("Toby"), Is.is("Hello Toby"));
		assertThat(hello.sayHi("Toby"), Is.is("Hi Toby"));
		assertThat(hello.sayThankYou("Toby"), Is.is("Thank You Toby"));
	}
	
	@Test
	public void simpleProxy2() {
//		Hello proxiedHello = new HelloUppercase(new HelloTarget());
		Hello proxiedHello = (Hello)Proxy.newProxyInstance(
				getClass().getClassLoader(), 
				new Class[] {Hello.class}, 
				new UppercaseHandler(new HelloTarget()));
		assertThat(proxiedHello.sayHello("Toby"), Is.is("HELLO TOBY"));
		assertThat(proxiedHello.sayHi("Toby"), Is.is("HI TOBY"));
		assertThat(proxiedHello.sayThankYou("Toby"), Is.is("THANK YOU TOBY"));
	}
}
