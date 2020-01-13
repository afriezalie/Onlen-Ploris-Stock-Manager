package onlenploris;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import onlenploris.flower.Flower;
import onlenploris.flower.FlowerAddDialog;
import onlenploris.flower.FlowerPane;
import onlenploris.flower.FlowerUpdateDialog;
import onlenploris.ftype.Ftype;
import onlenploris.ftype.FtypeAddDialog;
import onlenploris.ftype.FtypePane;
import onlenploris.ftype.FtypeUpdateDialog;

public class MainFrame extends JFrame implements ActionListener, ChangeListener {
	
	private JMenuBar mb;
	private JMenu menu_file;
	private JMenuItem mi_refresh, mi_reconnect;
	
	private JButton button_add, button_update, button_delete;
	
	private JTabbedPane tp;
	private FlowerPane sp_flower;
	private FtypePane sp_type;

	public MainFrame() {
		
		set_menu();
		set_tp();
		set_button();
		
		setTitle("Onlen Ploris Menejer");
		
		pack();
		setMinimumSize(new Dimension(640, 480));
		
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		ConnectForm cf = new ConnectForm(this);
		if(cf.DIALOG_STATE == ConnectForm.CONNECT_SUCCESS) {			
			setVisible(true);
			sp_flower.retrieve_data();
		}
		else {
			dispose();
		}
	}
	
	private void set_menu() {
		mb = new JMenuBar();
		menu_file = new JMenu("File");
		mi_refresh = new JMenuItem("Refresh");
		mi_reconnect = new JMenuItem("Reconnect");
		
		mi_refresh.addActionListener(this);
		mi_reconnect.addActionListener(this);
		
		menu_file.add(mi_refresh);
		menu_file.add(mi_reconnect);
		
		mb.add(menu_file);
		setJMenuBar(mb);
	}
	
	private void set_button() {
		JPanel panel_button = new JPanel();
		panel_button.setLayout(new BoxLayout(panel_button, BoxLayout.Y_AXIS));
		
		button_add = new JButton("New");
		button_update = new JButton("Update");
		button_delete = new JButton("Delete");
		
		button_add.setMaximumSize(button_update.getMaximumSize());
		button_delete.setMaximumSize(button_update.getMaximumSize());
		
		button_add.addActionListener(this);
		button_update.addActionListener(this);
		button_delete.addActionListener(this);
		
		button_update.setEnabled(false);
		button_delete.setEnabled(false);
		
		panel_button.add(button_add);
		panel_button.add(button_update);
		panel_button.add(button_delete);
		
		add(panel_button, BorderLayout.WEST);

	}
	
	private void set_tp() {
		sp_flower = new FlowerPane(this);
		sp_type = new FtypePane(this);
		
		tp = new JTabbedPane(JTabbedPane.BOTTOM);
		tp.add(sp_flower, "Flower");
		tp.add(sp_type, "Flower Type");
		tp.addChangeListener(this);
		
		add(tp, BorderLayout.CENTER);
	}
	
	public void enable_update_button(boolean enable) {
		button_update.setEnabled(enable);
	}
	
	public void enable_delete_button(boolean enable) {
		button_delete.setEnabled(enable);
	}
	
	public void reconnect() {
		dispose();
		new MainFrame();
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		if(arg0.getSource() == button_add) {
			if(tp.getSelectedComponent() == sp_flower) {
				FlowerAddDialog fad = new FlowerAddDialog(this);
				fad.setLocationRelativeTo(this);
				fad.setVisible(true);			
				if(fad.DIALOG_STATE == FlowerAddDialog.INSERT_SUCCESS) {
					sp_flower.retrieve_data();
				}
			}
			else {
				FtypeAddDialog fad = new FtypeAddDialog(this);
				fad.setLocationRelativeTo(this);
				fad.setVisible(true);			
				if(fad.DIALOG_STATE == FtypeAddDialog.INSERT_SUCCESS) {
					sp_type.retrieve_data();
				}
			}
		}
		
		else if(arg0.getSource() == button_update) {
			if(tp.getSelectedComponent() == sp_flower) {
				FlowerUpdateDialog fud = new FlowerUpdateDialog(sp_flower.get_selected_id(), this);
				fud.setLocationRelativeTo(this);
				fud.setVisible(true);		
				if(fud.DIALOG_STATE == FlowerUpdateDialog.UPDATE_SUCCESS) {
					sp_flower.retrieve_data();
					button_update.setEnabled(false);
					button_delete.setEnabled(false);
				}
			}
			else {
				FtypeUpdateDialog fud = new FtypeUpdateDialog(sp_type.get_selected_id(), this);
				fud.setLocationRelativeTo(this);
				fud.setVisible(true);		
				if(fud.DIALOG_STATE == FtypeUpdateDialog.UPDATE_SUCCESS) {
					sp_type.retrieve_data();
					button_update.setEnabled(false);
					button_delete.setEnabled(false);
				}
			}
		}
		
		else if(arg0.getSource() == button_delete) {
			if(tp.getSelectedComponent() == sp_flower) {				
				try {
					Flower.delete(sp_flower.get_selected_id());
				} catch (SQLException e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(this, "Delete flower from database failed", "Error", JOptionPane.ERROR_MESSAGE);
				}
				sp_flower.retrieve_data();
			}
			else {
				try {
					Ftype.delete(sp_type.get_selected_id());
				} catch (SQLException e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(this, "Delete flower type from database failed", "Error", JOptionPane.ERROR_MESSAGE);
				}
				sp_type.retrieve_data();
			}
			button_update.setEnabled(false);
			button_delete.setEnabled(false);
		}
		
		else if(arg0.getSource() == mi_refresh) {
			Component selected = tp.getSelectedComponent();
			if(selected instanceof FlowerPane) {
				((FlowerPane)selected).retrieve_data();
			}
			else {
				((FtypePane)selected).retrieve_data();
			}
			button_update.setEnabled(false);
			button_delete.setEnabled(false);
		}
		
		else if(arg0.getSource() == mi_reconnect) {
			reconnect();
		}
	}

	@Override
	public void stateChanged(ChangeEvent arg0) {
		if(arg0.getSource() == tp) {
			Component selected = tp.getSelectedComponent();
			if(selected instanceof FlowerPane) {
				((FlowerPane)selected).retrieve_data();
			}
			else {
				((FtypePane)selected).retrieve_data();
			}
			button_update.setEnabled(false);
			button_delete.setEnabled(false);
		}
	}
}
