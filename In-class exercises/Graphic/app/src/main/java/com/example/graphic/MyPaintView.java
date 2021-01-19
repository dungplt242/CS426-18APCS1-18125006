package com.example.graphic;

import android.content.ContentUris;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class MyPaintView extends View {
    private int iTool = 0;

    public MyPaintView(Context context) {
        super(context);
    }

    public MyPaintView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    private ArrayList<Shape> shapes = new ArrayList<>();
    private boolean bDraw = false;

    @Override
    protected void onDraw(Canvas canvas) {
        if (backgroundWithShapes != null) {
            canvas.drawBitmap(backgroundWithShapes, 0, 0, null);
            if (bDraw)
                shapes.get(shapes.size()-1).draw(canvas);
        }
        else {
            canvas.drawARGB(255, 255, 255, 255);
            for (int i = 0; i < shapes.size(); i++)
                shapes.get(i).draw(canvas);
        }
        //super.onDraw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //return super.onTouchEvent(event);
        // get pointer index from the event object
        int pointerIndex = event.getActionIndex();

        // get pointer ID
        int pointerId = event.getPointerId(pointerIndex);

        // get masked (not specific to a pointer) action
        int maskedAction = event.getActionMasked();
        float x = event.getX();
        float y = event.getY();

        switch (maskedAction) {

            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN: {
                // TODO use data
                beginDraw(x, y);
                break;
            }
            case MotionEvent.ACTION_MOVE: { // a pointer was moved
                // TODO use data
                if (bDraw)
                    processDraw(x, y);
                break;
            }
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP: {
                if (bDraw)
                    endDraw(x, y);
                break;
            }
            case MotionEvent.ACTION_CANCEL: {
                // TODO use data
                break;
            }
        }

        return true;
    }

    private void endDraw(float x, float y) {
        bDraw = false;
        processDraw(x, y);
        createBitmapOfCurrentShapes();
    }

    private void processDraw(float x, float y) {
        Shape curShape = shapes.get(shapes.size()-1);
        curShape.P2 = new Point((int)x, (int)y);
        invalidate();
    }

    private void beginDraw(float x, float y) {
        bDraw = true;

        createBitmapOfCurrentShapes();

        Shape newShape = null;
        if (iTool == 0) newShape = new MyLine();
        else if (iTool == 1) newShape = new MyRectangle();
        newShape.P1 = new Point((int)x, (int)y);
        newShape.P2 = new Point((int)x, (int)y);
        newShape.penColor = GlobalSetting.SelectedColor;
        shapes.add(newShape);
        invalidate();
    }

    Bitmap backgroundWithShapes = null;

    private void createBitmapOfCurrentShapes() {
        if (backgroundWithShapes == null)
            backgroundWithShapes = Bitmap.createBitmap(getCurrentScreenWidth(), getCurrentScreenHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(backgroundWithShapes);
        canvas.drawARGB(255,255,255,255);
        for (int i=0;i<shapes.size();i++) {
            shapes.get(i).draw(canvas);
        }
    }

    private int getCurrentScreenHeight() {
        return 1000;
    }

    private int getCurrentScreenWidth() {
        return 1000;
    }

    public void selectShape(int i) {
        iTool = i;
    }


}
