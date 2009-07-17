package com.googlecode.jdbcproc.daofactory.impl.parameterconverter;

import java.sql.*;

/**
 *  INTEGER - long
 */
public class ParameterConverter_INTEGER_long implements IParameterConverter<Long> {

    public void setValue(Long aValue, PreparedStatement aStmt, int aIndex) throws SQLException {
        aStmt.setLong(aIndex, aValue);
    }

    public void setValue(Long aValue, CallableStatement aStmt, String aParameterName) throws SQLException {
        aStmt.setLong(aParameterName, aValue);
    }

    public Long getOutputParameter(CallableStatement aStmt, String aParameterName) throws SQLException {
        return aStmt.getLong(aParameterName);
    }

    public Long getFromResultSet(ResultSet aResultSet, String aParameterName) throws SQLException {
        return aResultSet.getLong(aParameterName);
    }

    public ParameterSetterKey getKey() {
        return new ParameterSetterKey(Types.INTEGER, long.class);
    }
}