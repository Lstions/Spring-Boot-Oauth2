package com.example.oauth2.rbac;

import java.util.Collection;
import java.util.Map;

public interface EntityConvertor {

    Collection<Authority> extractAuthority(Map<String,?> map);

    Collection<User> extractUser(Map<String,?> map);

    Collection<Role> extractRole(Map<String,?> map);
}
