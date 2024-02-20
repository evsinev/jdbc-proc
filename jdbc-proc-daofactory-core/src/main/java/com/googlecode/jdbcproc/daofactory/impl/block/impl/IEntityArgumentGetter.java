package com.googlecode.jdbcproc.daofactory.impl.block.impl;

import com.googlecode.jdbcproc.daofactory.impl.dbstrategy.ICallableStatementSetStrategy;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by IntelliJ IDEA.
 * User: esinev
 * Date: 16.10.2009
 * Time: 2:56:21
 * To change this template use File | Settings | File Templates.
 */
public interface IEntityArgumentGetter {

    void setParameter(Object aEntity, ICallableStatementSetStrategy aStmt) throws InvocationTargetException, IllegalAccessException, SQLException;

    /**
     * for Collection insert query
     * @return  column name in tmp table
     */
    String getColumnNameForInsertQuery();
}
