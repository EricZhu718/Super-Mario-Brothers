import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class optionJFrames {
	static class BrickJFrame extends JFrame {
		String BrickCode;
		boolean canBreak = false;
		int coins = 0;

		BrickJFrame() {
			setResizable(true);
			
			JTextField getCoins = new JTextField();
			getCoins.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					JTextField source = (JTextField) arg0.getSource();
					boolean validLastChar = false;
					for (int i = 0; i < 10; i++) {
						validLastChar = source.getText().charAt(source.getText().length() - 1) == ("" + i).charAt(0)
								|| validLastChar;
					}
					if (!validLastChar) {
						source.setText(source.getText().substring(0, source.getText().length() - 1));
					} else {
						coins = Integer.parseInt(source.getText());
					}
					BrickCode = "Brick:" + coins + ":" + canBreak;
					System.out.println("TextField " + BrickCode);
				}
			});
			getCoins.setText("0");
			getCoins.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));
			JComboBox breakable = new JComboBox(
					new String[] { "Cannot be broken by large Mario", "Can be broken by large Mario" });
			breakable.setSelectedIndex(0);
			breakable.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					canBreak = ((JComboBox) arg0.getSource()).getSelectedIndex() == 1;
					BrickCode = "Brick:" + coins + ":" + canBreak;
					System.out.println("ComboBox " + BrickCode);
				}
			});
			breakable.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));
			setSize(300, 400);
			setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
			add(getCoins);
			add(breakable);
			

		}
	}

	static JFrame Brick = new BrickJFrame();

	public static void main(String[] args) {
		Brick.setVisible(true);
	}
}
