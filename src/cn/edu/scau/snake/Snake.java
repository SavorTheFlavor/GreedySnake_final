package cn.edu.scau.snake;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.Properties;
import java.util.Random;


/**
 * һֻ̰�Ե��ߣ��Ǹ���Ի���Խ��Խ���
 * �����������������˲������ʳ�����Ϣ
 * ����������
 * <br>
 *���������˫����������ݽṹģ��������
 *ÿ�ƶ�һ�����൱��ɾ��β�ڵ㣬�����µĽڵ�嵽ͷ�ڵ�ǰ��
 *��ʹ���һ���ڵ��Ϊ�µ�β�ڵ㣬��һ���ڵ��Ϊ�µ�ͷ���
 *ÿ��һ�����򴴽��µĽڵ�嵽β�ڵ����
 *
 */

public class Snake {
	

	private boolean BL = false,BU = false, BD = false, BR = false;	//������������µ����
	private Node head = null;//ͷ�ڵ�
	private Node tail = null;//β�ڵ�
	private int size = 0;//�ڵ���
	private Node n = new Node(((int) Math.random() * 100 + 20) % 80, ((int) Math.random() * 100 + 20) % 80,
			Direction.LEFT);
	private Yard yard;//����Yard������
	private int speed = yard.defultGameSpeed;
	
	private boolean isRushing = false;
	private boolean hasSmelled = false;
	
	private int smellTime = 1;
	

	private static Random r = new Random();
	
	private static Toolkit tk = null;
	private static Image flame = null;
	
	//ͼƬ�ļ��ط��ھ�̬�������
	static{
		 tk = Toolkit.getDefaultToolkit();
		flame = tk.getImage(Snake.class.getClassLoader().getResource("cn/edu/scau/snake/images/flame.gif"));
	}
	

	//��ʼ��
	public Snake(Yard yard) {
		head = n;
		tail = n;
		size = 1;
		this.yard=yard;
	}
	
	public int getSize() {
		return size;
	}



	/**
	 * ��new�����Ľڵ�ӵ�β����
	 * 
	 */
	public void addToTail() {
		Node node = null;
		switch (tail.dir) {
		case LEFT:
			node = new Node(tail.row, tail.col + 1, tail.dir);
			break;
		case RIGHT:
			node = new Node(tail.row, tail.col - 1, tail.dir);
			break;
		case UP:
			node = new Node(tail.row + 1, tail.col, tail.dir);
			break;
		case DOWN:
			node = new Node(tail.row - 1, tail.col, tail.dir);
			break;
		case LU:
			node = new Node(tail.row + 1, tail.col+1, tail.dir);
			break;
		case LD:
			node = new Node(tail.row - 1, tail.col + 1, tail.dir);
			break;
		case RU:
			node = new Node(tail.row + 1, tail.col -1, tail.dir);
			break;
		case RD:
			node = new Node(tail.row -1, tail.col -1, tail.dir);
			break;
		}
		tail.next = node;//���½ڵ�ӵ�β�ڵ����
		node.prev = tail;//�½ڵ��ǰ��ָ��β�ڵ�
		tail = node;//������Ϊ�µ�β�ڵ�
		size++;
	}

	/**
	 * ��new�����Ľڵ�ӵ�ͷ�ڵ���
	 * 
	 */
	
	public void addToHead() {
		Node node = null;
		switch (head.dir) {
		case LEFT:
			node = new Node(head.row, head.col - 1, head.dir);
			break;
		case RIGHT:
			node = new Node(head.row, head.col + 1, head.dir);
			break;
		case UP:
			node = new Node(head.row - 1, head.col, head.dir);
			break;
		case DOWN:
			node = new Node(head.row + 1, head.col, head.dir);
			break;
		case LU:
			node = new Node(tail.row - 1, tail.col-1, tail.dir);
			break;
		case LD:
			node = new Node(tail.row + 1, tail.col - 1, tail.dir);
			break;
		case RU:
			node = new Node(tail.row - 1, tail.col +1, tail.dir);
			break;
		case RD:
			node = new Node(tail.row +1, tail.col +1, tail.dir);
			break;
		}
		node.next = head;//�½ڵ�ĺ��ָ��ͷ���
		node.prev = null;//ǰ���ÿ�
		head.prev = node;//ͷ����ǰ��ָ���½ڵ�
		head = node;//�½ڵ��Ϊ�µ�ͷ���
		size++;
	}

	private void move() {
		addToHead();
		deleteTail();
		checkDead();
	}
	
	/*
	 * ����
	 */
	private void addSpeed(){
		speed-=4;
		if(this.isRushing){
			yard.gameSpeed = speed/2;	
		}
		else{
			yard.gameSpeed = speed;
		}
	}
	
	/*
	 * ����Ƿ�����
	 * ����Ϊ������Ϸ�Ѷȣ��Ͳ�����Լ����Լ���ײ�������
	 * ������ʵ��Ҳ��������ô�����߰�
	 */
	private void checkDead(){
		if(head.row<2||head.col<1||head.row>Yard.ROWS-2||head.col>Yard.COLS-2){
			yard.gameOver();
		}
	}
	
	/**
	 * ɾ��β�ڵ�
	 */
	public void deleteTail() {
		if (size == 0)
			return;
		tail.prev.next = null;
		tail = tail.prev;
	}

	/**
	 * ����
	 */
	public void draw(Graphics g) {
		if (size <= 0)
			return;
		move();
		Color c = g.getColor();
		//draw head.......
		g.setColor(Color.RED);
		g.fillRect(Yard.GRID * head.col, Yard.GRID * head.row, head.w, head.h);
		if(size<=1) 
			return;
		for (Node i = head.next; i != null; i = i.next) {
			i.draw(g);
		}
		g.setColor(c);
	}

	/**
	 * 
	 * �߽��Ķ���
	 *
	 */
	private class Node {
		int w = Yard.GRID;//��
		int h = Yard.GRID;//��
		int row, col;//����
		
		Direction dir = Direction.LEFT;//����
		Node next = null;//���
		Node prev = null;//ǰ��

		Node(int row, int col, Direction dir) {
			this.row = row;
			this.col = col;
			this.dir = dir;
		}

		void draw(Graphics g) {
			Color c = g.getColor();
			if(isRushing){
				g.setColor(Color.YELLOW);
				g.drawImage(flame,Yard.GRID * col+w/2-20, Yard.GRID * row+h/2-20,40,40,null );
			}
			g.setColor(new Color(200+size*10000));
			g.fillRect(Yard.GRID * col, Yard.GRID * row, w, h);
			g.setColor(c);
		}
	}
	
	/**
	 * �Ե�
	 * ͨ�����ͷ����뵰����Ӿ����Ƿ��ཻ
	 */
	public void eat(Egg e){
		if(this.getRect().intersects(e.getRect())){
			addLevel(e);
			e.reAppear();
			this.addToHead();
			this.addSpeed();
		}	
	}
	
	public void eat(Egg e,String another){
		if(this.getRect().intersects(e.getRect())){
			e.setBeEated(true);
			addLevel(e);
			this.addToHead();
			this.addSpeed();
		}	
	}
	
	//��һ�ѵ�
	public void eat(List<Egg> eggs){
		for(int i=0; i<eggs.size(); i++){
			Egg e = eggs.get(i);
			eat(e,"another");
		}	
	}
	
	/**
	 * �ؼ�����̰������������������;��˵�ʳ�����ʳ�����Ϣ
	 * 
	 */
	public void smell(){//only one time!!!!!
		if(smellTime>=1){
			int num = Integer.parseInt(PropertyManager.getInstance().getProperties("smellEggsNum"));
			for(int i=0; i<r.nextInt(num)+7 ;i++){
				yard.eggs.add(new Egg());
			}
			smellTime--;
			this.hasSmelled = true;
		}
	}
	
	public  boolean isHasSmelled() {
		return hasSmelled;
	}
	
	//��������
	public void rush(){
		yard.gameSpeed = speed/2;
		isRushing = true;
	}
	
	public void walk(){
		yard.gameSpeed = speed;
		isRushing = false;
	}
	
	//�ӷ�
	private void addLevel(Egg e){
		yard.score+=e.w-4;
	}
	
	private Rectangle getRect(){
		return new Rectangle(Yard.GRID * head.col, Yard.GRID * head.row,head.w, head.h);
	}
	
	/**
	 * 
	 * ���̼���~����
	 */
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		switch (key) {
		case KeyEvent.VK_LEFT:
			BL=true;
			break;
		case KeyEvent.VK_RIGHT:
			BR=true;
			break;
		case KeyEvent.VK_UP:
			BU=true;;
			break;
		case KeyEvent.VK_DOWN:
			BD=true;
			break;
		case KeyEvent.VK_SPACE:
			rush();
			break;
		}
		locateDirection();
	}

	/**
	 * 
	 * ���̼���~�ɿ�
	 */
	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		switch (key) {
		case KeyEvent.VK_LEFT:
			BL=false;
			break;
		case KeyEvent.VK_RIGHT:
			BR=false;
			break;
		case KeyEvent.VK_UP:
			BU=false;
			break;
		case KeyEvent.VK_DOWN:
			BD=false;
			break;
		case KeyEvent.VK_SPACE:
			walk();
			break;
		case KeyEvent.VK_S:
			smell();
			break;
		}
		locateDirection();
	}
	
//	private void suspend() {
//		yard.suspended = true;
//	}

	//��λ
	void locateDirection(){
		if(BL&&!BU&&!BD&&!BR)			head.dir=Direction.LEFT;
		else if(!BL&&BU&&!BD&&!BR)		head.dir=Direction.UP;
		else if(!BL&&!BU&&BD&&!BR)		head.dir=Direction.DOWN;
		else if(!BL&&!BU&&!BD&&BR)		head.dir=Direction.RIGHT;
		/*���ǲ�Ҫ�˸�������*/
//		else if(BL&&BU&&!BD&&!BR)		head.dir=Direction.LU;
//		else if(BL&&!BU&&BD&&!BR)		head.dir=Direction.LD;
//		else if(!BL&&BU&&!BD&&BR)		head.dir=Direction.RU;
//		else if(!BL&&!BU&&BD&&BR)		head.dir=Direction.RD;
	}
}
