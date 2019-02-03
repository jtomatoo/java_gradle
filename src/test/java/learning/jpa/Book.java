package learning.jpa;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("B")
public class Book extends Item {

	private String author;
	
	private String isbn;

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	@Override
	public String getTitle() {
		return "[제목:" + getName() + ", 저자:" + author + "]";
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}
}
