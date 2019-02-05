package learning.jpa;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:WEB-INF/test_applicationContext.xml")
@Transactional
public class JpaTransationTest {

	@Test
	public void simpleVersion() {
	}
}
