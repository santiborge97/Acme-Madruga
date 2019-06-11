<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>



<display:table name="enrolments" id="row" requestURI="${requestURI }" pagesize="5">

	<security:authorize access="hasRole('MEMBER')">
	<acme:column property="brotherhood.title" titleKey="enrolment.brotherhood" value= "${row.brotherhood}: "/>
	</security:authorize>
	
	<security:authorize access="hasRole('BROTHERHOOD')">
	<acme:column property="member.name" titleKey="enrolment.member" value= "${row.member}: "/>	
	</security:authorize>
	
	<jstl:if test="${language == 'en'}">
		<acme:column property="position.englishName" titleKey="enrolment.position" value= "${row.position}: "/>
	</jstl:if>
	
	<jstl:if test="${language == 'es'}">
		<acme:column property="position.spanishName" titleKey="enrolment.position" value= "${row.position}: "/>
	</jstl:if>
	
	<acme:column property="moment" titleKey="enrolment.moment" value= "${row.moment}: "/>
	
	<acme:column property="dropOutMoment" titleKey="enrolment.dropOutMoment" value= "${row.dropOutMoment}: "/>
	
	<display:column>
		<jstl:if test="${empty row.dropOutMoment}">
			<a href="enrolment/${autoridad }/dropout.do?enrolmentId=${row.id }"><spring:message code="enrolment.${autoridad}.dropOut"/></a>
			<security:authorize access="hasRole('BROTHERHOOD')">
				<a href="enrolment/${autoridad }/edit.do?enrolmentId=${row.id }"><spring:message code="enrolment.edit"/></a>
				<jstl:if test="${!noPosition }">
				<a href="member/brotherhood/display.do?memberId=${row.member.id }"><spring:message code="enrolment.displayMember"/></a>
				</jstl:if>
			</security:authorize>
		</jstl:if>
	</display:column>
	

</display:table>
	
	<acme:button name="back" code="enrolment.back" onclick="javascript: relativeRedir('welcome/index.do');" />




