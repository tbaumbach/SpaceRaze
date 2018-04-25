package sr.notifier;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import sr.server.properties.PropertiesHandler;
import sr.webb.users.User;

/**
 * This class generates new JNLP files for new and old players
 * @author WMPABOD
 *
 */
public class JnlpHandler {
	
	/**
	 * Read the contents of a specified textfile in the notifier folder 
	 * and return the contents as a String.
	 * @param aPlayer
	 * @return
	 */
	private static String getTextFileContents(String fileName){
		StringBuffer sb = new StringBuffer();
		String basepath = PropertiesHandler.getProperty("basepath");
		File aFile = new File(basepath + "\\webb2\\notifier\\" + fileName);
		try {
			FileReader fr = new FileReader(aFile);
			BufferedReader br = new BufferedReader(fr);
			String tmp = br.readLine();
			while (tmp != null){
				sb.append(tmp);
				tmp = br.readLine();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}
	
	/**
	 * Load the template file containing the contents of all new jnlp files
	 * The following string will have to be replaced by the actual contents:
	 * -CODEBASE
	 * -USER (i.e. login string)
	 * -TUNNELPATH
	 * @return jnlp template file contents
	 */
	private static String getJnlpFileTemplate(){
		return getTextFileContents("notifier_jnlp_template.txt");
	}
	
	/**
	 * Create new file contents for a user-specific jnlp file
	 * @param aUser
	 * @return
	 */
	private static String createNewJnlpFileContent(User aUser){
		String tmpContents = getJnlpFileTemplate();
		// get values to insert
		String playerLogin = aUser.getLogin();
		String playerPassword = aUser.getPassword();
		String baseurl = PropertiesHandler.getProperty("baseurl");
		String codebase = baseurl + "/webb2/notifier";
//		String tunnelUrl = baseurl + "/servlet/sr.notifier.NotifierTunnel";
		String tunnelUrl = baseurl + "/";
		tmpContents = tmpContents.replace("CODEBASE",codebase);
		tmpContents = tmpContents.replace("USER",playerLogin);
		tmpContents = tmpContents.replace("PASSWORD",playerPassword);
		tmpContents = tmpContents.replace("TUNNELURL",tunnelUrl);
		return tmpContents;
	}
	
	public static void createJnlpFile(User aUser){
		String fileContent = createNewJnlpFileContent(aUser);
		String basepath = PropertiesHandler.getProperty("basepath");
		File newJnlpFile = new File(basepath + "webb2\\notifier\\userfiles\\notifier_" + aUser.getLogin() + ".jnlp");
		FileWriter fw;
		try {
			fw = new FileWriter(newJnlpFile);
			fw.write(fileContent);
			fw.flush();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static boolean jnlpFileExist(User aUser){
		String basepath = PropertiesHandler.getProperty("basepath");
		File aJnlpFile = new File(basepath + "webb2\\notifier\\userfiles\\notifier_" + aUser.getLogin() + ".jnlp");
		return aJnlpFile.exists();
	}
	
	public static void checkJnlpFile(User aUser){
		if (!jnlpFileExist(aUser)){
			createJnlpFile(aUser);
		}
	}
	
}
