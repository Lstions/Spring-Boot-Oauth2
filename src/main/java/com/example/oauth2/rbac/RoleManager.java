package com.example.oauth2.rbac;

import java.util.Collection;
import java.util.List;

public interface RoleManager {

    List<Role> findAllRoles();

    List<Role> findUserRoles(int user);

    Collection<User> findUsersInRole(Role role);

    void addUserToRole(int user,Role role);

    void removeUserFromRole(int user,Role role);

    Role createRole(Role role);

    void deleteRole(Role role);

    void renameRole(Role role,String newName);

    void removeRoleAuthority(Role role,Authority authority);

    void addRoleAuthority(Role role,Authority authority);

    void addRoleAuthority(Role role, Collection<Authority> list);
}
