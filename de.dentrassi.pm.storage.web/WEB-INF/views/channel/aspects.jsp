<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ taglib tagdir="/WEB-INF/tags" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<h:main title="Channel aspects">

<ul class="button-bar">
	<li><a class="pure-button" href="view">Back</a></li>
</ul>

<div class="pure-g">

<div class="pure-u pure-u-md-1-2">

<div style="padding: 1em;">
<h2>Assigned aspects</h2>
<dl class="aspects">
<c:forEach items="${channel.aspects }" var="aspect">
<dt><span  class="<c:if test="${not aspect.resolved }">unresolved</c:if>">${fn:escapeXml(aspect.label) }</span><c:if test="${not aspect.resolved }"> (unresolved)</c:if></dt>
<dd>${fn:escapeXml(aspect.description) }<br/> <form action="removeAspect" method="POST"><input type="hidden" name="aspect" value="<c:out value="${aspect.factoryId }"></c:out>"><input type="submit" value="Remove" class="pure-button" /></form></dd>
</c:forEach>
</dl>
</div>
</div>

<div class="pure-u pure-u-md-1-2" >
<div style="padding: 1em;">
<h2>Additional aspects</h2>
<dl class="aspects">
<c:forEach items="${addAspects }" var="aspect">
<dt>${fn:escapeXml(aspect.label ) }</dt>
<dd>${fn:escapeXml(aspect.description) }<br/> <form action="addAspect" method="POST"><input type="hidden" name="aspect" value="<c:out value="${aspect.factoryId }"></c:out>"><input type="submit" value="Add" class="pure-button" /></form></dd>
</c:forEach>
</dl>
</div>
</div>

</div>

</h:main>