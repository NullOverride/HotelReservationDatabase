import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import java.awt.BorderLayout;
import javax.swing.JPanel;

public class HotelGUI {

	private JFrame frmHotelReservation;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
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
	 */
	public HotelGUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
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

}
