<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<form:form action="float/brotherhood/edit.do" modelAttribute="floatt">
	
	<form:hidden path="id" />
	<form:hidden path="version" />
	
	
	<acme:textbox code="float.title" path="title" obligatory="true"/>

	<acme:textarea code="float.description" path="description" obligatory="true"/>
	
    <acme:textbox code="float.pictures" size="100" pattern="^http(s*)://(?:[a-zA-Z0-9-]+[\\.\\:]{0,1})+([a-zA-Z/]+)*(,http(s*)://(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]+)*$" placeholder="http://____.___,https://___.___,..." path="pictures"/>

	
	<acme:submit name="save" code="float.save" />
	
	<acme:cancel code="float.cancel" url="welcome/index.do" />
	
	<jstl:if test="${floatt.id != 0}">
	<acme:submit name="delete" code="float.delete" />
	</jstl:if>	


</form:form>    