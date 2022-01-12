package view.login;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import controller.LoginController;

public class LoginFrame extends JFrame implements ActionListener{
	private LoginController controller = LoginController.getInstance();
	
	private JLabel nameLable, passLable, title;
	private JTextField nameField, passField;
	private JPanel northPanel, southPanel, centerPanel, nameLablePanel, nameFieldPanel, passFieldPanel, passLablePanel;
	private JButton loginButton;
	private Dimension fieldDimension;
	
	private static LoginFrame instance = null;
	
	private LoginFrame() {
		fieldDimension = new Dimension(200, 30);
		
		this.setSize(new Dimension(500, 200));
		this.setTitle("Login");
		this.setLayout(new BorderLayout());
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		
		//North Panel
		northPanel = new JPanel();
		title = new JLabel("Registration");
		northPanel.add(title);
		northPanel .setLayout(new FlowLayout());
		
		//Center Panel
		centerPanel = new JPanel();
		centerPanel.setLayout(new GridLayout(2,2));
		nameLable = new JLabel("User Name");
		nameLable.setPreferredSize(fieldDimension);
		passLable = new JLabel("Password");
		passLable.setPreferredSize(fieldDimension);
		nameField = new JTextField();
		nameField.setPreferredSize(fieldDimension);
		passField = new JPasswordField();
		passField.setPreferredSize(fieldDimension);
		
		nameLablePanel = new JPanel();
		nameFieldPanel = new JPanel();
		passFieldPanel = new JPanel();
		passLablePanel = new JPanel();
		nameLablePanel.add(nameLable);
		passLablePanel.add(passLable);
		nameFieldPanel.add(nameField);
		passFieldPanel.add(passField);
		
		centerPanel.add(nameLablePanel);
		centerPanel.add(nameFieldPanel);
		centerPanel.add(passLablePanel);
		centerPanel.add(passFieldPanel);
		
		//South Panel
		southPanel = new JPanel();
		loginButton = new JButton("Login");
		loginButton.addActionListener(this) ;
		southPanel.add(loginButton);
		
		this.add(northPanel, BorderLayout.NORTH);
		this.add(centerPanel, BorderLayout.CENTER);
		this.add(southPanel, BorderLayout.SOUTH);
		
		this.setVisible(false);
	}
	
	public void showLoginFail() {
		String message = "Wrong username or password!";
		JOptionPane.showMessageDialog(this, message, "FAIL", JOptionPane.ERROR_MESSAGE);
		this.reset();
	}
	
	public void reset() {
		nameField.setText("");
		passField.setText("");
	}
	
	public static synchronized LoginFrame getPage() {
		if (instance == null) {
			instance = new LoginFrame();
		}
		return instance;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == loginButton) {
			String name = nameField.getText().trim();
			String pass = passField.getText();
			controller.login(name, pass);
		}
	}

}
