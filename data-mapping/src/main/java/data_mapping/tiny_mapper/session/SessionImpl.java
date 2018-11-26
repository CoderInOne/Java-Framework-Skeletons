package data_mapping.tiny_mapper.session;

import data_mapping.tiny_mapper.proxy.MapperProxyFactory;

import java.lang.reflect.Proxy;

class SessionImpl implements Session {
    private SessionBuilder sessionBuilder;

    public SessionImpl(SessionBuilder sessionBuilder) {
        this.sessionBuilder = sessionBuilder;
    }

    @Override
    public <T> T getMapper(Class<T> clz) {
        MapperProxyFactory mapperProxyFactory = sessionBuilder.getRegistry().get(clz);
        return (T) Proxy.newProxyInstance(SessionImpl.class.getClassLoader(),
                new Class[] {clz}, mapperProxyFactory.createMapper());
    }
}
