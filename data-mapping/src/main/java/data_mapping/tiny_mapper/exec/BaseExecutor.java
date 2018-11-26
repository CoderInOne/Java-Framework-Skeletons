package data_mapping.tiny_mapper.exec;

import data_mapping.tiny_mapper.proxy.ResultSetHandler;
import data_mapping.tiny_mapper.proxy.ReturnTypeToken;
import data_mapping.tiny_mapper.session.SessionBuilder;
import data_mapping.tiny_mapper.sql.QueryEntry;

import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BaseExecutor implements Executor {
    private SessionBuilder sessionBuilder;

    public BaseExecutor(SessionBuilder sessionBuilder) {
        this.sessionBuilder = sessionBuilder;
    }

    @Override
    public Object query(Method method, Object[] args) throws SQLException {
        String methodName = method.getName();
        QueryEntry queryEntry = sessionBuilder.getQueries().get(methodName);

        // create statement
        PreparedStatement statement = sessionBuilder.getConnection()
                .prepareStatement(queryEntry.getSql());

        // set parameter to precompile statement
        for (int i = 1; args != null && i <= args.length; i++) {
            statement.setObject(i, args[i-1]);
        }

        // execute and return data rows
        ResultSet resultSet = statement.executeQuery();

        // record type
        Class<?> returnType = method.getReturnType();
        ReturnTypeToken token = ReturnTypeToken.create(returnType, queryEntry.getReturnType());
        ResultSetHandler<?> rsHandler = ResultSetHandler.selectBy(token);
        return rsHandler.handle(resultSet);
    }
}
