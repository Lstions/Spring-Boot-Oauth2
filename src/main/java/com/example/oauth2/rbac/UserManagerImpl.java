package com.example.oauth2.rbac;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Service
public class UserManagerImpl implements UserManager {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthorityManager authorityManager;


    @Override
    public User createUser(User user) {
        //TODO 验证user
        User userDetails= userRepository.save(user);

        if (userDetails.getAuthorities()!=null&&userDetails.getAuthorities().size()!=0){
            addUserAuthority(userDetails, (Collection<Authority>) user.getAuthorities());
        }
        return userDetails;
    }

    public void createUser(Collection<User> users){
        //TODO 验证users
        userRepository.saveAll(users);
        userRepository.flush();
    }

    @Override
    public void updateUser(User user) {

        userRepository.saveAndFlush(user);

    }

    public void updateUser(Collection<User> users){

        int i=0;
        for (User user:users){
            if (user.getId()==0){
                String errMsg=String.format("Index %d, Id Error: user id is 0",i);
                throw new IllegalArgumentException(errMsg);
            }
            i++;
        }

        createUser(users);
    }


    @Override
    public void deleteUser(User user) {

        //TODO 验证user

        userRepository.deleteById(user.getId());
    }

    @Transactional
    public void deleteUser(Collection<User> users){
        //TODO 验证users
        userRepository.deleteAll(users);
        userRepository.flush();
    }


    @Override
    public void changePassword(String oldPassword, String newPassword) throws AuthenticationException {
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null){
            //TODO changePassword 异常处理
        }

        userRepository.changePassword(authentication.getName(),oldPassword,newPassword);

    }

    @Override
    public boolean userExists(User user){
        //TODO 验证user
        if (user.getId()==0){
            throw new IllegalArgumentException("user id illegal");
        }

        return userRepository.existsById(user.getId());
    }

    public void addUserAuthority(User user,Collection<Authority> authorities){
        //TODO 性能优化
        int uid=user.getId();
        for (Authority authority:authorities){
            userRepository.addUserAuthority(uid,authority.getId());
        }
    }

    public void deleteUserAuthority(User user,Collection<Authority> authorities){
        //TODO 性能优化
        int uid=user.getId();
        for (Authority authority:authorities){
            userRepository.deleteUserAuthority(uid,authority.getId());
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user=userRepository.findUserByName(username);

        if (user==null){
            throw new UsernameNotFoundException("用户不存在");
        }

        Collection<Authority> r= authorityManager.findUserAllAuthority(user);
        user.setAuthorities(r);

        return  user;
    }

    @Override
    public User loadUserByUserId(int uid) {

        User user=userRepository.findById(uid).get();

        if (user==null){
            throw new UsernameNotFoundException("用户不存在");
        }

        Collection<Authority> r= authorityManager.findUserAllAuthority(user);
        user.setAuthorities(r);


        return user;
    }
}
