package com.example.oauth2.rbac;

import java.util.Collection;

public interface AuthorityManager{

    Authority addAuthority(Authority auth);

    void deleteAuthority(Authority auth);

    //根据id获取Authority
    Authority findAuthorityById(Authority auth);

    //获取所有Authority
    Collection<Authority> getAllAuthority();

    //获取role的权限
    Collection<Authority> findRoleAuthority(Role role);

    //获取user的权限
    Collection<Authority> findUserAuthority(User user);

    //获取user的所有权限（包含user自己的权限和user所在role的权限）
    Collection<Authority> findUserAllAuthority(User user);
}
