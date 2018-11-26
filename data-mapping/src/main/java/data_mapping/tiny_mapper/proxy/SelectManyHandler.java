package data_mapping.tiny_mapper.proxy;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SelectManyHandler<T> extends AbstractResultSetHandler<T> {
    private ReturnTypeToken typeToken;

    public SelectManyHandler(ReturnTypeToken typeToken) {
        this.typeToken = typeToken;
    }

    @Override
    public T handle(ResultSet rs) {
        try {
            List list = new ArrayList<>();
            while (rs.next()) {
                Object ret = map(rs, typeToken.getParamType(),
                        typeToken.getParamType().newInstance());
                list.add(ret);
            }
            return (T) list;
        } catch (SQLException
                | IllegalAccessException
                | InstantiationException e) {
            throw new RuntimeException(e);
        }
    }
}
