/**
 * This class gives a custom IdentityColumnSupport implementation that provides 
 * information about the level of support for identity columns.
 */
package com.persistent.db;

import org.hibernate.MappingException;
import org.hibernate.dialect.identity.IdentityColumnSupportImpl;

public class SQLiteIdentityColumnSupport extends IdentityColumnSupportImpl {

    /**
     * This method is used to indicate whether the current database dialect supports
     * identity columns that can be generated automatically by the database.
     * @return true if identity columns are supported,
     *         or false if not supported.
     */
    @Override
    public boolean supportsIdentityColumns() {
        return true;
    }

    /**
     * This method is used to return the SQL string to use selecting the last 
     * inserted identity column value.
     * @return The SQL string tp use for selecting the last inserted identity column
     * value.
     */
    @Override
    public String getIdentitySelectString(String table, String column, int type) throws MappingException {
        return "select last_insert_rowid()";
    }

    /**
     * The syntax used during DDL to define a column as being an IDENTITY of a 
     * particular type.
     * @param The java.sql.Types type code.
     * @return The appropriate DDL fragment.
     */
    @Override
    public String getIdentityColumnString(int type) throws MappingException {
        return "integer";
    }
}