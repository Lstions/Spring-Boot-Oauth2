package com.example.oauth2.rbac;

import java.util.Collection;
import java.util.Map;

public class DefaultEntityConvertor implements EntityConvertor {


    @Override
    public Collection<Authority> extractAuthority(Map<String, ?> map) {
        return null;
    }

    @Override
    public Collection<User> extractUser(Map<String, ?> map) {
        return null;
    }

    @Override
    public Collection<Role> extractRole(Map<String, ?> map) {
        return null;
    }
}
