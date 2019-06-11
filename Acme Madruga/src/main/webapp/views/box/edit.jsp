<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<form:form action="box/actor/edit.do" modelAttribute="box">
	
	<form:hidden path="id" />
	<form:hidden path="version" />
	
	
	<acme:textbox code="box.name" path="name" obligatory="true"/>

	<acme:select path="parent" items="${boxes}" itemLabel="name" code="box.parent"/>

	<acme:submit name="save" code="box.save" />
	
	<acme:cancel code="box.cancel" url="welcome/index.do" />
	
	<jstl:if test="${box.id != 0}">
	<acme:submit name="delete" code="box.delete" />
	</jstl:if>	


</form:form>    