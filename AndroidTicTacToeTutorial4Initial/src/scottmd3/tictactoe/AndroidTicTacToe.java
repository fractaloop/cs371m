package scottmd3.tictactoe;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.TextView;
import android.widget.Toast;

public class AndroidTicTacToe extends Activity {

	private static final String TAG = "AndroidTicTacToe";

	private static final int DIALOG_DIFFICULTY_ID = 0;
	private static final int DIALOG_QUIT_ID = 1;
	private static final int DIALOG_ABOUT_ID = 2;

	// Whose turn to go first
	private char mTurn = TicTacToeGame.COMPUTER_PLAYER;    

	// Keep track of wins
	private int mHumanWins = 0;
	private int mComputerWins = 0;
	private int mTies = 0;

	// game logic
	private TicTacToeGame mGame;

	// Various text displayed
	private TextView mInfoTextView;
	private TextView mHumanScoreTextView;
	private TextView mComputerScoreTextView;
	private TextView mTieScoreTextView;
	
	// for all the sounds we play
	private SoundPool mSounds;
	private int mHumanMoveSoundID;
	private int mComputerMoveSoundID;

	private boolean mGameOver; 

	private BoardView mBoardView;


	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		mGame = new TicTacToeGame();
		mBoardView = (BoardView) findViewById(R.id.board);
		mBoardView.setGame(mGame);
		// get the TextViews
		mInfoTextView = (TextView) findViewById(R.id.information);
		mHumanScoreTextView = (TextView) findViewById(R.id.player_score);
		mComputerScoreTextView = (TextView) findViewById(R.id.computer_score);
		mTieScoreTextView = (TextView) findViewById(R.id.tie_score);

		mBoardView.setOnTouchListener(mTouchListener);

		startNewGame();
	}

	@Override
	protected void onResume() {
		super.onResume();
		mSounds = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);
		// 2 = maximum sounds ot play at the same time, 
		// AudioManager.STREAM_MUSIC is the stream type typically used for games
		// 0 is the "the sample-rate converter quality. Currently has no effect. Use 0 for the default."
		mHumanMoveSoundID = mSounds.load(this, R.raw.user_sound, 1); 
		// Context, id of resource, priority (currently no effect)
		mComputerMoveSoundID = mSounds.load(this, R.raw.computer_sound, 1); 
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		Log.d(TAG, "in onPause");
		if(mSounds != null) {
			mSounds.release();
			mSounds = null;
		}
	}
	
	
	// Set up the game board. 
	private void startNewGame() {
		mGameOver = false;
		mGame.clearBoard();  
		mBoardView.invalidate();

		// Alternate who goes first
		if (mTurn == TicTacToeGame.HUMAN_PLAYER) {
			mTurn = TicTacToeGame.COMPUTER_PLAYER;
			mInfoTextView.setText(R.string.first_computer);
			int move = mGame.getComputerMove();
			setMove(TicTacToeGame.COMPUTER_PLAYER, move);
			mInfoTextView.setText(R.string.turn_human);
		}
		else {
			mTurn = TicTacToeGame.HUMAN_PLAYER;
			mInfoTextView.setText(R.string.first_human); 
		}	
	}

	private boolean setMove(char player, int location) {
		if(mGame.setMove(player, location)){ 
			mSounds.play(mHumanMoveSoundID, 1, 1, 1, 0, 1); 
			mBoardView.invalidate();
			return true;
		}
		return false;
	}

	// when game is over, disable all buttons and set flag
	private void gameOver() {
		mGameOver = true;
	}

	@Override 
	public boolean onCreateOptionsMenu(Menu menu) { 
		super.onCreateOptionsMenu(menu); 

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.options_menu, menu);
		return true;

	}


	@SuppressWarnings("deprecation")
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.new_game:
			startNewGame();
			return true;
		case R.id.ai_difficulty: 
			showDialog(DIALOG_DIFFICULTY_ID);   	
			return true;
		case R.id.quit:
			showDialog(DIALOG_QUIT_ID);
			return true;
		case R.id.about:
			showDialog(DIALOG_ABOUT_ID);
			return true;
		}
		return false;
	}   

	protected Dialog onCreateDialog(int id) {

		Log.d(TAG, "In onCreateDialog");

		Dialog dialog = null;
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		switch(id) {
		case DIALOG_DIFFICULTY_ID:

			builder.setTitle(R.string.difficulty_choose);

			final CharSequence[] levels = {
					getResources().getString(R.string.difficulty_easy),
					getResources().getString(R.string.difficulty_harder), 
					getResources().getString(R.string.difficulty_expert)};

			final int selected = mGame.getDifficultyLevel().ordinal();
			Log.d(TAG, "selected difficulty value: " + selected + ", level: " + mGame.getDifficultyLevel());

			builder.setSingleChoiceItems(levels, selected, 
					new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int item) {
					dialog.dismiss();   // Close dialog

					mGame.setDifficultyLevel(TicTacToeGame.DifficultyLevel.values()[item]);
					Log.d(TAG, "Difficulty level: " + mGame.getDifficultyLevel());

					// Display the selected difficulty level
					Toast.makeText(getApplicationContext(), levels[item], 
							Toast.LENGTH_SHORT).show();        	    
				}
			});
			dialog = builder.create();
			break;    // this case
		case DIALOG_QUIT_ID:
			// Create the quit confirmation dialog

			builder.setMessage(R.string.quit_question).setCancelable(false)
			.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					AndroidTicTacToe.this.finish();
				}
			}).setNegativeButton(R.string.no, null);   
			dialog = builder.create();
			break;
		case DIALOG_ABOUT_ID:
			Log.d(TAG, "Create about dialog");
			Context context = getApplicationContext();
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
			View layout = inflater.inflate(R.layout.about_dialog, null); 		
			builder.setView(layout);
			builder.setPositiveButton("OK", null);	
			dialog = builder.create();
			break;
		}
		if(dialog == null)
			Log.d(TAG, "Uh oh! Dialog is null");
		else
			Log.d(TAG, "Dialog created: " + id + ", dialog: " + dialog);
		return dialog;          
	}

	//Listen for touches on the board
	private OnTouchListener mTouchListener = new OnTouchListener() {
		public boolean onTouch(View v, MotionEvent event) {
			// Determine which cell was touched 
			int col = (int) event.getX() / mBoardView.getBoardCellWidth();
			int row = (int) event.getY() / mBoardView.getBoardCellHeight();
			int pos = row * 3 + col;
			if (!mGameOver && setMove(TicTacToeGame.HUMAN_PLAYER, pos)){
				// If no winner yet, let the <Mike's version> computer make a move
				int winner = mGame.checkForWinner();
				if (winner == 0) { 
					mInfoTextView.setText(R.string.turn_computer);
					int move = mGame.getComputerMove();
					setMove(TicTacToeGame.COMPUTER_PLAYER, move);
					mSounds.play(mComputerMoveSoundID, 1, 1, 1, 0, 1); 
					winner = mGame.checkForWinner();
				} 
				if (winner == 0)
					mInfoTextView.setText(R.string.turn_human);
				else {
					if (winner == 1)  {
						mInfoTextView.setText(R.string.result_tie);
						mTies++;
						mTieScoreTextView.setText(Integer.toString(mTies));
					}
					else if (winner == 2) {
						mHumanWins++;
						mHumanScoreTextView.setText(Integer.toString(mHumanWins));
						mInfoTextView.setText(R.string.result_human_wins);
					}
					else {
						mComputerWins++;
						mComputerScoreTextView.setText(Integer.toString(mComputerWins));
						mInfoTextView.setText(R.string.result_computer_wins);
					}
					gameOver();
				}
			}
			//So we aren't notified of continued events when finger is moved
			return false;
		}
	};
}