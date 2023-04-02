/**
 * follow.jsp
 * true -> follow 하기
 * false -> unfollow 하기
 */
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