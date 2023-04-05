/**
 *  좋아요 like heart
 */
async function onFeedLoad(imageId){
	let msg = await heart_click(imageId);
	if(msg==="ok") {
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