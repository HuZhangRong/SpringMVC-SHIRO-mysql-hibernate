package cn.yznu.eco.pojo.auth;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;


/**
 * 用户 角色 中间表
 * 
 * @author ZhouKaiDong
 *
 */
@Entity
@Table
public class UserRole extends IDEntity implements java.io.Serializable {
  private User user;
  private Role role;

  @ManyToOne(fetch = FetchType.EAGER, targetEntity = User.class)
  @JoinColumn(name = "user_id")
  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  @ManyToOne(fetch = FetchType.EAGER, targetEntity = Role.class)
  // ManyToOne指定了多对一的关系，fetch=FetchType.LAZY属性表示在多的那一方通过延迟加载的方式加载对象(默认不是延迟加载)
  @JoinColumn(name = "role_id")
  public Role getRole() {
    return role;
  }

  public void setRole(Role role) {
    this.role = role;
  }
}
