package kroaming_swing;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.event.*;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

import com.sun.rowset.CachedRowSetImpl;

public class TableEditor extends JPanel {
	// Dialog box for inserting a new record
	private NewRecordDialog newRecordDialog = new NewRecordDialog();
	
	// JDBC Connection
	private Connection connection;
	
	// Table name
	private String tableName;
	
	// JDBC statement
	private Statement statement;
	
	// ResultSet for the table
	private ResultSet resultSet;
	
	// Table model
	private DefaultTableModel tableModel = new DefaultTableModel();
	
	// Table selection model
	private DefaultListSelectionModel listSelectionModel = new DefaultListSelectionModel();
	
	// New row vectors
	private Vector rowVectors = new Vector();
	
	// Column count
	private int columnCount;
	
	// columnHeaderVector to hold column names 
	private Vector columnHeaderVector = new Vector();
	
	private JButton jbtFirst = new JButton("First");
	private JButton jbtNext = new JButton("Next");
	private JButton jbtPrior = new JButton("Prior");
	private JButton jbtLast = new JButton("Last");
	private JButton jbtInsert = new JButton("Insert");
	private JButton jbtDelete = new JButton("Delete");
	private JButton jbtUpdate = new JButton("Update");
	
	private JLabel jlblStatus = new JLabel();
	private JTable jTable1 = new JTable();

	/** Create a TableEditor */
	public TableEditor() {
		jTable1.setModel(tableModel);
		jTable1.setSelectionModel(listSelectionModel);
		
		JPanel jPanel1 = new JPanel();
		jPanel1.add(jbtFirst);
		jPanel1.add(jbtNext);
		jPanel1.add(jbtPrior);
		jPanel1.add(jbtLast);
		jPanel1.add(jbtInsert);
		jPanel1.add(jbtDelete);
		jPanel1.add(jbtUpdate);
		
		setLayout(new BorderLayout());
		add(jPanel1, BorderLayout.NORTH);
		add(new JScrollPane(jTable1), BorderLayout.CENTER);
		add(jlblStatus, BorderLayout.SOUTH);

		// Register listeners
		jbtFirst.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				moveCursor("first");
			}
		});
		jbtNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				moveCursor("next");
			}
		});
		jbtPrior.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				moveCursor("previous");
			}
		});
		jbtLast.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				moveCursor("last");
			}
		});
		jbtInsert.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				try {
					insert();
				} catch (NullPointerException e) {
					e.printStackTrace();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		});
		jbtDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				delete();
			}
		});
		jbtUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				update();
			}
		});
		listSelectionModel.addListSelectionListener(
			new ListSelectionListener() {
				public void valueChanged(ListSelectionEvent e) {
					handleSelectionValueChanged(e);
				}
			});
		}

	/* Delete a row */
	private void delete() {
		try {
			// Delete the record from the database
			resultSet.deleteRow();
			refreshResultSet();
			tableModel.removeRow(listSelectionModel.getLeadSelectionIndex());
		} catch (Exception ex) {
			jlblStatus.setText(ex.toString());
		}
	}
	
	private void insert() throws SQLException, NullPointerException {
		// Display the dialog box
		newRecordDialog.displayTable(columnHeaderVector);
		Vector<?> newRecord = newRecordDialog.getNewRecord();
		
		if(newRecord == null) return;
		
		// Insert the record to the swing table
		tableModel.addRow(newRecord);
		
		// INsert the record to the database table
		try {
			for(int i = 1; i <= columnCount; i++) {
				resultSet.updateObject(i, newRecord.elementAt(i - 1));
			}
			
			resultSet.insertRow();
			refreshResultSet();
		} catch(Exception ex) {
			jlblStatus.setText(ex.toString());
		}
	} 

	/** Set cursor in the table and set the row number in the status */
	private void setTableCursor() throws Exception {
		int row = resultSet.getRow();
		listSelectionModel.setSelectionInterval(row - 1, row - 1);
		jlblStatus.setText("Current row number: " + row);
	}
	
	private void update() {
		try {
			// Get the current row
			int row = jTable1.getSelectedRow();
			
			// Gather data from the UI and update the database fields
			for(int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
				resultSet.updateObject(i, tableModel.getValueAt(row, i - 1));
			}
			
			// Invoke the update method in the result set
			resultSet.updateRow();
			refreshResultSet();
		} catch(Exception ex) {
			jlblStatus.setText(ex.toString());
		}
	}


	/** Move cursor to the specified location */
	private void moveCursor(String whereToMove) {
		try {
			if (whereToMove.equals("first"))
			    resultSet.first();
			else if (whereToMove.equals("next"))
				resultSet.next();
			else if (whereToMove.equals("previous"))
				resultSet.previous();
			else if (whereToMove.equals("last"))
				resultSet.last();
			setTableCursor();
		} catch (Exception ex) {
			jlblStatus.setText(ex.toString());
		}
	}

	// Refresh the result set
	private void refreshResultSet() {
		try {
			resultSet = statement.executeQuery(
					"SELECT * FROM " + tableName);
			// Set the cursor to the first record in the table
			moveCursor("first");
		} catch(SQLException ex) {
			ex.printStackTrace();
		}
	}

	// set database connection and table name in the TableEditor
	public void setConnectionAndTable(Connection newConnection, String newTableName) {
		connection = newConnection;
		tableName = newTableName;
		
		try {
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			showTable();
			moveCursor("first");
		} catch(SQLException ex) {
			ex.printStackTrace();
		}
	}
	
	// Display database table to the swing table
	private void showTable() throws SQLException {
		// Clear vectors to store data for a new table
		rowVectors.clear();
		columnHeaderVector.clear();
		
		// Obtain table contents
		resultSet = statement.executeQuery(
				"SELECT * FROM " + tableName + ";");
		
		// Get the column count
		columnCount = resultSet.getMetaData().getColumnCount();
		
		// Store rows to rowVectors
		while(resultSet.next()) {
			Vector singleRow = new Vector();
			for(int i = 0; i < columnCount; i++) {
				singleRow.addElement(resultSet.getObject(i + 1));
			}
			rowVectors.addElement(singleRow);
		}
		
		for(int i = 1; i <= columnCount; i++)
			columnHeaderVector.addElement(resultSet.getMetaData().getColumnName(i));
		
		tableModel.setDataVector(rowVectors, columnHeaderVector);
	}
	
	/** Handle the selection in the table */
	private void handleSelectionValueChanged(ListSelectionEvent e) { 
		int selectedRow = jTable1.getSelectedRow();
		try {
			resultSet.absolute(selectedRow + 1);
			setTableCursor();
		} catch(Exception ex) {
			jlblStatus.setText(ex.toString());
		}
	}
}