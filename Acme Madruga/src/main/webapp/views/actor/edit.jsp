<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>



<form:form action="profile/${actionURI }"
	modelAttribute="${authority}">

	<form:hidden path="id" />
	<form:hidden path="version" />
	
	<acme:textbox code="actor.name" path="name" obligatory="true"/>

	<acme:textbox code="actor.middleName" path="middleName" />
	
	<acme:textbox code="actor.surname" path="surname" obligatory="true"/>
	
	<acme:textbox code="actor.photo" path="photo" />
	
<jstl:choose>
	<jstl:when test="${authority=='administrator' }">
	<acme:textbox code="actor.email" path="email" obligatory="true" size="35" placeholder="_@_._ / _<_@_._> / _@ / _<_@>" pattern="^[\\w]+@((?:[a-zA-Z0-9-]+\\.)+[a-zA-Z0-9]+){0,1}|(([\\w]\\s)*[\\w])+<\\w+@((?:[a-zA-Z0-9-]+\\.)+[a-zA-Z0-9]+){0,1}>"/>	
	</jstl:when>
	<jstl:otherwise>
	<acme:textbox code="actor.email" path="email" obligatory="true" size="35" placeholder="_@_._ / _<_@_._>" pattern="^[\\w]+@(?:[a-zA-Z0-9]+\\.)+[a-zA-Z0-9]+|(([\\w]\\s)*[\\w])+<\\w+@(?:[a-zA-Z0-9]+\\.)+[a-zA-Z0-9]+>$"/>
	</jstl:otherwise>
</jstl:choose>
	<acme:textbox code="actor.address" path="address" />
	
	<acme:textbox code="actor.phone" path="phone" id="phone" onblur="javascript: checkPhone();"/> 
	
	<security:authorize access="hasRole('BROTHERHOOD')">
		<acme:textbox code="brotherhood.title" path="title" obligatory="true"/>
		<acme:textbox code="brotherhood.pictures" path="pictures" obligatory="true"/>
		<acme:textbox code="brotherhood.establishment" path="establishment" placeholder="yyyy/MM/dd" obligatory="true"/>
	</security:authorize> 
	
	<acme:submit name="save" code="actor.save"/>	

	<acme:button name="cancel" code="actor.cancel" onclick="javascript: relativeRedir('profile/displayPrincipal.do');" />
	
</form:form>


<script type="text/javascript">
	function checkPhone() {
		var target = document.getElementById("phone");
		var input = target.value;
		var regExp1 = new RegExp("(^[+]([1-9]{1,3})) ([(][1-9]{1,3}[)]) (\\d{4,}$)");
		var regExp2 = new RegExp("(^[+]([1-9]{1,3})) (\\d{4,}$)");
		var regExp3 = new RegExp("(^\\d{4,}$)");

		if ('${phone}' != input && regExp1.test(input) == false && regExp2.test(input) == false && regExp3.test(input) == false) {
			if (confirm('<spring:message code="actor.phone.wrong" />') == false) {
				return true;
			
			}
		} else if ('${phone}' != input && regExp1.test(input) == false && regExp2.test(input) == false && regExp3.test(input) == true) {
			target.value = '${defaultCountry}' + " " + input;
		}
	}
</script>  
