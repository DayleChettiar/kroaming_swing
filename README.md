# This application allows us to manage usage information for a particular user. 

## Get the solution locally: from the terminal, run
```
     git clone https://github.com/DayleChettiar/kroaming_swing.git
```
  extract the swing application to a particular directory on your file system

## From the command line, run the 'scriptSQL.sql' file as follows:
```
     >>source <path to the scriptSQL.sql file>/scriptSQL.sql
```
  This creates a database named 'UserUsage' and a table named 'User' in it with user = root and password = test

## From the command line, run the 'insertSQL.sql' file as follows:
```
     >>source <path to the insertSQL.sql file>/insertSQL.sql
```
  This inserts values into a database named 'UserUsage' and a table named 'User' in it with user = root and password = test

## Now open the application named 'kroaming_swing' on an IDE of your choice as a java application and run the file:
  ```
  src/kroaming_swing/SQLClient.java
  ```
    Insert the username 'root' and password 'test' and in the 'Insert an SQL command' tab, insert the following query:
    ```
        select * from User;
    ```
    You can see the contents of the database table 'User'
    
## Next, run the file:
  ```
  src/kroaming_swing/TestTableEditor.java
  ```
    Insert the username 'root' and password 'test' and table name 'User'.
    Connect to the database to manage the database using java Swing.
    
  You can view contents of the database table 'User' and if you intend to, update the table contents using the 'Update' button
