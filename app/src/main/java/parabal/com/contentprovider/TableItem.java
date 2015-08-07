package parabal.com.contentprovider;

import android.util.Log;

/**
 * Created by tcelestin on 7/31/15.
 */
public class TableItem
{
    private String tableName = "";
    private String[] values = new String[]{};
    private int id = -1;

    /**
     * TableItem constructor that initializes variables.
     */
    TableItem()
    {}

    /**
     * TableItem constructor that initializes variables.
     * @param tableName the table's name
     * @param id the_id value of the row
     * @param values the values of the row set as an array of strings.
     */
    TableItem(String tableName, int id, String[] values)
    {
        setTableName(tableName);
        setID(id);
        setValues(values);
    }

    /**
     * Sets the table name.
     * @param tableName the table name
     */
    final public void setTableName(String tableName)
    {
        this.tableName = tableName;
    }

    /**
     * Sets the id value with the_id's value.
     * @param id the id
     */
    final public void setID(int id)
    {
        this.id = id;
    }

    /**
     * Sets the values of a column in the row.
     * @param index chooses the numbered column
     * @param value the value of the column
     */
    final public void setValue(int index, String value)
    {
        values[index] = value;
    }

    /**
     * Sets the values of the row.
     * @param values the values of the row
     */
    final public void setValues(String[] values)
    {
        this.values = new String[values.length];

        for(int i = 0; i < values.length; i++)
        {
            this.values[i] = values[i];
        }
    }

    /**
     * Returns the table name of the row.
     * @return
     */
    final public String getTableName()
    {
        return this.tableName;
    }

    /**
     * Returns the values of the row.
     * @return
     */
    final public String[] getValues()
    {
        return this.values;
    }

    /**
     * Returns the row ID. -1 if no row ID was set.
     * @return
     */
    final public int getID()
    {
        return this.id;
    }

    /**
     * Prints the contents of a TableItem.
     */
    final public void printAll()
    {
        Log.d("TableItem", this.tableName);
        Log.d("TableItem", Integer.toString(this.getID()));

        for(int i = 0; i < this.getValues().length; i++)
        {
            Log.d("TableItem Value: ", this.getValues()[i]);
        }
    }
}
