<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

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

    <div class="container">
      <div class="box">
      	<div class="bigbox">
      		<div>
      			<img alt="x" src="/images/loginLogo.png">
	      	</div>
	      	<br>
	      	
	      	<div class="text">
	      		<hr class="`hr">
	      		추가 인증 필요
	      		<hr class="`hr">
	      	</div>
	      	
	      	<form action="/auth/kakao/joinProc" method="post">
	      		<table class="table">
	      			<tr>
	      				<td><input type="text" name="username" placeholder="Username"></td>
	      			</tr>
	      			<tr>
	      				<td><input type="email" name="email" placeholder="Email"></td>
	      			</tr>
	      		</table>
	          	<input type="submit" value="가입완료">
	        </form>
			
		</div>
	    <div class="smallbox">
	    	<div class="text">
	        	<a href="javascript:widow.history.back()" class="under1">뒤로가기</a>
	    	</div>
	      </div>
      
	</div>
</div>
  
  <%@ include file="../include/footer.jsp" %>

</body>
</html>
