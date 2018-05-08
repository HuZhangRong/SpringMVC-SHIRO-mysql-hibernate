package cn.yznu.eco.pojo.auth;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

/**
 * 权限表
 * 
 * @author ZhouKaiDong
 *
 */
@Entity
@Table
public class Permission extends IDEntity implements java.io.Serializable {
  private String permissionName;// 权限名称
  private String permissionCode;// 权限编码
  private List<RolePermission> rolePermissions = new ArrayList<RolePermission>();

  public String getPermissionName() {
    return permissionName;
  }

  public void setPermissionName(String permissionName) {
    this.permissionName = permissionName;
  }

  public String getPermissionCode() {
    return permissionCode;
  }

  public void setPermissionCode(String permissionCode) {
    this.permissionCode = permissionCode;
  }

  @OneToMany(targetEntity = RolePermission.class, mappedBy = "permission")
  @LazyCollection(LazyCollectionOption.EXTRA)
  public List<RolePermission> getRolePermissions() {
    return rolePermissions;
  }

  public void setRolePermissions(List<RolePermission> rolePermissions) {
    this.rolePermissions = rolePermissions;
  }
}
