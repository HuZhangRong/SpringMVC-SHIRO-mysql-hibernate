package cn.yznu.eco.pojo.auth;

import java.util.ArrayList;
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
 * 用户表
 * 
 * @author ZhouKaiDong
 *
 */
@Entity
@Table
public class User extends IDEntity implements java.io.Serializable {
  private static final long serialVersionUID = 1L;
  private String username;// 用户名
  private String password;// 用户密码
  private Short status = 1;// 状态   1：正常, 0：禁用
  private String salt;// 加密时会用到的“盐”
  private List<UserRole> userRoles = new ArrayList<UserRole>();

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public Short getStatus() {
    return status;
  }

  public void setStatus(Short status) {
    this.status = status;
  }

  public String getSalt() {
    return salt;
  }

  public void setSalt(String salt) {
    this.salt = salt;
  }

  @OneToMany(targetEntity = UserRole.class, mappedBy = "user")
  @LazyCollection(LazyCollectionOption.EXTRA)
  public List<UserRole> getUserRoles() {
    return userRoles;
  }

  public void setUserRoles(List<UserRole> userRoles) {
    this.userRoles = userRoles;
  }
}
