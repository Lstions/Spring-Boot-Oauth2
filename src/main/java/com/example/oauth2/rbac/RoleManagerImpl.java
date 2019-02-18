package com.example.oauth2.rbac;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.sql.DataSource;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Service
public class RoleManagerImpl implements RoleManager {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private DataSource dataSource;


    @Override
    public List<Role> findAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    public List<Role> findUserRoles(int user) {
        return roleRepository.findRoleByUser(user);
    }

    @Override
    public Collection<User> findUsersInRole(Role role) {

        List<User> lu = userRepository.findUserInRole(role.getId());

        return  lu;

    }

    @Override
    public void addUserToRole(int user, Role role) {


        roleRepository.addUserToRole(user, role.getId());
    }

    @Override
    public void removeUserFromRole(int user, Role role) {


        roleRepository.deleteUserFromRole(user, role.getId());
    }

    @Override
    public Role createRole(Role role) {

        Role rr = roleRepository.save(role);

        //添加权限
        Collection auths= role.getAuthorities();
        if (auths!=null && auths.size()>0){
            addRoleAuthority(rr,auths);
        }
        return rr;
    }

    @Override
    public void deleteRole(Role role) {
        //roleRepository.deleteById(role.getId());
        roleRepository.delete(role);
    }


    @Override
    public void renameRole(Role role, String newName) {
        //verify(role);

        role.setRoleName(newName);
        //roleRepository.renameRole(newName, role.getId());
        roleRepository.save(role);
    }


    @Override
    public void removeRoleAuthority(Role role, Authority authority) {
        //verify(role);
        //TODO SQL性能优化
        roleRepository.removeRoleAuthority(role.getId(), authority.getId());


    }

//    public void removeRoleAuthority(Role role, Collection<Authority> authority) {
//        try {
//            int roleid=role.getId();
//            MySQLConnection connection= new Mysqlconn
//
//            PreparedStatement preparedStatement=new com.mysql.jdbc.PreparedStatement
//                    (connection,"delete from role_authority where roleid = ? and authorityid = ?");
//            for (Authority a :authority){
//                preparedStatement.setInt(0,roleid);
//                preparedStatement.setInt(1,a.getId());
//                preparedStatement.addBatch();
//            }
//            preparedStatement.execute();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//    }



    @Override
    public void addRoleAuthority(Role role, Authority authority) {
        //verify(role);

        addRoleAuthority(role, Arrays.asList(authority));
    }

    //批量添加组权限
    public void addRoleAuthority(Role role, Collection<Authority> list) {
        //verify(role);
        //TODO SQL性能优化
        int roleid = role.getId();

        for (Authority a : list) {
            roleRepository.addRoleAuthority(roleid, a.getId());
        }
    }
//
//    public void verify(Role role){
//        if (role.getId()==0){
//            throw new IllegalArgumentException("role id is illegal");
//        }
//    }

}
