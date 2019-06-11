<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<display:table name="positions" id="row" requestURI="${requestURI }" pagesize="5">

	<acme:column property="englishName" titleKey="position.englishName" value="${row.englishName}" />
	
	<acme:column property="spanishName" titleKey="position.spanishName" value="${row.englishName}" />
	
	<acme:url href="position/administrator/edit.do?positionId=${row.id }" code="position.edit" />
	
	<acme:url href="position/administrator/display.do?positionId=${row.id }" code="position.display" />

</display:table>

<a href="position/administrator/create.do"><spring:message code="position.create"/></a>

<acme:button name="back" code="position.back" onclick="javascript: relativeRedir('welcome/index.do');" />