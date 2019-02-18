

-- user
INSERT INTO `user` (`name`,`password`,`enabled`) VALUES 
('abcde','$2a$10$DuPt2LenxVM9Au7cBSRSQ.fFehKvCm4BicGbcTg03WxWgRcg8l0Gm',1),
('test','$2a$10$DuPt2LenxVM9Au7cBSRSQ.fFehKvCm4BicGbcTg03WxWgRcg8l0Gm',1),
('monitor1','$2a$10$DuPt2LenxVM9Au7cBSRSQ.fFehKvCm4BicGbcTg03WxWgRcg8l0Gm',1);


-- authority
INSERT INTO `authority` (`name`) VALUES ('ALT_CLS_STU'),('ALT_CLS_COU'),('GET_CLS_STU'),('GET_CLS_BASEINFO');
insert into authority (name) values ('USER');


-- role
insert into role (name) values ('STUDENT'),('MONITOR');
insert into role_authority (roleid,authorityid) values(1,4),(1,1);
insert into role_authority (roleid,authorityid) values(2,1),(2,2),(2,3);

-- user-authority

insert into user_authority (userid,authorityid) values (1,2),(1,1);


-- user-role
insert into role_member (roleid,userid) values
(1,1),
(2,2);


 select ra.authorityid from role_authority ra join role_member rm on rm.roleid=ra.roleid where rm.userid=1 
            union select authorityid from user_authority where userid=1