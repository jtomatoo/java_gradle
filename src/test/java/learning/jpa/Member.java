package learning.jpa;

import javax.jdo.annotations.Cacheable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.QueryHint;

@Cacheable
@Entity
@NamedQuery(hints=@QueryHint(name="org.hibernate.cacheable", value="true"),
			name="Member.findByUsername",
			query="select m.address from Member m where m.name= :username"
)
public class Member {
	
	@Id
	private String id;
	
	private String name;

	public Member() {
	}

	public Member(String id, String name) {
		this.id = id;
		this.name = name;
	}


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public int hashCode() {
		return name != null ? name.hashCode() : 0;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		/*
		if (getClass() != obj.getClass()) {
			return false;
		}
		*/
		if(!(obj instanceof Member)) {
			return false;
		}
		
		Member other = (Member) obj;
/*		
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id)) {
			return false;
		}
*/		
		if (name == null) {
			if (other.name != null)
				return false;
		}
		/*
		else if (!name.equals(other.name)) {
			return false;
		}
		*/
		else if(!name.equals(other.getName())) {
			return false;
		}
		
		return true;
	}
	
	
}
