package data_mapping.tiny_mapper;

import data_mapping.Blog;

import java.util.List;

public interface BlogTinyMapper {
    Blog findById(int id);
    List<Blog> findAll();
}
