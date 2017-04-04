package cn.edu.scau.snake;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.util.Random;

public class Egg {
	int row, col;
	int w, h;
	Color color =Color.PINK;
	
	private static Random r = new Random();//��Random�������������һ���͹��ˣ���˶�����Ϊ��̬��
	
	private boolean beEated = false;
	
	private static Toolkit tk = Toolkit.getDefaultToolkit();
	private static Image flame = tk.getImage(Egg.class.getClassLoader().
								getResource("cn/edu/scau/snake/images/flame.gif"));



	public Egg(int row, int col) {
		this.row = row;
		this.col = col;
	}
	

	public Egg() {
		//�������λ����ߴ��С
		this(r.nextInt(Yard.ROWS-5)+3, r.nextInt(Yard.COLS-5)+3);
		this.h = this.w = r.nextInt(Yard.GRID + 3) + 7;
	}
	
	/**
	 * �����Ե������³���
	 */
	public void reAppear() {
		this.row = r.nextInt(Yard.ROWS - 8) + 4;
		this.col = r.nextInt(Yard.COLS - 8) + 4;
		this.h = this.w = r.nextInt(Yard.GRID + 3) + 7;
	}

	//��������ײ���ĸ�������
	public Rectangle getRect() {
		return new Rectangle(Yard.GRID * col, Yard.GRID * row, w, h);
	}

	/**
	 * 
	 * ������.......����С��������Сѧ������Ĵ����.......
	 */
	public void draw(Graphics g) {
		Color c = g.getColor();
		g.setColor(color);
		g.fillOval(Yard.GRID * col, Yard.GRID * row, w, h);
		g.setColor(c);
//		g.drawImage(egg,Yard.GRID * col, Yard.GRID * row,w,h,null);�Ҳ����ʺϵļ����ز�
		g.drawImage(flame,Yard.GRID * col, Yard.GRID * row,w,h,null);//���ӻ���buff
		if (color == Color.PINK)
			color = Color.RED;
		else if (color == Color.RED)
			color =Color.YELLOW;
		else
			color=Color.PINK;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getCol() {
		return col;
	}

	public void setCol(int col) {
		this.col = col;
	}
	
	public boolean isBeEated() {
		return beEated;
	}

	public void setBeEated(boolean beEated) {
		this.beEated = beEated;
	}

}
