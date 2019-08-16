package com.example.myvideo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

public class MainActivity extends AppCompatActivity {

    private VideoView videoView;
    private SeekBar play_seek;
    private ImageView pause_img;
    private TextView time_current_tv;
    private TextView time_total_tv;
    private LinearLayout left_layout;
    private SeekBar volume_seek;
    private ImageView screen_img;
    private LinearLayout controllerbar_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        videoView = (VideoView) findViewById(R.id.id_VideoView);
        play_seek = (SeekBar) findViewById(R.id.play_seek);
        pause_img = (ImageView) findViewById(R.id.pause_img);
        time_current_tv = (TextView) findViewById(R.id.time_current_tv);
        time_total_tv = (TextView) findViewById(R.id.time_total_tv);
        left_layout = (LinearLayout) findViewById(R.id.left_layout);
        volume_seek = (SeekBar) findViewById(R.id.volume_seek);
        screen_img = (ImageView) findViewById(R.id.screen_img);
        controllerbar_layout = (LinearLayout) findViewById(R.id.controllerbar_layout);

        if(videoView.isPlaying()){
            pause_img.setImageResource(R.drawable.pause_btn_style);
            //暂停播放
            videoView.pause();
        }
        else{
            pause_img.setImageResource(R.drawable.pause_btn_style);
            //播放
            videoView.start();
        }

    }
}
