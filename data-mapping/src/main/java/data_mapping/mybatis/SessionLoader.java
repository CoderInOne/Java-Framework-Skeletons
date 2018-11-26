package data_mapping.mybatis;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

public class SessionLoader {
    public static SqlSession load() throws IOException {
        // load xml files
        String resource = "mybatis/mybatis-config.xml";
        InputStream inputStream = SessionLoader.class
                .getClassLoader()
                .getResourceAsStream(resource);

        SqlSessionFactory sessionFactory =
                new SqlSessionFactoryBuilder().build(inputStream);
        return sessionFactory.openSession();
    }
}