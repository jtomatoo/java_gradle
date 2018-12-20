package learning;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.hamcrest.CoreMatchers;
import org.hamcrest.core.Is;
import org.hamcrest.core.IsNot;
import org.hamcrest.core.IsSame;
import org.junit.Test;
import org.junit.matchers.JUnitMatchers;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.support.NullValue;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:config/junit.xml")
public class JunitTest {
	
	@Autowired
	private ApplicationContext context;

//	public static JunitTest testObject;
	public static Set<JunitTest> testObjects = new HashSet<JunitTest>();
	
	public static ApplicationContext contextObject = null;
	
	@Test
	public void test1() {
//		assertThat(this, Is.is(IsNot.not(IsSame.sameInstance(testObject))));
//		testObject = this;
		assertThat(testObjects, IsNot.not(JUnitMatchers.hasItem(this)));
		testObjects.add(this);
		
		assertThat(contextObject == null || contextObject == this.context, Is.is(true));
		contextObject = this.context;
	}
	
	@Test
	public void test2() {
//		assertThat(this, Is.is(IsNot.not(IsSame.sameInstance(testObject))));
//		testObject = this;
		assertThat(testObjects, IsNot.not(JUnitMatchers.hasItem(this)));
		testObjects.add(this);
		
		assertTrue(contextObject == null || contextObject == this.context);
		contextObject = this.context;
	}
	
	@Test
	public void test3() {
//		assertThat(this, Is.is(IsNot.not(IsSame.sameInstance(testObject))));
//		testObject = this;
		assertThat(testObjects, IsNot.not(JUnitMatchers.hasItem(this)));
		testObjects.add(this);
		
		assertThat(contextObject, JUnitMatchers.either(Is.is(CoreMatchers.nullValue())).or(Is.is(this.context)));
		contextObject = this.context;
	}
}
