package parabal.com.contentprovider;

/**
 * Created by tcelestin on 7/31/15.
 */

public class TableInfo
{
    private String tableName = "";
    private String[] columnNames = new String[] {};
    private String[] columnTypes = new String[] {};
    private boolean[] uniques = new boolean[] {};
    private boolean[] foreignKeys = new boolean[] {};
    private String[] refTables = new String[] {};
    private String[] refColumns = new String[] {};

    public TableInfo()
    {}

    public TableInfo(String tableName, String[] columnNames, String[] columnTypes, boolean[] uniques, boolean[] foreignKeys, String[] refTable, String[] refColumn)
    {
        setTableName(tableName);
        setColumnNames(columnNames);
        setColumnTypes(columnTypes);
        setForeignKeys(foreignKeys);
        setUniques(uniques);
        setRefTables(refTable);
        setRefColumns(refColumn);
    }

    final public boolean isEmpty()
    {
        return (tableName.compareTo("") == 0);
    }

    final public String getTableName()
    {
        return this.tableName;
    }

    final public String[] getColumnNames()
    {
        return this.columnNames;
    }

    final public String[] getColumnTypes()
    {
        return this.columnTypes;
    }

    final public boolean[] getUniques()
    {
        return this.uniques;
    }

    final public boolean[] getForeignKeys()
    {
        return this.foreignKeys;
    }

    final public String[] getRefTables()
    {
        return this.refTables;
    }

    final public String[] getRefColumns()
    {
        return this.refColumns;
    }

    final public void setTableName(String tableName)
    {
        this.tableName = tableName;
    }

    final public void setColumnNames(String[] columnNames)
    {
        this.columnNames = new String[columnNames.length];
        System.arraycopy(columnNames, 0, this.columnNames, 0, columnNames.length);
    }

    final public void setColumnTypes(String[] columnTypes)
    {
        this.columnTypes = new String[columnTypes.length];
        System.arraycopy(columnTypes, 0, this.columnTypes, 0, columnTypes.length);
    }

    final public void setUniques(boolean[] uniques)
    {
        this.uniques = new boolean[uniques.length];
        System.arraycopy(uniques, 0, this.uniques, 0, uniques.length);
    }

    final public void setForeignKeys(boolean[] foreignKeys)
    {
        this.foreignKeys = new boolean[foreignKeys.length];
        System.arraycopy(foreignKeys, 0, this.foreignKeys, 0, foreignKeys.length);
    }

    final public void setRefTables(String[] refTables)
    {
        this.refTables = new String[refTables.length];
        System.arraycopy(refTables, 0, this.refTables, 0, refTables.length);
    }

    final public void setRefColumns(String[] refColumns)
    {
        this.refColumns = new String[refColumns.length];
        System.arraycopy(refColumns, 0, this.refColumns, 0, refColumns.length);
    }
}
