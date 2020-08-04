package com.onegold.maskchecker;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import com.google.mlkit.vision.face.Face;

import java.util.List;

public class DrawView extends View {
    private Bitmap cacheBitmap;
    private Canvas canvas;
    private Paint paint;

    public interface FaceDetector {
        void drawFaceRect(List<Face> faces, float widthRatio, float heightRatio);
    }

    public DrawView(Context context) {
        super(context);
        init(context);
    }

    public DrawView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStrokeWidth(10);
        paint.setStyle(Paint.Style.STROKE);
    }

    public void drawRect() {
        if (canvas != null) {
            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            canvas.drawRect(191, 340, 702, 851, paint);
            canvas.drawRect(100, 100, 200, 200, paint);
        }
    }

    public void drawFaceRect(List<Face> faces, float widthRatio, float heightRatio) {
        if (canvas == null)
            return;

        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        if (faces != null) {
            for (Face face : faces) {
                Rect bounds = face.getBoundingBox();
                float rotY = face.getHeadEulerAngleY();  // Head is rotated to the right rotY degrees
                float rotZ = face.getHeadEulerAngleZ();  // Head is tilted sideways rotZ degrees

                float left = bounds.left * widthRatio;
                float top = bounds.top * heightRatio;
                float right = bounds.right * widthRatio;
                float bottom = bounds.bottom * heightRatio;
                Log.d("############", "b" + bounds.bottom + "l" + bounds.left + "r" + bounds.right + "t" + bounds.top);
                Log.d("!!!!!!!!!!!!", "b" + bottom + "l" + left + "r" + right + "t" + top);

                canvas.drawRect(left, top, right, bottom, paint);
            }
            invalidate();
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        cacheBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        canvas = new Canvas();
        canvas.setBitmap(cacheBitmap);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (cacheBitmap != null && canvas != null) {
            canvas.drawBitmap(cacheBitmap, 0, 0, null);
        }
    }
}