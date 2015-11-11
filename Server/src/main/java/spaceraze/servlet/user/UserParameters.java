package spaceraze.servlet.user;

public class UserParameters {
	
	
	String name, login, role, email, turnEmail, gameEmail, adminEmail, password, repeatedPassword;
	boolean rulesOk = Boolean.FALSE;
	
	public UserParameters(){}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTurnEmail() {
		return turnEmail;
	}

	public void setTurnEmail(String turnEmail) {
		this.turnEmail = turnEmail;
	}

	public String getGameEmail() {
		return gameEmail;
	}

	public void setGameEmail(String gameEmail) {
		this.gameEmail = gameEmail;
	}

	public String getAdminEmail() {
		return adminEmail;
	}

	public void setAdminEmail(String adminEmail) {
		this.adminEmail = adminEmail;
	}

	public boolean isRulesOk() {
		return rulesOk;
	}

	public void setRulesOk(boolean rulesOk) {
		this.rulesOk = rulesOk;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRepeatedPassword() {
		return repeatedPassword;
	}

	public void setRepeatedPassword(String repeatedPassword) {
		this.repeatedPassword = repeatedPassword;
	}

	@Override
	public String toString() {
		return "UserParameters [name=" + name + ", login=" + login + ", role=" + role + ", email=" + email
				+ ", turnEmail=" + turnEmail + ", gameEmail=" + gameEmail + ", adminEmail=" + adminEmail + ", password="
				+ password + ", repeatedPassword=" + repeatedPassword + ", rulesOk=" + rulesOk + "]";
	}

	

}
