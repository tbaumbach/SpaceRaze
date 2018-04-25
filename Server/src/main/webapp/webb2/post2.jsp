<%@ page import="sr.webb.*"%>
<%@ page import="sr.server.*"%>
<%@ page import="sr.webb.users.*"%>
<%@ page import="sr.webb.guides.*"%>
<%@ page import="java.io.*"%>
<%@ page import="java.util.*"%>

<form id="formNews" name="formNews" method="post" action="post.jsp"  ENCTYPE="multipart/form-data">
<div style="left: 132px;width: 718px;position: absolute;top: 90px;">	
	<div class="Form_name" style="width:718"><div class="SolidText">SpaceRaze - Guides</div></div>
	<div class="Form_Header" style="width:718"><div class="SolidText"><b>Edit - </b></div></div>
	<div class="Form_Text"  style="width:718"><div class="SolidText">

Title: <input type="text" class="InputText" id="title" name="title" value="">
<br><br>
Article content:<br>
<textarea name="content" class="InputText" id="content" cols="100" rows="20"></textarea>
<br><br>
Posted by: <input type="text" class="InputText" id="creator" name="creator" value="">
<br>
<p>


<!--input id="todo" name="todo" type="submit" value="Save"-->
<!--input id="todo" name="todo" type="button" onclick="submit();" value="Save"-->

	</div></div>
	<div class="Form_header" ALIGN=RIGHT>
		<div class="SolidText">
			<A href="#" id="nas" name="nas" onclick='document.forms["formNews"].submit();'><IMG onmouseout="OnMouseOverNOut_Image(this,'images/btn_save.jpg','&nbsp;','GuideArea');" onmouseover="OnMouseOverNOut_Image(this,'images/btn_Over_save.jpg','Save: Save new or update guide.','GuideArea');" alt="Save" hspace=0 src="images/btn_save.jpg" vspace=0 border=0></A>
	
	<A href="Master.jsp?action=guides_list" id="cas" name="cas"><IMG onmouseout="OnMouseOverNOut_Image(this,'images/btn_Red_cancel.jpg','&nbsp;','GuideArea');" onmouseover="OnMouseOverNOut_Image(this,'images/btn_Red_Over_cancel.jpg','Cancel, return to guide list.','GuideArea');" alt="Save" hspace=0 src="images/btn_Red_cancel.jpg" vspace=0 border=0></A>
	<br><br><br>
	
<input type="submit" value="Submit">
	<br><br><br>
			
	</div></div>
	<div class="List_End"> </div>		
	</div>
	
</form>
