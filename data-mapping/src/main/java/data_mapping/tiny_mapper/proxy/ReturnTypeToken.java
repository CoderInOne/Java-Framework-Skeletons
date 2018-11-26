package data_mapping.tiny_mapper.proxy;

import java.util.Collection;

public class ReturnTypeToken {
    private Class<?> type;
    private Class<?> paramType;
    private boolean isCollection;

    private ReturnTypeToken(Class<?> type, Class<?> paramType, boolean isCollection) {
        this.type = type;
        this.paramType = paramType;
        this.isCollection = isCollection;
    }

    private ReturnTypeToken(Class<?> type, Class<?> paramType) {
        this.type = type;
        this.paramType = paramType;
    }

    public static ReturnTypeToken create(Class<?> returnType, String typeName) {
        boolean isCollection = Collection.class.isAssignableFrom(returnType);
        Class<?> type = returnType;
        Class<?> paramType = isCollection ? ReflectionUtil.getClass(typeName) : null;
        return new ReturnTypeToken(type, paramType, isCollection);
    }

    public boolean isCollection() {
        return isCollection;
    }

    public Class<?> getType() {
        return type;
    }

    public Class<?> getParamType() {
        return paramType;
    }
}
