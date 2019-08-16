package com.example.videodemo;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.VideoView;

/**
 * @name 张东
 * @time 2019/8/15  15:24
 * @describe
 */
public class CustomVideoView extends VideoView {

    int defaultWidth = 1920;
    int defaultHeight = 1080;

    public CustomVideoView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    public CustomVideoView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
    }

    public CustomVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getDefaultSize(defaultWidth, widthMeasureSpec);
        int height = getDefaultSize(defaultHeight, heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

}
