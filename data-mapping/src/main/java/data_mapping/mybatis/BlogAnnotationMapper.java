package data_mapping.mybatis;

import data_mapping.Blog;
import org.apache.ibatis.annotations.Select;

public interface BlogAnnotationMapper {
    @Select("select * from blog where id = #{id}")
    Blog findById(int id);
}
