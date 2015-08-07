package parabal.com.contentprovider;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by tcelestin on 7/30/15.
 */
public class DBDataSource
{
    private DBData dbData = null;
    private SQLiteDatabase database = null;

    /**
     * Constructor for a DBDataSource
     * @param context
     */
    public DBDataSource(Context context)
    {
        dbData = new DBData(context);
    }

    /**
     * Open the database.
     * @throws SQLException
     */
    public void open() throws SQLException
    {
        database = dbData.open();
    }

    /**
     * Close the database.
     */
    public void close()
    {
        dbData.close();
    }

    /**
     * Create a table with foreign keys.
     * @param tableName the table name
     * @param columnNames the column names
     * @param columnTypes the types of the corresponding names
     * @param foreignKeys sets the corresponding columnName as foreign keys
     * @param refTable the tables the corresponding foreign keys reference
     * @param refColumn the columns referenced in the referenced table
     * @return
     */
    public boolean onCreateTable(String tableName, String[] columnNames, String[] columnTypes, boolean[] foreignKeys, String[] refTable, String[] refColumn)
    {
        return dbData.onCreateTable(tableName, columnNames, columnTypes, foreignKeys, refTable, refColumn);
    }

    /**
     * Create a table with unique keys.
     * @param tableName the table name
     * @param columnNames the column names
     * @param columnTypes the types of the corresponding names
     * @param uniques creates unique columns
     * @return
     */
    public boolean onCreateTable(String tableName, String[] columnNames, String[] columnTypes, boolean[] uniques)
    {
        return dbData.onCreateTable(tableName, columnNames, columnTypes, uniques);
    }

    /**
     * Create a table with foreign and unique keys.
     * @param tableName the table name
     * @param columnNames the column names
     * @param columnTypes the types of the corresponding names
     * @param uniques creates unique columns
     * @param foreignKeys sets the corresponding columnName as foreign keys
     * @param refTable the tables the corresponding foreign keys reference
     * @param refColumn the columns referenced in the referenced table
     * @return
     */
    public boolean onCreateTable(String tableName, String[] columnNames, String[] columnTypes, boolean[] uniques, boolean[] foreignKeys, String[] refTable, String[] refColumn)
    {
        return dbData.onCreateTable(tableName, columnNames, columnTypes, uniques, foreignKeys, refTable, refColumn);
    }

    /**
     * Inserts a row into the table and returns a TableItem with the same content.
     * @param tableName the table name
     * @param columnNames the column names
     * @param columnValues the column values
     * @param conflictIgnore Allow the
     * @return
     */
    public TableItem createTableItem(String tableName, String[] columnNames, String[] columnValues, boolean conflictIgnore)
    {
        ContentValues values = new ContentValues();
        Cursor cursor;

        for(int i = 0; i < columnNames.length; i++)
        {
            values.put(columnNames[i], columnValues[i]);
        }

        TableInfo tableInfo = dbData.dbHelper.getTableInfo(tableName);

        if(tableInfo.isEmpty())
        {
            Log.e("DBDataSource", "The table could not be found. An empty table item was returned instead.");
            return new TableItem();
        }

        long insertId = dbData.tableInsertItem(tableInfo.getTableName(), values, conflictIgnore);

        String[] allColumns = new String[tableInfo.getColumnNames().length + 1];
        allColumns[0] = DbHelper.C_ID;

        System.arraycopy(tableInfo.getColumnNames(), 0, allColumns, 1, allColumns.length - 1);
        cursor = database.query(tableInfo.getTableName(), allColumns, DbHelper.C_ID + " = " + insertId, null, null, null, null);
        cursor.moveToFirst();
        TableItem tableItem = dbData.cursorToTableItem(cursor, tableInfo);
        cursor.close();
        return tableItem;
    }

    /**
     * Get all row from the table in an ArrayList of TableItems.
     * @param tableName table name
     * @return
     */
    public ArrayList<TableItem> getRowsAsArrayList(String tableName)
    {
        return dbData.getTableItemByColumnsAsArrayList(tableName, null, null, false, DbHelper.C_ID);
    }

    /**
     * Get all rows from the table in an ArrayList of TableItems.
     * @param tableName table name
     * @param isDescending sets the list in ascending or descending order based on a column
     * @param orderByColumn the column the list is ordered by
     * @return
     */
    public ArrayList<TableItem> getRowsAsArrayList(String tableName, boolean isDescending, String orderByColumn)
    {
        return dbData.getTableItemByColumnsAsArrayList(tableName, null, null, isDescending, orderByColumn);
    }

    /**
     * Get all rows from the table in an ArrayList or TableItems.
     * @param tableName table name
     * @param inputColumnNames column names used in a WHERE clause. Example: WHERE inputColumns1 = "values1"
     *                                                                       AND inputColumns2 = "values2", ...
     * @param values the values of the corresponding inputColumnNames to to search for.
     * @return
     */
    public ArrayList<TableItem> getRowsAsArrayList(String tableName, String[] inputColumnNames, String[] values)
    {
        return dbData.getTableItemByColumnsAsArrayList(tableName, inputColumnNames, values, false, DbHelper.C_ID);
    }

    /**
     * Get all rows from the table in an ArrayList or TableItems.
     * @param tableName table name
     * @param inputColumnNames column names used in a WHERE clause. Example: WHERE inputColumns1 = "values1"
     *                                                                       AND inputColumns2 = "values2", ...
     * @param values the values of the corresponding inputColumnNames to to search for.
     * @param isDescending sets the list in ascending or descending order based on a column
     * @param orderByColumn the column the list is ordered by
     * @return
     */
    public ArrayList<TableItem> getRowsAsArrayList(String tableName, String[] inputColumnNames, String[] values, boolean isDescending, String orderByColumn)
    {
        return dbData.getTableItemByColumnsAsArrayList(tableName, inputColumnNames, values, isDescending, orderByColumn);
    }

    /**
     * Return all columns from a table.
     * @param tableName the table name
     * @return
     */
    public Cursor getRowsAsCursor(String tableName)
    {
        return dbData.getColumnsByColumnsAsCursor(tableName, null, null, null, false, DbHelper.C_ID);
    }

    /**
     * Return all columns from a table ordered by a column.
     * @param tableName the table name
     * @param isDescending sets the list in ascending or descending order based on a column
     * @param orderByColumn the column the list is ordered by
     * @return
     */
    public Cursor getRowsAsCursor(String tableName, boolean isDescending, String orderByColumn)
    {
        return dbData.getColumnsByColumnsAsCursor(tableName, null, null, null, isDescending, orderByColumn);
    }

    /**
     * Return all columns from a table based on a WHERE clause.
     * @param tableName the table name
     * @param outputColumnNames the columns of the query to return
     * @param inputColumnNames column names used in a WHERE clause. Example: WHERE inputColumns1 = "values1"
     *                                                                       AND inputColumns2 = "values2", ...
     * @param values the values of the corresponding inputColumnNames to to search for.
     * @return
     */
    public Cursor getRowsAsCursor(String tableName, String[] outputColumnNames, String[] inputColumnNames, String[] values)
    {
        return dbData.getColumnsByColumnsAsCursor(tableName, outputColumnNames, inputColumnNames, values, false, DbHelper.C_ID);
    }

    /**
     * Return all columns from a table based on a WHERE clause and ordered by a column.
     * @param tableName the table name
     * @param outputColumnNames the columns of the query to return
     * @param inputColumnNames column names used in a WHERE clause. Example: WHERE inputColumns1 = "values1"
     *                                                                       AND inputColumns2 = "values2", ...
     * @param values the values of the corresponding inputColumnNames to to search for.
     * @param isDescending sets the list in ascending or descending order based on a column
     * @param orderByColumn the column the list is ordered by
     * @return
     */
    public Cursor getRowsAsCursor(String tableName, String[] outputColumnNames, String[] inputColumnNames, String[] values, boolean isDescending, String orderByColumn)
    {
        return dbData.getColumnsByColumnsAsCursor(tableName, outputColumnNames, inputColumnNames, values, isDescending, orderByColumn);
    }

    /**
     * Return the row, whose value is the minimum in a column, as a TableItem.
     * @param tableName the table name
     * @param orderByColumn the column the list is ordered by
     * @return
     */
    public TableItem getMinAsTableItem(String tableName, String orderByColumn)
    {
        TableInfo tableInfo = dbData.dbHelper.getTableInfo(tableName);
        TableItem tableItem;

        Cursor cursor = getMinAsCursor(tableName, orderByColumn);
        cursor.moveToFirst();
        tableItem = dbData.cursorToTableItem(cursor, tableInfo);
        cursor.close();
        return tableItem;
    }

    /**
     * Return the row, whose value is the maximum in a column, as a TableItem.
     * @param tableName the table name
     * @param orderByColumn the column the list is ordered by
     * @return
     */
    public TableItem getMaxAsTableItem(String tableName, String orderByColumn)
    {
        TableInfo tableInfo = dbData.dbHelper.getTableInfo(tableName);
        TableItem tableItem;

        Cursor cursor = getMaxAsCursor(tableName, orderByColumn);
        cursor.moveToFirst();
        tableItem = dbData.cursorToTableItem(cursor, tableInfo);
        cursor.close();
        return tableItem;
    }

    /**
     * Return the row, whose value is the maximum in a column, as a Cursor.
     * @param tableName the table name
     * @param orderByColumn the column the list is ordered by
     * @return
     */
    public Cursor getMaxAsCursor(String tableName, String orderByColumn)
    {
        TableInfo tableInfo = dbData.dbHelper.getTableInfo(tableName);
        String[] newColumns = new String[tableInfo.getColumnNames().length + 1];
        newColumns[0] = DbHelper.C_ID;

        for(int i = 0; i < tableInfo.getColumnNames().length; i++)
        {
            if(tableInfo.getColumnNames()[i].equals(orderByColumn.trim()))
            {
                newColumns[i+1] = "MAX( " + orderByColumn.trim() + " )";
            }

            else
            {
                newColumns[i+1] = tableInfo.getColumnNames()[i];
            }
        }

        Cursor cursor = dbData.getColumnsByColumnsAsCursor(tableName, newColumns, null, null, false, orderByColumn);
        cursor.moveToFirst();
        String[] newCursorValues = new String[cursor.getColumnNames().length];

        for(int i = 0; i < cursor.getColumnNames().length; i++)
        {
            newCursorValues[i] = cursor.getString(i);
        }

        String[] allColumns = new String[tableInfo.getColumnNames().length + 1];
        allColumns[0] = DbHelper.C_ID;

        System.arraycopy(tableInfo.getColumnNames(), 0, newColumns, 1, newColumns.length - 1);

        MatrixCursor matrixCursor = new MatrixCursor(newColumns);
        matrixCursor.addRow(newCursorValues);

        return matrixCursor;
    }

    /**
     * Return the row, whose value is the minimum in a column, as a Cursor.
     * @param tableName the table name
     * @param orderByColumn the column the list is ordered by
     * @return
     */
    public Cursor getMinAsCursor(String tableName, String orderByColumn)
    {
        TableInfo tableInfo = dbData.dbHelper.getTableInfo(tableName);
        String[] newColumns = new String[tableInfo.getColumnNames().length + 1];
        newColumns[0] = DbHelper.C_ID;

        for(int i = 0; i < tableInfo.getColumnNames().length; i++)
        {
            if(tableInfo.getColumnNames()[i].equals(orderByColumn.trim()))
            {
                newColumns[i+1] = "MIN( " + orderByColumn.trim() + " )";
            }

            else
            {
                newColumns[i+1] = tableInfo.getColumnNames()[i];
            }
        }

        Cursor cursor = dbData.getColumnsByColumnsAsCursor(tableName, newColumns, null, null, false, orderByColumn);
        cursor.moveToFirst();
        String[] newCursorValues = new String[cursor.getColumnNames().length];

        for(int i = 0; i < cursor.getColumnNames().length; i++)
        {
            newCursorValues[i] = cursor.getString(i);
        }

        String[] allColumns = new String[tableInfo.getColumnNames().length + 1];
        allColumns[0] = DbHelper.C_ID;

        System.arraycopy(tableInfo.getColumnNames(), 0, newColumns, 1, newColumns.length - 1);

        MatrixCursor matrixCursor = new MatrixCursor(newColumns);
        matrixCursor.addRow(newCursorValues);
        return matrixCursor;
    }

    /**
     * Delete the row based on the the_id
     * @param tableName the table name
     * @param id the id of the object
     */
    public void deleteRowByID(String tableName, long id)
    {
        database.delete(tableName, DbHelper.C_ID + " = " + id, null);
    }

}
