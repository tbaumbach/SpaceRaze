package sr.webb;


import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import spaceraze.user.User;
import sr.webb.users.UserHandler;

/**
 * This class is used by jsp pages to get the correct user object.
 * Uses cookies.
 * @author WMPABOD
 *
 */
public class CheckLogin {
	private static final String guestLogin = "guest";

	public static User getUser(HttpSession session, HttpServletRequest request, HttpServletResponse response){
		System.out.println("checklogin2.jsp");
		User tmpUser = null;
		Cookie[] cookies = request.getCookies();
		Cookie userLoginCookie = findLogin("userlogin",cookies);
		if (userLoginCookie != null){
				String userLogin = userLoginCookie.getValue();
				String userExists = UserHandler.isUser(userLogin);
				if (userExists.equals("yes")) {
					tmpUser = UserHandler.getUser(userLogin);
				}
		}else{
			// user not logged in, return guest User instance
			tmpUser = UserHandler.getUser(guestLogin);
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
