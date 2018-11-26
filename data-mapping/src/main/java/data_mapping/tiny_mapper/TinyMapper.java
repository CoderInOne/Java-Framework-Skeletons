package data_mapping.tiny_mapper;

import data_mapping.Blog;
import data_mapping.tiny_mapper.session.Session;
import data_mapping.tiny_mapper.session.SessionBuilder;

import java.sql.*;
import java.util.List;

public class TinyMapper {
    public static void main(String[] args) {
        Session session = SessionBuilder.newBuilder()
                .configConnection("tiny_mapper/configConnection.yml")
                .registerMapper("tiny_mapper/sql.yml")
                .build();

        BlogTinyMapper mapper = session.getMapper(BlogTinyMapper.class);

        Blog blog = mapper.findById(1);
        System.out.println("select one:" + blog);

        List<Blog> blogs = mapper.findAll();
        System.out.println("select many:" + blogs);

        mapper.toString();
    }
}
