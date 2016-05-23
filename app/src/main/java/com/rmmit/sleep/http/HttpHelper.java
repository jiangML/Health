package com.rmmit.sleep.http;

import android.text.TextUtils;

import com.rmmit.sleep.activity.MyAppliction;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/4/26.
 */
public class HttpHelper {


    /**
     * 获取用户信息
     * @param url
     * @param cookie
     * @return
     */
    public static  String getToString(String url,String cookie)
    {
        System.out.println("url=="+url+"\n cookie==>"+cookie);
        if(url==null)
        {
            return null;
        }
        String result=null;
        try{
            URL u=new URL(url);
            HttpURLConnection con=(HttpURLConnection)u.openConnection();
            con.setConnectTimeout(5 * 1000);
            con.setRequestMethod("GET");
            if(cookie!=null)
            con.setRequestProperty("cookie",cookie);
            con.connect();
            InputStream  is=con.getInputStream();
            BufferedReader br=new BufferedReader(new InputStreamReader(is,"utf-8"));
            String r=null;
            StringBuffer buffer=new StringBuffer();
            while ((r=br.readLine())!=null)
            {
                System.out.println("结果====>"+r);
                buffer.append(r);
            }

            result=buffer.toString();
            System.out.println("结果====>"+result);
            con.disconnect();
        }catch (MalformedURLException e)
        {
            e.printStackTrace();
            System.out.println("结果 url错误====>" + e.getMessage());

        }catch (IOException io)
        {
            System.out.println("结果 io错误====>" + io.getMessage());
            io.printStackTrace();
        }
        return result;

    }

    /**
     * 登录接口 并获取cookie
     * @param url
     * @param name
     * @param pwd
     * @return
     */
    public static  boolean loging(URL url,String name,String pwd) {    //{"state":true,"utype":"-1"}
        System.out.println( "===登录账号========"+name);
        System.out.println( "===登录密码========"+pwd);
        boolean state = false;
        HttpURLConnection con=null;
        try {
            String result = null;
            String cookie = "";
            con= (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setConnectTimeout(5*1000);
            con.setDoOutput(true);
            StringBuffer buffer = new StringBuffer();
            buffer.append("uName").append("=").append(name).append("&") .append("uPwd").append("=").append(pwd);

            con.getOutputStream().write(buffer.toString().getBytes());
            InputStream is = con.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is, "utf-8"));
            StringBuffer b = new StringBuffer();
            while ((result = br.readLine()) != null) {
                b.append(result);
            }
            result = b.toString();
            System.out.println( "===登录结果========"+result);
            if(result!=null&&result!="null")
            {
                MyAppliction.login_result=result;
                JSONObject object = new JSONObject(result);
                if(object.has("state"))
                {
                    state=object.getBoolean("state");
                }
            }else{
                MyAppliction.login_result=null;
            }
            StringBuffer sb=new StringBuffer();
            Map<String, List<String>> map = con.getHeaderFields();
            for (String key : map.keySet()) {
                System.out.println(key + "===========");
                if("Set-Cookie".equals(key))
                {
                    for(String v:map.get(key))
                    {
                        System.out.println(key + "==========="+v);
                        sb.append(v.split(";")[0] + "; ");
                    }
                }

            }
            cookie = sb.toString().substring(0,sb.toString().lastIndexOf(";"));
            if(state)
            {
                MyAppliction.cookie=cookie;
            }
            System.out.println("cookie===========" + cookie);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException io) {
            io.printStackTrace();
        } catch (JSONException j)
        {
           j.printStackTrace();
        }finally {
            con.disconnect();
        }
        return state;
    }


    public static  String getUserInfo(String url)
    {
        if(url==null)
        {
            return null;
        }
        String result=getToString(url,MyAppliction.cookie);
        System.out.println("=====用户信息===="+result);
        if(TextUtils.isEmpty(result)||"null".equals(result))
        {
            return null;
        }
        try {
            JSONObject object = new JSONObject(result);
            if (object.has("UserAccount")) {
                MyAppliction.userName = object.getString("UserAccount");
                System.out.println("UserAccount========="+MyAppliction.userName);
            }
            if (object.has("ID"))
            {
                MyAppliction.id=object.getString("ID");
                System.out.println("ID========="+MyAppliction.id);
            }
        }catch (JSONException json)
        {
            json.printStackTrace();
        }

        return result;
    }





}
