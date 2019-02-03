package learning.jpa;

public class PrintVisitor implements Visitor {

	@Override
	public void visit(Book book) {
		System.out.println("book.class= " + book.getClass());
		System.out.println("[PrintVisitor][����:" + book.getName() + " ����:" + book.getAuthor() + "]");
	}

	@Override
	public void visit(Album album) {
		System.out.println("album.class= " + album.getClass());
		System.out.println("[PrintVisitor][����:" + album.getName() + " ��Ƽ��Ʈ:" + album.getArtist() + "]");
	}

	@Override
	public void visit(Movie movie) {
		System.out.println("movie.class= " + movie.getClass());
		System.out.println("[PrintVisitor][����:" + movie.getName() + " ������:" + movie.getDirector() + "]");
	}

}
