package com.example.oauth2.rbac;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AuthorityRepository extends JpaRepository<Authority,Integer> {

    @Query(value = "select a.id, a.name from authority a join user_authority ua on a.id=ua.authorityid where ua.userid=:id",nativeQuery = true)
    List<Authority> findUserAuthority(int id);

    @Query(value = "select a.id, a.name from authority a join role_authority ra on a.id=ra.authorityid where ra.roleid=:id",nativeQuery = true)
    List<Authority> findRoleAuthority(int id);

    //根据用户id 获取组的所有权限
    @Query(value = "select id, name from authority where id in ( select ra.authorityid from role_authority ra join role_member rm on rm.roleid=ra.roleid where rm.userid=:id union select authorityid from user_authority where userid=:id)",nativeQuery = true)
    List<Authority> findUserRoleAuthority(int id);


}
