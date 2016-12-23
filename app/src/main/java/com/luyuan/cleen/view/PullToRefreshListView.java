package com.luyuan.cleen.view;


import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.luyuan.cleen.R;


public class PullToRefreshListView extends ListView implements OnScrollListener {

    private LinearLayout header_root;
    private LinearLayout header_view;
    private ImageView imageView;
    private ProgressBar progressbar;
    private TextView text;
    private TextView time;
    private View customView;
    private int headerViewHeight;
    private RotateAnimation up;
    private RotateAnimation down;
    private int downY = -1;
    private View headerView;

    private static final int PULL_DOWN = 1;
    private static final int RELEASE_REFRESH = 2;
    private static final int ISREFRESHING = 3;
    private static int CURRENTSTATE = PULL_DOWN;

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            //结束刷新
            finish();
        }

        ;
    };
    private View mFooterView;
    private int mFooterViewHeight;
    private boolean isLoadMore;
    private OnRefreshListenter refreshListenter;

    /**
     * 结束刷新
     */
    public void finish() {

        //判断是结束下拉刷新还是结束加载更多
        if (CURRENTSTATE == ISREFRESHING) {
            text.setText("下拉刷新");
            progressbar.setVisibility(View.GONE);
            imageView.setVisibility(View.VISIBLE);
            CURRENTSTATE = PULL_DOWN;
            headerView.setPadding(0, -headerViewHeight, 0, 0);
        }
        if (isLoadMore) {
            isLoadMore = false;
            mFooterView.setPadding(0, -mFooterViewHeight, 0, 0);
        }


    }

    public PullToRefreshListView(Context context) {
        this(context, null);
    }

    public PullToRefreshListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PullToRefreshListView(Context context, AttributeSet attrs,int defStyle) {
        super(context, attrs, defStyle);
        // 添加刷新头
        setHeader();
        //添加加载更多操作
        setFooter();
        //设置滑动监听
        setOnScrollListener(this);
    }

    /**
     * 添加加载更多条目
     */
    private void setFooter() {
        mFooterView = View.inflate(getContext(), R.layout.refresh_footer, null);

        //隐藏加载更多条目
        mFooterView.measure(0, 0);
        mFooterViewHeight = mFooterView.getMeasuredHeight();
        mFooterView.setPadding(0, -mFooterViewHeight, 0, 0);

        addFooterView(mFooterView);
    }

    /**
     * 添加刷新头
     */
    private void setHeader() {
        headerView = View.inflate(getContext(), R.layout.refresh_header, null);
        header_root = (LinearLayout) headerView
                .findViewById(R.id.refresh_header_root);
        header_view = (LinearLayout) headerView
                .findViewById(R.id.refresh_header_view);

        imageView = (ImageView) headerView.findViewById(R.id.imageView);
        progressbar = (ProgressBar) headerView.findViewById(R.id.progressbar);
        text = (TextView) headerView.findViewById(R.id.state_text);
        time = (TextView) headerView.findViewById(R.id.time);
        // 设置隐藏刷新头
        headerView.measure(0, 0);
        headerViewHeight = headerView.getMeasuredHeight();
        headerView.setPadding(0, -headerViewHeight, 0, 0);
        // 添加刷新头
        addHeaderView(headerView);
        // 初始化动画
        setAnimation();
    }

    /**
     * 初始化动画
     */
    private void setAnimation() {
        up = new RotateAnimation(0, -180, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        up.setDuration(300);
        up.setFillAfter(true);// 设置动画结束保持结束的状态

        down = new RotateAnimation(-180, -360, Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        down.setDuration(300);
        down.setFillAfter(true);// 设置动画结束保持结束的状态

    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downY = (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (downY == -1) {
                    downY = (int) ev.getY();
                }

                //如果是正在刷新，不能再次将listview拉动
                if (CURRENTSTATE == ISREFRESHING) {
                    return true;
                }

                int moveY = (int) ev.getY();

                if (moveY - downY > 0 && getFirstVisiblePosition() == 0) {
                    // 下拉刷新操作
                    int padding = moveY - downY - headerViewHeight;
                    // 刷新头移动相应的位置
                    headerView.setPadding(0, padding, 0, 0);
                    // 下拉刷新 -> 释放刷新
                    if (padding > 0 && CURRENTSTATE == PULL_DOWN) {
                        CURRENTSTATE = RELEASE_REFRESH;
                        swtichOption();
                    }
                    // 释放刷新 -> 下拉刷新
                    if (padding < 0 && CURRENTSTATE == RELEASE_REFRESH) {
                        CURRENTSTATE = PULL_DOWN;
                        swtichOption();
                    }
                    return true;
                }

                break;
            case MotionEvent.ACTION_UP:
                //downY = -1;
                // 释放刷新 -> 正在刷新
                if (CURRENTSTATE == RELEASE_REFRESH) {
                    CURRENTSTATE = ISREFRESHING;
                    headerView.setPadding(0, 0, 0, 0);
                    swtichOption();
                    handler.sendEmptyMessageDelayed(0, 3000);
                    /*if (refreshListenter != null) {
                        refreshListenter.refresh();
                    }*/
                }
                // 下拉刷新 -> 弹回
                if (CURRENTSTATE == PULL_DOWN) {
                    headerView.setPadding(0, -headerViewHeight, 0, 0);
                }
                break;
            default:
                break;
        }
        return super.onTouchEvent(ev);
    }

    /**
     * 根据状态，更改UI效果
     */
    public void swtichOption() {
        switch (CURRENTSTATE) {
            case PULL_DOWN:
                text.setText("下拉刷新");
                imageView.startAnimation(down);
                break;
            case RELEASE_REFRESH:
                text.setText("释放刷新");
                imageView.startAnimation(up);
                break;
            case ISREFRESHING:
                text.setText("正在刷新");
                imageView.clearAnimation();
                imageView.setVisibility(View.GONE);
                progressbar.setVisibility(View.VISIBLE);
                time.setText(formatDate());
                break;
        }
    }

    /**
     * 获取当前系统的时间
     *
     * @return
     */
    private String formatDate() {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(date);
    }

    /**
     * 添加viewpager
     *
     * @param view
     */
    public void addCustomVipager(View view) {
        customView = view;
        header_root.addView(view);
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {

    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == OnScrollListener.SCROLL_STATE_IDLE && getLastVisiblePosition() == getCount() - 1 && isLoadMore == false) {
            isLoadMore = true;
            mFooterView.setPadding(0, 0, 0, 0);
            //定位到最后一个条目
            setSelection(getCount() - 1);
            handler.sendEmptyMessageDelayed(0, 3000);
            /*if (refreshListenter != null) {
                refreshListenter.loadmore();
            }*/
        }
    }

    /**
     * 获取监听器
     *
     * @author Administrator
     */
    public void setOnRefreshListener(OnRefreshListenter refreshListenter) {
        this.refreshListenter = refreshListenter;
    }


    public interface OnRefreshListenter {
        /**
         * 下拉刷新
         */
        void refresh();

        /**
         * 上拉加载
         */
        void loadmore();
    }

}
