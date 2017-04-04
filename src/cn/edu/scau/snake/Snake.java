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
 * 一只贪吃的蛇，是个大吃货，越吃越疯狂
 * 具有灵敏的嗅觉，能瞬间察觉到食物的气息
 * 还经常暴走
 * <br>
 *这里采用了双向链表的数据结构模拟这条蛇
 *每移动一步就相当于删除尾节点，创建新的节点插到头节点前面
 *并使最后一个节点成为新的尾节点，第一个节点成为新的头结点
 *每磕一个蛋则创建新的节点插到尾节点后面
 *
 */

public class Snake {
	

	private boolean BL = false,BU = false, BD = false, BR = false;	//各方向键被按下的情况
	private Node head = null;//头节点
	private Node tail = null;//尾节点
	private int size = 0;//节点数
	private Node n = new Node(((int) Math.random() * 100 + 20) % 80, ((int) Math.random() * 100 + 20) % 80,
			Direction.LEFT);
	private Yard yard;//持有Yard的引用
	private int speed = yard.defultGameSpeed;
	
	private boolean isRushing = false;
	private boolean hasSmelled = false;
	
	private int smellTime = 1;
	

	private static Random r = new Random();
	
	private static Toolkit tk = null;
	private static Image flame = null;
	
	//图片的加载放在静态代码块里
	static{
		 tk = Toolkit.getDefaultToolkit();
		flame = tk.getImage(Snake.class.getClassLoader().getResource("cn/edu/scau/snake/images/flame.gif"));
	}
	

	//初始化
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
	 * 把new出来的节点加到尾巴上
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
		tail.next = node;//把新节点接到尾节点后面
		node.prev = tail;//新节点的前驱指向尾节点
		tail = node;//让它成为新的尾节点
		size++;
	}

	/**
	 * 把new出来的节点加到头节点上
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
		node.next = head;//新节点的后继指向头结点
		node.prev = null;//前驱置空
		head.prev = node;//头结点的前驱指向新节点
		head = node;//新节点成为新的头结点
		size++;
	}

	private void move() {
		addToHead();
		deleteTail();
		checkDead();
	}
	
	/*
	 * 加速
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
	 * 检查是否死亡
	 * 这里为降低游戏难度，就不检查自己和自己相撞的情况了
	 * 不过现实中也不会有这么笨的蛇吧
	 */
	private void checkDead(){
		if(head.row<2||head.col<1||head.row>Yard.ROWS-2||head.col>Yard.COLS-2){
			yard.gameOver();
		}
	}
	
	/**
	 * 删除尾节点
	 */
	public void deleteTail() {
		if (size == 0)
			return;
		tail.prev.next = null;
		tail = tail.prev;
	}

	/**
	 * 画蛇
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
	 * 蛇结点的定义
	 *
	 */
	private class Node {
		int w = Yard.GRID;//宽
		int h = Yard.GRID;//长
		int row, col;//坐标
		
		Direction dir = Direction.LEFT;//方向
		Node next = null;//后继
		Node prev = null;//前驱

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
	 * 吃蛋
	 * 通过检测头结点与蛋的外接矩形是否相交
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
	
	//吃一堆蛋
	public void eat(List<Egg> eggs){
		for(int i=0; i<eggs.size(); i++){
			Egg e = eggs.get(i);
			eat(e,"another");
		}	
	}
	
	/**
	 * 特技！！贪吃蛇用其灵敏的嗅觉和惊人的食欲察觉食物的气息
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
	
	//跑起来！
	public void rush(){
		yard.gameSpeed = speed/2;
		isRushing = true;
	}
	
	public void walk(){
		yard.gameSpeed = speed;
		isRushing = false;
	}
	
	//加分
	private void addLevel(Egg e){
		yard.score+=e.w-4;
	}
	
	private Rectangle getRect(){
		return new Rectangle(Yard.GRID * head.col, Yard.GRID * head.row,head.w, head.h);
	}
	
	/**
	 * 
	 * 键盘监听~按下
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
	 * 键盘监听~松开
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

	//定位
	void locateDirection(){
		if(BL&&!BU&&!BD&&!BR)			head.dir=Direction.LEFT;
		else if(!BL&&BU&&!BD&&!BR)		head.dir=Direction.UP;
		else if(!BL&&!BU&&BD&&!BR)		head.dir=Direction.DOWN;
		else if(!BL&&!BU&&!BD&&BR)		head.dir=Direction.RIGHT;
		/*还是不要八个方向了*/
//		else if(BL&&BU&&!BD&&!BR)		head.dir=Direction.LU;
//		else if(BL&&!BU&&BD&&!BR)		head.dir=Direction.LD;
//		else if(!BL&&BU&&!BD&&BR)		head.dir=Direction.RU;
//		else if(!BL&&!BU&&BD&&BR)		head.dir=Direction.RD;
	}
}
