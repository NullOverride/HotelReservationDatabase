import java.awt.EventQueue;
import java.sql.*;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import com.mysql.jdbc.Statement;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JCheckBox;
import javax.sql.DataSource;
import javax.swing.JButton;

public class HotelGUI {

	private JFrame frmHotelReservation;

	private static Connection connection =  null; 

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
		ResultSet rs = null;
		
	
		Statement stmt = (Statement) connection.createStatement();
		
		rs = stmt.executeQuery("SELECT * FROM Room");
		printResultSet(rs);
	
		frmHotelReservation = new JFrame();
		frmHotelReservation.setTitle("Hotel Reservation");
		frmHotelReservation.setBounds(100, 100, 809, 477);
		frmHotelReservation.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		frmHotelReservation.getContentPane().add(tabbedPane, BorderLayout.CENTER);

		JPanel panel = new JPanel();
		tabbedPane.addTab("Customer", null, panel, null);

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
