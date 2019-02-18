package com.example.oauth2.rbac;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface RoleRepository extends JpaRepository<Role,Integer> {

    @Transactional
    @Modifying
    @Query(value = "update Role r set r.name = :name where r.id = :id")
    void renameRole(String name, int id);

    //获取用户拥有组
    @Query(value = "select r.id, r.name from role r join role_member rm on rm.roleid=r.id where rm.userid=:userId",nativeQuery = true)
    List<Role> findRoleByUser(int userId);

    //从组里删除用户
    @Transactional
    @Modifying
    @Query(value = "delete from role_member where userid = :userId and roleid = :roleId",nativeQuery = true)
    int deleteUserFromRole(int userId,int roleId);


    //添加用户到组
    @Transactional
    @Modifying
    @Query(value = "insert into role_member (userid,roleid) values (:userId,:roleId)",nativeQuery = true)
    void addUserToRole(int userId,int roleId);

    //添加组权限
    @Transactional
    @Modifying
    @Query(value = "insert into role_authority(roleid,authorityid) values (:role,:auth)",nativeQuery = true)
    void addRoleAuthority(int role,int auth);

    //移除组权限
    @Transactional
    @Modifying
    @Query(value = "delete from role_authority where roleid = :role and authorityid = :auth",nativeQuery = true)
    void removeRoleAuthority(int role,int auth);


}
