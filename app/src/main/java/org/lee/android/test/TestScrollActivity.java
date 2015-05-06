package org.lee.android.test;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import org.lee.android.doodles.R;
import org.lee.android.doodles.fragment.DoodleRecyclerAdapter;
import org.lee.android.doodles.fragment.HidingScrollListener;
import org.lee.android.util.Log;

/**
 * Created by author:李瑞宇
 * email:allnet@live.cn
 * on 15-4-24.
 */
public class TestScrollActivity extends Activity {

    private RecyclerView mRecyclerView;
    private View mCardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_activity_scroll);


        init();
    }

    private void init() {
        ScrollView scrollView;


        mCardView = findViewById(R.id.CardView);
        mRecyclerView = (RecyclerView) findViewById(R.id.RecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        final LayoutInflater inflater = getLayoutInflater();

        mRecyclerView.setAdapter(new RecyclerView.Adapter() {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = inflater.inflate(R.layout.doodle_list_item_adview, parent, false);
                return new DoodleRecyclerAdapter.AdViewHolder(view);
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

            }

            @Override
            public int getItemCount() {
                return 4;
            }
        });
//        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//            }
//
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//            }
//        });


    }

    int bottom;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN && bottom == 0) {
            float y = mCardView.getY();
            bottom = mCardView.getBottom();
            Log.anchor("y:" + y + ", bottom:" + bottom);

            mRecyclerView.setOnScrollListener(new HidingScrollListener((int) y) {
                @Override
                public void onMoved(int distance, int dx, int dy) {
                    mCardView.setTranslationY(-distance);
                }

                @Override
                public void onShow() {

                }

                @Override
                public void onHide() {

                }
            });
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onResume() {
        super.onResume();


    }
}
