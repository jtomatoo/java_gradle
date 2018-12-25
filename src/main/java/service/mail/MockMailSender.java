package service.mail;

import java.util.ArrayList;
import java.util.List;

import org.springframework.mail.MailException;import org.springframework.mail.MailMessage;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

public class MockMailSender implements MailSender{

	private List<String> requests = new ArrayList<String>();

	public List<String> getRequests() {
		return requests;
	}

	@Override
	public void send(SimpleMailMessage simpleMessage) throws MailException {
		requests.add(simpleMessage.getTo()[0]);
	}

	@Override
	public void send(SimpleMailMessage[] simpleMessages) throws MailException {
		
	}

}
