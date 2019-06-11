<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<jstl:if test="${AreInFinder }">
<security:authorize access="hasRole('MEMBER')">
<form:form action="${requestAction }" modelAttribute="finder"> 

	<form:hidden path="id"/>
	<form:hidden path="version"/>

	<acme:textbox path="keyWord" code="procession.keyWord" />
	
	<acme:textbox path="area" code="procession.area" />
	
	<acme:textbox path="minDate" code="procession.minDate" />
	
	<acme:textbox path="maxDate" code="procession.maxDate" />
	
	<input type="submit" name="find" value="<spring:message code="procession.find"/>"/>
	
</form:form> 
</security:authorize>
</jstl:if>

<br/>

<display:table name="processions" id="row" requestURI="${requestURI }" pagesize="${pagesize }">
	
	
	<acme:column property="title" titleKey="procession.title" value= "${row.title}: "/>
	
	<acme:column property="brotherhood.title" titleKey="procession.brotherhood" value= "${row.brotherhood.title} "/>
	
	<acme:column property="brotherhood.area.name" titleKey="procession.area" value="${row.brotherhood.area.name }" />
	
	<acme:column property="description" titleKey="procession.description" value= "${row.description}: "/>
	
	<acme:dateFormat titleKey="procession.organisationMoment" value="${row.organisationMoment }" pattern="yyyy/MM/dd HH:mm" />

	<acme:url href="float/procession/list.do?processionId=${row.id }" code="procession.float" />

</display:table>
	
	<br/>
	
	<acme:button name="back" code="procession.back" onclick="javascript: relativeRedir('welcome/index.do');" />