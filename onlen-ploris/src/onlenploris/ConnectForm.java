package onlenploris;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class ConnectForm extends JDialog implements ActionListener {
	
	public static final int CONNECT_SUCCESS = 1;
	public int DIALOG_STATE;
	
	private JTextField input_host, input_port, input_db_name, input_username;
	private JPasswordField input_password;
	
	private JButton button_connect, button_clear;
	
	public ConnectForm(MainFrame parent) {
		super(parent);
		
		setTitle("Connect to Database");
		setLayout(new BorderLayout(0, 25));
		
		form();
		
		setMinimumSize(new Dimension(480, 190));
		setResizable(false);
		
		setAlwaysOnTop(true);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setModalityType(DEFAULT_MODALITY_TYPE);
		
		setVisible(true);		
	}
	
	private void form() {
		input_host = new JTextField();
		input_port = new JTextField();
		input_db_name = new JTextField();
		input_username = new JTextField();
		input_password = new JPasswordField();
		
		button_connect = new JButton("Connect");
		button_clear = new JButton("Clear");
		button_connect.addActionListener(this);
		button_clear.addActionListener(this);
		
		JPanel panel_form = new JPanel(new GridLayout(5, 2));	
		panel_form.add(new JLabel("Host"));
		panel_form.add(input_host);		
		panel_form.add(new JLabel("Port"));
		panel_form.add(input_port);	
		panel_form.add(new JLabel("DB Name"));
		panel_form.add(input_db_name);	
		panel_form.add(new JLabel("Username"));
		panel_form.add(input_username);	
		panel_form.add(new JLabel("Password"));
		panel_form.add(input_password);
		
		JPanel panel_button = new JPanel(new FlowLayout());
		panel_button.add(button_connect);
		panel_button.add(button_clear);
		
		add(panel_form, BorderLayout.CENTER);
		add(panel_button, BorderLayout.SOUTH);
	}
	
	private void form_connect() {
		String host = input_host.getText();
		String port = input_port.getText();
		String db_name = input_db_name.getText();
		String username = input_username.getText();
		String password = new String(input_password.getPassword());
		
		if(host.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Host must be filled", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		if(port.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Port must be filled", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		if(db_name.isEmpty()) {
			JOptionPane.showMessageDialog(this, "DB Name must be filled", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		if(username.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Username must be filled", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		try {
			DatabaseConnection.connect(host, port, db_name, username, password);
			JOptionPane.showMessageDialog(this, "Connected to database", "Success", JOptionPane.INFORMATION_MESSAGE);
			DIALOG_STATE = CONNECT_SUCCESS;
			dispose();
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "Can't connect to database", "Error", JOptionPane.ERROR_MESSAGE);
			form_clear();
		}
	}
	
	private void form_clear() {
		input_host.setText("");
		input_port.setText("");
		input_db_name.setText("");
		input_username.setText("");
		input_password.setText("");
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if(arg0.getSource() == button_connect) {
			form_connect();
		}
		else if(arg0.getSource() == button_clear) {
			form_clear();
		}
	}
}