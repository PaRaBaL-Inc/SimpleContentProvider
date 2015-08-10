# SimpleContentProvider
<h3><b>Simple Content Provider/b></h3>

<h4> SETUP </h4>

1. Copy all the java files into your Android project.

Alternative:
1. Inside Android Studio, click on File->New->Import Module.
2. Select the source directory for this library.
3. Click Finish.

<h4>USAGE</h4>
1. First, create a DbDataSource object. This object should only be created once. This will create the database.
	```
	DBDataSource trial = new DBDataSource(this);
	```
2. To create a table inside the database, call the onCreateTable function.
	```
	String tableName = "accounts";
        String[] columnNames = {"name", "authorization_key"};
        String[] columnTypes = {"text", "text"};
        boolean[] uniques = {false, false};
        boolean[] foreignKeys = {false, false};
        String[] refTables = {"", ""};
        String[] refColumns = {"", ""};
        
        trial.onCreateTable(tableName, columnNames, columnTypes, uniques, foreignKeys, refTables, refColumns);
  	```  
  3. Once the tables are installed, you can:
  <ul>
  <li>Insert row</li>
  <li type="square">Delete row</li>
  <li>Query for row and return Cursor</li>
  <li>Query for row and return individual items and lists of items of the object TableItem.</li>
</ul>
