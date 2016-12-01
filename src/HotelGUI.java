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

public class HotelGUI {

	private JFrame frmHotelReservation;

	private static Connection connection; 
	private ResultSet rs;
	private JTable table;
	ListTableModel model;
	private JTable table_1;

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
		frmHotelReservation.setBounds(100, 100, 829, 477);
		frmHotelReservation.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		frmHotelReservation.getContentPane().add(tabbedPane, BorderLayout.CENTER);
		
		// Customer Tab
		JPanel panel = new JPanel();
		tabbedPane.addTab("Customer", null, panel, null);
		
		//Create buttons
		JButton btnGetRooms = new JButton("Get open rooms");
		JButton btnNewButton = new JButton("Get rIDs");
		
		//Create Table for Customer Queries
		table = new JTable();
		
		//Button functions
		btnGetRooms.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					 rs = stmt.executeQuery("SELECT rID, class, price FROM room JOIN roomtype WHERE Room.roomType = roomtype.typeID AND assignedTo IS NULL");
					 model = ListTableModel.createModelFromResultSet(rs);
					 table.setModel(model);
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		});
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					 rs = stmt.executeQuery("SELECT rID FROM Room");
					 model = ListTableModel.createModelFromResultSet(rs);
					 table.setModel(model);
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		});
		
		//MANAGER TAB
		JPanel panel_1 = new JPanel();
		tabbedPane.addTab("Manager", null, panel_1, null);
		
		//Create buttons
		JButton button = new JButton("Get rooms");

		//Create table for manager queries
		table_1 = new JTable();
		

		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					 rs = stmt.executeQuery("SELECT rID FROM Room");
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
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addComponent(btnGetRooms)
					.addComponent(btnNewButton)
					.addGap(618))
				.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 818, GroupLayout.PREFERRED_SIZE)
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnGetRooms)
						.addComponent(btnNewButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
					.addPreferredGap(ComponentPlacement.RELATED, 252, Short.MAX_VALUE)
					.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 135, GroupLayout.PREFERRED_SIZE))
		);
		panel.setLayout(gl_panel);
		scrollPane.setViewportView(table);
		
		
		JScrollPane scrollPane_1 = new JScrollPane();
		GroupLayout gl_panel_1 = new GroupLayout(panel_1);
		gl_panel_1.setHorizontalGroup(
			gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addContainerGap()
					.addComponent(button)
					.addContainerGap(707, Short.MAX_VALUE))
				.addComponent(scrollPane_1, GroupLayout.DEFAULT_SIZE, 798, Short.MAX_VALUE)
		);
		gl_panel_1.setVerticalGroup(
			gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addContainerGap()
					.addComponent(button)
					.addPreferredGap(ComponentPlacement.RELATED, 252, Short.MAX_VALUE)
					.addComponent(scrollPane_1, GroupLayout.PREFERRED_SIZE, 135, GroupLayout.PREFERRED_SIZE))
		);
		scrollPane_1.setViewportView(table_1);
		panel_1.setLayout(gl_panel_1);

	}

}
