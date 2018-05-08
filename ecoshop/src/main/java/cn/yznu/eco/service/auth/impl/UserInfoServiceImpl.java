package cn.yznu.eco.service.auth.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.yznu.eco.dao.impl.BaseDaoImpl;
import cn.yznu.eco.pojo.auth.Permission;
import cn.yznu.eco.pojo.auth.Role;
import cn.yznu.eco.pojo.auth.RolePermission;
import cn.yznu.eco.pojo.auth.User;
import cn.yznu.eco.pojo.auth.UserRole;
import cn.yznu.eco.service.auth.UserInfoService;
import cn.yznu.eco.utils.PSWUtil;

@Service
@Transactional
public class UserInfoServiceImpl extends BaseDaoImpl implements UserInfoService {

  @Override
  public List<Role> findRoles(String username) {
    // TODO Auto-generated method stub
    List<UserRole> userRoles = new ArrayList<UserRole>();
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("username", username);
    userRoles = getListByHQL("from UserRole where user.username=:username", map);
    List<Role> roles = new ArrayList<Role>();
    for (UserRole userRole : userRoles) {
      roles.add(userRole.getRole());
    }
    return roles;
  }

  @Override
  public List<Permission> findPermissions(String username) {
    // TODO Auto-generated method stub
    List<Role> roles = findRoles(username);
    List<Permission> permissions = new ArrayList<Permission>();
    for (Role role : roles) {
      Map<String, Object> map = new HashMap<String, Object>();
      map.put("roleName", role.getRoleName());
      List<RolePermission> rolePermissions =
          getListByHQL("from RolePermission where role.roleName=:roleName", map);
      for (RolePermission rolePermission : rolePermissions) {
        Boolean i = true;
        for (Permission permission : permissions) {
          if (permission.getPermissionName() == rolePermission.getPermission()
              .getPermissionName()) {
            i = false;
          }
        }
        if (i) {
          permissions.add(rolePermission.getPermission());
        }
      }
    }
    return permissions;
  }

  @Override
  public User findByUsername(String username) {
    // TODO Auto-generated method stub
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("username", username);
    return getSingleByHQL("from User where username=:username", map);
  }

  @Override
  public void set_Role(User user, Role role) {
    // TODO Auto-generated method stub
    UserRole userRole = new UserRole();
    userRole.setUser(user);
    userRole.setRole(role);
    save(userRole);
  }

  @Override
  public Boolean verifyUser(User user) {
    // TODO Auto-generated method stub
    Boolean boo = true;
    User acquireUser = getSingleByHQL("from User where username = '" + user.getUsername()+"'");
    if (acquireUser == null) {
      boo = false;
    }
    return boo;
  }

}
