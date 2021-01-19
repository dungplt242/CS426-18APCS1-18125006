package com.example.graphic;

import android.graphics.Canvas;
import android.graphics.Paint;

public class MyRectangle extends Shape {
    @Override
    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(penColor);
        paint.setStrokeWidth(30);
        canvas.drawRect(P1.x, P1.y, P2.x, P2.y, paint);

    }
}
