package view;
import model.*;
import control.*;
import java.awt.BorderLayout;
import javax.swing.JPanel;
/**
 * Panel chinh chua GamePanel va InfoPanel
 * @author Trinh Quang Tien
 *
 */
public class MainPanel extends JPanel{
	public MainPanel() {
		Core core=new Core();
		
		ActionHandle action=new ActionHandle(core, null, null);
		GamePanel gamePn=new GamePanel(core, action);
		InfoPanel infoPn=new InfoPanel(core, action);
		
		action.setGamePn(gamePn);
		action.setInfoPn(infoPn);
		
		setLayout(new BorderLayout());
		add(gamePn,BorderLayout.CENTER);
		add(infoPn,BorderLayout.EAST);
		
	}
}
