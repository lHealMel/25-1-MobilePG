package com.example.hello;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.*;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.*;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MyDrawEx extends View {
    private Paint mPaint;
    private Bitmap mAndroidGreen;
    private Bitmap mAndroidRed;
    private int nAngle = 0;

    public void init() {
        mPaint = new Paint();

        Resources res = getResources();
        mAndroidGreen = BitmapFactory.decodeResource(res, R.drawable.android_green);
        mAndroidRed = BitmapFactory.decodeResource(res, R.drawable.android_red);

    }

    public MyDrawEx(Context c) {
        super(c);
        init();
    }

    public MyDrawEx(Context c, AttributeSet a) {
        super(c, a);
        init();
    }

    public boolean onTouchEvent(MotionEvent event) {
// if it's an up (“release”) action
        if (event.getAction() == MotionEvent.ACTION_UP) {
            nAngle += 5; // rotate 5 degree angle upon a click
            invalidate();
        }
// indicates that the event was handled
        return true;
    } // end of onTouchEvent

    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(mAndroidGreen, 0, 0, mPaint);
        canvas.save(); // Save the current state
        canvas.rotate(nAngle);
        canvas.drawBitmap(mAndroidRed, 0, 0, mPaint);
    }
}
