<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<h3><spring:message code="actor.administrators" /></h3>

<display:table name="administrators" id="row" requestURI="${requestURI }" pagesize="5">

	<acme:column property="name" titleKey="actor.name" value= "${row.name}: "/>
	
	<acme:column property="surname" titleKey="actor.surname" value= "${row.surname}: "/>
	
	<display:column>
		<a href="message/actor/create.do?actorId=${row.id }"><spring:message code="actor.message"/></a>
	</display:column>
	
</display:table>

<h3><spring:message code="actor.members" /></h3>

<display:table name="members" id="row" requestURI="${requestURI }" pagesize="5">

	<acme:column property="name" titleKey="actor.name" value= "${row.name}: "/>
	
	<acme:column property="surname" titleKey="actor.surname" value= "${row.surname}: "/>
	
	<display:column>
		<a href="message/actor/create.do?actorId=${row.id }"><spring:message code="actor.message"/></a>
	</display:column>
	
</display:table>

<h3><spring:message code="actor.brotherhoods" /></h3>

<display:table name="brotherhoods" id="row" requestURI="${requestURI }" pagesize="5">

	<acme:column property="name" titleKey="actor.name" value= "${row.name}: "/>
	
	<acme:column property="surname" titleKey="actor.surname" value= "${row.surname}: "/>
	
	<acme:column property="title" titleKey="actor.title" value= "${row.title}: "/>
	
	<display:column>
		<a href="message/actor/create.do?actorId=${row.id }"><spring:message code="actor.message"/></a>
	</display:column>
	
</display:table>

<br/>
<br/>

<acme:button name="back" code="box.back" onclick="javascript: relativeRedir('welcome/index.do');" />