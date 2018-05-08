package cn.yznu.eco.pojo.auth;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

/**
 * 角色表
 * 
 * @author ZhouKaiDong
 *
 */
@Entity
@Table
public class Role extends IDEntity implements java.io.Serializable {

  private String roleName;// 角色名称
  private String roleCode;// 角色编码
  private List<UserRole> userRoles = new ArrayList<UserRole>();
  private List<RolePermission> rolePermissions = new ArrayList<RolePermission>();

  public String getRoleName() {
    return roleName;
  }

  public void setRoleName(String roleName) {
    this.roleName = roleName;
  }

  public String getRoleCode() {
    return roleCode;
  }

  public void setRoleCode(String roleCode) {
    this.roleCode = roleCode;
  }

  @OneToMany(targetEntity = UserRole.class, mappedBy = "role")
  @LazyCollection(LazyCollectionOption.EXTRA)
  public List<UserRole> getUserRoles() {
    return userRoles;
  }

  public void setUserRoles(List<UserRole> userRoles) {
    this.userRoles = userRoles;
  }

  @OneToMany(targetEntity = RolePermission.class, mappedBy = "role")
  @LazyCollection(LazyCollectionOption.EXTRA)
  public List<RolePermission> getRolePermissions() {
    return rolePermissions;
  }

  public void setRolePermissions(List<RolePermission> rolePermissions) {
    this.rolePermissions = rolePermissions;
  }

}
