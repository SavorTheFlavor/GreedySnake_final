package cn.edu.scau.snake;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 
 *������Ϸ��һЩ���ز���
 *����/��ȡ������¼֮���
 *
 */
public class GameManager {

	private Yard yard;

	public GameManager(Yard yard) {
		this.yard = yard;
	}

	/**
	 * ������߷�
	 */
	public void saveScore() {
		DataInputStream dis = null;
		DataOutputStream dos = null;
		// try(fw = new FileWriter("something/level.txt");)
		// {
		// fw.write(yard.score);
		// }catch (Exception e) {
		// }//jdk1.7��������

		try {
			dis = new DataInputStream(
					new FileInputStream("something/level.txt"));
			int score;
			try {
				score = dis.readInt();
			} catch (EOFException e) {// �ڶ�ȡ�����ֽ�֮ǰ����ĩβ,
													//���ĵ��޼�¼�������˶����޸�level�ĵ�ʱ���µ��쳣
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
	 * ��ȡ������¼
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
