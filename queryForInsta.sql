-- DATABASE 생성
CREATE DATABASE insta CHARACTER SET utf8 DEFAULT COLLATE utf8_general_ci;

-- DATABASE 사용 선언
use insta;

-- 한글 설정 보기
show variables like 'c%';

-- drop table follow;
-- drop table image;
-- drop table tag;
-- drop table user;
-- drop table user_images;
commit;

select * from user;
select * from image;
select * from tag;
select * from follow;

select id, bio,createDate,email,gender, name, password, phone, profileImage,updateDate,username,website from user where id=1;

select a.id , a.createDate, a.fromUserId, a.toUserId, a.updateDate from follow a left outer join user b on a.fromUserId=b.id where b.id=1;

-- delete from follow where id=38;
-- delete from user where id=4;

select * from follow;

-- delete from tag;
-- delete from image;

select * from image
where userId in (select toUserId from follow where fromUserId = 3); -- userId: 로그인ID

SELECT * FROM image WHERE userId in (SELECT toUserId FROM follow WHERE fromUserId=3);

SELECT * FROM image i 
inner join follow f on i.userId=f.toUserId 
inner join user u on i.userId=u.id
where f.fromUserId=3;

select * from user;
select * from image;
select * from likes;

Insert into likes(userId, imageId) values(3,25);
Insert into likes(userId, imageId) values(3,27);
Insert into likes(userId, imageId) values(3,24);
update likes set createDAte = now(), updateDate=now() where id=2;

-- delete from likes where imageId =24;
SELECT * FROM likes WHERE imageId in (SELECT id FROM image WHERE userId = 1) order by id desc limit 5;