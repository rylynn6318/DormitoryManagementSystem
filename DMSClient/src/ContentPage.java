import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
//import com.jgoodies.forms.layout.FormLayout;
//import com.jgoodies.forms.layout.ColumnSpec;
//import com.jgoodies.forms.layout.RowSpec;
//import net.miginfocom.swing.MigLayout;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.SystemColor;

public class ContentPage extends JFrame {

	private JPanel contentPane;
	protected Object inputId;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ContentPage frame = new ContentPage();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ContentPage() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 2400, 1400);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBackground(Color.WHITE);
		panel.setBounds(0, 0, 352, 1360);
		contentPane.add(panel);
		panel.setLayout(null);

		JPanel contentPanel = new JPanel();
		contentPanel.setBounds(354, 0, 2040, 1360);
		contentPane.add(contentPanel);
		setResizable(false);
		setLocationRelativeTo(null);
		contentPanel.setLayout(null);
		//신청페이지
		Apply apply = new Apply();
		contentPanel.add(apply);
		apply.setResizable(false);
		apply.setVisible(false);
		//신청확인 페이지
		Apply_confirm apply_confirm = new Apply_confirm();
		contentPanel.add(apply_confirm);
		apply_confirm.setResizable(false);
		apply_confirm.setVisible(false);
		//호실확인 페이지
		Room_confirm room_confirm = new Room_confirm();
		contentPanel.add(room_confirm);
		room_confirm.setResizable(false);
		room_confirm.setVisible(false);
		//결핵진단서 제출
		Diagnosis_submit diagnosis_submit = new Diagnosis_submit();
		contentPanel.add(diagnosis_submit);
		diagnosis_submit.setResizable(false);
		diagnosis_submit.setVisible(false);
		//
		
		
		
		JButton btnNewButton = new JButton("생활관 입사 신청");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				apply.setVisible(true);
			}
		});
		
		
		
		btnNewButton.setFont(new Font("LG PC", Font.BOLD, 22));
		btnNewButton.setBounds(25, 30, 302, 46);
		panel.add(btnNewButton);
		
		JButton button = new JButton("생활관 호실 확인");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		button.setFont(new Font("LG PC", Font.BOLD, 22));
		button.setBounds(25, 128, 302, 46);
		panel.add(button);
		
		JButton button_1 = new JButton("생활관 신청확인/고지서 출력");
		button_1.setFont(new Font("LG PC", Font.BOLD, 22));
		button_1.setBounds(25, 236, 302, 46);
		panel.add(button_1);
		
		JButton button_2 = new JButton("결핵진단서 제출");
		button_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		button_2.setFont(new Font("LG PC", Font.BOLD, 22));
		button_2.setBounds(25, 347, 302, 46);
		panel.add(button_2);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBounds(0, 441, 352, 919);
		panel.add(panel_2);
		panel_2.setBackground(SystemColor.inactiveCaptionBorder);
		panel_2.setLayout(null);
		
		JButton button_3 = new JButton("배정");
		button_3.setFont(new Font("LG PC", Font.BOLD, 22));
		button_3.setBounds(17, 60, 302, 46);
		panel_2.add(button_3);
		
		JButton button_4 = new JButton("선발 일정 등록");
		button_4.setFont(new Font("LG PC", Font.BOLD, 22));
		button_4.setBounds(17, 160, 302, 46);
		panel_2.add(button_4);
		
		JButton button_5 = new JButton("생활관 사용료 및 급식비 등록");
		button_5.setFont(new Font("LG PC", Font.BOLD, 22));
		button_5.setBounds(17, 260, 302, 46);
		panel_2.add(button_5);
		
		JButton button_6 = new JButton("입사 선발자 결과 등록");
		button_6.setFont(new Font("LG PC", Font.BOLD, 22));
		button_6.setBounds(17, 360, 302, 46);
		panel_2.add(button_6);
		
		JButton button_7 = new JButton("입사자 등록 및 조회");
		button_7.setFont(new Font("LG PC", Font.BOLD, 22));
		button_7.setBounds(17, 460, 302, 46);
		panel_2.add(button_7);
		
		JButton button_8 = new JButton("결핵 진단서 제출 확인");
		button_8.setFont(new Font("LG PC", Font.BOLD, 22));
		button_8.setBounds(17, 560, 302, 46);
		panel_2.add(button_8);
		
		JButton button_9 = new JButton("은행파일 업로드");
		button_9.setFont(new Font("LG PC", Font.BOLD, 22));
		button_9.setBounds(17, 660, 302, 46);
		panel_2.add(button_9);
		
		JButton button_10 = new JButton("생활관 입사자 현황 조회");
		button_10.setFont(new Font("LG PC", Font.BOLD, 22));
		button_10.setBounds(17, 760, 302, 46);
		panel_2.add(button_10);
		
		JButton button_11 = new JButton("호실별 입사자 명단");
		button_11.setFont(new Font("LG PC", Font.BOLD, 22));
		button_11.setBounds(17, 858, 302, 46);
		panel_2.add(button_11);
		
	}
}
