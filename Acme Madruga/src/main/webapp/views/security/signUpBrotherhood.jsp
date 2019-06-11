<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<form:form action="register/editBrotherhood.do" modelAttribute="brotherhood">
	
	<acme:textbox code="brotherhood.title" path="title" obligatory="true"/>
	
	<acme:textbox code="brotherhood.pictures" size="100" pattern="^http(s*)://(?:[a-zA-Z0-9-]+[\\.\\:]{0,1})+([a-zA-Z/]+)*(,http(s*)://(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]+)*$" placeholder="http(s)://____.___,http(s)://___.___,..." path="pictures" obligatory="true"/>
	
	<acme:textbox code="brotherhood.establishment" path="establishment" placeholder="yyyy/MM/dd" obligatory="true"/>
	
	<acme:textbox code="brotherhood.name" path="name" obligatory="true"/>

	<acme:textbox code="brotherhood.middleName" path="middleName" />
	
	<acme:textbox code="brotherhood.surname" path="surname" obligatory="true"/>
	
	<acme:textbox code="brotherhood.photo" path="photo" />
	
	<acme:textbox code="brotherhood.email" path="email" obligatory="true" size="35" placeholder="_@_._ / _<_@_._>" pattern="^[\\w]+@(?:[a-zA-Z0-9]+\\.)+[a-zA-Z0-9]+|(([\\w]\\s)*[\\w])+<\\w+@(?:[a-zA-Z0-9]+\\.)+[a-zA-Z0-9]+>$"/>
	
	<acme:textbox code="brotherhood.address" path="address" />
	
	<acme:textbox code="brotherhood.phone" path="phone" id="phone" onblur="javascript: checkPhone();"/> 
	
	<acme:textbox code="brotherhood.username" path="username" obligatory="true"/>
	
	<acme:password code="brotherhood.password" path="password" obligatory="true"/>
	
	<acme:password code="brotherhood.confirmPassword" path="confirmPassword" obligatory="true"/>
	
	<acme:checkbox path="checkbox" code1="brotherhood.checkBox1" code2="brotherhood.checkBox2" href="termsAndConditions/show.do" />
		
	<jstl:if test="${showError == true}">
		<div class="error">
			<spring:message code="brotherhood.commit.error" />
		</div>
	</jstl:if>
	
	<input type="submit" name="save" value="<spring:message code="brotherhood.save" />" />
	
	<acme:cancel code="brotherhood.cancel" url="welcome/index.do" />
	
</form:form>

   <jstl:if test="${message!=null }">
<div class="error"><spring:message code="brotherhood.commit.error" /></div>
</jstl:if> 
 
<script type="text/javascript">
	function checkPhone() {
		var target = document.getElementById("phone");
		var input = target.value;
		var regExp1 = new RegExp("(^[+]([1-9]{1,3})) ([(][1-9]{1,3}[)]) (\\d{4,}$)");
		var regExp2 = new RegExp("(^[+]([1-9]{1,3})) (\\d{4,}$)");
		var regExp3 = new RegExp("(^\\d{4}$)");

		if ('${phone}' != input && regExp1.test(input) == false && regExp2.test(input) == false && regExp3.test(input) == false) {
			if (confirm('<spring:message code="actor.phone.wrong" />') == false) {
				return true;
			
			}
		} else if ('${phone}' != input && regExp1.test(input) == false && regExp2.test(input) == false && regExp3.test(input) == true) {
			target.value = '${defaultCountry}' + " " + input;
		}
	}
</script>
