package data_mapping.tiny_mapper.sql;

public class QueryEntry {
    private String sql;
    private String returnType;

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public String getReturnType() {
        return returnType;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }

    @Override
    public String toString() {
        return "QueryEntry{" +
                ", sql='" + sql + '\'' +
                ", returnType='" + returnType + '\'' +
                '}';
    }
}
