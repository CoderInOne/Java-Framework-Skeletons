package data_mapping.tiny_mapper.session;

import data_mapping.tiny_mapper.TinyMapper;
import data_mapping.tiny_mapper.proxy.MapperProxyFactory;
import data_mapping.tiny_mapper.sql.MapperEntry;
import data_mapping.tiny_mapper.sql.QueryEntry;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class SessionBuilder {
    private String configFile;
    private String[] mapperFiles;

    Connection connection;
    Map<String, QueryEntry> queries;
    Map<Class<?>, MapperProxyFactory> registry;

    private SessionBuilder() {
        this.queries = new HashMap<>();
        this.registry = new HashMap<>();
    }

    public Connection getConnection() {
        return connection;
    }

    public Map<String, QueryEntry> getQueries() {
        return queries;
    }

    public Map<Class<?>, MapperProxyFactory> getRegistry() {
        return registry;
    }

    public static SessionBuilder newBuilder() {
        return new SessionBuilder();
    }

    public SessionBuilder configConnection(String config) {
        this.configFile = config;
        return this;
    }

    public SessionBuilder registerMapper(String... mappers) {
        this.mapperFiles = mappers;
        return this;
    }

    public Session build() {
        loadConnection();

        // get sql from configConnection file
        for (String mf : mapperFiles) {
            Yaml yaml = new Yaml(new Constructor(MapperEntry.class));
            InputStream is = TinyMapper.class.getClassLoader()
                    .getResourceAsStream(mf);
            MapperEntry result = yaml.load(is);
            Map<String, QueryEntry> sqlMap = result.getQueries();
            queries.putAll(sqlMap);

            registry.put(getMapperClass(result.getMapper()),
                    new MapperProxyFactory(this));
        }

        return new SessionImpl(this);
    }

    private Class<?> getMapperClass(String mc) {
        try {
            return Class.forName(mc);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadConnection() {
        Yaml configYaml = new Yaml(new Constructor(ConnectionConfig.class));
        InputStream inputStream = this.getClass()
                .getClassLoader()
                .getResourceAsStream(configFile);
        ConnectionConfig config = configYaml.load(inputStream);
        try {
            this.connection = DriverManager.getConnection(config.url, config.user, config.password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
