<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<form:form action="enrolment/brotherhood/edit.do" modelAttribute="enrolment">
	
	<form:hidden path="id" />
	<form:hidden path="version" />
	
	<jstl:if test="${language == 'en'}">
		<acme:select path="position" items="${positions }" itemLabel="englishName" code="enrolment.position"/>
	</jstl:if>
	<jstl:if test="${language == 'es'}">
		<acme:select path="position" items="${positions }" itemLabel="spanishName" code="enrolment.position" />
	</jstl:if>
	
	<acme:submit name="save" code="enrolment.save" />
	
	<acme:cancel code="enrolment.cancel" url="welcome/index.do" />


</form:form>    