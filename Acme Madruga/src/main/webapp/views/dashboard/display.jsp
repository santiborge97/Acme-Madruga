<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>


<h3><spring:message code="dashboard.membersPerBrotherhood" /></h3>

<fieldset>
	<ul>
	<li><spring:message code="dashboard.average" />: ${avgMemberPerBrotherhood}</li>
	<li><spring:message code="dashboard.minimum" />: ${minMemberPerBrotherhood}</li>
	<li><spring:message code="dashboard.maximum" />: ${maxMemberPerBrotherhood}</li>
	<li><spring:message code="dashboard.deviation" />: ${stddevMemberPerBrotherhood}</li>
	</ul>
</fieldset>
	
<h3><spring:message code="dashboard.theLargestBrotherhoods" /></h3>
<fieldset>

   <jstl:forEach items="${theLargestBrotherhoods}" var="b">
    <jstl:out value="${b}" />
    <br />
   </jstl:forEach>

</fieldset>
	
<h3><spring:message code="dashboard.theSmallestBrotherhoods" /></h3>
<fieldset>

   <jstl:forEach items="${theSmallestBrotherhoods}" var="b">
    <jstl:out value="${b}" />
    <br />
   </jstl:forEach>

</fieldset>

<h3><spring:message code="dashboard.ratiosRequestForProcession" /></h3>
<fieldset>

<jstl:if test="${existProcession}">

	<form:form action="dashboard/administrator/calculate.do" modelAttribute="processionIdForm">
	
	<form:label path="id">
		<spring:message code="dashboard.procession" />:</form:label>
	<form:select path="id">
		<form:options items="${processions }" itemLabel="title" itemValue="id" />
	</form:select>
	<form:errors cssClass="error" path="id" />
	<br />
	
	<acme:submit name="save" code="dashboard.calculate" />
	
</form:form>  
</jstl:if>
 
   	<ul>
	<li><spring:message code="dashboard.approved" />: ${approvedRatio}</li>
	<li><spring:message code="dashboard.pending" />: ${pendingRatio}</li>
	<li><spring:message code="dashboard.rejected" />: ${rejectedRatio}</li>
	</ul>

</fieldset>

<h3><spring:message code="dashboard.processionsLessThirtyDays" /></h3>
<fieldset>

   <jstl:forEach items="${findProcessionsLessThirtyDays}" var="p">
    <jstl:out value="${p}" />
    <br />
   </jstl:forEach>

</fieldset>

<h3><spring:message code="dashboard.ratiosRequest" /></h3>
<fieldset>

   <ul>
	<li><spring:message code="dashboard.approved" />: ${ratiosRequest[0][0]}</li>
	<li><spring:message code="dashboard.pending" />: ${ratiosRequest[0][1]}</li>
	<li><spring:message code="dashboard.rejected" />: ${ratiosRequest[0][2]}</li>
	</ul>

</fieldset>

<h3><spring:message code="dashboard.membersTenPerCent" /></h3>
<fieldset>

   <jstl:forEach items="${membersTenPerCent}" var="m">
    <jstl:out value="${m}" />
    <br />
   </jstl:forEach>

</fieldset>
	
<h3><spring:message code="dashboard.brotherhoodPerArea" /></h3>

<fieldset>

	<ul>
	<li><spring:message code="dashboard.ratio" />: ${ratioBrotherhoodPerArea}</li>
	<li><spring:message code="dashboard.count" />: ${countBrotherhoodPerArea}</li>
	<li><spring:message code="dashboard.average" />: ${avgBrotherhoodPerArea}</li>
	<li><spring:message code="dashboard.minimum" />: ${minBrotherhoodPerArea}</li>
	<li><spring:message code="dashboard.maximum" />: ${maxBrotherhoodPerArea}</li>
	<li><spring:message code="dashboard.deviation" />: ${stddevBrotherhoodPerArea}</li>
	</ul>

</fieldset>

<h3><spring:message code="dashboard.statsOfFinderResults" /></h3>

<fieldset>

	<ul>
	<li><spring:message code="dashboard.average" />: ${avgResultPerFinder}</li>
	<li><spring:message code="dashboard.minimum" />: ${minResultPerFinder}</li>
	<li><spring:message code="dashboard.maximum" />: ${maxResultPerFinder}</li>
	<li><spring:message code="dashboard.deviation" />: ${stddevResultPerFinder}</li>
	</ul>

</fieldset>	
	
<h3><spring:message code="dashboard.ratioEmptyFinders" /></h3>
<fieldset>
	${ratioEmptyFinders}
</fieldset>

<script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.5.0/Chart.min.js"></script>

<div style="width: 25%;">
	<br/>
	<canvas id="myChart" width="400" height="400"></canvas>
	<br/>
	<canvas id="myChart2" width="400" height="400"></canvas>
	<br/>
	<canvas id="myChart3" width="400" height="400"></canvas>
	<br/>
	<canvas id="myChart4" width="400" height="400"></canvas>
	<br/>
	<canvas id="myChart5" width="400" height="400"></canvas>
	<br/>
</div>

<br/>

<acme:button name="back" code="dashboard.back" onclick="javascript: relativeRedir('welcome/index.do');" />
	
<jstl:if test="${language == 'en'}">
	<script>
	window.onload = function() {
			
		var ctx = document.getElementById("myChart").getContext('2d');
		var myChart = new Chart(ctx, {
		    type: 'bar',
		    data: {
		        labels: ["Positions"],
		        datasets: [${histogramaEn}]
		    },
		    options: {
		        scales: {
		            yAxes: [{
		            	stacked: false,
		                ticks: {
		                    beginAtZero:true
		                }
		            }],
		            xAxes: [{
		            	barPercentage: 0.5,
		            	stacked: true,
		                ticks: {
		                    beginAtZero:true
		                }
		            }]
		        }
		    }
		});
		
		var ctx2 = document.getElementById("myChart2").getContext('2d');
		var myChart2 = new Chart(ctx2, {
		    type: 'pie',
		    data: {
		        labels: ["No spammer", "Spammer"],
		        datasets: [{
		        	data: [${spammers}],
		        	backgroundColor: ["#82b74b", "#c94c4c"],
		        	label: "Ratio spammers"
		        }]
		    },
			options: {
				responsive: true
			}
		});
		
		var ctx3 = document.getElementById("myChart3").getContext('2d');
		var myChart3 = new Chart(ctx3, {
		    type: 'pie',
		    data: {
		        labels: ["-1 - -0,75", "-0,75 - -0,5", "-0,5 - -0,25", "-0,25 - 0", "0 - 0,25", "0,25 - 0,5", "0,5 - 0,75", "0,75 - 1"],
		        datasets: [{
		        	data: [${scores}],
		        	backgroundColor: ["#ff0000", "#ff4000", "#ff8000", "#ffcc00", "#ccff00", "#80ff00", "#40ff00", "#00ff00"],
		        	label: "Ratio score"
		        }]
		    },
			options: {
				responsive: true
			}
		});
		
		var ctx4 = document.getElementById("myChart4").getContext('2d');
		var myChart4 = new Chart(ctx4, {
		    type: 'bar',
		    data: {
		        labels: ["Brotherhoods per Area"],
		        datasets: [${BrotherhoodPerAreaThingsEn}]
		    },
		    options: {
		        scales: {
		            yAxes: [{
		            	stacked: false,
		                ticks: {
		                    beginAtZero:true
		                }
		            }],
		            xAxes: [{
		            	barPercentage: 0.5,
		            	stacked: true,
		                ticks: {
		                    beginAtZero:true
		                }
		            }]
		        }
		    }
		});
		
		var ctx5 = document.getElementById("myChart5").getContext('2d');
		var myChart5 = new Chart(ctx5, {
		    type: 'bar',
		    data: {
		        labels: ["Members per Brotherhood"],
		        datasets: [${MemberPerBrotherhoodThingsEn}]
		    },
		    options: {
		        scales: {
		            yAxes: [{
		            	stacked: false,
		                ticks: {
		                    beginAtZero:true
		                }
		            }],
		            xAxes: [{
		            	barPercentage: 0.5,
		            	stacked: true,
		                ticks: {
		                    beginAtZero:true
		                }
		            }]
		        }
		    }
		});
	};
	</script>	
</jstl:if>

	
<jstl:if test="${language == 'es'}">
	<script>
	window.onload = function() {
		
		var ctx = document.getElementById("myChart").getContext('2d');
		var myChart = new Chart(ctx, {
		    type: 'bar',
		    data: {
		        labels: ["Cargos"],
		        datasets: [${histogramaEs}]
		    },
		    options: {
		        scales: {
		            yAxes: [{
		            	stacked: false,
		                ticks: {
		                    beginAtZero:true
		                }
		            }],
		            xAxes: [{
		            	barPercentage: 0.5,
		            	stacked: true,
		                ticks: {
		                    beginAtZero:true
		                }
		            }]
		        }
		    }
		});
		
		var ctx2 = document.getElementById("myChart2").getContext('2d');
		var myChart2 = new Chart(ctx2, {
		    type: 'pie',
		    data: {
		        labels: ["No spammer", "Spammer"],
		        datasets: [{
		        	data: [${spammers}],
		        	backgroundColor: ["#82b74b", "#c94c4c"],
		        	label: "Ratio spammers"
		        }]
		    },
			options: {
				responsive: true
			}
		});
		
		var ctx3 = document.getElementById("myChart3").getContext('2d');
		var myChart3 = new Chart(ctx3, {
		    type: 'pie',
		    data: {
		        labels: ["-1 - -0,75", "-0,75 - -0,5", "-0,5 - -0,25", "-0,25 - 0", "0 - 0,25", "0,25 - 0,5", "0,5 - 0,75", "0,75 - 1"],
		        datasets: [{
		        	data: [${scores}],
		        	backgroundColor: ["#ff0000", "#ff4000", "#ff8000", "#ffcc00", "#ccff00", "#80ff00", "#40ff00", "#00ff00"],
		        	label: "Ratio puntuación"
		        }]
		    },
			options: {
				responsive: true
			}
		});
		
		var ctx4 = document.getElementById("myChart4").getContext('2d');
		var myChart4 = new Chart(ctx4, {
		    type: 'bar',
		    data: {
		        labels: ["Hermandades por Area"],
		        datasets: [${BrotherhoodPerAreaThingsEs}]
		    },
		    options: {
		        scales: {
		            yAxes: [{
		            	stacked: false,
		                ticks: {
		                    beginAtZero:true
		                }
		            }],
		            xAxes: [{
		            	barPercentage: 0.5,
		            	stacked: true,
		                ticks: {
		                    beginAtZero:true
		                }
		            }]
		        }
		    }
		});
		
		var ctx5 = document.getElementById("myChart5").getContext('2d');
		var myChart5 = new Chart(ctx5, {
		    type: 'bar',
		    data: {
		        labels: ["Miembros por Hermandad"],
		        datasets: [${MemberPerBrotherhoodThingsEs}]
		    },
		    options: {
		        scales: {
		            yAxes: [{
		            	stacked: false,
		                ticks: {
		                    beginAtZero:true
		                }
		            }],
		            xAxes: [{
		            	barPercentage: 0.5,
		            	stacked: true,
		                ticks: {
		                    beginAtZero:true
		                }
		            }]
		        }
		    }
		});
	};
	</script>	
</jstl:if>