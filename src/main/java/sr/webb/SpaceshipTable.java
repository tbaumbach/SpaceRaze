/*
 * Created on 2005-jan-01
 */
package sr.webb;

/**
 * @author WMPABOD
 *
 * Support class for the spaceships.jsp page
 */
public class SpaceshipTable {
	
	public static String getSpaceship(String fileName, String name, String abbreviation, int strength, String range, int build, int support, String faction, boolean troops, int bombardment, int squadrons, int supportSquadrons, boolean hyperblock){
		StringBuffer retStr = new StringBuffer("<tr>");
		retStr.append("<td>"); 
		retStr.append("<img src=\"../applet/images/" + fileName + ".gif\">");	
		retStr.append("</td>");
		retStr.append("<td>" + name + "<br>(" + abbreviation + ")</td>");
		retStr.append("<td>"); 
		for (int i = 0; i < strength; i++) {
			retStr.append("<img src=\"images/ybar.gif\">");	
		}
		retStr.append("</td>");
		retStr.append("<td>" + range + "</td>");
		retStr.append("<td align=\"center\">" + build + "/" + support + "</td>");
		retStr.append("<td>");
		if (faction.equalsIgnoreCase("Rebels")){
			retStr.append("<font color=\"#00FF00\">");
		}else
		if (faction.equalsIgnoreCase("League")){
			retStr.append("<font color=\"#0000FF\">");
		}else
		if (faction.equalsIgnoreCase("Empire")){
			retStr.append("<font color=\"#FF0000\">");
		}
		retStr.append(faction);
		if (!faction.equalsIgnoreCase("All")){
			retStr.append("</font>");
		}
		retStr.append("</td>");
		retStr.append("<td>");
		if (troops){
			retStr.append("<img src=\"images/troops.gif\">");
		}
		for (int i = 0; i < bombardment; i++){
			retStr.append("<img src=\"images/bombardment.gif\">");
		}
		for (int i = 0; i < squadrons; i++){
			String filename = "";
			if (faction.equalsIgnoreCase("Rebels")){
				filename = "x-wing";
			}else
			if (faction.equalsIgnoreCase("League")){
				filename = "z-95";
			}else
			if (faction.equalsIgnoreCase("Empire")){
				filename = "tie";
			}else{ // neutral
				filename = "z-95";
			}
			retStr.append("<img src=\"images/" + filename + ".gif\">");
		}
		for (int i = 0; i < supportSquadrons; i++){
			String filename = "";
			if (faction.equalsIgnoreCase("Rebels")){
				filename = "x-wings";
			}else
			if (faction.equalsIgnoreCase("League")){
				filename = "z-95s";
			}else
			if (faction.equalsIgnoreCase("Empire")){
				filename = "ties";
			}else{ // neutral
				filename = "z-95s";
			}
			retStr.append("<img src=\"images/" + filename + ".gif\">");
		}
		if (hyperblock){
			retStr.append("<img src=\"images/hyperblock.gif\">");
		}
		retStr.append("</td>");
		retStr.append("</tr>");
		return retStr.toString();
	}

}
