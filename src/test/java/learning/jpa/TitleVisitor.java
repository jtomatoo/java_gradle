package learning.jpa;

public class TitleVisitor implements Visitor {

	private String title;
	
	public String getTitle() {
		return title;
	}

	@Override
	public void visit(Book book) {
		title = "[����:" + book.getName() + " ����:" + book.getAuthor() + "]";
	}

	@Override
	public void visit(Album album) {
		title = "[����:" + album.getName() + " ��Ƽ��Ʈ:" + album.getArtist() + "]";
	}

	@Override
	public void visit(Movie movie) {
		title = "[����:" + movie.getName() + " ������:" + movie.getDirector() + "]";
	}

	
}
