package com.example.xjc.work2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class Chess extends View {
    private static final String TAG = "Chess";
    private Paint mPaint;
    private int w;//窗口宽
    private int oy = 30;//原点y
    private int ox = 20;//原点x
    private float grid = 15;//格子
    private int pW = 2;//画笔宽
    private int[][] flag;
    private float gW; //每个格子的宽
    private float gH; //高
    private Bitmap black, white;
    private Rect rectBlack, rectWhite;
    private boolean curr = false;//当前下子，false白，true黑
    private boolean isOver = false;//是否结束
    /*初始化接口变量*/
    private CallBack callBack = null;

    public Chess(Context context) {
        this(context, null);
    }

    public Chess(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);//抗锯齿
        mPaint.setStrokeWidth(pW);

        black = BitmapFactory.decodeResource(getResources(), R.mipmap.black);
        white = BitmapFactory.decodeResource(getResources(), R.mipmap.white);
        rectBlack = new Rect(0, 0, black.getWidth(), black.getHeight());
        rectWhite = new Rect(0, 0, white.getWidth(), white.getHeight());

        flag = new int[(int) grid][(int) grid];
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        w = getWidth();
        gW = (w - 2 * ox) / grid;
        gH = (w - 2 * oy) / grid;
        callBack.curr(curr);
        chessBoard(canvas);
        chessPieces(canvas);
    }

    /**
     * 清空棋盘，重新开始
     */
    public void clearChess() {
        flag = new int[(int) grid][(int) grid];
        isOver = false;//是否结束

        invalidate();
    }

    /**
     * 棋盘
     *
     * @param canvas
     */
    private void chessBoard(Canvas canvas) {
        canvas.drawColor(getResources().getColor(R.color.chessBg));   //设置画布背景颜色
        float x, y;
        for (int i = 0; i <= grid; i++) {
            x = i * gW;
            y = i * gH;
            canvas.drawLine(ox, oy + y, w - ox, oy + y, mPaint);//横
            canvas.drawLine(ox + x, oy, ox + x, w - oy, mPaint);//竖
        }
//        canvas.drawLine(ox, oy , w - ox, oy , mPaint);//横
//        canvas.drawLine(ox, w-oy, w - ox, w-oy , mPaint);//横
//        canvas.drawLine(ox , oy, ox , w-oy, mPaint);//竖
//        canvas.drawLine(w-ox , oy, w-ox, w-oy, mPaint);//竖
    }

    /**
     * 棋子
     */
    private void chessPieces(Canvas canvas) {
        for (int i = 0; i < flag.length; i++) {
            for (int j = 0; j < flag[i].length; j++) {
                switch (flag[i][j]) {
                    case 0:
                        continue;
                    case 1:
                        drawChess(canvas, i, j, 1);
                        break;
                    case 2:
                        drawChess(canvas, i, j, 2);
                        break;
                }
            }
        }
    }

    /**
     * @param canvas
     * @param x      格子位置
     * @param y
     * @param type   1 黑子 2白
     */
    private void drawChess(Canvas canvas, int x, int y, int type) {
        if (type == 1) {
            canvas.drawBitmap(black, rectBlack, new Rect(ox + (int) (x * gW), oy + (int) (y * gH), (int) gW + ox + (int) (x * gW), (int) gH + oy + (int) (y * gH)), mPaint);
        } else {
            canvas.drawBitmap(white, rectWhite, new Rect(ox + (int) (x * gW), oy + (int) (y * gH), (int) gW + ox + (int) (x * gW), (int) gH + oy + (int) (y * gH)), mPaint);
        }
        if (!isOver && isVictory(x, y, type)) {
            isOver = true;
            callBack.over(type);
        }
    }

    /*自定义事件*/
    public void setOnOver(CallBack callBack) {
        this.callBack = callBack;
    }

    private boolean isVictory(int x, int y, int type) {
        boolean[] b = new boolean[8];
        int[] sum = new int[4];
        for (int i = 1; i < 5; i++) {
            if (!b[0] && (x + i) < grid && flag[x + i][y] == type) {//右
                sum[0]++;
            } else {
                b[0] = true;
            }
            if (!b[1] && (x - i) >= 0 && flag[x - i][y] == type) {//左
                sum[0]++;
            } else {
                b[1] = true;
            }

            if (!b[2] && (y + i) < grid && flag[x][y + i] == type) {//下
                sum[1]++;
            } else {
                b[2] = true;
            }
            if (!b[3] && (y - i) >= 0 && flag[x][y - i] == type) {//上
                sum[1]++;
            } else {
                b[3] = true;
            }
            if (!b[4] && (x + i) < grid && (y + i) < grid && flag[x + i][y + i] == type) {//右下斜
                sum[2]++;
            } else {
                b[4] = true;
            }
            if (!b[5] && (y - i) >= 0 && (x - i) >= 0 && flag[x - i][y - i] == type) {//左上斜
                sum[2]++;
            } else {
                b[5] = true;
            }
            if (!b[6] && (x - i) >= 0 && (y + i) < grid && flag[x - i][y + i] == type) {//左下斜
                sum[3]++;
            } else {
                b[6] = true;
            }
            if (!b[7] && (y - i) >= 0 && (x + i) < grid && flag[x + i][y - i] == type) {//右上斜
                sum[3]++;
            } else {
                b[7] = true;
            }
        }
        for (int i = 0; i < sum.length; i++) {
            if (sum[i] >= 4) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isOver) {//是否结束
            return super.onTouchEvent(event);
        }
        float x, y;
        x = event.getX() - ox;
        y = event.getY() - oy;
        int gx, gy;
        gx = (int) (x / gW);
        gy = (int) (y / gH);
        if (x > 0 && y > 0 && gx < 15 && gy < 15) {
            if (flag[gx][gy] == 0) {
                if (curr) {
                    flag[gx][gy] = 1;
                    curr = false;
                } else {
                    flag[gx][gy] = 2;
                    curr = true;
                }
                invalidate();
            }
        }
        return super.onTouchEvent(event);
    }

    public interface CallBack {
        /**
         * 胜利 1 黑子 2白
         *
         * @param type
         */
        void over(int type);

        /**
         * 当前下子，false白，true黑
         *
         * @param curr
         */
        void curr(boolean curr);
    }

}
