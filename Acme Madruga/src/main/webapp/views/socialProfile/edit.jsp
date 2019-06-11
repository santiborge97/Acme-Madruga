<%--
 * action-1.jsp
 *
 * Copyright (C) 2018 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<form:form modelAttribute="socialProfile" action="socialProfile/administrator,brotherhood,member/edit.do">

	<form:hidden path="id" />
	<form:hidden path="version" />
	
	<acme:textbox path="nick" code="socialProfile.nick" obligatory="true"/>
	
	<acme:textbox path="socialName" code="socialProfile.socialName" obligatory="true"/>
	
	<acme:textbox path="link" code="socialProfile.link" size="50" obligatory="true" placeholder="http(s)://"/>
	
	<acme:submit name="save" code="socialProfile.save"/>
	
	<acme:cancel code="socialProfile.cancel" url="socialProfile/administrator,brotherhood,member/list.do"/>
 
	<jstl:if test="${socialProfile.id != 0}">
	<acme:submit name="delete" code="socialProfile.delete" />
	</jstl:if>

</form:form>