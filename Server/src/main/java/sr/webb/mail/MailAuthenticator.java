/*
 * Created on 2005-apr-10
 */
package sr.webb.mail;

/**
 * @author WMPABOD
 *
 * Authenticator class
 */
import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

public class MailAuthenticator extends Authenticator {
	String username, password;
	
	public MailAuthenticator(String username, String password){
		this.username = username;
		this.password = password;
	}

	public PasswordAuthentication getPasswordAuthentication(String username, String password) {
		return new PasswordAuthentication(username, password);
	}
}
