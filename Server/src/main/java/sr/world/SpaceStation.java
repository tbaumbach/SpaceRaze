package sr.world;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
//TODO (Tobbe) ta bort dennna klass.
public class SpaceStation implements Serializable{
	private static final long serialVersionUID = 1L;
	private int openProdBonus,closedProdBonus,techBonus;
	private boolean spaceport;
	
	public SpaceStation(){
		
	}
	
	public SpaceStation(SpaceStation spaceStation){
		openProdBonus = spaceStation.getOpenProdBonus();
		closedProdBonus = spaceStation.getClosedProdBonus();
		techBonus = spaceStation.getTechBonus();
		spaceport = spaceStation.isSpaceport();
	}
	
	  public static String getHTMLHeaderRow(){
		  StringBuffer sb = new StringBuffer();
		  sb.append("<tr>");
		  sb.append("<td>Open<br>Planet<br>Bonus</td>");
		  sb.append("<td>Closed<br>Planet<br>Bonus</td>");
		  sb.append("<td>Tech<br>Bonus</td>");
		  sb.append("<td>Spaceport");
		  sb.append("</tr>\n");
		  return sb.toString();
	  }

	  public String getHTMLTableRow(){
		  StringBuffer sb = new StringBuffer();
		  sb.append("<tr>");
		  sb.append("<td>" + openProdBonus + "</td>");
		  sb.append("<td>" + closedProdBonus + "</td>");
		  sb.append("<td>" + techBonus + "</td>");
		  sb.append("<td>" + spaceport + "</td>");
		  sb.append("</tr>\n");
		  return sb.toString();
	  }

    public int getClosedProdBonus() {
		return closedProdBonus;
	}
	
	public void setClosedProdBonus(int closedProdBonus) {
		this.closedProdBonus = closedProdBonus;
	}
	
	public int getOpenProdBonus() {
		return openProdBonus;
	}
	
	public void setOpenProdBonus(int openProdBonus) {
		this.openProdBonus = openProdBonus;
	}
	
	public boolean isSpaceport() {
		return spaceport;
	}
	
	public void setSpaceport(boolean spaceport) {
		this.spaceport = spaceport;
	}
	
	public int getTechBonus() {
		return techBonus;
	}
	
	public void setTechBonus(int techBonus) {
		this.techBonus = techBonus;
	}

	public List<String> getAbilitiesStrings(){
		List<String> strings = new LinkedList<String>();
		if (techBonus > 0){
			strings.add("Research center: +" + techBonus + "% tech bonus");
		}
		if (openProdBonus > 0){
			strings.add("Merchant station: +" + openProdBonus + " production on open planet");
		}
		if (closedProdBonus > 0){
			strings.add("Supply station: +" + closedProdBonus + " production on closed planet");
		}
		if (spaceport){
			strings.add("Spaceport: planets connected by spaceports gets short range");
		}
		return strings;
	}
	
	public String toString(){
		return "SpaceStation (" + openProdBonus + "/" + closedProdBonus + "/" + techBonus + "/" + spaceport + ")";
	}
}
