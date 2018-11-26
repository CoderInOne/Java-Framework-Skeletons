package mybatis;

import org.apache.ibatis.parsing.GenericTokenParser;
import org.apache.ibatis.parsing.TokenHandler;
import org.junit.Test;

public class GenericTokenParserTest {
    @Test
    public void parse() {
        String sql = "select * from blog where id = \\#{id}";
        GenericTokenParser genericTokenParser = new GenericTokenParser("#{", "}", new TokenHandler() {
            @Override
            public String handleToken(String content) {
                return "?";
            }
        });
        String parsedSql = genericTokenParser.parse(sql);
        System.out.println(parsedSql);
    }
}
