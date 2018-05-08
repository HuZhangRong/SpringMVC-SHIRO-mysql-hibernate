package cn.yznu.eco.utils.auth;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import cn.yznu.eco.pojo.auth.User;

public class UserUtil {

  
  public User get_User() {
    // TODO Auto-generated method stub
    Subject subject = SecurityUtils.getSubject();
    User user = (User) subject.getSession().getAttribute("user");   
    return user;
  }
}
