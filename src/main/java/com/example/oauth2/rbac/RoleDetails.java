package com.example.oauth2.rbac;

import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;
import java.util.Collection;

public interface RoleDetails extends Serializable {

    String getRoleName();

    Collection<? extends GrantedAuthority> getAuthorities();
}
