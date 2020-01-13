package onlenploris.flower;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Window;
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

public class FlowerAddDialog extends JDialog implements ActionListener{
	
	public static final int INSERT_SUCCESS = 1;
	public static final int INSERT_CANCEL = 0;
	public int DIALOG_STATE = Integer.MIN_VALUE;
	

	private JTextField input_name, input_stock, input_price;
	private JComboBox<String> input_type;
	private JButton button_insert, button_cancel;
	
	private Vector<String> list_ftype;
	
	public FlowerAddDialog(MainFrame parent) {
		super(parent);
		
		setLayout(new BorderLayout(0, 25));
		setTitle("Insert Flower");
		
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
		
		button_insert = new JButton("Insert");
		button_cancel = new JButton("Cancel");
		button_insert.addActionListener(this);
		button_cancel.addActionListener(this);
		
		JPanel panel_form = new JPanel(new GridLayout(4, 2));
		
		panel_form.add(new JLabel("Flower Name"));
		panel_form.add(input_name);
		
		panel_form.add(new JLabel("Flower Type"));
		panel_form.add(input_type);
		
		panel_form.add(new JLabel("Stock"));
		panel_form.add(input_stock);
		
		panel_form.add(new JLabel("Price"));
		panel_form.add(input_price);
		
		JPanel panel_button = new JPanel(new FlowLayout());
		panel_button.add(button_insert);
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
		
		if(name.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Name must be filled", "Error", JOptionPane.ERROR_MESSAGE);
			return null;
		}
		
		String type;
		if(selected_type == null) {
			JOptionPane.showMessageDialog(this, "No types found", "Error", JOptionPane.ERROR_MESSAGE);
			return null;
		}
		else {
			type = selected_type.toString();
		}
				
		if(temp_stock.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Stock must be filled", "Error", JOptionPane.ERROR_MESSAGE);
			return null;
		}
		
		int stock = 0;
		try {
			stock = Integer.parseInt(temp_stock);
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(this, "Stock must be integer", "Error", JOptionPane.ERROR_MESSAGE);
			return null;
		}
		
		if(temp_price.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Price must be filled", "Error", JOptionPane.ERROR_MESSAGE);
			return null;
		}
		
		int price = 0;
		try {
			price = Integer.parseInt(temp_price);
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(this, "Price must be integer", "Error", JOptionPane.ERROR_MESSAGE);
			return null;
		}
		
		result[0] = name;
		result[1] = type;
		result[2] = stock;
		result[3] = price;
		return result;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if(arg0.getSource() == button_insert) {		
			Object[] input = get_input();	
			if(input != null) {				
				try {
					Flower.insert((String)input[0], (String)input[1], (int)input[2], (int)input[3]);
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
