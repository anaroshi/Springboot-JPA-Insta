<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>

<sec:authorize access="isAuthenticated()">
	<sec:authentication property="principal" var="principal" />
</sec:authorize>

<nav class="navigation">
  <a href="feed">
    <img src="/images/logo.png" />
  </a>
  <input type="text" placeholder="Search">
  <div class="navigation__links">
    <a href="/image/explore/${principal.user.id }" class="navigation__link">
        <i class="fa fa-compass"></i>
    </a>
    <a href="#" class="navigation__link like_popup">
        <i class="fa fa-heart-o"></i>
    </a>
    <a href="/user/${principal.user.id }" class="navigation__link">
        <i class="fa fa-user-o"></i>
    </a>
  </div>
</nav>

<%@ include file="modal.jsp" %>