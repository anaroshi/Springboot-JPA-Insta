-- DATABASE 생성
CREATE DATABASE insta CHARACTER SET utf8 DEFAULT COLLATE utf8_general_ci;

-- DATABASE 사용 선언
use insta;

-- 한글 설정 보기
show variables like 'c%';

drop table follow;
drop table image;
drop table tag;
drop table user;
drop table user_images;
commit;

