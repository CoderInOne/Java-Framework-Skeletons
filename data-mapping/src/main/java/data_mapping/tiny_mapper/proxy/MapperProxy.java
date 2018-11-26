package data_mapping.tiny_mapper.proxy;

import data_mapping.tiny_mapper.exec.Executor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.sql.SQLException;

public class MapperProxy implements InvocationHandler {
    private Executor executor;

    MapperProxy(Executor executor) {
        this.executor = executor;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws SQLException {
        // TODO think about mybatis's MapperMethod, it's better?
        // TODO filter object method or default method
        return executor.query(method, args);
    }

}
