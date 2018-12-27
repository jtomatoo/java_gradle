package learning;

import static org.junit.Assert.assertThat;

import org.hamcrest.core.Is;
import org.junit.Test;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;

import learning.point.Bean;
import learning.point.Target;

public class PointcutExpressionTest {

	@Test
	public void methodSignaturePointcut() throws SecurityException, NoSuchMethodException {
		AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
		pointcut.setExpression("execution(public int " +
			"learning.point.Target.minus(int,int)" + 
				" throws java.lang.RuntimeException)");
		
		// Target.minus()
		assertThat(pointcut.getClassFilter().matches(Target.class) && 
					pointcut.getMethodMatcher().matches(Target.class.getMethod("minus", int.class, int.class), null), Is.is(true));
		// Target.plus()
		assertThat(pointcut.getClassFilter().matches(Target.class) && 
					pointcut.getMethodMatcher().matches(Target.class.getMethod("plus", int.class, int.class), null), Is.is(false));
		
		// Bean.method()
		assertThat(pointcut.getClassFilter().matches(Bean.class) &&
					pointcut.getMethodMatcher().matches(Target.class.getMethod("method", null), null), Is.is(false));
	}
}
