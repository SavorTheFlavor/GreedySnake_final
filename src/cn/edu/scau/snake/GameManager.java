package cn.edu.scau.snake;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 
 *关于游戏的一些加载操作
 *保存/读取分数记录之类的
 *
 */
public class GameManager {

	private Yard yard;

	public GameManager(Yard yard) {
		this.yard = yard;
	}

	/**
	 * 保存最高分
	 */
	public void saveScore() {
		DataInputStream dis = null;
		DataOutputStream dos = null;
		// try(fw = new FileWriter("something/level.txt");)
		// {
		// fw.write(yard.score);
		// }catch (Exception e) {
		// }//jdk1.7的新特性

		try {
			dis = new DataInputStream(
					new FileInputStream("something/level.txt"));
			int score;
			try {
				score = dis.readInt();
			} catch (EOFException e) {// 在读取所有字节之前到达末尾,
													//即文档无记录或者有人恶意修改level文档时导致的异常
				score = 0;
			}
			if (score < yard.score) {
				dos = new DataOutputStream(new FileOutputStream(
						"something/level.txt"));
				dos.writeInt(yard.score);
			}
		} catch (IOException e) {

		} finally {
			try {
				if (dis != null)
					dis.close();
				if (dos != null)
					dos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 读取分数记录
	 */
	public void loadScore() {
		DataInputStream dis = null;
		try {
			dis = new DataInputStream(
					new FileInputStream("something/level.txt"));
			int hs = dis.readInt();
			yard.highestScore = hs;
		} catch (IOException e) {
			yard.highestScore = 1;
		} finally {
			try {
				if (dis != null)
					dis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
}
