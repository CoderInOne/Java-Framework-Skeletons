package data_mapping.tiny_mapper.exec;

import java.lang.reflect.Method;
import java.sql.SQLException;

public interface Executor {
    Object query(Method method, Object[] args) throws SQLException;
}
