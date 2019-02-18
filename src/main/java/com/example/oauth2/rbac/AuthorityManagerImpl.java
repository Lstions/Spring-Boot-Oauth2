package com.example.oauth2.rbac;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
public class AuthorityManagerImpl implements AuthorityManager {

    @Autowired
    private AuthorityRepository authRepository;

    @Autowired
    private RoleManager roleManager;


    @Value("${roleprefix}")
    private String rolePrefix;

    @Override
    public Authority addAuthority(Authority auth) {
        return authRepository.save(auth);
    }

    @Transactional
    public Collection<Authority> addAuthority(Collection<Authority> auths){
        authRepository.saveAll(auths);
        return auths;
    }

    @Override
    public void deleteAuthority(Authority auth) {

        authRepository.delete(auth);
    }

    @Transactional
    public void deleteAuthority(Collection<Authority> auths) {
        authRepository.deleteAll(auths);
    }

    @Override
    public Authority findAuthorityById(Authority auth) {
        return authRepository.findById(auth.getId()).get();
    }

    @Override
    public Collection<Authority> getAllAuthority() {

        return  authRepository.findAll();
    }

    @Override
    public Collection<Authority> findRoleAuthority(Role role) {
         return authRepository.findRoleAuthority(role.getId());

    }


    @Override
    public Collection<Authority> findUserAuthority(User user) {

        return authRepository.findUserAuthority(user.getId());
    }


    @Override
    public Collection<Authority> findUserAllAuthority(User user){
        int uid=user.getId();

        List<Authority> authority = authRepository.findUserRoleAuthority(uid);
        Collection<Authority> roleAuthority=getUserRole(uid);
        //现在 数据库查询去重复权限
        if (roleAuthority!=null){

            authority.addAll(roleAuthority);
        }

        return authority;
    }

    //利用键值对去重复权限
    public Collection<Authority> merge(Collection<Authority>... list){
        Map<Integer,Authority> result=new HashMap<>();

        for (Collection<Authority> j :list){

            for (Authority i:j){

                int key=i.getId();

                if (!result.containsKey(key)){
                    String name=i.getAuthority().toUpperCase();
                    i.setName(name);

                    result.put(key,i);
                }
            }
        }
        return result.values();
    }

    public Collection<Authority> getUserRole(int uid){
        List<Role> roles= roleManager.findUserRoles(uid);

        if (roles.size()<=0){
            return null;
        }

        List<Authority> authorities =new ArrayList<>();
        for (Role role:roles){
            String name=rolePrefix+role.getRoleName().toUpperCase();
            authorities.add(new Authority(name));
        }

        return authorities;

    }


}
