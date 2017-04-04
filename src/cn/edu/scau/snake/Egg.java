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
	
	private static Random r = new Random();//用Random产生随机数，有一个就够了，因此定义其为静态的
	
	private boolean beEated = false;
	
	private static Toolkit tk = Toolkit.getDefaultToolkit();
	private static Image flame = tk.getImage(Egg.class.getClassLoader().
								getResource("cn/edu/scau/snake/images/flame.gif"));



	public Egg(int row, int col) {
		this.row = row;
		this.col = col;
	}
	

	public Egg() {
		//随机产生位置与尺寸大小
		this(r.nextInt(Yard.ROWS-5)+3, r.nextInt(Yard.COLS-5)+3);
		this.h = this.w = r.nextInt(Yard.GRID + 3) + 7;
	}
	
	/**
	 * 蛋被吃掉后重新出现
	 */
	public void reAppear() {
		this.row = r.nextInt(Yard.ROWS - 8) + 4;
		this.col = r.nextInt(Yard.COLS - 8) + 4;
		this.h = this.w = r.nextInt(Yard.GRID + 3) + 7;
	}

	//用来做碰撞检测的辅助方法
	public Rectangle getRect() {
		return new Rectangle(Yard.GRID * col, Yard.GRID * row, w, h);
	}

	/**
	 * 
	 * 画鸡蛋.......（不小心想起了小学课文里的达芬奇.......
	 */
	public void draw(Graphics g) {
		Color c = g.getColor();
		g.setColor(color);
		g.fillOval(Yard.GRID * col, Yard.GRID * row, w, h);
		g.setColor(c);
//		g.drawImage(egg,Yard.GRID * col, Yard.GRID * row,w,h,null);找不到适合的鸡蛋素材
		g.drawImage(flame,Yard.GRID * col, Yard.GRID * row,w,h,null);//附加火焰buff
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
