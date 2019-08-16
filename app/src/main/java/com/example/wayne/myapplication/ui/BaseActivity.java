package com.example.wayne.myapplication.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

/**
 * Created by zlc on 2017/9/7.
 */

public abstract class BaseActivity extends AppCompatActivity implements IBaseUI,View.OnClickListener{

    protected static String TAG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TAG = this.getClass().getSimpleName();
       // setContentView(getLayoutId());
    }

    public abstract int getLayoutId();

    protected void initView(){}

    protected void initData(){}

    protected void initListener(){}

    @Override
    public void startActivity(Class clasz, String... strs){
        Intent intent = setActivity(clasz, strs);
        startActivity(intent);
    }

    @Override
    public void startActivityForResult(Class clasz,int resquestCode,String ... strs){
        Intent intent = setActivity(clasz, strs);
        startActivityForResult(intent,resquestCode);
    }

    @NonNull
    private Intent setActivity(Class clasz, String[] strs) {
        Intent intent = new Intent(this,clasz);
        if(strs != null) {
            for (int i = 0; i < strs.length; i++) {
                if(i % 2 ==1){
                    intent.putExtra(strs[i-1],strs[i]);
                    Log.e("key",strs[i-1]);
                    Log.e("value",strs[i]);
                }
            }
        }
        return intent;
    }

    @Override
    public <T extends View> T findView(int viewId) {
        return (T)findViewById(viewId);
    }

    @Override
    public void onClick(View view) {
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
