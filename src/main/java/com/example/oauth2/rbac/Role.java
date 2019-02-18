package com.example.oauth2.rbac;

import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "role")
public class Role  implements RoleDetails {

    private static final long serialVersionUID = -3138990019422800130L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    @Transient
    private Collection<? extends GrantedAuthority> authorities;

    @Column(name="name")
    @Override
    public String getRoleName() {
        return this.name;
    }

    public Role setRoleName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    public Role setAuthorities(Collection<? extends GrantedAuthority> authorities) {
        this.authorities = authorities;
        return this;
    }

    public int getId() {
        return id;
    }

    public Role setId(int id) {
        this.id = id;
        return this;
    }

    public Role(){}

    public Role(String roleName,Collection<? extends GrantedAuthority> auth){
        this.name=roleName;
        this.authorities= auth;
    }

}
