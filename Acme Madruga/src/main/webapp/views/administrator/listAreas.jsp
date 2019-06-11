<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<display:table name="areas" id="row" requestURI="${requestURI }" pagesize="5">

	<acme:column property="name" titleKey="area.name" value="${row.name}" />
	
	<acme:column property="pictures" titleKey="area.pictures" value="${row.pictures}" />

	
	<acme:url href="area/administrator/edit.do?areaId=${row.id }" code="edit" />	
	

</display:table>

<acme:button name="back" code="actor.back" onclick="javascript: relativeRedir('welcome/index.do');" />