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
import java.awt.GridLayout;
import javax.swing.JScrollPane;

public class HotelGUI {

	private JFrame frmHotelReservation;

	private static Connection connection; 
	private ResultSet rs;
	private JTable table;
	ListTableModel model;

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
		frmHotelReservation.setTitle("Hotel Reservation");
		frmHotelReservation.setBounds(100, 100, 809, 477);
		frmHotelReservation.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		frmHotelReservation.getContentPane().add(tabbedPane, BorderLayout.CENTER);

		JPanel panel = new JPanel();
		tabbedPane.addTab("Customer", null, panel, null);
		
		JButton btnGetRooms = new JButton("Get rooms");
		btnGetRooms.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					 rs = stmt.executeQuery("SELECT * FROM Room");
					 model = ListTableModel.createModelFromResultSet(rs);
					 table.setModel(model);
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		});
		panel.setLayout(new GridLayout(0, 2, 0, 0));
		panel.add(btnGetRooms);
		
		JButton btnNewButton = new JButton("Get rIDs");
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
		panel.add(btnNewButton);
		
		JScrollPane scrollPane = new JScrollPane();
		panel.add(scrollPane);
		
		table = new JTable();
		scrollPane.setViewportView(table);

		JPanel panel_1 = new JPanel();
		tabbedPane.addTab("Manager", null, panel_1, null);
	}
	
	private static void printResultSet(ResultSet rs) throws SQLException
	{
	   while(rs.next())
	   {
	      int id = rs.getInt("rID"); 
	      int type = rs.getInt("roomType");
	      System.out.println("rID:" + id + " Type:" + type); 
	   }
	}

}
