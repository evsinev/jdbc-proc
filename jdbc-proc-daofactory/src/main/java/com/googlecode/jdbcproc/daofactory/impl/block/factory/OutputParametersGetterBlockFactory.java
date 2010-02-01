package com.googlecode.jdbcproc.daofactory.impl.block.factory;

import com.googlecode.jdbcproc.daofactory.impl.block.IOutputParametersGetterBlock;
import com.googlecode.jdbcproc.daofactory.impl.block.impl.EntityPropertySetter;
import com.googlecode.jdbcproc.daofactory.impl.block.impl.OutputParametersGetterBlockEntity;
import com.googlecode.jdbcproc.daofactory.impl.procedureinfo.StoredProcedureInfo;
import com.googlecode.jdbcproc.daofactory.impl.procedureinfo.StoredProcedureArgumentInfo;
import com.googlecode.jdbcproc.daofactory.impl.parameterconverter.IParameterConverter;
import com.googlecode.jdbcproc.daofactory.impl.parameterconverter.ParameterConverterManager;

import java.lang.reflect.Method;
import java.util.List;
import java.util.LinkedList;

import org.springframework.util.Assert;

/**
 * Creates IOutputParametersGetter block
 */
public class OutputParametersGetterBlockFactory {

    public IOutputParametersGetterBlock create(ParameterConverterManager aConverterManager
            ,  Method aDaoMethod, StoredProcedureInfo aProcedureInfo) {

        // List.getAll();
        if(BlockFactoryUtils.isGetAllMethod(aDaoMethod, aProcedureInfo)) {
            return null;

        // CREATE ENTITY
        } else if( isCreateEntityMethod(aDaoMethod, aProcedureInfo) ) {
            List<EntityPropertySetter> setters = new LinkedList<EntityPropertySetter>();
            Class entityClass = aDaoMethod.getParameterTypes()[0];
            for (StoredProcedureArgumentInfo argumentInfo : aProcedureInfo.getArguments()) {
                if(argumentInfo.isOutputParameter()) {
                    Method getterMethod = BlockFactoryUtils.findGetterMethod(entityClass, argumentInfo);
                    Method setterMethod = BlockFactoryUtils.findSetterMethod(entityClass, getterMethod);
                    IParameterConverter paramConverter = aConverterManager.findConverter(argumentInfo.getDataType(), getterMethod.getReturnType());
                    setters.add(new EntityPropertySetter(setterMethod, paramConverter
                            , argumentInfo.getColumnName(), argumentInfo.getDataType()));
                }
            }
            return setters.size() > 0 ? new OutputParametersGetterBlockEntity(setters) : null;

        // return one output parameter
        } else if( BlockFactoryUtils.isOneOutputHasReturn(aDaoMethod, aProcedureInfo) ) {
            // see ResultSetConverterBlockOutputParameterHasReturn
            return null;

        // DEFAULT NO     
        } else {
            return null;
        }
    }

    private boolean isCreateEntityMethod(Method aDaoMethod, StoredProcedureInfo aProcedureInfo) {
        return     aProcedureInfo.getArgumentsCounts() > 1
                && aDaoMethod.getParameterTypes().length==1
                && ! BlockFactoryUtils.isSimpleType(aDaoMethod.getParameterTypes()[0])
        ;
    }


}
