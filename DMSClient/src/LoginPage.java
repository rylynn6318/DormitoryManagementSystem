import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.PopupMenu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class LoginPage extends JFrame {

	private JPanel contentPane;
	private PopupMenu textField;
	private JPasswordField passwordField;
	private JTextField inputId;
	private JPasswordField inputPw;
	private boolean succeed;
	JButton loginBtn;
	/**
	 * Launch the application.
	 */
	

	/**
	 * Create the frame.
	 */
	public LoginPage() {
		succeed = false;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(0, 0, 1680, 1050);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		setResizable(false);
		setLocationRelativeTo(null);
		JPanel panel_1 = new JPanel();
		panel_1.setBounds(359, 241, 966, 610);
		contentPane.add(panel_1);
		panel_1.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Login");
		lblNewLabel.setBounds(192, 86, 556, 103);
		panel_1.add(lblNewLabel);
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setFont(new Font("Arial", Font.BOLD, 55));
		
		JPanel panel = new JPanel();
		panel.setBorder(new LineBorder(new Color(0, 0, 0), 2));
		panel.setBounds(89, 251, 759, 261);
		panel_1.add(panel);
		panel.setLayout(null);
		//로그인 버튼
		loginBtn = new JButton("Login");
		loginBtn.setBounds(524, 87, 183, 103);
		panel.add(loginBtn);
		//아이디 입력 라벨
		JLabel lblId = new JLabel("ID :");
		lblId.setBounds(218, 68, 183, 59);
		panel.add(lblId);
		lblId.setFont(new Font("Arial", Font.BOLD, 29));
		//비밀번호 입력 라벨
		JLabel lblPassward = new JLabel("PASSWARD :");
		lblPassward.setBounds(93, 142, 242, 59);
		panel.add(lblPassward);
		lblPassward.setFont(new Font("Arial", Font.BOLD, 29));
		//아이디 입력 텍스트 필드
		setInputId(new JTextField());
		getInputId().setBounds(322, 87, 156, 27);
		panel.add(getInputId());
		getInputId().setColumns(10);
		//비밀번호 입력 패스워드 필드
		setInputPw(new JPasswordField());
		getInputPw().setBounds(322, 163, 156, 27);
		panel.add(getInputPw());
	}
	protected boolean isUser(String id, String pw)
	{
		if(id.equals(inputId.getText()) && pw.equals(inputPw.getText()))
			return true;
		else
			return false;
	}	
	boolean getSucceed()
	{
		return succeed;
	}
	public JTextField getInputId() {
		return inputId;
	}
	public void setInputId(JTextField inputId) {
		this.inputId = inputId;
	}
	public JPasswordField getInputPw() {
		return inputPw;
	}
	public void setInputPw(JPasswordField inputPw) {
		this.inputPw = inputPw;
	}
}

