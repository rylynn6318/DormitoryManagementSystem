import java.awt.EventQueue;
import java.awt.SystemColor;

import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;

public class Apply extends JInternalFrame {

	public Apply() {
		setBounds(0, 0, 2040, 1360);
		getContentPane().setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBackground(SystemColor.textHighlightText);
		panel.setBounds(0, 0, 2024, 1319);
		getContentPane().add(panel);
		panel.setLayout(null);
		setTitle("생활관 신청");
	}

}
