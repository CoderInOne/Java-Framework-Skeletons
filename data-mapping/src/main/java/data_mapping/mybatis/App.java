package data_mapping.mybatis;

import data_mapping.Blog;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class App {
    public static void main(String[] args) throws IOException {
        // load xml files
        String resource = "mybatis/mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);

        // create proxy
        SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        sessionFactory.getConfiguration()
                .addMapper(BlogAnnotationMapper.class);
        SqlSession session = sessionFactory.openSession();

        BlogMapper blogMapper = session.getMapper(BlogMapper.class);
        BlogAnnotationMapper annoBlogMapper =
                session.getMapper(BlogAnnotationMapper.class);

        // query data
        Blog blog = blogMapper.findByTitleAndUrl("The Old Man and the Sea", "");
        System.out.println(blog);

        Blog blog1 = annoBlogMapper.findById(1);
        System.out.println(blog1);

        System.out.println("blogs:" + blogMapper.blogs(Arrays.asList(1, 2), "The Old Man and the Sea"));

        // close resource
        session.close();
    }
}
