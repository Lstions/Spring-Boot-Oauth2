package com.example.oauth2.rbac;

import com.example.oauth2.Oauth2ApplicationTests;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

@FixMethodOrder(value = MethodSorters.NAME_ASCENDING)
public class UserDetailsManagerImplTest extends Oauth2ApplicationTests {

    @Autowired
    private UserManagerImpl userManager;

    @Autowired
    private AuthorityManagerImpl authorityManager;

    @Autowired
    private PasswordEncoder encoder;

    public static List<User> users=new ArrayList<>();


    @Test
    public void A1createUser() {

        int len=100;
        final CountDownLatch countDownLatch=new CountDownLatch(30);
        for (int i=0;i<30;i++){

            MyThread mt=new MyThread();
            mt
                    .setCountDownLatch(countDownLatch)
                    .setEncoder(new BCryptPasswordEncoder())
                    .setUserManager(userManager)
                    .setIndex(i*len)
                    .setLen(len);
            Thread t=new Thread(mt);
            t.start();
        }

        try {
            countDownLatch.await();
            System.out.println("finished");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void A2updateUser() {
        String pwd=encoder.encode("test");
        for (User user :users){
            user.setPassword(pwd);
        }

        userManager.updateUser(users);
    }


    @Test
    public void A3changePassword() {
        //SecurityContextHolder.getContext().setAuthentication();
    }

    @Test
    public void A4userExists() {
        boolean isExists= userManager.userExists(users.get(0));
        Assert.assertTrue(isExists);
    }

    @Test
    public void A5loadUserByUsername() {
         UserDetails userDetails= userManager.loadUserByUsername("UserTest0");
         Assert.assertNotNull(userDetails.getUsername());
         Assert.assertNotNull(userDetails.getPassword());
    }

    @Test
    public void A6addUserAuthority(){

    }

    @Test
    public void A7deleteUserAuthority(){

    }

    @Test
    public void B1deleteUser() {
        userManager.deleteUser(users);
    }
}

class MyThread implements Runnable{

    private PasswordEncoder encoder;
    private UserManager userManager;
    private CountDownLatch countDownLatch;

    private int index;
    private int len;

    public int getIndex() {
        return index;
    }

    public MyThread setIndex(int index) {
        this.index = index;
        return this;
    }

    public int getLen() {
        return len;
    }

    public MyThread setLen(int len) {
        this.len = len;
        return this;
    }

    public PasswordEncoder getEncoder() {
        return encoder;
    }

    public MyThread setEncoder(PasswordEncoder encoder) {
        this.encoder = encoder;
        return this;
    }

    public UserManager getUserManager() {
        return userManager;
    }

    public MyThread setUserManager(UserManager userManager) {
        this.userManager = userManager;
        return this;
    }

    public CountDownLatch getCountDownLatch() {
        return countDownLatch;
    }

    public MyThread setCountDownLatch(CountDownLatch countDownLatch) {
        this.countDownLatch = countDownLatch;
        return this;
    }

    @Override
    public void run() {
        List<User> u=new ArrayList<>();
        for (int i=index;i<index+len;i++){

            String pwd=encoder.encode("123456"+i);

            u.add(new User("UserTest"+i,pwd,null));
        }
        synchronized (UserDetailsManagerImplTest.users){

            UserDetailsManagerImplTest.users.addAll(u);
        }
        userManager.createUser(u);
        countDownLatch.countDown();
    }
}