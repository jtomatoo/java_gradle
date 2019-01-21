package controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;

public abstract class GenericController<T, K, S> {
	
	private S service;
	
	@RequestMapping("/add")
	public void add(T entity) {}
	
	@RequestMapping("/update")
	public void update(T entity) {}
	
	@RequestMapping("/view")
	public T view(K id) {
		T t = null;
		return t;
	}
	
	@RequestMapping("/delete")
	public void delete(K id) {}
	
	@RequestMapping("/list")
	public List<T> list() {
		List<T> list = new ArrayList<T>();
		
		return list;
	}
	
}
