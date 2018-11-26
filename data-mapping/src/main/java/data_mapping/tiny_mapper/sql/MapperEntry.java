package data_mapping.tiny_mapper.sql;

import java.util.Map;

public class MapperEntry {
    private String mapper;
    private Map<String, QueryEntry> queries;

    public Map<String, QueryEntry> getQueries() {
        return queries;
    }

    public void setQueries(Map<String, QueryEntry> queries) {
        this.queries = queries;
    }

    public String getMapper() {
        return mapper;
    }

    public void setMapper(String mapper) {
        this.mapper = mapper;
    }
}
