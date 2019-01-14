package domain;

public class StringPrinter implements Printer {

	private StringBuffer buffer = new StringBuffer();
	
	@Override
	public void print(String meessgae) {
		this.buffer.append(meessgae);
	}

	@Override
	public String toString() {
		return this.buffer.toString();
	}

}
