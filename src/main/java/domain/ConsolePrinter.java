package domain;

public class ConsolePrinter implements Printer {

	@Override
	public void print(String meessgae) {
		System.out.println(meessgae);
	}

}
