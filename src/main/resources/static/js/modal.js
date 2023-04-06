/**
 *  모달창
 */
$(".like_popup").click(function (){
	load_like_notification();	
});

$(".like_popup_close button").click(function (){
	$("#modal").removeClass("active");	
});

async function load_like_notification() {
	$("#modal .like_popup_items *").remove();
	
	let response = await fetch("/like/notification");
	let likesList = await response.json();	
	
	likesList.forEach(function(likes){
		let like_box = make_like_notification(likes);
		$("#modal .like_popup_items").append(like_box);
	});
	
	$("#modal").addClass("active");
}

function make_like_notification(likes) {
	let like_box = `
		<div class="like_popup_item">
			<img src="/upload/${likes.user.profileImage}" onerror="this.onerror=null; this.src='/images/avatar.jpg'">
			<p><a href="/user/${likes.user.id}">${likes.user.username}님이 ${likes.image.caption} 사진을 좋아합니다.</a>	</p>
		</div>
	`;	
	return like_box;
}