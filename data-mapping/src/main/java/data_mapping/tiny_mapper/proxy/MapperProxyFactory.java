package data_mapping.tiny_mapper.proxy;

import data_mapping.tiny_mapper.exec.BaseExecutor;
import data_mapping.tiny_mapper.exec.Executor;
import data_mapping.tiny_mapper.session.SessionBuilder;

public class MapperProxyFactory {
    private Executor executor;

    public MapperProxyFactory(SessionBuilder builder) {
        this.executor = new BaseExecutor(builder);
    }

    // TODO create mapper or proxy?
    // protected T newInstance(MapperProxy<T> mapperProxy)
    // public T newInstance(SqlSession sqlSession)
    // in
    public MapperProxy createMapper() {
        return new MapperProxy(executor);
    }
}
