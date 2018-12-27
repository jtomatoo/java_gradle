package factory;

import static org.junit.Assert.assertThat;

import org.hamcrest.core.Is;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import domain.Message;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:config/FactoryBeanTest-context.xml")
public class FacotryBeanTest {
	@Autowired
	private ApplicationContext context;
	
	@Test
	public void getMessageFromFactoryBean() {
		Object message = context.getBean("message");
		assertThat(message, Is.is(Message.class));
		assertThat(((Message)message).getText(), Is.is("Factory Bean"));
	}
	
	public void getFactoryBean() throws Exception {
		Object factory = context.getBean("&message");
		assertThat(factory, Is.is(MessageFactoryBean.class));
	}
}
