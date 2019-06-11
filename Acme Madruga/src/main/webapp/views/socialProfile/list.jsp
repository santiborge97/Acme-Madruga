<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<display:table pagesize="5" name="socialProfiles" id="row" 
requestURI="${requestURI }" class="displaytag">

	<acme:column property="nick" titleKey="socialProfile.nick" value= "${row.nick}: "/>
	
	<acme:column property="socialName" titleKey="socialProfile.socialName" value= "${row.socialName}: "/>
	
	
	<acme:column property="link" titleKey="socialProfile.link" value= "${row.link}: "/>
	
	<acme:url href="socialProfile/administrator,brotherhood,member/edit.do?socialProfileId=${row.id}" code="socialProfile.edit"/>
	
	<acme:url href="socialProfile/administrator,brotherhood,member/display.do?socialProfileId=${row.id}" code="socialProfile.display"/>
	
</display:table>

	<acme:button name="create" code="socialProfile.create" onclick="javascript: relativeRedir('socialProfile/administrator,brotherhood,member/create.do');"/>
	
	<acme:button name="back" code="socialProfile.back" onclick="javascript: relativeRedir('profile/displayPrincipal.do');"/>
<br />
