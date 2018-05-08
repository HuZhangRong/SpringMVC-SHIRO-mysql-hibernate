package cn.yznu.eco.utils.auth;

import java.util.HashSet;
import java.util.Set;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import cn.yznu.eco.pojo.auth.Permission;
import cn.yznu.eco.pojo.auth.Role;
import cn.yznu.eco.pojo.auth.User;
import cn.yznu.eco.service.auth.UserInfoService;
import cn.yznu.eco.service.auth.impl.UserInfoServiceImpl;

/**
 * Realm模块
 * 
 * @author ZhouKaiDong
 *
 */
public class UserRealm extends AuthorizingRealm {

  // 用户对应的角色信息与权限信息都保存在数据库中，通过userInfoService获取数据
  // private UserInfoServiceImpl userInfoService = new UserInfoServiceImpl();
  @Autowired
  UserInfoService userInfoService;

  /**
   * 提供用户信息返回权限信息
   */
  @Override
  protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {

    String username = (String) principals.getPrimaryPrincipal();
    SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
    // 根据用户名查询当前用户拥有的角色
    Set<Role> roles = new HashSet<>(userInfoService.findRoles(username));
    Set<String> roleNames = new HashSet<String>();
    for (Role role : roles) {
      roleNames.add(role.getRoleName());
    }
    // 将角色名称提供给info
    authorizationInfo.setRoles(roleNames);
    // 根据用户名查询当前用户权限
    Set<Permission> permissions = new HashSet<>(userInfoService.findPermissions(username));
    Set<String> permissionNames = new HashSet<String>();
    for (Permission permission : permissions) {
      permissionNames.add(permission.getPermissionName());
    }
    
    System.out.println("获取的权限列表"+permissionNames);
    // 将权限名称提供给info
    authorizationInfo.setStringPermissions(permissionNames);

    return authorizationInfo;
  }

  /**
   * 提供账户信息返回认证信息
   */
  @Override
  protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token)
      throws AuthenticationException {
    String username = (String) token.getPrincipal();
    User user = userInfoService.findByUsername(username);
    if (user == null) {
      // 用户名不存在抛出异常
      throw new UnknownAccountException();
    }
    if (user.getStatus() == 0) {
      // 用户被管理员锁定抛出异常
      throw new LockedAccountException();
    }
    SimpleAuthenticationInfo authenticationInfo =
        new SimpleAuthenticationInfo(user.getUsername(), user.getPassword(),
            ByteSource.Util.bytes(new PasswordHelper().getCredentialsSalt(user)), getName());
    return authenticationInfo;
  }
}
