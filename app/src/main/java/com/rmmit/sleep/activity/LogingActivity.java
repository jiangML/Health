package com.rmmit.sleep.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.rmmit.sleep.health.MainActivity;
import com.rmmit.sleep.health.R;
import com.rmmit.sleep.http.HttpHelper;
import com.rmmit.sleep.util.CashUtil;
import com.rmmit.sleep.util.Util;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Administrator on 2016/4/26.
 * 登录界面
 */
public class LogingActivity   extends BaseActivity{

    private EditText et_account;
    private EditText et_password;
    private Button btn_login;

    private String account;
    private String password;

    //登录成功
    private  static  final int LOGIN_RESULT_OK=1;
    //登录失败
    private  static  final int LOGIN_RESULT_FAIL=2;
    //连接超时
    private  static  final int LOGIN_TIME_OUT=3;

    private ProgressDialog dialog;
    private RelativeLayout rl_login;

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //dialog.dismiss();
            switch (msg.what)
            {
                case LOGIN_RESULT_OK:
                   // showToast("登录成功");
                    Intent intent=new Intent(LogingActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                case LOGIN_RESULT_FAIL:
                    rl_login.setVisibility(View.VISIBLE);
                    showToast("账号或者密码错误！");
                    break;
                case LOGIN_TIME_OUT:
                    rl_login.setVisibility(View.VISIBLE);
                    showToast("网络连接超时！");
                    break;
                default:
                    break;
            }
            btn_login.setEnabled(true);
            btn_login.setText("登 录");
            btn_login.setTextColor(Color.BLACK);
        }
    };




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view=View.inflate(this,R.layout.activity_login,null);
        Animation animation= AnimationUtils.loadAnimation(this, R.anim.login_anim);
       // viewAnimation(view);
        view.setAnimation(animation);
        setContentView(view);
        et_account=(EditText)findViewById(R.id.et_account);
        et_password=(EditText)findViewById(R.id.et_password);
        btn_login = (Button)findViewById(R.id.btn_login);
        rl_login = (RelativeLayout) findViewById(R.id.rl_login);
        rl_login.setVisibility(View.GONE);
        init();

    }

    private void init()
    {
       btn_login.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               getData();
               if (checkData()) {
                   login(account, password);
               }
           }
       });

        CashUtil cashUtil=new CashUtil(LogingActivity.this);
        account=cashUtil.getString(getResources().getString(R.string.account));
        password=cashUtil.getString(getResources().getString(R.string.password));

        if(!TextUtils.isEmpty(account)&&!TextUtils.isEmpty(password))
        {
            System.out.println("account==========="+account);
            System.out.println("password==========="+password);
            System.out.println("name==========="+cashUtil.getString("userName"));
            System.out.println("uid==========="+cashUtil.getString("uid"));
            login(account,password);
        }else{
            System.out.println("=====本地账号为空======");
            rl_login.setVisibility(View.VISIBLE);
        }


    }

    /**
     * 获取账号和密码
     */
    private void getData()
    {
        account=et_account.getText().toString().trim();
        password=et_password.getText().toString().trim();
    }

    /**
     * 检测数据有效性
     * @return
     */
    private boolean checkData()
    {


        if(TextUtils.isEmpty(account))
        {
            showToast("账号不能为空");
            return false;
        }
        if(TextUtils.isEmpty(password))
        {
            showToast("密码不能为空");
            return false;
        }
        return true;
    }

    /**
     * 登录操作
     * @param ac
     * @param pwd
     */
    private  void login(final  String ac, final  String pwd)
    {
        // dialog=ProgressDialog.show(LogingActivity.this,null,"登录中...");
          if(!Util.hasNetWork(LogingActivity.this))
          {
              showToast("网络有问题，请检查网络连接");
              return;
          }
            btn_login.setEnabled(false);
            btn_login.setText("登录中...");
            btn_login.setTextColor(Color.BLACK);
          new Thread(){
              @Override
              public void run() {
                  super.run();
                  try {
                      String path=getResources().getString(R.string.host)+getResources().getString(R.string.login_interface);
                      Message msg=handler.obtainMessage();
                      URL url=new URL(path);
                      boolean state =  HttpHelper.loging(url,ac,pwd);
                      if(MyAppliction.login_result==null)
                      {
                          msg.what=LOGIN_TIME_OUT;
                          msg.sendToTarget();
                          return;
                      }
                      if(state)
                      {
                          String r=HttpHelper.getUserInfo(getResources().getString(R.string.user_info_interface));
                          if(TextUtils.isEmpty(r)||"null".equals(r))
                          {
                              msg.what=LOGIN_TIME_OUT;
                          }else{
                              CashUtil cashUtil=new CashUtil(LogingActivity.this);
                              cashUtil.putString(getResources().getString(R.string.account),ac);
                              cashUtil.putString(getResources().getString(R.string.password),pwd);
                              cashUtil.putString(getResources().getString(R.string.user_name),MyAppliction.userName);
                              cashUtil.putString(getResources().getString(R.string.uid),MyAppliction.id);
                              msg.what=LOGIN_RESULT_OK;
                          }
                      }else{
                          msg.what=LOGIN_RESULT_FAIL;
                      }
                      msg.sendToTarget();
                  }catch (MalformedURLException m)
                  {
                      m.printStackTrace();
                  }
              }
          }.start();
    }

    /**
     * view动画
     * @param view
     */
    private void viewAnimation(View view)
    {
        AlphaAnimation aa = new AlphaAnimation(0.3f,1.0f);
        aa.setDuration(2500);
        view.startAnimation(aa);
        aa.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation arg0) {
                //redirectTo();
                if(TextUtils.isEmpty(account)||TextUtils.isEmpty(password))
                {
                    rl_login.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationStart(Animation animation) {
            }

        });
    }

}
