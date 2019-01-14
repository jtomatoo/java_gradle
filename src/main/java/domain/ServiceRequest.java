package domain;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class ServiceRequest {

	private String customerNo;
	
	private String productNo;
	
	private String description;
	
}
