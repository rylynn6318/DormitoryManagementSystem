import java.awt.EventQueue;
import java.awt.SystemColor;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;

public class Room_confirm extends JInternalFrame {

	public Room_confirm() {
		setBounds(0, 0, 2040, 1360);
		getContentPane().setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBackground(SystemColor.textHighlightText);
		panel.setBounds(0, 0, 2024, 1319);
		getContentPane().add(panel);
		panel.setLayout(null);
		setTitle("생활관 호실 확인");

	}

}
