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
import javax.swing.JSeparator;

public class HotelGUI {

	private JFrame frmHotelReservation;

	private static Connection connection; 
	private ResultSet rs;
	private JTable cstmrTable;
	ListTableModel model;
	private JTable mngrTable;
	private JTextField addName;
	private JTextField addStreet;
	private JTextField addPhone;
	private JTextField addEmail;
	private JTextField emailField;
	private JTextField phoneField;
	private JTextField addressField;
	private JTextField nameField;
	private JTextField textField;
	private JTextField roomNumber;

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

		addName = new JTextField();
		addName.setText("John");
		addName.setToolTipText("Name");
		addName.setColumns(10);

		addStreet = new JTextField();
		addStreet.setToolTipText("Address");
		addStreet.setText("123 Fake Street");
		addStreet.setColumns(10);

		addPhone = new JTextField();
		addPhone.setToolTipText("Phone Number");
		addPhone.setText("123456789");
		addPhone.setColumns(10);

		addEmail = new JTextField();
		addEmail.setToolTipText("Email");
		addEmail.setText("a@a.com");
		addEmail.setColumns(10);

		JComboBox comboBox = new JComboBox();

		//Create Table for Customer Queries
		cstmrTable = new JTable();

		//Buttons
		JButton btnGetRooms = new JButton("Get open rooms");
		btnGetRooms.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					rs = stmt.executeQuery("SELECT rID, class, price FROM room JOIN roomtype WHERE Room.roomType = roomtype.typeID AND assignedTo IS NULL");
					model = ListTableModel.createModelFromResultSet(rs);
					cstmrTable.setModel(model);
					comboBox.removeAllItems();
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
						rs = stmt.executeQuery("SELECT email FROM customer WHERE email = '"+ addEmail.getText() + "'");
						if (rs.next()) {
							JOptionPane popup = new JOptionPane();
							popup.showMessageDialog(frmHotelReservation.getContentPane(), "Duplicate email found. Please use another email.");
						}
						else {
							stmt.executeUpdate("INSERT INTO customer(name,address,phone,email,room) VALUES(" + "'" + addName.getText() + "','" 
									+ addStreet.getText() + "'," 
									+ addPhone.getText() + ",'" 
									+ addEmail.getText() +"'," 
									+ comboBox.getSelectedItem() + ")"  
									);
							cstmrTable.setModel(model);
						}
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
				}
			}
		});		


		emailField = new JTextField();
		emailField.setToolTipText("Email");
		emailField.setColumns(10);

		phoneField = new JTextField();
		phoneField.setToolTipText("Phone");
		phoneField.setColumns(10);

		addressField = new JTextField();
		addressField.setToolTipText("Address");
		addressField.setColumns(10);

		nameField = new JTextField();
		nameField.setToolTipText("Name");
		nameField.setColumns(10);



		JLabel lblUpdateInfo = new JLabel("Update Info:");

		JButton btnUpdate = new JButton("Update!");
		btnUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(phoneField == null || addressField == null || nameField == null) {
					JOptionPane popup = new JOptionPane();
					popup.showMessageDialog(frmHotelReservation.getContentPane(), "Please fill in all of the empty spaces.");
				} 
				else {
					try {
						stmt.executeUpdate("UPDATE customer SET name='" + nameField.getText() + "', address= '" + addressField.getText() + "', phone= " + phoneField.getText() + " WHERE email = '" + emailField.getText() + "'");
						cstmrTable.setModel(model);
						JOptionPane popup = new JOptionPane();
						popup.showMessageDialog(frmHotelReservation.getContentPane(), "Successfully updated!");


					} catch (SQLException e1) {
						e1.printStackTrace();
						JOptionPane popup = new JOptionPane();
						popup.showMessageDialog(frmHotelReservation.getContentPane(), "Update failed... Check your fields.");
					}
				}
			}
		});
		JButton btnCancelReservation = new JButton("Cancel Reservation");
		btnCancelReservation.setEnabled(false);
		btnCancelReservation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					stmt.executeUpdate("DELETE FROM customer WHERE email = '"+ addEmail.getText() + "'");
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				cstmrTable.setModel(model);
				btnCancelReservation.setEnabled(false);
				lblUpdateInfo.setVisible(false);
				phoneField.setVisible(false);
				addressField.setVisible(false);
				nameField.setVisible(false);
				btnUpdate.setVisible(false);
			}
		});

		lblUpdateInfo.setVisible(false);
		phoneField.setVisible(false);
		addressField.setVisible(false);
		nameField.setVisible(false);
		btnUpdate.setVisible(false);

		JLabel lblEmail = new JLabel("Email:");

		JButton btnGetReservation = new JButton("Get Reservation");
		btnGetReservation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(emailField.getText() == null || emailField.getText().length() == 0) {
					JOptionPane popup = new JOptionPane();
					popup.showMessageDialog(frmHotelReservation.getContentPane(), "Please enter an email to check your reservation.");
				}
				else {

					try {
						rs = stmt.executeQuery("SELECT cID, name, room FROM customer WHERE email = '" + emailField.getText() +"'");
						if (!rs.isBeforeFirst()) {
							JOptionPane popup = new JOptionPane();
							popup.showMessageDialog(frmHotelReservation.getContentPane(), "Reservation Not Found.");
						} else {
							model = ListTableModel.createModelFromResultSet(rs);
							cstmrTable.setModel(model);
							btnCancelReservation.setEnabled(true);
							lblUpdateInfo.setVisible(true);
							phoneField.setVisible(true);
							addressField.setVisible(true);
							nameField.setVisible(true);
							btnUpdate.setVisible(true);
						}
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
				}
			}
		});


		JSeparator separator = new JSeparator();

		JLabel lblAcceptedCustomer = new JLabel("Accepted Customer");
		JLabel lblCid = new JLabel("cID:");

		textField = new JTextField();
		textField.setColumns(10);
		JComboBox comboBox_2 = new JComboBox();
		comboBox_2.addItem("CREDIT");
		comboBox_2.addItem("DEBIT");
		comboBox_2.setVisible(false);

		JButton btnPay = new JButton("Pay!");
		btnPay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnPay.setVisible(false);

		JButton btnGetInvoice = new JButton("Get Invoice");
		btnGetInvoice.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					rs = stmt.executeQuery("SELECT cID, balance from invoice WHERE cID = " + textField.getText() + " AND paid = 0");
					if (!rs.isBeforeFirst()) {
						JOptionPane popup = new JOptionPane();
						popup.showMessageDialog(frmHotelReservation.getContentPane(), "cID not found.");
					} else {
						model = ListTableModel.createModelFromResultSet(rs);
						cstmrTable.setModel(model);
						comboBox_2.setVisible(true);
						btnPay.setVisible(true);
					}
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		});




		//MANAGER TAB
		JPanel panel_1 = new JPanel();
		tabbedPane.addTab("Manager", null, panel_1, null);

		//Create cstmrTable for manager queries
		mngrTable = new JTable();

		JComboBox comboBox_1 = new JComboBox();
		JButton btnAssignRoom = new JButton("Assign Room");
		JButton btnDeleteRequest = new JButton("Delete Request");
		btnAssignRoom.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					rs = stmt.executeQuery("SELECT cID, rID FROM booking WHERE cID = " + comboBox_1.getSelectedItem().toString().substring(5, comboBox_1.getSelectedItem().toString().indexOf(" |")) 
							+ " AND rID = " + comboBox_1.getSelectedItem().toString().substring(comboBox_1.getSelectedItem().toString().indexOf("Room ") + 5) );
					if (rs.next())
					{
						JOptionPane popup = new JOptionPane();
						popup.showMessageDialog(frmHotelReservation.getContentPane(), "Booking already exists.");
					} else {

						stmt.executeUpdate("UPDATE room SET assignedTo = " + comboBox_1.getSelectedItem().toString().substring(5, comboBox_1.getSelectedItem().toString().indexOf(" |")) 
								+ " WHERE rID = " + comboBox_1.getSelectedItem().toString().substring(comboBox_1.getSelectedItem().toString().indexOf("Room ") + 5) );

						JOptionPane popup = new JOptionPane();
						popup.showMessageDialog(frmHotelReservation.getContentPane(), "Successfully updated!");
						btnAssignRoom.setEnabled(false);
						btnDeleteRequest.setEnabled(false);
					}
				} catch (SQLException e1) {
					JOptionPane popup = new JOptionPane();
					popup.showMessageDialog(frmHotelReservation.getContentPane(), "Update failed.");
					e1.printStackTrace();
				}
			}
		});
		btnAssignRoom.setEnabled(false);

		btnDeleteRequest.setEnabled(false);
		btnDeleteRequest.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					stmt.executeUpdate("DELETE FROM customer WHERE cID = " + comboBox_1.getSelectedItem().toString().substring(5, comboBox_1.getSelectedItem().toString().indexOf(" |")) 
							+ " AND room = " + comboBox_1.getSelectedItem().toString().substring(comboBox_1.getSelectedItem().toString().indexOf("Room ") + 5) );

					JOptionPane popup = new JOptionPane();
					popup.showMessageDialog(frmHotelReservation.getContentPane(), "Successfully deleted!");
					btnAssignRoom.setEnabled(false);
					btnDeleteRequest.setEnabled(false);
				} catch (SQLException e1) {
					JOptionPane popup = new JOptionPane();
					popup.showMessageDialog(frmHotelReservation.getContentPane(), "Delete failed.");
					e1.printStackTrace();
				}
			}
		});

		JButton button_1 = new JButton("Get Customer Requests");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					rs = stmt.executeQuery("SELECT cID, name, room FROM customer WHERE room IS NOT NULL ORDER BY cID ASC");
					if(!rs.isBeforeFirst()) {
						JOptionPane popup = new JOptionPane();
						popup.showMessageDialog(frmHotelReservation.getContentPane(), "No customer requests.");
					} else {
						model = ListTableModel.createModelFromResultSet(rs);
						mngrTable.setModel(model);
						btnAssignRoom.setEnabled(true);
						btnDeleteRequest.setEnabled(true);
						comboBox_1.removeAllItems();
						for (int i = 0; i < model.getRowCount(); i++) {
							comboBox_1.addItem("CID: " + model.getValueAt(i, 0).toString() + " | " + model.getValueAt(i, 1).toString() + " - Room " + model.getValueAt(i, 2).toString()); 
						}
					}
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}

		});
		JButton btnGetBookedRooms = new JButton("Get Booked Rooms");
		btnGetBookedRooms.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					rs = stmt.executeQuery("SELECT * from booking WHERE rID in (SELECT rID from room WHERE assignedTo IS NOT NULL)");
					model = ListTableModel.createModelFromResultSet(rs);
					mngrTable.setModel(model);
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		});

		JButton btnGetOf = new JButton("Get # of Open Rooms");
		btnGetOf.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					rs = stmt.executeQuery("SELECT count(*) from room WHERE assignedTo IS NULL");
					model = ListTableModel.createModelFromResultSet(rs);
					mngrTable.setModel(model);
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		});

		JButton btnCountCustomersGroup = new JButton("Show room tpyes with 3 or more occupied rooms");
		btnCountCustomersGroup.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					rs = stmt.executeQuery("SELECT class, count(rID) from room JOIN roomtype ON room.roomType = roomType.typeID WHERE assignedTo IS NOT NULL GROUP BY class HAVING count(rID) >= 3");
					model = ListTableModel.createModelFromResultSet(rs);
					mngrTable.setModel(model);
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		});


		JLabel lblRoomNumber = new JLabel("Room Number:");

		JComboBox classType = new JComboBox();
		classType.addItem("economy");
		classType.addItem("luxury");
		classType.addItem("middle");

		roomNumber = new JTextField();
		roomNumber.setColumns(10);

		JButton btnAddRoom = new JButton("Add Room");
		btnAddRoom.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					rs = stmt.executeQuery("SELECT rID FROM room WHERE rID = " + roomNumber.getText());
					if(rs.next()) {
						JOptionPane popup = new JOptionPane();
						popup.showMessageDialog(frmHotelReservation.getContentPane(), "Room already exists.");
					} else {
						stmt.executeUpdate("INSERT INTO room(rID, roomType) VALUES (" + roomNumber.getText() + ", " + (classType.getSelectedIndex() + 1) + ")");
						JOptionPane popup = new JOptionPane();
						popup.showMessageDialog(frmHotelReservation.getContentPane(), "Room successfully created!");
					}
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		});

		JButton btnDeleteRoom = new JButton("Delete Room");
		btnDeleteRoom.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					rs = stmt.executeQuery("SELECT rID FROM room WHERE rID = " + roomNumber.getText() + " AND assignedTo IS NOT NULL");
					if(rs.next()) {
						JOptionPane popup = new JOptionPane();
						popup.showMessageDialog(frmHotelReservation.getContentPane(), "Cannot delete a room that has a person in it.");
					} else {
						stmt.executeUpdate("DELETE FROM room WHERE rID = " + roomNumber.getText() + " AND assignedTo IS NULL");
						JOptionPane popup = new JOptionPane();
						popup.showMessageDialog(frmHotelReservation.getContentPane(), "Room successfully deleted!");
					}
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		});

		JButton btnGetPaidCustomers = new JButton("Get Customers Who Did Not Pay");
		btnGetPaidCustomers.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					rs = stmt.executeQuery("SELECT name, paid FROM invoice LEFT OUTER JOIN customer ON invoice.cID = customer.cID AND paid = 0 ORDER BY name DESC;");
					model = ListTableModel.createModelFromResultSet(rs);
					mngrTable.setModel(model);
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
				.addGroup(gl_panel.createSequentialGroup()
						.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING)
								.addGroup(gl_panel.createSequentialGroup()
										.addComponent(btnGetRooms)
										.addGap(10)
										.addComponent(lblAddNewCustomer)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(addName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(addStreet, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
								.addComponent(lblEmail)
								.addGroup(gl_panel.createSequentialGroup()
										.addComponent(lblUpdateInfo)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(nameField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(addressField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
						.addPreferredGap(ComponentPlacement.RELATED)
						.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_panel.createSequentialGroup()
										.addComponent(addPhone, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(addEmail, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(comboBox, GroupLayout.PREFERRED_SIZE, 47, GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(btnNewButton))
								.addGroup(gl_panel.createSequentialGroup()
										.addComponent(emailField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(btnGetReservation)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(btnCancelReservation))
								.addGroup(gl_panel.createSequentialGroup()
										.addComponent(phoneField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(btnUpdate)))
						.addGap(246))
				.addGroup(gl_panel.createSequentialGroup()
						.addComponent(separator, GroupLayout.DEFAULT_SIZE, 1007, Short.MAX_VALUE)
						.addContainerGap())
				.addGroup(gl_panel.createSequentialGroup()
						.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 1007, GroupLayout.PREFERRED_SIZE)
						.addContainerGap())
				.addGroup(gl_panel.createSequentialGroup()
						.addContainerGap()
						.addComponent(lblAcceptedCustomer)
						.addContainerGap(913, Short.MAX_VALUE))
				.addGroup(gl_panel.createSequentialGroup()
						.addContainerGap()
						.addComponent(lblCid)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(btnGetInvoice)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(comboBox_2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(btnPay)
						.addContainerGap(675, Short.MAX_VALUE))
				);
		gl_panel.setVerticalGroup(
				gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
						.addGap(16)
						.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblAddNewCustomer)
								.addComponent(addName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(addStreet, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(addPhone, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(addEmail, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(btnGetRooms)
								.addComponent(comboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(btnNewButton, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(ComponentPlacement.RELATED)
						.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblEmail)
								.addComponent(emailField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(btnGetReservation)
								.addComponent(btnCancelReservation))
						.addPreferredGap(ComponentPlacement.RELATED)
						.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
								.addComponent(phoneField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(addressField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(nameField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblUpdateInfo)
								.addComponent(btnUpdate))
						.addGap(20)
						.addComponent(separator, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(lblAcceptedCustomer)
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblCid)
								.addComponent(textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(btnGetInvoice)
								.addComponent(comboBox_2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(btnPay))
						.addPreferredGap(ComponentPlacement.RELATED, 178, Short.MAX_VALUE)
						.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 135, GroupLayout.PREFERRED_SIZE))
				);
		panel.setLayout(gl_panel);
		scrollPane.setViewportView(cstmrTable);


		JScrollPane scrollPane_1 = new JScrollPane();






		GroupLayout gl_panel_1 = new GroupLayout(panel_1);
		gl_panel_1.setHorizontalGroup(
				gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup()
						.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_panel_1.createSequentialGroup()
										.addGap(10)
										.addComponent(button_1, GroupLayout.PREFERRED_SIZE, 192, GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(comboBox_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(btnAssignRoom)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(btnDeleteRequest))
								.addComponent(scrollPane_1, GroupLayout.PREFERRED_SIZE, 1005, GroupLayout.PREFERRED_SIZE)
								.addGroup(gl_panel_1.createSequentialGroup()
										.addContainerGap()
										.addComponent(btnGetBookedRooms)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(btnGetOf)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(btnCountCustomersGroup))
								.addGroup(gl_panel_1.createSequentialGroup()
										.addContainerGap()
										.addComponent(lblRoomNumber)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(roomNumber, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(classType, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(btnAddRoom)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(btnDeleteRoom))
								.addGroup(gl_panel_1.createSequentialGroup()
										.addContainerGap()
										.addComponent(btnGetPaidCustomers)))
						.addContainerGap(2, Short.MAX_VALUE))
				);
		gl_panel_1.setVerticalGroup(
				gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup()
						.addContainerGap()
						.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE)
								.addComponent(button_1)
								.addComponent(comboBox_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(btnAssignRoom)
								.addComponent(btnDeleteRequest))
						.addPreferredGap(ComponentPlacement.RELATED)
						.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE)
								.addComponent(btnGetBookedRooms)
								.addComponent(btnGetOf)
								.addComponent(btnCountCustomersGroup))
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblRoomNumber)
								.addComponent(roomNumber, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(classType, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(btnAddRoom)
								.addComponent(btnDeleteRoom))
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(btnGetPaidCustomers)
						.addPreferredGap(ComponentPlacement.RELATED, 222, Short.MAX_VALUE)
						.addComponent(scrollPane_1, GroupLayout.PREFERRED_SIZE, 135, GroupLayout.PREFERRED_SIZE))
				);
		scrollPane_1.setViewportView(mngrTable);
		panel_1.setLayout(gl_panel_1);

	}
}
