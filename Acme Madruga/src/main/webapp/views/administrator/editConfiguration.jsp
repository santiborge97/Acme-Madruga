<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>


<form:form action="configuration/administrator/edit.do" modelAttribute="configuration">

	<form:hidden path="id" />
	<form:hidden path="version" />
	
	<acme:textbox code="configuration.spamWords" size="100" path="spamWords" obligatory="true"/>

	<acme:textbox code="configuration.banner" size="100" path="banner" obligatory="true"/>

	<acme:textbox code="configuration.positiveWords" size="100" path="positiveWords" obligatory="true"/>
	
	<acme:textbox code="configuration.negativeWords" size="100" path="negativeWords" obligatory="true"/>
	
	<acme:textbox code="configuration.countryCode" path="countryCode" obligatory="true"/>
	
	<acme:textbox code="configuration.finderTime" path="finderTime" obligatory="true"/>
	
	<acme:textbox code="configuration.finderResult" path="finderResult" obligatory="true"/>
	
	<acme:textbox code="configuration.priorities" size="100" path="priorities" obligatory="true"/>
	
	<acme:submit name="save" code="configuration.save" />
	
	<acme:cancel code="configuration.cancel" url="welcome/index.do" />
	
</form:form> 
