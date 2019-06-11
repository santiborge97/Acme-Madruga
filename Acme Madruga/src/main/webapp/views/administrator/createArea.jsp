<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<form:form action="area/administrator/edit.do" modelAttribute="area">
	
	<form:hidden path="id"/>
	<form:hidden path="version"/>
	
	<acme:textbox code="area.name" path="name" obligatory="true"/>
	
	<acme:textbox code="area.pictures" size="100" pattern="^http(s*)://(?:[a-zA-Z0-9-]+[\\.\\:]{0,1})+([a-zA-Z/]+)*(,http(s*)://(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]+)*$" placeholder="http://____.___,https://___.___,..." path="pictures" obligatory="true"/>
	
	<jstl:if test="${area.id != 0 && empty area.brotherhoods}">
	<acme:submit name="save" code="save" />
	<acme:submit name="delete" code="delete" />
	</jstl:if>
	<jstl:if test="${area.id == 0}">
	<acme:submit name="save" code="create" />
	</jstl:if>
	
	
	<acme:cancel code="cancel" url="welcome/index.do" />
	

</form:form>  
	