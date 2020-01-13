package onlenploris.flower;
import javax.swing.table.DefaultTableModel;

public class FlowerTableModel extends DefaultTableModel{

	public FlowerTableModel() {
		addColumn("ID");
		addColumn("Name");
		addColumn("Type");
		addColumn("Stock");
		addColumn("Price");
	}
	
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

}