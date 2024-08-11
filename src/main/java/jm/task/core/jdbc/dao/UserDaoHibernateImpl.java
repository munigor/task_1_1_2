package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.SqlUtil;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;

public class UserDaoHibernateImpl implements UserDao {
    private final SessionFactory sessionFactory = Util.getSessionFactory();
    private final Class<User> className = User.class;

    static {
        System.out.println(UserDaoHibernateImpl.class.getName());
    }

    public UserDaoHibernateImpl() {
    }

    @Override
    public void createUsersTable() {
        executeSql(SqlUtil.createTableSql(className));
    }

    @Override
    public void dropUsersTable() {
        executeSql(SqlUtil.dropTableSql(className));
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        User user = new User(name, lastName, age);
        Session session = sessionFactory.openSession();
        session.save(user);
        session.close();
    }

    @Override
    public void removeUserById(long id) {
        Session session = sessionFactory.openSession();
        User user = new User();
        user.setId(id);
        Transaction transaction = session.beginTransaction();
        session.delete(user);
        transaction.commit();
        session.close();
    }

    @Override
    public List<User> getAllUsers() {
        Session session = sessionFactory.openSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        var criteria = cb.createQuery(className);
        criteria.from(className);
        List<User> users = session.createQuery(criteria).getResultList();
        session.close();
        return users;
    }

    @Override
    public void cleanUsersTable() {
        executeSql(SqlUtil.truncateTableSql(className));
    }

    private void executeSql(String sql) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session
            .createSQLQuery(sql)
            .executeUpdate();
        transaction.commit();
        session.close();
    }
}
