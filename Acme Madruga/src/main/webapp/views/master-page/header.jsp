<%--
 * header.jsp
 *
 * Copyright (C) 2019 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>

<div>
	<a href="#"><img src="${banner}" alt="Acme Madrugá Co., Inc." width="489" height="297"/></a>
</div>

<div>
	<ul id="jMenu">
		<!-- Do not forget the "fNiv" class for the first level links !! -->
		<security:authorize access="hasRole('ADMIN')">
			<li><a class="fNiv"><spring:message	code="master.page.administrator" /></a>
				<ul>
					<li class="arrow"></li>	
					<li><a href="configuration/administrator/edit.do"><spring:message code="master.page.configuration" /></a></li>	
					<li><a href="broadcast/administrator/create.do"><spring:message code="master.page.broadcast" /></a></li>	
					<li><a href="administrator/create.do"><spring:message code="master.page.signUpAdmin" /></a></li>
					<%-- <li><a href="procession/list.do"><spring:message code="master.page.procession" /></a></li>	 --%>
					<li><a href="position/administrator/list.do"><spring:message code="master.page.position" /></a></li>	
					<li><a href="actor/administrator/score/list.do"><spring:message code="master.page.score" /></a></li>	
					<li><a href="actor/administrator/spammer/list.do"><spring:message code="master.page.spammer" /></a></li>
					<li><a href="dashboard/administrator/display.do"><spring:message code="master.page.dashboard" /></a></li>	
					<li><a href="actor/administrator/profile/list.do"><spring:message code="master.page.profiles" /></a></li>	
				</ul>
			</li>
			<li><a class="fNiv"><spring:message	code="master.page.area" /></a>
				<ul>
					<li class="arrow"></li>	
					<li><a href="area/administrator/list.do"><spring:message code="master.page.area.list" /></a></li>	
					<li><a href="area/administrator/create.do"><spring:message code="master.page.area.create" /></a></li>
				</ul>
			</li>
			<li><a class="fNiv" href="brotherhood/list.do"><spring:message code="master.page.brotherhood.list" /></a></li>
		</security:authorize>
		
		<security:authorize access="hasRole('BROTHERHOOD')">
			<li><a class="fNiv"><spring:message	code="master.page.brotherhood" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="float/brotherhood/list.do"><spring:message code="master.page.brotherhood.float.list" /></a></li>
					<li><a href="procession/brotherhood/list.do"><spring:message code="master.page.procession" /></a></li>
					<li><a href="enrolment/brotherhood/list.do"><spring:message code="master.page.enrolments" /></a></li> 			
					<li><a href="enrolment/brotherhood/listNoPosition.do"><spring:message code="master.page.enrolmentNoPosition" /></a></li> 	
					<li><a href="area/brotherhood/listAreas.do"><spring:message code="master.page.area.select" /></a></li> 		 					
				</ul>
			</li>
			<li><a class="fNiv"><spring:message	code="master.page.requests" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="request/brotherhood/listPending.do"><spring:message code="master.page.request.brotherhood.list.pending" /></a></li>
					<li><a href="request/brotherhood/list.do"><spring:message code="master.page.request.brotherhood.list.final" /></a></li>				      				
				</ul>
			</li>
			<li><a class="fNiv" href="brotherhood/list.do"><spring:message code="master.page.brotherhood.list" /></a></li>
		</security:authorize>
		
		<security:authorize access="hasRole('MEMBER')">
			<li><a class="fNiv"><spring:message	code="master.page.member" /></a>
				<ul>
					<li class="arrow"></li>
					<%-- <li><a href="procession/list.do"><spring:message code="master.page.procession" /></a></li> --%>
					<li><a href="enrolment/member/list.do"><spring:message code="master.page.enrolment" /></a></li>
					<li><a href="finderProcession/member/find.do"><spring:message code="master.page.finder" /></a></li>
				</ul>
			</li>
			<li><a class="fNiv"><spring:message	code="master.page.requests" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="request/member/list.do"><spring:message code="master.page.requests.list" /></a></li>
					<li><a href="request/member/listProcessions.do"><spring:message code="master.page.requests.listProcession" /></a></li>
				</ul>
			</li>
			<li><a class="fNiv" href="brotherhood/list.do"><spring:message code="master.page.brotherhood.list" /></a></li>
		</security:authorize>
		
		<security:authorize access="isAnonymous()">
			<li><a class="fNiv" href="security/login.do"><spring:message code="master.page.login" /></a></li>
			<li><a class="fNiv" href="register/createBrotherhood.do"><spring:message code="master.page.brotherhood.signup" /></a></li>
			<li><a class="fNiv" href="register/createMember.do"><spring:message code="master.page.member.signup" /></a></li>
			<li><a class="fNiv" href="brotherhood/list.do"><spring:message code="master.page.brotherhood.list" /></a></li>
		</security:authorize>
		
		<security:authorize access="isAuthenticated()">
			<li>
				<a class="fNiv"> 
					<spring:message code="master.page.profile" /> 
			        (<security:authentication property="principal.username" />)
				</a>
				<ul>
					<li><a href="profile/displayPrincipal.do"><spring:message code="master.page.profile" /></a></li>				
					<li><a href="box/actor/list.do"><spring:message code="master.page.box" /> </a></li>			
					<security:authorize access="hasRole('MEMBER')">
					<li><a href="data/member/get.do"><spring:message code="master.page.get.data" /> </a></li>	
					</security:authorize>
					<security:authorize access="hasRole('BROTHERHOOD')">
					<li><a href="data/brotherhood/get.do"><spring:message code="master.page.get.data" /> </a></li>	
					</security:authorize>
					<li><a href="j_spring_security_logout"><spring:message code="master.page.logout" /> </a></li>
				</ul>
			</li>
		</security:authorize>
	</ul>
</div>

<div>
	<a href="?language=en">en</a> | <a href="?language=es">es</a>
</div>

