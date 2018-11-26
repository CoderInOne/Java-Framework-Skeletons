package data_mapping.tiny_mapper.session;

public interface Session {
    <T> T getMapper(Class<T> clz);
}
