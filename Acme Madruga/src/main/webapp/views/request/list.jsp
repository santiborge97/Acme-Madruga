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
	
	<acme:column property="member.name" titleKey="request.member.name" value= "${row.member}: "/>
	
	<acme:column property="member.surname" titleKey="request.member.surname" value= "${row.member}: "/>
	
	<acme:column property="status" titleKey="request.status" value= "${row.status}: "/>
	
	<acme:column property="rowNumber" titleKey="request.rowNumber" value= "${row.rowNumber}: "/>
	
	<acme:column property="columnNumber" titleKey="request.columnNumber" value= "${row.columnNumber}: "/>
	
	<acme:column property="comment" titleKey="request.comment" value= "${row.comment}: "/>
	
	<security:authorize access="hasRole('MEMBER')">
	
	<acme:url href="request/member/display.do?requestId=${row.id}" code="display" />
	
	<display:column>
	<jstl:if test="${row.status == 'PENDING'}">
	<acme:url href="request/member/delete.do?requestId=${row.id}" code="delete" />
	</jstl:if>
	</display:column>
	</security:authorize>
	
	
</display:table>
	
	<input type="button" name="back" value="<spring:message code="back" />"
	onclick="javascript: relativeRedir('welcome/index.do');" />

<script>
	var table = document.getElementById('row');
	for (var i = 1, row; row = table.rows[i]; i++) {
		var status = row.cells[3].innerText;
	   	if (status == 'PENDING') {
	   		row.style.background = 'gray'; 
	   	} else if (status == 'APPROVED') {
	   		row.style.background = 'green'; 
	   	} else if (status == 'REJECTED') {
	   		row.style.background = 'orange'; 
	   	}
	}
</script>

