package com.example.wayne.myapplication.ui;

import android.view.View;

/**
 * Created by zlc on 2017/9/7.
 */

public interface IBaseUI {

    <T extends View> T findView(int viewId);
    void startActivity(Class clasz, String... strs);
    void startActivityForResult(Class clasz, int resquestCode, String... strs);

}
