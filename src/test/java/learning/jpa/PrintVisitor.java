package learning.jpa;

public class PrintVisitor implements Visitor {

	@Override
	public void visit(Book book) {
		System.out.println("book.class= " + book.getClass());
		System.out.println("[PrintVisitor][力格:" + book.getName() + " 历磊:" + book.getAuthor() + "]");
	}

	@Override
	public void visit(Album album) {
		System.out.println("album.class= " + album.getClass());
		System.out.println("[PrintVisitor][力格:" + album.getName() + " 酒萍胶飘:" + album.getArtist() + "]");
	}

	@Override
	public void visit(Movie movie) {
		System.out.println("movie.class= " + movie.getClass());
		System.out.println("[PrintVisitor][力格:" + movie.getName() + " 力累磊:" + movie.getDirector() + "]");
	}

}
