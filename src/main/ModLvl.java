package main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ModLvl {
	private File lvlEnvFile;
	private int[][] board;

	public ModLvl(int lvlNum) {
		lvlEnvFile =  new File(ClimberMain.dir, String.format("/Lvls/Lvl%d.clvl", lvlNum));
		try (ObjectInputStream inStream = new ObjectInputStream(new FileInputStream(lvlEnvFile));) {
			Object tmpRead = inStream.readObject();
			if (!(tmpRead instanceof int[][])) {
				throw new IOException("Invalid level file");
			}
			board = (int[][]) tmpRead;
			System.out.println("Board loaded");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		ModLvl l = new ModLvl(2);
		l.board = new int[40][100];
		for (int i = 0; i < l.board.length; i++) {
			for (int j = 0; j < l.board[i].length; j++) {
				l.board[i][j] = 22; 
			}
		}
		l.update();
		System.out.println("Done");
	}

	private void update() {
		try {
			ObjectOutputStream outStream = new ObjectOutputStream(new FileOutputStream(lvlEnvFile));
			outStream.writeObject(board);
			outStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
//				System.out.println("Saved");
	}
}
