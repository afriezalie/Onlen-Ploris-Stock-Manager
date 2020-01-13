package onlenploris.ftype;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import onlenploris.MainFrame;

public class FtypeAddDialog extends JDialog implements ActionListener {

	public static final int INSERT_SUCCESS = 1;
	public static final int INSERT_CANCEL = 0;
	public int DIALOG_STATE = Integer.MIN_VALUE;
	
	private JTextField input_name;
	private JButton button_insert, button_cancel;
	
	public FtypeAddDialog(MainFrame parent) {
		super(parent);
		setLayout(new BorderLayout(0, 25));
		setTitle("Insert Flower Type");
		
		form();
		
		pack();
		setMinimumSize(new Dimension(320, 50));
		setResizable(false);
		
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setModalityType(DEFAULT_MODALITY_TYPE);
	}
	
	private void form() {
		input_name = new JTextField();

		button_insert = new JButton("Insert");
		button_cancel = new JButton("Cancel");
		button_insert.addActionListener(this);
		button_cancel.addActionListener(this);
		
		JPanel panel_form = new JPanel(new GridLayout(1, 2));
		
		panel_form.add(new JLabel("Flower Name"));
		panel_form.add(input_name);
		
		JPanel panel_button = new JPanel(new FlowLayout());
		panel_button.add(button_insert);
		panel_button.add(button_cancel);
		
		add(panel_form, BorderLayout.CENTER);
		add(panel_button, BorderLayout.SOUTH);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if(arg0.getSource() == button_insert) {
			String name = input_name.getText();		
			if(name.isEmpty()) {
				JOptionPane.showMessageDialog(this, "Name must be filled", "Error", JOptionPane.ERROR_MESSAGE);
			}
			else {
				try {
					Ftype.insert(name);
					DIALOG_STATE = INSERT_SUCCESS;
					dispose();
				} catch (SQLException e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(this, "Lost connection to database", "Error", JOptionPane.ERROR_MESSAGE);
					((MainFrame)getParent()).reconnect();
				}
			}
		}
		else if(arg0.getSource() == button_cancel) {
			DIALOG_STATE = INSERT_CANCEL;
			dispose();
		}
	}

}
