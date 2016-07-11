package app;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;


import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import javax.swing.JLabel;
import javax.swing.JSeparator;
import javax.swing.JButton;

import java.awt.Color;

import javax.swing.border.EtchedBorder;


import manager.MongoDBStarter;
import manager.QueryManager;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.CardLayout;
import java.util.HashMap;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JTextArea;

import java.awt.BorderLayout;

import java.net.URISyntaxException;
import java.awt.Toolkit;

public class MainApp extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JPanel contentPane;

	JPanel properties,textAreaPanel,qmTextAreaPanel;
	CardLayout card;
	
	MongoDBPanel mdp;
	MongoDBStarter ms;
	QueryManagerPanel qm;
	UserPanel userPanel;
	ViewUserPanel viewUser;
	SalesDWPanel sw;
	
	JButton btnStart, btnStop;
	JLabel lblstatus, lblViewLogFile,lblProperties,lblProperties_1;
	JLabel lblLoadData,lblProperties_2;
	
	JTextArea area,qmArea;
	JScrollPane sc;
	ETLManagerDialog dialog;
	private JLabel lblAddUser;
	private JLabel lblViewUsers;
	private JLabel lblViewLog,lblQmStatus;
	private QueryManager queryManager = null;
	JButton btnStartQM, btnStopQM;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainApp frame = new MainApp();
					frame.setVisible(true);
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, e.getMessage());
				}
			}
		});
	}

	/**
	 * Create the frame.
	 * @throws URISyntaxException 
	 */
	public MainApp() throws URISyntaxException {
		
		setIconImage(Toolkit.getDefaultToolkit().getImage(MainApp.class.getResource("/javax/swing/plaf/metal/icons/ocean/hardDrive.gif")));
		setResizable(false);
		
		setBackground(Color.CYAN);
		setTitle("Sales Data Analysis and Decision Support System Using Hadoop and Data Mining");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1167, 607);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		
		
		properties = new JPanel();
		properties.setBackground(Color.WHITE);
		properties.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		properties.setBounds(236, 11, 919, 562);
		sw = new SalesDWPanel();
	
		contentPane.add(properties);
		
		card = new CardLayout();
		
		
		properties.setLayout(card);
		
		textAreaPanel = new JPanel(new BorderLayout());
		qmTextAreaPanel = new JPanel(new BorderLayout());
		
		sc = new JScrollPane();
	
		area = new JTextArea();
		qmArea = new JTextArea();
		
		qmArea.setEditable(false);
		
		sc.setViewportView(area);
		
		JScrollPane sc2 = new JScrollPane();
		sc2.setViewportView(qmArea);
		
		area.setEditable(false);
		
		mdp = new MongoDBPanel();
		properties.add(mdp,"MongoDB Server Properties");
		
		
		textAreaPanel.add(sc,BorderLayout.CENTER);
		qmTextAreaPanel.add(sc2,BorderLayout.CENTER);
		
		properties.add(sw,"Sales Datawarehouse Properties");
		properties.add(textAreaPanel,"Log");
		
		properties.add(qmTextAreaPanel,"Query Manager Log");
	
		qm = new QueryManagerPanel();
		
		properties.add(qm,"Query Manager Properties");
		
		userPanel = new UserPanel();
		
		properties.add(userPanel,"Add New User Panel");
		
		viewUser = new ViewUserPanel();
		
		properties.add(viewUser,"View Users");
		
		JPanel mongoDBPanel = new JPanel();
		mongoDBPanel.setBackground(Color.WHITE);
		
		mongoDBPanel.setLayout(null);
		mongoDBPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		mongoDBPanel.setBounds(12, 11, 216, 174);
		contentPane.add(mongoDBPanel);
		
		JLabel lblMongoDb = new JLabel("Mongo DB Server");
		lblMongoDb.setBounds(10, 0, 123, 31);
		mongoDBPanel.add(lblMongoDb);
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(10, 27, 196, 2);
		mongoDBPanel.add(separator_1);
		
		JLabel label_1 = new JLabel("Status:");
		label_1.setBounds(10, 43, 67, 14);
		mongoDBPanel.add(label_1);
		
		 lblstatus = new JLabel("Stopped");
		lblstatus.setBounds(54, 43, 115, 14);
		mongoDBPanel.add(lblstatus);
		
		 btnStart = new JButton("Start");
		
		btnStart.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				
				HashMap<String,String> map = mdp.getMap();
				
				 ms = new MongoDBStarter(map,area);
				 ms.start();
				 lblstatus.setText("Started");
				 lblViewLogFile.setEnabled(true);
				 
				 btnStart.setEnabled(false);
				 btnStop.setEnabled(true);
				}
		});
		btnStart.setBounds(8, 68, 91, 23);
		mongoDBPanel.add(btnStart);
		
		 btnStop = new JButton("Stop");
		btnStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ms.stop();
				lblstatus.setText("Stopped");
				 lblViewLogFile.setEnabled(false);
				 btnStop.setEnabled(false);
				 btnStart.setEnabled(true);
			}
		});
		btnStop.setBounds(109, 68, 97, 23);
		mongoDBPanel.add(btnStop);
		
		btnStop.setEnabled(false);
		
		 lblViewLogFile = new JLabel("+ View Log");
		 lblViewLogFile.setEnabled(false);
		 lblViewLogFile.addMouseListener(new MouseAdapter() {
		 	@Override
		 	public void mouseEntered(MouseEvent arg0) {
		 		lblViewLogFile.setForeground(Color.BLUE);
		 	}
		 	
		 	public void mouseExited(MouseEvent me)
		 	{
		 		lblViewLogFile.setForeground(Color.BLACK);
		 	}
		 	@Override
		 	public void mouseClicked(MouseEvent arg0) {
		 		
		 		card.show(properties, "Log");
		 		//HashMap<String,String> map = mdp.getMap();
		 		//area.setText("");
		 		//showFile(map.get("Log File Path") + "mongodb.log");	
		 	}

		 });
		
		lblViewLogFile.setBounds(10, 137, 196, 15);
		mongoDBPanel.add(lblViewLogFile);
		 lblProperties = new JLabel("+ Properties");
		lblProperties.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				card.show(properties,"MongoDB Server Properties" );
			}
			@Override
			public void mouseEntered(MouseEvent arg0) {
				lblProperties.setForeground(Color.BLUE);
			}
			
			public void mouseExited(MouseEvent arg0) {
				lblProperties.setForeground(Color.BLACK);
			}
		});
		lblProperties.setBounds(10, 102, 106, 23);
		mongoDBPanel.add(lblProperties);
		
		JPanel panel = new JPanel();
		panel.setBackground(Color.WHITE);
		panel.setLayout(null);
		panel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel.setBounds(12, 334, 216, 239);
		contentPane.add(panel);
		
		JLabel lblQueryManager = new JLabel("Query Manager");
		lblQueryManager.setBounds(10, 0, 159, 31);
		panel.add(lblQueryManager);
		
		JSeparator separator_2 = new JSeparator();
		separator_2.setBounds(10, 27, 196, 4);
		panel.add(separator_2);
		
		JLabel label_2 = new JLabel("Status:");
		label_2.setBounds(10, 43, 55, 14);
		panel.add(label_2);
		
		lblQmStatus = new JLabel("Stopped");
		lblQmStatus.setBounds(56, 43, 115, 14);
		panel.add(lblQmStatus);
		
	    btnStartQM = new JButton("Start");
	    
		btnStartQM.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				HashMap<String,String> map = sw.getMap();
				queryManager = new QueryManager(qm.getPort(),map.get("Hostname"),map.get("Database"),qmArea);
				lblQmStatus.setText("Started");
				btnStartQM.setEnabled(false);
				btnStopQM.setEnabled(true);
				lblViewLog.setEnabled(true);
			}
		});
		btnStartQM.setBounds(10, 68, 89, 23);
		panel.add(btnStartQM);
		
		btnStopQM = new JButton("Stop");
		
		btnStopQM.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				queryManager.setFlag(false);
				queryManager = null;
				System.gc();
				lblQmStatus.setText("Stopped");
				btnStartQM.setEnabled(true);
				btnStopQM.setEnabled(false);
				lblViewLog.setEnabled(false);
			}
		});
		btnStopQM.setBounds(109, 68, 97, 23);
		panel.add(btnStopQM);
		btnStopQM.setEnabled(false);
		
		 lblProperties_2 = new JLabel("+ Properties");
		lblProperties_2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				lblProperties_2.setForeground(Color.BLUE);
			}
			
			public void mouseExited(MouseEvent me)
			{
				lblProperties_2.setForeground(Color.BLACK);
			}
			@Override
			public void mouseClicked(MouseEvent arg0) {
				
				card.show(properties, "Query Manager Properties");
			}
		});
		lblProperties_2.setBounds(10, 103, 115, 14);
		panel.add(lblProperties_2);
		
		lblAddUser = new JLabel("+ Add New User");
		
		lblAddUser.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				lblAddUser.setForeground(Color.BLUE);
			}
			
			public void mouseExited(MouseEvent me)
			{
				lblAddUser.setForeground(Color.BLACK);
			}
			@Override
			public void mouseClicked(MouseEvent arg0) {
				
				card.show(properties, "Add New User Panel");
				
			}
		});
		lblAddUser.setBounds(11, 201, 195, 14);
		panel.add(lblAddUser);
		
		lblViewUsers = new JLabel("+ View Users");
		
		lblViewUsers.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				lblViewUsers.setForeground(Color.BLUE);
			}
			
			public void mouseExited(MouseEvent me)
			{
				lblViewUsers.setForeground(Color.BLACK);
			}
			@Override
			public void mouseClicked(MouseEvent arg0) {
				
				viewUser  = null;
				System.gc();
				try {
					viewUser = new ViewUserPanel();
				} catch (URISyntaxException e) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(null, e.getMessage());
				}
				properties.add(viewUser,"View Users");
				card.show(properties, "View Users");
			}
		});
		
		lblViewUsers.setBounds(10, 167, 196, 14);
		panel.add(lblViewUsers);
		
		lblViewLog = new JLabel("+ View Log");
		lblViewLog.setEnabled(false);
		lblViewLog.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				card.show(properties, "Query Manager Log");
			}
			@Override
			public void mouseEntered(MouseEvent arg0) {
				lblViewLog.setForeground(Color.BLUE);
			}
			@Override
			public void mouseExited(MouseEvent arg0) {
				lblViewLog.setForeground(Color.BLACK);
			}
		});
		lblViewLog.setBounds(10, 133, 89, 14);
		panel.add(lblViewLog);
		
		try
		{
		
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		
			SwingUtilities.updateComponentTreeUI(this);
			
		}catch(Exception e)
		{
		
		
		}
		
		JPanel dataWarehousePanel = new JPanel();
		dataWarehousePanel.setBounds(12, 191, 216, 138);
		contentPane.add(dataWarehousePanel);
		dataWarehousePanel.setBackground(Color.WHITE);
		dataWarehousePanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		dataWarehousePanel.setLayout(null);
		
		JLabel lblSalesDataWarehouse = new JLabel("Sales Data Warehouse");
		lblSalesDataWarehouse.setBounds(10, 0, 159, 31);
		dataWarehousePanel.add(lblSalesDataWarehouse);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(10, 27, 196, 2);
		dataWarehousePanel.add(separator);
		
		 lblProperties_1 = new JLabel("+ Properties");
		lblProperties_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				
				card.show(properties,"Sales Datawarehouse Properties");
			}
			@Override
			public void mouseEntered(MouseEvent arg0) {
				lblProperties_1.setForeground(Color.BLUE);
			}
			
			public void mouseExited(MouseEvent arg0) {
				lblProperties_1.setForeground(Color.BLACK);
			}
		});
		lblProperties_1.setBounds(10, 55, 127, 14);
		dataWarehousePanel.add(lblProperties_1);
		
		 lblLoadData = new JLabel("+ Load data (ECTL Operation)");
		lblLoadData.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				
				lblLoadData.setForeground(Color.BLUE);
			}
			
			public void mouseExited(MouseEvent arg0) {
				
				lblLoadData.setForeground(Color.BLACK);
			}
			@Override
			public void mouseClicked(MouseEvent arg0) {
				
				
				HashMap<String,String> map = sw.getMap();
				dialog.setMongoHost(map.get("Hostname"));
				dialog.setDbName(map.get("Database"));
				dialog.setVisible(true);
				
			}
		});
		lblLoadData.setBounds(10, 93, 196, 14);
		dataWarehousePanel.add(lblLoadData);
		dialog = new ETLManagerDialog(this);
	}
	
		
	
}
