package data_mapping.tiny_mapper.proxy;

import java.sql.ResultSet;

public interface ResultSetHandler<T> {
    T handle(ResultSet rs);

    static <T> ResultSetHandler<T> selectBy(ReturnTypeToken returnType) {
        if (returnType.getType().equals(Void.class)) {
            return new VoidHandler<>();
        }

        if (returnType.isCollection()) {
            return new SelectManyHandler<>(returnType);
        }
        else {
            return new SelectOneHandler<>(returnType);
        }
    }
}
