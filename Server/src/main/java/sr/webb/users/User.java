/*
 * Created on 2005-feb-10
 */
package sr.webb.users;

import sr.general.StringTokenizerPlusPlus;
import sr.general.logging.Logger;
import sr.webb.mail.Base64;

/**
 * @author WMPABOD
 *
 * Handles data for one user
 */
public class User {
	private String login,name,password;
	private String emails; // is a space separated list of email addresses
	private boolean recieveTurnMail,recieveGameMail,recieveAdminMail;
	/**
	 * 0 = guest
	 * 1 = player
	 * 2 = administrator
	 */
	private int accessLevel = -1;
	public static final String ROLE_GUEST = "guest";
	public static final String ROLE_PLAYER = "player";
	public static final String ROLE_ADMIN = "admin";
	public static final int WANT_EMAIL_TURN = 0;
	public static final int WANT_EMAIL_GAME = 1;	
	public static final int WANT_EMAIL_ADMIN = 2;
	
	public User(String userData){
		StringTokenizerPlusPlus stpp = new StringTokenizerPlusPlus(userData,"\t");
		name = stpp.nextToken();
		login = stpp.nextToken();
		password = stpp.nextToken();
		String tmpAccessLevel = stpp.nextToken();
		if (tmpAccessLevel.equals(ROLE_ADMIN)){
			accessLevel = 2;
		}else
		if (tmpAccessLevel.equals(ROLE_PLAYER)){
			accessLevel = 1;
		}else{ // guest
			accessLevel = 0;
		}
		if (stpp.hasMoreTokens()){
			emails = stpp.nextToken();
			String tmp = stpp.nextToken();
			if (tmp.equalsIgnoreCase("true")){
				recieveTurnMail = true;
			}
			tmp = stpp.nextToken();
			if (tmp.equalsIgnoreCase("true")){
				recieveGameMail = true;
			}
			tmp = stpp.nextToken();
			if (tmp.equalsIgnoreCase("true")){
				recieveAdminMail = true;
			}
		}else{
			emails = "";
			recieveTurnMail = true;
			recieveGameMail = true;
			recieveAdminMail = true;
		}
	}
	
	public boolean isUser(String aUserLogin){
		boolean same = false;
		if ((login != null) & (aUserLogin != null)){
			same = (login.equalsIgnoreCase(aUserLogin));
		}
		return same;
	}
	
	public boolean checkPassword(String aPassword){
		boolean same = false;
		if ((password != null) & (aPassword != null)){
			same = (password.equalsIgnoreCase(aPassword));
			if (!same){ // password may be encrypted
				// encrypt password
				byte[] passwordBytes = password.getBytes();
				String encPassword = Base64.encodeBytes(passwordBytes);
				same = aPassword.equals(encPassword);
			}
		}
		return same;		
	}
	
	public String getSaveString(int index){
		String saveString = "user" + index + " = ";
		saveString = saveString + name + "\t";
		saveString = saveString + login + "\t";
		saveString = saveString + password + "\t";
		switch (accessLevel) {
		case 0:
			saveString = saveString + ROLE_GUEST + "\t";
			break;
		case 1:
			saveString = saveString + ROLE_PLAYER + "\t";
			break;
		case 2:
			saveString = saveString + ROLE_ADMIN + "\t";
			break;
		}
		saveString = saveString + emails + "\t";
		saveString = saveString + recieveTurnMail + "\t";
		saveString = saveString + recieveGameMail + "\t";
		saveString = saveString + recieveAdminMail + "\t";
		return saveString;
	}

	@SuppressWarnings("unused")
	private boolean hasRights(int level){
		return (level >= accessLevel);
	}

	private boolean hasRightsNO(int level){
		return (accessLevel >= level);
	}	
	
	private boolean hasExactRights(int level){
		return (level == accessLevel);
	}

	public boolean isAdmin(){
		return hasExactRights(2);
	}
	
	public boolean isPlayerOrAdmin(){
		return hasRightsNO(1);
	}

	public boolean isGuest(){
		return hasExactRights(0);
	}

	public String getName(){
		return name;
	}
	
	public String getLogin(){
		return login;
	}

	public String getPassword(){
		return password;
	}

	public String getRole(){
		String tmpRole = "guest";
		if (accessLevel == 1){
			tmpRole = "player";
		}else
		if (accessLevel == 2){
			tmpRole = "administrator";
		}
		return tmpRole;
	}
	
	public void setPassword(String newPassword){
		password = newPassword;
	}
	
	public String getEmails() {
		return emails;
	}
	
	public boolean getRecieveMail(int type) {
		boolean retVal = false;
		switch (type) {
		case WANT_EMAIL_TURN:
			retVal = recieveTurnMail;
			break;
		case WANT_EMAIL_GAME:
			retVal = recieveGameMail;
			break;
		case WANT_EMAIL_ADMIN:
			retVal = recieveAdminMail;
			break;
		}
		return retVal;
	}
	
	public void setAllFields(String userName, String userLogin, String userPassword, String userRole,String email,String turnEmail,String gameEmail,String adminEmail){
		Logger.finer("setAllFields: turnEmail: " + turnEmail + ", newGameEmail: " + gameEmail);
		name = userName;
		login = userLogin;
		password = userPassword;
		String tmpAccessLevel = userRole;
		if (tmpAccessLevel.equals(ROLE_ADMIN)){
			accessLevel = 2;
		}else
		if (tmpAccessLevel.equals(ROLE_PLAYER)){
			accessLevel = 1;
		}else{ // guest
			accessLevel = 0;
		}
		this.emails = email;
		String tmp = turnEmail;
		if ((tmp != null) && (tmp.equalsIgnoreCase("true"))){
			recieveTurnMail = true;
		}else{
			recieveTurnMail = false;
		}
		tmp = gameEmail;
		if ((tmp != null) && (tmp.equalsIgnoreCase("true"))){
			recieveGameMail = true;
		}else{
			recieveGameMail = false;
		}
		tmp = adminEmail;
		if ((tmp != null) && (tmp.equalsIgnoreCase("true"))){
			recieveAdminMail = true;
		}else{
			recieveAdminMail = false;
		}
	}
	
	@Override
	public String toString(){
		StringBuffer sb = new StringBuffer();
		sb.append("User: name=");
		sb.append(name);
		sb.append(" login=");
		sb.append(login);
		return sb.toString();
	}
}
