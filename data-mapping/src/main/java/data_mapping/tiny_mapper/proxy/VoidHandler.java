package data_mapping.tiny_mapper.proxy;

import java.sql.ResultSet;

public class VoidHandler<Void> implements ResultSetHandler<Void> {
    @Override
    public Void handle(ResultSet rs) {
        return null;
    }
}
