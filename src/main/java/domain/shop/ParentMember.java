package domain.shop;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Cacheable
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class ParentMember {
	
	@Id
	@GeneratedValue
	private Long id;
	
	private String name;
	
	@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
	@OneToMany(mappedBy="parentMember", cascade=CascadeType.ALL)
	private List<ChildMember> childMembers = new ArrayList<ChildMember>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<ChildMember> getChildMembers() {
		return childMembers;
	}

	public void setChildMembers(List<ChildMember> childMembers) {
		this.childMembers = childMembers;
	}
}
