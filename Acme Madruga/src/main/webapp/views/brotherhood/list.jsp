<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>



<display:table name="brotherhoods" id="row" requestURI="${requestURI }" pagesize="5">

	
	<acme:column property="title" titleKey="brotherhood.title" value= "${row.title}: "/>
	
	<acme:column property="establishment" titleKey="brotherhood.establishment" value= "${row.establishment}: "/>
	
	<acme:column property="pictures" titleKey="brotherhood.pictures" value= "${row.pictures}: "/>
	
	<acme:url href="member/list.do?brotherhoodId=${row.id }" code="brotherhood.members" />
	
	<acme:url href="procession/list.do?brotherhoodId=${row.id }" code="brotherhood.processions" />
	
	<acme:url href="float/list.do?brotherhoodId=${row.id }" code="brotherhood.floats" />
	
	<security:authorize access="hasRole('MEMBER')">
	<acme:url href="enrolment/member/enrol.do?brotherhoodId=${row.id }" code="brotherhood.enrol" />
	</security:authorize>
	
</display:table>
	
	<acme:button name="back" code="brotherhood.back" onclick="javascript: relativeRedir('welcome/index.do');" />



