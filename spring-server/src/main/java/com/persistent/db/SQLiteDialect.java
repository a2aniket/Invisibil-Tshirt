/**
 * This class provides an SQLIte dialect for use with Hibernate.
 * It extends the org.hibernate.dialect.Dialect class and implements the necessary 
 * methods for translating SQL queries and other commands into the appropriate 
 * SQLIte syntax. 
 */
package com.persistent.db;

import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.identity.IdentityColumnSupport;

import java.sql.Types;

public class SQLiteDialect extends Dialect {

	/**
	 * Constructs a new instance of the SQLite dialect.
	 */
	public SQLiteDialect() {
		registerColumnType(Types.BIT, "integer");
		registerColumnType(Types.TINYINT, "tinyint");
		registerColumnType(Types.SMALLINT, "smallint");
		registerColumnType(Types.INTEGER, "integer");
		registerColumnType(Types.BIGINT, "bigint");
		registerColumnType(Types.FLOAT, "float");
		registerColumnType(Types.REAL, "real");
		registerColumnType(Types.DOUBLE, "double");
		registerColumnType(Types.NUMERIC, "numeric");
		registerColumnType(Types.DECIMAL, "decimal");
		registerColumnType(Types.CHAR, "char");
		registerColumnType(Types.VARCHAR, "varchar");
		registerColumnType(Types.LONGVARCHAR, "longvarchar");
		registerColumnType(Types.DATE, "date");
		registerColumnType(Types.TIME, "time");
		registerColumnType(Types.TIMESTAMP, "timestamp");
		registerColumnType(Types.BINARY, "blob");
		registerColumnType(Types.VARBINARY, "blob");
		registerColumnType(Types.LONGVARBINARY, "blob");
		registerColumnType(Types.BLOB, "blob");
		registerColumnType(Types.CLOB, "clob");
		registerColumnType(Types.BOOLEAN, "integer");
	}

	/**
	 * Returns an implementation of the IdentityColumnSupport interface that 
	 * provides information about the level of support for identity columns in 
	 * the current database dialect.
	 * @return An implementation of the IdentityColumnSupport interface.
	 */
	public IdentityColumnSupport getIdentityColumnSupport() {
		return new SQLiteIdentityColumnSupport();
	}

	/**
	 * This method is used to indicate whether this dialect support the ALTER TABLE
	 * syntax.
	 * @return true if support altering of tables.
	 * 		   or false if not.	     	 
	 */
	public boolean hasAlterTable() {
		return false;
	}

	/**
	 * This method is used to indicate whether to drop constraints before dropping 
	 * tables in this dialect.
	 * @return true if constraints must be dropped prior to dropping the table.
	 * 		   or false if not.
	 */
	public boolean dropConstraints() {
		return false;
	}

	public String getDropForeignKeyString() {
		return "";
	}

	/**
	 * This method is used to add a foreign key constraint to a table.
	 * @param constraintName  The Foreign Key constraint name.
	 * @param foreignKey - The names of the columns comprising the Foreign Key
	 * @param referencedTable - The table referenced by the Foreign Key
	 * @param primaryKey - The explicit columns in the referencedTable referenced 
	 * by this Foreign Key.
	 * @param referencesPrimaryKey - if false, constraint should be explicit about 
	 * which column names the constraint refers to
	 * @return 
	 */
	public String getAddForeignKeyConstraintString(String constraintName, String[] foreignKey, String referencedTable,
			String[] primaryKey, boolean referencesPrimaryKey) {
		return "";
	}

	/**
	 * This method is used to add a Primary Key constraint to a table.
	 * @param constraintName - The name of the Primary Key constraint.
	 */
	public String getAddPrimaryKeyConstraintString(String constraintName) {
		return "";
	}

	/**
	 * This method is used to Get the string to append to SELECT statements to 
	 * acquire locks for this dialect.
	 */
	public String getForUpdateString() {
		return "";
	}

	/**
	 * This method is used to add a column to a table.
	 * @return The "add column" fragment.
	 */
	public String getAddColumnString() {
		return "add column";
	}

	/**
	 * This method is used to indicate whether this dialect support FOR UPDATE 
	 * in conjunction with outer joined rows.
	 * @return true if outer joined rows can be locked via FOR UPDATE.
	 * 		   or false if not. 
	 */
	public boolean supportsOuterJoinForUpdate() {
		return false;
	}

	/**
	 * This method is used to indicate whether this dialect supports IF EXISTS clause
	 * @return true if IF EXISTS is supported.
	 * 		   or false if not supported.
	 */
	public boolean supportsIfExistsBeforeTableName() {
		return true;
	}

	/**
	 * This method is used to indicate whether this dialect supports cascade delete.
	 * @return true if cascade delete is supported.
	 * 		   or false if not supported.
	 */
	public boolean supportsCascadeDelete() {
		return false;
	}
}
