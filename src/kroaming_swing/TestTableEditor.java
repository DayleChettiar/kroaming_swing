package kroaming_swing;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;


public class TestTableEditor extends JApplet {
	private JComboBox jcboDriver = new JComboBox(new String[] {
			"com.mysql.jdbc.Driver"
	});

	private JComboBox jcboURL = new JComboBox(new String[] {
			"jdbc:mysql://localhost:3306/UserUsage"
	});

	private JButton jbtConnect = new JButton("Connect to DB & Get Table");
	private JTextField jtfUserName = new JTextField();
	private JPasswordField jpfPassword = new JPasswordField();
	private JTextField jtfTableName = new JTextField();
	private TableEditor tableEditor1 = new TableEditor();
	private JLabel jlblStatus = new JLabel();
	
	/** Creates new form TestTableEditor */
	public TestTableEditor() {
		JPanel jPanel1 = new JPanel(new GridLayout(5, 0));
		jPanel1.add(jcboDriver);
		jPanel1.add(jcboURL);
		jPanel1.add(jtfUserName);
		jPanel1.add(jpfPassword);
		jPanel1.add(jtfTableName);

		JPanel jPanel2 = new JPanel(new GridLayout(5, 0));
		jPanel2.add(new JLabel("JDBC Driver"));
		jPanel2.add(new JLabel("Database URL"));
		jPanel2.add(new JLabel("Username"));
		jPanel2.add(new JLabel("Password"));
		jPanel2.add(new JLabel("Table Name"));

		JPanel jPanel3 = new JPanel(new BorderLayout());
		jPanel3.add(jbtConnect, BorderLayout.SOUTH);
		jPanel3.add(jPanel2, BorderLayout.WEST);
		jPanel3.add(jPanel1, BorderLayout.CENTER);
		tableEditor1.setPreferredSize(new Dimension(400, 200));

		jcboURL.setEditable(true);
		jcboDriver.setEditable(true);
		
		add(new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
				jPanel3, tableEditor1), BorderLayout.CENTER);
		add(jlblStatus, BorderLayout.SOUTH);

		jbtConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				try {
					// Connect to the database
					Connection connection = getConnection();
					tableEditor1.setConnectionAndTable(connection, jtfTableName.getText().trim());
				} catch (Exception ex) {
					jlblStatus.setText(ex.toString());
				}
			}
		});
	}
	
	// Connect to the database
	private Connection getConnection() throws Exception {
		// Load the JDBC
		System.out.println((String)jcboDriver.getSelectedItem());
		Class.forName(((String)jcboDriver.getSelectedItem()).trim());
		System.out.println("Driver Loaded");
		
		// Establish a connection
		Connection connection = DriverManager.getConnection(((String)jcboURL.getSelectedItem()).trim(), 
				jtfUserName.getText().trim(),
				new String(jpfPassword.getPassword()));
		jlblStatus.setText("Database connected");
		
		return connection;
	}
}