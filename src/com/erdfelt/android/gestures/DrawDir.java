package com.erdfelt.android.gestures;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;

import com.erdfelt.android.gestures.nav.Dir;

public class DrawDir {
    public static enum Type {
        DRAG(Color.RED), FLICK(Color.BLUE), CURSOR(Color.MAGENTA);

        private int color;

        private Type(int color) {
            this.color = color;
        }
    }

    private Type  type      = Type.DRAG;
    private Dir   activeDir = Dir.NONE;
    private Path  pathNE;
    private Path  pathN;
    private Path  pathNW;
    private Path  pathW;
    private Path  pathSW;
    private Path  pathS;
    private Path  pathSE;
    private Path  pathE;
    private Paint paint;

    public DrawDir() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.FILL);
    }

    public void updateSize(int width, int height) {
        int midX = width / 2;
        int midY = height / 2;
        
        Path arrow = new Path();
        arrow.moveTo(0, 0);
        arrow.lineTo(60, 20);
        arrow.lineTo(50, 30);
        arrow.lineTo(70, 60);
        arrow.lineTo(60, 70);
        arrow.lineTo(30, 50);
        arrow.lineTo(20, 60);
        arrow.lineTo(0, 0);
        arrow.close();

        Matrix matrix = new Matrix();
        
        pathNW = new Path(arrow);

        matrix.reset();
        matrix.postRotate(45, 0, 0);
        matrix.postTranslate(midX, 0);
        pathN = new Path(arrow);
        pathN.transform(matrix);
        
        matrix.reset();
        matrix.postRotate(90, 0, 0);
        matrix.postTranslate(width, 0);
        pathNE = new Path(arrow);
        pathNE.transform(matrix);
        
        matrix.reset();
        matrix.postRotate(135, 0, 0);
        matrix.postTranslate(width, midY);
        pathE = new Path(arrow);
        pathE.transform(matrix);
        
        matrix.reset();
        matrix.postRotate(180, 0, 0);
        matrix.postTranslate(width, height);
        pathSE = new Path(arrow);
        pathSE.transform(matrix);

        matrix.reset();
        matrix.postRotate(225, 0, 0);
        matrix.postTranslate(midX, height);
        pathS = new Path(arrow);
        pathS.transform(matrix);

        matrix.reset();
        matrix.postRotate(270, 0, 0);
        matrix.postTranslate(0, height);
        pathSW = new Path(arrow);
        pathSW.transform(matrix);

        matrix.reset();
        matrix.postRotate(315, 0, 0);
        matrix.postTranslate(0, midY);
        pathW = new Path(arrow);
        pathW.transform(matrix);
    }

    public void onDraw(Canvas canvas) {
        paint.setColor(type.color);

        switch (activeDir) {
            case EAST:
                canvas.drawPath(pathE, paint);
                break;
            case WEST:
                canvas.drawPath(pathW, paint);
                break;
            case NORTH_EAST:
                canvas.drawPath(pathNE, paint);
                break;
            case NORTH_WEST:
                canvas.drawPath(pathNW, paint);
                break;
            case SOUTH_EAST:
                canvas.drawPath(pathSE, paint);
                break;
            case SOUTH_WEST:
                canvas.drawPath(pathSW, paint);
                break;
            case NORTH:
                canvas.drawPath(pathN, paint);
                break;
            case SOUTH:
                canvas.drawPath(pathS, paint);
                break;
        }
    }

    public void setDir(Type type, Dir dir) {
        this.type = type;
        this.activeDir = dir;
    }

    public void hide() {
        this.activeDir = Dir.NONE;
    }
}
