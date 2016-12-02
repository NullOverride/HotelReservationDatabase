import java.awt.EventQueue;
import java.sql.*;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import com.mysql.jdbc.Statement;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.sql.DataSource;
import javax.swing.JButton;
import javax.swing.JTable;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JComboBox;

public class HotelGUI {

	private JFrame frmHotelReservation;

	private static Connection connection; 
	private ResultSet rs;
	private JTable table;
	ListTableModel model;
	private JTable table_1;
	private JTextField txtJohn;
	private JTextField txtFakeStreet;
	private JTextField textField;
	private JTextField txtAacom;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		DataSource ds = DataSourceFactory.getMySQLDataSource();     

		try {
			connection = ds.getConnection();

		} catch (SQLException e) {
			System.out.println("Connection Failed! Check output console");
			e.printStackTrace();
			return;
		}

		if (connection != null) {
			System.out.println("You made it, take control your database now!");

		} else {
			System.out.println("Failed to make connection!");
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					HotelGUI window = new HotelGUI();
					window.frmHotelReservation.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});


	}

	/**
	 * Create the application.
	 * @throws SQLException 
	 */
	public HotelGUI() throws SQLException {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 * @throws SQLException 
	 */
	private void initialize() throws SQLException {

		Statement stmt = (Statement) connection.createStatement();

		frmHotelReservation = new JFrame();
		frmHotelReservation.setResizable(false);
		frmHotelReservation.setTitle("Hotel Reservation");
		frmHotelReservation.setBounds(100, 100, 1018, 539);
		frmHotelReservation.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		frmHotelReservation.getContentPane().add(tabbedPane, BorderLayout.CENTER);

		// Customer Tab
		JPanel panel = new JPanel();
		tabbedPane.addTab("Customer", null, panel, null);

		//Add New Customer Labels and Text Fields
		JLabel lblAddNewCustomer = new JLabel("Add New Customer:");

		txtJohn = new JTextField();
		txtJohn.setText("John");
		txtJohn.setToolTipText("Name");
		txtJohn.setColumns(10);

		txtFakeStreet = new JTextField();
		txtFakeStreet.setToolTipText("Address");
		txtFakeStreet.setText("123 Fake Street");
		txtFakeStreet.setColumns(10);

		textField = new JTextField();
		textField.setToolTipText("Phone Number");
		textField.setText("123456789");
		textField.setColumns(10);

		txtAacom = new JTextField();
		txtAacom.setToolTipText("Email");
		txtAacom.setText("a@a.com");
		txtAacom.setColumns(10);

		JComboBox comboBox = new JComboBox();

		//Create Table for Customer Queries
		table = new JTable();

		//Buttons
		JButton btnGetRooms = new JButton("Get open rooms");
		btnGetRooms.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					rs = stmt.executeQuery("SELECT rID, class, price FROM room JOIN roomtype WHERE Room.roomType = roomtype.typeID AND assignedTo IS NULL");
					model = ListTableModel.createModelFromResultSet(rs);
					table.setModel(model);
					for (int i = 0; i < model.getRowCount(); i++) {
						comboBox.addItem(model.getValueAt(i, 0)); 
					}
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		});
		JButton btnNewButton = new JButton("Add new customer");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(comboBox.getSelectedItem() == null) {
					JOptionPane popup = new JOptionPane();
					popup.showMessageDialog(frmHotelReservation.getContentPane(), "Room cannot be blank. Run get empty rooms to populate rooms.");
				} 
				else {
					try {
						rs = stmt.executeQuery("SELECT email FROM customer WHERE email = '"+ txtAacom.getText() + "'");
						if (rs.next()) {
							JOptionPane popup = new JOptionPane();
							popup.showMessageDialog(frmHotelReservation.getContentPane(), "Duplicate email found. Please use another email.");
						}
						else {
							stmt.executeUpdate("INSERT INTO customer(name,address,phone,email,room) VALUES(" + "'" + txtJohn.getText() + "','" 
									+ txtFakeStreet.getText() + "'," 
									+ textField.getText() + ",'" 
									+ txtAacom.getText() +"'," 
									+ comboBox.getSelectedItem() + ")"  
									);
							table.setModel(model);
						}
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
				}
			}
		});		

		JButton btnCancelReservation = new JButton("Cancel Reservation");
		btnCancelReservation.setEnabled(false);
		btnCancelReservation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					stmt.executeUpdate("DELETE FROM customer WHERE email = '"+ txtAacom.getText() + "'");
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				table.setModel(model);
				btnCancelReservation.setEnabled(false);
			}
		});

		JButton btnGetReservation = new JButton("Get Reservation");
		btnGetReservation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(txtAacom.getText() == null || txtAacom.getText().length() == 0) {
					JOptionPane popup = new JOptionPane();
					popup.showMessageDialog(frmHotelReservation.getContentPane(), "Please enter an email to check your reservation.");
				}
				else {

					try {
						rs = stmt.executeQuery("SELECT cID, name, room FROM customer WHERE email = '" + txtAacom.getText() +"'");
						if (!rs.isBeforeFirst()) {
							JOptionPane popup = new JOptionPane();
							popup.showMessageDialog(frmHotelReservation.getContentPane(), "Reservation Not Found.");
						}
						else {
							model = ListTableModel.createModelFromResultSet(rs);
							table.setModel(model);
							btnCancelReservation.setEnabled(true);
						}
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
				}
			}
		});




		//MANAGER TAB
		JPanel panel_1 = new JPanel();
		tabbedPane.addTab("Manager", null, panel_1, null);

		//Create table for manager queries
		table_1 = new JTable();


		JButton button_1 = new JButton("Get Customer Requests");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					rs = stmt.executeQuery("SELECT cID, name, room FROM customer WHERE room IS NOT NULL ORDER BY cID ASC");
					model = ListTableModel.createModelFromResultSet(rs);
					table_1.setModel(model);
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}

		});

		////////////////////////////////////////////////////
		//FORMATTING DON'T TOUCH
		////////////////////////////////////////////////////

		JScrollPane scrollPane = new JScrollPane();






		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
				gl_panel.createParallelGroup(Alignment.LEADING)
				.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 1007, GroupLayout.PREFERRED_SIZE)
				.addGroup(gl_panel.createSequentialGroup()
						.addContainerGap()
						.addComponent(btnGetRooms)
						.addGap(10)
						.addComponent(lblAddNewCustomer)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(txtJohn, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(txtFakeStreet, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(txtAacom, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(comboBox, GroupLayout.PREFERRED_SIZE, 47, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
								.addComponent(btnCancelReservation)
								.addComponent(btnGetReservation)
								.addComponent(btnNewButton))
						.addGap(236))
				);
		gl_panel.setVerticalGroup(
				gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
						.addGap(16)
						.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblAddNewCustomer)
								.addComponent(txtJohn, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(txtFakeStreet, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(txtAacom, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(btnGetRooms)
								.addComponent(comboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(btnNewButton, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(btnGetReservation)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(btnCancelReservation)
						.addPreferredGap(ComponentPlacement.RELATED, 251, Short.MAX_VALUE)
						.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 135, GroupLayout.PREFERRED_SIZE))
				);
		panel.setLayout(gl_panel);
		scrollPane.setViewportView(table);


		JScrollPane scrollPane_1 = new JScrollPane();
		GroupLayout gl_panel_1 = new GroupLayout(panel_1);
		gl_panel_1.setHorizontalGroup(
				gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addComponent(scrollPane_1, GroupLayout.DEFAULT_SIZE, 1007, Short.MAX_VALUE)
				.addGroup(gl_panel_1.createSequentialGroup()
						.addGap(10)
						.addComponent(button_1, GroupLayout.PREFERRED_SIZE, 192, GroupLayout.PREFERRED_SIZE)
						.addContainerGap())
				);
		gl_panel_1.setVerticalGroup(
				gl_panel_1.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel_1.createSequentialGroup()
						.addContainerGap()
						.addComponent(button_1)
						.addPreferredGap(ComponentPlacement.RELATED, 314, Short.MAX_VALUE)
						.addComponent(scrollPane_1, GroupLayout.PREFERRED_SIZE, 135, GroupLayout.PREFERRED_SIZE))
				);
		scrollPane_1.setViewportView(table_1);
		panel_1.setLayout(gl_panel_1);

	}
}
