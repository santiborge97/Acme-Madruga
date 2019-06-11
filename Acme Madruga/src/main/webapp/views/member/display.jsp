<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<security:authorize access="hasRole('BROTHERHOOD')">

<spring:message code="member.name" var="name" />
<jstl:out value="${name }: " />
<jstl:out value="${member.name}" />
<br>

<spring:message code="member.middleName" var="middleName" />
<jstl:out value="${middleName }: " />
<jstl:out value="${member.middleName}" />
<br>

<spring:message code="member.surname" var="surname" />
<jstl:out value="${surname }: " />
<jstl:out value="${member.surname}" />
<br>

<spring:message code="member.photo" var="photo" />
<jstl:out value="${photo }: " />
<jstl:out value="${member.photo}" />
<br>

<spring:message code="member.email" var="email" />
<jstl:out value="${email }: " />
<jstl:out value="${member.email}" />
<br>

<spring:message code="member.phone" var="phone" />
<jstl:out value="${phone }: " />
<jstl:out value="${member.phone}" />
<br>

<spring:message code="member.address" var="address" />
<jstl:out value="${address }: " />
<jstl:out value="${member.address}" />
<br>

<input type="button" name="back"
	value="<spring:message code="member.back" />"
	onclick="javascript: relativeRedir('welcome/index.do');" />
<br />
<br />

</security:authorize>

