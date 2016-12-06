import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.activation.DataSource;
import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.BorderLayout;
import java.awt.Dialog.ModalExclusionType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;


public class initFrame extends JDialog {

	private JFrame frame;
	private JTextField txtRoot;
	private JTextField txtPassword;
	MysqlDataSource ds;
	
	/**
	 * Create the application.
	 */
	public initFrame() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Username:");
		lblNewLabel.setBounds(10, 42, 64, 14);
		frame.getContentPane().add(lblNewLabel);
		
		JLabel label = new JLabel("Username:");
		label.setBounds(10, 67, 64, 14);
		frame.getContentPane().add(label);
		
		JLabel lblEnterDatabaseInformation = new JLabel("Enter Database information");
		lblEnterDatabaseInformation.setBounds(10, 17, 161, 14);
		frame.getContentPane().add(lblEnterDatabaseInformation);
		
		txtRoot = new JTextField();
		txtRoot.setText("root");
		txtRoot.setBounds(84, 39, 86, 20);
		frame.getContentPane().add(txtRoot);
		txtRoot.setColumns(10);
		
		txtPassword = new JTextField();
		txtPassword.setText("password");
		txtPassword.setColumns(10);
		txtPassword.setBounds(84, 64, 86, 20);
		frame.getContentPane().add(txtPassword);
		
		JButton goButton = new JButton("Go!!!");
		goButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				ds = (MysqlDataSource) DataSourceFactory.getMySQLDataSource(txtRoot.getText(), txtPassword.getText());
				frame.dispose();
			}
		});
		
		
		goButton.setBounds(38, 92, 89, 23);
		frame.getContentPane().add(goButton);
		frame.setPreferredSize(new Dimension(200,200));
		frame.pack();
		frame.setVisible(true);
	}
	
	public MysqlDataSource getDS() {
		return ds;
	}
}
