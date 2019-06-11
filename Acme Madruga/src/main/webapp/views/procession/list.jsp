<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<%-- Search Procession --%>

<display:table name="processions" id="row" requestURI="${requestURI }" pagesize="5">
	
	<security:authorize access="hasRole('BROTHERHOOD')">
	<acme:column property="ticker" titleKey="procession.ticker" value= "${row.ticker}: "/>
	</security:authorize> 
	
	<acme:column property="title" titleKey="procession.title" value= "${row.title}: "/>
	
	<acme:column property="brotherhood.title" titleKey="procession.brotherhood" value= "${row.brotherhood.title} "/>
	
	<acme:column property="brotherhood.area.name" titleKey="procession.area" value="${row.brotherhood.area.name }" />
	
	<acme:column property="description" titleKey="procession.description" value= "${row.description}: "/>
	
	<acme:dateFormat titleKey="procession.organisationMoment" value="${row.organisationMoment }" pattern="yyyy/MM/dd HH:mm" />
	
	<jstl:if test="${autoridad == 'brotherhood'}">
		<acme:column property="finalMode" titleKey="procession.finalMode" value="${row.finalMode }" />
	</jstl:if>

	
	<%-- <security:authorize access="isAnonymous()"> 
	<acme:url href="float/procession/list.do?processionId=${row.id }" code="procession.float" />
 	</security:authorize> --%>
	
	<security:authorize access="hasRole('BROTHERHOOD')">
	<acme:url href="procession/brotherhood/edit.do?processionId=${row.id }" code="procession.edit" />
	<acme:url href="procession/brotherhood/display.do?processionId=${row.id }" code="procession.display" />
	<acme:url href="float/brotherhood/floatAddProcession.do?processionId=${row.id }" code="procession.addFloat" />	
	<acme:url href="float/brotherhood/listByProcession.do?processionId=${row.id }" code="procession.float" />
	</security:authorize> 

</display:table>
	<security:authorize access="hasRole('BROTHERHOOD')">
	<a href="procession/brotherhood/create.do"><spring:message code="procession.create"/></a>
	</security:authorize>
	
	<acme:button name="back" code="procession.back" onclick="javascript: relativeRedir('welcome/index.do');" />