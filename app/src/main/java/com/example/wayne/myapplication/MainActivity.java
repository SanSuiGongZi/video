package com.example.wayne.myapplication;


import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.example.wayne.myapplication.ui.BaseActivity;
import com.example.wayne.myapplication.util.ScreenUtil;
import com.example.wayne.myapplication.util.StatusBarUtil;

public class MainActivity extends BaseActivity {

    private RelativeLayout rl_video_container;
    private FullScreenVideoView videoview;
    private VideoController id_video_controller;
    private VideoBusiness mVideoBusiness;
    private String mVideoUrl = "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4 ";
    private TextView id_tv_title;
    private Toolbar mTool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        initView();
        initData();
        initListener();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        super.initView();
        rl_video_container = findView(R.id.rl_video_container);
        videoview = findView(R.id.id_videoview);
        id_video_controller = findView(R.id.id_video_controller);
        id_tv_title = findView(R.id.id_tv_title);
        mTool = findView(R.id.mTool);

        mTool.setTitle("");
        setSupportActionBar(mTool);

    }

    @Override
    protected void initData() {
        super.initData();
        setVideoInfo();
    }

    private void setVideoInfo() {
        //放一个视频到res/raw下就能播放 或者放一个在线地址
       // Uri uri= Uri.parse("android.resource://" + getPackageName() + "/"/*+R.raw.test*/);

        mVideoBusiness = new VideoBusiness(this);
         mVideoBusiness.initVideo(videoview,id_video_controller,mVideoUrl);
       // mVideoBusiness.initVideo(videoview,id_video_controller,uri);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        int screenWidth = ScreenUtil.getScreenWidth(this);
        setVideoContainerParam(screenWidth,screenWidth * 9 / 16);
    }

    private boolean isFullScreen;
    @Override
    public void onConfigurationChanged(Configuration newConfig) {

        super.onConfigurationChanged(newConfig);
        //全屏看视频
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            id_tv_title.setVisibility(View.GONE);
            int matchParent = ViewGroup.LayoutParams.MATCH_PARENT;
            setVideoContainerParam(matchParent,matchParent);
            if(StatusBarUtil.hasNavBar(this)){
                StatusBarUtil.hideBottomUIMenu(this);
            }
            isFullScreen = true;
            StatusBarUtil.fullscreen(true,this);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) { //从视频全屏界面恢复竖屏

            if(StatusBarUtil.hasNavBar(this)){
                StatusBarUtil.showBottomUiMenu(this);
            }
            StatusBarUtil.fullscreen(false,this);
            id_tv_title.setVisibility(View.VISIBLE);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            int screenWidth = ScreenUtil.getScreenWidth(this);
            setVideoContainerParam(screenWidth,screenWidth * 9 / 16);
            isFullScreen = false;
        }
    }

    private void setVideoContainerParam(int w,int h) {
        ViewGroup.LayoutParams params = rl_video_container.getLayoutParams();
        params.width = w;
        params.height = h;
        rl_video_container.setLayoutParams(params);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isFullScreen) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                ImageView imageView = (ImageView) rl_video_container.findViewById(R.id.id_iv_video_expand);
                imageView.setImageResource(R.drawable.zuidahua_2x);
                return true;
            } else {
                finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void initListener() {
        super.initListener();
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()){
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mVideoBusiness!=null){
            mVideoBusiness.release();
        }
    }
}