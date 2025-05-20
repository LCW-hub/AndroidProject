package com.example.gps;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class JoystickView extends View {
    private float centerX, centerY, baseRadius, hatRadius;
    private float touchX = 0, touchY = 0;
    private Paint basePaint, hatPaint;
    private JoystickListener joystickCallback;

    public interface JoystickListener {
        void onJoystickMoved(float xPercent, float yPercent);
    }

    public JoystickView(Context context, AttributeSet attrs) {
        super(context, attrs);
        basePaint = new Paint();
        basePaint.setColor(Color.parseColor("#AA000000"));
        basePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        basePaint.setStrokeWidth(5);

        hatPaint = new Paint();
        hatPaint.setColor(Color.parseColor("#FF4081"));
        hatPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        hatPaint.setStrokeWidth(5);

        if (context instanceof JoystickListener)
            joystickCallback = (JoystickListener) context;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.TRANSPARENT);
        centerX = getWidth() / 2f;
        centerY = getHeight() / 2f;
        baseRadius = Math.min(getWidth(), getHeight()) / 3f;
        hatRadius = Math.min(getWidth(), getHeight()) / 5f;

        canvas.drawCircle(centerX, centerY, baseRadius, basePaint);
        float hatX = centerX + (touchX * baseRadius);
        float hatY = centerY + (touchY * baseRadius);
        canvas.drawCircle(hatX, hatY, hatRadius, hatPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float dx = event.getX() - centerX;
        float dy = event.getY() - centerY;
        float displacement = (float) Math.sqrt(dx * dx + dy * dy);

        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_DOWN:
                if (displacement < baseRadius) {
                    touchX = dx / baseRadius;
                    touchY = dy / baseRadius;
                } else {
                    float ratio = baseRadius / displacement;
                    touchX = (dx * ratio) / baseRadius;
                    touchY = (dy * ratio) / baseRadius;
                }

                if (joystickCallback != null) {
                    joystickCallback.onJoystickMoved(touchX, touchY);
                }
                break;

            case MotionEvent.ACTION_UP:
                // 손을 뗐을 때 중심으로 초기화
            touchX = 0;
            touchY = 0;
                if (joystickCallback != null) {
                    joystickCallback.onJoystickMoved(0, 0);
                }
                break;
        }

        invalidate();
        return true;
    }

}
