package sr.client;

/**
 * <p>Title: SpaceRaze 3.0</p>
 * <p>Description: SpaceRaze with Swing GUI</p>
 * @author Paul Bodin
 * @version 3.0
 */

import java.awt.Color;
import java.awt.Font;

import sr.enums.DiplomacyLevel;

public class StyleGuide {
//  public static final Color colorRebel = new Color(0,255,0);
//  public static final Color colorEmpire = new Color(255,0,0);
//  public static final Color colorLeague = new Color(0,0,255);
  public static final Color colorMapEditorGrid = new Color(0,255,0);
  public static final Color colorNeutral = new Color(255,191,0); // Light Orange
  public static final Color colorNeutralWhite = new Color(250,250,200); // White
  public static final Color colorBackground = new Color(0,0,0); // black
  
  public static final Color colorNAVBackground1 = new Color(227,189,35); // black
  public static final Color colorNAVForeground1 = new Color(255,255,255); // black
  
  public static final Color colorNAVBackground2 = new Color(12,51,105); // black
  public static final Color colorNAVForeground2 = new Color(172,172,222); // black
  public static final Color colorNAVBackground3 = new Color(12,51,105); // black
  public static final Color colorNAVForeground3 = new Color(172,172,222); // black
  public static final Color colorNAVBackground4 = new Color(12,51,105); // black
  public static final Color colorNAVForeground4 = new Color(172,172,222); // black
  public static final Color colorNAVBackground5 = new Color(12,51,105); // black
  public static final Color colorNAVForeground5 = new Color(172,172,222); // black
  public static final Color colorNAVBackground6 = new Color(12,51,105); // black
  public static final Color colorNAVForeground6 = new Color(172,172,222); // black
  public static final Color colorNAVBackground7 = new Color(12,51,105); // black
  public static final Color colorNAVForeground7 = new Color(172,172,222); // black
  public static final Color colorNAVBackground8 = new Color(12,51,105); // black
  public static final Color colorNAVForeground8 = new Color(172,172,222); // black
  public static final Color colorNAVBackground9 = new Color(12,51,105); // black
  public static final Color colorNAVForeground9 = new Color(172,172,222); // black
  
  public static final Color colorNAVBorder = new Color(103,111,157); // black
  public static final Color colorNAV1Border = new Color(255,255,255); // black
  
  public static final Color colorGreenBackground9 = new Color(20,102,4); // black
  public static final Color colorGreenForeground9 = new Color(0,254,35); // black
  public static final Color colorGreenBorder = new Color(0,254,35); // black
  
  
  public static final Color colorMapInsets = new Color(0,127,0); // dark green
  public static final Color colorMapPlanetNames = new Color(41,198,255); // light blue
  public static Color colorCurrent = new Color(255,191,0); // Initiates to Neutral color = Light Orange
  public static final Font buttonFont = new Font("Dialog",1,12);
  public static final Color colorMapLongRange = new Color(0,25,108); // long range dark blue
  public static final Color colorMapShortRange = new Color(160,0,40); // short range dark red
  public static final Color colorMapSpacePortsRange = new Color(255,140,80); // short range due to spaceports orange
  
  public static final Color COLOR_DIPLOMACY_ETERNALWAR = new Color(255,127,0);
  public static final Color COLOR_DIPLOMACY_WAR = new Color(255,0,0);
  public static final Color COLOR_DIPLOMACY_CEASEFIRE = new Color(255,255,255);
  public static final Color COLOR_DIPLOMACY_PEACE = new Color(255,255,0);
  public static final Color COLOR_DIPLOMACY_ALLINNCE = new Color(0,255,0);
  public static final Color COLOR_DIPLOMACY_CONFEDERACY = new Color(0,0,255);
  public static final Color COLOR_DIPLOMACY_VASSALL = new Color(0,255,255);
  public static final Color COLOR_DIPLOMACY_LORD = new Color(191,0,255);
  public static final Color COLOR_DIPLOMACY_DISABLED = new Color(63,63,63);
  
  public static Color getDiplomacyLevelColor(DiplomacyLevel level){
	  Color tmpColor = null;
	  if (level == DiplomacyLevel.ETERNAL_WAR){
		  tmpColor = COLOR_DIPLOMACY_ETERNALWAR;
	  }else
	  if (level == DiplomacyLevel.WAR){
		  tmpColor = COLOR_DIPLOMACY_WAR;
	  }else
	  if (level == DiplomacyLevel.PEACE){
		  tmpColor = COLOR_DIPLOMACY_PEACE;
	  }else
	  if (level == DiplomacyLevel.CEASE_FIRE){
		  tmpColor = COLOR_DIPLOMACY_CEASEFIRE;
	  }else
	  if (level == DiplomacyLevel.ALLIANCE){
		  tmpColor = COLOR_DIPLOMACY_ALLINNCE;
	  }else
	  if (level == DiplomacyLevel.CONFEDERACY){
		  tmpColor = COLOR_DIPLOMACY_CONFEDERACY;
	  }else
	  if (level == DiplomacyLevel.VASSAL){
		  tmpColor = COLOR_DIPLOMACY_VASSALL;
	  }else
	  if (level == DiplomacyLevel.LORD){
		  tmpColor = COLOR_DIPLOMACY_LORD;
	  }
	  return tmpColor;
  }
  
  public static void reset(){
  	colorCurrent = colorNeutral;
  }
}