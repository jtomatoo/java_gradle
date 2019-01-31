package domain.shop.listener;

import javax.persistence.PostPersist;
import javax.persistence.PrePersist;

public class DuckListener {

	@PrePersist
	private void prePersist(Object obj) {
		System.out.println("DuckListener.prePersist obj = [" +  obj + "]");
	}
	
	@PostPersist
	private void postPersist(Object obj) {
		System.out.println("DuckListener.postPersist obj = [" + obj + "]" );
	}
}
