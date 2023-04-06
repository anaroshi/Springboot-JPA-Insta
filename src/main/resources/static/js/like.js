/**
 *  좋아요 like heart
 *  카운트
 */
async function onFeedLoad(imageId){
	let msg = await heart_click(imageId);
	let likeCount = $("#photo_likes_count_"+imageId).text();
	if(msg==="like") {
		$("#photo_likes_count_"+imageId).text(Number(likeCount)+1);
		$("#"+imageId).toggleClass("heart-clicked fa-heart fa-heart-o");
	} else if(msg==="unLike") {
		$("#photo_likes_count_"+imageId).text(Number(likeCount)-1);
		$("#"+imageId).toggleClass("heart-clicked fa-heart fa-heart-o");
	} else {
		alert(msg);
	}
}

async function heart_click(imageId) {
	let response = await fetch(`/image/like/${imageId}`, {
		method:"POST"
	});
	let msg = await response.text();
	return msg;	
}


//$(document).ready(function() {
//	$(".heart").on("click", function(e) {
//		console.log(e.target.id);
//		
//		let imageId = $(this).attr("id");
//		let msg = heart_click(imageId);
//		
//		if (msg==="ok") {
//			$(target).toggleClass("heart-clicked fa-heart fa-heart-o");
//		}
//	});	
//});