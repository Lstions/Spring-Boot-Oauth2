package com.example.oauth2.rbac;

import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Collection;

public interface UserManager extends UserDetailsService {

    /**
     * Create a new user with the supplied details.
     */
    User createUser(User user);
    void createUser(Collection<User> users);

    /**
     * Update the specified user.
     */
    void updateUser(User user);
    void updateUser(Collection<User> users);

    void deleteUser(User user);
    void deleteUser(Collection<User> users);

    void deleteUserAuthority(User user, Collection<Authority> authorities);

    void addUserAuthority(User user,Collection<Authority> authorities);

    void changePassword(String oldPassword, String newPassword);

    boolean userExists(User user);

    User loadUserByUserId(int uid);
}
