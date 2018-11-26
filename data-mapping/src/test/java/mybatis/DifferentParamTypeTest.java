package mybatis;

import data_mapping.BlogExt;
import data_mapping.mybatis.BlogMapper;
import data_mapping.mybatis.SessionLoader;
import org.junit.Test;

import java.io.IOException;

// bad trial
public class DifferentParamTypeTest {
    @Test
    public void test() throws IOException {
        BlogMapper mapper = SessionLoader.load().getMapper(BlogMapper.class);
        BlogExt<Float> blogExt = mapper.findByIdExt(1);
        BlogExt<Integer> blogExt1 = mapper.findByIdExt(2);
        System.out.println(blogExt);
        System.out.println(blogExt1);
    }
}
