package com.wzq.drawdemo;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;

import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.View;

//需求：自定义一个waveView
//属性：process  （波纹的高度）
//      CIRCLE_COLOR_DEFAULT    （圆形颜色）
//      WAVE_COLOR_DEFAULT     （波纹颜色）
//      waveDegree  (波纹的浮动明显度)
//      waveWidth    (波纹周期)
//      waveHeight   （波纹振幅）
public class WaveView extends View {

    public static final boolean Debug = true;//debug 开关

    private static final float PROCESS_DEFAULT = 0.5F;
    private static final int DEFAULT_SIZE = 200;//默认高度
    private static final int WAVE_COLOR_DEFAULT = Color.parseColor("#ffff0000");
    private static final int CIRCLE_COLOR_DEFAULT = Color.parseColor("#ff00ff00");
    private static final int WAVE_WIDTH_DEFAULT = 40;
    private static final int WAVE_HEIGHT_DEFAULT = 10;
   // private static final int WAVE_MOVE_SPEED_DEFAULT = 10;

    private int mWaveColor;
    private int mCircleColor;
    private float mProcess;
    private int mSize;
    private Paint mWavePaint;
    private Paint mCirclePaint;
    private int[] mPosition_y;
    private int mOffset;
    private int mWaveHeight;
    private int mWaveWidth;
    private Paint mPaint;
    private int mWaveMoveSpeed;


    public WaveView(Context context) {
        this(context, null);
    }

    public WaveView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        //get attrs
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.WaveView);
        mWaveColor = typedArray.getColor(R.styleable.WaveView_waveColor, WAVE_COLOR_DEFAULT);
        mCircleColor = typedArray.getColor(R.styleable.WaveView_circleColor, CIRCLE_COLOR_DEFAULT);
        mProcess = typedArray.getFloat(R.styleable.WaveView_process, PROCESS_DEFAULT);
        mWaveHeight = typedArray.getDimensionPixelOffset(R.styleable.WaveView_waveHeight, WAVE_HEIGHT_DEFAULT);
        mWaveWidth = typedArray.getDimensionPixelOffset(R.styleable.WaveView_waveWidth, WAVE_WIDTH_DEFAULT);
       // mWaveMoveSpeed=typedArray.getInteger(R.styleable.WaveView_waveMoveSpeed,WAVE_MOVE_SPEED_DEFAULT);
        if (Debug) {
            System.out.println(mWaveHeight + "==" + mWaveWidth);
        }
        initPaint(context);
    }

    // paint 的初始化工作
    private void initPaint(Context context) {
        mWavePaint = new Paint();
        mWavePaint.setAntiAlias(true);
        mWavePaint.setStrokeWidth(2);

        mCirclePaint = new Paint();
        mCirclePaint.setAntiAlias(true);

    }

    //1：处理wrap_content的情况
    //2:处理width！=height的情况
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        switch (widthMode) {
            case MeasureSpec.AT_MOST:
                //Wrap_content
                width = DEFAULT_SIZE;
                break;
            case MeasureSpec.EXACTLY:
                //match_parent  和 xxdp  不处理
                break;
            default:
                break;
        }
        switch (heightMode) {
            case MeasureSpec.AT_MOST:
                height = DEFAULT_SIZE;
                break;
            case MeasureSpec.EXACTLY:
                break;
            default:
                break;
        }
        //处理width！=height的情况
        int size = (width > height) ? width : height;

        if (Debug) {
            System.out.println(size + "==size");
        }
        setMeasuredDimension(size, size);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        //
        if (Debug) {
            System.out.println("onSizeChanged:" + w + "--" + h + "****old:" + oldw + "--" + oldh);
        }
        super.onSizeChanged(w, h, oldw, oldh);
        mSize = w;
        //初始化y坐标的数组
        mPosition_y = new int[mSize + 1];
        for (int i = 0; i <= mSize; i++) {
            mPosition_y[i] = (int) (mSize - ((mWaveHeight / 2.0) * Math.sin((2 * Math.PI) / mWaveWidth * i) + (mProcess * mSize) + 0.5f));
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //为了达到动态效果：绘制完成，需要重置y坐标数组，并再次触发onDraw
        doDraw(canvas);
        resetY();
        postInvalidate();
    }

    private void doDraw(Canvas canvas) {
        //new  pictureLayer    使用PorterDuffXferMode的关键
        int layerId = canvas.saveLayer(0f, 0f, canvas.getWidth(), canvas.getHeight(), null, Canvas.ALL_SAVE_FLAG);
        //绘制圆
        mCirclePaint.setColor(mCircleColor);
        canvas.drawCircle(mSize / 2.0f, mSize / 2.0f, mSize / 2.0f, mCirclePaint);
        //绘制wave   如果process为0.95以上或0.05以下(做一个误差处理)
        mWavePaint.setColor(mWaveColor);
        mWavePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));
        if (mProcess > 0.95) {   //绘制wave圆
            canvas.drawCircle(mSize / 2.0f, mSize / 2.0f, mSize / 2.0f, mWavePaint);
        } else if (mProcess > 0.05) {//绘制wave
            for (int i = 0; i <= mSize; i++) {
                canvas.drawLine(i, mPosition_y[i], i, mSize, mWavePaint);
            }
        }else {
            //do nothing //什么也不绘制
        }
        mWavePaint.setXfermode(null);
        //将图层覆盖到canvas上
        canvas.restoreToCount(layerId);
    }

    //重置y坐标数组
    private void resetY() {
        if (mOffset > 1000) {
            mOffset = 0;
        }

        mOffset += 50;//修改波纹的移速

        for (int i = 0; i < mSize; i++) {
            mPosition_y[i] = (int) (mSize - ((mWaveHeight / 2.0) * Math.sin((2 * Math.PI) / mWaveWidth * i + mOffset) + (mProcess * mSize) + 0.5f));
        }
    }

    //提供set进度的method
    public void setProcess(float process) {
        if (process > 1 || process < 0) {
            return;
        }
        //获取进度，刷新
        mProcess = process;
        postInvalidate();
    }

    public void setBackColor(int mColor) {
        mCircleColor = mColor;
        postInvalidate();
    }

    public void setWaveColor(int mColor) {
        mWaveColor = mColor;
        postInvalidate();
    }

    public void setWaveHeight(int waveHeight) {
        mWaveHeight = waveHeight;
        postInvalidate();
    }

    public void setWaveWidth(int waveWidth) {
        mWaveWidth = waveWidth;
        postInvalidate();
    }

    public void setWaveMoveSpeed(int speed){
        mWaveMoveSpeed=speed;
    }
}
