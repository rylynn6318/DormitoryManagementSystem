import java.awt.EventQueue;
import java.awt.SystemColor;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;

public class Fee_registration extends JInternalFrame {

	
	public Fee_registration() {
		setBounds(0, 0, 2040, 1360);
		getContentPane().setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBackground(SystemColor.textHighlightText);
		panel.setBounds(0, 0, 2024, 1319);
		getContentPane().add(panel);
		panel.setLayout(null);
		setTitle("생활관 사용료 및 급식비 등록");

	}

}
