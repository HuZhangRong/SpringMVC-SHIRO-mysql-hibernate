package cn.yznu.eco.pojo.auth;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
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
public class RolePermission extends IDEntity implements java.io.Serializable {
  private Role role;// 角色
  private Permission permission;// 权限

  @ManyToOne(fetch = FetchType.EAGER, targetEntity = Role.class)
  @JoinColumn(name = "role_id")
  public Role getRole() {
    return role;
  }

  public void setRole(Role role) {
    this.role = role;
  }

  @ManyToOne(fetch = FetchType.EAGER, targetEntity = Permission.class)
  @JoinColumn(name = "permission_id")
  public Permission getPermission() {
    return permission;
  }

  public void setPermission(Permission permission) {
    this.permission = permission;
  }

}
