/**
 * follow.jsp
 * true -> follow 하기
 * false -> unfollow 하기
 */
function follow(check, userId, index) {
//	alert("check : "+check+", userId : "+userId+", index : "+index);
	let url = "/follow/"+userId;

	if (check) {
		fetch(url, {
			method : "POST"			  
		}).then(function(res) {
			return res.text();
		}).then(function(res) {			  
			if(res==="OK") {	  
				let follow_item_el = document.querySelector("#follow_item_"+index);				  
				follow_item_el.innerHTML = `<button onclick="follow(false, ${userId}, ${index})" class="following_btn">팔로잉</button>`;
			}
		});
	} else {
		fetch(url, {
			method : "DELETE"			  
		}).then(function(res) {
			return res.text();
		}).then(function(res) {
			if(res==="OK") {
				let follow_item_el = document.querySelector("#follow_item_"+index);
				follow_item_el.innerHTML = `<button onclick="follow(false, ${userId}, ${index})" class="follow_btn">팔로우</button>`;
			}
		});
	}
}