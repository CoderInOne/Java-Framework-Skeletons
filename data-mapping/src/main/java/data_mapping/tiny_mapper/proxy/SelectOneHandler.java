package data_mapping.tiny_mapper.proxy;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SelectOneHandler<T> extends AbstractResultSetHandler<T> {
    private ReturnTypeToken typeToken;

    public SelectOneHandler(ReturnTypeToken typeToken) {
        this.typeToken = typeToken;
    }

    @Override
    public T handle(ResultSet rs) {
        try {
            rs.next();
            Object ret = typeToken.getType().newInstance();
            return (T) super.map(rs, typeToken.getType(), ret);
        } catch (SQLException
                | IllegalAccessException
                | InstantiationException e) {
            throw new RuntimeException(e);
        }
    }
}
