package net.YeYongQuan.person.push.utils;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

/**
 * Created by qiujuer
 * on 2017/2/17.
 */
public class Hib {
    // 全局SessionFactory
    private static SessionFactory sessionFactory;

    static {
        // 静态初始化sessionFactory
        init();
    }

    private static void init() {
        // 从hibernate.cfg.xml文件初始化
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure() // configures settings from hibernate.cfg.xml
                .build();
        try {
            // build 一个sessionFactory
            sessionFactory = new MetadataSources(registry)
                    .buildMetadata()
                    .buildSessionFactory();
        } catch (Exception e) {
            e.printStackTrace();
            // 错误则打印输出，并销毁
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }

    /**
     * 获取全局的SessionFactory
     *
     * @return SessionFactory
     */
    public static SessionFactory sessionFactory() {
        return sessionFactory;
    }

    /**
     * 从SessionFactory中得到一个Session会话
     *
     * @return Session
     */
    public static Session session() {
        return sessionFactory.getCurrentSession();
    }

    /**
     * 关闭sessionFactory
     */
    public static void closeFactory() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }


//    接收Hib的事务的接口
    public interface QueryOnly{
        void query(Session session);
    }

    //    接收Hib的事务的接口
    public interface Query<T>{
        T query(Session session);
    }

    public static <T>T query(Query<T>  query){
        Session session = session();
        // 开启事物
        final Transaction transaction = session.beginTransaction();
        T t  = null;
        try {
            t = query.query(session);
            session.getTransaction().commit();

        }catch(Exception e){
            e.printStackTrace();
            try {
                session.getTransaction().rollback();
            }catch (Exception e1){
                e1.printStackTrace();
            }
        }finally{
            session.close();
        }
        return t;
    }

    public static void query(QueryOnly  query){
        Session session = session();
        // 开启事物
        final Transaction transaction = session.beginTransaction();
        try {
            query.query(session);
            session.getTransaction().commit();
        }catch(Exception e){
            e.printStackTrace();
            try {
                session.getTransaction().rollback();
            }catch (Exception e1){
                e1.printStackTrace();
            }
        }finally{
            session.close();
        }
    }
}
