package com.example.oauth2;

import com.example.oauth2.rbac.AuthorityManagerImplTest;
import com.example.oauth2.rbac.RoleManagerImplTest;
import com.example.oauth2.rbac.UserDetailsManagerImplTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        AuthorityManagerImplTest.class,
        RoleManagerImplTest.class,
        UserDetailsManagerImplTest.class
})
public class SuiteTest {
}
