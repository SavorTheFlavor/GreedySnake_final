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
 * ����ģ��̰�������ڵ��Ӳݴ�����Ժ��
 * ������Ϸ����Ļ��ơ����̼�������Ϸ�Ŀ�ʼ�ͽ����ȹ���
 * main�����������ڴ�����
 * 
 *
 */
public class Yard extends Frame {

	
	public static final int ROWS = 30;//����
	public static final int COLS =50;//����
	public static final int GRID = 20;//ÿ�����ӵĴ�С
	public int gameSpeed = defultGameSpeed;//��Ϸ�ٶȣ�ԽСԽ��
	public static int defultGameSpeed;//��ʼ�ٶ�
	public int score=0;
	public int highestScore = 0;
	public boolean gameOver=false;
	
	private static Toolkit tk = null;
	private static Image grassland = null;
	
	//��̬����飬�������ʱ���ȼ���ͼƬ
	static{
		 tk = Toolkit.getDefaultToolkit();
		grassland = tk.getImage(Yard.class.getClassLoader().getResource("cn/edu/scau/snake/images/grassland.jpg"));//�������첽IO��
		 //��ȡ�����ļ��������
		defultGameSpeed = Integer.parseInt(PropertyManager.getInstance().getProperties("gameSpeed"));
	}
	
	
	
	PaintThread paintThread=new PaintThread();//���������Ϸ������߳�
	GameManager gm = new GameManager(this);//������Ϸ��һЩ����
	
	private Snake snake = new Snake(this);//Ժ��һ��ʵ��������һֻ��
	private Egg egg= new Egg();//Ժ��һ��ʵ��������һ����
	
	public List<Egg> eggs = new ArrayList<Egg>();
	
	private static Random r = new Random();
	private Color fontColor = Color.BLUE;
	
	
	Image offScreenImage = null;//����˫���壬�����˸����

	/**
	 * ��дupdate����������˫���壬�����˸����
	 */
	public void update(Graphics g) {
		if(offScreenImage==null){
			offScreenImage = this.createImage(COLS*GRID,ROWS*GRID);//��������ͼƬ
		}
		Graphics gOff=offScreenImage.getGraphics();
		paint(gOff);//�ȰѶ�����������ͼƬ��
		g.drawImage(offScreenImage, 0, 0, null);//��һ����ʾ����
	}
	
	/**
	 * ���ڷ��䴰��
	 */
	public void launch() {
		this.setTitle("GreedySnake");
		this.setLocation(300, 100);
		this.setSize(COLS * GRID, ROWS * GRID);
		this.setVisible(true);
		this.setResizable(false);
		this.addWindowListener(new WindowAdapter() {//����window�ر��¼�
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		
		gm.loadScore();//��ȡ��߷�
		new Thread(paintThread).start();//�������ƻ����߳�
		this.addKeyListener(new KeyMonitor());//���Ӽ��̼���
	}
	
	/*�ı���Ϸ״̬~end*/
	public void gameOver(){
		gm.saveScore();
		gameOver=true;
	}
	
	/**
	 * 
	 * ����ѯ����Ϸ�Ƿ����
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
	 * �������¿�ʼ��Ϸ
	 */
	public void restart(){
		this.snake=new Snake(this);
		this.paintThread=new PaintThread();
		this.eggs.clear();
		gameOver = false;
		score = 0;
		gameSpeed = defultGameSpeed;
	}
	
	/*����frame*/
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
			g.setFont(new Font("΢���ź�",Font.BOLD | Font.ITALIC,99));
			g.setColor(Color.RED);
			g.drawString("Game Over!!!", 244, 300);
			paintThread.gameOver();//����paintThread��gameOver������ʹ���߳�ֹͣ
		}
	}
	
	//����һ��ѵ�
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
	//�ؼ���
	public void displayLevel(Graphics g){
		g.setColor(new Color(5+(snake.getSize()-1)*10000));
		g.setFont(new Font("Cambria Math",Font.BOLD | Font.ITALIC,50));
		g.drawString("Level:"+score+"!!!",200, 70);
	}
	//�ؼ�����
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
	//�ؼ�����
	public void displayHighestLevel(Graphics g){
		if(highestScore < score) highestScore = score;
		Color c = g.getColor();
		g.setColor(Color.RED);
		g.setFont(new Font("Cambria Math",Font.BOLD | Font.ITALIC,15));
		g.drawString("HighestLevel:"+highestScore,200, 90);
		g.setColor(c);
	}
	//�ؼ�������������Щ���ĵ������........
	public void displaySkillTips(Graphics g){
		Color c = g.getColor();
		g.setFont(new Font("����",Font.BOLD | Font.ITALIC,19));
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
	
	//���帺�������Ϸ������߳�
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
	//������̼����࣬��дpress��release�¼��ķ���
	private class KeyMonitor extends KeyAdapter{

		public void keyReleased(KeyEvent e) {
			snake.keyReleased(e);
		}

		public void keyPressed(KeyEvent e) {
			snake.keyPressed(e);
		}
		
	}
}

