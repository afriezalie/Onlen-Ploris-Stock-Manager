package onlenploris.flower;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import onlenploris.MainFrame;
import onlenploris.ftype.Ftype;

public class FlowerUpdateDialog extends JDialog implements ActionListener {
	
	public static final int UPDATE_SUCCESS = 1;
	public static final int UPDATE_CANCEL = 0;
	public int DIALOG_STATE = Integer.MIN_VALUE;
	
	private long id;

	private JTextField input_name, input_stock, input_price;
	private JComboBox<String> input_type;
	private JButton button_update, button_cancel;
	
	private Vector<String> list_ftype;

	public FlowerUpdateDialog(long id, MainFrame parent) {
		super(parent);
		this.id = id;
		
		setLayout(new BorderLayout(0, 25));
		setTitle("Update Flower");
		
		try {
			list_ftype = Ftype.all_names();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		form();
		
		pack();
		setMinimumSize(new Dimension(320, 175));
		setResizable(false);
		
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setModalityType(DEFAULT_MODALITY_TYPE);
	}
	
	private void form() {
		input_name = new JTextField();
		input_type = new JComboBox<String>(list_ftype);
		input_stock = new JTextField();
		input_price = new JTextField();
		
		button_update = new JButton("Update");
		button_cancel = new JButton("Cancel");
		button_update.addActionListener(this);
		button_cancel.addActionListener(this);
		
		JPanel panel_form = new JPanel(new GridLayout(5, 2));
		
		panel_form.add(new JLabel("Flower Name"));
		panel_form.add(input_name);
		
		panel_form.add(new JLabel("Flower Type"));
		panel_form.add(input_type);
		
		panel_form.add(new JLabel("Stock"));
		panel_form.add(input_stock);
		
		panel_form.add(new JLabel("Price"));
		panel_form.add(input_price);
		
		panel_form.add(new JLabel("*left empty to use old value"));
		
		JPanel panel_button = new JPanel(new FlowLayout());
		panel_button.add(button_update);
		panel_button.add(button_cancel);
		
		add(panel_form, BorderLayout.CENTER);
		add(panel_button, BorderLayout.SOUTH);
	}
	
	public Object[] get_input() {
		String name = input_name.getText();
		Object selected_type = input_type.getSelectedItem();
		
		String temp_stock = input_stock.getText();
		String temp_price = input_price.getText();
		
		Object[] result = new Object[4];
		
		Flower old;
		try {
			old = Flower.id(id);
		} catch (SQLException e1) {
			e1.printStackTrace();
			JOptionPane.showMessageDialog(this, "Can't connect to database", "Error", JOptionPane.ERROR_MESSAGE);
			dispose();
			return null;
		}
		
		String old_name = old.getName();
		int old_stock = old.getStock();
		int old_price = old.getPrice();
		
		if(name.isEmpty()) {
			name = old_name;
		}
		
		String type;
		if(selected_type == null) {
			JOptionPane.showMessageDialog(this, "No types found", "Error", JOptionPane.ERROR_MESSAGE);
			return null;
		}
		else {
			type = selected_type.toString();
		}
				
		int stock = 0;
		if(temp_stock.isEmpty()) {
			stock = old_stock;
		}
		else {
			try {
				stock = Integer.parseInt(temp_stock);
			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(this, "Stock must be integer", "Error", JOptionPane.ERROR_MESSAGE);
				return null;
			}
		}
		
		int price = 0;	
		if(temp_price.isEmpty()) {
			price = old_price;
		}
		else {
			try {
				price = Integer.parseInt(temp_price);
			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(this, "Price must be integer", "Error", JOptionPane.ERROR_MESSAGE);
				return null;
			}
		}
			
		result[0] = name;
		result[1] = type;
		result[2] = stock;
		result[3] = price;
		return result;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if(arg0.getSource() == button_update) {		
			Object[] input = get_input();	
			if(input != null) {				
				try {
					Flower.update(id, (String)input[0], (String)input[1], (int)input[2], (int)input[3]);
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
