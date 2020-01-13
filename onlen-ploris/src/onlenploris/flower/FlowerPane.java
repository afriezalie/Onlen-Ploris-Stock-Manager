package onlenploris.flower;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import onlenploris.ConnectForm;
import onlenploris.MainFrame;

public class FlowerPane extends JScrollPane implements ListSelectionListener{
	
	private MainFrame parent;
	
	private JTable table_flower;
	private FlowerTableModel model_flower;
	

	public FlowerPane(MainFrame parent) {
		this.parent = parent;
		set_table();
	}
	
	private void set_table() {
		model_flower = new FlowerTableModel();
		table_flower = new JTable(model_flower);
		table_flower.getSelectionModel().addListSelectionListener(this);
		getViewport().add(table_flower);
	}
	
	public long get_selected_id() {
		int selected_row = table_flower.getSelectedRow();
		if(selected_row == -1) {
			return -1;
		}
		return (long)(table_flower.getValueAt(selected_row, 0));
	}
	
	public void retrieve_data() {
		while(model_flower.getRowCount() != 0) {
			model_flower.removeRow(0);
		}
			
		try {
			Vector<Flower> flowers = Flower.all();
			for (Flower flower : flowers) {
				Object[] data = new Object[] {
					flower.getId(),
					flower.getName(),
					flower.getType(),
					flower.getStock(),
					flower.getPrice()
				};
				model_flower.addRow(data);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(parent, "Can't connect to database", "Error", JOptionPane.ERROR_MESSAGE);
			parent.dispose();
			new MainFrame();
		}
	}

	@Override
	public void valueChanged(ListSelectionEvent arg0) {
		parent.enable_update_button(true);
		parent.enable_delete_button(true);
	}
	
}
