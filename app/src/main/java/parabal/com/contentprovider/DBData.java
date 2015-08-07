package parabal.com.contentprovider;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by tcelestin on 7/30/15.
 */
public class DBData
{
    final DbHelper dbHelper;
    SQLiteDatabase db = null;

    /**
     * DBData constructor.
     * @param context the activity context.
     */
    DBData(Context context)
    {
        dbHelper = new DbHelper(context);
    }

    /**
     * Create a table with unique keys.
     * @param tableName the table name
     * @param columnNames the names of the columns for the table
     * @param columnTypes the corresponding types of the created columns
     * @param uniques creates unique columns
     * @return
     */
    public boolean onCreateTable(String tableName, String[] columnNames, String[] columnTypes, boolean[] uniques)
    {
        return dbHelper.onCreateTable(tableName, columnNames, columnTypes, uniques);
    }

    /**
     * Creates a table with foreign keys.
     * @param tableName the table name
     * @param columnNames the names of the columns for the table
     * @param columnTypes the corresponding types of the created columns
     * @param foreignKeys sets the corresponding columnName as foreign keys
     * @param refTable the tables the corresponding foreign keys reference
     * @param refColumn the columns referenced in the referenced table
     * @return
     */
    public boolean onCreateTable(String tableName, String[] columnNames, String[] columnTypes, boolean[] foreignKeys, String[] refTable, String[] refColumn)
    {
        return dbHelper.onCreateTable(tableName, columnNames, columnTypes, foreignKeys, refTable, refColumn);
    }

    /**
     * Create a table with foreign and unique keys.
     * @param tableName the table name
     * @param columnNames the names of the columns for the table
     * @param columnTypes the corresponding types of the created columns
     * @param uniques creates unique columns
     * @param foreignKeys sets the corresponding columnName as foreign keys
     * @param refTable the tables the corresponding foreign keys reference
     * @param refColumn the columns referenced in the referenced table
     * @return
     */
    public boolean onCreateTable(String tableName, String[] columnNames, String[] columnTypes, boolean[] uniques, boolean[] foreignKeys, String[] refTable, String[] refColumn)
    {
        return dbHelper.onCreateTable(tableName, columnNames, columnTypes, uniques, foreignKeys, refTable, refColumn);
    }

    /**
     * Opens the database.
     * @return
     * @throws SQLiteException
     */
    public SQLiteDatabase open() throws SQLiteException
    {
        db = this.dbHelper.getReadableDatabase();
        return db;
    }

    /**
     * Closes the database.
     */
    public void close()
    {
        dbHelper.close();
    }

    /**
     * Inserts a ContentValues object into the database.
     * @param tableName the table name
     * @param values holds values of the row to be inserted into the table
     * @param conflictIgnore set to true or false to add or ignore duplicate rows.
     * @return
     */
    public long tableInsertItem(String tableName, ContentValues values, boolean conflictIgnore)
    {
        long id = -1;
        TableInfo tableInfo = dbHelper.getTableInfo(tableName);
        String[] columnNames = tableInfo.getColumnNames();
        String where = "";
        Cursor cursor = null;
        TableItem tableItem = null;

        for(int i = 0; i < columnNames.length; i++)
        {
            where = where + columnNames[i] + " = \"" + values.getAsString(columnNames[i]) + "\" and ";
        }

        where = where.substring(0, where.length() - 5);

        try
        {
            cursor = db.query(tableName, null, where , null, null, null, null);
            int count = 0;

            if(cursor.getCount() > 0)
            {
                cursor.moveToFirst();
                tableItem = cursorToTableItem(cursor, tableInfo);

                for(int i = 0; i < tableItem.getValues().length; i++ )
                {
                    if( tableItem.getValues()[i].equals(values.getAsString(columnNames[i])) )
                    {
                        count++;
                    }
                }

            }

            if( (count != 0) && (count == tableItem.getValues().length) && !conflictIgnore)
            {
                id = cursor.getInt(0);
            }

            else
            {
                id = db.insert(tableName, null, values);
            }
        }

        catch(IllegalStateException e)
        {
            Log.d("DBData", "Creates ");
        }

        finally
        {
            try
            {
                cursor.close();
            }

            catch(NullPointerException e)
            {
                Log.d("DBData", "Cursor couldn't close.");
            }
        }

        return id;
    }

//    public int getColumnIntByColumnText(String tableName, String outputColumnName, String inputColumnName, String value)
//    {
//        try
//        {
//            Cursor cursor = db.query(tableName, new String[] {outputColumnName}, inputColumnName + "=" + value, null, null, null, null);
//
//            try
//            {
//                return cursor.moveToNext() ? cursor.getInt(cursor.getColumnIndex(outputColumnName)) : null;
//            }
//
//            finally
//            {
//                cursor.close();
//            }
//        }
//
//        finally
//        {
//            db.close();
//        }
//    }

    /**
     * Return specific columns from a table based on a WHERE clause.
     * @param tableName the table name
     * @param outputColumnNames the columns of the query to return
     * @param inputColumnNames column names used in a WHERE clause. Example: WHERE inputColumns1 = "values1"
     *                                                                       AND inputColumns2 = "values2", ...
     * @param values the values of the corresponding inputColumnNames to to search for.
     * @param isDescending set the returned values as ascending or descending
     * @param orderedByColumn the column to order the returned values by
     * @return
     */
    public Cursor getColumnsByColumnsAsCursor(String tableName, String[] outputColumnNames, String[] inputColumnNames, String[] values, boolean isDescending, String orderedByColumn)
    {
        Cursor cursor = null;
        String where = "";

        if(inputColumnNames != null)
        {
            for( int i = 0; i < inputColumnNames.length; i++ )
            {
                where = where + inputColumnNames[i] + " = \"" + values[i] + "\" and ";
            }

            if( where.length() > 5 )
            {
                where = where.substring(0, where.length() - 5);
            }
        }

        boolean exists = false;

        if(outputColumnNames != null)
        {
            for(int i = 0; i < outputColumnNames.length; i++)
            {
                if((outputColumnNames[i].trim()).equals(DbHelper.C_ID))
                {
                    exists = true;
                }
            }

            if(!exists)
            {
                String[] newOutputColumnNames = new String[outputColumnNames.length + 1];
                newOutputColumnNames[0] = DbHelper.C_ID;

                System.arraycopy(newOutputColumnNames, 1, outputColumnNames, 0, outputColumnNames.length);

//                for(int i = 0; i < outputColumnNames.length; i++)
//                {
//                    newOutputColumnNames[i+1] = outputColumnNames[i];
//                }

                outputColumnNames = newOutputColumnNames;
            }
        }

        String descending = " DESC";

        if(isDescending)
        {
            descending = " ASC";
        }

        try
        {
            cursor = db.query(tableName, outputColumnNames, where, null, null, null, orderedByColumn.trim() + " " + descending);
        }

        catch(IllegalStateException e)
        {
            Log.e("DBData", "Failed to query cursor.");
        }

        finally
        {}

        return cursor;
    }

    /**
     * Returns table row in the form of TableItems based on a a WHERE clause.
     * @param tableName the table name
     * @param inputColumnNames column names used in a WHERE clause. Example: WHERE inputColumns1 = "values1"
     *                                                                       AND inputColumns2 = "values2", ...
     * @param values the values of the corresponding inputColumnNames to to search for.
     * @param isDescending set the returned values as ascending or descending
     * @param orderedByColumn the column to order the returned values by
     * @return
     */
    public ArrayList<TableItem> getTableItemByColumnsAsArrayList(String tableName, String[] inputColumnNames, String[] values, boolean isDescending, String orderedByColumn)
    {
        Cursor cursor = null;
        TableInfo tableInfo = dbHelper.getTableInfo(tableName);
        ArrayList<TableItem> tableList= new ArrayList<>();

        cursor = getColumnsByColumnsAsCursor(tableName, null, inputColumnNames, values, isDescending, orderedByColumn);

        if(cursor.getCount() > 0)
        {
            cursor.moveToFirst();
            TableItem tableItem = cursorToTableItem(cursor, tableInfo);
            tableItem.printAll();
            tableList.add(tableItem);

            while( cursor.moveToNext() )
            {
                tableItem = cursorToTableItem(cursor, tableInfo);
                tableList.add(tableItem);
            }
        }

        cursor.close();
        return tableList;
    }

//    public double getColumnRealByColumnText(String tableName, String outputColumnName, String inputColumnName, String value)
//    {
////        SQLiteDatabase db = this.dbHelper.getReadableDatabase();
//
//        try
//        {
//            Cursor cursor = db.query(tableName, new String[] {outputColumnName}, inputColumnName + "=" + value, null, null, null, null);
//
//            try
//            {
//                return cursor.moveToNext() ? cursor.getDouble(cursor.getColumnIndex(outputColumnName)) : null;
//            }
//
//            finally
//            {
//                cursor.close();
//            }
//        }
//
//        finally
//        {
//            db.close();
//        }
//    }
//
//    public byte[] getColumnBlobByColumnText(String tableName, String outputColumnName, String inputColumnName, String value)
//    {
////        SQLiteDatabase db = this.dbHelper.getReadableDatabase();
//
//        try
//        {
//            Cursor cursor = db.query(tableName, new String[] {outputColumnName}, inputColumnName + "=" + value, null, null, null, null);
//
//            try
//            {
//                return cursor.moveToFirst() ? cursor.getBlob(cursor.getColumnIndex(outputColumnName)) : null;
//            }
//
//            finally
//            {
//                cursor.close();
//            }
//        }
//
//        finally
//        {
//            db.close();
//        }
//    }
//
//    public int getColumnIntByColumnReal(String tableName, String outputColumnName, float inputColumnName, float value)
//    {
////        SQLiteDatabase db = this.dbHelper.getReadableDatabase();
//
//        try
//        {
//            Cursor cursor = db.query(tableName, new String[] {outputColumnName}, inputColumnName + "=" + Float.toString(value), null, null, null, null);
//
//            try
//            {
//                return cursor.moveToNext() ? cursor.getInt(cursor.getColumnIndex(outputColumnName)) : null;
//            }
//
//            finally
//            {
//                cursor.close();
//            }
//        }
//
//        finally
//        {
//            db.close();
//        }
//    }
//
//    public String getColumnTextByColumnReal(String tableName, String outputColumnName, String inputColumnName, float value)
//    {
////        SQLiteDatabase db = this.dbHelper.getReadableDatabase();
//
//        try
//        {
//            Cursor cursor = db.query(tableName, new String[] {outputColumnName}, inputColumnName + "=" + Float.toString(value), null, null, null, null);
//
//            try
//            {
//                return cursor.moveToNext() ? cursor.getString(cursor.getColumnIndex(outputColumnName)) : null;
//            }
//
//            finally
//            {
//                cursor.close();
//            }
//        }
//
//        finally
//        {
//            db.close();
//        }
//    }
//
//    public double getColumnRealByColumnReal(String tableName, String outputColumnName, String inputColumnName, float value)
//    {
////        SQLiteDatabase db = this.dbHelper.getReadableDatabase();
//
//        try
//        {
//            Cursor cursor = db.query(tableName, new String[] {outputColumnName}, inputColumnName + "=" + Float.toString(value), null, null, null, null);
//
//            try
//            {
//                return cursor.moveToNext() ? cursor.getDouble(cursor.getColumnIndex(outputColumnName)) : null;
//            }
//
//            finally
//            {
//                cursor.close();
//            }
//        }
//
//        finally
//        {
//            db.close();
//        }
//    }
//
//    public byte[] getColumnBlobByColumnReal(String tableName, String outputColumnName, String inputColumnName, float value)
//    {
////        SQLiteDatabase db = this.dbHelper.getReadableDatabase();
//
//        try
//        {
//            Cursor cursor = db.query(tableName, new String[] {outputColumnName}, inputColumnName + "=" + Float.toString(value), null, null, null, null);
//
//            try
//            {
//                return cursor.moveToFirst() ? cursor.getBlob(cursor.getColumnIndex(outputColumnName)) : null;
//            }
//
//            finally
//            {
//                cursor.close();
//            }
//        }
//
//        finally
//        {
//            db.close();
//        }
//    }
//
//    public int getColumnIntByColumnInt(String tableName, String outputColumnName, float inputColumnName, int value)
//    {
////        SQLiteDatabase db = this.dbHelper.getReadableDatabase();
//
//        try
//        {
//            Cursor cursor = db.query(tableName, new String[] {outputColumnName}, inputColumnName + "=" + Integer.toString(value), null, null, null, null);
//
//            try
//            {
//                return cursor.moveToNext() ? cursor.getInt(cursor.getColumnIndex(outputColumnName)) : null;
//            }
//
//            finally
//            {
//                cursor.close();
//            }
//        }
//
//        finally
//        {
//            db.close();
//        }
//    }
//
//    public String getColumnTextByColumnInt(String tableName, String outputColumnName, String inputColumnName, int value)
//    {
//        double real = 0;
//
//        try
//        {
//            Cursor cursor = db.query(tableName, new String[] {outputColumnName}, inputColumnName + "=" + Integer.toString(value), null, null, null, null);
//
//            try
//            {
//                return cursor.moveToNext() ? cursor.getString(cursor.getColumnIndex(outputColumnName)) : null;
//            }
//
//            finally
//            {
//                cursor.close();
//            }
//        }
//
//        finally
//        {
//            db.close();
//        }
//    }
//
//    public double getColumnRealByColumnInt(String tableName, String outputColumnName, String inputColumnName, int value)
//    {
//        double real = 0;
//
//        try
//        {
//            Cursor cursor = db.query(tableName, new String[] {outputColumnName}, inputColumnName + "=" + Integer.toString(value), null, null, null, null);
//
//            try
//            {
//                real = cursor.moveToNext() ? cursor.getDouble(cursor.getColumnIndex(outputColumnName)) : null;
//            }
//
//            catch(NullPointerException e)
//            {
//                Log.e("DBData", "Failed to query cursor.");
//            }
//
//            finally
//            {
//                cursor.close();
//            }
//        }
//
//        catch(IllegalStateException e)
//        {
//            Log.e("DBData", "Failed to query cursor.");
//        }
//
//        return real;
//    }
//
//    public byte[] getColumnBlobByColumnInt(String tableName, String outputColumnName, String inputColumnName, int value)
//    {
//        byte[] blob = null;
//
//        try
//        {
//            Cursor cursor = db.query(tableName, new String[] {outputColumnName}, inputColumnName + "=" + Integer.toString(value), null, null, null, null);
//
//            try
//            {
//                blob = cursor.moveToFirst() ? cursor.getBlob(cursor.getColumnIndex(outputColumnName)) : null;
//            }
//
//            catch(IllegalStateException e)
//            {
//                Log.e("DBData", "Failed to query cursor.");
//            }
//
//            finally
//            {
//                cursor.close();
//            }
//        }
//
//        catch(IllegalStateException e)
//        {
//            Log.e("DBData", "Failed to query cursor.");
//        }
//
//        return blob;
//    }

    /**
     * Convert the cursor's current position into a TableItem
     * @param cursor
     * @param tableInfo TableInfo object holding schema information.
     * @return
     */
    public TableItem cursorToTableItem(Cursor cursor, TableInfo tableInfo)
    {
        TableItem tableItem = new TableItem();
        tableItem.setTableName(tableInfo.getTableName());
        String[] values = new String[tableInfo.getColumnNames().length];

        try
        {
            tableItem.setID(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DbHelper.C_ID))));
        }

        catch(IllegalStateException e)
        {
            tableItem.setID(-1);
        }

        for(int i = 0; i < tableInfo.getColumnNames().length; i++)
        {
            try
            {
                values[i] = cursor.getString(cursor.getColumnIndex(tableInfo.getColumnNames()[i]));
            }

            catch(IllegalStateException e)
            {
                Log.e("DBDataSource", "Failed to get the " + tableInfo.getColumnNames()[i]);
            }
        }

        tableItem.setValues(values);
        return tableItem;
    }
}