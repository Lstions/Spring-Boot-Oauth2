package com.example.oauth2.rbac;

import com.example.oauth2.Oauth2ApplicationTests;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@FixMethodOrder(value = MethodSorters.NAME_ASCENDING)
public class RoleManagerImplTest extends Oauth2ApplicationTests {

    @Autowired
    private RoleManagerImpl roleManager;

    @Autowired
    private UserManagerImpl userDetailsManager;

    @Autowired
    private AuthorityManagerImpl authorityManager;

    @Autowired
    private PasswordEncoder encoder;

    public static Role roleDetails;
    public static User userDetails;
    public static Collection<Authority> authorities;


    @Test
    public void A1createRole() {

        Role role = new Role();
        role.setRoleName("TEST_ROLE");
        roleDetails = (Role)roleManager.createRole(role);
        Assert.assertNotNull(roleDetails);


    }

    @Test
    public void A2findAllRoles() {

        List<Role> roles = roleManager.findAllRoles();
        if (roles.size() < 1) {
            Assert.fail();
        }
    }


    @Test
    public void A3addUserToRole() {
        User u=new User("aaaaa",encoder.encode("123456"),null);
        User uu = (User) userDetailsManager.createUser(u);
        userDetails=uu;
        roleManager.addUserToRole(uu.getId(),roleDetails);
    }

    @Test
    public void A4findUsersInRole() {
        Collection<User> us = roleManager.findUsersInRole(roleDetails);

        for (User user:us){

            if (user.getUsername().equals(userDetails.getUsername())){
                return;
            }
        }

        Assert.fail();
    }

    @Test
    public void A5removeUserFromRole() {
        User u= userDetails;
        roleManager.removeUserFromRole(u.getId(),roleDetails);
    }

    @Test
    public void A6renameRole() {

        String oldName = roleDetails.getRoleName();

        roleManager.renameRole(roleDetails, "NEW_TEST_ROLE");
        String newName = roleDetails.getRoleName();

        Assert.assertNotSame(oldName, newName);
    }


    @Test
    public void A7addRoleAuthority() {
        authorities=new ArrayList<>();
        for (int i=0;i<1000;i++){
            authorities.add(new Authority("auth"+i));
        }
        authorityManager.addAuthority(authorities);

        roleManager.addRoleAuthority(roleDetails,authorities);
    }

    @Test
    public void A8findRoleAuthority() {
        Collection<Authority> aus= authorityManager.findRoleAuthority(roleDetails);

        Assert.assertNotSame(aus.size(),authorities.size());
    }

    @Test
    public void A9removeRoleAuthority() {
        for (Authority auth:authorities){
            roleManager.removeRoleAuthority(roleDetails,auth);
        }
    }

    @Test
    public void B1deleteRole() {

        userDetailsManager.deleteUser(userDetails);

        roleManager.deleteRole( roleDetails);

        authorityManager.deleteAuthority(authorities);

    }


}