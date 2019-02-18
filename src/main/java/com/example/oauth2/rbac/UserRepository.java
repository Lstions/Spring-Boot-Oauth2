package com.example.oauth2.rbac;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface UserRepository extends JpaRepository<User,Integer> {


    User findUserByName(String name);

    @Query(value = "update User set password = :newPwd where password = :oldPwd and name = :name ")
    int changePassword(String name,String oldPwd,String newPwd);

    @Query(value = "select u.id, u.name, u.password, u.enabled from user u join role_member rm on rm.userid = u.id where rm.roleid = :roleId",nativeQuery = true)
    List<User> findUserInRole(int roleId);

    @Modifying
    @Transactional
    @Query(value = "insert into user_authority(userid,authorityid) values (:userid,:authid)",nativeQuery = true)
    void addUserAuthority(int userid,int authid);

    @Modifying
    @Transactional
    @Query(value = "delete from user_authority where userid=:userid and authorityid=:authid",nativeQuery = true)
    void deleteUserAuthority(int userid,int authid);

}
