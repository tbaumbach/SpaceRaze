<%@page import="spaceraze.battlehandler.spacebattle.simulation.BattleSim"%>
<%@page import="spaceraze.servlethelper.handlers.GameWorldHandler"%>
<%@page import="spaceraze.webb.support.world.GameWorldHelper"%>
<%@ page import="spaceraze.world.*"%>
<%@ page import="java.util.*"%>
<!DOCTYPE html>
<html>
<head>
<title>SpaceRaze Battle Simulator</title>
<meta charset="UTF-8">
<link rel="stylesheet" HREF="styles.css" type="text/css">
</head>
<%@ include file="checklogin2.jsp" %>
<%
	//TODO 2020-02-27 Den h�r ska inte vara tillg�nlig f�r vanliga anv�ndare, finns det behov f�r admin?  I s� fall fixa den.

	// maybe perform simulation 
	String todoStr = request.getParameter("todo");
	String gameWorldName = request.getParameter("gameworld");
	GameWorld gameWorld = GameWorldHandler.getGameWorld(gameWorldName);
	List<VIPType> viptypes = gameWorld.getBattleVIPtypes();
	BattleSim bs = null;
	String ships1 = gameWorld.getBattleSimDefaultShips1();
	String ships2 = gameWorld.getBattleSimDefaultShips2();
	int iterations = 100; //200
	int maxShips = 100;
	int sleepTime = 10; // 10
	if (!tmpUser.isAdmin()){
		maxShips = 30;
		iterations = 200;
		sleepTime = 15;
	}		
	if ((todoStr != null) && (todoStr.equals("Start Sim"))){
		ships1 = request.getParameter("ships1");
		ships2 = request.getParameter("ships2");
		//TODO 2020-02-29 Ta bort den h�r sidan helt o h�llet?
		//bs = BattleSim.simulateBattles(ships1,ships2,iterations,maxShips,sleepTime,gameWorldName);
	}
%>

<script>

	function ClearShips(){
		document.forms[0].ships1.value = "";
		document.forms[0].ships2.value = "";
	}

	function init(){
		document.forms[0].elements[0].focus;
	}

	function AddShips(){
	  //Kontrollera f�r vilken sida
	  //Skriv ut Skepp, Ledare, Expert Engineer, Screened, Antal
	  //[4]Stc(AES)

      if (document.forms[0].cboShip.options[document.forms[0].cboShip.selectedIndex].value != ""){
        var strShip = "";
	    var strAddon ="";
	    
	    if(document.forms[0].rdoSide[0].checked == true){
		  if (document.forms[0].ships1.value != ""){
			strShip = ";";
		  }

		  if(document.forms[0].txtSize.value != ""){
			strShip = strShip + "[" + document.forms[0].txtSize.value + "]";
		  }

		  strShip = strShip + document.forms[0].cboShip.options[document.forms[0].cboShip.selectedIndex].value;
		
		  strAddon = "(";

		  if(document.forms[0].chkScreened.checked == true){
			strAddon = strAddon + "s";
		  }

		  if(document.forms[0].txtTech.value != ""){
		    if (strAddon != "("){
			  strAddon = strAddon + ",";
		    }
			strAddon = strAddon + "t:" + document.forms[0].txtTech.value;
		  }
	
<%
	for (int i = 0; i < viptypes.size(); i++){
		VIPType viptype = (VIPType)viptypes.get(i);		
%>	
		  if(document.forms[0].chk<%= viptype.getShortName() %>.checked == true){
		    if (strAddon != "("){
			  strAddon = strAddon + ",";
		    }
			strAddon = strAddon + "<%= viptype.getShortName() %>";
		  }
<%
	}
%>

		  strAddon = strAddon + ")";

		  if (strAddon != "()"){
			strShip = strShip + strAddon;
		  }

		  document.forms[0].ships1.value = document.forms[0].ships1.value + strShip;
	    }else{
		  if (document.forms[0].ships2.value != ""){
			strShip = ";";
		  }

		  if(document.forms[0].txtSize.value != ""){
			strShip = strShip + "[" + document.forms[0].txtSize.value + "]";
		  }

		  strShip = strShip + document.forms[0].cboShip.options[document.forms[0].cboShip.selectedIndex].value;

		  strAddon = "(";

          if(document.forms[0].chkScreened.checked == true){
			strAddon = strAddon + "s";
		  }
	
		  if(document.forms[0].txtTech.value != ""){
		    if (strAddon != "("){
			  strAddon = strAddon + ",";
		    }
			strAddon = strAddon + "t:" + document.forms[0].txtTech.value;
		  }
	
<%
	for (int i = 0; i < viptypes.size(); i++){
		VIPType viptype = (VIPType)viptypes.get(i);		
%>	
		  if(document.forms[0].chk<%= viptype.getShortName() %>.checked == true){
		    if (strAddon != "("){
			  strAddon = strAddon + ",";
		    }
			strAddon = strAddon + "<%= viptype.getShortName() %>";
		  }
<%
	}
%>
		
		  strAddon = strAddon + ")";

		  if (strAddon != "()"){
			strShip = strShip + strAddon; 
		  }
	
		  document.forms[0].ships2.value = document.forms[0].ships2.value + strShip;
	    }	
      }else{
	    alert("Choose a ship type");
      }

	  document.forms[0].txtSize.value = "";
	  document.forms[0].elements[0].focus;
    }
    
</script>

<body background="images/spaze.gif" onload="init();">

<div style="left:130px;width:718px;position: absolute;top: 89px;">
 <div class="Form_Name" style="width:718"><div class="SolidText">SpaceRaze</div></div>
 <div class="Form_Header" style="width:718"><div class="SolidText"><b>SpaceRaze Battle Simulator: <%= gameWorld.getFullName() %></b>&nbsp;</div></div>
 <div class="Form_Text" style="width:718"><div class="SolidText">
						Battle Simulator - Select ships for a battle simulator scenario
		</div></div>
 <div class="Form_Header" style="width:718"><div class="SolidText"><b>Simulator: <%= gameWorld.getFullName() %></b>&nbsp;</div></div>
<div class="Form_Text" style="width:718"><div class="SolidText">

<!-- GameWorld: <%= gameWorldName %> -->
Each simulated battle will be run <%= iterations %> times, and
maximum number of ships in battle is <%= maxShips %>.<br>
<% if (bs != null){%>
<% if (bs.getMessage() != null){%>
<br>
<font color="#FF0000">
Message from BattleSim:<br>
<%= bs.getMessage() %>
</font>
<% } %>
<% } %>
<!-- 585 + 95? -->
<form action="Master.jsp">
  <input type="hidden" name="gameworld" value="<%= gameWorldName %>">
  <input type="hidden" name="action" value="battle_sim">
		<table class="ListTable" style="width:680px">
			<tr>
				<td width="5"><img src="px.gix" width="5" height="1"></td>
				<td>Ship types</td>
				<td width="5"><img src="px.gix" width="5" height="1"></td>
				<td>Side</td>
				<td width="5"><img src="px.gix" width="5" height="1"></td>
				<td>VIPs</td>
				<td width="5"><img src="px.gix" width="5" height="1"></td>
				<td nowrap>Tech Bonus</td>
				<td width="5"><img src="px.gix" width="5" height="1"></td>
				<td nowrap>Screened</td>
				<td width="5"><img src="px.gix" width="5" height="1"></td>
				<td>No of ships</td>
				<td width="5"><img src="px.gix" width="5" height="1"></td>
			</tr>
			<tr>
				<td width="5"><img src="px.gix" width="5" height="1"></td>
				<td rowspan=3 valign=top>
					<SELECT id="cboShip" name="cboShip" tabIndex=1 style="width=150px">
						<OPTION selected value="">Select Ship</OPTION>
							<%= new GameWorldHelper(gameWorld).getSpaceshipTypeOptionsHTML() %>
					</SELECT>
				</td>
				<td width="5"><img src="px.gix" width="5" height="1"></td>
				<td><INPUT id="rdoSide" checked type="radio" value="1" name="rdoSide" tabIndex=2>A</td>
				<td width="5"><img src="px.gix" width="5" height="1"></td>
				<td nowrap>
<%
	if (viptypes.size() >= 1){
%>
				<INPUT id="chk<%= ((VIPType)viptypes.get(0)).getShortName() %>" type="checkbox" name="chk<%= ((VIPType)viptypes.get(0)).getShortName() %>" value="1" tabIndex=4><%= ((VIPType)viptypes.get(0)).getName() %>(<%= ((VIPType)viptypes.get(0)).getShortName() %>)
<%
	}
%>				
				</td>
				<td width="5"><img src="px.gix" width="5" height="1"></td>
				<td><INPUT id="txtTech" type="text" value="" name="txtTech" tabIndex=4 style="width=70px"></td>
				<td width="5"><img src="px.gix" width="5" height="1"></td>
				<td><INPUT id="chkScreened" type="checkbox" name="chkScreened" value="1" tabIndex=5>Yes</td>
				<td width="5"><img src="px.gix" width="5" height="1"></td>
				<td><INPUT id="txtSize" type="text" value="" name="txtSize" tabIndex=6 style="width=70px"></td>
				<td width="5"><img src="px.gix" width="5" height="1"></td>
			</tr>
			<tr>
				<td width="5"><img src="px.gix" width="5" height="1"></td>
				<td width="5"><img src="px.gix" width="5" height="1"></td>
				<td><INPUT id="rdoSide" type="radio" value="2" name="rdoSide">B</td>
				<td width="5"><img src="px.gix" width="5" height="1"></td>
				<td nowrap>
<%
	if (viptypes.size() >= 2){
%>
				<INPUT id="chk<%= ((VIPType)viptypes.get(1)).getShortName() %>" type="checkbox" name="chk<%= ((VIPType)viptypes.get(1)).getShortName() %>" value="1" tabIndex=4><%= ((VIPType)viptypes.get(1)).getName() %>(<%= ((VIPType)viptypes.get(1)).getShortName() %>)
<%
	}
%>				
				</td>
				<td width="5"><img src="px.gix" width="5" height="1"></td>
				<td></td>
				<td width="5"><img src="px.gix" width="5" height="1"></td>
				<td></td>
				<td width="5"><img src="px.gix" width="5" height="1"></td>
				<td></td>
				<td width="5"><img src="px.gix" width="5" height="1"></td>
			</tr>
			<tr>
				<td width="5"><img src="px.gix" width="5" height="1"></td>
				<td width="5"><img src="px.gix" width="5" height="1"></td>
				<td></td>
				<td width="5"><img src="px.gix" width="5" height="1"></td>
				<td nowrap>
<%
	if (viptypes.size() >= 3){
%>
				<INPUT id="chk<%= ((VIPType)viptypes.get(2)).getShortName() %>" type="checkbox" name="chk<%= ((VIPType)viptypes.get(2)).getShortName() %>" value="1" tabIndex=4><%= ((VIPType)viptypes.get(2)).getName() %>(<%= ((VIPType)viptypes.get(2)).getShortName() %>)
<%
	}
%>				
				</td>
				<td width="5"><img src="px.gix" width="5" height="1"></td>
				<td></td>
				<td width="5"><img src="px.gix" width="5" height="1"></td>
				<td></td>
				<td width="5"><img src="px.gix" width="5" height="1"></td>
				<td align="right"><INPUT  tabIndex=7 id="Button2" type="button" value="Add Ship(s)" name="Button1" onclick="javascript:AddShips();"></td>
				<td width="5"><img src="px.gix" width="5" height="1"></td>
			</tr>

<%
	if (viptypes.size() >= 4){
	  for (int i = 3; i < viptypes.size(); i++){
%>
			<tr>
				<td width="5"><img src="px.gix" width="5" height="1"></td>
				<td width="5"><img src="px.gix" width="5" height="1"></td>
				<td></td>
				<td width="5"><img src="px.gix" width="5" height="1"></td>
				<td width="5"><img src="px.gix" width="5" height="1"></td>
				<td nowrap>
					<INPUT id="chk<%= ((VIPType)viptypes.get(i)).getShortName() %>" type="checkbox" name="chk<%= ((VIPType)viptypes.get(i)).getShortName() %>" value="1" tabIndex=4><%= ((VIPType)viptypes.get(i)).getName() %>(<%= ((VIPType)viptypes.get(i)).getShortName() %>)
				</td>
				<td></td>
				<td width="5"><img src="px.gix" width="5" height="1"></td>
				<td></td>
				<td width="5"><img src="px.gix" width="5" height="1"></td>
				<td align="right"></td>
				<td width="5"><img src="px.gix" width="5" height="1"></td>
			</tr>
<%
	  }
	}
%>				

			<tr>
				<td width="5"><img src="px.gix" width="5" height="1"></td>  
				<td width="140"><img src="px.gix" width="140" height="1"></td>
				<td width="10"><img src="px.gix" width="10" height="1"></td>
				<td width="60"><img src="px.gix" width="60" height="1"></td>
				<td width="5"><img src="px.gix" width="5" height="1"></td>
				<td width="90"><img src="px.gix" width="90" height="1"></td>
				<td width="5"><img src="px.gix" width="10" height="1"></td>
				<td width="90"><img src="px.gix" width="90" height="1"></td>
				<td width="5"><img src="px.gix" width="10" height="1"></td>
				<td width="90"><img src="px.gix" width="90" height="1"></td>
				<td width="5"><img src="px.gix" width="5" height="1"></td>
				<td width="100"><img src="px.gix" width="100" height="1"></td>
				<td width="5"><img src="px.gix" width="5" height="1"></td>
			</tr>
		</table>
<br>
<table style="width:663px">
<tr>
	<td align=left>
Ships in Side A
<% if ((bs != null) && (bs.getMessage() == null)){%>
	<font color="#00FF00">&nbsp;&nbsp;&nbsp;[wins: <%= bs.getTf1wins() %> %] - [cost (build/supply): <!--%= bs.getTf1CostBuy() %-->/<!-- %= bs.getTf1CostSupply() %-->]</font>
<% } %>
<br>
<textarea name="ships1" cols="70" rows="3"><%= ships1 %></textarea><p>
	</td>
	<td></td>
	<td align=left>
			<INPUT type=button onclick="ClearShips();" value="Clear All" name=todo>
	</td>
</tr>

<tr>
	<td align=left>
Ships in Side B
<% if ((bs != null) && (bs.getMessage() == null)){%>
	<font color="#00FF00">&nbsp;&nbsp;&nbsp;[wins: <%= bs.getTf2wins() %> %] - [cost (build/supply): <!-- %= bs.getTf2CostBuy() %-->/<!-- %= bs.getTf2CostSupply() %-->]</font>
<% } %>
<br>
<textarea name="ships2" cols="70" rows="3"><%= ships2 %></textarea><p>
	</td>
	<td nowrap>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
	<td align=left>
		<INPUT type=submit value="Start Sim" name=todo> 
	</td>
</tr>
</table>
<p>
Tip 1: you can type the ships of side A & B manually in the textareas if you wish<br>
Tip 2: use long or short shiptype names from the spaceship list for the selected GameWorld.<br>
Tip 3: any blank in a shiptype name (in the beginning, end or anywhere except inside a (long) name) will cause error.<br> 
Tip 4: always use semicolon as separator between ships<br>
Tip 5: use [x] as prefix to set number of a shiptype, where x is an integer<br>
Tip 6: use (s,t:y,x1,x2...) as suffix where "s" for screened, x1 etc is short VIPtype names and "t:" followed by a tech bonus. Example StC(s,t:10,adm,gun) is a screened Strike Cruiser built with a tech bonus of 10% and has 2 VIPs onboard (with short names "adm" and "gun").<br>

</form>
</div></div>
<div class="Form_Header" ALIGN="RIGHT" style="width:718"><div class="SolidText"></div>
			<div class="SolidText"></div>
		</div>
		<div class="List_End"></div>	
</div>

</body>
</html>