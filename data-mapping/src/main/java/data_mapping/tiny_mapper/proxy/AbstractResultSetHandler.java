package data_mapping.tiny_mapper.proxy;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;

abstract class AbstractResultSetHandler<T> implements ResultSetHandler<T> {
    Object map(ResultSet rs, Class<?> clz, Object ret) throws SQLException, IllegalAccessException {
        Set<Field> fields = collectDeclaredFields(clz);

        // meta data of ResultSet is information of table columns
        // we should keep them to decide whether our field matches them
        ResultSetMetaData metaData = rs.getMetaData();
        List<String> columnList = new ArrayList<>();
        for (int i = 1; i <= metaData.getColumnCount(); i++) {
            columnList.add(metaData.getColumnName(i));
        }

        for (Field f : fields) {
            // no such column, so ignore it
            if (!columnList.contains(f.getName())) {
                continue;
            }
            Object object = rs.getObject(f.getName());

            f.setAccessible(true);
            f.set(ret, object);
        }

        return ret;
    }

    // collect all declared fields including parents'
    private Set<Field> collectDeclaredFields(Class<?> clz) {
        Set<Field> fieldSet = new HashSet<>();
        doCollectDeclaredFields(clz, fieldSet);
        return fieldSet;
    }

    private void doCollectDeclaredFields(Class<?> clz, Set<Field> fieldSet) {
        Field[] fields = clz.getDeclaredFields();
        fieldSet.addAll(Arrays.asList(fields));

        Class<?> superclass = clz.getSuperclass();
        if (!Object.class.equals(superclass)) {
            doCollectDeclaredFields(superclass, fieldSet);
        }
    }
}
