package learning;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import org.hamcrest.core.Is;
import org.junit.Before;
import org.junit.Test;

public class CalcSumTest {

	public Calculator calculator;
	
	public String numFilePath;
	
	@Before
	public void setUp() {
		this.calculator = new Calculator();
		this.numFilePath = getClass().getResource("numbers.txt").getPath();
	}
	
	@Test
	public void sumOfNumbers() throws IOException {
		Calculator calculator = new Calculator();
		int sum = calculator.calcSum(getClass().getResource("numbers.txt").getPath());
		
		assertThat(sum, Is.is(10));
	}
	
	@Test
	public void multiplyOfNumbers() throws IOException {
		assertThat(calculator.calcMultiply(this.numFilePath), Is.is(24));
	}
	
	@Test
	public void concatenateStrings() throws IOException {
		assertThat(calculator.concatenate(this.numFilePath), Is.is("1234"));
	}
}
