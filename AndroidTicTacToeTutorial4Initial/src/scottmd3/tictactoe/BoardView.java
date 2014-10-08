package scottmd3.tictactoe;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;


public class BoardView extends View {

	public static final int GRID_LINE_WIDTH = 6;
	private Bitmap mHumanBitmap;
	private Bitmap mComputerBitmap;
	private Paint mPaint;
	private TicTacToeGame mGame;
	
	public void setGame(TicTacToeGame game) {
		mGame = game;
	}
	
	public BoardView(Context context) {
		super(context);
		initialize();
	}
	public BoardView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initialize();
	}
	public BoardView(Context context, AttributeSet attrs) {
		super(context, attrs); 
		initialize();
	}

	public void initialize() { 
		mHumanBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.x_icon); 
		mComputerBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.circle_icon);
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	}


	@Override
	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		// Determine the width and height of the View
		int boardWidth = getWidth();
		int boardHeight = getHeight();
		// Make thick, light gray lines
		mPaint.setColor(Color.LTGRAY); 
		mPaint.setStrokeWidth(GRID_LINE_WIDTH);

		// Draw the vertical and horizontal board lines
		canvas.drawLine(boardWidth / 3, 0, boardWidth / 3, boardHeight, mPaint);
		canvas.drawLine(2 * boardWidth / 3, 0, 2* boardWidth / 3, boardHeight, mPaint);
		canvas.drawLine(0, boardHeight / 3, boardWidth, boardHeight / 3, mPaint);
		canvas.drawLine(0, 2 * boardHeight / 3, boardWidth, 2 * boardHeight / 3, mPaint);
		
		// Draw all the X and O images
		for (int i = 0; i < TicTacToeGame.BOARD_SIZE; i++){
			int col = i % 3;
			int row = i / 3;
			// Define the boundaries of a destination rectangle for the image
			int cellWidth = boardWidth / 3;
			int xTopLeft = col * cellWidth;
			int yTopLeft = row * cellWidth;
			int xBottomRight = (col + 1) * cellWidth;
			int yBottomRight = (row + 1) * cellWidth;
			if (mGame != null && mGame.getBoardOccupant(i) == TicTacToeGame.HUMAN_PLAYER) {
				canvas.drawBitmap(mHumanBitmap, null, //src
						new Rect(xTopLeft, yTopLeft, xBottomRight, yBottomRight), // dest
						null);						
			}
			else if (mGame != null && mGame.getBoardOccupant(i) == TicTacToeGame.COMPUTER_PLAYER) {
				canvas.drawBitmap(mComputerBitmap, null, // src
						new Rect(xTopLeft, yTopLeft, xBottomRight, yBottomRight), // dest 
						null);
			}
		}
	}


	public int getBoardCellWidth() {
		return getWidth() / 3;
	}

	public int getBoardCellHeight() {
		return getHeight() / 3;
	}




	
}
