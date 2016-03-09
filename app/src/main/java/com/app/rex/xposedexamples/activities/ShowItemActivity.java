package com.app.rex.xposedexamples.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.app.rex.xposedexamples.R;
import com.app.rex.xposedexamples.adapters.ItemListAdapter;
import com.app.rex.xposedexamples.domain.Item;
import com.app.rex.xposedexamples.model.Items;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ShowItemActivity extends AppCompatActivity {


    @Bind(R.id.rv_items)
    RecyclerView mRecyclerView;

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    private static final int PRELOAD_SIZE = 6;


    private ItemListAdapter mItemListAdapter;
    private List<Item> mItemList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_item);
        ButterKnife.bind(this);
        mItemList = Items.getItems();

        setupRecyclerView();

        mToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRecyclerView.smoothScrollToPosition(0);
            }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();
        if (mItemList.size() < Items.getItems().size()) {
            mItemList = Items.getItems();
            mItemListAdapter.notifyDataSetChanged();
        }
    }

    private void setupRecyclerView() {
        final StaggeredGridLayoutManager layoutManager
                = new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mItemListAdapter = new ItemListAdapter(this, mItemList);
        mRecyclerView.setAdapter(mItemListAdapter);

        mRecyclerView.addOnScrollListener(getOnBottomListener(layoutManager));
        mItemListAdapter.setOnItemClickListener(new ItemListAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, String data) {
                Toast.makeText(ShowItemActivity.this,data,Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ShowItemActivity.this, WebActivity.class);
                intent.putExtra(WebActivity.EXTRA_URL,data);
                startActivity(intent);
            }
        });
    }


    RecyclerView.OnScrollListener getOnBottomListener(final StaggeredGridLayoutManager layoutManager) {
        return new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView rv, int dx, int dy) {
                boolean isBottom =
                        layoutManager.findLastCompletelyVisibleItemPositions(
                                new int[2])[1] >=
                                mItemListAdapter.getItemCount() -
                                        PRELOAD_SIZE;
                /*if (!mSwipeRefreshLayout.isRefreshing() && isBottom) {
                    if (!mIsFirstTimeTouchBottom) {
                        mSwipeRefreshLayout.setRefreshing(true);
                        mPage += 1;
                        loadData();
                    } else {
                        mIsFirstTimeTouchBottom = false;
                    }
                }*/
            }
        };
    }


}

