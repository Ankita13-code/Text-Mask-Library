package com.example.android.textfilllibrary;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;



public class TextFill extends AppCompatTextView {

    private Bitmap maskBitmap, backgroundBitmap;
    private Canvas maskCanvas, backgroundCanvas;
    private Paint paint;
    private Drawable backgroundImg;



    public TextFill(Context context) {
        super(context);
    }

    public TextFill(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    private void initialize() {
        paint = new Paint();
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
        super.setTextColor(Color.BLACK);
        super.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    @Override
    public void setBackgroundDrawable(final Drawable bg) {
        if (backgroundImg == bg) {
            return;
        }

        backgroundImg = bg;

        int bwidth = getWidth();
        int bheight = getHeight();
        if (backgroundImg != null && bwidth != 0 && bheight != 0) {
            backgroundImg.setBounds(0, 0, bwidth, bheight);
        }
        requestLayout();
        invalidate();
    }

    @Override
    public void setBackgroundColor(final int color) {
        setBackgroundDrawable(new ColorDrawable(color));
    }

    @Override
    protected void onSizeChanged(final int initialWidth, final int initialHeight, final int oldWidth, final int oldHeight) {
        super.onSizeChanged(initialWidth, initialHeight, oldWidth, oldHeight);
        if (initialWidth == 0 || initialHeight == 0) {
            clearBitmaps();
            return;
        }

        createBitmaps(initialWidth, initialHeight);
        if (backgroundImg != null) {
            backgroundImg.setBounds(0, 0, initialWidth, initialHeight);
        }
    }

    private void createBitmaps(int width, int height) {
        backgroundBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        backgroundCanvas = new Canvas(backgroundBitmap);
        maskBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ALPHA_8);
        maskCanvas = new Canvas(maskBitmap);
    }

    private void clearBitmaps() {
        backgroundBitmap = null;
        backgroundCanvas = null;
        maskBitmap = null;
        maskCanvas = null;
    }

    @Override
    protected void onDraw(final Canvas canvas) {
        if (backgroundImg == null
                || getWidth() == 0
                || getHeight() == 0) {
            return;
        }

        clear(maskCanvas);
        clear(backgroundCanvas);

        super.onDraw(maskCanvas);
        backgroundImg.draw(backgroundCanvas);
        backgroundCanvas.drawBitmap(maskBitmap, 0.f, 0.f, paint);

        canvas.drawBitmap(backgroundBitmap, 0.f, 0.f, null);
    }


    private static void clear(Canvas canvas) {
        canvas.drawColor(Color.BLACK, PorterDuff.Mode.CLEAR);
    }

   }
