package basil.tictactoe;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class AndroidTicTacToe extends Activity {
	
	//Represents the internal state of the game
	private TicTacToeGame mGame;	
	// Buttons making up the board
	private Button mBoardButtons[];
	// Various text displayed
	private TextView mInfoTextView;
	private boolean mGameOver;
	private boolean mPlayerFirst;
	private int human_wins; private int android_wins; private int ties;
	
	static final int DIALOG_DIFFICULTY_ID = 0;
	static final int DIALOG_QUIT_ID = 1;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mBoardButtons = new Button[TicTacToeGame.BOARD_SIZE];
        mBoardButtons[0] = (Button) findViewById(R.id.one);
        mBoardButtons[1] = (Button) findViewById(R.id.two); 
        mBoardButtons[2] = (Button) findViewById(R.id.three); 
        mBoardButtons[3] = (Button) findViewById(R.id.four); 
        mBoardButtons[4] = (Button) findViewById(R.id.five); 
        mBoardButtons[5] = (Button) findViewById(R.id.six); 
        mBoardButtons[6] = (Button) findViewById(R.id.seven); 
        mBoardButtons[7] = (Button) findViewById(R.id.eight); 
        mBoardButtons[8] = (Button) findViewById(R.id.nine);
        mInfoTextView = (TextView) findViewById(R.id.information); 
        mGame = new TicTacToeGame();
        mPlayerFirst = false;
        human_wins=android_wins=ties=0;
        TextView tv = (TextView) findViewById(R.id.score);
    	String s = "Human:  " + human_wins + "\t\tAndroid:  " + android_wins + "\t\tTies:  " + ties;
    	tv.setText(s);
        startNewGame();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) { 
     super.onCreateOptionsMenu(menu); 
     
     MenuInflater inflater = getMenuInflater();
     inflater.inflate(R.menu.options_menu, menu);
     return true;
    }
    
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
    	}
    	return false;
    } 
    
    protected Dialog onCreateDialog(int id) {
    	Dialog dialog = null;
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);

    	switch(id) {
    	case DIALOG_DIFFICULTY_ID:
    		builder.setTitle(R.string.difficulty_choose);
    		final CharSequence[] levels = {
    				getResources().getString(R.string.difficulty_easy),
    				getResources().getString(R.string.difficulty_harder), 
    				getResources().getString(R.string.difficulty_expert)};

    		// TODO: Set selected, an integer (0 to n-1), for the Difficulty dialog.
    		// selected is the radio button that should be selected.
    		int selected = mGame.getDifficultyLevel().ordinal();
    		builder.setSingleChoiceItems(levels, selected, 
    				new DialogInterface.OnClickListener() {
    			public void onClick(DialogInterface dialog, int item) {
    				dialog.dismiss(); // Close dialog
    				// TODO: Set the diff level of mGame based on which item was selected.
    				mGame.setDifficultyLevel(TicTacToeGame.DifficultyLevel.values()[item]);
    				// Display the selected difficulty level
    				Toast.makeText(getApplicationContext(), levels[item], 
    						Toast.LENGTH_SHORT).show(); 
    			}
    		});
    		dialog = builder.create();

    		break; 
    	case DIALOG_QUIT_ID:
    		// Create the quit confirmation dialog

    		builder.setMessage(R.string.quit_question)
    		.setCancelable(false)
    		.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
    			public void onClick(DialogInterface dialog, int id) {
    				AndroidTicTacToe.this.finish();
    			}
    		})
    		.setNegativeButton(R.string.no, null); 
    		dialog = builder.create();
    		break;
    	}
    	return dialog;
    }
    
    private void startNewGame(){
    	mGame.clearBoard();
    	mGameOver = false;
    	mPlayerFirst = !mPlayerFirst;
    	for(int i = 0; i < mBoardButtons.length; i++){
    		 mBoardButtons[i].setText("");
    		 mBoardButtons[i].setEnabled(true); 
    		 mBoardButtons[i].setOnClickListener(new ButtonClickListener(i)); 
    	}
    	mInfoTextView.setText(R.string.first_human);
    	if(!mPlayerFirst){
    		mInfoTextView.setText(R.string.first_android);
    		int move = mGame.getComputerMove();
			setMove(TicTacToeGame.COMPUTER_PLAYER, move);		
    	}
    }
    
    
    
    private void setMove(char player, int location) {
    	 if(!mGameOver){
	    	 mGame.setMove(player, location);	    	 
	    	 mBoardButtons[location].setText(String.valueOf(player));    	 
	    	 mBoardButtons[location].setEnabled(false); 
	    	 if (player == TicTacToeGame.HUMAN_PLAYER) 
	    		 mBoardButtons[location].setTextColor(Color.rgb(0, 200, 0)); 
	    	 else
	    		 mBoardButtons[location].setTextColor(Color.rgb(200, 0, 0));
    	 }	    	 
    }
    
    
    private class ButtonClickListener implements View.OnClickListener {
    	int location;
    	
    	public ButtonClickListener(int location){
    		this.location = location;
    	}
    	
    	
    	public void onClick(View v){
    		if(mBoardButtons[location].isEnabled()){
    			setMove(TicTacToeGame.HUMAN_PLAYER, location);
    			//If no winner yet
    			int winner = mGame.checkForWinner();
    			if(winner == 0){
    				mInfoTextView.setText(R.string.turn_computer);
    				int move = mGame.getComputerMove();
    				setMove(TicTacToeGame.COMPUTER_PLAYER, move);
    				winner = mGame.checkForWinner();
    			}
    			if(winner == 0)
    				mInfoTextView.setText(R.string.turn_human);
    			else if(winner == 1 && !mGameOver){
    				mInfoTextView.setText(R.string.result_tie);
    				ties++;
    			}
    			else if(winner == 2 && !mGameOver){
    				mInfoTextView.setText(R.string.result_human_wins);
    				human_wins++;
    			}
    			else if(!mGameOver){
    				mInfoTextView.setText(R.string.result_computer_wins);
    				android_wins++;
    			}
    			TextView tv = (TextView) findViewById(R.id.score);
    	    	String s = "Human:  " + human_wins + "\tAndroid:  " + android_wins + "\tTies:  " + ties;
    	    	tv.setText(s);
    			mGameOver = winner > 0;
     		}
    	}
     }
   
 }