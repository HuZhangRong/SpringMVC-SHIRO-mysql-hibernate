package cn.yznu.eco.dao;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.stereotype.Repository;

import cn.yznu.eco.dao.impl.PageResults;

 
/**
 * @ClassName: IBaseDao 通用接口
 * @Description: TODO hibernate通用接口
 */
@Repository
public interface IBaseDao{
	public Session getSession();
	//添加操作----------------------------------------------------------------------------------------------------------

	/**
	 * <save>
	 * <保存实体>
	 * @param entity 实体
	 * @return
	 */
	public <T> Serializable save(T entity);
    
    /**
     * <batchSave>
     * <批量保存实体>
     * @param entities 实体集
     */
    public <T> void batchSave(List<T> entities);

    /**
     * <saveOrUpdate>
     * <方法作用>
     * @param entity 实体
     */
    public <T> void saveOrUpdate(T entity);
    

    /**
     * <delete>
     * <删除实体>
     * @param entity 实体
     */
    public <T> void delete(T entity); //mmbw

    /**
     * <batchDelete>
     * <批量删除实体>
     * @param entities 实体集
     */
    public <T> void batchDelete(Collection<T> entities);
    
    /**
     * <deleteById>
     * <根据ID删除数据>
     * @param Id 实体id
     */
    public <T> void deleteById(Class<T> entityName, Serializable id);
    
    /**
     * <deleteAll>
     * <删除所有该类型实体>
     * @param entityName 实体类型名称
     */
    public <T> void deleteAll(final Class<T> entityName);

    //修改操作----------------------------------------------------------------------------------------------------------
    
    /**
     * <update>
     * <更新实体>
     * @param entity 实体
     */
    public <T> void update(T entity);
    
    /**
     * <batchUpdate>
     * <批量修改实体>
     * @param entitys 实体集
     */
	public <T> void batchUpdate(List<T> entitys);
	
	/**
	 * <updateBySqlString>
	 * <根据sql更新>
	 * @param sql
	 * @return
	 */
	public int updateBySqlString(String sql);
    
	//查询操作----------------------------------------------------------------------------------------------------------
	
	/**
	 * <load>
	 * <加载实体的load方法>
	 * @param entityName 实体类型名称
	 * @param id 实体的id
	 * @return 查询出来的实体
	 */
    public <T> T load(final Class<T> entityName, Serializable id);
 
    /**
     * <loadAll>
     * <加载全部实体>
     * @param entityClass 实体类型名称
     * @return 实体集
     */
    public <T> List<T> loadAll(final Class<T> entityClass);
    
    /**
     * <get>
     * <查找的get方法>
     * @param entityName 实体类型名称
     * @param id 实体的id
     * @return 查询出来的实体
     */
    public <T> T get(final Class<T> entityName, Serializable id);
    /**
     * <getEntity>
     * <根据实体名称和主键获取实体>
     * @param entityName 实体类名称
     * @param id 实体的主键id
     * @return
     */
    public <T> T getEntity(final Class<T> entityName, Serializable id);
    /**
     * <getAll>
     * <获取该类型所有实体>
     * @param entityName 实体类型名称
     * @return 实体集
     */
    //
    public <T> List<T> getAll(final Class<T> entityName);

    /**
     * <getSingleByProperty>
     * <通过实体类名，属性名称，属性值获取唯一实体>
     * @param entityName 实体类型名称
     * @param propertyName 属性名称
     * @param value 属性值
     * @return 查询实体
     */
	public <T> T getSingleByProperty(final Class<T> entityName,
			String propertyName, Object value);
	
	/**
	 * <getSingleByHQL>
	 * <根据HQL语句查找唯一实体>
	 * @param hqlString HQL语句
	 * @return 查询实体
	 */
	public <T> T getSingleByHQL(String hqlString);
    
    /**
     * <getSingleByHQL>
     * <根据HQL语句查找唯一实体>
     * @param hqlString HQL语句
     * @param values 不定参数的Object数组
     * @return 查询实体
     */
    public <T> T getSingleByHQL(String hqlString, Object... values);
    
    /**
     * <getSingleByHQL>
     * <根据HQL语句查找唯一实体>
     * @param hqlString HQL语句
     * @param values Map键值对
     * @return 查询实体
     */
    public <T> T getSingleByHQL(String hqlString, Map<String, Object> values);
 
    /**
     * <getSingleBySQL>
     * <根据SQL语句查找唯一实体>
     * @param sqlString SQL语句
     * @param entityName 实体类型名称
     * @return 查询实体
     */
    public <T> T getSingleBySQL(String sqlString, final Class<T> entityName);
    
    /**
     * <getSingleBySQL>
     * <根据SQL语句查找唯一实体>
     * @param sqlString SQL语句
     * @param entityName 实体类型名称
     * @param values 不定参数的Object数组
     * @return 查询实体
     */
    public <T> T getSingleBySQL(String sqlString, final Class<T> entityName, Object... values);
    
    /**
     * <getSingleBySQL>
     * <根据SQL语句查找唯一实体>
     * @param sqlString SQL语句]
     * @param entityName 实体类型名称
     * @param values Map键值对
     * @return 查询实体
     */
    public <T> T getSingleBySQL(String sqlString, final Class<T> entityName, Map<String, Object> values);

    /**
     * <getListByProperty>
	 * <通过实体类名，属性名称，属性值获取实体集>
	 * @param entityName 实体类型名称
	 * @param propertyName 属性名称
	 * @param value 属性值
	 * @return 查询多个实体的List集合
	 */
	public <T> List<T> getListByProperty(final Class<T> entityName,
			String propertyName, Object value);
	
	/**
	 * <getListByHQL>
     * <根据HQL语句，得到对应的list>
     * @param hqlString HQL语句
     * @return 查询多个实体的List集合
     */
    public <T> List<T> getListByHQL(String hqlString);
    
    /**
     * <getListByHQL>
     * <根据HQL语句，得到对应的list>
     * @param hqlString HQL语句
     * @param values 不定参数的Object数组
     * @return 查询多个实体的List集合
     */
    public <T> List<T> getListByHQL(String hqlString, Object... values);
    
    /**
     * <getListByHQL>
     * <根据HQL语句，得到对应的list>
     * @param hqlString HQL语句
     * @param values Map键值对
     * @return 查询多个实体的List集合
     */
    public <T> List<T> getListByHQL(String hqlString, Map<String, Object> values);
 
    /**
     * <getListBySQL>
     * <根据SQL语句，得到对应的list>
     * @param sqlString HQL语句
     * @param entityName 实体类型名称
     * @return 查询多个实体的List集合
     */
    public <T> List<T> getListBySQL(String sqlString, final Class<T> entityName);
    
    /**
     * <getListBySQL>
     * <根据SQL语句，得到对应的list>
     * @param sqlString SQL语句
     * @param entityName 实体类型名称
     * @param values 不定参数的Object数组
     * @return 查询多个实体的List集合
     */
    public <T> List<T> getListBySQL(String sqlString, final Class<T> entityName, Object... values);
    
    /**
     * <getListBySQL>
     * <根据SQL语句，得到对应的list>
     * @param sqlString HQL语句
     * @param entityName 实体类型名称
     * @param values Map键值对
     * @return 查询多个实体的List集合
     */
    public <T> List<T> getListBySQL(String sqlString, final Class<T> entityName, Map<String, Object> values);
    
    
    public <T> List<T> getListbySql(final String sql);
    
    
    /**
     * <getByPropertyisOrder>
     * <通过属性称获取实体带排序>
     * @param entityClass 实体类型名称
     * @param propertyName 属性名称
     * @param value 属性值
     * @param isAsc 升（降）序
     * @return 实体集
     */
    public <T> List<T> getByPropertyisOrder(final Class<T> entityClass,
			String propertyName, Object value, boolean isAsc);
    
    /**
     * <getByExample>
     * <根据实体模版查找>
     * @param <T>
     * @param entityName 实体类型名称
     * @param exampleEntity 实体对象
     * @return 实体集
     */
    public <T> List<T> getByExample(final String entityName,
			final Object exampleEntity);
    
    /**
     * <getHashMapbyHql>
     * <通过hql 查询语句查找HashMap对象>
     * @param query hql语句
     * @return
     */
    public Map<Object, Object> getHashMapbyHql(String hql);
    
    
    /**
     * <getCount>
	 * <获取指定实体总记录数>
	 * @param entityName 实体类型名称
	 * @return 该实体类型的记录总数
	 */
	public <T> int getCount(final Class<T> entityName) ;

    /**
     * <getCountByHql>
     * <根据HQL得到记录数>
     * @param hql HQL语句
     * @param values 不定参数的Object数组
     * @return 记录总数
     */
    public Long getCountByHql(String hql, Object... values);
    
    /**
     * <getCountByHql>
     * <根据HQL得到记录数>
     * @param hql HQL语句
     * @param values Map键值对
     * @return 记录总数
     */
    public Long getCountByHql(String hql, Map<String, Object> values);
     
    /**
     * <getCountForJdbc>
     * <使用指定的检索标准检索数据并分页返回数据For JDBC>
     * @param sql sql查询语句
     * @return
     */
    public Long getCountForJdbc(String sql);
    
    /**
     * <getPageByFetchedHql>
     * <HQL分页查询>
     * @param hql HQL语句
     * @param countHql 查询记录条数的HQL语句
     * @param pageNo 下一页
     * @param pageSize 每页的总条数
     * @param values 不定Object数组参数
     * @return PageResults的封装类，里面包含了页码的信息以及查询的数据List集合
     */
    public <T> PageResults<T> getPageByFetchedHql(String hql, String countHql, int pageNo, int pageSize, Object... values);
   
    /**
     * <getPageByFetchedHql>
     * <HQL分页查询>
     * @param hql HQL语句
     * @param countHql 查询记录条数的HQL语句
     * @param pageNo 下一页
     * @param pageSize 一页总条数
     * @param values Map键值对
     * @return PageResults的封装类，里面包含了页码的信息以及查询的数据List集合
     */
    public <T> PageResults<T> getPageByFetchedHql(String hql, String countHql, int pageNo, int pageSize, Map<String, Object> values);
    
    /**
     * <getPageList>
     * <方法作用>
     * @param dc
     * @param firstResult
     * @param maxResult
     * @return
     */
    public <T> List<T> getPageList(DetachedCriteria dc, int firstResult,
			int maxResult);
    
    /**
     * <findForJdbc>
     * <通过JDBC查找对象集合 使用指定的检索标准检索数据返回数据>
     * @param sql sql语句
     * @param objs 对象数组
     * @return 键值对的list集合
     */
    public List<Map<String, Object>> findForJdbc(String sql, Object... objs);
    
    /**
     * <findForJdbc>
     * <通过JDBC查找对象集合,带分页 使用指定的检索标准检索数据并分页返回数据>
     * @param sql sql语句
     * @param page 页码
     * @param rows 每页记录数
     * @return 键值对的list集合
     */
    public List<Map<String, Object>> findForJdbc(String sql, int page, int rows);
    
    /**
     * <findOneForJdbc>
     * <通过JDBC查找对象集合 使用指定的检索标准检索数据返回数据>
     * @param sql sql语句
     * @param objs 对象数组
     * @return map
     */
    public Map<String, Object> findOneForJdbc(String sql, Object... objs);
    
    /**
     * <findObjForJdbc>
     * <通过JDBC查找对象集合,带分页 使用指定的检索标准检索数据并分页返回数据>
     * @param sql sql语句
     * @param page 页码
     * @param rows 每页记录数
     * @param clazz 类型名称
     * @return 实体集
     */
    public <T> List<T> findObjForJdbc(String sql, int page, int rows,
			Class<T> clazz);
    
    /**
     * <findForJdbcParam>
     * <>
     * @param sql
     * @param page
     * @param rows
     * @param objs
     * @return
     */
    public List<Map<String, Object>> findForJdbcParam(String sql, int page,
			int rows, Object... objs);
    
    //其它操作----------------------------------------------------------------------------------------------------------

    /**
     * <contains>
     * <是否包含实体>
     * @param entity 实体
     * @return 是否包含实体
     */
    public <T> boolean contains(T entity);
 
    /**
     * <executeHql>
     * <执行Hql语句>
     * @param hqlString hql语句
     * @param values 不定参数数组
     */
    public Integer executeHql(String hqlString, Object... values); 
    
    /**
     * <executeHql>
     * <执行Hql语句>
     * @param hqlString hql语句
     * @param values
     * @return
     */
    public Integer executeHql(String hqlString, List<Object> values);
    
    /**
     * <executeHql>
     * <执行Hql语句>
     * @param hqlString hql语句
     * @param values Map键值对
     */
    public Integer executeHql(String hqlString, Map<String, Object> values);
     
    /**
     * <executeSql>
     * <执行Sql语句>
     * @param sqlString sql语句
     * @param values 不定参数数组
     */
    public Integer executeSql(String sqlString, Object... values); 
    
    /**
     * <executeSql>
     * <执行Sql语句>
     * @param sqlString sql语句
     * @param values List集合
     * @return
     */
    public Integer executeSql(String sqlString, List<Object> values);
    
    /**
     * <executeSql>
     * <执行Sql语句>
     * @param sqlString sql语句
     * @param values Map键值对
     */
    public Integer executeSql(String sqlString, Map<String, Object> values); 
   
    /**
     * <executeSqlReturnKey>
     * <执行SQL 使用:name占位符,并返回插入的主键值>
     * @param sql
     * @param param
     * @return
     */
    public Object executeSqlReturnKey(String sql, Map<String, Object> param);
    
    /**
     * <refresh>
     * <刷新>
     * @param entity 实体
     */
    public <T> void refresh(T entity);
 
    public <T> List<T> executeQuery(String hql, Object[] values);
}