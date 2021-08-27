package view;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
/**
 * Lay cac thong so cua chuoi duoc ve
 * @author Trinh Quang Tien
 *
 */
public class StringMetricGUI {
	private Font font;
	private FontRenderContext frc;
	public StringMetricGUI(Graphics2D g2d) {
		font=g2d.getFont();
		frc=g2d.getFontRenderContext();
	}
	/**
	 * Lay gioi han cua chuoi
	 * @param message chuoi can lay gioi han
	 * @return rectangle2D
	 */
	public Rectangle2D getBound(String message)
	{
		return font.getStringBounds(message, frc);
	}
	/**
	 * Lay chieu rong cua chuoi duoc ve
	 * @param message chuoi can lay chuoi rong
	 * @return chieu rong
	 */
	public double getWith(String message)
	{
		return getBound(message).getWidth();
	}
	/**
	 * Lay chieu cao cua chuoi duoc ve
	 * @param message chuoi can lay chieu cao
	 * @return chieu cao
	 */
	public double getHeight(String message)
	{
		return getBound(message).getHeight();
	}

}
