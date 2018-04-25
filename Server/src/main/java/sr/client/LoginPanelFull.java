package sr.client;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;

import javax.swing.border.LineBorder;

import sr.client.components.CheckBoxPanel;
import sr.client.components.ComboBoxPanel;
import sr.client.components.SRBasePanel;
import sr.client.components.SRButton;
import sr.client.components.SRLabel;
import sr.client.components.SRPasswordField;
import sr.client.components.SRTextField;
import sr.general.Functions;
import sr.general.logging.Logger;
import sr.world.Faction;

public class LoginPanelFull extends SRBasePanel implements ItemListener, ActionListener{
	private static final long serialVersionUID = 1L;
	private SRTextField govenorfield,namefield;
	private SRPasswordField passwordfield;
	private SRButton okbtn;
	private CheckBoxPanel firsttime;
	private SRLabel titleLabel,nameLabel,passwordLabel,factionLabel,govenorLabel;
	private SpaceRazeApplet client;
	private ComboBoxPanel factionsChoice;
	private final int GOV_NAME_MAX_LENGTH = 20;
	private List<Faction> openFactions;

  public LoginPanelFull(SpaceRazeApplet client, String userName, String userPassword, List<Faction> openFactions, boolean randomFaction){
    setLayout(null);
    setBackground(StyleGuide.colorBackground);
    setForeground(StyleGuide.colorNeutral);
    this.client = client;
    this.openFactions = openFactions;

    titleLabel = new SRLabel("SpaceRaze Login");
    titleLabel.setBounds(20,30,130,20);
    titleLabel.setFont(new Font("Helvetica",1,14));
    add(titleLabel);

    nameLabel = new SRLabel("Login:");
    nameLabel.setBounds(20,50,100,20);
    nameLabel.setToolTipText("Type your login you will use every time you log in to this game");
    add(nameLabel);

    namefield = new SRTextField();
    namefield.setBounds(20,70,100,20);
    namefield.setToolTipText("Type your login you will use every time you log in to this game");
    namefield.addActionListener(this);
    add(namefield);

    passwordLabel = new SRLabel("Password:");
    passwordLabel.setBounds(20,95,100,20);
    passwordLabel.setToolTipText("Type the password you will use every time you log in to this game");
    add(passwordLabel);

    passwordfield = new SRPasswordField();
    passwordfield.setBounds(20,115,100,20);
    passwordfield.setToolTipText("Type the password you will use every time you log in to this game");
    add(passwordfield);

    firsttime = new CheckBoxPanel("Create new player");
    firsttime.setBounds(20,145,140,20);
    firsttime.setToolTipText("Check this to create a new Player. To log on an already created player leave it unchecked.");
    firsttime.addItemListener(this);
    add(firsttime);

    govenorLabel = new SRLabel("Governors name:");
    govenorLabel.setBounds(20,165,140,20);
    govenorLabel.setToolTipText("This is the your public name that the other players will see.");
    add(govenorLabel);

    govenorfield = new SRTextField();
    govenorfield.setBounds(20,185,170,20);
    govenorfield.setToolTipText("This is the your public name that the other players will see.");
    govenorfield.setEnabled(false);
    govenorfield.setBackground(StyleGuide.colorCurrent.darker().darker().darker());
    govenorfield.addActionListener(this);
    add(govenorfield);

    factionLabel = new SRLabel("Choose faction:");
    factionLabel.setBounds(20,210,100,20);
    factionLabel.setToolTipText("Decides which side you are on");
    add(factionLabel);

    okbtn = new SRButton("Ok");
    okbtn.setBounds(20,265,170,20);
    okbtn.setToolTipText("Send login data to server");
    okbtn.setBorder(new LineBorder(StyleGuide.colorCurrent));
    okbtn.setForeground(StyleGuide.colorCurrent);
    okbtn.setBackground(StyleGuide.colorBackground);
    //okbtn.addListeners();
    okbtn.addActionListener(this);
    add(okbtn);
    
    factionsChoice = new ComboBoxPanel();
    factionsChoice.addActionListener(this);
    factionsChoice.setBounds(20,230,170,20);
    System.out.println("Random faction: " + randomFaction);
    factionsChoice.addItem("Random");
    if (randomFaction){    	
    	factionsChoice.setEnabled(false);
    }else{
    	for (Faction aFaction : openFactions) {
    		factionsChoice.addItem(aFaction.getName());
    	}
    	// TODO (Tobbe) fixa panelens utsende o aktivera detta igen
    //	client.showFaction(((Faction)openFactions.get(0)).getName());
    }
    

    factionsChoice.setEnabled(false);
    add(factionsChoice);
    
  	if ((userName != null) && (userPassword != null) && (!userName.equals("")) && (!userPassword.equals(""))){
  		// set values
  		namefield.setText(userName);
  		passwordfield.setText(userPassword);
  		// disable components
  		namefield.setEditable(false);
  		passwordfield.setEditable(false);
  		// enable components
        govenorfield.setEnabled(true);
        govenorfield.setBackground(StyleGuide.colorBackground);
        factionsChoice.setEnabled(true);
        // check new player
        firsttime.setSelected(true);
        // hide components
        nameLabel.setVisible(false);
        namefield.setVisible(false);
        passwordLabel.setVisible(false);
		passwordfield.setVisible(false);
		firsttime.setVisible(false);
		// move components
		govenorLabel.setLocation(20,50);
	    govenorfield.setLocation(20,70);
	    factionLabel.setLocation(20,95);
	    factionsChoice.setLocation(20,115);
	    okbtn.setLocation(20,150);
	    // change tiotle text
	    titleLabel.setText("Join Game");
  	}else{
        // check new player
        firsttime.setSelected(true);
        // hide components
		firsttime.setVisible(false);
  		// enable components
        govenorfield.setEnabled(true);
        govenorfield.setBackground(StyleGuide.colorBackground);
        factionsChoice.setEnabled(true);
	    // change tiotle text
	    titleLabel.setText("Create Test Player");
  	}
  }

  public void setNamefieldFocus(){
    namefield.requestFocus();
  }

  public void setGovenorfieldFocus(){
    govenorfield.requestFocus();
  }

  public void itemStateChanged(ItemEvent ie){
    try{
      if (ie.getItemSelectable() instanceof CheckBoxPanel){
      	CheckBoxPanel tempcb = (CheckBoxPanel)ie.getItemSelectable();
        if (tempcb.isSelected()){
          govenorfield.setEnabled(true);
          govenorfield.setBackground(StyleGuide.colorBackground);
          factionsChoice.setEnabled(true);
        }else{
          govenorfield.setEnabled(false);
          govenorfield.setBackground(StyleGuide.colorCurrent.darker().darker().darker());
          factionsChoice.setEnabled(false);
        }
        
      }
    }
    catch(NumberFormatException e){
      System.out.println(e.toString());
    }
  }

  private boolean checkGovName(){
    boolean nameOk = true;
    String title = "";
    String message = "";
	String govName = govenorfield.getText(); 
    if (govName.equalsIgnoreCase("")){
      nameOk = false;
      title = "Governor name error";
      message = "Governor name can not be empty";
    }else
    if (govName.length() > GOV_NAME_MAX_LENGTH){
    	nameOk = false;
    	title = "Governor name error";
    	message = "Governor name can not be more than " + GOV_NAME_MAX_LENGTH + " characters long";
    }else{
    	// check that gov name only contains letters
    	for (int i = 0; i < govName.length(); i++) {
			char c = govName.charAt(i);
			if (!Character.isLetter(c) & !Character.isWhitespace(c) & (c != '-') & (c != '\'')){
				nameOk = false;
			}
		}
        if (!nameOk){
        	title = "Governor name error";
        	message = "Governor name can only contain letters, blanks, - and '";
        }
    }
    
    if (!nameOk){
    	openMessagePopup(title,message);
    }
    return nameOk;
  }

  @SuppressWarnings("deprecation")
public void actionPerformed(ActionEvent e){
  	Logger.fine("actionPerformed called: " + e.getSource());
  	if ((e.getSource() == okbtn) | (e.getSource() == govenorfield)){
  		if (checkGovName() | !firsttime.isSelected()){
  			String message;
  			if (firsttime.isSelected()){
  				message = "newplayer ";
  				if (factionsChoice.getSelectedItem().equals("Random")){
  	  				// get random faction
  	  				int randomFactionIndex = Functions.getRandomInt(0,openFactions.size()-1);
  	  				String factionName = openFactions.get(randomFactionIndex).getName();
  	  				client.setLogin(message + namefield.getText() + " " + passwordfield.getText() + " " + govenorfield.getText().replace(' ','#') + " " + factionName);
  				}else{
  					client.setLogin(message + namefield.getText() + " " + passwordfield.getText() + " " + govenorfield.getText().replace(' ','#') + " " + factionsChoice.getSelectedItem());
  				}
  			}else{
  				message = "oldlogin ";
  				client.setLogin(message + namefield.getText() + " " + passwordfield.getText());
  			}
  			
  			setVisible(false);
  			Logger.fine("LoginPanelFull closing " + message + namefield.getText() + " " + passwordfield.getText());
  		}else{
  			//govenorfield.setText("Fel i namnet...");
  		}
  	}else{
  	// TODO (Tobbe) fixa panelens utsende o aktivera detta igen
  	//	client.showFaction(factionsChoice.getSelectedItem());
  	}
  }

  private void openMessagePopup(String title, String message){
  	Logger.fine("openMessagePopup called: " + message);
    GeneralMessagePopupPanel popup = new GeneralMessagePopupPanel(title,this,message);
	popup.setPopupSize(400,110);
	popup.open(this);
  }

}