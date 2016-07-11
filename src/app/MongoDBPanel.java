package app;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;

import java.awt.Component;
import java.awt.Font;

import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JCheckBox;
import javax.swing.border.EtchedBorder;

import manager.ConfigFileReader;

import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.SwingConstants;

public class MongoDBPanel extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField portTextField;
	private JTextField ipathTextField;
	private JTextField dbPathTextField;

	private JButton btnIPath,btnDBPath,btnSaveChanges,btnCancel;
	
	JCheckBox chckbxEdit;
	
	private HashMap<String,String> map;
	
	/**
	 * Create the panel.
	 */
	
	public MongoDBPanel() {
		
		setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel.setBounds(205, 114, 534, 229);
		add(panel);
		panel.setLayout(null);
		
		JLabel lblPort = new JLabel(" Port:");
		lblPort.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblPort.setBounds(184, 69, 31, 17);
		panel.add(lblPort);
		
		portTextField = new JTextField();
		portTextField.setEditable(false);
		portTextField.setColumns(10);
		portTextField.setBounds(237, 67, 196, 20);
		panel.add(portTextField);
		
		JLabel lblPath = new JLabel("Installation Path:");
		lblPath.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblPath.setBounds(138, 18, 96, 24);
		panel.add(lblPath);
		
		ipathTextField = new JTextField();
		ipathTextField.setEditable(false);
		ipathTextField.setColumns(10);
		ipathTextField.setBounds(236, 20, 196, 20);
		panel.add(ipathTextField);
		
		 btnIPath = new JButton("...");
		 btnIPath.setEnabled(false);
		btnIPath.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				String path = getPath(null,JFileChooser.FILES_ONLY);
				
				if(path!=null)
				ipathTextField.setText(path);
			}
		});
		btnIPath.setBounds(450, 19, 28, 23);
		panel.add(btnIPath);
		
		JLabel lblPath_1 = new JLabel("Database Path:");
		lblPath_1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblPath_1.setBounds(138, 115, 83, 24);
		panel.add(lblPath_1);
		
		dbPathTextField = new JTextField();
		dbPathTextField.setEditable(false);
		dbPathTextField.setColumns(10);
		dbPathTextField.setBounds(236, 115, 196, 20);
		panel.add(dbPathTextField);
		
		 btnDBPath = new JButton("...");
		 btnDBPath.addActionListener(new ActionListener() {
		 	public void actionPerformed(ActionEvent arg0) {
		 		
		 		String path = getPath(null,JFileChooser.DIRECTORIES_ONLY);
		 		
		 				if(path!=null)
			 		dbPathTextField.setText(path);
			 		
			 	}
		 });
		 btnDBPath.setEnabled(false);
		btnDBPath.setBounds(450, 114, 28, 23);
		panel.add(btnDBPath);
		
		btnSaveChanges = new JButton("Save Changes");
		
		btnSaveChanges.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int port=0;
				try
				{
				
				String ipath = ipathTextField.getText().trim();
				port = Integer.parseInt( portTextField.getText().trim());
				String dbPath = dbPathTextField.getText().trim();
				
				if((!ipath.equals("")) && (ipath.endsWith("mongod.exe")))
				{
				
					if((port > 0) && port < 65536)
					{	
						
						if(!dbPath.equals(""))
						{
							ConfigFileReader cfg = new ConfigFileReader();
				
							map.put("Port",portTextField.getText());
							map.put("Installation Path", ipath);
							map.put("DB Path", dbPath);
				 
							cfg.writeValues("mongod.confg",map);
				
							chckbxEdit.setSelected(false);
						}
						else
							JOptionPane.showMessageDialog(null,"Please specify a valid MongoDB database path","Invalid path", JOptionPane.ERROR_MESSAGE);
					}
					else
					{
						JOptionPane.showMessageDialog(null,"Please specify a valid port no.","Invalid port", JOptionPane.ERROR_MESSAGE);
					}
				
				}
				else
				{
					JOptionPane.showMessageDialog(null,"Please specify MongoDB Server Installation path.", ipath, JOptionPane.ERROR_MESSAGE);
				}
			}
			catch(NumberFormatException e)
			{
				JOptionPane.showMessageDialog(null,"Please specify a valid port no.","Invalid port", JOptionPane.ERROR_MESSAGE);
			}
			}
		});
		btnSaveChanges.setEnabled(false);
		btnSaveChanges.setBounds(111, 179, 137, 23);
		panel.add(btnSaveChanges);
		
	    btnCancel = new JButton("Cancel");
	 
	    btnCancel.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent arg0) {
	    
	    		
				portTextField.setText( map.get("Port"));
				ipathTextField.setText(map.get("Installation Path"));
				
				dbPathTextField.setText(map.get("DB Path"));
				
				
				chckbxEdit.setSelected(false);
	    		
	    	}
	    });
	    
		btnCancel.setEnabled(false);
		btnCancel.setBounds(293, 179, 120, 23);
		panel.add(btnCancel);
		
		 chckbxEdit = new JCheckBox("Edit");
		 chckbxEdit.setBounds(683, 85, 56, 23);
		 add(chckbxEdit);
		 
		 JLabel lblMongodbServerProperties = new JLabel("MongoDB Server Properties");
		 lblMongodbServerProperties.setVerticalTextPosition(SwingConstants.TOP);
		 lblMongodbServerProperties.setIcon(new ImageIcon(MongoDBPanel.class.getResource("/app/images/mongodb.png")));
		 lblMongodbServerProperties.setFont(new Font("SansSerif", Font.PLAIN, 14));
		 lblMongodbServerProperties.setHorizontalAlignment(SwingConstants.LEFT);
		 lblMongodbServerProperties.setBounds(6, 6, 479, 64);
		 add(lblMongodbServerProperties);
		 
		 	chckbxEdit.addItemListener(new ItemListener() {
		 		public void itemStateChanged(ItemEvent ie) {
		 			
		 			
		 			JCheckBox c = (JCheckBox) ie.getItem();
		 			
		 			if(c.isSelected())
		 			{
		 			  enable(true);
		 		    }
		 			else
		 			{
		 				enable(false);
		 			}
		 			
		 		}
		 	});
		
		readValues();

	}
	
	public void enable(boolean value)
	{
		
		  portTextField.setEditable(value);
		  ipathTextField.setEditable(value);
		  dbPathTextField.setEditable(value);
		 
		  
		  btnIPath.setEnabled(value);
		  btnDBPath.setEnabled(value);
		  
		 
		  
		  btnSaveChanges.setEnabled(value);
		  btnCancel.setEnabled(value);
	}
	
	
	void readValues()
	{
		try {
			
			ConfigFileReader cfg = new ConfigFileReader();
			
			map = cfg.readValues("mongod.confg");
			
			if(map != null)
			{
			
			portTextField.setText( map.get("Port"));
			ipathTextField.setText(map.get("Installation Path"));
			
			dbPathTextField.setText(map.get("DB Path"));
			
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	HashMap<String, String> getMap()
	{
		return map;
	}
	
	String getPath(Component c,int type)
	{
		JFileChooser jfc = new JFileChooser();
		jfc.setFileSelectionMode(type);
		int value = jfc.showOpenDialog(c);
		
		String path = null;
		
		if(value == JFileChooser.APPROVE_OPTION)
		{
			path = jfc.getSelectedFile().getAbsolutePath();
		}
		
		return path;
	}
}
