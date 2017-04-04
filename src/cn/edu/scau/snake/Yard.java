package cn.edu.scau.snake;
import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JOptionPane;

/**
 * 
 * 此类模拟贪吃蛇所在的杂草丛生的院子
 * 负责游戏画面的绘制、键盘监听及游戏的开始和结束等功能
 * main方法被定义在此类中
 * 
 *
 */
public class Yard extends Frame {

	
	public static final int ROWS = 30;//行数
	public static final int COLS =50;//列数
	public static final int GRID = 20;//每个格子的大小
	public int gameSpeed = defultGameSpeed;//游戏速度，越小越快
	public static int defultGameSpeed;//初始速度
	public int score=0;
	public int highestScore = 0;
	public boolean gameOver=false;
	
	private static Toolkit tk = null;
	private static Image grassland = null;
	
	//静态代码块，在类加载时便先加载图片
	static{
		 tk = Toolkit.getDefaultToolkit();
		grassland = tk.getImage(Yard.class.getClassLoader().getResource("cn/edu/scau/snake/images/grassland.jpg"));//好像是异步IO的
		 //获取配置文件里的设置
		defultGameSpeed = Integer.parseInt(PropertyManager.getInstance().getProperties("gameSpeed"));
	}
	
	
	
	PaintThread paintThread=new PaintThread();//负责绘制游戏画面的线程
	GameManager gm = new GameManager(this);//负责游戏的一些操作
	
	private Snake snake = new Snake(this);//院子一被实例化就有一只蛇
	private Egg egg= new Egg();//院子一被实例化就有一个蛋
	
	public List<Egg> eggs = new ArrayList<Egg>();
	
	private static Random r = new Random();
	private Color fontColor = Color.BLUE;
	
	
	Image offScreenImage = null;//用于双缓冲，解决闪烁现象

	/**
	 * 重写update方法，加入双缓冲，解决闪烁现象
	 */
	public void update(Graphics g) {
		if(offScreenImage==null){
			offScreenImage = this.createImage(COLS*GRID,ROWS*GRID);//创建虚拟图片
		}
		Graphics gOff=offScreenImage.getGraphics();
		paint(gOff);//先把东西画在虚拟图片上
		g.drawImage(offScreenImage, 0, 0, null);//再一次显示出来
	}
	
	/**
	 * 用于发射窗体
	 */
	public void launch() {
		this.setTitle("GreedySnake");
		this.setLocation(300, 100);
		this.setSize(COLS * GRID, ROWS * GRID);
		this.setVisible(true);
		this.setResizable(false);
		this.addWindowListener(new WindowAdapter() {//监听window关闭事件
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		
		gm.loadScore();//读取最高分
		new Thread(paintThread).start();//启动绘制画面线程
		this.addKeyListener(new KeyMonitor());//增加键盘监听
	}
	
	/*改变游戏状态~end*/
	public void gameOver(){
		gm.saveScore();
		gameOver=true;
	}
	
	/**
	 * 
	 * 用于询问游戏是否继续
	 */
	public boolean isRestart(){
		int choice = JOptionPane.showConfirmDialog(this, "again?","game over!!!!",JOptionPane.YES_NO_OPTION);
		if(choice==0){
			return true;
		}
		else
		if(choice==1){
			System.exit(0);
			return false;
		}
		else{
			System.out.println("i don't know!");
			return false;
		}
	}
	
	/**
	 * 用于重新开始游戏
	 */
	public void restart(){
		this.snake=new Snake(this);
		this.paintThread=new PaintThread();
		this.eggs.clear();
		gameOver = false;
		score = 0;
		gameSpeed = defultGameSpeed;
	}
	
	/*绘制frame*/
	public void paint(Graphics g) {
		Color c = g.getColor();
//		g.setColor(Color.GRAY);
//		g.fillRect(0, 0, COLS * GRID, ROWS * GRID);
		g.drawImage(grassland,0,0,COLS * GRID, ROWS * GRID,this);
		displayLevel(g);
		displaySpeed(g);
		displayHighestLevel(g);
		displaySkillTips(g);
		g.setColor(Color.BLACK);
		g.setColor(c);
		snake.draw(g);
		egg.draw(g);
		drawEggs(g);
		snake.eat(egg);
		snake.eat(eggs);
		if(gameOver){
			g.setFont(new Font("微软雅黑",Font.BOLD | Font.ITALIC,99));
			g.setColor(Color.RED);
			g.drawString("Game Over!!!", 244, 300);
			paintThread.gameOver();//调用paintThread的gameOver方法，使该线程停止
		}
	}
	
	//绘制一大堆蛋
	private void drawEggs(Graphics g){
		for(int i=0; i<eggs.size(); i++){
			Egg e = eggs.get(i);
			if(!e.isBeEated()){
				e.draw(g);
			}
			else{
				eggs.remove(e);
			}
		}
	}
	//特技！
	public void displayLevel(Graphics g){
		g.setColor(new Color(5+(snake.getSize()-1)*10000));
		g.setFont(new Font("Cambria Math",Font.BOLD | Font.ITALIC,50));
		g.drawString("Level:"+score+"!!!",200, 70);
	}
	//特技！！
	public void displaySpeed(Graphics g){
		g.setColor(Color.ORANGE);
		g.setFont(new Font("Cambria Math",Font.BOLD | Font.ITALIC,20));
		g.drawString("GreedyLevel:",700, 70);
		g.setColor(Color.RED);
		int t=(100-gameSpeed)/10;
		for(int i=1;i<=t;i++){
			g.fillOval(827+i*8, 59, i*3+4, i*3+4);
		}
	}
	//特技！！
	public void displayHighestLevel(Graphics g){
		if(highestScore < score) highestScore = score;
		Color c = g.getColor();
		g.setColor(Color.RED);
		g.setFont(new Font("Cambria Math",Font.BOLD | Font.ITALIC,15));
		g.drawString("HighestLevel:"+highestScore,200, 90);
		g.setColor(c);
	}
	//特技！！！（都是些无聊的玩意儿........
	public void displaySkillTips(Graphics g){
		Color c = g.getColor();
		g.setFont(new Font("楷体",Font.BOLD | Font.ITALIC,19));
		g.setColor(Color.BLACK);
		g.drawString("press", 685, 550);
		g.drawString("  to rush!!!", 790, 550);
		g.drawString("press", 700, 569);
		g.drawString("  to smell!!(one-off)", 762, 569);
		g.setColor(fontColor);
		g.drawString("SPACE", 750, 550);
		if(!snake.isHasSmelled()){
			g.drawString("S", 765, 569);
		}
		else{
			g.setColor(Color.BLACK);
			g.drawString("S", 765, 569);
		}
		if(fontColor==Color.BLUE){
			fontColor = Color.RED;
		}
		else {
			fontColor = Color.BLUE;
		}
		
	}
	
	
	public static void main(String[] args) {
		new Yard().launch();
	}
	
	//定义负责绘制游戏画面的线程
	class PaintThread implements Runnable{
		private boolean running=true;
		public void run(){
			while(running){
				repaint();
				try{
					Thread.sleep(gameSpeed);
				}
				catch(Exception e){
					e.printStackTrace();
				}
			}
			if(isRestart()){
				restart();
				new Thread(paintThread).start();
			}
		}
		private void gameOver(){
			running=false;
		}
	}
	//定义键盘监听类，重写press和release事件的方法
	private class KeyMonitor extends KeyAdapter{

		public void keyReleased(KeyEvent e) {
			snake.keyReleased(e);
		}

		public void keyPressed(KeyEvent e) {
			snake.keyPressed(e);
		}
		
	}
}

