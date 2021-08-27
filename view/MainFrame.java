package view;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.Toolkit;
import javax.swing.JFrame;

/**
 * Hien thi game
 * @author Trinh Quang Tien
 * @version 1.0.0.0
 */
public class MainFrame extends JFrame {

	public MainFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1270,1030);
		setTitle("2048 Offline");
		Image image=Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("/images/2048.jpg"));
		setIconImage(image);
		add(new MainPanel());
		setResizable(false);
		setLocationRelativeTo(null);
		setVisible(true);
	}
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				new MainFrame();
			}
		});
	}

}
