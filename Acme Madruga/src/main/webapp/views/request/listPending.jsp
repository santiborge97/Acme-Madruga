<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>



<display:table name="requests" id="row" requestURI="${requestURI }" pagesize="5">

	<acme:column property="procession.title" titleKey="request.procession" value= "${row.procession}: "/>
	
	<acme:column property="member.name" titleKey="request.member" value= "${row.member}: "/>
	
	<acme:column property="member.surname" titleKey="request.member.surname" value= "${row.member}: "/>
	
	<acme:column property="status" titleKey="request.status" value= "${row.status}: "/>
	
	<acme:url href="request/brotherhood/accept.do?requestId=${row.id}" code="request.accept" />
	
	<acme:url href="request/brotherhood/reject.do?requestId=${row.id}" code="request.reject" />
	
</display:table>
	
	<acme:button name="back" code="back" onclick="javascript: relativeRedir('welcome/index.do');" />


