<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <meta http-equiv="X-UA-Compatible" content="ie=edge">
  <title>Costagram</title>
  <link href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css" rel="stylesheet">
  <link rel="shortcut icon" href="/images/favicon.ico">
  <link rel="stylesheet" href="/css/styles.css">
</head>
<body>

<%@ include file="../include/nav.jsp" %>

 <main id="follow">
    <ul class="follow__users u-default-box">
    
	<c:forEach items="${followers }" var="follower">    
      <li class="follow__user">
        <div class="follow__content">
          <img src="/images/avatar.jpg" />
          <div class="follow__info">
            <span class="follow__username">${follower.fromUser.username}</span>
          </div>
        </div>
        
        <div id="follow_check">
        	<c:if test="${principal.user.id ne follower.fromUser.id }">
		        <c:choose>
		        	<c:when test="${follower.followState eq true }">
		        		<button onclick="follow(false, ${follower.fromUser.id})" class="following_btn">팔로잉</button>
		        	</c:when>
		        	<c:otherwise>
		        		<button  onclick="follow(true, ${follower.fromUser.id})" class="follow_btn">팔로우</button>
		        	</c:otherwise>
		        </c:choose>
			</c:if>        
        </div>
        
      </li>
      </c:forEach>

    </ul>
  </main>

  <%@ include file="../include/footer.jsp" %>
<!--    <script type="text/javascript" src="/js/follow.js" /> -->
   <script type="text/javascript">
   function follow(check, userId) {
		alert("follow");
		let url = "/follow/"+userId;

		if (check) {
			fetch(url, {
				method : "POST"			  
			}).then(function(res) {
				return res.text();
			}).then(function(res) {			  
				if(res==="OK") {	  
					let follow_check_el = document.querySelector("#follow_check");				  
					follow_check_el.innerHTML = "<button onclick='follow(false,"+userId+")' class='profile_edit_btn'>팔로잉</button>";
				}
			});
		} else {
			fetch(url, {
				method : "DELETE"			  
			}).then(function(res) {
				return res.text();
			}).then(function(res) {
				if(res==="OK") {
					let follow_check_el = document.querySelector("#follow_check");
					follow_check_el.innerHTML = "<button onclick='follow(true,"+userId+")' class='profile_followe_btn'>팔로우</button>";
				}
			});
		}
	}
   
   
   </script>
   
</body>
</html>