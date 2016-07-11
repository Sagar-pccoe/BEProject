package app;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import javax.swing.border.EtchedBorder;

import java.awt.HeadlessException;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;

import app.tools.User;
import java.awt.Font;

public class UserPanel extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField txtUserName;
	private JPasswordField txtPassword;
	private JTextField txtMobileNo;
	private JTextField txtEmail;

	/**
	 * Create the panel.
	 */
	public UserPanel() {
		setLayout(null);
		
		
		JLabel lblUserimage = new JLabel("userImage");
		lblUserimage.setIcon(new ImageIcon(UserPanel.class.getResource("/app/images/staff.jpg")));
		lblUserimage.setBounds(440, 11, 100, 104);
		add(lblUserimage);
		
		JPanel panel = new JPanel();
		panel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel.setBounds(268, 142, 443, 152);
		add(panel);
		panel.setLayout(null);
		
		JLabel lblUserName = new JLabel("User Name:");
		lblUserName.setBounds(45, 11, 95, 24);
		panel.add(lblUserName);
		
		txtUserName = new JTextField();
		txtUserName.setBounds(158, 14, 193, 20);
		panel.add(txtUserName);
		txtUserName.setColumns(10);
		
		JLabel lblPassword = new JLabel("Password:");
		lblPassword.setBounds(45, 47, 95, 14);
		panel.add(lblPassword);
		
		txtPassword = new JPasswordField();
		txtPassword.setBounds(158, 46, 193, 20);
		panel.add(txtPassword);
		
		JLabel lblMobileNo = new JLabel("Mobile No:");
		lblMobileNo.setBounds(45, 80, 87, 14);
		panel.add(lblMobileNo);
		
		txtMobileNo = new JTextField();
		
		
		txtMobileNo.setBounds(158, 78, 193, 20);
		panel.add(txtMobileNo);
		txtMobileNo.setColumns(10);
		
		JLabel lblEmailid = new JLabel("Email-Id:");
		lblEmailid.setBounds(51, 112, 81, 14);
		panel.add(lblEmailid);
		
		txtEmail = new JTextField();
		txtEmail.setBounds(158, 110, 193, 20);
		panel.add(txtEmail);
		txtEmail.setColumns(10);
		
		JLabel lblAddNewUser = new JLabel("Add New User");
		lblAddNewUser.setFont(new Font("SansSerif", Font.PLAIN, 14));
		lblAddNewUser.setBounds(10, 11, 145, 22);
		add(lblAddNewUser);
		
		JButton btnSubmit = new JButton("Submit");
		btnSubmit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				String un = txtUserName.getText();
				char pwd[] = txtPassword.getPassword();
				String mn = txtMobileNo.getText();
				long mobileno;
				String email = txtEmail.getText();
				
				String emailPattern ="^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
		    			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
				
				try
				{
				
					if( pwd.length < 8)
					{
						JOptionPane.showMessageDialog(null,"Password must be of eight characters or more.","Password too short.",JOptionPane.ERROR_MESSAGE);
						 return;
					}
					else
						if(mn.length() != 10)
						{
							JOptionPane.showMessageDialog(null,"Mobile number must be of 10 digits.","Wrong mobile number.",JOptionPane.ERROR_MESSAGE);
							return;
						}
						else
							 if(!email.matches(emailPattern))
							 {
								 JOptionPane.showMessageDialog(null,"Enter valid email address","Invalid Email Address.",JOptionPane.ERROR_MESSAGE);
									return;
							 }
						
				    	mobileno= Long.parseLong(mn);
				    	
				    	User user = new User(un,new String(pwd),mobileno,email);
				    	
				    	if(user.insert())
				    	{
				    		JOptionPane.showMessageDialog(null, "User information has been sucessfully Added.","Success..!",JOptionPane.INFORMATION_MESSAGE);
				    		clear();
				    	}
				    	else
				    		JOptionPane.showMessageDialog(null,"Please choose another username.","User already exist.",JOptionPane.ERROR_MESSAGE);
				   
				}
				catch(NumberFormatException e)
				{
					JOptionPane.showMessageDialog(null,"Please Enter correct mobile number.","Wrong mobile number.",JOptionPane.ERROR_MESSAGE);
					return;
				} catch (HeadlessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(null, e.getMessage());
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}catch(URISyntaxException e)
				{
					JOptionPane.showMessageDialog(null, e.getMessage());
				}
			}
		});
		btnSubmit.setBounds(368, 331, 108, 23);
		add(btnSubmit);
		
		JButton btnClear = new JButton("Clear");
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				clear();
			}
		});
		btnClear.setBounds(528, 331, 101, 23);
		add(btnClear);

	}
	
	void clear()
	{
		txtUserName.setText("");
		txtPassword.setText("");
		txtMobileNo.setText("");
		txtEmail.setText("");
	}
}
