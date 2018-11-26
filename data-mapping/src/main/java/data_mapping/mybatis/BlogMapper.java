package data_mapping.mybatis;

import data_mapping.Blog;
import data_mapping.BlogExt;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BlogMapper {
    Blog findById(int id);
    BlogExt findByIdExt(int id);
    Blog findByTitleAndUrl(@Param("title") String title, @Param("url") String url);
    List<Blog> blogs(@Param("id_list") List<Integer> idList, @Param("title") String title);
}
