package cn.yznu.eco.pojo.auth;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.GenericGenerator;

/*
 * 标注为@MappedSuperclass的类将不是一个完整的实体类，
 * 他将不会映射到数据库表，但是他的属性都将映射到其子类的数据库字段中。
 */
@MappedSuperclass
public class IDEntity {
	
	private String id;

	@Id
	@GeneratedValue(generator = "paymentableGenerator")
	@GenericGenerator(name = "paymentableGenerator", strategy = "uuid")
	@Column(name ="ID",nullable=false,length=32)
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
