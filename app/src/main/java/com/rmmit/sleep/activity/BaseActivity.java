package com.rmmit.sleep.activity;

import android.app.Activity;
import android.widget.Toast;

/**
 * Created by Administrator on 2016/4/26.
 */
public class BaseActivity extends Activity {
    protected Toast toast;
    protected  void showToast(String message)
    {
        if(toast==null)
        {
          toast=Toast.makeText(this,message,Toast.LENGTH_SHORT);
        }
        toast.setText(message);
        toast.show();
    }

}
