package learning;

import static org.junit.Assert.assertThat;

import java.util.HashSet;
import java.util.Set;

import org.hamcrest.CoreMatchers;
import org.hamcrest.core.Is;
import org.hamcrest.core.IsNot;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.matchers.Null;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.context.support.StaticApplicationContext;

import app.config.AnnotatedHelloConfig;
import domain.AnnotatedHello;
import domain.Hello;
import domain.Printer;
import domain.StringPrinter;

public class AppContextTest {

	private StaticApplicationContext ac;
	
	@Before
	public void setUp() {
		ac = new StaticApplicationContext();
	}
	
	
	@Test
	public void registerBean() {
		ac.registerSingleton("hello1", Hello.class);
		
		Hello hello1= ac.getBean("hello1", Hello.class);
		assertThat(hello1, IsNot.not(Null.class));
		
		BeanDefinition helloDef = new RootBeanDefinition(Hello.class);
		helloDef.getPropertyValues().addPropertyValue("name", "Spring");
		ac.registerBeanDefinition("hello2", helloDef);
		
		Hello hello2 = ac.getBean("hello2", Hello.class);
		assertThat(hello2.sayHello(), Is.is("Hello Spring"));
		
		assertThat(hello1, IsNot.not(hello2));
		
		assertThat(ac.getBeanFactory().getBeanDefinitionCount(), Is.is(2));
	}
	
	@Test
	public void registerBeanWithDependency() {
		ac.registerBeanDefinition("printer", new RootBeanDefinition(StringPrinter.class));
		
		BeanDefinition helloDef = new RootBeanDefinition(Hello.class);
		helloDef.getPropertyValues().addPropertyValue("name", "Spring");
		helloDef.getPropertyValues().addPropertyValue("printer", new RuntimeBeanReference("printer"));
		
		ac.registerBeanDefinition("hello", helloDef);
		
		Hello hello = ac.getBean("hello", Hello.class);
		hello.print();
		
		assertThat(ac.getBean("printer").toString(), Is.is("Hello Spring"));
	}
	
	@Test
	public void genericApplicationContext() {
		/*
		GenericApplicationContext ac = new GenericApplicationContext();
		XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(ac);
		reader.loadBeanDefinitions("classpath:config/genericApplication-context.xml");
		
		ac.refresh();
		*/
		GenericApplicationContext ac = new GenericXmlApplicationContext("classpath:config/genericApplication-context.xml");
		
		Hello hello = ac.getBean("hello", Hello.class);
		hello.print();
		
		assertThat(ac.getBean("printer").toString(), Is.is("Hello Spring"));
	}
	
	@Test
	public void stratifiedSetupContext() {
		ApplicationContext parent = new GenericXmlApplicationContext("classpath:config/parent-context.xml");
		GenericApplicationContext child = new GenericApplicationContext(parent);
		
		XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(child);
		reader.loadBeanDefinitions("classpath:config/child-context.xml");
		child.refresh();
		
		Printer printer = child.getBean("printer", Printer.class);
		assertThat(printer, IsNot.not(CoreMatchers.nullValue()));
		
		Hello hello = child.getBean("hello", Hello.class);
		assertThat(hello, IsNot.not(CoreMatchers.nullValue()));
		
		hello.print();
		assertThat(printer.toString(), Is.is("Hello Child"));
		
	}
	
	@Test
	public void simpleBeanScanning() {
		ApplicationContext ctx = new AnnotationConfigApplicationContext("domain");
		
		AnnotatedHello hello = ctx.getBean("annotatedHello", AnnotatedHello.class);
		
		assertThat(hello, IsNot.not(CoreMatchers.nullValue()));
		
	}
	
	@Test
	public void simpleBeanFactory() {
		ApplicationContext ctx = new AnnotationConfigApplicationContext(AnnotatedHelloConfig.class);
		AnnotatedHello hello = ctx.getBean("annotatedHello", AnnotatedHello.class);
		
		assertThat(hello, IsNot.not(CoreMatchers.nullValue()));
		
		AnnotatedHelloConfig config = ctx.getBean("annotatedHelloConfig", AnnotatedHelloConfig.class);
		assertThat(config, IsNot.not(CoreMatchers.nullValue()));
		
//		assertThat(config.annotatedHello(), is(samsameInstance(hello)));
		
	}
	
	@Test
	public void simpleAtAutowired() {
		AbstractApplicationContext ac = new AnnotationConfigApplicationContext(BeanA.class, BeanB.class);
		BeanA beanA = ac.getBean(BeanA.class);
		assertThat(beanA.beanB, IsNot.not(CoreMatchers.nullValue()));
	}
	
	private static class BeanA {
		
		@Autowired
		private BeanB beanB;
	}
	
	private static class BeanB {
	}
	
	@Test
	public void singletonScope() {
		ApplicationContext ac = new AnnotationConfigApplicationContext(SingletonBean.class, SingletonClientBean.class);
		Set<SingletonBean> beans = new HashSet<SingletonBean>();
		
		beans.add(ac.getBean(SingletonBean.class));
		beans.add(ac.getBean(SingletonBean.class));
		
		assertThat(beans.size(), Is.is(1));
		
		beans.add(ac.getBean(SingletonClientBean.class).bean1);
		beans.add(ac.getBean(SingletonClientBean.class).bean2);
		assertThat(beans.size(), Is.is(1));
	}
	
	private static class SingletonBean {
	}
	
	private static class SingletonClientBean {
		
		@Autowired
		private SingletonBean bean1;
		
		@Autowired
		private SingletonBean bean2;
	}
	
	
	@Test
	public void prototypeScope() {
		ApplicationContext ac = new AnnotationConfigApplicationContext(PrototypeBean.class, PrototypeClientBean.class);
		Set<PrototypeBean> beans = new HashSet<PrototypeBean>();
		
		beans.add(ac.getBean(PrototypeBean.class));
		assertThat(beans.size(), Is.is(1));
		beans.add(ac.getBean(PrototypeBean.class));
		assertThat(beans.size(), Is.is(2));
		
		beans.add(ac.getBean(PrototypeClientBean.class).bean1);
		assertThat(beans.size(), Is.is(3));
		
		beans.add(ac.getBean(PrototypeClientBean.class).bean2);
		assertThat(beans.size(), Is.is(4));
	}
	
	@Scope("prototype")
	private static class PrototypeBean {
		
	}
	
	private static class PrototypeClientBean {
		
		@Autowired
		private PrototypeBean bean1;
		
		@Autowired
		private PrototypeBean bean2;
	}
}
