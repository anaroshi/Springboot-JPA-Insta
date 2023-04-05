/**
 * 스크롤 페이징 - 무한 스크롤 Infinite Scroll
 */

  let page = 0;
  
  function make_feed_box(image) {
	  alert(image);
	  let feed_box = `
		    <div class="photo u-default-box">
	      <header class="photo__header">
	      	<img src="/upload/${image.user.profileImage}" onerror="this.onerror=null; this.src='/images/avatar.jpg'" />
	        <img src="/images/avatar.jpg" />
	        <div class="photo_user_info">
		        <span class="photo__username">${image.user.username }</span>
		        <span class="photo__location">${image.location }</span>
	        </div>
	      </header>
	      <div class="photo_post_image">
		      <img src="/upload/${image.postImage}" />
	      </div>
	      <div class="photo__info">
	          <div class="photo__actions">
	            <span class="photo__action">
				
				if(image.heart == true) {
					<i onClick="onFeedLoad(${image.id})" id="${image.id}" class="fa fa-heart heart heart-clicked"></i>
				} else {
					<i onClick="onFeedLoad(${image.id})" id="${image.id}" class="fa fa-heart-o heart"></i>
				}

	            </span>
	            <span class="photo__action">
	              <i class="fa fa-comment-o"></i>
	            </span>
	          </div>
	          <span class="photo__likes">${image.likeCount} likes</span>
	          <div class="photo_caption">
	          	<span class="photo__username">
	          		${image.user.username }
	          	</span>
		          ${image.caption}
	          </div>
	          <div class="photo_tag">
				<c:forEach items="${image.tags}" var="tag">
		        	#${tag.name}
		        </c:forEach>
		      </div>    
	          <ul class="photo__comments">
	            <li class="photo__comment">
	              <span class="photo__comment-author">serranoarevalo</span> i love this!
	            </li>
	            <li class="photo__comment">
	              <span class="photo__comment-author">serranoarevalo</span> i don't love this!
	            </li>
	          </ul>
	          <span class="photo__date">${image.createDate }</span>
	          <div class="photo__add-comment-container">
	            <textarea placeholder="Add a comment..."></textarea>
	            <i class="fa fa-ellipsis-h"></i>
	          </div>
	      </div>
	    </div>  
	  `;
  		
  		return feed_box;
  }
  
$(window).scroll(function() {
	/*버그 수정 (근사치 계산)*/
    let checkNum = ($(window).scrollTop()) - ($(document).height() - $(window).height());
	if (checkNum < 1 && checkNum > -1) {
    	console.log('hi');
        page++;
        load_feed_box(); // 박스 3개씩 로드
    }
});
  
async function load_feed_box(){
	console.log("load_feed_box()");
	// fetch 로 다운로드
	let response = await fetch(`/image/feed/scroll?page=${page}`);
	let images = await response.json();
	
	console.log("images : "+images);
	
	// 3번 실행 필요
	images.forEach(function(image){
		let feed_box = make_feed_box(image);
		console.log("feed_box : "+feed_box);
	    $("#feed").append(feed_box);
	});
}
