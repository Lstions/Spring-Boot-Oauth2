package com.example.oauth2.rbac;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public abstract class UserUtils {

    public static final String USER_NAME="name";
    public static final String USER_PWD="pwd";
    public static final String USER_AUTH="authorities";
    public static final String USER_GROUP="groups";
    public static final String AUTH_SPACE=",";

    public static User createUser(Map<String,String> params, PasswordEncoder encoder){
        String uName=params.get(USER_NAME);
        String uPwd=params.get(USER_PWD);
        String uAuth=params.get(USER_AUTH);
        String uGroup=params.get(USER_GROUP);
        boolean useGroup= uGroup==null? false:true;

        if (uName==null||uPwd==null||uAuth==null){
            throw new IllegalArgumentException("bad arguments");
        }

        String[] strAuth= uAuth.split(AUTH_SPACE);

        Collection<SimpleGrantedAuthority> auths= new ArrayList<>();

        if (strAuth.length>0){
            for (String item:
                 strAuth) {
                 auths.add(new SimpleGrantedAuthority(item));
            }
        }
        else {
            auths.add(new SimpleGrantedAuthority(uAuth));
        }

        User user=new User(uName,encoder.encode(uPwd),auths);

        return user;
    }
}
