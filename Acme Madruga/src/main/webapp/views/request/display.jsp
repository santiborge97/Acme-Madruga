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
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<acme:display code="request.procession" property="${request.procession.title}" />

<acme:display code="request.status" property="${request.status}" />

<acme:display code="request.rowNumber" property="${request.rowNumber}" />

<acme:display code="request.columnNumber" property="${request.columnNumber}" />

<acme:display code="request.comment" property="${request.comment}" />

<acme:button name="back" code="back" onclick="javascript: relativeRedir('request/member/list.do');" />



