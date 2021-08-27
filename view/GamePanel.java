package view;
import model.*;
import control.*;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;
/**
 * Lop GamePanel the hien mainBoard cua lop Core thanh GUI
 * @author Trinh Quang Tien
 *
 */
public class GamePanel extends JPanel{
	private Core core;
	private int[][] board;
	private int width;//Chieu rong cua 1 o
	private StringMetricGUI stringMetric;
	private ActionHandle action;
	private int widthLine;//Kich thuoc duong ke phan tach cac o
	public GamePanel(Core core,ActionHandle action) {
		this.core=core;
		this.action=action;
		this.board=core.getMainBoard();
		this.widthLine=990/core.getSize()/10;//Chieu rong duong ke bang 1/10 chieu rong 1 o
		this.width=990/core.getSize();
		this.addMouseListener(this.action);
		this.addKeyListener(this.action);
		this.setFocusable(true);
		this.setFocusTraversalKeysEnabled(false);
	}
	/**
	 * Cap nhat lai lop GamePanel khi khoi tao lai lop Core
	 * @param core lop Core moi
	 */
	public void update(Core core)
	{
		this.core=core;
		this.board=this.core.getMainBoard();
		this.widthLine=990/core.getSize()/10;
		this.width=990/core.getSize();
		this.repaint();
	}
	@Override
	protected void paintComponent(Graphics g) {
		// TODO Auto-generated method stub
		super.paintComponent(g);
		setBackground(Color.lightGray);
		for(int i=0;i<board.length;i++)
		{
			outLoop:for(int j=0;j<board[i].length;j++)
			{
				if(board[i][j]!=0)
				{
					//So chu so cua 1 so
					//Vi du: 22 co 2 chu so
					int nums=(board[i][j]+"").length();
					int sizeChar=0;//Kich thuoc cua so 
					if(nums>0 && nums<=3)
						sizeChar=width/2;
					if(nums>3 && nums<=6)
						sizeChar=width/4;
					else if(nums>6)
						sizeChar=width/6;
					g.setFont(new Font(Font.SERIF, Font.BOLD,sizeChar));

					//	Lay kich thuoc cua chu
					//		Cach 1:
					//			int stringWidth=g.getFontMetrics().stringWidth(board[i][j]+"");
					//		Cach 2:
					//			stringWidth lay chieu rong cua chu hien thi
					//			stringHeight lay chieu cao bang 2 lan chieu cao hien thi

					stringMetric=new StringMetricGUI((Graphics2D)g);
					int stringWidth=(int) stringMetric.getWith(board[i][j]+"");//Chieu rong cua 1 chuoi
					int stringHeight=(int) stringMetric.getHeight(board[i][j]+"");//Chieu cao cua 1 chuoi
					int num=board[i][j];
					// set mau cua o dua vao so chu so cua gia tri cua o
					switch (num)
					{
					case 2:
					case 4:
						g.setColor(Color.white);
						g.fillRect(j*width, i*width, width, width);
						g.setColor(Color.gray);	
						g.drawString(board[i][j]+"", j*width+width/2-stringWidth/2, i*width+width/2+stringHeight/4);
						continue outLoop;
					case 8:
					case 16:
						g.setColor(Color.pink);
						break;
					case 32:
					case 64:
						g.setColor(Color.yellow);
						break;
					case 128:
					case 256:
						g.setColor(Color.orange);
						break;
					case 512 :
					case 1024:
						g.setColor(Color.magenta);
						break;			
					default:
						if(num>1024 && num<=8192)
							g.setColor(Color.red);
						else if(num>8192 && num<=65536)
							g.setColor(Color.cyan);
						else if(num>65536 && num<=524288)
							g.setColor(Color.blue);
						else if(num>524288 && num<=8388608)
							g.setColor(Color.green);
						else
							g.setColor(Color.black);
						break;

					}
					g.fillRect(j*width, i*width, width, width);
					g.setColor(Color.white);	
					g.drawString(board[i][j]+"", j*width+width/2-stringWidth/2, i*width+width/2+stringHeight/4);

				}
			}
		}
		// Ve 1 hinh chu nhat voi chieu rong duong bien bang widthLine de lam duong line
		Graphics2D g2d=(Graphics2D)g;
		g2d.setStroke(new BasicStroke(this.widthLine));
		for(int i=0;i<board.length;i++)
		{
			for(int j=0;j<board[i].length;j++)
			{
				g2d.setColor(Color.gray);
				g2d.drawRect(j*width, i*width, width, width);
			}
		}

	}

}
