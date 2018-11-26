package mybatis;

import data_mapping.Blog;
import data_mapping.BlogExt;
import data_mapping.mybatis.BlogMapper;
import data_mapping.mybatis.SessionLoader;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class ColumnFieldsMatchTest {
    private BlogMapper mapper;

    @Before
    public void setUp() throws Exception {
        mapper = SessionLoader.load().getMapper(BlogMapper.class);
        assertNotNull(mapper);
    }

    @Test
    public void columnMoreThanFields() {
        Blog blog = mapper.findById(1);
        assertNotNull(blog);
    }

    @Test
    public void fieldsMoreThanColumns() {
        // get all columns info from ResultSetMetaData, from which we'll know
        // how many columns we have and their name
        BlogExt blog = mapper.findByIdExt(1);
        assertNotNull(blog);
        System.out.println(blog);
    }
}
