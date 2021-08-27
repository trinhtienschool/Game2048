package model;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.Random;
/**
 * Thuc hien cac action lien quan den nhap/xuat file
 * @author Trinh Quang Tien
 *
 */
public class FileIO {
	/**
	 * Load file da luu voi path va loai bo chuoi ma hoa
	 * Thay doi duong dan bang duong dan moi bi mat loai dinh dang file
	 * Giai ma list tra ve
	 * @param path duong dan file
	 * @return linkedList
	 */
	public static LinkedList<String> loadFile(String path)
	{
		path=getCurrentPath()+path;
		LinkedList<String>list=new LinkedList<>();
		try
		{
			FileReader fr=new FileReader(path);
			BufferedReader br=new BufferedReader(fr);
			try
			{
				String line=br.readLine();
				while(line!=null)
				{
					list.add(line);
					line=br.readLine();
				}
				br.close();
				fr.close();
			} 
			catch (IOException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		list=decryptList(list);
		return list;
	}
	/**
	 * Luu list va chuoi ma hoa thanh file voi duong dan.
	 * Ma hoa list truoc khi luu
	 * Thay doi duong dan bang duong dan moi bi mat loai dinh dang file
	 * @param path duong dan de luu
	 * @param list danh sach can luu
	 * @return true/false;
	 */
	public static boolean saveFile(LinkedList<String>list,String path)
	{
		path=getCurrentPath()+path;
		list=encryptList(list);
		if(list.isEmpty())
			return true;
		try
		{
			FileWriter fw=new FileWriter(path);
			BufferedWriter bw=new BufferedWriter(fw);
			for(String line:list)
			{
				bw.write(line);
				bw.newLine();
			}
			bw.close();
			fw.close();
			return true;
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;

	}
	/**
	 * Giai ma list.
	 * Cach hoat dong:
	 * Lay phan tu cuoi cung trong list;
	 * chay i tu 1 den 9 neu giai ma phan tu cuoi cung trong list giong voi chuoi giai ma thi luu lai gia tri voi key
	 * Loai bo chuoi ma hoa khoi danh sach
	 * Thuc hien giai ma tung phan tu cua list voi key bang phuong thuc decrypt
	 * @param list danh sach can giai ma
	 * @return list da gia ma
	 */
	private static LinkedList<String> decryptList(LinkedList<String>list)
	{
		int key=0;
		if(list.isEmpty())
			return list;
		String keyString=list.getLast();
		for(int i=1;i<10;i++)
		{
			if(decrypt(keyString, i).equals("G#20&gnAuq/neIT(hniR%+mE48"))  
			{
				key=i;
				break;
			}
		}
		list.removeLast();
		for(int i=0;i<list.size();i++)
		{
			list.set(i, decrypt(list.get(i), key));
		}
		return list;
	}
	/**
	 * Chuyen list thanh list da ma hoa va them chuoi da ma hoa vao list
	 * Cach hoat dong:
	 * 	Ramdom key;
	 * 	thuc hien ma hoa voi key va tung phan tu cua list bang phuong thuc encrypt
	 * @param list danh sach can ma hoa
	 * @return list sau khi ma hoa
	 */
	private static LinkedList<String> encryptList(LinkedList<String>list)
	{
		Random r=new Random();
		int key=1+r.nextInt(10);
		list.addLast("G#20&gnAuq/neIT(hniR%+mE48");
		for(int i=0;i<list.size();i++)
		{
			list.set(i, encrypt(list.get(i), key));
		}
		return list;
	}
	/**
	 * Ma hoa chuoi input voi key
	 * Su dung gia thuat Caesar cipher
	 * @param input chuoi can ma hoa
	 * @param key key ma hoa
	 * @return chuoi da ma hoa
	 */
	private static String encrypt(String input,int key)
	{
		char[] msg=input.toCharArray();
		for(int i=0;i<msg.length;i++)
		{
			if(Character.isUpperCase(msg[i]))
				msg[i]=(char)('A'+(msg[i]+key-'A')%26);	
			else if(Character.isLowerCase(msg[i]))
				msg[i]=(char)('a'+(msg[i]+key-'a')%26);
			else if(Character.isDigit(msg[i]))
				msg[i]=(char)('0'+(msg[i]+key-'0')%10);
			else if((int)(msg[i])>=33 && (int)(msg[i])<=47)
			{
				msg[i]=(char)(33+(msg[i]+key-33)%15);
			}
		}
		return new String(msg);
	}
	/**
	 * Giai ma chuoi input voi key
	 * Su dung gia thuat Caesar cipher
	 * @param input chuoi can giai ma
	 * @param key key giai ma
	 * @return chuoi da gia ma
	 */
	private static String decrypt(String input,int key)
	{
		char[] msg=input.toCharArray();
		for(int i=0;i<msg.length;i++)
		{
			if(Character.isUpperCase(msg[i]))
				msg[i]=(char)('A'+(msg[i]-key+26-'A')%26);
			else if(Character.isLowerCase(msg[i]))
				msg[i]=(char)('a'+(msg[i]-key+26-'a')%26);
			else if(Character.isDigit(msg[i]))
				msg[i]=(char)('0'+(msg[i]-key+10-'0')%10);
			else if((int)(msg[i])>=33 && (int)(msg[i])<=47)
			{
				msg[i]=(char)(33+(msg[i]-key+15-33)%15);
			}	
		}
		return new String(msg);
	}
	/**
	 * Xoa du lieu cua file voi duong dan path
	 * @param path duong dan den file can xoa
	 * @return true/false;
	 */
	public static boolean delFile(String path)
	{
		path=getCurrentPath()+path;
		File file=new File(path);
		return file.delete();
	}
	/**
	 * Kiem tra file voi duong dan path co rong hay khong
	 * @param path duong dan den file can kiem tra
	 * @return true/false;
	 */
	public static boolean checkExistFile(String path)
	{
		path=getCurrentPath()+path;
		File file=new File(path);
		if(file.exists())
			return true;
		return false;
	}
	/**
	 * Lay dia chi tuyet doi cua ung dung o thoi diem hien tai
	 * @return path
	 */
	public static String getCurrentPath()
	{
		//Cach 1:
			return System.getProperty("user.dir");
		//Cach 2:
			//Path currentRelativePath = Paths.get("");
			//String s = currentRelativePath.toAbsolutePath().toString();
	}
	/**
	 * Khoi tao folder data cho lan dau tien chay
	 */
	public static void initDataFolder()
	{
		File folder=new File(getCurrentPath()+"/2048_data");
		if(!folder.exists())
		{
			folder.mkdir();
		}
	}
}
