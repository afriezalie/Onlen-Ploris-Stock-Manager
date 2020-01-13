package onlenploris.ftype;
import javax.swing.table.DefaultTableModel;

public class FtypeTableModel extends DefaultTableModel {

	public FtypeTableModel() {
		addColumn("ID");
		addColumn("Name");
	}
	
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}
}
