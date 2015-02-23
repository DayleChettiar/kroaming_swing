This application allows us to manage usage information for a particular user. 

step 1: extract the jar to a particular directory on your file system
step 2: from the command line, run the 'scriptSQL.sql' file as follows:
          >> source <path to the scriptSQL.sql file>/scriptSQL.sql 
  This creates a database named 'UserUsage' and a table named 'User' in it with user = root and password = test
          
step 3: from the command line, run the 'insertSQL.sql' file as follows:
          >> source <path to the insertSQL.sql file>/insertSQL.sql 
  This inserts values into a database named 'UserUsage' and a table named 'User' in it with user = root and password = test
          
step 4: Now open the application named 'kroaming_swing' on an IDE of your choice as a java application by running the files:
    SQLClient.java. Insert the username 'root' and password 'test' and table name 'User'.
    Connect to the database to manage the database using java Swing.

        