package app;

import java.awt.BorderLayout;





import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JProgressBar;
import javax.swing.border.TitledBorder;
import javax.swing.JLabel;





import java.awt.Font;

import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;

import manager.ExcelSheetManager;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.UIManager;

public class ETLManagerDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTextField filePath;
	JFileChooser fileChooser;
	JButton btnBrowse,btnLoad,btnCancel;
	JTextArea area;
	JProgressBar bar;
	
	private String mongoHost="localhost", dbName="test";
	

	public String getMongoHost() {
		return mongoHost;
	}

	public void setMongoHost(String mongoHost) {
		this.mongoHost = mongoHost;
	}

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			ETLManagerDialog dialog = new ETLManagerDialog(null);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public ETLManagerDialog(JFrame f) {
		super(f,true);
		setResizable(false);
		
		setTitle("ETL Manager");
		setBounds(100, 100, 528, 435);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		JPanel panel = new JPanel();
		panel.setBackground(UIManager.getColor("Button.background"));
		panel.setBounds(0, 0, 522, 53);
		contentPanel.add(panel);
		panel.setLayout(null);
		
		JLabel lblNewDataSource = new JLabel("New Data Source");
		lblNewDataSource.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblNewDataSource.setBounds(10, 11, 233, 31);
		panel.add(lblNewDataSource);
		
		JLabel lblFile = new JLabel("File : ");
		lblFile.setBounds(35, 63, 46, 14);
		contentPanel.add(lblFile);
		
		filePath = new JTextField();
		filePath.setEditable(false);
		filePath.setBounds(70, 60, 323, 20);
		contentPanel.add(filePath);
		filePath.setColumns(10);
		
	    btnBrowse = new JButton("Browse...");
		btnBrowse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				fileChooser = new JFileChooser();
											
				int i = fileChooser.showOpenDialog(ETLManagerDialog.this);
				
				if(i == JFileChooser.APPROVE_OPTION)
					{
					
					   File f = fileChooser.getSelectedFile();	
					   String fpath = f.getAbsolutePath();
					   String fname = f.getName();
					   
					   if(fname.endsWith(".xls") || fname.endsWith(".xlsx") || fname.endsWith(".xlsm") || fname.endsWith(".xlsb"))
					   {
						   filePath.setText(fpath);
						   
						   btnLoad.setEnabled(true);
						   btnCancel.setEnabled(true);
					   }
					   else
					   {
						   JOptionPane.showMessageDialog(ETLManagerDialog.this,"Please select vaild Excel Sheet file.","Invalid File",JOptionPane.WARNING_MESSAGE);
					   }
					}
			}
		});
		btnBrowse.setBounds(403, 59, 109, 23);
		contentPanel.add(btnBrowse);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(186, 89, -174, 94);
		contentPanel.add(scrollPane);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBounds(10, 126, 502, 241);
		contentPanel.add(panel_1);
		panel_1.setLayout(new BorderLayout(0, 0));
		
		area = new JTextArea();
		area.setEditable(false);
		panel_1.add(area, BorderLayout.CENTER);
		
	    btnLoad = new JButton("Load");
	    btnLoad.setEnabled(false);
	    
	    addWindowListener(new WindowAdapter() {
	    	
	    	public void windowClosing(WindowEvent we)
	    	{
	    		setDefault();
	    	}
		});
	    
	    btnLoad.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent arg0) {

	    		try {
	    			
	    		 	 String file = filePath.getText();
	    		
	    		 	 btnBrowse.setEnabled(false);
	    		 	 btnLoad.setEnabled(false);
	    		 	 
	    		 	 area.append("\nFile Size: "+ (new File(file).length()/(1024*1024) + " MB."));
	    			
	    		 	area.append("\nGetting Information. Please wait..."); 	
	    		 	
	    			ExcelSheetManager  e = new ExcelSheetManager(file,mongoHost,dbName);
	    			
	    		    e.extract(bar,area);
	    		   
	    		    
	    		} catch (Exception e1) {
	    			// TODO Auto-generated catch block
	    			JOptionPane.showMessageDialog(null, e1.getMessage(), "Error" ,JOptionPane.ERROR_MESSAGE);
	    			area.append("\nError Occured. Exiting now...");
	    			ETLManagerDialog.this.dispose();
	    			setDefault();
	    		}
	    	}
	    });
		btnLoad.setBounds(157, 92, 89, 23);
		contentPanel.add(btnLoad);
		
	    btnCancel = new JButton("Cancel");
	    btnCancel.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent arg0) {
	    		
	    		setDefault();
	    		dispose();
	    	}
	    });
	    btnCancel.setEnabled(false);
		btnCancel.setBounds(256, 92, 89, 23);
		contentPanel.add(btnCancel);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setBorder(new TitledBorder(null, "Progress", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			buttonPane.setLayout(new BorderLayout(0, 0));
			{
				 bar = new JProgressBar();
				 bar.setStringPainted(true);
				 buttonPane.add(bar);
			}
		}
		
	}
	
	void setDefault()
	{
		btnLoad.setEnabled(true);
		btnBrowse.setEnabled(true);
		area.setText("");
		bar.setMaximum(0);
	}
}
