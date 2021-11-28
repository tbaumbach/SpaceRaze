
	function TextToLayer(sText,oLayer)
	{
		//document.getElementById(oLayer).innerHTML = sText;
	}

	function OnMouseOverNOut_Image(oControl,sImageName,sText,oLayer)
	{
		TextToLayer(sText,oLayer);
		oControl.src = sImageName;
	}

	function OnMouseOverNOut_Class(oControl,sClassName)
	{
		TextToLayer("Closing Window",'GuideArea')
		oControl.className = sClassName;
	}

	function OnMouseOverNOut_Color(oControl,sColor)
	{
	
	}

	function ShowHelpLayer(oLayer)
	{
		document.getElementById(oLayer).style.display = '';
	}

	function HideHelpLayer(oLayer)
	{	
		document.getElementById(oLayer).style.display = 'none';
	}	
	function OnMouseOver(Control)
	{
		Control.className = "OnMouseOver";	
		//document.form1.PageAction.innerHTML = 'Click on a Gameworld to see detailed information!';	
	}
	function OnMouseOut(Control)
	{
		Control.className = "TRMain";	
		//document.form1.PageAction.innerHTML = '';
	}
	function OnMouseOut(Control,Class)
	{
		Control.className = Class;	
		//document.form1.PageAction.innerHTML = '';
	}


