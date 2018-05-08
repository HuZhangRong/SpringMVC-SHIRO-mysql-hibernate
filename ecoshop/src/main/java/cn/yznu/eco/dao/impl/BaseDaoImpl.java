package cn.yznu.eco.dao.impl;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import cn.yznu.eco.dao.IBaseDao;

@Repository
@Transactional
public class BaseDaoImpl implements IBaseDao {

  private static final Logger logger = Logger.getLogger(BaseDaoImpl.class);

  @Autowired
  @Qualifier("jdbcTemplate")
  private JdbcTemplate jdbcTemplate;

  @Autowired
  @Qualifier("namedParameterJdbcTemplate")
  private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  @Autowired
  @Qualifier("sessionFactory")
  private SessionFactory sessionFactory;

  public Session getSession() {
    // 事务必须是开启的(Required)，否则获取不到
    //    sessionFactory.openSession();
    //	  Session session =  sessionFactory.openSession();    
    //      Transaction tran = session.beginTransaction(); 
    return sessionFactory.getCurrentSession();
  }

  /**
   * <createCriteria> <创建单一Criteria对象>
   * 
   * @param entityClass
   * @return
   */
  private <T> Criteria createCriteria(Class<T> entityClass) {
    Criteria criteria = getSession().createCriteria(entityClass);
    return criteria;
  }

  /**
   * <createCriteria> <创建Criteria对象，有排序功能>
   * 
   * @param entityClass
   * @param isAsc
   * @param criterions
   * @return
   */
  private <T> Criteria createCriteria(Class<T> entityClass, boolean isAsc,
      Criterion... criterions) {
    Criteria criteria = createCriteria(entityClass, criterions);
    if (isAsc) {
      criteria.addOrder(Order.asc("asc"));
    } else {
      criteria.addOrder(Order.desc("desc"));
    }
    return criteria;
  }

  /**
   * <createCriteria> <创建Criteria对象，有排序功能>
   * 
   * @param entityClass
   * @param isAsc
   * @param criterions
   * @return
   */
  private <T> Criteria createCriteria(Class<T> entityClass, String propertyName, boolean isAsc,
      Criterion... criterions) {
    Criteria criteria = createCriteria(entityClass, criterions);
    if (isAsc) {
      criteria.addOrder(Order.asc(propertyName));
    } else {
      criteria.addOrder(Order.desc(propertyName));
    }
    return criteria;
  }

  /**
   * <createCriteria> <创建Criteria对象带属性比较>
   * 
   * @param entityClass
   * @param criterions
   * @return
   */
  private <T> Criteria createCriteria(Class<T> entityClass, Criterion... criterions) {
    Criteria criteria = getSession().createCriteria(entityClass);
    for (Criterion c : criterions) {
      criteria.add(c);
    }
    return criteria;
  }

  // 实现方法----------------------------------------------------------------------------------------------

  @Override
  public <T> Serializable save(T entity) {
    System.out.println("正在保存实体。。。");
    try {
      Serializable id = getSession().save(entity);
      getSession().flush();
      //getSession().getTransaction().commit();
      if (logger.isDebugEnabled()) {
        logger.debug("保存实体成功," + entity.getClass().getName());
      }
      return id;
    } catch (RuntimeException e) {
      logger.error("保存实体异常", e);
      throw e;
    }
  }

  @Override
  public <T> void batchSave(List<T> entities) {
    for (int i = 0; i < entities.size(); i++) {
      getSession().save(entities.get(i));
      if (i % 20 == 0) {
        // 20个对象后才清理缓存，写入数据库
        getSession().flush();
        getSession().clear();
      }
    }
    // 最后清理一下----防止大于20小于40的不保存
    getSession().flush();
    getSession().clear();
  }

  @Override
  public <T> void saveOrUpdate(T entity) {
    try {
      getSession().saveOrUpdate(entity);
      getSession().flush();
      if (logger.isDebugEnabled()) {
        logger.debug("添加或更新成功," + entity.getClass().getName());
      }
    } catch (RuntimeException e) {
      logger.error("添加或更新异常", e);
      throw e;
    }
  }

  @Override
  public <T> void delete(T entity) {
    try {
      getSession().delete(entity);
      getSession().flush();
      if (logger.isDebugEnabled()) {
        logger.debug("删除成功," + entity.getClass().getName());
      }
    } catch (RuntimeException e) {
      logger.error("删除异常", e);
      throw e;
    }
  }

  @Override
  public <T> void batchDelete(Collection<T> entities) {
    for (T t : entities) {
      delete(t);
    }
  }

  @Override
  public <T> void deleteById(Class<T> entityName, Serializable id) {
    delete(get(entityName, id));
    getSession().flush();
  }

  @Override
  public <T> void deleteAll(Class<T> entityName) {
    String hql = "delete from " + entityName.getName();
    Query query = this.getSession().createQuery(hql);
    query.executeUpdate();
  }

  @Override
  public <T> void update(T entity) {
    try {
      getSession().update(entity);
      getSession().flush();
      if (logger.isDebugEnabled()) {
        logger.debug("更新成功," + entity.getClass().getName());
      }
      // closeSession();
    } catch (RuntimeException e) {
      logger.error("更新异常", e);
      throw e;
    }
  }

  @Override
  public <T> void batchUpdate(List<T> entitys) {
    for (int i = 0; i < entitys.size(); i++) {
      getSession().update(entitys.get(i));
      if (i % 20 == 0) {
        // 20个对象后才清理缓存，写入数据库
        getSession().flush();
        getSession().clear();
      }
    }
    // 最后清理一下----防止大于20小于40的不保存
    getSession().flush();
    getSession().clear();
  }

  @Override
  public int updateBySqlString(String sql) {
    Query querys = getSession().createSQLQuery(sql);
    return querys.executeUpdate();
  }

  @Override
  public <T> T load(Class<T> entityName, Serializable id) {
    return get(entityName, id);
  }

  @Override
  public <T> List<T> loadAll(Class<T> entityClass) {
    Criteria criteria = createCriteria(entityClass);
    return criteria.list();
  }

  @Override
  public <T> T get(Class<T> entityName, Serializable id) {
    return (T) getSession().get(entityName, id);
  }

  @Override
  public <T> T getEntity(Class<T> entityName, Serializable id) {
    T t = (T) getSession().get(entityName, id);
    if (t != null) {
      getSession().flush();
    }
    return t;
  }

  @Override
  public <T> List<T> getAll(Class<T> entityName) {
    String hql = "from " + entityName.getName();
    Query query = getSession().createQuery(hql);
    return query.list();
  }

  @Override
  public <T> T getSingleByProperty(Class<T> entityName, String propertyName, Object value) {
    T t = null;
    String hql = "from " + entityName.getName() + " where " + propertyName + "=:value";
    Query query = this.getSession().createQuery(hql);
    query.setParameter("value", value);
    List<T> list = query.list();
    if (list.size() == 1) {
      t = list.get(0);
    }
    return t;
  }

  @Override
  public <T> T getSingleByHQL(String hqlString) {
    Query query = this.getSession().createQuery(hqlString);
    return (T) query.uniqueResult();
  }

  @Override
  public <T> T getSingleByHQL(String hqlString, Object... values) {
    Query query = this.getSession().createQuery(hqlString);
    if (values != null) {
      for (int i = 0; i < values.length; i++) {
        query.setParameter(i, values[i]);
      }
    }
    return (T) query.uniqueResult();
  }

  @Override
  public <T> T getSingleByHQL(String hqlString, Map<String, Object> values) {
    Query query = this.getSession().createQuery(hqlString);
    if (values != null && values.size() > 0) {
      for (Entry<String, Object> p : values.entrySet()) {
        query.setParameter(p.getKey(), p.getValue());
      }
    }
    return (T) query.uniqueResult();
  }

  @Override
  public <T> T getSingleBySQL(String sqlString, Class<T> entityName) {
    Query query = this.getSession().createSQLQuery(sqlString)
        .setResultTransformer(Transformers.aliasToBean(entityName));
    return (T) query.uniqueResult();
  }

  @Override
  public <T> T getSingleBySQL(String sqlString, Class<T> entityName, Object... values) {
    Query query = this.getSession().createSQLQuery(sqlString)
        .setResultTransformer(Transformers.aliasToBean(entityName));
    if (values != null) {
      for (int i = 0; i < values.length; i++) {
        query.setParameter(i, values[i]);
      }
    }
    return (T) query.uniqueResult();
  }

  @Override
  public <T> T getSingleBySQL(String sqlString, Class<T> entityName, Map<String, Object> values) {
    Query query = this.getSession().createSQLQuery(sqlString)
        .setResultTransformer(Transformers.aliasToBean(entityName));
    if (values != null && values.size() > 0) {
      for (Entry<String, Object> p : values.entrySet()) {
        query.setParameter(p.getKey(), p.getValue());
      }
    }
    return (T) query.uniqueResult();
  }

  @Override
  public <T> List<T> getListByProperty(Class<T> entityName, String propertyName, Object value) {
    String hql = "from " + entityName.getName() + " where " + propertyName + "=:value";
    Query query = this.getSession().createQuery(hql);
    query.setParameter("value", value);
    return query.list();
  }

  @Override
  public <T> List<T> getListByHQL(String hqlString) {
    Query query = this.getSession().createQuery(hqlString);
    return query.list();
  }

  @Override
  public <T> List<T> getListByHQL(String hqlString, Object... values) {
    Query query = this.getSession().createQuery(hqlString);
    if (values != null) {
      for (int i = 0; i < values.length; i++) {
        query.setParameter(i, values[i]);
      }
    }
    return query.list();
  }

  @Override
  public <T> List<T> getListByHQL(String hqlString, Map<String, Object> values) {
    Query query = this.getSession().createQuery(hqlString);
    if (values != null && values.size() > 0) {
      for (Entry<String, Object> p : values.entrySet()) {
        query.setParameter(p.getKey(), p.getValue());
      }
    }
    return query.list();
  }

  @Override
  public <T> List<T> getListBySQL(String sqlString, Class<T> entityName) {
    Query query = this.getSession().createSQLQuery(sqlString)
        .setResultTransformer(Transformers.aliasToBean(entityName));
    return query.list();
  }

  @Override
  public <T> List<T> getListBySQL(String sqlString, Class<T> entityName, Object... values) {
    Query query = this.getSession().createSQLQuery(sqlString)
        .setResultTransformer(Transformers.aliasToBean(entityName));
    if (values != null) {
      for (int i = 0; i < values.length; i++) {
        query.setParameter(i, values[i]);
      }
    }
    return query.list();
  }

  @Override
  public <T> List<T> getListBySQL(String sqlString, Class<T> entityName,
      Map<String, Object> values) {
    Query query = this.getSession().createSQLQuery(sqlString)
        .setResultTransformer(Transformers.aliasToBean(entityName));
    if (values != null && values.size() > 0) {
      for (Entry<String, Object> p : values.entrySet()) {
        query.setParameter(p.getKey(), p.getValue());
      }
    }
    return query.list();
  }

  /**
   * 通过sql查询语句查找对象
   * 
   * @param <T>
   * @param query
   * @return
   */
  public <T> List<T> getListbySql(final String sql) {
    Query querys = getSession().createSQLQuery(sql);
    return querys.list();
  }

  @Override
  public <T> List<T> getByPropertyisOrder(Class<T> entityClass, String propertyName, Object value,
      boolean isAsc) {
    Assert.hasText(propertyName);
    return createCriteria(entityClass, propertyName, isAsc, Restrictions.eq(propertyName, value))
        .list();
  }

  @Override
  public <T> List<T> getByExample(String entityName, Object exampleEntity) {
    Assert.notNull(exampleEntity, "Example entity must not be null");
    Criteria executableCriteria = (entityName != null ? getSession().createCriteria(entityName)
        : getSession().createCriteria(exampleEntity.getClass()));
    executableCriteria.add(Example.create(exampleEntity));
    return executableCriteria.list();
  }

  @Override
  public Map<Object, Object> getHashMapbyHql(String hql) {
    Query query = getSession().createQuery(hql);
    List list = query.list();
    Map<Object, Object> map = new HashMap<Object, Object>();
    for (Iterator iterator = list.iterator(); iterator.hasNext();) {
      Object[] tm = (Object[]) iterator.next();
      map.put(tm[0].toString(), tm[1].toString());
    }
    return map;
  }

  @Override
  public <T> int getCount(Class<T> entityName) {
    String hql = "from " + entityName.getName();
    Query query = this.getSession().createQuery(hql);
    return query.list().size();
  }

  @Override
  public Long getCountByHql(String hql, Object... values) {
    Query query = this.getSession().createQuery(hql);
    if (values != null) {
      for (int i = 0; i < values.length; i++) {
        query.setParameter(i, values[i]);
      }
    }
    return (Long) query.uniqueResult();
  }

  @Override
  public Long getCountByHql(String hql, Map<String, Object> values) {
    Query query = this.getSession().createQuery(hql);
    if (values != null && values.size() > 0) {
      for (Entry<String, Object> p : values.entrySet()) {
        query.setParameter(p.getKey(), p.getValue());
      }
    }
    return (Long) query.uniqueResult();
  }


  @Override
  public <T> PageResults<T> getPageByFetchedHql(String hql, String countHql, int pageNo,
      int pageSize, Object... values) {
    PageResults<T> retValue = new PageResults<T>();
    Query query = this.getSession().createQuery(hql);
    if (values != null) {
      for (int i = 0; i < values.length; i++) {
        query.setParameter(i, values[i]);
      }
    }
    int currentPage = pageNo > 1 ? pageNo : 1;
    retValue.setCurrentPage(currentPage);
    retValue.setPageSize(pageSize);
    if (countHql == null) {
      ScrollableResults results = query.scroll();
      results.last();
      retValue.setTotalCount(results.getRowNumber() + 1);// 设置总记录数
    } else {
      Long count = getCountByHql(countHql, values);
      retValue.setTotalCount(count.intValue());
    }
    retValue.resetPageNo();
    List<T> itemList =
        query.setFirstResult((currentPage - 1) * pageSize).setMaxResults(pageSize).list();
    if (itemList == null) {
      itemList = new ArrayList<T>();
    }
    retValue.setResults(itemList);

    return retValue;
  }

  @Override
  public <T> PageResults<T> getPageByFetchedHql(String hql, String countHql, int pageNo,
      int pageSize, Map<String, Object> values) {
    PageResults<T> retValue = new PageResults<T>();
    Query query = this.getSession().createQuery(hql);
    if (values != null && values.size() > 0) {
      for (Entry<String, Object> p : values.entrySet()) {
        query.setParameter(p.getKey(), p.getValue());
      }
    }
    int currentPage = pageNo > 1 ? pageNo : 1;
    retValue.setCurrentPage(currentPage);
    retValue.setPageSize(pageSize);
    if (countHql == null) {
      ScrollableResults results = query.scroll();
      results.last();
      retValue.setTotalCount(results.getRowNumber() + 1);// 设置总记录数
    } else {
      Long count = getCountByHql(countHql, values);
      retValue.setTotalCount(count.intValue());
    }
    retValue.resetPageNo();
    List<T> itemList =
        query.setFirstResult((currentPage - 1) * pageSize).setMaxResults(pageSize).list();
    if (itemList == null) {
      itemList = new ArrayList<T>();
    }
    retValue.setResults(itemList);

    return retValue;
  }

  @Override
  public <T> List<T> getPageList(DetachedCriteria dc, int firstResult, int maxResult) {
    Criteria criteria = dc.getExecutableCriteria(getSession());
    criteria.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
    criteria.setFirstResult(firstResult);
    criteria.setMaxResults(maxResult);
    return criteria.list();
  }

  /**
   * 根据实体名字获取唯一记录
   * 
   * @param propertyName
   * @param value
   * @return
   */
  public <T> T findUniqueByProperty(Class<T> entityClass, String propertyName, Object value) {
    Assert.hasText(propertyName);
    return (T) createCriteria(entityClass, Restrictions.eq(propertyName, value)).uniqueResult();
  }

  /**
   * 按属性查找对象列表.
   */
  public <T> List<T> findByProperty(Class<T> entityClass, String propertyName, Object value) {
    Assert.hasText(propertyName);
    return (List<T>) createCriteria(entityClass, Restrictions.eq(propertyName, value)).list();
  }

  @Override
  public List<Map<String, Object>> findForJdbc(String sql, Object... objs) {
    return this.jdbcTemplate.queryForList(sql, objs);
  }

  @Override
  public Map<String, Object> findOneForJdbc(String sql, Object... objs) {
    try {
      return this.jdbcTemplate.queryForMap(sql, objs);
    } catch (EmptyResultDataAccessException e) {
      return null;
    }
  }

  @Override
  public <T> boolean contains(T entity) {
    return getSession().contains(entity);
  }

  @Override
  public Integer executeHql(String hqlString, Object... values) {
    Query query = this.getSession().createQuery(hqlString);
    if (values != null) {
      for (int i = 0; i < values.length; i++) {
        query.setParameter(i, values[i]);
      }
    }
    return query.executeUpdate();
  }

  @Override
  public Integer executeHql(String hqlString, List<Object> values) {
    Query query = getSession().createQuery(hqlString);
    if (values != null && values.size() > 0) {
      for (int i = 0; i < values.size(); i++) {
        query.setParameter(i, values.get(i));
      }
    }
    return query.executeUpdate();
  }

  @Override
  public Integer executeHql(String hqlString, Map<String, Object> values) {
    Query query = this.getSession().createQuery(hqlString);
    if (values != null && values.size() > 0) {
      for (Entry<String, Object> p : values.entrySet()) {
        query.setParameter(p.getKey(), p.getValue());
      }
    }
    return query.executeUpdate();
  }

  @Override
  public Integer executeSql(String sqlString, Object... values) {
    return this.jdbcTemplate.update(sqlString, values);
  }

  @Override
  public Integer executeSql(String sqlString, List<Object> values) {
    return this.jdbcTemplate.update(sqlString, values);
  }

  @Override
  public Integer executeSql(String sqlString, Map<String, Object> values) {
    return this.namedParameterJdbcTemplate.update(sqlString, values);
  }

  @Override
  public Object executeSqlReturnKey(String sql, Map<String, Object> param) {
    Object keyValue = null;
    try {
      KeyHolder keyHolder = new GeneratedKeyHolder();
      SqlParameterSource sqlp = new MapSqlParameterSource(param);
      this.namedParameterJdbcTemplate.update(sql, sqlp, keyHolder);
      keyValue = keyHolder.getKey().longValue();
    } catch (Exception e) {
      keyValue = null;
    }
    return keyValue;
  }

  @Override
  public <T> void refresh(T entity) {
    getSession().refresh(entity);
  }

  @Override
  public <T> List<T> executeQuery(String hql, Object[] values) {
    Query query = getSession().createQuery(hql);
    for (int i = 0; (values != null) && (i < values.length); i++) {
      query.setParameter(i, values[i]);
    }
    return query.list();
  }



  @Override
  public Long getCountForJdbc(String sql) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public List<Map<String, Object>> findForJdbc(String sql, int page, int rows) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public <T> List<T> findObjForJdbc(String sql, int page, int rows, Class<T> clazz) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public List<Map<String, Object>> findForJdbcParam(String sql, int page, int rows,
      Object... objs) {
    // TODO Auto-generated method stub
    return null;
  }

}
