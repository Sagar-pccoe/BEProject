package app;


import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import app.tools.User;

public class ViewUserPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	String[] colHeads = { "User Name", "Mobile No", "Email" };
	
	// Initialize data.
	Object[][] data={};
	
	JTable table;
	
	public ViewUserPanel() throws URISyntaxException {
		setLayout(null);
	

		JScrollPane pane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		pane.setBounds(51, 38, 752, 369);
		
		read();
		
		table = new JTable(data,colHeads);
		table.setEnabled(false);
		
		pane.setViewportView(table);
		
		add(pane);
		
	}
	
	void read() throws URISyntaxException 
	{
		ArrayList<User> list = null;
		try {
			
			list = (ArrayList<User>) User.read();
		
		Iterator<User> it = list.iterator();
		
		data = new Object[list.size()][3];
		
		int i = 0,j;
		User user;
		
		while(it.hasNext())
		{
			j=0;
			user = it.next();
			data[i][j++] = user.getUsername();
			data[i][j++] = user.getMobileNo();
			data[i][j] = user.getEmail();
			i++;
		}

	} catch (ClassNotFoundException | IOException e) {
		e.printStackTrace();
		
	}
		
  }
}
