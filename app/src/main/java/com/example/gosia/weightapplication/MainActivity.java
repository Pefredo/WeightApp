package com.example.gosia.weightapplication;

import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.TextView;

import com.example.gosia.weightapplication.databinding.ActivityMainBinding;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import net.steamcrafted.loadtoast.LoadToast;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    public final String LOGGER = this.getClass().getSimpleName();

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private LinearLayoutManager layoutManager;

    private RecyclerView fractionRecyclerView;
    private RecyclerView.Adapter fractionAdapter;
    private LinearLayoutManager fractionlayoutManager;

    private List<Integer> mListWeight;
    private List<Integer> mListFractionWeight;

    /**
     *
     */
    private SnapHelper snapHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Logger.addLogAdapter(new AndroidLogAdapter());

        //binding variable
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setVariable(this);

        loadRecyclers();
        loadData();
    }


    private void loadRecyclers() {
        snapHelper = new LinearSnapHelper();
        loadRecyclerViewForIntegers();
        loadRecyclerViewForFractions();
    }

    /**
     *
     */
    private void loadRecyclerViewForIntegers() {
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        snapHelper.attachToRecyclerView(recyclerView);

        mListWeight = new ArrayList<>();
        for (int i = 0; i < 250; i++) {
            mListWeight.add(i);
        }

        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                View centerView = snapHelper.findSnapView(layoutManager);
                int pos = layoutManager.getPosition(centerView);

                if (pos >= mListWeight.size()) {
                    pos -= mListWeight.size();
                }

                TextView weightValue = findViewById(R.id.weight_value);
                weightValue.setText(String.valueOf(mListWeight.get(pos)));

                int firstItemVisible = layoutManager.findFirstVisibleItemPosition();
                if (firstItemVisible != 0 && firstItemVisible % mListWeight.size() == 0) {
                    recyclerView.getLayoutManager().scrollToPosition(0);
                    Log.d(LOGGER, "" + firstItemVisible);
                }

               /* int lastItemVisible = layoutManager.findFirstVisibleItemPosition();
                if (lastItemVisible != 10 && lastItemVisible % mListWeight.size() == 0) {
                    recyclerView.getLayoutManager().scrollToPosition(10);
                }*/
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    View centerView = snapHelper.findSnapView(layoutManager);
                    int pos = layoutManager.getPosition(centerView);

                    TextView weightValue = findViewById(R.id.weight_value);
                    weightValue.setText(String.valueOf(mListWeight.get(pos)));
                }
            }
        });

        mAdapter = new WeightAdapter(mListWeight);
        recyclerView.setAdapter(mAdapter);
    }

    private void loadRecyclerViewForFractions() {
        fractionRecyclerView = findViewById(R.id.fraction_recycler_view);
        fractionRecyclerView.setHasFixedSize(true);

        snapHelper.attachToRecyclerView(fractionRecyclerView);

        fractionlayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        fractionRecyclerView.setLayoutManager(fractionlayoutManager);


        mListFractionWeight = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            mListFractionWeight.add(i);
        }

        fractionRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recycler, int dx, int dy) {
                super.onScrolled(recycler, dx, dy);

                View centerView = snapHelper.findSnapView(fractionlayoutManager);
                int position = fractionlayoutManager.getPosition(centerView);
                if (position >= mListFractionWeight.size()) {
                    position -= mListFractionWeight.size();
                }

                TextView weightValue = findViewById(R.id.fraction_weight_value);
                weightValue.setText(String.valueOf(mListFractionWeight.get(position)));

                int firstItemVisible = fractionlayoutManager.findFirstVisibleItemPosition();
                if (firstItemVisible != 0 && firstItemVisible % mListFractionWeight.size() == 0) {
                    fractionRecyclerView.getLayoutManager().scrollToPosition(0);
                }

                /*int lastItemVisible = fractionlayoutManager.findFirstVisibleItemPosition();
                if (lastItemVisible != mListFractionWeight.size() && lastItemVisible % mListFractionWeight.size() == 0) {
                    recycler.getLayoutManager().scrollToPosition(10);
                    Logger.d(lastItemVisible);
                }*/

            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    View centerView = snapHelper.findSnapView(fractionlayoutManager);
                    int pos = fractionlayoutManager.getPosition(centerView);

                    TextView weightValue = findViewById(R.id.fraction_weight_value);
                    weightValue.setText(String.valueOf(mListFractionWeight.get(pos)));
                }
            }
        });

        fractionAdapter = new FractionWeightAdapter(mListFractionWeight);
        fractionRecyclerView.setAdapter(fractionAdapter);
    }

    private void loadData() {

        List<Entry> entries = new ArrayList<>();

        entries.add(new Entry(44f, 0));
        entries.add(new Entry(88f, 1));
        entries.add(new Entry(66f, 2));
        entries.add(new Entry(12f, 3));
        entries.add(new Entry(19f, 4));
        entries.add(new Entry(91f, 5));

        LineDataSet lineDataSet = new LineDataSet(entries, "# of Calls");

        LineChart chart = findViewById(R.id.chart);

        LineData lineData = new LineData(lineDataSet);
        chart.setData(lineData);
        chart.invalidate(); // refresh
    }

    public void showList(View view) {
        boolean sendingData = true;
        boolean succeed = false;
        boolean error = false;

        LoadToast loadToast = new LoadToast(this);

        loadToast.setTextColor(Color.BLACK)
                .setBackgroundColor(Color.WHITE)
                .setProgressColor(R.color.colorPeach);
        loadToast.setTranslationY(550);

        if (sendingData) {
            loadToast.setText("Sending");
            loadToast.show();

            /*if(succeed){
                loadToast.success();
            }else if(error){
                loadToast.error();
            }*/
        }


    }
}
