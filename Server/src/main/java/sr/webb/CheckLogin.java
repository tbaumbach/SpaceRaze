package sr.webb;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import sr.webb.users.User;
import sr.webb.users.UserHandler;

/**
 * This class is used by jsp pages to get the correct user object.
 * Uses cookies.
 * @author WMPABOD
 *
 */
public class CheckLogin {
	private static final String guestLogin = "guest";
	private static final String guestPassword = "guest";

	public static User getUser(HttpSession session, HttpServletRequest request, HttpServletResponse response){
		System.out.println("checklogin2.jsp");
		User tmpUser = null;
		Cookie[] cookies = request.getCookies();
		Cookie userLoginCookie = findLogin("userlogin",cookies);
		if (userLoginCookie != null){
			// login cookie found, search for password
			Cookie userPasswordCookie = findLogin("userpassword",cookies);
			if (userPasswordCookie != null){
				String userLogin = userLoginCookie.getValue();
				String userPassword = userPasswordCookie.getValue();
				String userExists = UserHandler.isUser(userLogin,userPassword);
				if (userExists.equals("yes")){
					// user credentials match a user, get the User instance!
					tmpUser = UserHandler.getUser(userLogin,userPassword);
				}else{
					// user credentials does not match any users, login as guest
					tmpUser = UserHandler.getUser(guestLogin,guestPassword);
				}
			}else{
				// password missing, login as guest
				tmpUser = UserHandler.getUser(guestLogin,guestPassword);
			}
		}else{
			// user not logged in, return guest User instance
			tmpUser = UserHandler.getUser(guestLogin,guestPassword);
		}		
		return tmpUser;
	}
	
	private static Cookie findLogin(String cookieName, Cookie[] cookies){
		Cookie found = null;
		int index = 0;
		while((found == null) & (index < cookies.length)){
			Cookie cookie = cookies[index];
			if (cookie.getName().equalsIgnoreCase(cookieName)){
				found = cookie;
			}else{
				index++;
			}
		}
		return found;
	}
}
