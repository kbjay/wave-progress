package com.wzq.drawdemo;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

public class MainActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener, View.OnClickListener {

    private WaveView mWaveView;
    private SeekBar mSeekBar;
    private Button mBtChangeBackColor;
    private Button mBtChangeWaveColor;
    private Button mBtChangeWaveHeight;
    private Button mBtChangeWaveWidth;
    private int mBtBackColorClickCount;
    private int mBtWaveColorClickCount;
    private int mBtWaveHeightClickCount;
    private int mBtWaveWidthClickCount;
    //   private int mBtChangeSpeedClickCount;
    private int[] mColors;
    private int[] mSizes;
//    private Button mBtChangeMoveSpeed;
//    private int[] mSpeeds;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mWaveView = (WaveView) this.findViewById(R.id.waveView);
        mSeekBar = (SeekBar) this.findViewById(R.id.seekBar);
        mBtChangeBackColor = (Button) this.findViewById(R.id.bt_changeBackColor);
        mBtChangeWaveColor = (Button) this.findViewById(R.id.bt_changeWaveColor);
        mBtChangeWaveHeight = (Button) this.findViewById(R.id.bt_changeWaveHeight);
        mBtChangeWaveWidth = (Button) this.findViewById(R.id.bt_changeWaveWidth);
        //    mBtChangeMoveSpeed = (Button) this.findViewById(R.id.bt_changeWaveMoveSpeed);
        init();

    }

    private void init() {
        //设置color数组
        mColors = new int[]{Color.RED, Color.GRAY, Color.BLUE, Color.GREEN};
        //设置size数组
        mSizes = new int[]{5, 10, 15, 20, 25, 30, 35, 40, 45};
     /*   //设置speed数组
        mSpeeds = new int[]{1,2,3,4,5,6,7,8,9};*/
        //设置初始状态
        mSeekBar.setMax(100);
        mSeekBar.setProgress(40);
        mWaveView.setProcess(40 / 100.0f);

        mSeekBar.setOnSeekBarChangeListener(this);
        mBtChangeBackColor.setOnClickListener(this);
        mBtChangeWaveColor.setOnClickListener(this);
        mBtChangeWaveHeight.setOnClickListener(this);
        mBtChangeWaveWidth.setOnClickListener(this);
        // mBtChangeMoveSpeed.setOnClickListener(this);
    }

    //seekbar
    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        mWaveView.setProcess(i / 100.0f);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    //button
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_changeBackColor:
                mWaveView.setBackColor(mColors[mBtBackColorClickCount++]);
                if (mBtBackColorClickCount == mColors.length) {
                    mBtBackColorClickCount = 0;
                }
                break;
            case R.id.bt_changeWaveColor:
                mWaveView.setWaveColor(mColors[mBtWaveColorClickCount++]);
                if (mBtWaveColorClickCount == mColors.length) {
                    mBtWaveColorClickCount = 0;
                }
                break;

            case R.id.bt_changeWaveHeight:
                mWaveView.setWaveHeight(mSizes[mBtWaveHeightClickCount++]);
                if (mBtWaveHeightClickCount == mSizes.length) {
                    mBtWaveHeightClickCount = 0;
                }
                break;
            case R.id.bt_changeWaveWidth:
                mWaveView.setWaveWidth(mSizes[mBtWaveWidthClickCount++]);
                if (mBtWaveWidthClickCount == mSizes.length) {
                    mBtWaveWidthClickCount = 0;
                }
                break;

          /*  case R.id.bt_changeWaveMoveSpeed:
                mWaveView.setWaveMoveSpeed(mSpeeds[mBtChangeSpeedClickCount++]);
                if (mBtChangeSpeedClickCount == mSpeeds.length) {
                    mBtChangeSpeedClickCount = 0;
                }
                break;*/
            default:
                break;
        }
    }
}
