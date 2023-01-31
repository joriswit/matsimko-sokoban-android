package com.vsb.sim0323.sokoban;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

/**
 * This class doesn't handle events, so it can be used as image preview
 */
public class BaseSokoView extends View {

    private Bitmap[] bmp;

    protected int width;
    protected int height;

    protected Level currentLevel = null;
    protected Board board;

    public BaseSokoView(Context context) {
        super(context);
        init(context);
    }

    public BaseSokoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BaseSokoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    //needs to be private so it can't be overridden
    private void init(Context context) {
        bmp = new Bitmap[8];

        bmp[0] = BitmapFactory.decodeResource(getResources(), R.drawable.empty);
        bmp[1] = BitmapFactory.decodeResource(getResources(), R.drawable.wall);
        bmp[2] = BitmapFactory.decodeResource(getResources(), R.drawable.box);
        bmp[3] = BitmapFactory.decodeResource(getResources(), R.drawable.goal);
        bmp[4] = BitmapFactory.decodeResource(getResources(), R.drawable.hero);
        bmp[5] = BitmapFactory.decodeResource(getResources(), R.drawable.boxok);
        bmp[6] = BitmapFactory.decodeResource(getResources(), R.drawable.herook);
        bmp[7] = BitmapFactory.decodeResource(getResources(), R.drawable.outside);
    }


    public void setLevel(Level level) {
        currentLevel = level;
        width = getWidth() / currentLevel.getWidth();
        height = getHeight() / currentLevel.getHeight();
        board = new Board(level);
        invalidate();
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        width = w / currentLevel.getWidth();
        height = h / currentLevel.getHeight();
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        //making the iterator was rather redundant as I should've just made a board.draw() method
        // (it would also be more appropriate encapsulation-wise)
        for(Tile tile : board) {
            tile.draw(canvas, bmp, width, height);
        }

    }
}
