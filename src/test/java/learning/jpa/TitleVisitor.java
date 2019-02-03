package learning.jpa;

public class TitleVisitor implements Visitor {

	private String title;
	
	public String getTitle() {
		return title;
	}

	@Override
	public void visit(Book book) {
		title = "[力格:" + book.getName() + " 历磊:" + book.getAuthor() + "]";
	}

	@Override
	public void visit(Album album) {
		title = "[力格:" + album.getName() + " 酒萍胶飘:" + album.getArtist() + "]";
	}

	@Override
	public void visit(Movie movie) {
		title = "[力格:" + movie.getName() + " 力累磊:" + movie.getDirector() + "]";
	}

	
}
