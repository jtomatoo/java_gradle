package learning;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Calculator {
	
	public Integer calcMultiply(String filePath) throws IOException {
		LineCallback<Integer> multiplyCallback = 
			new LineCallback<Integer>() {
				@Override
				public Integer doSomethingWithLine(String line, Integer value) {
					return value * Integer.parseInt(line);
				}
			};
			
		return lineReadTemplate(filePath, multiplyCallback, 1);
		/*
		BufferReaderCallback multiplyCallback = 
			new BufferReaderCallback() {
				@Override
				public Integer doSomethingWithReader(BufferedReader br) throws IOException {
					Integer multiply = 1;
					String line = null;
					while((line = br.readLine()) != null) {
						multiply *= Integer.valueOf(line);
					}
					
					return multiply;
				}
			};
		
		return fileReadTemplate(filePath, multiplyCallback);
		*/
	}
	
	public Integer calcSum(String filePath) throws IOException {
		LineCallback<Integer> sumCallback = 
			new LineCallback<Integer>() {
				@Override
				public Integer doSomethingWithLine(String line, Integer value) {
					return value + Integer.valueOf(line);
				}
			};
			
		return lineReadTemplate(filePath, sumCallback, 0);
		/*
		BufferReaderCallback sumCallback = 
			new BufferReaderCallback() {
				@Override
				public Integer doSomethingWithReader(BufferedReader br) throws IOException {
					Integer sum = 0;
					String line = null;
					while((line = br.readLine()) != null) {
						sum += Integer.valueOf(line);
					}
					
					return sum;
				}
			};
		
		return fileReadTemplate(filePath, sumCallback);
		*/
		/*
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(filePath));
			Integer sum = 0;
			String line = null;
			while((line = br.readLine()) != null) {
				sum += Integer.valueOf(line);
			}
			return sum;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			throw e;
		} finally {
			if(br != null) {
				try {
					br.close();
				} catch (IOException ie) {
				}
			}
		}
		*/
	}
	
	public Integer fileReadTemplate(String filePath, BufferReaderCallback callback) throws IOException {
		
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(filePath));
			int ret = callback.doSomethingWithReader(br);
			return ret;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			throw e;
		} finally {
			if(br != null) {
				try {
					br.close();
				} catch (IOException ie) {
					System.out.println(ie.getMessage());
				}
			}
		}
		
	}
	
	public <T> T lineReadTemplate(String filePath, LineCallback<T> callback, T initVal) throws IOException {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(filePath));
			T res = initVal;
			String line = null;
			while((line = br.readLine()) != null) {
				res = callback.doSomethingWithLine(line, res);
			}
			return res;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			throw e;
		} finally {
			if(br != null) {
				try {
					br.close();
				} catch (IOException ie) {
					System.out.println(ie.getMessage());
				}
			}
		}
	}
	
	public String concatenate(String filePath) throws IOException {
		LineCallback<String> concatenateCallback = 
			new LineCallback<String>() {
				@Override
				public String doSomethingWithLine(String line, String value) {
					return value + line;
				}
			};
		
		return lineReadTemplate(filePath, concatenateCallback, "");
	}
}
