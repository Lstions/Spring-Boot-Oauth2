package com.example.oauth2.rbac;

import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

@Entity
@Table(name = "authority")
public class Authority  implements GrantedAuthority{

    private static final long serialVersionUID = 4711604405443319746L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String name;

    @Override
    public String getAuthority() {
        return this.name;
    }

    public int getId() {
        return id;
    }

    public Authority setId(int id) {
        this.id = id;
        return this;
    }

    public Authority setName(String name) {
        this.name = name;
        return this;
    }

    public Authority(){}

    public Authority(String authorityName){
        setName(authorityName);
    }

}
