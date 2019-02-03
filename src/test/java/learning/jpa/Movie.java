package learning.jpa;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("M")
public class Movie extends Item {
	
	private String director;
	private String actor;
	
	public String getDirector() {
		return director;
	}
	public void setDirector(String director) {
		this.director = director;
	}
	public String getActor() {
		return actor;
	}
	public void setActor(String actor) {
		this.actor = actor;
	}
	
	
	@Override
	public String getTitle() {
		return "[제목:" + getName() + ", 감독:" + director + ", 배우:" + actor + "]";
	}
	
	
	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}
}
