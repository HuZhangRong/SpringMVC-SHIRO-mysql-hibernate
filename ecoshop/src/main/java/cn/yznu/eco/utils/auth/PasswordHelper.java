package cn.yznu.eco.utils.auth;

import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

import cn.yznu.eco.pojo.auth.User;

public class PasswordHelper {
  private RandomNumberGenerator randomNumberGenerator = new SecureRandomNumberGenerator();
  private String algorithmName = "md5";
  private final int hashIterations = 2;

  /**
   * 生成用户 专属 “盐” key
   * @param user
   * @return
   */
  public String getCredentialsSalt(User user) {
    // TODO Auto-generated method stub
    return user.getUsername() + user.getSalt();
  }

  public User encryptPassword(User user) {
    // User对象包含最基本的字段Username和Password
    user.setSalt(randomNumberGenerator.nextBytes().toHex());
    // 将用户的注册密码经过散列算法替换成一个不可逆的新密码保存进数据，散列过程使用了盐
    String newPassword = new SimpleHash(algorithmName, user.getPassword(),
        ByteSource.Util.bytes(getCredentialsSalt(user)), hashIterations).toHex();
    user.setPassword(newPassword);
    return user;
  }
}
