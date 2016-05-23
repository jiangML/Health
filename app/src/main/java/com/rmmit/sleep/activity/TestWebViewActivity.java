package com.rmmit.sleep.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.rmmit.sleep.health.R;

/**
 * Created by Administrator on 2016/5/3.
 */
public class TestWebViewActivity extends BaseActivity {
    private TextView tv_back;
    private Button btn;
    private EditText url;
    private WebView wv;

    private ProgressDialog dialog;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_test_web_view);
        tv_back=(TextView)findViewById(R.id.tv_back);
        btn=(Button)findViewById(R.id.btn_open);
        url=(EditText)findViewById(R.id.et_url);
        wv=(WebView)findViewById(R.id.wv_test);

        wv.getSettings().setJavaScriptEnabled(true);
        wv.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if(dialog!=null&&dialog.isShowing())
                {
                    dialog.dismiss();
                }

            }
        });


        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog == null) {
                    dialog = ProgressDialog.show(TestWebViewActivity.this, null, "正在加载...");
                } else {
                    dialog.show();
                }
                wv.loadUrl(url.getText().toString().trim());
            }
        });


    }


}
