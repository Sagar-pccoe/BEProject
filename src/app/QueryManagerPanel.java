package app;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.border.EtchedBorder;

import java.awt.Font;

import javax.swing.JOptionPane;
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
import java.awt.Color;

public class QueryManagerPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField txtPort;
	private JCheckBox chckbxEdit;
	private JButton btnSaveChanges, btnCancel;
	
	HashMap<String,String> map;
	private JLabel lblQueryManagerProperties_1;
	
	/**
	 * Create the panel.
	 */
	public QueryManagerPanel() {
		setBackground(new Color(214, 217, 223));
		setLayout(null);
		setBackground(new Color(240,240,240));
		JPanel panel_1 = new JPanel();
		panel_1.setLayout(null);
		panel_1.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel_1.setBounds(251, 121, 477, 174);
		add(panel_1);
		
		JLabel lblQueryManagerPort = new JLabel("Query Manager Listening Port:");
		lblQueryManagerPort.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblQueryManagerPort.setBounds(57, 40, 174, 17);
		panel_1.add(lblQueryManagerPort);
		
		txtPort = new JTextField();
		txtPort.setEnabled(false);
		txtPort.setColumns(10);
		txtPort.setBounds(243, 38, 189, 20);
		panel_1.add(txtPort);
		
		btnSaveChanges = new JButton("Save Changes");
		btnSaveChanges.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			
				try
				{
				
				int port = Integer.parseInt(txtPort.getText());
			
				if(port >= 65536)
				throw new Exception();
				
				ConfigFileReader cfg = new ConfigFileReader();
				
				 map.put("Port", ""+port);
				 
				cfg.writeValues("qm.confg",map);

				chckbxEdit.setSelected(false);
				}
				catch(Exception e)
				{
					JOptionPane.showMessageDialog(null, "Invalid Port Number.");
				}
			}
		});
		btnSaveChanges.setBounds(103, 109, 128, 23);
		panel_1.add(btnSaveChanges);
		btnSaveChanges.setEnabled(false);
		
		btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				txtPort.setText( map.get("Port"));
				
				chckbxEdit.setSelected(false);
			}
		});
		btnCancel.setBounds(260, 109, 120, 23);
		panel_1.add(btnCancel);
		btnCancel.setEnabled(false);
		
		 chckbxEdit = new JCheckBox("Edit");
		chckbxEdit.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				
				if(chckbxEdit.isSelected())
					setEnable(true);
				else
					setEnable(false);
			}
		});
		chckbxEdit.setBounds(685, 91, 58, 23);
		add(chckbxEdit);
		
		lblQueryManagerProperties_1 = new JLabel("Query Manager Properties");
		lblQueryManagerProperties_1.setVerticalTextPosition(SwingConstants.TOP);
		lblQueryManagerProperties_1.setVerticalAlignment(SwingConstants.TOP);
		lblQueryManagerProperties_1.setIcon(null);
		lblQueryManagerProperties_1.setFont(new Font("SansSerif", Font.PLAIN, 14));
		lblQueryManagerProperties_1.setHorizontalAlignment(SwingConstants.LEFT);
		
		lblQueryManagerProperties_1.setBounds(6, 6, 360, 35);
		add(lblQueryManagerProperties_1);
		
		readValues();
	}
	
	void setEnable(boolean value)
	{
		txtPort.setEnabled(value);
		btnSaveChanges.setEnabled(value);
		btnCancel.setEnabled(value);
	}
	
	public int getPort()
	{
		return Integer.parseInt(map.get("Port"));
	}
	
	void readValues()
	{
		try {
			
			ConfigFileReader cfg = new ConfigFileReader();
			
			map = cfg.readValues("qm.confg");
			
			if(map != null)
			{
			txtPort.setText( map.get("Port"));
			}
			
		}catch(Exception e)
		{
			// TODO Auto-generated catch block
		}

}
	
}
