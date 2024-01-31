package com.googlecode.jdbcproc.daofactory.impl.block.impl;

import com.googlecode.jdbcproc.daofactory.impl.block.IResultSetConverterBlock;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Fill entity with @OneToMany annotations
 */
public class ResultSetConverterBlockEntityOneToMany implements IResultSetConverterBlock {
    /**
     * Creates entity with OneToMany build block
     *
     * @param aEntityType              base entity class
     * @param aEntityPropertySetters   base entity property setters
     * @param aOneToOneLinks           one to one links
     * @param aChildClass              child class
     * @param aChildPropertySetters    chile property setters
     * @param aChileOneToOneLinks      child one to one links
     * @param aOneToManyMethod         one to many setter method
     */
    public ResultSetConverterBlockEntityOneToMany(
              Class aEntityType
            , List<EntityPropertySetter> aEntityPropertySetters
            , List<OneToOneLink> aOneToOneLinks
            , Class aChildClass
            , List<EntityPropertySetter> aChildPropertySetters
            , List<OneToOneLink> aChileOneToOneLinks
            , Method aOneToManyMethod
    ) {

        theEntityPropertySetters = aEntityPropertySetters;
        theEntityType = aEntityType;
        theOneToOneLinks = aOneToOneLinks;
        theChildClass = aChildClass;
        theOneToManyMethod = aOneToManyMethod;
        theChildPropertySetters = aChildPropertySetters;
        theChildOneToOneLinks = aChileOneToOneLinks;
    }

    public Object convertResultSet(ResultSet aResultSet, CallableStatement aStmt) throws SQLException {
        Objects.requireNonNull(aResultSet, "ResultSet is null");

        Object entity = null;
        List<Object> oneToManyList = new ArrayList<Object>();
        while(aResultSet.next()) {
            // create only once base entity
            if(entity==null) {
                entity = createParentEntity(aResultSet);
                setOneToManyList(entity, oneToManyList);
            }
            // creates child entity and add to list
            Object childEntity = createChildEntity(aResultSet);
            oneToManyList.add(childEntity);
        }

        return entity;
    }

    protected void setOneToManyList(Object aBaseEntity, List<Object> aList) {
        try {
            theOneToManyMethod.invoke(aBaseEntity, aList);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Error while setting OneToMany list: "+e.getMessage(), e);
        } catch (InvocationTargetException e) {
            throw new IllegalStateException("Error while setting OneToMany list: "+e.getMessage(), e);
        }
    }

    protected Object createParentEntity(ResultSet aResultSet) {
        return ResultSetConverterBlockUtil.createEntity(aResultSet, theEntityType, theEntityPropertySetters, theOneToOneLinks);
    }

    protected Object createChildEntity(ResultSet aResultSet) {
        return ResultSetConverterBlockUtil.createEntity(
                  aResultSet
                , theChildClass
                , theChildPropertySetters
                , theChildOneToOneLinks);
    }


    public String toString() {
        return "ResultSetConverterBlockEntityOneToMany{" +
                "theEntityPropertySetters=" + theEntityPropertySetters +
                ", theChildPropertySetters=" + theChildPropertySetters +
                ", theEntityType=" + theEntityType +
                ", theOneToOneLinks=" + theOneToOneLinks +
                ", theChildOneToOneLinks=" + theChildOneToOneLinks +
                ", theChildClass=" + theChildClass +
                ", theOneToManyMethod=" + theOneToManyMethod +
                '}';
    }

    private final List<EntityPropertySetter> theEntityPropertySetters;
    private final List<EntityPropertySetter> theChildPropertySetters;
    private final Class theEntityType;
    private final List<OneToOneLink> theOneToOneLinks;
    private final List<OneToOneLink> theChildOneToOneLinks;
    private final Class theChildClass;
    private final Method theOneToManyMethod;

}
