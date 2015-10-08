package org.geek.link;

import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

import org.geek.link.board.GameService;
import org.geek.link.board.impl.GameServiceImpl;
import org.geek.link.object.GameConf;
import org.geek.link.object.LinkInfo;
import org.geek.link.view.GameView;
import org.geek.link.view.Piece;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.TextView;

public class Link extends Activity {
	private GameConf config;
	private GameService gameService;
	private GameView gameView;
	private Button startButton;
	private TextView timeTextView;
	private AlertDialog.Builder lostDialog;
	private AlertDialog.Builder successDialog;
	private Timer timer = new Timer();
	private int gameTime;
	private boolean isPlaying;
	SoundPool soundPool = new SoundPool(2, AudioManager.STREAM_SYSTEM, 8);
	int dis;
	private Piece selected = null;
	private Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0x123:
				timeTextView.setText("剩余时间："+gameTime);
				gameTime--;
				if (gameTime<0) {
					stopTimer();
					isPlaying = false;
					lostDialog.show();
					return;
				}
				break;
			}
		}
	};
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		init();
	}
	
	private void init() {
		config = new GameConf(8,11,2,6,100000,this);
		gameView = (GameView) findViewById(R.id.gameView);
		timeTextView = (TextView) findViewById(R.id.timeText);
		startButton = (Button) findViewById(R.id.startButton);
		dis = soundPool.load(this, R.raw.dis,1);
		gameService = new GameServiceImpl(this.config);
		gameView.setGameService(gameService);
		startButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View source) {
				startGame(GameConf.DEFAULT_TIME);
			}
		});
		this.gameView.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent e) {
				if (!isPlaying) {
					return false;
				}
				if (e.getAction()==MotionEvent.ACTION_DOWN) {
					gameViewTouchDown(e);
				}
				if (e.getAction()==MotionEvent.ACTION_UP) {
					gameViewTouchUp(e);
				}
				return true;
			}
		});
		lostDialog = createDialog("Lost","游戏失败！ 重新开始",R.drawable.lost).setPositiveButton("确定",new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				startGame(GameConf.DEFAULT_TIME);
			}
		});
		successDialog = createDialog("Success","游戏胜利！ 重新开始",R.drawable.success).setPositiveButton("确定",new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				startGame(GameConf.DEFAULT_TIME);
			}
		});
	}

	@Override
	protected void onPause() {
		stopTimer();
		super.onPause();
	}

	@Override
	protected void onResume() {
		if (isPlaying) {
			startGame(gameTime);
		}
		super.onResume();
	}

	private void gameViewTouchDown(MotionEvent event){
		Piece[][] pieces = gameService.getPieces();
		float touchX = event.getX();
		float touchY = event.getY();
		Piece currentPiece = gameService.findPiece(touchX,touchY);
		if (currentPiece==null) 
			return;
		this.gameView.setSelectedPiece(currentPiece);
		if (this.selected==null) {
			this.selected = currentPiece;
			this.gameView.postInvalidate();
			return;
		}
		if (this.selected!=null) {
			LinkInfo linkInfo = this.gameService.link(this.selected,currentPiece);
			if (linkInfo==null) {
				this.selected=currentPiece;
				this.gameView.postInvalidate();
			}else {
				handleSuccessLink(linkInfo,this.selected,currentPiece,pieces);
			}
		}
	}

	protected void gameViewTouchUp(MotionEvent e) {
		this.gameView.postInvalidate();
	}
	
	private void startGame(int gameTime) {
		if (this.timer!=null) {
			stopTimer();
		}
		this.gameTime = gameTime;
		if (gameTime==GameConf.DEFAULT_TIME) {
			gameView.startGame();
		}
		isPlaying=true;
		this.timer = new Timer();
		this.timer.schedule(new TimerTask() {
			@Override
			public void run() {
				handler.sendEmptyMessage(0x123);
			}
		}, 0, 1000);
		this.selected=null;
	}
	
	private void handleSuccessLink(LinkInfo linkInfo, Piece prePiece, Piece currentPiece, Piece[][] pieces) {
		this.gameView.setLinkInfo(linkInfo);
		this.gameView.setSelectedPiece(null);
		this.gameView.postInvalidate();
		pieces[prePiece.getIndexX()][prePiece.getIndexY()]=null;
		pieces[currentPiece.getIndexX()][currentPiece.getIndexY()]=null;
		this.selected=null;
		soundPool.play(dis, 1, 1, 0, 0, 1);
		if (!this.gameService.hasPieces()) {
			this.successDialog.show();
			stopTimer();
			isPlaying = false;
		}
	}
	
	private Builder createDialog(String title, String message, int imageResource) {
		return new  AlertDialog.Builder(this).setTitle(title).setMessage(message).setIcon(imageResource);
	}
	
	private void stopTimer() {
		this.timer.cancel();
		this.timer = null;
	}
	
}
