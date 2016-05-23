package com.rmmit.sleep.health;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rmmit.sleep.activity.BaseActivity;
import com.rmmit.sleep.activity.LogingActivity;
import com.rmmit.sleep.activity.MyAppliction;
import com.rmmit.sleep.activity.TestWebViewActivity;
import com.rmmit.sleep.fragment.MainFragment;
import com.rmmit.sleep.util.CashUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends BaseActivity {

    private RelativeLayout title;
    private ImageView iv_back;
    private TextView tv_content;
    private FrameLayout fl_main;
    private NavigationView nv_menu;
    private DrawerLayout dl_drawer;
    private int currentItemId=R.id.item_sleep_day;
    private List<Integer> itemIds=new ArrayList<>();
    private Map<Integer,String> itemMap=new HashMap<>();
    private Map<Integer,MainFragment> fragmentMap=new HashMap<>();
    private TextView tv_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        initView();
        init();
    }

    /**
     * 初始化view
     */
    private void initView()
    {
        iv_back=(ImageView)findViewById(R.id.iv_back);
        tv_content=(TextView)findViewById(R.id.tv_content);
        fl_main=(FrameLayout)findViewById(R.id.fl_main);
        nv_menu=(NavigationView)findViewById(R.id.nv_menu);
        dl_drawer=(DrawerLayout)findViewById(R.id.dl_drawer);
    }

    /**
     * 初始化数据和显示界面
     */
    private void init()
    {
        itemIds.add(R.id.item_sleep_day);
        itemIds.add(R.id.item_sleep_month);
        itemIds.add(R.id.item_health_report);
        itemIds.add(R.id.item_my_device);
        itemIds.add(R.id.item_about);
        itemIds.add(R.id.item_feedback);
        itemIds.add(R.id.item_logout);
       // itemIds.add(R.id.item_test);

        itemMap.put(R.id.item_sleep_day, getResources().getString(R.string.sleep_day));
        itemMap.put(R.id.item_sleep_month,getResources().getString(R.string.sleep_month));
        itemMap.put(R.id.item_health_report,getResources().getString(R.string.health_report));
        itemMap.put(R.id.item_my_device, getResources().getString(R.string.my_device));
        itemMap.put(R.id.item_about, getResources().getString(R.string.about));
        itemMap.put(R.id.item_feedback, getResources().getString(R.string.feedback));



        nv_menu.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                if (itemIds.contains(item.getItemId())) {
                    System.out.println("=========点击了====" + item.getTitle() + "====id==" + item.getItemId());
                    tv_content.setText(item.getTitle());
                    item.setChecked(true);
                    selectItem(item.getItemId());
                    dl_drawer.closeDrawers();
                    return true;
                } else {
                    return false;
                }
            }
        });
        RelativeLayout head=(RelativeLayout)nv_menu.getHeaderView(0);

         tv_name=(TextView)head.findViewById(R.id.tv_name);
         tv_name.setText(MyAppliction.userName);
         iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dl_drawer.openDrawer(nv_menu);
            }
        });


        MainFragment fragment=MainFragment.newInstance(itemMap.get(currentItemId));
        fragmentMap.put(currentItemId,fragment);
        FragmentManager fragmentManager=getFragmentManager();
        fragmentManager.beginTransaction().add(R.id.fl_main, fragment).commit();
        tv_content.setText("今日睡眠");
    }

    /**
     * 选择菜单
     * @param id
     */
    private void selectItem(int id)
    {
        if(id==R.id.item_logout)
        {
            CashUtil cashUtil=new CashUtil(MainActivity.this);
            cashUtil.deleteString(getResources().getString(R.string.account));
            cashUtil.deleteString(getResources().getString(R.string.password));
            cashUtil.deleteString(getResources().getString(R.string.user_name));
            cashUtil.deleteString(getResources().getString(R.string.uid));
            Intent intent=new Intent(MainActivity.this, LogingActivity.class);
            startActivity(intent);
            finish();
            return;
        }

//        if(id==R.id.item_test)
//        {
//            Intent intent=new Intent(this, TestWebViewActivity.class);
//            startActivity(intent);
//            return;
//        }

       // showToast(itemMap.get(id));
        MainFragment fragment=null;
        FragmentManager fragmentManager=getFragmentManager();
        if(!fragmentMap.keySet().contains(id))
        {
            String url=itemMap.get(id);
            fragment =MainFragment.newInstance(url);
            fragmentMap.put(id,fragment);
        }else{
            fragment=fragmentMap.get(id);
        }
        if(!fragment.isAdded())
        {
            fragmentManager.beginTransaction().add(R.id.fl_main,fragment).commit();
        }
        if(currentItemId==id)
        {
            return;
        }
        fragmentManager.beginTransaction().hide(fragmentMap.get(currentItemId)).show(fragmentMap.get(id)).commit();
        currentItemId=id;
    }

}
