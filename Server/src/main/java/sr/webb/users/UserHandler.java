/*
 * Created on 2005-jan-12
 */
package sr.webb.users;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import sr.general.Functions;
import sr.general.logging.Logger;
import sr.server.ServerHandler;
import sr.server.properties.PropertiesHandler;
import sr.webb.mail.MailHandler;

/**
 * @author wmpabod
 *
 * Handles all users as a properties file
 */
public class UserHandler {
	private static Properties properties;
	private static final String DEFAULT_PROPERTIES_NAME = "users", TEMP_PROPERTIES_NAME = "tempUsers";
	private static List<User> allUsers, allTempUsers;
	private static User guestUser = findUser("guest"); // used to speed up user checks
	private static final String COOKIE_LOGIN = "sr_user";
	private static final String COOKIE_TIMESTAMP = "sr_id";
//	private static final String COOKIE_TEMP_LOGIN = "sr_temp_user";
//	private static final String COOKIE_TEMP_TIMESTAMP = "sr_temp_id";
	private static Map<String,Long> sessions = new HashMap<String,Long>();
	
	/**
	 * Reads login and password from request. If they match a user timestanp 
	 * is created and set in cookie and hashmap. If incorrect errormessage is 
	 * returned.
	 * @param request
	 * @param response
	 * @return errormessage, "ok" if login is performed
	 */
	public static String loginUser(HttpServletRequest request, HttpServletResponse response){
		String loginParam = request.getParameter("login");
		String pwdParam = request.getParameter("password");
		
		return loginUser(loginParam, pwdParam, response);
		
	}
	
	public static String loginUser(String loginParam, String pwdParam, HttpServletResponse response){
		String message = "";
		String userExists = "";
		System.out.println(loginParam + " " + pwdParam);
		if ((loginParam != null) && (pwdParam != null)){
			userExists = UserHandler.isUser(loginParam,pwdParam);
			if (userExists.equals("yes")){
			    System.out.println("userExists = yes");
				User loggedInUser = UserHandler.getUser(loginParam,pwdParam);
				System.out.println(loggedInUser.getName());
				long timestamp = new Date().getTime();
				System.out.println(timestamp);
				sessions.put(loggedInUser.getLogin(),new Long(timestamp));
				Cookie userCookie = new Cookie(COOKIE_LOGIN,loggedInUser.getLogin());
				userCookie.setMaxAge(60*60*24*365);
				Cookie idCookie = new Cookie(COOKIE_TIMESTAMP,String.valueOf(timestamp));
				idCookie.setMaxAge(60*60*24*365);
			    response.addCookie(userCookie);
			    response.addCookie(idCookie);
				message = "ok";
			}else{
				message = userExists;
			}
		}else{
			if (loginParam == null){
				message = "Login name is empty";
			}else{
				message = "Password is empty";
			}
		}
		return message;
	}
	
	
	/**
	 * Removes a user from the sessions map.
	 * Cookies are left as they are.
	 * @param request
	 * @param response
	 * @return
	 */
	public static String logoutUser(HttpServletRequest request, HttpServletResponse response){
		String message = "";
		Cookie[] cookies = request.getCookies();
		Cookie userLoginCookie = findLogin(COOKIE_LOGIN,cookies);
		if (userLoginCookie != null){
			// login cookie found, search for password
			String userLogin = userLoginCookie.getValue();
			sessions.remove(userLogin);
		}		
		return message;
	}
	
	private static Cookie findLogin(String cookieName, Cookie[] cookies){
		Cookie found = null;
		if (cookies != null){
			int index = 0;
			while ((found == null) & (index < cookies.length)) {
				Cookie cookie = cookies[index];
				System.out.println("Cookie: name=" + cookie.getName() + " value=" + cookie.getValue());
				if (cookie.getName().equalsIgnoreCase(cookieName)){
					System.out.println("Cookie found!");
					found = cookie;
				}else{
					index++;
				}
			}
		}else{
			System.out.println("UserHandler.findLogin: cookies is null");
		}
		return found;
	}
	
	public static User getUser(HttpSession session, HttpServletRequest request, HttpServletResponse response){
		System.out.println("UserHandler.getUser()");
		User tmpUser = null;
		Cookie[] cookies = request.getCookies();
		Cookie userLoginCookie = findLogin(COOKIE_LOGIN,cookies);
		System.out.println("COOKIE_LOGIN = " + userLoginCookie != null);
		if (userLoginCookie != null){
			// login cookie found, search for timestamp
			Cookie userTimestampCookie = findLogin(COOKIE_TIMESTAMP,cookies);
			if (userTimestampCookie != null){
				String userLogin = userLoginCookie.getValue();
				try{
					Long userTimestamp = Long.parseLong(userTimestampCookie.getValue());
					Long sessionTimestamp = sessions.get(userLogin);
					if (sessionTimestamp != null){
						if (userTimestamp.longValue() == sessionTimestamp.longValue()){
							User foundUser = findUser(userLogin);
							if (foundUser != null){
								tmpUser = foundUser; 
							}else{
								tmpUser = guestUser;
							}
						}else{
							tmpUser = guestUser;
						}
					}else{
						tmpUser = guestUser;
					}
				}catch(NumberFormatException nfe){
					tmpUser = guestUser;
				}
			}else{
				tmpUser = guestUser;
			}
		}else{
			tmpUser = guestUser;
		}		
		return tmpUser;
	}

	public static String isUser(String userLogin, String password){
		String message = "";
		User foundUser = findUser(userLogin);
		if (foundUser != null){
			boolean passwordOk = foundUser.checkPassword(password);
			if (passwordOk){
				message = "yes";
			}else{
				message = "User found but password is incorrect";
			}
		}else{
			message = "User not found";
		}
		return message;
	}
	
	public static String isTempUser(String userLogin, String password){
		String message = "";
		User foundUser = findTempUser(userLogin);
		if (foundUser != null){
			boolean passwordOk = foundUser.checkPassword(password);
			if (passwordOk){
				message = "yes";
			}else{
				message = "User found but password is incorrect";
			}
		}else{
			message = "User not found";
		}
		return message;
	}

	public static User getUser(String userLogin, String password){
		User foundUser = findUser(userLogin);
		return foundUser;
	}
	
	public static User getTempUser(String userLogin, String password){
		User foundUser = findTempUser(userLogin);
		return foundUser;
	}
	
	public static User findUser(String userLogin){
//		System.out.println("findUser: " + userLogin);
		User foundUser = null;
		List<User> users = getList();
		for (User aUser : users) {
			if (aUser.isUser(userLogin)){
				foundUser = aUser;
			}
		}
//		System.out.println("findUser, found: " + foundUser);
		return foundUser;
	}
	
	private static User findTempUser(String userLogin){
		User foundUser = null;
		List<User> users = getTempList();
		for (User aUser : users) {
			if (aUser.isUser(userLogin)){
				foundUser = aUser;
			}
		}
		return foundUser;
	}
	
	private static boolean isUserUniqueName(String name){
		
		for (User user : allUsers) {
			if(user.getName().equals(name)){
				return false;
			}
		}
		return true;
	}
	
	private static boolean isUserUniqueLogin(String login){
		
		for (User user : allUsers) {
			if(user.getLogin().equals(login)){
				return false;
			}
		}
		return true;
	}
	
	private static boolean isUserUniqueEmail(String email){
		
		for (User user : allUsers) {
			if(user.getEmails().equals(email)){
				return false;
			}
		}
		return true;
	}
	
	private static void addUser(String userName, String userLogin, String userPassword, String userRole,String email,String turnEmail,String gameEmail,String adminEmail){
		
		if( userPassword == null){
			
			userPassword = new Integer(Functions.getRandomInt(0,9999999)).toString();
			
		}
		
		getTempList();
		String userString = userName + "\t" + userLogin + "\t" + userPassword + "\t" + userRole + "\t" + email + "\t" + turnEmail + "\t" + gameEmail + "\t" + adminEmail;
		User newUser = new User(userString);
		allTempUsers.add(newUser);
		saveTempUsers();
		// email the new user
		MailHandler.sendNewPlayerMessage(newUser);
	}
	
	public static String activateUser(String userLogin, String userPassword,  String userPassword2){
		String message = "";
		
		if (userPassword == null){
			message = "Password is required";
		}else
		if (userPassword.equals("")){
			message = "Password can not be empty";
		}else
		if (userPassword2 == null){
			message = "Repeat password is required";
		}else
		if (userPassword2.equals("")){
			message = "Repeat password can not be empty";
		}else
		if (!userPassword.equals(userPassword2)){
			message = "Password does not match Repeat password";
		}else{
			// get user from tempUser, set the password, remove tempUser, save the user. 
			getTempList();
			User theUser = findTempUser(userLogin);
			theUser.setPassword(userPassword);
			deleteTempUser(userLogin);
			
			
			getList();
			allUsers.add(theUser);
			saveUsers();
			
			message = "ok";
		}
		
	
		return message;
	}

	public static String addUser(String userName, String userLogin, String userRole,String email,String turnEmail,String gameEmail,String adminEmail,boolean rulesOk){
		String message = "";
		getList();
		// check all fields
		if (userName == null){
			message = "Public name is required";
		}else
		if (userName.equals("")){
			message = "Public name is required";
		}else
		if (userName.length() > 20){
			message = "Public name can not be longar than 20 characters";
		}else
		if(!isUserUniqueName(userName) ){
			message = "Public name is already in use.";
		}else
		if (userLogin == null){
			message = "Login is required";
		}else
		if (userLogin.equals("")){
			message = "Login is required";
		}else
		if (userLogin.length() > 15){
			message = "Login can not be longar than 15 characters";
		}else
		if(!isUserUniqueLogin(userLogin) ){
			message = "Login is already in use.";
		}else
		if (email == null){
			message = "Email is required";
		}else
		if (email.equals("")){
			message = "Email is required";
		}else
		if(!isUserUniqueEmail(email) ){
			message = "Email is already in use.";
		}else
		if (!rulesOk){
			message = "You must read and acknowledge that you understand the rules about copyright and sharing";
		}else{ 
			// check that there is no user already with the same login
			User foundUser = findUser(userLogin);
			if (foundUser == null){
				// ok, create new user
				String tmpTurnEmail = "";
				if ((turnEmail != null) && (turnEmail.equals("checked"))){
					tmpTurnEmail = "true";
				}
				String tmpGameEmail = "";
				if ((gameEmail != null) && (gameEmail.equals("checked"))){
					tmpGameEmail = "true";
				}
				String tmpAdminEmail = "";
				if ((adminEmail != null) && (adminEmail.equals("checked"))){
					tmpAdminEmail = "true";
				}
				
				addUser(userName,userLogin,null,userRole,email,tmpTurnEmail,tmpGameEmail,tmpAdminEmail);
				message = "ok";
			}else{
				message = "Login \"" + userLogin + "\" is already in use by another player. Please try again with another login.";
			}
		}
		return message;
	}

	public static void deleteUser(String userName){
		getList();
		User tmpUser = findUser(userName);
		allUsers.remove(tmpUser);
		saveUsers();
	}
	
	private static void deleteTempUser(String userName){
		getTempList();
		User tmpUser = findTempUser(userName);
		allTempUsers.remove(tmpUser);
		saveTempUsers();
	}

	public static void saveUser(String userName, String userLogin, String userPassword, String userRole,String email,String turnEmail,String gameEmail,String adminEmail,ServerHandler sh){
		getList();
		User tmpUser = findUser(userLogin);
		tmpUser.setAllFields(userName,userLogin,userPassword,userRole,email,turnEmail,gameEmail,adminEmail);
		saveUsers();
		sh.newPlayerPassword(userLogin,userPassword);
	}

	public static String getRemovableUsers(){
		String remUserStr = "";
		getList();
		int counter = 0;
		for (User aUser : allUsers) {
			if (counter <= 1){
				counter++;
			}else{
				remUserStr = remUserStr + "<option value=\"" + aUser.getLogin() + "\">" + aUser.getName() + " / " + aUser.getLogin() + "</option>\n";
			}
		}
		return remUserStr;
	}
	
	public static String getEditableUserList(){
		String userListStr = "";
		getList();
		for (User aUser : allUsers) {
			userListStr = userListStr + "<a href=\"user_show.jsp?login=" + aUser.getLogin() + "\">" + aUser.getName() + " / " + aUser.getLogin() + "</a><br>\n";
		}
		return userListStr;
	}

	public static String getEditableUserListNO(){
		String userListStr = "<table class='ListTable' cellspacing='0' cellpadding='0' width='100%'>";
		userListStr = userListStr + "<tr class='ListheaderRow' height='16'><td class='ListHeader' WIDTH='10'></td><td class='ListHeader' WIDTH='150'><div class='SolidText'>User Name</div></td><td class='ListHeader' nowrap><div class='SolidText'>User ID:&nbsp;&nbsp;&nbsp;</div></td></tr>\n";
		
		getList();
		String RowName = "";
		int i =0;
		
		for (Iterator<User> iter = allUsers.iterator(); iter.hasNext();) {
			i= i+1;
			RowName = i + "UserListRow";
			
			User aUser = iter.next();
			userListStr = userListStr + "<tr class='ListTextRow' style='height:21px' valign='middle'  onMouseOver=\"TranparentRow('" + RowName + "',3,1);\" onMouseOut=\"TranparentRow('" + RowName + "',3,0);\"  onclick=\"location.href='Master.jsp?action=user_show&login=" + aUser.getLogin() + "'\"  ><td class='ListText' id='" + RowName + "1'>&nbsp;</td><td class='ListText' id='" + RowName + "2'><div class='SolidText'>" + aUser.getName() + "</div></td><td class='ListText' id='" + RowName + "3'><div class='SolidText'>" + aUser.getLogin() + "</a></div></td></tr>\n";
		}
		userListStr = userListStr + "</table>";
		return userListStr;
	}

	
	/*
	public static String getEditableUserList(){
		String userListStr = "";
		getList();
		int counter = 0;
		for (Iterator iter = allUsers.iterator(); iter.hasNext();) {
			User aUser = (User) iter.next();
			if (counter <= 1){
				counter++; // cannot edit administrator & guest
			}else{
				userListStr = userListStr + "<a href=\"user_show.jsp?login=" + aUser.getLogin() + "\">" + aUser.getName() + " / " + aUser.getLogin() + "</a><br>\n";
			}
		}
		return userListStr;
	}
*/
	public static void newPassword(String login, String newPassword, ServerHandler sh){
		getList();
		User theUser = findUser(login);
		theUser.setPassword(newPassword);
		saveUsers();
		sh.newPlayerPassword(login,newPassword);
	}

	private static void saveUsers(){
		String basePath = PropertiesHandler.getProperty("basepath");
		String completePath = basePath + "WEB-INF\\classes\\users.properties";
		File usersFile = new File(completePath);
		try{
			FileWriter fw = new FileWriter(usersFile);
			PrintWriter pw = new PrintWriter(fw);
			pw.println("# File created " + (new Date()));
//			int index = -2;
			int index = -1;
			int counter = 0;
			for (User aUser : allUsers) {
				if (index < 0){
					index++;
				}else{
					pw.println(aUser.getSaveString(counter));
					counter++;
				}
			}
			pw.close();
			System.out.println("Sparningen lyckades");
		}
		catch(IOException ioe){
			Logger.severe("Error while saving users");
			ioe.printStackTrace();
		}

	}
	
	private static void saveTempUsers(){
		String basePath = PropertiesHandler.getProperty("basepath");
		String completePath = basePath + "WEB-INF\\classes\\tempUsers.properties";
		File usersFile = new File(completePath);
		try{
			FileWriter fw = new FileWriter(usersFile);
			PrintWriter pw = new PrintWriter(fw);
			pw.println("# File created " + (new Date()));
			int index = -1;
			int counter = 0;
			for (User aUser : allTempUsers) {
				if (index < 0){
					index++;
				}else{
					pw.println(aUser.getSaveString(counter));
					counter++;
				}
			}
			pw.close();
			System.out.println("Sparningen lyckades");
		}
		catch(IOException ioe){
			Logger.severe("Error while saving tempUsers");
			ioe.printStackTrace();
		}

	}
	
	private static String getUserProperty(String key){
		String retVal = "";
		properties = UserHandler.getInstance(DEFAULT_PROPERTIES_NAME);
		String tmpValue = properties.getProperty(key);
		if (tmpValue != null){
			retVal = tmpValue;
		}else{
			retVal = "";
		}
		return retVal;
	}
	
	private static String getTempUserProperty(String key){
		String retVal = "";
		properties = UserHandler.getInstance(TEMP_PROPERTIES_NAME);
		String tmpValue = properties.getProperty(key);
		if (tmpValue != null){
			retVal = tmpValue;
		}else{
			retVal = "";
		}
		return retVal;
	}
	
	private static List<User> getList(){
		if (allUsers == null){
			allUsers = new ArrayList<User>();
			// add default users
			guestUser = new User("Anonymous\tguest\tguest\tguest");
			allUsers.add(guestUser);
//			allUsers.add(new User("Administrator\tadmin\toverlord\tadmin"));
//			properties = UserHandler.getInstance();
			int index = 0;
			boolean continueLoop = true;
			while (continueLoop){
				String tmpStr = UserHandler.getUserProperty("user" + index);
				if (!tmpStr.equals("")){
//				if ((tmpStr != null) && (!tmpStr.equals(""))){
					Logger.finest("adding user: " + tmpStr);
					User tmpUser = new User(tmpStr);
					allUsers.add(tmpUser);
					index++;
				}else{
					continueLoop = false;
				}
			}
		}
		return allUsers;
	}
	
	private static List<User> getTempList(){
		if (allTempUsers == null){
			allTempUsers = new ArrayList<User>();
			int index = 0;
			boolean continueLoop = true;
			while (continueLoop){
				String tmpStr = UserHandler.getTempUserProperty("user" + index);
				if (!tmpStr.equals("")){
					User tmpUser = new User(tmpStr);
					allTempUsers.add(tmpUser);
					index++;
				}else{
					continueLoop = false;
				}
			}
		}
		return allTempUsers;
	}
	
	private static Properties getInstance(String name){
		if (properties == null){
			try {
				properties = loadParams(name);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return properties;
	}

	private static Properties loadParams(String file) throws IOException {
		// Loads a ResourceBundle and creates Properties from it
/*
		Properties prop = new Properties();
		ResourceBundle bundle = ResourceBundle.getBundle(file);
		Enumeration enum = bundle.getKeys();
		String key = null;

		while (enum.hasMoreElements()) {
			key = (String) enum.nextElement();
			prop.put(key, bundle.getObject(key));
		}
*/
		Properties prop = PropertiesHandler.getInstance(file);
		return prop;
	}
	
	/**
	 * 
	 * @param type as defined in User, Example: User.WANT_EMAIL_ADMIN
	 * @return
	 */
	public static List<User> getUsers(int type){
		List<User> allUsers = getList();
		List<User> foundUsers = new LinkedList<User>();
		for (User aUser : allUsers) {
			if (aUser.getRecieveMail(type)){
				foundUsers.add(aUser);
			}
		}
		return foundUsers;
	}

	public static List<User> getAdminUsers(){
		List<User> allUsers = getList();
		List<User> foundUsers = new LinkedList<User>();
		for (User aUser : allUsers) {
			if (aUser.isAdmin()){
				foundUsers.add(aUser);
			}
		}
		return foundUsers;
	}
	
	public static int getUserNr(){
		int nr = 0;
		if (allUsers != null){
			nr = allUsers.size();
		}
		return nr;
	}

}
