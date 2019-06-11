<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<display:table name="actors" id="row" requestURI="${requestURI }" pagesize="5">

	<acme:column property="userAccount.username" titleKey="actor.username" value= "${row.userAccount.username}: "/>
	
	<jstl:if test="${score}">
		<display:column titleKey="actor.score"> 
			<jstl:if test="${empty row.score}">
				<spring:message code="actor.status.na" />
			</jstl:if>
			<jstl:if test="${not empty row.score}">
				<jstl:out value="${row.score}"/>
			</jstl:if>
		</display:column>
	</jstl:if>	
	
	<jstl:if test="${spam}">
		<display:column titleKey="actor.spammer"> 
			<jstl:if test="${empty row.spammer}">
				<spring:message code="actor.status.na" />
			</jstl:if>
			
			<jstl:if test="${not empty row.spammer and !row.spammer}">
				<spring:message code="actor.status.notSpammer" />
			</jstl:if>
			
			<jstl:if test="${not empty row.spammer and row.spammer}">
				<spring:message code="actor.status.spammer" />
			</jstl:if>
	
		</display:column>
	</jstl:if>	

	<jstl:if test="${spam or score}">
		<display:column titleKey="actor.status.banned"> 
			<jstl:if test="${row.userAccount.isNotBanned}">
				<spring:message code="actor.status.unban" />
			</jstl:if>
			
			<jstl:if test="${!row.userAccount.isNotBanned}">
				<spring:message code="actor.status.ban" />
			</jstl:if>
		
		</display:column>
		</jstl:if>	
	
	<jstl:if test="${spam or score}">
		<display:column titleKey="actor.banned"> 
			
				<jstl:if test="${spam}">
					<jstl:if test="${row.spammer and row.userAccount.isNotBanned}">
						<a href="actor/administrator/spammer/banActor.do?actorId=${row.id}"><spring:message code="actor.ban"/></a>
					</jstl:if>
				
					<jstl:if test="${row.spammer and !row.userAccount.isNotBanned}">
						<a href="actor/administrator/spammer/banActor.do?actorId=${row.id}"><spring:message code="actor.unban"/></a>
					</jstl:if>
				</jstl:if>
				
			<jstl:if test="${not empty row.score}">	
				<jstl:if test="${score}">
					<jstl:if test="${row.score < -0.5 and row.userAccount.isNotBanned}">
						<a href="actor/administrator/score/banActor.do?actorId=${row.id}"><spring:message code="actor.ban"/></a>
					</jstl:if>
				
					<jstl:if test="${row.score < -0.5 and !row.userAccount.isNotBanned}">
						<a href="actor/administrator/score/banActor.do?actorId=${row.id}"><spring:message code="actor.unban"/></a>
					</jstl:if>
				</jstl:if>
				
			</jstl:if>
		

		</display:column>
	</jstl:if>
	
	<jstl:if test="${admin}">
		<display:column> 
			<a href="actor/administrator/profile/displayActor.do?actorId=${row.id}"><spring:message code="actor.display"/></a>
		</display:column>
	</jstl:if>
	
</display:table>

<jstl:if test="${score}">
		<a href="actor/administrator/score/calculate.do"><spring:message code="actor.calculate"/></a>
</jstl:if>	

<jstl:if test="${spam}">
		<a href="actor/administrator/spammer/calculate.do"><spring:message code="actor.find"/></a>
</jstl:if>	

<acme:button name="back" code="actor.back" onclick="javascript: relativeRedir('welcome/index.do');" />