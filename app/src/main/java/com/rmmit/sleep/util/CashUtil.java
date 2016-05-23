package com.rmmit.sleep.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Administrator on 2016/4/26.
 */
public class CashUtil  {
    private Context context;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private static  final  String cashName="mycash";
    public CashUtil(Context context)
    {
        this.context=context;
        sharedPreferences=context.getSharedPreferences(cashName,Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();
    }

    /**
     * 添加缓存数据
     * @param key
     * @param value
     */
    public  void putString(String key,String value )
    {
         editor.putString(key,value);
         editor.commit();
    }

    /**
     * 提取缓存数据
     * @param key
     * @return
     */
    public String getString(String key)
    {
        return sharedPreferences.getString(key,"");
    }

    public void deleteString(String key)
    {
        editor.remove(key);
        editor.commit();
    }

}
