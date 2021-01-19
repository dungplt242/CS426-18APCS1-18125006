package com.example.customui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MyView extends View {

    private ArrayList<My2DSprite> sprites;
    private Timer timer;
    private TimerTask timerTask;

    public MyView(Context context) {
        super(context);
        prepareContent();
    }

    private void prepareContent() {
        sprites = new ArrayList<>();
        CreateIsland(100, 100, R.drawable.starter_island01);
        CreateBuilding(50, 150, R.drawable.kindergarten01);
        for (int i=0; i<1; i++)
            CreateAngel(100+i*30, 100+i*2);

        timerTask = new TimerTask() {
            @Override
            public void run() {
                for (int i=0; i<sprites.size(); ++i) {
                    sprites.get(i).update();
                }
                postInvalidate();
            }
        };

        timer = new Timer();
        timer.schedule(timerTask, 1000, 40);
    }

    private void CreateAngel(int left, int top) {
        Bitmap[] BMPs = new Bitmap[15];
        BMPs[0] = BitmapFactory.decodeResource(getResources(), R.drawable.angel01);
        BMPs[1] = BitmapFactory.decodeResource(getResources(), R.drawable.angel02);
        BMPs[2] = BitmapFactory.decodeResource(getResources(), R.drawable.angel03);
        BMPs[3] = BitmapFactory.decodeResource(getResources(), R.drawable.angel04);
        BMPs[4] = BitmapFactory.decodeResource(getResources(), R.drawable.angel05);
        BMPs[5] = BitmapFactory.decodeResource(getResources(), R.drawable.angel06);
        BMPs[6] = BitmapFactory.decodeResource(getResources(), R.drawable.angel07);
        BMPs[7] = BitmapFactory.decodeResource(getResources(), R.drawable.angel08);
        BMPs[8] = BitmapFactory.decodeResource(getResources(), R.drawable.angel09);
        BMPs[9] = BitmapFactory.decodeResource(getResources(), R.drawable.angel10);
        BMPs[10] = BitmapFactory.decodeResource(getResources(), R.drawable.angel11);
        BMPs[11] = BitmapFactory.decodeResource(getResources(), R.drawable.angel12);
        BMPs[12] = BitmapFactory.decodeResource(getResources(), R.drawable.angel13);
        BMPs[13] = BitmapFactory.decodeResource(getResources(), R.drawable.angel14);
        BMPs[14] = BitmapFactory.decodeResource(getResources(), R.drawable.angel15);
        sprites.add(new My2DSprite(BMPs, left, top, 0, 0));
    }

    private void CreateBuilding(int left, int top, int resourceID) {
        createSpriteWithASingleImage(left, top, resourceID);
    }

    private void createSpriteWithASingleImage(int left, int top, int resourceID) {
        Bitmap[] BMPs = new Bitmap[1];
        BMPs[0] = BitmapFactory.decodeResource(getResources(), resourceID);
        sprites.add(new My2DSprite(BMPs, left, top, 0, 0));
    }

    private void CreateIsland(int left, int top, int resourceID) {
        createSpriteWithASingleImage(left, top, resourceID);
    }

    public MyView(Context context, AttributeSet attributeSet)
    {
        super (context, attributeSet);
        prepareContent();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawARGB(255, 59, 112, 168);
        for (int i=0; i<sprites.size(); ++i)
        {
            sprites.get(i).draw(canvas);
        }
        //super.onDraw(canvas);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int pointerIndex = event.getActionIndex();
        int pointerId = event.getPointerId(pointerIndex);

        int maskedAction = event.getAction();
        int tempIdx;
        float x=event.getX();
        float y=event.getY();

        switch (maskedAction){
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN: {
                // TODO use data
                tempIdx = getSelectedSpriteIndex(x, y);
                if (tempIdx != -1)
                    selectSprite(tempIdx);
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                // TODO use data
                break;
            }
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP: {
                break;
            }
            case MotionEvent.ACTION_CANCEL: {
                // TODO use data
                break;
            }
        }
        return true;
    }

    private void selectSprite(int newIndex) {
        for (int i=0; i<sprites.size(); i++)
            if (i == newIndex)
                sprites.get(i).State = 1;
            else
                sprites.get(i).State = 0;
    }

    private int getSelectedSpriteIndex(float x, float y) {
        int ret = -1;
        for (int i=sprites.size() - 1; i>=0; i++)
            if (sprites.get(i).isSelected(x, y))
                return i;
        return ret;
    }
}
