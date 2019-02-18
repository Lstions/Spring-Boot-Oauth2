package com.example.oauth2.rbac;

import com.example.oauth2.Oauth2ApplicationTests;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@FixMethodOrder(value = MethodSorters.NAME_ASCENDING)
public class AuthorityManagerImplTest extends Oauth2ApplicationTests {

    @Autowired
    private AuthorityManagerImpl authorityManager;

    @Autowired
    private RoleManager roleManager;

    @Autowired
    private UserManagerImpl userManager;

    public static List<Authority> authorities=new ArrayList<>();
    public static User user;
    public static Role role;

    @Test
    public void A1addAuthority() {
        for (int i=0;i<50;i++){
            authorities.add(new Authority("authTest"+i));
        }
        authorityManager.addAuthority(authorities);
    }

    @Test
    public void A2findAuthorityById() {
        Authority auth=authorities.get(10);
        Authority authGet=authorityManager.findAuthorityById(auth);

        Assert.assertEquals(auth.getId(),authGet.getId());
        Assert.assertEquals(auth.getAuthority(),authGet.getAuthority());
    }

    @Test
    public void A3getAllAuthority() {
        List<Authority> authsTmp=new ArrayList<>(authorityManager.getAllAuthority());

        Assert.assertEquals(authsTmp.size(),authorities.size());
    }

    @Test
    public void A4findRoleAuthority() {
        role= (Role) roleManager.createRole(new Role("AuthTestRole",authorities));
        List<Authority> authtmp=new ArrayList<>(authorityManager.findRoleAuthority(role));

        Assert.assertEquals(authorities.size(),authtmp.size());
    }

    @Test
    public void A5findUserAuthority() {
        user= (User) userManager.createUser(new User("AuthTestUser","123123",authorities));
        List<Authority> authtmp=new ArrayList<>( authorityManager.findUserAuthority(user));

        Assert.assertEquals(authorities.size(),authtmp.size());
    }

    @Test
    public void A6findUserAllAuthority() {
        List<Authority> auth=new ArrayList<>( authorityManager.findUserAllAuthority(user));

        Assert.assertNotNull(auth);
        Assert.assertTrue(auth.size()>0);
    }

    @Test
    public void A7deleteAuthority() {
        userManager.deleteUser(user);
        roleManager.deleteRole(role);
        authorityManager.deleteAuthority(authorities);
    }

//    @Test
//    public void merge() {
//    }
}