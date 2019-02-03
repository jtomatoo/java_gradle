package learning.jpa;

public interface Visitor {

	public void visit(Book book);
	public void visit(Album album);
	public void visit(Movie movie);
}
