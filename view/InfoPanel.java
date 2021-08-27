package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import control.ActionHandle;
import model.Core;
/**
 * Lop InfoPanel hien thi cac thong tin cho nguoi dung
 * @author Trinh Quang Tien
 *
 */
public class InfoPanel extends JPanel{
	private Core core;
	private Font font;
	private JLabel scoreLb,hiScoreLb;
	private JLabel statusImageLb,guideLb;
	private JButton continueBt;
	private JComboBox<String>sizeCb;
	private int numGuide=1;
	private ActionHandle action;
	private JButton newBt;
	public InfoPanel(Core core,ActionHandle action) {
		this.action=action;
		this.core=core;
		setPreferredSize(new Dimension(270,0));
		font=new Font(Font.SERIF, Font.BOLD, 25);
		continueBt=createButton("Continue...", "continueBt");
		continueBt.setEnabled(false);
		newBt=createButton("New Game", "newGameBt");
		setLayout(new GridLayout(5, 1));
		scoreLb=createLabel(this.core.getScore()+"", Color.magenta);
		hiScoreLb=createLabel(this.core.getHiScore()+"", Color.red);
		sizeCb=createComboBox();
		statusImageLb=createLabel("/images/welcome.png");
		if(this.core.getScore()>=2048)
		{
			setImage("/images/win.jpg", statusImageLb);			
		}
		else
			setImage("/images/welcome.png", statusImageLb);			
		add(createTitlePn());
		add(createStatusPn());
		add(createInfoPn());
		add(createSettingPn());
		add(createGuidePn());
		autoChangeGuideLb();
		this.setFocusable(false);
		this.setFocusTraversalKeysEnabled(false);
		
	}
	/**
	 * Tao TitlePn gom:
	 * label tieu de 2048;
	 * label tac gia
	 * @return titlePn
	 */
	public JPanel createTitlePn()
	{
		JPanel containNamePn=new JPanel();
		JPanel namePn=new JPanel(new GridLayout(1, 3));
		namePn.add(createLabel("2", 130, Color.red));
		namePn.add(createLabel("0", 130, Color.orange));
		namePn.add(createLabel("4", 130, Color.green));
		namePn.add(createLabel("8", 130, Color.blue));
		containNamePn.add(namePn);
		
		JPanel authorPn=new JPanel();
		authorPn.add(createLabel("by trinhtien6236@gmail.com", 20, Color.red));
		
		JPanel pn=new JPanel(new BorderLayout());
		pn.add(namePn,BorderLayout.CENTER);
		pn.add(authorPn,BorderLayout.SOUTH);
		return pn;
	}
	/**
	 * Tao label voi ten, size, mau chi dinh
	 * @param name ten
	 * @param size kich thuoc
	 * @param c mau
	 * @return label
	 */
	public JLabel createLabel(String name,int size,Color c)
	{
		JLabel lb=new JLabel(name);
		lb.setFont(new Font(Font.SERIF, Font.BOLD, size));
		lb.setForeground(c);
		return lb;
	}
	/**
	 * Tao welcomePn chua statusImageLb
	 * @return statusPn
	 */
	public JPanel createStatusPn()
	{
		JPanel pn=new JPanel();
		pn.add(statusImageLb);
		return pn;
	}
	/**
	 * Tao InfoPn chua scoreLb,hiScoreLb
	 * @return infoPn
	 */
	public JPanel createInfoPn()
	{
		JPanel pn=new JPanel(new GridLayout(2, 1));
		pn.setBorder(createBorder("Information"));
		pn.add(createScorePn(createLabel("Score:", Color.darkGray),scoreLb));
		pn.add(createScorePn(createLabel("High score:", Color.darkGray),hiScoreLb));
		return pn;
	}
	/**
	 * Tao SettingPn chua sizeCb,newBt,continueBt;
	 * @return settingPn
	 */
	public JPanel createSettingPn()
	{
		JPanel pn=new JPanel(new GridLayout(3, 1,10,10));
		pn.setBorder(createBorder("Setting"));
		pn.add(sizeCb);
		pn.add(newBt);
		pn.add(continueBt);
		return pn;		
	}
	/**
	 * Tao guidePn chua guideLb
	 * @return guidePn
	 */
	public JPanel createGuidePn()
	{
		JPanel pn=new JPanel();
		pn.setBorder(createBorder("Guide"));
		guideLb=createLabel("/images/guide0.png");
		pn.add(guideLb);
		return pn;
	}
	/**
	 * Tu dong thay doi anh trong guideLb sau 7 giay
	 */
	public void autoChangeGuideLb()
	{
		Timer timer=new Timer(7000, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				guideLb.setSize(250,250);
				setImage("/images/guide"+numGuide+".png", guideLb);
				if(numGuide==3)
					numGuide=0;
				else
					numGuide++;
				
			}
		});
		timer.start();
	}
	/**
	 * Cap nhat lai lop InfoPanel khi khoi tao lai lop Core.
	 * Gom cap nhat:
	 * scoreLb,hiScoreLb,continueBt,statusImageLb
	 * @param core lop Core moi
	 */
	public void update(Core core)
	{
		this.core=core;
		scoreLb.setText(this.core.getScore()+"");
		hiScoreLb.setText(this.core.getHiScore()+"");
		this.continueBt.setEnabled(false);
		if(this.core.getScore()>=2048)
		{
			setImage("/images/win.jpg", statusImageLb);			
		}
		else
			setImage("/images/welcome.png", statusImageLb);			
		this.repaint();
	}
	/**
	 * Tao label va setImage cho label 
	 * @param path duong dan den image file 
	 * @return label
	 */
	public JLabel createLabel(String path)
	{
		JLabel lb=new JLabel();
		lb.setSize(250, 250);
		setImage(path, lb);

		return lb;
	}
	/**
	 * Tao border cho panel
	 * @param title ten panel
	 * @return border
	 */
	public Border createBorder(String title)
	{
		Font titleFont=new Font(Font.SERIF, Font.BOLD, 30);
		Border borderBound=BorderFactory.createLineBorder(Color.LIGHT_GRAY);
		Border border=BorderFactory.createTitledBorder(borderBound, title, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, titleFont, Color.gray);
		return border;
	}
	/**
	 * Tao ComboBox va set cac thuoc tinh 
	 * @return comboBox
	 */
	public JComboBox<String> createComboBox()
	{
		String[] arr= {"3x3","4x4","5x5","6x6","8x8","10x10"};
		JComboBox<String> combo=new JComboBox<String>(arr) {
			@Override
			protected void paintComponent(Graphics g) {
				// TODO Auto-generated method stub
				super.paintComponent(g);
				setBackground(Color.lightGray);
			}
		};
		combo.setSelectedItem(core.getSize()+"x"+core.getSize());;
		combo.setFont(font);
		combo.addActionListener(this.action);
		combo.setActionCommand("sizeCb");
		combo.setFocusable(false);
		return combo;
	}
	/**
	 * Tao ScorePn chua label va scoreLb
	 * @param nameLb ten label
	 * @param scoreLb 
	 * @return scorePn
	 */
	public JPanel createScorePn(JLabel nameLb,JLabel scoreLb)
	{
		JPanel pn=new JPanel(new GridLayout(2,1));
		pn.add(nameLb);
		pn.add(scoreLb);
		return pn;
	}
	/**
	 * Tao label va set ten va mau 
	 * @param name ten label
	 * @param c mau label
	 * @return label
	 */
	public JLabel createLabel(String name,Color c)
	{
		JLabel lb=new JLabel(name);
		lb.setForeground(c);
		lb.setFont(font);
		return lb;
	}
	/**
	 * Tao button voi ten, set action va set color
	 * @param name ten button
	 * @param actionCommand 
	 * @return button
	 */
	public JButton createButton(String name,String actionCommand)
	{
		JButton bt=new JButton(name) {
			@Override
			protected void paintComponent(Graphics g) {
				// TODO Auto-generated method stub
				super.paintComponent(g);
				setBackground(Color.lightGray);
			}
		};
		bt.setFont(font);
		bt.addActionListener(this.action);
		bt.setActionCommand(actionCommand);
		bt.setFocusable(false);
		return bt;
	}
	/**
	 * Load Image trong file jar thi phai dung InputStream
	 * Set image cho lable;
	 * Dinh dang lai anh de vua voi kich thuoc cua lable.
	 * @param path duong dan den image file
	 * @param label label can set image
	 */
	public void setImage(String path,JLabel label)
	{
		try {
			BufferedImage image = ImageIO.read(this.getClass().getResourceAsStream(path));
			int x = label.getWidth();
			int y = label.getHeight();
			int ix = image.getWidth();
			int iy = image.getHeight();
			int dx,dy;
			if((x/y)>(ix/iy))
			{
				dy = y;
				dx = dy*ix/iy;
			}
			else
			{
				dx = x;
				dy = dx*iy/ix;
			}
			ImageIcon icon = new ImageIcon(image.getScaledInstance(dx,dy, image.SCALE_SMOOTH));
			label.setIcon(icon);
		} catch (IOException ex) {
			Logger.getLogger(InfoPanel.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	/**
	 * @return the scoreLb
	 */
	public JLabel getScoreLb() {
		return scoreLb;
	}
	/**
	 * @param scoreLb the scoreLb to set
	 */
	public void setScoreLb(JLabel scoreLb) {
		this.scoreLb = scoreLb;
	}
	/**
	 * @return the hiScoreLb
	 */
	public JLabel getHiScoreLb() {
		return hiScoreLb;
	}
	/**
	 * @param hiScoreLb the hiScoreLb to set
	 */
	public void setHiScoreLb(JLabel hiScoreLb) {
		this.hiScoreLb = hiScoreLb;
	}
	/**
	 * @return the continueBt
	 */
	public JButton getContinueBt() {
		return continueBt;
	}
	/**
	 * @param continueBt the continueBt to set
	 */
	public void setContinueBt(JButton continueBt) {
		this.continueBt = continueBt;
	}
	/**
	 * @return the imageLb
	 */
	public JLabel getImageLb() {
		return statusImageLb;
	}
	/**
	 * @param imageLb the imageLb to set
	 */
	public void setImageLb(JLabel imageLb) {
		this.statusImageLb = imageLb;
	}
	/**
	 * @return comboBox
	 */
	public JComboBox<String>getSizeCb()
	{
		return this.sizeCb;
	}
	/**
	 * 
	 * @param core the core to set
	 */
	public void setCorePn(Core core)
	{
		this.core=core;
	}
	
	
	


}
