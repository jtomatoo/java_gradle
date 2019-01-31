package domain.shop;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.PostLoad;
import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;

import domain.shop.listener.DuckListener;

@Entity
@EntityListeners(value=DuckListener.class)
public class Duck {

	
	@Id
	@GeneratedValue
	public Long id;
	
	private String name;
	/*
	@PrePersist
	public void prePersist() {
		System.out.println("Duck.prePersist id=" + id);
	}
	
	@PostPersist
	public void postPersist() {
		System.out.println("Duck.postPresist id=" + id);
	}
	
	@PostLoad
	public void postLoad() {
		System.out.println("Duck.postLoad");
	}
	
	@PreRemove
	public void preRemove() {
		System.out.println("Duck.preRemove");
	}
	
	@PostRemove
	public void postRemove() {
		System.out.println("Duck.postRemove");
	}
	*/
}
