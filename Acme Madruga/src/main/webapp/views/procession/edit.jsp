<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<form:form action="procession/brotherhood/edit.do" modelAttribute="procession">
	
	
	<form:hidden path="id" />
	<form:hidden path="version" />
	
	
	<acme:textbox path="title" code="input.title" />
	
	<acme:textbox path="description" code="input.description" />
	
	<acme:textbox path="maxRow" code="input.maxRow" />
	
	<acme:textbox path="maxColumn" code="input.maxColumn" />
	
	<acme:textbox path="organisationMoment" code="input.organisationMoment" placeholder="yyyy/MM/dd hh:mm"/>
  	
  	<acme:choose path="finalMode" code="input.finalMode" value1="true" value2="false" label1="Final" label2="No Final" />
			
	<acme:submit name="save" code="procession.save" />	

	<acme:cancel code="procession.cancel" url="procession/brotherhood/list.do" />
	
	<jstl:if test="${procession.id != 0}">
	<acme:delete confirmation="procession.confirm.delete" code="procession.delete" />
	</jstl:if>	
	

</form:form>    