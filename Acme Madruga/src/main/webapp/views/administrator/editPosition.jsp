<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<form:form action="position/administrator/edit.do" modelAttribute="position">
	
	<form:hidden path="id" />
	<form:hidden path="version" />
	
	
	<acme:textbox code="position.englishName" path="englishName" obligatory="true" />
	<acme:textbox code="position.spanishName" path="spanishName" obligatory="true" />
	
	<acme:submit name="save" code="position.save" />
	
	<acme:cancel code="position.cancel" url="position/administrator/list.do" />
	
	<jstl:if test="${position.id != 0 and notEnrol}">
	<acme:submit name="delete" code="position.delete" />
	</jstl:if>	
	

</form:form>