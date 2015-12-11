package com.cs442.team11.tictactoe;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.EmbossMaskFilter;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by nivedita.kalele on 12/10/2015.
 */
public class GameView extends View {





    private int startX = 0;
    private int startY = 0;
    private int GRID_WIDTH = GlobalData.getWidth()/10;
    private int GRID_MOD = GlobalData.getWidth()%10;
    private int GRID_NUM = 11;
    private Paint paint = null;

    private int[][] chess = new int[GRID_NUM][GRID_NUM];
    private int CHESS_BLACK = 1;
    private int CHESS_WHITE = 2;
    private int chess_flag = 0;

    private int mstartX = 3*GRID_WIDTH + GRID_WIDTH/2;
    private int mstartY = 10*GRID_WIDTH+ GRID_WIDTH/2;
    private int mGRID_NUM = 4;
    private int[][] winplace = new int[GRID_NUM][GRID_NUM];
    private int[][] win = new int[][]{{0,0,0},{0,0,0},{0,0,0}};

    public int whowin=0;
    private int flag_click=0;
    private boolean turn=true;

    public GameView(Context context) {
        super(context);

        paint = new Paint();
        paint.setAntiAlias(true);

        if(GRID_MOD == 0){
            startX = GRID_WIDTH/2;
            startY = GRID_WIDTH/2;
            GRID_NUM--;
        }
        else{
            startX = GRID_MOD/2;
            startY = GRID_MOD/2;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //canvas.drawColor(0xFF7986CB);
        paint.setColor(Color.BLACK);
        for(int i=0;i<GRID_NUM;i++)
        {
            canvas.drawLine(startX, startY+i*GRID_WIDTH,startX+(GRID_NUM-1)*GRID_WIDTH , startY+i*GRID_WIDTH, paint);
            canvas.drawLine(startX+i*GRID_WIDTH, startY,startX+i*GRID_WIDTH , startY+(GRID_NUM-1)*GRID_WIDTH, paint);
        }
        paint.setColor(Color.YELLOW);
        canvas.drawLine(startX + 3 * GRID_WIDTH, startY, startX + 3 * GRID_WIDTH, startY + (GRID_NUM - 1) * GRID_WIDTH, paint);
        canvas.drawLine(startX + 6 * GRID_WIDTH, startY, startX + 6 * GRID_WIDTH, startY + (GRID_NUM - 1) * GRID_WIDTH, paint);
        canvas.drawLine(startX, startY+3*GRID_WIDTH, startX+(GRID_NUM-1)*GRID_WIDTH, startY+3*GRID_WIDTH, paint);
        canvas.drawLine(startX, startY+6*GRID_WIDTH, startX+(GRID_NUM-1)*GRID_WIDTH, startY+6*GRID_WIDTH, paint);

        paint.setColor(Color.BLACK);
        for(int i=0;i<mGRID_NUM;i++)
        {
            canvas.drawLine(mstartX, mstartY+i*GRID_WIDTH,mstartX+(mGRID_NUM-1)*GRID_WIDTH , mstartY+i*GRID_WIDTH, paint);
            canvas.drawLine(mstartX+i*GRID_WIDTH, mstartY,mstartX+i*GRID_WIDTH , mstartY+(mGRID_NUM-1)*GRID_WIDTH, paint);
        }

        float[] dire = new float[]{1,1,1};
        float light = 0.5f;
        float spe = 6;
        float blur = 3.5f;
        EmbossMaskFilter emboss=new EmbossMaskFilter(dire,light,spe,blur);
        paint.setMaskFilter(emboss);

        for(int i=0;i<GRID_NUM;i++)
        {
            for(int j=0;j<GRID_NUM;j++)
            {
                if(chess[i][j] == CHESS_BLACK)
                {
                    paint.setColor(Color.BLUE);
                    paint.setStyle(Paint.Style.STROKE);
                    canvas.drawCircle(startX+i*GRID_WIDTH+GRID_WIDTH/2,startY+j*GRID_WIDTH+GRID_WIDTH/2 ,GRID_WIDTH/2-3, paint);
                }
                if(chess[i][j] == CHESS_WHITE)
                {
                    paint.setColor(Color.GREEN);
                    paint.setStyle(Paint.Style.STROKE);
                    //canvas.drawCircle(startX+i*GRID_WIDTH+GRID_WIDTH/2,startY+j*GRID_WIDTH+GRID_WIDTH/2 ,GRID_WIDTH/2-3, paint);
                    canvas.drawLine(startX+i*GRID_WIDTH, startY+j*GRID_WIDTH, startX+i*GRID_WIDTH+GRID_WIDTH, startY+j*GRID_WIDTH+GRID_WIDTH, paint);
                    canvas.drawLine(startX+i*GRID_WIDTH, startY+j*GRID_WIDTH+GRID_WIDTH, startX+i*GRID_WIDTH+GRID_WIDTH, startY+j*GRID_WIDTH, paint);
                }
            }
        }

        paint.setColor(Color.RED);
        paint.setStrokeWidth(10);
        if(flag1==1) {
            canvas.drawLine(limitX, limitY, limitX + 3 * GRID_WIDTH, limitY, paint);
            canvas.drawLine(limitX, limitY, limitX, limitY + 3 * GRID_WIDTH, paint);
            canvas.drawLine(limitX + 3 * GRID_WIDTH, limitY, limitX + 3 * GRID_WIDTH, limitY + 3 * GRID_WIDTH, paint);
            canvas.drawLine(limitX, limitY + 3 * GRID_WIDTH, limitX + 3 * GRID_WIDTH, limitY + 3 * GRID_WIDTH, paint);
        }
        for(int i=0;i<GRID_NUM;i++) {
            for (int j = 0; j < GRID_NUM; j++) {
                ChectWin();
                if(winplace[i][j]==1) {
                    flag_click=1;
                    win[i][j]=1;
                    paint.setColor(Color.BLUE);
                    paint.setStyle(Paint.Style.STROKE);
                    canvas.drawCircle(mstartX + GRID_WIDTH / 2 + i * GRID_WIDTH, mstartY + GRID_WIDTH / 2 + j * GRID_WIDTH, GRID_WIDTH / 2 - 3, paint);
                    canvas.drawCircle(startX + GRID_WIDTH / 2 + GRID_WIDTH + i * 3 * GRID_WIDTH, startY + GRID_WIDTH + GRID_WIDTH / 2 + j * 3 * GRID_WIDTH, GRID_WIDTH, paint);
                }
                if(winplace[i][j]==2) {
                    flag_click=1;
                    win[i][j]=2;
                    paint.setColor(Color.GREEN);
                    paint.setStyle(Paint.Style.STROKE);
                    canvas.drawLine(mstartX + i * GRID_WIDTH, mstartY + j * GRID_WIDTH, mstartX + i * GRID_WIDTH + GRID_WIDTH, mstartY + j * GRID_WIDTH + GRID_WIDTH, paint);
                    canvas.drawLine(mstartX + i * GRID_WIDTH, mstartY + j * GRID_WIDTH + GRID_WIDTH, mstartX + i * GRID_WIDTH + GRID_WIDTH, mstartY + j * GRID_WIDTH, paint);

                    canvas.drawLine(startX+i*3*GRID_WIDTH, startY+j*3*GRID_WIDTH, startX+i*3*GRID_WIDTH+3*GRID_WIDTH, startY+j*3*GRID_WIDTH+3*GRID_WIDTH, paint);
                    canvas.drawLine(startX+i*3*GRID_WIDTH, startY+j*3*GRID_WIDTH+3*GRID_WIDTH, startX+i*3*GRID_WIDTH+3*GRID_WIDTH, startY+j*3*GRID_WIDTH, paint);
                }
            }
        }
        if(whowin==1){
            paint.setColor(Color.BLUE);
            paint.setStyle(Paint.Style.STROKE);
            canvas.drawCircle(startX + GRID_WIDTH / 2 + 4 * GRID_WIDTH, startY + GRID_WIDTH / 2 + 4 * GRID_WIDTH, GRID_WIDTH / 2+4*GRID_WIDTH, paint);
        }
        if(whowin==2){
            paint.setColor(Color.GREEN);
            paint.setStyle(Paint.Style.STROKE);
        }
        if(turn==true){
            paint.setColor(Color.BLUE);
            paint.setStyle(Paint.Style.STROKE);
            canvas.drawCircle(mstartX + GRID_WIDTH / 2 - 2 * GRID_WIDTH, mstartY + GRID_WIDTH / 2+GRID_WIDTH, GRID_WIDTH, paint);
        }
        if(turn==false){
            paint.setColor(Color.GREEN);
            paint.setStyle(Paint.Style.STROKE);
            canvas.drawLine(mstartX+3*GRID_WIDTH+GRID_WIDTH/2, mstartY+GRID_WIDTH/2, mstartX+3*GRID_WIDTH+3*GRID_WIDTH, mstartY+3*GRID_WIDTH-GRID_WIDTH/2, paint);
            canvas.drawLine(mstartX+3*GRID_WIDTH+GRID_WIDTH/2, mstartY+3*GRID_WIDTH-GRID_WIDTH/2, mstartX+3*GRID_WIDTH+3*GRID_WIDTH, mstartY+GRID_WIDTH/2, paint);
        }


    }
    int flag=1;
    int flag1=0;
    int index_x=0;
    int index_y=0;
    int limitX=0;
    int limitY=0;

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        float touchX = event.getX();
        float touchY = event.getY();
        index_x = Math.round((touchX-startX-GRID_WIDTH/2)/GRID_WIDTH);
        index_y = Math.round((touchY-startY-GRID_WIDTH/2)/GRID_WIDTH);
        turn=!turn;

        if(touchX < startX || touchX>startX+(GRID_NUM-1)*GRID_WIDTH || touchY < startY || touchY>startY+(GRID_NUM-1)*GRID_WIDTH)
        {

        }
        else if(touchX>limitX && touchY>limitY && touchX<limitX+3*GRID_WIDTH && touchY<limitY+3*GRID_WIDTH || flag==1)
        {
            flag=0;
            flag1 = 1;


            if(chess_flag == 0)
            {
                chess[index_x][index_y] = CHESS_BLACK;
                chess_flag = CHESS_BLACK;

            }else if( chess_flag == CHESS_BLACK && chess[index_x][index_y] == 0)
            {
                chess[index_x][index_y] = CHESS_WHITE;
                chess_flag = CHESS_WHITE;
            }else if(chess_flag == CHESS_WHITE && chess[index_x][index_y] == 0)
            {
                chess[index_x][index_y] = CHESS_BLACK;
                chess_flag = CHESS_BLACK;
            }
        }
        if(flag_click==1){
            if((touchX > startX || touchX<startX+(GRID_NUM-1)*GRID_WIDTH || touchY > startY || touchY<startY+(GRID_NUM-1)*GRID_WIDTH)) {
                flag = 0;
                flag1 = 1;

                if (chess_flag == 0) {
                    chess[index_x][index_y] = CHESS_BLACK;
                    chess_flag = CHESS_BLACK;

                } else if (chess_flag == CHESS_BLACK && chess[index_x][index_y] == 0) {
                    chess[index_x][index_y] = CHESS_WHITE;
                    chess_flag = CHESS_WHITE;
                } else if (chess_flag == CHESS_WHITE && chess[index_x][index_y] == 0) {
                    chess[index_x][index_y] = CHESS_BLACK;
                    chess_flag = CHESS_BLACK;
                }
            }
        }
        switch(index_x%3){
            case 0: limitX = startX; break;
            case 1: limitX = startX+3*GRID_WIDTH; break;
            case 2: limitX = startX+6*GRID_WIDTH; break;
        }
        switch(index_y%3){
            case 0: limitY = startY; break;
            case 1: limitY = startY+3*GRID_WIDTH; break;
            case 2: limitY = startY+6*GRID_WIDTH; break;
        }
        Log.d("checkwin", "click");



        ChectWin();
        invalidate();
        return super.onTouchEvent(event);
    }

    public void ChectWin(){
        for(int i=0;i<=2;i++){
            for(int j=0;j<=2;j++){
                if(chess[i*3][j*3]==1 && chess[i*3][j*3+1]==1 && chess[i*3][j*3+2]==1)
                {winplace[i][j]=1; }
                if(chess[i*3+1][j*3]==1 && chess[i*3+1][j*3+1]==1 && chess[i*3+1][j*3+2]==1)
                {winplace[i][j]=1; }
                if(chess[i*3+2][j*3]==1 && chess[i*3+2][j*3+1]==1 && chess[i*3+2][j*3+2]==1)
                {winplace[i][j]=1; }
                if(chess[i*3][j*3]==1 && chess[i*3+1][j*3]==1 && chess[i*3+2][j*3]==1)
                {winplace[i][j]=1; }
                if(chess[i*3][j*3+1]==1 && chess[i*3+1][j*3+1]==1 && chess[i*3+2][j*3+1]==1)
                {winplace[i][j]=1; }
                if(chess[i*3][j*3+2]==1 && chess[i*3+1][j*3+2]==1 && chess[i*3+2][j*3+2]==1)
                {winplace[i][j]=1; }
                if(chess[i*3][j*3]==1 && chess[i*3+1][j*3+1]==1 && chess[i*3+2][j*3+2]==1)
                {winplace[i][j]=1; }
                if(chess[i*3][j*3+2]==1 && chess[i*3+1][j*3+1]==1 && chess[i*3+2][j*3]==1)
                {winplace[i][j]=1; }
            }
        }
        for(int i=0;i<=2;i++){
            for(int j=0;j<=2;j++){
                if(chess[i*3][j*3]==2 && chess[i*3][j*3+1]==2 && chess[i*3][j*3+2]==2)
                {winplace[i][j]=2; }
                if(chess[i*3+1][j*3]==2 && chess[i*3+1][j*3+1]==2 && chess[i*3+1][j*3+2]==2)
                {winplace[i][j]=2; }
                if(chess[i*3+2][j*3]==2 && chess[i*3+2][j*3+1]==2 && chess[i*3+2][j*3+2]==2)
                {winplace[i][j]=2; }
                if(chess[i*3][j*3]==2 && chess[i*3+1][j*3]==2 && chess[i*3+2][j*3]==2)
                {winplace[i][j]=2; }
                if(chess[i*3][j*3+1]==2 && chess[i*3+1][j*3+1]==2 && chess[i*3+2][j*3+1]==2)
                {winplace[i][j]=2; }
                if(chess[i*3][j*3+2]==2 && chess[i*3+1][j*3+2]==2 && chess[i*3+2][j*3+2]==2)
                {winplace[i][j]=2; }
                if(chess[i*3][j*3]==2 && chess[i*3+1][j*3+1]==2 && chess[i*3+2][j*3+2]==2)
                {winplace[i][j]=2; }
                if(chess[i*3][j*3+2]==2 && chess[i*3+1][j*3+1]==2 && chess[i*3+2][j*3]==2)
                {winplace[i][j]=2; }
            }
        }
        if(win[0][0]==1 && win[0][0]==1 && win[0][0]==1)
        {whowin=1; }
        if(win[1][0]==1 && win[1][1]==1 && win[1][2]==1)
        {whowin=1; }
        if(win[2][0]==1 && win[2][1]==1 && win[2][2]==1)
        {whowin=1; }
        if(win[0][0]==1 && win[1][0]==1 && win[2][0]==1)
        {whowin=1; }
        if(win[0][1]==1 && win[1][1]==1 && win[2][1]==1)
        {whowin=1; }
        if(win[0][2]==1 && win[1][2]==1 && win[2][2]==1)
        {whowin=1; }
        if(win[0][0]==1 && win[1][1]==1 && win[2][2]==1)
        {whowin=1; }
        if(win[0][2]==1 && win[1][1]==1 && win[2][0]==1)
        {whowin=1; }

        if(win[0][0]==2 && win[0][0]==2 && win[0][0]==2)
        {whowin=2; }
        if(win[1][0]==2 && win[1][1]==2 && win[1][2]==2)
        {whowin=2; }
        if(win[2][0]==2 && win[2][1]==2 && win[2][2]==2)
        {whowin=2; }
        if(win[0][0]==2 && win[1][0]==2 && win[2][0]==2)
        {whowin=2; }
        if(win[0][1]==2 && win[1][1]==2 && win[2][1]==2)
        {whowin=2; }
        if(win[0][2]==2 && win[1][2]==2 && win[2][2]==2)
        {whowin=2; }
        if(win[0][0]==2 && win[1][1]==2 && win[2][2]==2)
        {whowin=2; }
        if(win[0][2]==2 && win[1][1]==2 && win[2][0]==2)
        {whowin=2; }
    }

}
