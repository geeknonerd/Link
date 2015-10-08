package org.geek.link.object;

import android.content.Context;

public class GameConf {
	public static final int PIECE_WIDTH = 60;
	public static final int PIECE_HEIGHT = 60;
	public static int DEFAULT_TIME = 100;
	private int xSize;
	private int ySize;
	private int beginImageX;
	private int beginImageY;
	private long gameTime;
	private Context context;
	public GameConf(int xSize, int ySize, int beginImageX, int beginImageY, long gameTime, Context context) {
		this.xSize = xSize;
		this.ySize = ySize;
		this.beginImageX = beginImageX;
		this.beginImageY = beginImageY;
		this.gameTime = gameTime;
		this.context = context;
	}
	public int getXSize() {
		return xSize;
	}
	public int getYSize() {
		return ySize;
	}
	public int getBeginImageX() {
		return beginImageX;
	}
	public int getBeginImageY() {
		return beginImageY;
	}
	public long getGameTime() {
		return gameTime;
	}
	public Context getContext() {
		return context;
	}
}
