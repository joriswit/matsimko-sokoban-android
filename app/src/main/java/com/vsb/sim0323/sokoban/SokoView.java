package com.vsb.sim0323.sokoban;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;

import java.util.LinkedList;
import java.util.List;


public class SokoView extends BaseSokoView {
    private SokoControls controls;
    private long sleepMillis = 150;
    private boolean moveInProgress;

    private Player player;

    private List<OnWinListener> onWinListeners = new LinkedList<>();

    public SokoView(Context context) {
        super(context);
        init(context);
    }

    public SokoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SokoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        controls = new TapFollowSokoControls(); //default controls
    }


    @Override
    public void setLevel(Level level) {
        super.setLevel(level);
        player = board.getPlayer();
        controls.setPlayer(player);
    }



    //using a command interface so that I can have only one loop no matter if it is moveX or moveY
    private interface MoveCommand {
        boolean canMove();
        void move();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(isMoveInProgress()) {
            return true;
        }

        if(controls.handle(event, width, height)) {
            MoveCommand moveCommand = null;
            int moveCount = 0;
            if(controls.hasMovedX()) {
                int dx = controls.getDx();
                int dxPerMove = dx > 0 ? 1 : -1;
                moveCommand = new MoveCommand() {
                    @Override
                    public boolean canMove() {
                        return player.canMoveX(dxPerMove);
                    }

                    @Override
                    public void move() {
                        player.moveX(dxPerMove);
                    }
                };
                moveCount = Math.abs(dx);
            }
            else if(controls.hasMovedY()) {
                int dy = controls.getDy();
                int dyPerMove = dy > 0 ? 1 : -1;
                moveCommand = new MoveCommand() {
                    @Override
                    public boolean canMove() {
                        return player.canMoveY(dyPerMove);
                    }

                    @Override
                    public void move() {
                        player.moveY(dyPerMove);
                    }
                };
                moveCount = Math.abs(dy);
            } else if (controls instanceof ReplaySolutionSokoControls) {
                ReplaySolutionSokoControls replay = (ReplaySolutionSokoControls)controls;
                moveCommand = new MoveCommand() {
                    @Override
                    public boolean canMove() {

                        return !replay.isAtEndOfSolution();
                    }

                    @Override
                    public void move() {

                        Character move = replay.getNextMove();
                        switch(Character.toLowerCase(move)) {
                            case 'u': player.moveY(-1); break;
                            case 'd': player.moveY(1); break;
                            case 'l': player.moveX(-1); break;
                            case 'r': player.moveX(1); break;
                        }
                    }
                };
                moveCount = replay.getSolutionLength();
            }


            Handler handler = new Handler();
            class RunMove implements  Runnable {
                private int i;
                private MoveCommand moveCommand;

                public RunMove(MoveCommand moveCommand, int i) {
                    this.moveCommand = moveCommand;
                    this.i = i;
                }

                @Override
                public void run() {
                    if(i == 0) {
                        setMoveInProgress(false);
                        handler.post(() -> checkForWin()); //the listeners should be notified on UI thread in case they work with UI
                        return;
                    }
                    if(moveCommand.canMove()) {
                        moveCommand.move();
                        invalidate();
                        handler.postDelayed(new RunMove(moveCommand, i - 1), sleepMillis);
                    }
                    else {
                        //player can no longer move
                        setMoveInProgress(false);
                        handler.post(() -> checkForWin()); //the listeners should be notified on UI thread in case they work with UI
                        return;
                    }
                }
            }
            //move tile by tile up to moveCount tiles or until legal
            setMoveInProgress(true);
            new RunMove(moveCommand, moveCount).run();

        }

            
        return true;
    }

    private void checkForWin() {
        if(board.isLevelWon()) {
            notifyOnWinListeners();
        }
    }

    private void notifyOnWinListeners() {
        for(OnWinListener listener : onWinListeners) {
            listener.onWin();
        }
    }

    public void addOnWinListener(OnWinListener listener) {
        onWinListeners.add(listener);
    }

    public int getScore() {
        return board.getReachedGoalCount();
    }

    void sleep(long l) {
        try {
            Thread.sleep(l);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void resetLevel() {
        if(!isMoveInProgress()) {
            setLevel(currentLevel);
        }

    }

    public synchronized boolean isMoveInProgress() {
        return moveInProgress;
    }

    public synchronized void setMoveInProgress(boolean moveInProgress) {
        this.moveInProgress = moveInProgress;
    }

    public void setControls(SokoControls controls) {
        this.controls = controls;
    }
}
