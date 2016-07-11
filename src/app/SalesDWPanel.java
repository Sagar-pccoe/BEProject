package app;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.border.EtchedBorder;

import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JCheckBox;

import manager.ConfigFileReader;

import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import java.util.HashMap;
import javax.swing.SwingConstants;
import javax.swing.ImageIcon;
import java.awt.Color;

public class SalesDWPanel extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField serverHostname;
	private JTextField portNo;
	private JTextField dbName;
	JButton btnSaveChanges,btnCancel;
	
	JCheckBox checkBox;
	
	private HashMap<String,String> map;
	/**
	 * Create the panel.
	 */
	public SalesDWPanel() {
		setLayout(null);
		
		JLabel lblSalesDataWarehouse = new JLabel("Sales Data Warehouse Properties");
		lblSalesDataWarehouse.setVerticalTextPosition(SwingConstants.TOP);
		lblSalesDataWarehouse.setForeground(Color.BLACK);
		lblSalesDataWarehouse.setIcon(new ImageIcon(SalesDWPanel.class.getResource("/app/images/dw.jpg")));
		lblSalesDataWarehouse.setFont(new Font("SansSerif", Font.PLAIN, 14));
		lblSalesDataWarehouse.setHorizontalAlignment(SwingConstants.LEFT);
		lblSalesDataWarehouse.setBounds(6, 6, 400, 160);
		add(lblSalesDataWarehouse);
		
		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel.setBounds(226, 123, 509, 188);
		add(panel);
		
		JLabel lblMongodbServerhostnameip = new JLabel("MongoDB Server (Hostname/IP address):");
		lblMongodbServerhostnameip.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblMongodbServerhostnameip.setBounds(29, 22, 225, 17);
		panel.add(lblMongodbServerhostnameip);
		
		serverHostname = new JTextField();
		serverHostname.setEnabled(false);
		serverHostname.setColumns(10);
		serverHostname.setBounds(266, 20, 189, 20);
		panel.add(serverHostname);
		
		JLabel label_2 = new JLabel(" Port:");
		label_2.setFont(new Font("Tahoma", Font.PLAIN, 12));
		label_2.setBounds(218, 50, 36, 17);
		panel.add(label_2);
		
		portNo = new JTextField();
		portNo.setEnabled(false);
		portNo.setColumns(10);
		portNo.setBounds(266, 48, 189, 20);
		panel.add(portNo);
		
		JLabel lbldbName = new JLabel("Database Name:");
		lbldbName.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lbldbName.setBounds(165, 78, 89, 24);
		panel.add(lbldbName);
		
		dbName = new JTextField();
		dbName.setEnabled(false);
		dbName.setColumns(10);
		dbName.setBounds(266, 80, 189, 20);
		panel.add(dbName);
		
		readValues();
		
	    btnSaveChanges = new JButton("Save Changes");
	    btnSaveChanges.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent arg0) {
	    		
	    		ConfigFileReader cfg = new ConfigFileReader();
				
				 map.put("Hostname", serverHostname.getText());
				 map.put("Port",portNo.getText() );
				 map.put("Database", dbName.getText());
				 
				cfg.writeValues("salesdw.confg",map);

				checkBox.setSelected(false);
	    	}
	    });
		btnSaveChanges.setEnabled(false);
		btnSaveChanges.setBounds(134, 141, 120, 23);
		panel.add(btnSaveChanges);
		
	    btnCancel = new JButton("Cancel");
	    btnCancel.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent arg0) {
	    		
	    		serverHostname.setText( map.get("Hostname"));
				portNo.setText( map.get("Port"));
				dbName.setText(map.get("Database"));
				checkBox.setSelected(false);
	    		
	    	}
	    });
		btnCancel.setEnabled(false);
		btnCancel.setBounds(273, 141, 120, 23);
		panel.add(btnCancel);
		
		checkBox = new JCheckBox("Edit");
		checkBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent ie) {
				
			 if(checkBox.isSelected())
			  setEnable(true);
			 else
				setEnable(false); 
			}
		});
		checkBox.setBounds(688, 88, 48, 23);
		add(checkBox);

	}
	
	void setEnable(boolean value)
	{
		serverHostname.setEnabled(value);
		portNo.setEnabled(value);
		dbName.setEnabled(value);
		btnSaveChanges.setEnabled(value);
		btnCancel.setEnabled(value);
	}
	
	void readValues()
	{
		try {
			
			ConfigFileReader cfg = new ConfigFileReader();
			
			map = cfg.readValues("salesdw.confg");
			
			if(map != null)
			{
				serverHostname.setText( map.get("Hostname"));
				portNo.setText( map.get("Port"));
				dbName.setText(map.get("Database"));
			}
			
		}catch(Exception e)
		{
			
		}
		
	}
	
	public HashMap<String,String> getMap()
	{
		return map;
	}
}
