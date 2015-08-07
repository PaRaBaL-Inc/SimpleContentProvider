package parabal.com.contentprovider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

class DbHelper extends SQLiteOpenHelper
{
	static final int VERSION = 1;
	static final String DATABASE = "parabal.db";
	static final String ACCOUNT_TABLE = "accounts";
	static final String TASK_TABLE = "tasks";
	static final String PROJECT_TABLE = "projects";
	static final String COMMENT_TABLE = "comments";
	public static final String C_ID = "the_id";

	private ArrayList<TableInfo> tableinfo = null;
	private SQLiteDatabase database = null;

	/**
	 *
	 * @param context
	 */
	public DbHelper(Context context)
	{
		super(context, DATABASE, null, VERSION);
		tableinfo = new ArrayList<>();
	}

	/**
	 * Creates the tables used for the database.
	 * @param db the database
	 */
	@Override
    public void onCreate(SQLiteDatabase db)
    {
		database = db;
    }

	/**
	 * Creates an object that holds the schema for a table and adds it to a list of tables.
	 * @param tableName the table name
	 * @param columnNames the names of the columns for the table
	 * @param columnTypes the corresponding types of the created columns
	 * @param uniques creates unique columns
	 * @param foreignKeys sets the corresponding columnName as foreign keys
	 * @param refTable the tables the corresponding foreign keys reference
	 * @param refColumn the columns referenced in the referenced table
	 */
	public void createTableInfo(String tableName, String[] columnNames, String[] columnTypes, boolean[] uniques, boolean[] foreignKeys, String[] refTable, String[] refColumn)
	{
		TableInfo table = new TableInfo(tableName, columnNames, columnTypes, uniques, foreignKeys, refTable, refColumn);
		tableinfo.add(table);
	}

	/**
	 * Returns all table info as a list.
	 * @return
	 */
	public ArrayList<TableInfo> getAllTableInfo()
	{
		return tableinfo;
	}

	/**
	 * Returns the schema of a given table as a TableInfo object.
	 * @param tableName the table name
	 * @return
	 */
	public TableInfo getTableInfo(String tableName)
	{
		TableInfo table = null;

		for(int i = 0; i < tableinfo.size(); i++)
		{
			if( tableName.equals(tableinfo.get(i).getTableName()))
			{
				table = tableinfo.get(i);
			}
		}

		if(table == null)
		{
			return new TableInfo();
		}

		return table;
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
		boolean[] uniques = new boolean[columnNames.length];

		for(int i = 0; i < columnNames.length; i++)
		{
			uniques[i] = false;
		}

		return onCreateTable(tableName, columnNames, columnTypes, uniques, foreignKeys, refTable, refColumn);
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

		boolean[] foreignKeys = new boolean[columnNames.length];
		String[] refTable = new String[columnNames.length];
		String[] refColumn = new String[columnNames.length];

		for(int i = 0; i < columnNames.length; i++)
		{
			foreignKeys[i] = false;
			refTable[i] = "";
			refColumn[i] = "";
		}

		return onCreateTable(tableName, columnNames, columnTypes, uniques, foreignKeys, refTable, refColumn);
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
		String sql = "create table";

		if(columnNames.length != columnTypes.length)
		{
			Log.e("DbHelper", "The list of column names and column types are not equal.");
			return false;
		}

		sql = sql + " " + tableName + " ( " + C_ID + " integer primary key autoincrement,";

		String unique = " unique( ";
		String foreign = "";
		int count_u = 0;
		int count_f = 0;

		for(int i = 0; i < columnNames.length; i++)
		{
			String type = columnTypes[i].replaceAll(" ", "");
			String name = columnNames[i].replaceAll(" ", "");

			sql = sql + " " + name + " " + type + ",";

			if(uniques[i])
			{
				count_u++;
				unique = unique + columnNames[i] + ", ";
			}

			if(foreignKeys[i])
			{
				count_f++;
				foreign = foreign + ", foreign key(" + columnNames[i] + ") REFERENCES " + refTable[i].trim() + "(" + refColumn[i].trim() + ")";
			}
		}

		if(count_u == 0)
		{
			unique = "";
		}

		else
		{
			unique = ", " + unique.substring(0, unique.length() - 2) + ") ";
		}

		if(count_f == 0)
		{
			foreign = "";
		}

		else
		{
			foreign = foreign.substring(0, foreign.length()-1) + ") ";
		}

		sql = sql.substring(0, sql.length() - 1) + unique + foreign + ")";

		try
		{
			getWritableDatabase().execSQL(sql);
		}

		catch(Exception e)
		{
			Log.d("DbHelper string", sql);
			Log.e("DbHelper", e.toString());
			return false;
		}

		createTableInfo(tableName, columnNames, columnTypes, uniques, foreignKeys, refTable, refColumn);
		return true;
	}

	/**
	 * Drops the tables if there is a new table version.
	 * @param db the database
	 * @param oldVersion the old version
	 * @param newVersion the new version
	 */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
    	db.execSQL("drop table if exists " + ACCOUNT_TABLE);
        db.execSQL("drop table if exists " + PROJECT_TABLE);
    	db.execSQL("drop table if exists " + TASK_TABLE);
    	db.execSQL("drop table if exists " + COMMENT_TABLE);

    	this.onCreate(db);
    }

	/**
	 * Delete a table.
	 * @param tableName the table name
	 */
	public void delete(String tableName)
	{
		SQLiteDatabase db = getWritableDatabase();
		db.delete(tableName, null, null);
		db.close();
	}
}
