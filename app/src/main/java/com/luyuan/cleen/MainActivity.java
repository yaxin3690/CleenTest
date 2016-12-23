package com.luyuan.cleen;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.luyuan.cleen.adapter.LvAdapter;
import com.luyuan.cleen.model.CleenHero;
import com.luyuan.cleen.view.PullToRefreshListView;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.RequestQueue;
import com.yolanda.nohttp.rest.Response;

public class MainActivity extends Activity {

    private PullToRefreshListView mRows;
    private String url_user = "http://thoughtworks-ios.herokuapp.com/facts.json";
    private Handler mhandler;
    private LvAdapter mAdapyer;
    private TextView mTitle;
    /**
     * 用来标志请求的what, 类似handler的what一样，这里用来区分请求
     */
    private static final int NOHTTP_WHAT_TEST = 0x001;

    /**
     * 请求队列
     */
    private RequestQueue requestQueue;
    private LinearLayout mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        mRows = (PullToRefreshListView) findViewById(R.id.prf_rows);
        mTitle = (TextView) findViewById(R.id.tv_tit);
        mDialog = (LinearLayout) findViewById(R.id.ll_dialog);
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.pb_progressbar);
        TextView mMsg = (TextView) findViewById(R.id.tv_msg);

        requestQueue = NoHttp.newRequestQueue();
        initData();
        LoadAndRefresh();

    }

    private void initData() {
        MyOnResponseListener onResponseListener = new MyOnResponseListener();
        Request<String> stringRequest = NoHttp.createStringRequest(url_user);
        requestQueue.add(NOHTTP_WHAT_TEST, stringRequest, onResponseListener);
    }

    class MyOnResponseListener implements OnResponseListener {

        @Override
        public void onStart(int what) {

        }

        @Override
        public void onSucceed(int what, Response response) {
            if (what == NOHTTP_WHAT_TEST) {
                String string = (String) response.get();
                Gson gson = new Gson();
                CleenHero cleenHero = gson.fromJson(string, CleenHero.class);
                Log.e("TAG", cleenHero.getRows().size() + "dfghjk");
                mDialog.setVisibility(View.GONE);
                mTitle.setVisibility(View.VISIBLE);
                mRows.setVisibility(View.VISIBLE);
                mTitle.setText(cleenHero.getTitle());
                CreatAdapter(cleenHero);
            }
        }

        @Override
        public void onFailed(int what, Response response) {
            Toast.makeText(MainActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onFinish(int what) {

        }
    }

    private void CreatAdapter(CleenHero data) {
        if (mAdapyer == null) {
            mAdapyer = new LvAdapter(MainActivity.this, data);
            mRows.setAdapter(mAdapyer);
        } else {
            mAdapyer.notifyDataSetChanged();
        }
    }


    private void LoadAndRefresh() {
        mRows.setOnRefreshListener(new PullToRefreshListView.OnRefreshListenter() {
            @Override
            public void refresh() {
                mAdapyer.notifyDataSetChanged();
            }

            @Override
            public void loadmore() {
                initData();
            }
        });
    }

}
