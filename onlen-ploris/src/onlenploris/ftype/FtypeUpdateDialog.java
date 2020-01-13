package onlenploris.ftype;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Window.Type;
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

public class FtypeUpdateDialog extends JDialog implements ActionListener {
	
	public static final int UPDATE_SUCCESS = 1;
	public static final int UPDATE_CANCEL = 0;
	public int DIALOG_STATE = Integer.MIN_VALUE;
	
	private long id;
	
	private JTextField input_name;
	private JButton button_update, button_cancel;

	public FtypeUpdateDialog(long id, MainFrame parent) {
		super(parent);
		this.id = id;
		
		setLayout(new BorderLayout(0, 25));
		setTitle("Update Flower Type");
		
		form();
		
		pack();
		setMinimumSize(new Dimension(320, 50));
		setResizable(false);
		
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setModalityType(DEFAULT_MODALITY_TYPE);
	}
	
	private void form() {
		input_name = new JTextField();

		button_update = new JButton("Update");
		button_cancel = new JButton("Cancel");
		button_update.addActionListener(this);
		button_cancel.addActionListener(this);
		
		JPanel panel_form = new JPanel(new GridLayout(1, 2));
		
		panel_form.add(new JLabel("Flower Name"));
		panel_form.add(input_name);
		
		JPanel panel_button = new JPanel(new FlowLayout());
		panel_button.add(button_update);
		panel_button.add(button_cancel);
		
		add(panel_form, BorderLayout.CENTER);
		add(panel_button, BorderLayout.SOUTH);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if(arg0.getSource() == button_update) {
			String name = input_name.getText();		
			if(name.isEmpty()) {
				JOptionPane.showMessageDialog(this, "Name must be filled", "Error", JOptionPane.ERROR_MESSAGE);
			}
			else {
				try {
					Ftype.update(id, name);
					DIALOG_STATE = UPDATE_SUCCESS;
					dispose();
				} catch (SQLException e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(this, "Lost connection to database", "Error", JOptionPane.ERROR_MESSAGE);
					((MainFrame)getParent()).reconnect();
				}
			}
		}
		else if(arg0.getSource() == button_cancel) {
			DIALOG_STATE = UPDATE_CANCEL;
			dispose();
		}
	}
}
