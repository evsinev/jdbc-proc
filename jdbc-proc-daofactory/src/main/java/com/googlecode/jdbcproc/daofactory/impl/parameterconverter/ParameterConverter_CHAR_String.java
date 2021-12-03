package com.googlecode.jdbcproc.daofactory.impl.parameterconverter;

import com.googlecode.jdbcproc.daofactory.impl.dbstrategy.ICallableStatementGetStrategy;
import com.googlecode.jdbcproc.daofactory.impl.dbstrategy.ICallableStatementSetStrategy;
import com.googlecode.jdbcproc.daofactory.impl.dbstrategy.StatementArgument;

import java.sql.*;

/**
 *  CHAR - String
 */
public class ParameterConverter_CHAR_String
    implements IParameterConverter<ParameterConverter_CHAR_String, String> {

  public static final Type<ParameterConverter_CHAR_String> TYPE
      = new Type<ParameterConverter_CHAR_String>(Types.CHAR, String.class);

  private final boolean isFilter3ByteChars;

  public ParameterConverter_CHAR_String(boolean isFilter3ByteChars) {
    this.isFilter3ByteChars = isFilter3ByteChars;
  }

  public void setValue(String value, ICallableStatementSetStrategy stmt, StatementArgument parameterName)
      throws SQLException {
    if (isFilter3ByteChars) {
      String fixedString = ParameterConverterUtils.filter3BytesUTF(value);
      stmt.setString(parameterName, fixedString);
    } else {
      stmt.setString(parameterName, value);
    }
  }

  public String getOutputParameter(ICallableStatementGetStrategy aStmt, StatementArgument aParameterName)
      throws SQLException {
    return aStmt.getString(aParameterName);
  }

  public String getFromResultSet(ResultSet resultSet, String parameterName) throws SQLException {
    return resultSet.getString(parameterName);
  }

  public Type<ParameterConverter_CHAR_String> getType() {
    return TYPE;
  }

  public String toString() {
    return "ParameterConverter_CHAR_String{}";
  }
}
