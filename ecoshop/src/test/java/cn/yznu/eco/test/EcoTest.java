package cn.yznu.eco.test;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import cn.yznu.eco.pojo.auth.Permission;
import cn.yznu.eco.pojo.auth.Role;
import cn.yznu.eco.pojo.auth.RolePermission;
import cn.yznu.eco.pojo.auth.User;
import cn.yznu.eco.pojo.auth.UserRole;
import cn.yznu.eco.service.auth.UserInfoService;
import cn.yznu.eco.service.auth.impl.UserInfoServiceImpl;

@ContextConfiguration(locations = {"classpath:spring-mvc.xml", "classpath:spring-hibernate.xml",
    "classpath:spring-beans.xml", "classpath:spring-shiro.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = false)
@Transactional
public class EcoTest {

  @Autowired
  UserInfoService userInfoService;

  @Test
  public void add() {
    // TODO Auto-generated method stub
    UserRole role = new UserRole();
    userInfoService.save(role);
    System.out.println("添加成功！");
  }

  @Test
  public void getList() {
    // TODO Auto-generated method stub
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("username", "admin");
    System.out.println(
        "长度为：" + userInfoService.getListByHQL("from UserRole where User.username=:username", map));
  }
}
