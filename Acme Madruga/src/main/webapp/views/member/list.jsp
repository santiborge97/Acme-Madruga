<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>



<display:table name="members" id="row" requestURI="${requestURI }" pagesize="5">

	
	<acme:column property="name" titleKey="member.name" value= "${row.name}: "/>
	
	<acme:column property="middleName" titleKey="member.middleName" value= "${row.middleName}: "/>
	
	<acme:column property="surname" titleKey="member.surname" value= "${row.surname}: "/>
	
	<acme:column property="photo" titleKey="member.photo" value= "${row.photo}: "/>
	
	<acme:column property="email" titleKey="member.email" value= "${row.email}: "/>
	
	<acme:column property="phone" titleKey="member.phone" value= "${row.phone}: "/>
	
	<acme:column property="address" titleKey="member.address" value= "${row.address}: "/>
	

</display:table>
	
	<acme:button name="back" code="member.back" onclick="javascript: relativeRedir('welcome/index.do');" />




