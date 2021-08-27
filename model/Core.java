package model;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;
/**
 * Lop Core xu li cac van de cot loi cua game
 *
 * @author Trinh Quang Tien
 * @since 2019-11-21
 * @version 1.0.0.0
 */
public class Core {
	private int[][] mainBoard;
	private int[][] bufferBoard;
	private int bufferScore;
	private int size;
	private boolean signInitValue;
	private boolean signBackup;
	private int score;
	private int hiScore;
	private String path;
	private long time;
	private boolean endGame;
	private HashMap<Integer, Integer> hiScoreMap;
	/**
	 * Contructor tu dong do xem mang game cuoi cung choi la mang nao va load 
	 */
	public Core() {
		FileIO.initDataFolder();
		if(!FileIO.checkExistFile("/2048_data/lastOpen"))
		{
			
			if(!FileIO.checkExistFile("/2048_data/matrix4"))
				initContructor(4, false);
			else
				initContructor(4, true);
			saveLastOpen(4);
		}
		else
		{
			LinkedList<String>list=FileIO.loadFile("/2048_data/lastOpen");
			this.size=Integer.parseInt(list.getFirst());
			if(!FileIO.checkExistFile("/2048_data/matrix"+this.size))
			{
				if(!FileIO.checkExistFile("/2048_data/matrix4"))
					initContructor(4, false);
				else
					initContructor(4, true);
				saveLastOpen(4);
			}
			else
			{
				initContructor(this.size, true);
			}
		}

	}
	/**
	 * Contructor khoi tao mang game khac
	 * @param size kich thuoc cua game
	 * @param conPlay choi tiep hay khong
	 */
	public Core(int size,boolean conPlay) {
		initContructor(size, conPlay);
	}
	/**
	 * Ho tro khoi tao lop Core
	 * @param size kich thuoc cua game
	 * @param conPlay tiep tuc choi tiep hay khong
	 */
	public void initContructor(int size,boolean conPlay)
	{
		this.path="/2048_data/matrix"+size;
		mainBoard=new int[size][size];
		bufferBoard=new int[size][size];
		this.size=size;
		score=0;
		bufferScore=0;
		signInitValue=false;
		signBackup=true;
		endGame=false;
		time=System.currentTimeMillis();
		hiScoreMap=getHiScoreMapFromFile();
		if(conPlay)
			playContinue();
		else 
		{
			initvalue();
			initvalue();
		}
		initHiScore();
	}
	/**
	 * Khoi tao gia tri cho cac o, binh thuong gia tri la 2, sau moi 3,3 phut gia tri la 4
	 */
	public void initvalue()
	{
		Random r=new Random();
		int x=r.nextInt(size);
		int y=r.nextInt(size);
		if(mainBoard[x][y]==0)
		{
			if(System.currentTimeMillis()-time>200000)
			{
				mainBoard[x][y]=4;
				time=System.currentTimeMillis();
			}
			else
				mainBoard[x][y]=2;
			return;
		}
		else
			initvalue();
	}
	/**
	 * Load toan bo mang game da choi tu file.
	 * Khoi tao lai gia tri cho mainBoard va bufferBoard
	 * Khoi tao lai gia tri cho score va bufferScore
	 */
	public void playContinue()
	{
		LinkedList<String>list=FileIO.loadFile(path);
		for(int i=0;i<mainBoard.length;i++)
		{
			String[] arr=list.get(i).split("!");
			for(int j=0;j<mainBoard[i].length;j++)
			{		
				mainBoard[i][j]=Integer.parseInt(arr[j]);
				bufferBoard[i][j]=mainBoard[i][j];
			}
		}
		this.score=Integer.parseInt(list.getLast().trim());
		this.bufferScore=score;
	}
	/**
	 * Khoi tao diem cao. Diem cao duoc lay tu mot HashMap diem da duoc luu tru trong file hiScoreMap
	 */
	public void initHiScore()
	{
		if(hiScoreMap.isEmpty())
		{
			hiScore=0;
		}
		else
		{
			if(hiScoreMap.containsKey(size))
				hiScore=hiScoreMap.get(size);
			else
				hiScore=0;
		}
	}
	/**
	 * Lay diem cao tu file hiScoreMap.txt va chuyen thanh HashMap diem cao
	 * @return hashMapHiScore
	 */
	public HashMap<Integer, Integer> getHiScoreMapFromFile()
	{

		if(!FileIO.checkExistFile("/2048_data/hiScoreMap"))
		{
			return new HashMap<>();
		}
		LinkedList<String>lines=FileIO.loadFile("/2048_data/hiScoreMap");
		HashMap<Integer, Integer>hiScoreMap=new HashMap<>();
		for(String line:lines)
		{
			String[] arr=line.split("!");
			if(arr.length==2)
			{
				hiScoreMap.put(Integer.parseInt(arr[0]), Integer.parseInt(arr[1]));
			}
		}
		return hiScoreMap;
	}
	/**
	 * Luu kich thuoc cua mang game choi cuoi cung
	 * @param size kich thuoc cua mang game cuoi cung
	 * @return 
	 * true/false;
	 */
	public boolean saveLastOpen(int size)
	{
		LinkedList<String>lastOpen=new LinkedList<>();
		lastOpen.add(size+"");
		return FileIO.saveFile(lastOpen, "/2048_data/lastOpen");
	}
	/**
	 * Luu hiScoreMap xuong file. Neu hiScore>score: hiScore=score va cap nhat lai hiScoreMap sau do luu xuong file
	 * @return
	 * Neu luu thanh cong: True.
	 * Neu luu that bai: False
	 */
	public boolean saveHiScoreMap()
	{
		if(score>hiScore)
		{
			this.hiScore=score;
			hiScoreMap.put(size,hiScore);
			LinkedList<String>lines=new LinkedList<>();
			for(int key:hiScoreMap.keySet())
			{
				String line=key+"!"+hiScoreMap.get(key);
				lines.add(line);
			}
			return FileIO.saveFile(lines, "/2048_data/hiScoreMap");
		}
		return false;
	}
	/**
	 * Lay gia tri lon nhat trong mainBoard
	 * @return
	 * gia tri lon nhat trong mainBoard
	 */
	public int getMaxValue()
	{
		int max=mainBoard[0][0];
		for(int i=0;i<mainBoard.length;i++)
		{
			for(int j=0;j<mainBoard[i].length;j++)
			{
				max=Math.max(max, mainBoard[i][j]);
			}
		}
		return max;
	}
	/**
	 * Kiem tra ket thuc game.
	 * Neu khong co o nao co gia tri khac 0 va doi voi 1 o khong co cac o khac co gia tri giong voi chinh no thi ket thuc game
	 * @return
	 * Neu ket thuc game: True.
	 * Neu chua ket thuc: False.
	 */
	public boolean checkEndGame()
	{
		for(int row=0;row<mainBoard.length;row++)
		{
			for(int col=0;col<mainBoard[row].length;col++)
			{
				if(mainBoard[row][col]==0)
					return false;
				else if(checkSameNumAround(row, col))
				{
					return false;
				}
			}
		}
		return true;
	}
	/**
	 * Kiem tra voi 1 o co cac o lien ke co gia tri giong no hay khong
	 * @param row dong cua o 
	 * @param col cot cua o
	 * @return
	 * true/false
	 */
	private boolean checkSameNumAround(int row,int col)
	{
		for(int i=row-1;i<=row+1;i++)
		{
			if(i==row || i<0 || i>mainBoard.length-1);
			else if(mainBoard[row][col]==mainBoard[i][col])
				return true;
		}
		for(int i=col-1;i<=col+1;i++)
		{
			if(i==col || i<0 || i>mainBoard.length-1);
			else if(mainBoard[row][col]==mainBoard[row][i])
				return true;
		}
		return false;
	}
	/**
	 * Luu mang game hien tai xuong file. Gom co luu mainBoard va score
	 * @return 
	 * Neu luu thanh cong: True.
	 * Neu luu that bai: False.
	 */
	public boolean saveGame()
	{
		LinkedList<String>list=new LinkedList<>();
		for(int i=0;i<mainBoard.length;i++)
		{
			String line="";
			for(int j=0;j<mainBoard[i].length;j++)
			{
				line +=mainBoard[i][j]+"!";
			}
			list.add(line.substring(0,line.lastIndexOf('!')));
		}
		list.addLast(this.score+"");
		return FileIO.saveFile(list, path);
	}
	/**
	 * Gan gia tri cua mainBoard cho bufferBoard
	 */
	public void backupGame()
	{
		for(int i=0;i<size;i++)
		{
			for(int j=0;j<size;j++)
			{
				bufferBoard[i][j]=mainBoard[i][j];
			}
		}
		this.bufferScore=this.score;
	}
	/**
	 * Phuc vu cho Su kien Undo cua game.
	 * Gan gia tri cua bufferBoard cho mainBoard.
	 * Gan gia tri cua bufferScore cho score.
	 * Luu game xuong file
	 */
	public void undo()
	{
		for(int i=0;i<size;i++)
		{
			for(int j=0;j<size;j++)
			{
				mainBoard[i][j]=bufferBoard[i][j];
			}
		}
		this.score=this.bufferScore;
		saveGame();
	}
	/**
	 * Kiem tra trong mainBoard co o trong hay khong.
	 * @return
	 * true/false
	 */
	public boolean checkEmptyCell()
	{
		for(int i=0;i<mainBoard.length;i++)
		{
			for(int j=0;j<mainBoard[i].length;j++)
			{
				if(mainBoard[i][j]==0)
					return true;
			}
		}
		return false;
	}
	/**
	 * Bat su kien qua TRAI. Gom cac buoc:
	 * Gop cac o qua trai.
	 * Cong cac o lien ke tu trai qua phai.
	 * Gop cac o qua trai sau khi da cong.
	 * Sau khi gop neu con o trong va truoc lan gop dau tien neu tat ca cac o khong ben trai lien ke nhau thi khoi tao 1 gia tri moi va luu game. 
	 * Neu kiem tra ket thuc game bang True thi endGame=true va xoa file cua mang game hien tai
	 */
	public void turnLeft()
	{
		if(endGame)
			return;
		signInitValue=false;
		signBackup=true;
		moveFullCellsLeft();
		addTurnLeft();
		moveFullCellsLeft();
		saveHiScoreMap();
		if(signInitValue && checkEmptyCell())
		{
			initvalue();
			saveGame();
		}
		if(checkEndGame())
		{
			endGame=true;
			FileIO.delFile(path);
		}
	}

	/**
	 *Di chuyen cac o co so qua trai. Duyet tung dong, trong moi dong neu co 1 o bang 0 thi thuc hien mergeRecursionLeft
	 */
	private void moveFullCellsLeft()
	{
		for(int row=0;row<size;row++)
		{
			for(int col=0;col<size;col++)
			{
				if(mainBoard[row][col]==0)
				{
					moveFullCellsInRowLeft(row, col, col+1);
					break;
				}
			}
		}
	}
	/**
	 * Di chuyen cac o co so trong cung dong sang trai. Tu o bang 0 duyet tiep cac o khac trong cung dong, neu co 1 o khac 0 thi:
	 *  thuc hien backupGame(), gan signBackup=false, signInitValue=true, hoan doi 2 gia tri cua 2 o cho nhau
	 *  va tiep tuc thuc hien doi voi o emptyColCell+1 va o fullColCell+1.
	 *  Neu khong co o nao khac 0 thi thuc hien doi voi o emptyColCell va o fullColCell+1  
	 * @param row dong cua o
	 * @param emptyColCell cot cua o co gia tri bang 0
	 * @param fullColCell cot cua o co gia tri khac 0
	 */
	private void moveFullCellsInRowLeft(int row,int emptyColCell,int fullColCell)
	{
		if(fullColCell==size)
			return;
		else
		{
			if(mainBoard[row][fullColCell]!=0)
			{
				if(signBackup)
				{
					backupGame();
					signBackup=false;
				}
				mainBoard[row][emptyColCell]=mainBoard[row][fullColCell];
				mainBoard[row][fullColCell]=0;
				signInitValue=true;
				moveFullCellsInRowLeft(row, emptyColCell+1, fullColCell+1);
			}
			else
			{
				moveFullCellsInRowLeft(row, emptyColCell, fullColCell+1);
			}
		}
	}
	/**
	 * Cong 2 o co gia tri giong nhau sang trai.
	 * Duyet tren tung dong tu trai qua phai, neu o va o+1 co gia tri giong nhau thi gia tri o=o*2 va gia tri o+1=0, score=score+gia tri o sang khi nhan 2.
	 */
	private void addTurnLeft()
	{
		for(int row=0;row<size;row++)
		{
			for(int col=0;col<size-1;)
			{
				if(mainBoard[row][col]==mainBoard[row][col+1] && mainBoard[row][col]!=0)
				{
					if(signBackup)
					{
						backupGame();
						signBackup=false;
					}
					mainBoard[row][col]=mainBoard[row][col]*2;
					score +=mainBoard[row][col];
					signInitValue=true;
					mainBoard[row][col+1]=0;
					col=col+2;
				}
				else
					col++;
			}
		}
	}

	/**
	 * Bat su kien qua PHAI. Gom cac buoc:
	 * Gop cac o qua phai.
	 * Cong cac o lien ke tu phai qua trai.
	 * Gop cac o qua phai sau khi da cong.
	 * Sau khi gop neu con o trong va truoc lan gop dau tien neu tat ca cac o khong ben phai lien ke nhau thi khoi tao 1 gia tri moi va luu game. 
	 * Neu kiem tra ket thuc game bang True thi endGame=true va xoa file cua mang game hien tai
	 */
	public void turnRight()
	{
		if(endGame)
			return;
		signInitValue=false;
		signBackup=true;
		moveFullCellsRight();
		addTurnRight();
		moveFullCellsRight();
		saveHiScoreMap();
		if(signInitValue && checkEmptyCell())
		{
			initvalue();
			saveGame();
		}
		if(checkEndGame())
		{
			endGame=true;
			FileIO.delFile(path);
		}

	}
	/**
	 * Di chuyen cac o co so qua phai. Duyet tung dong, trong moi dong neu co 1 o bang 0 thi thuc hien mergeRecursionRight
	 */
	private void moveFullCellsRight()
	{
		for(int row=0;row<size;row++)
		{
			for(int col=size-1;col>0;col--)
			{
				if(mainBoard[row][col]==0)
				{
					moveFullCellsInRowRight(row, col, col-1);
					break;
				}
			}
		}
	}
	/**
	 * Di chuyen cac o co so trong cung dong sang phai. Tu o bang 0 duyet tiep cac o khac trong cung dong, neu co 1 o khac 0 thi:
	 *  thuc hien backupGame(), gan signBackup=false, signInitValue=true, hoan doi 2 gia tri cua 2 o cho nhau
	 *  va tiep tuc thuc hien doi voi o emptyColCell+1 va o fullColCell+1.
	 *  Neu khong co o nao khac 0 thi thuc hien doi voi o emptyColCell va o fullColCell+1  
	 * @param row dong cua o
	 * @param emptyColCell cot cua o co gia tri bang 0
	 * @param fullColCell cot cua o co gia tri khac 0
	 */
	private void moveFullCellsInRowRight(int row,int emptyColCell,int fullColCell)
	{
		if(fullColCell==-1)
			return;
		else
		{
			if(mainBoard[row][fullColCell]!=0)
			{
				if(signBackup)
				{
					backupGame();
					signBackup=false;
				}
				mainBoard[row][emptyColCell]=mainBoard[row][fullColCell];
				mainBoard[row][fullColCell]=0;
				signInitValue=true;
				moveFullCellsInRowRight(row, emptyColCell-1, fullColCell-1);
			}
			else
			{
				moveFullCellsInRowRight(row, emptyColCell, fullColCell-1);
			}
		}
	}
	/**
	 * Cong 2 o co gia tri giong nhau sang phai.
	 * Duyet tren tung dong tu phai qua trai, neu o va o-1 co gia tri giong nhau thi gia tri o=o*2 va gia tri o-1=0, score=score+gia tri o sang khi nhan 2.
	 */
	private void addTurnRight()
	{
		for(int row=0;row<size;row++)
		{
			for(int col=size-1;col>0;)
			{
				if(mainBoard[row][col]==mainBoard[row][col-1] && mainBoard[row][col]!=0)
				{
					if(signBackup)
					{
						backupGame();
						signBackup=false;
					}
					mainBoard[row][col]=mainBoard[row][col]*2;
					score +=mainBoard[row][col];
					signInitValue=true;
					mainBoard[row][col-1]=0;
					col=col-2;
				}
				else
					col--;
			}
		}
	}
	/**
	 * Bat su kien len TREN. Gom cac buoc:
	 * Gop cac o len tren.
	 * Cong cac o lien ke tu tren xuong.
	 * Gop cac o len tren sau khi da cong.
	 * Sau khi gop neu con o trong va truoc lan gop dau tien neu tat ca cac o khong phia tren lien ke nhau thi khoi tao 1 gia tri moi va luu game. 
	 * Neu kiem tra ket thuc game bang True thi endGame=true va xoa file cua mang game hien tai
	 */
	public void turnUp()
	{
		if(endGame)
			return;
		signInitValue=false;
		signBackup=true;
		moveFullCellsUp();
		addTurnUp();
		moveFullCellsUp();
		saveHiScoreMap();
		if(signInitValue && checkEmptyCell())
		{
			initvalue();
			saveGame();
		}
		if(checkEndGame())
		{
			endGame=true;
			FileIO.delFile(path);
		}
	}
	/**
	 * Di chuyen cac o co so len tren. Duyet tung cot, trong moi cot neu co 1 o bang 0 thi thuc hien mergeRecursionUp
	 */
	private void moveFullCellsUp()
	{
		for(int col=0;col<size;col++)
		{
			for(int row=0;row<size;row++)
			{
				if(mainBoard[row][col]==0)
				{
					moveFullCellsInColUp(col, row, row+1);
					break;
				}
			}
		}
	}
	/**
	 * Di chuyen cac o co so trong cung cot len tren. Tu o bang 0 duyet tiep cac o khac trong cung cot, neu co 1 o khac 0 thi:
	 *  thuc hien backupGame(), gan signBackup=false, signInitValue=true, hoan doi 2 gia tri cua 2 o cho nhau
	 *  va tiep tuc thuc hien doi voi o emptyRowCell+1 va o fullRowCell+1.
	 *  Neu khong co o nao khac 0 thi thuc hien doi voi o emptyRowCell va o fullRowCell+1  
	 * @param col cot cua o
	 * @param emptyRowCell dong cua o co gia tri bang 0
	 * @param fullRowCell dong cua o co gia tri khac 0
	 */
	private void moveFullCellsInColUp(int col,int emptyRowCell,int fullRowCell)
	{
		if(fullRowCell==size)
			return;
		else
		{
			if(mainBoard[fullRowCell][col]!=0)
			{
				if(signBackup)
				{
					backupGame();
					signBackup=false;
				}
				mainBoard[emptyRowCell][col]=mainBoard[fullRowCell][col];
				mainBoard[fullRowCell][col]=0;
				signInitValue=true;
				moveFullCellsInColUp(col, emptyRowCell+1, fullRowCell+1);
			}
			else
			{
				moveFullCellsInColUp(col, emptyRowCell, fullRowCell+1);
			}
		}
	}
	/**
	 * Cong 2 o co gia tri giong nhau tren xuong.
	 * Duyet tren tung cot tu tren qua cuong, neu o va o+1 co gia tri giong nhau thi gia tri o=o*2 va gia tri o+1=0, score=score+gia tri o sang khi nhan 2.
	 */
	private void addTurnUp()
	{
		for(int col=0;col<size;col++)
		{
			for(int row=0;row<size-1;)
			{
				if(mainBoard[row][col]==mainBoard[row+1][col] && mainBoard[row][col]!=0)
				{
					if(signBackup)
					{
						backupGame();
						signBackup=false;
					}
					mainBoard[row][col]=mainBoard[row][col]*2;
					score +=mainBoard[row][col];
					signInitValue=true;
					mainBoard[row+1][col]=0;
					row=row+2;
				}
				else
					row++;
			}
		}
	}
	/**
	 * Bat su kien XUONG. Gom cac buoc:
	 * Gop cac o xuong.
	 * Cong cac o lien ke tu duoi len.
	 * Gop cac o duoi len sau khi da cong.
	 * Sau khi gop neu con o trong va truoc lan gop dau tien neu tat ca cac o khong phia duoi lien ke nhau thi khoi tao 1 gia tri moi va luu game. 
	 * Neu kiem tra ket thuc game bang True thi endGame=true va xoa file cua mang game hien tai
	 */
	public void turnDown()
	{
		if(endGame)
			return;
		signInitValue=false;
		signBackup=true;
		moveFullCellsDown();
		addTurnDown();
		moveFullCellsDown();
		saveHiScoreMap();
		if(signInitValue && checkEmptyCell())
		{
			initvalue();
			saveGame();
		}
		if(checkEndGame())
		{
			endGame=true;
			FileIO.delFile(path);
		}
	}
	/**
	 * Di chuyen cac o co so xuong. Duyet tung cot, trong moi cot neu co 1 o bang 0 thi thuc hien mergeRecursionDown
	 */
	private void moveFullCellsDown()
	{
		for(int col=0;col<size;col++)
		{
			for(int row=size-1;row>0;row--)
			{
				if(mainBoard[row][col]==0)
				{
					moveFullCellsInColDown(col, row, row-1);
					break;
				}
			}
		}
	}
	/**
	 * Di chuyen cac o co so tren cung cot xuong duoi. Tu o bang 0 duyet tiep cac o khac trong cung cot, neu co 1 o khac 0 thi:
	 *  thuc hien backupGame(), gan signBackup=false, signInitValue=true, hoan doi 2 gia tri cua 2 o cho nhau
	 *  va tiep tuc thuc hien doi voi o emptyRowCell+1 va o fullRowCell+1.
	 *  Neu khong co o nao khac 0 thi thuc hien doi voi o emptyRowCell va o fullRowCell+1  
	 * @param col cot cua o
	 * @param emptyRowCell dong cua o co gia tri bang 0
	 * @param fullRowCell dong cua o co gia tri khac 0
	 */
	private void moveFullCellsInColDown(int col,int emptyRowCell,int fullRowCell)
	{
		if(fullRowCell==-1)
			return;
		else
		{
			if(mainBoard[fullRowCell][col]!=0)
			{
				if(signBackup)
				{
					backupGame();
					signBackup=false;
				}
				mainBoard[emptyRowCell][col]=mainBoard[fullRowCell][col];
				mainBoard[fullRowCell][col]=0;
				signInitValue=true;
				moveFullCellsInColDown(col, emptyRowCell-1, fullRowCell-1);
			}
			else
			{
				moveFullCellsInColDown(col, emptyRowCell, fullRowCell-1);
			}
		}
	}
	/**
	 * Cong 2 o co gia tri giong nhau duoi len.
	 * Duyet tren tung cot tu duoi len, neu o va o-1 co gia tri giong nhau thi gia tri o=o*2 va gia tri o-1=0, score=score+gia tri o sang khi nhan 2.
	 */
	private void addTurnDown()
	{
		for(int col=0;col<size;col++)
		{
			for(int row=size-1;row>0;)
			{
				if(mainBoard[row][col]==mainBoard[row-1][col] && mainBoard[row][col]!=0)
				{
					if(signBackup)
					{
						backupGame();
						signBackup=false;
					}
					mainBoard[row][col]=mainBoard[row][col]*2;
					score +=mainBoard[row][col];
					signInitValue=true;
					mainBoard[row-1][col]=0;
					row=row-2;
				}
				else
					row--;
			}
		}
	}
	/**
	 * Lay gia tri cua endGame
	 * @return endGame
	 */
	public boolean getEndGame()
	{
		return endGame;
	}
	/**
	 * 
	 * @param end gia tri endGame moi
	 */
	public void setEndGame(boolean end)
	{
		this.endGame=end;
	}
	/**
	 * Lay gia tri cua hiScore
	 * @return hiScore
	 */
	public int getHiScore()
	{
		return this.hiScore;
	}
	/**
	 * Lay gia tri cua score
	 * @return score
	 */
	public int getScore()
	{
		return this.score;
	}
	/**
	 * Lay gia tri cua mainBoard
	 * @return the mainBoard
	 */
	public int[][] getMainBoard() {
		return mainBoard;
	}
	/**
	 * Lay gia tri cua bufferBoard
	 * @return the bufferBoard
	 */
	public int[][] getBufferBoard() {
		return bufferBoard;
	}
	/**
	 * Lay gia tri cua size
	 * @return size
	 */
	public int getSize()
	{
		return size;
	}

}
