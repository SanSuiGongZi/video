package com.example.videodemo;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private CustomVideoView videoView;
    private ImageView operation_bg;
    private ImageView operation_percent;
    private SeekBar play_seek;
    private ImageView pause_img;
    private TextView time_current_tv;
    private TextView time_total_tv;
    private LinearLayout left_layout;
    private ImageView volume_img;
    private SeekBar volume_seek;
    private ImageView screen_img;
    private LinearLayout controllerbar_layout;
    private RelativeLayout videoLayout;
    private AudioManager mAudioManager;

    public static final int UPDATE_UI = 1;
    private boolean isAdjust = false;
    private int threshold = 108;
    private LinearLayout progress_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        initView();

        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "ht.mp4";

        videoView.setVideoPath(path);
        videoView.start();

        UIhandler.sendEmptyMessage(UPDATE_UI);

    }

    private void initView() {
        videoView = (CustomVideoView) findViewById(R.id.video);
        operation_bg = (ImageView) findViewById(R.id.operation_bg);
        operation_percent = (ImageView) findViewById(R.id.operation_percent);
        play_seek = (SeekBar) findViewById(R.id.play_seek);
        pause_img = (ImageView) findViewById(R.id.pause_img);
        time_current_tv = (TextView) findViewById(R.id.time_current_tv);
        time_total_tv = (TextView) findViewById(R.id.time_total_tv);
        left_layout = (LinearLayout) findViewById(R.id.left_layout);
        volume_img = (ImageView) findViewById(R.id.volume_img);
        volume_seek = (SeekBar) findViewById(R.id.volume_seek);
        screen_img = (ImageView) findViewById(R.id.screen_img);
        controllerbar_layout = (LinearLayout) findViewById(R.id.controllerbar_layout);
        videoLayout = (RelativeLayout) findViewById(R.id.videoLayout);
        progress_layout = (LinearLayout) findViewById(R.id.progress_layout);

        /*
         * 当前设备的最大音量
         */
        int streamMaxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        /*
         * 获取设备当前的音量
         */
        int streamVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        volume_seek.setMax(streamMaxVolume);
        volume_seek.setProgress(streamVolume);


    }

    private void updateTextViewWithTimeFormat(TextView textView, int millisecond) {

        int second = millisecond / 1000;
        int hh = second / 3600;
        int mm = second % 3600 / 60;
        int ss = second % 60;
        String str = null;
        if (hh != 0) {
            str = String.format("%02d:%02d:%02d", hh, mm, ss);
        } else {
            str = String.format("%02d:%02d", mm, ss);
        }
        textView.setText(str);
    }

    private Handler UIhandler = new Handler() {

        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (msg.what == UPDATE_UI) {
                // 获取视频当前的播放时间
                int currentPosition = videoView.getCurrentPosition();
                // 获取视频播放的总时间
                int totalDuration = videoView.getDuration();

                // 格式化视频播放时间
                updateTextViewWithTimeFormat(time_current_tv, currentPosition);
                updateTextViewWithTimeFormat(time_total_tv, totalDuration);

                play_seek.setMax(totalDuration);
                play_seek.setProgress(currentPosition);

                UIhandler.sendEmptyMessageDelayed(UPDATE_UI, 300);
            }
        }

        ;
    };

    private void setPlayerEvent() {

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                // TODO Auto-generated method stub
                pause_img.setImageResource(R.drawable.play_btn_style);

            }
        });

        /*
         * 控制视频的暂停与播放
         */
        pause_img.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (videoView.isPlaying()) {
                    pause_img.setImageResource(R.drawable.play_btn_style);
                    // 暂停播放
                    videoView.pause();
                    UIhandler.removeMessages(UPDATE_UI);
                } else {
                    pause_img.setImageResource(R.drawable.pause_btn_style);
                    // 继续播放
                    videoView.start();
                    UIhandler.sendEmptyMessage(UPDATE_UI);
                }
            }
        });

        play_seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
                int progress = seekBar.getProgress();
                // 令视频播放进度遵循seekBar停止拖动这一刻的进度
                videoView.seekTo(progress);
                pause_img.setImageResource(R.drawable.pause_btn_style);
                videoView.start();
                UIhandler.sendEmptyMessage(UPDATE_UI);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
                UIhandler.removeMessages(UPDATE_UI);
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // TODO Auto-generated method stub
                updateTextViewWithTimeFormat(time_current_tv, progress);
            }
        });

        volume_seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // TODO Auto-generated method stub
                /*
                 * 设置当前设备的的音量
                 */
                mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
            }
        });

        //设置转换屏幕方向的点击事件：
        screen_img.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (isFullScreen) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    screen_img.setImageResource(R.drawable.zuidahua_2x);
                } else {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    screen_img.setImageResource(R.drawable.xiaohua_2x);
                }
            }
        });



    }

     /*videoView.setOnTouchListener(new View.OnTouchListener() {

        private float lastX;
        private float lastY;
        private float x;
        private float y;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            // TODO Auto-generated method stub
            if (isFullScreen)
                switch (event.getAction()) {

                    // 手指落下屏幕的那一刻，只会调用一次
                    case MotionEvent.ACTION_DOWN: {
                        x = event.getRawX();
                        y = event.getRawY();
                        lastX = x;
                        lastY = y;
                        break;
                    }
                    // 手指在屏幕上移动，调用多次
                    case MotionEvent.ACTION_MOVE: {
                        x = event.getRawX();
                        y = event.getRawY();
                        float detlaX = x - lastX;
                        float detlaY = y - lastY;
                        float absdetlaX = Math.abs(detlaX);
                        float absdetlaY = Math.abs(detlaY);
                        if (absdetlaX > threshold && absdetlaY > threshold) {
                            if (absdetlaX < absdetlaY) {
                                isAdjust = true;
                            } else {
                                isAdjust = false;
                            }
                        } else if (absdetlaX < threshold && absdetlaY > threshold) {
                            isAdjust = true;
                        } else if (absdetlaX > threshold && absdetlaY < threshold) {
                            isAdjust = false;
                        }

                        if (isAdjust) {
                            *//*
                             * 在判断好当前手势事件已经合法的前提下，去区分此时手势应该调节亮度还是调节声音
                             *//*
                            if (x < screen_height / 2) {

                                changeBrightness(-detlaY);
                            } else {
                                // 调节声音
                                changeVolume(-detlaY);
                            }
                        }
                        break;
                    }
                    // 手指从屏幕上抬起
                    case MotionEvent.ACTION_UP: {

                        progress_layout.setVisibility(View.GONE);

                        break;
                    }
                }
            return true;
        }
    });
    private void changeVolume(float detalY) {
        int max = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int current = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        int index = (int) (detalY / screen_height * max * 3);
        int volume = Math.max(current + index, 0);
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0);
        if (progress_layout.getVisibility() == View.GONE) {
            progress_layout.setVisibility(View.VISIBLE);
        }
        if (volume > max) {
            volume = max;
        }
        operation_bg.setImageResource(R.drawable.yinliang);
        ViewGroup.LayoutParams layoutParams = operation_percent.getLayoutParams();
        layoutParams.width = (int) (PixelUtil.dp2px(120) * (float) volume / max);
        operation_percent.setLayoutParams(layoutParams);
        volume_seek.setProgress(volume);
    }

    private void changeBrightness(float detlaY) {
        WindowManager.LayoutParams attributes = getWindow().getAttributes();
        float mBrightness = attributes.screenBrightness;
        float index = detlaY / screen_height / 3;
        mBrightness += index;
        if (mBrightness > 1.0f) {
            mBrightness = 1.0f;
        }
        if (mBrightness < 0.01f) {
            mBrightness = 0.01f;
        }
        attributes.screenBrightness = mBrightness;
        if (progress_layout.getVisibility() == View.GONE) {
            progress_layout.setVisibility(View.VISIBLE);
        }
        operation_bg.setImageResource(R.drawable.liangdu);
        ViewGroup.LayoutParams layoutParams = operation_percent.getLayoutParams();
        layoutParams.width = (int) (PixelUtil.dp2px(120) * mBrightness);
        operation_percent.setLayoutParams(layoutParams);
        getWindow().setAttributes(attributes);
    }*/

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        pause_img.setImageResource(R.drawable.play_btn_style);
        // 暂停播放
        videoView.pause();
        UIhandler.removeMessages(UPDATE_UI);
    }

    private boolean isFullScreen = false;

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // TODO Auto-generated method stub
        super.onConfigurationChanged(newConfig);

        /*
         * 当屏幕方向为横屏的时候
         */
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {

            setVideoViewScale(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            volume_img.setVisibility(View.VISIBLE);
            volume_seek.setVisibility(View.VISIBLE);
            isFullScreen = true;
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        /*
         * 当屏幕方向为竖屏的时候
         */
        else {
            setVideoViewScale(ViewGroup.LayoutParams.MATCH_PARENT, PixelUtil.dp2px(240));
            volume_img.setVisibility(View.GONE);
            volume_seek.setVisibility(View.GONE);
            isFullScreen = false;
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }

    //为控件设置宽高
    private void setVideoViewScale(int width, int height) {
        ViewGroup.LayoutParams layoutParams = videoView.getLayoutParams();
        layoutParams.width = width;
        layoutParams.height = height;
        videoView.setLayoutParams(layoutParams);

        ViewGroup.LayoutParams layoutParams2 = videoLayout.getLayoutParams();
        layoutParams2.width = width;
        layoutParams2.height = height;
        videoLayout.setLayoutParams(layoutParams2);
    }

}
