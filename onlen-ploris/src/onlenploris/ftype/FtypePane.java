package onlenploris.ftype;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import onlenploris.ConnectForm;
import onlenploris.MainFrame;

public class FtypePane extends JScrollPane implements ListSelectionListener{

	private MainFrame parent;
	
	private JTable table_type;
	private FtypeTableModel model_type;
	
	public FtypePane(MainFrame parent) {
		this.parent = parent;
		set_table();
	}
	
	private void set_table() {
		model_type = new FtypeTableModel();
		table_type = new JTable(model_type);
		table_type.getSelectionModel().addListSelectionListener(this);
		getViewport().add(table_type);
	}
	
	public long get_selected_id() {
		int selected_row = table_type.getSelectedRow();
		if(selected_row == -1) {
			return -1;
		}
		return (long)(table_type.getValueAt(selected_row, 0));
	}
	
	public void retrieve_data() {
		while(model_type.getRowCount() != 0) {
			model_type.removeRow(0);
		}
				
		try {
			Vector<Ftype> types = Ftype.all();
			for (Ftype type : types) {
				Object[] data = new Object[] {
					type.getId(),
					type.getName(),
				};
				model_type.addRow(data);
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
