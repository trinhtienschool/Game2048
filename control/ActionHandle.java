package control;
import model.*;
import view.*;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JOptionPane;
/**
 * Lop ActionHandle xu li cac van de ve su kien
 * @author Trinh Quang Tien
 */
public class ActionHandle implements KeyListener,ActionListener,MouseListener{
	private int x;
	private int y;
	private boolean showWinMessage;//hien thi JOptionPanel thong bao chien thang,dam bao thong bao chien thang chi xuat hien 1 lan
	private Core core;
	private GamePanel gamePn;
	private InfoPanel infoPn;
	public ActionHandle(Core core,GamePanel gamePn,InfoPanel infoPn) {
		x=0;
		y=0;
		this.core=core;
		showWinMessage=false;
		this.gamePn=gamePn;
		this.infoPn=infoPn;
	}
	/**
	 * Cap nhat lai lop Core. Khi lop Core duoc khoi tao moi thi doi voi lop ActionHandle co bien core va showWinPn thay doi gia tri
	 * @param core lop Core
	 */
	public void update(Core core)
	{
		this.core=core;
		showWinMessage=false;
	}
	/**
	 * Xu li su kien khi nhan phim.
	 * Khi nhan cac phim su kien thi:
	 * Goi lop Core de xu li su kien khi nhan phim tuong ung TRAI, PHAI,LEN,XUONG,UNDO.
	 * Lop GamePanel se repain() chinh no
	 * Kiem tra ket thuc game
	 * Cap nhat lai scoreLb,hiScoreLb,statusImageLb cua lop InfoPanel
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode()==KeyEvent.VK_CONTROL ||e.getKeyCode()==KeyEvent.VK_U || e.getKeyCode()==KeyEvent.VK_E)
		{
			core.undo();
			gamePn.repaint();
			infoPn.getScoreLb().setText(core.getScore()+"");
			if(core.getMaxValue()<2048)
				showWinMessage=false;
			core.setEndGame(false);
			if(core.getMaxValue()>=2048)
				infoPn.setImage("/images/win.jpg", infoPn.getImageLb());
			else
				infoPn.setImage("/images/welcome.png", infoPn.getImageLb());
			infoPn.repaint();	
			return;
		}
		else if(e.getKeyCode()==KeyEvent.VK_LEFT || e.getKeyCode()==KeyEvent.VK_J || e.getKeyCode()==KeyEvent.VK_A)
			core.turnLeft();
		else if(e.getKeyCode()==KeyEvent.VK_RIGHT || e.getKeyCode()==KeyEvent.VK_L || e.getKeyCode()==KeyEvent.VK_D)
			core.turnRight();
		else if(e.getKeyCode()==KeyEvent.VK_UP || e.getKeyCode()==KeyEvent.VK_I || e.getKeyCode()==KeyEvent.VK_W)
			core.turnUp();
		else if(e.getKeyCode()==KeyEvent.VK_DOWN || e.getKeyCode()==KeyEvent.VK_K || e.getKeyCode()==KeyEvent.VK_S)
			core.turnDown();
		gamePn.repaint();
		infoPn.getScoreLb().setText(core.getScore()+"");
		infoPn.getHiScoreLb().setText(core.getHiScore()+"");
		if(core.getMaxValue()==2048 && showWinMessage==false)
		{
			showWinMessage=true;
			infoPn.setImage("/images/win.jpg", infoPn.getImageLb());
			infoPn.repaint();
			JOptionPane.showMessageDialog(gamePn, infoPn.createLabel("You win!", Color.black));
		}
		else if(core.getEndGame())
		{
			infoPn.setImage("/images/gameOver.jpg", infoPn.getImageLb());
			infoPn.repaint();		
			JOptionPane.showMessageDialog(gamePn, infoPn.createLabel("Game over!", Color.black));
		}
		else 
			infoPn.repaint();


	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}
	/**
	 * Xu li newGameBt,continueBt,sizeCb.
	 * Neu sizeCb thi:
	 * 	kiem tra file tuong ung voi mang game do co ton tai hay khong;
	 * 	neu ton tai thi hien nut Continue; 
	 * 	neu khong ton tai thi an nut Continue.
	 * Neu newGameBt thi:
	 * 	lay kich thuoc game cua sizeCb va tao new Core;
	 * 	cap nhat lai lop GamePanel,InfoPanel;
	 * 	goi lop Core de saveGame() va saveLastOpen().
	 * Neu continueBt thi:
	 *  khoi tao lai lop Core voi mang da choi
	 *  cap nhat lai lop GamePanel,InfoPanel;
	 * 	goi lop Core de saveLastOpen().
	 *  
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("sizeCb"))
		{
			String size=(String)(infoPn.getSizeCb().getSelectedItem());
			size=size.substring(0,size.indexOf("x"));
			if(FileIO.checkExistFile("/2048_data/matrix"+size))
			{
				infoPn.getContinueBt().setEnabled(true);
				infoPn.repaint();
			}
			else
			{
				infoPn.getContinueBt().setEnabled(false);
				infoPn.repaint();
			}			
		}
		else if(e.getActionCommand().equals("newGameBt"))
		{
			String size=(String)(infoPn.getSizeCb().getSelectedItem());
			size=size.substring(0,size.indexOf("x"));
			Core core=new Core(Integer.parseInt(size), false);
			this.update(core);
			gamePn.update(core);
			infoPn.update(core);
			core.saveGame();
			core.saveLastOpen(Integer.parseInt(size));

		}
		else if(e.getActionCommand().equals("continueBt"))
		{
			String size=(String)(infoPn.getSizeCb().getSelectedItem());
			size=size.substring(0,size.indexOf("x"));
			Core core=new Core(Integer.parseInt(size), true);
			this.update(core);
			gamePn.update(core);
			infoPn.getContinueBt().setEnabled(false);
			infoPn.update(core);
			core.saveLastOpen(Integer.parseInt(size));
		}
	}
	/**
	 * Khi click chuot phai thi thuc hien UNDO:
	 * Goi lop Core de thuc hien Undo
	 * Lop GamePanel repaint() lai chinh no
	 * Kiem tra ket thuc game
	 * Cap nhat lai scoreLb,hiScoreLb,statusImageLb cua lop InfoPanel
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
		if(e.getButton()==MouseEvent.BUTTON3)
		{
			core.undo();
			gamePn.repaint();
			infoPn.getScoreLb().setText(core.getScore()+"");
			if(core.getMaxValue()<2048)
				showWinMessage=false;
			core.setEndGame(false);
			if(core.getMaxValue()>=2048)
				infoPn.setImage("/images/win.jpg", infoPn.getImageLb());
			else
				infoPn.setImage("/images/welcome.png", infoPn.getImageLb());
			infoPn.repaint();	
		}

	}
	/**
	 * Khi click chuot trai thi lay vi tri x,y
	 */
	@Override
	public void mousePressed(MouseEvent e) {
		if(e.getButton()==MouseEvent.BUTTON1)
		{
			x=e.getX();
			y=e.getY();
		}
	}
	/**
	 * Khi tha chuot trai thi thuc hien:
	 * x=vi tri x hien tai - x;
	 * y=vi tri y hien tai -y;
	 * Neu |x|>|y|:
	 * 	neu x>0: qua phai.
	 * 	neu x<0: qua trai.
	 * Neu |x|<|y|:
	 * 	neu y>0: xuong.
	 * 	neu y<0: len.
	 * Sau do goi lop Core de thuc hien su kien
	 * Lop GamePanel repaint() lai chinh no
	 * Kiem tra ket thuc game
	 * Cap nhat lai scoreLb,hiScoreLb,statusImageLb cua lop InfoPanel
	 */
	@Override
	public void mouseReleased(MouseEvent e) {
		if(e.getButton()==MouseEvent.BUTTON1)
		{
			x=e.getX()-x;
			y=e.getY()-y;
			if(Math.abs(x)>Math.abs(y))
			{
				if(x>0)
					core.turnRight();
				else if(x<0)
					core.turnLeft();
			}
			else if(Math.abs(x)<Math.abs(y))
			{
				if(y>0)
					core.turnDown();
				else if(y<0)
					core.turnUp();
			}
			gamePn.repaint();
			infoPn.getScoreLb().setText(core.getScore()+"");
			infoPn.getHiScoreLb().setText(core.getHiScore()+"");
			if(core.getMaxValue()==2048 && showWinMessage==false)
			{
				showWinMessage=true;
				infoPn.setImage("/images/win.jpg", infoPn.getImageLb());
				infoPn.repaint();
				JOptionPane.showMessageDialog(gamePn, infoPn.createLabel("You win!", Color.black));
			}
			else if(core.getEndGame())
			{
				infoPn.setImage("/images/gameOver.jpg", infoPn.getImageLb());
				infoPn.repaint();		
				JOptionPane.showMessageDialog(gamePn, infoPn.createLabel("Game over!", Color.black));
			}
			else 
				infoPn.repaint();

		}

	}
	/**
	 * @param gamePn
	 */
	public void setGamePn(GamePanel gamePn) {
		this.gamePn = gamePn;
	}
	/**
	 * @param infoPn the infoPn to set
	 */
	public void setInfoPn(InfoPanel infoPn) {
		this.infoPn = infoPn;
	}
	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
