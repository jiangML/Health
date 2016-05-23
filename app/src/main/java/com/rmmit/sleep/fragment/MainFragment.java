package com.rmmit.sleep.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

import com.rmmit.sleep.activity.MyAppliction;
import com.rmmit.sleep.health.R;

/**
 * Created by Administrator on 2016/4/27.
 */
public class MainFragment extends BaseFragment {

    private static final String URL_PATH="url_path";
    private View view;
    private WebView wv;
    private ProgressDialog dialog;
    private FrameLayout fl_load;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_main,container,false);
        wv=(WebView)view.findViewById(R.id.wv);
        fl_load=(FrameLayout)view.findViewById(R.id.fl_load);
       // dialog=ProgressDialog.show(getActivity(),null,"正在加载...");
        //启用支持javascript
        WebSettings settings = wv.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setUseWideViewPort(true);//设置此属性，可任意比例缩放
        settings.setLoadWithOverviewMode(true);
        //settings.setBuiltInZoomControls(true);
        settings.setSupportZoom(true);
        settings.setDomStorageEnabled(true);
        settings.setBlockNetworkImage(false);

        wv.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                fl_load.setVisibility(View.GONE);
                wv.setVisibility(View.VISIBLE);
                super.onPageFinished(view, url);
            }
        });
        String url=getArguments().getString(URL_PATH,"")+"?uid="+ MyAppliction.id;
        System.out.println("=======url======"+url);
        wv.loadUrl(url);
        return view;
    }

    public static  MainFragment newInstance(String url)
    {
        MainFragment fragment=new MainFragment();
        Bundle bundle=new Bundle();
        bundle.putString(URL_PATH, url);
        fragment.setArguments(bundle);
        return fragment;
    }

}
