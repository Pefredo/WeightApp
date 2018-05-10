package com.example.gosia.weightapplication;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gosia.weightapplication.databinding.FragmentHistoryBinding;
import com.example.gosia.weightapplication.model.WeightData;
import com.orhanobut.logger.Logger;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.ArrayList;
import java.util.List;


public class HistoryFragment extends Fragment {

    private List<WeightData> mWeightData = new ArrayList<>();
    private FragmentHistoryBinding mFragmentHistoryBinding;
    private ListAdapter mAdapter;
    //private RecyclerView mRecyclerView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, viewGroup, false);

        //binding variable
        mFragmentHistoryBinding = DataBindingUtil.setContentView(getActivity(), R.layout.fragment_history);
        mFragmentHistoryBinding.setVariable(this);

        //recycler
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        // define an adapter
        mAdapter = new ListAdapter(mWeightData, getContext());
        recyclerView.setAdapter(mAdapter);

        prepareWeightData();

        return view;
    }

    private void prepareWeightData() {
        mWeightData = SQLite.select().
                from(WeightData.class).queryList();

        Logger.d(mWeightData.toString()); //The list isn't null, I have data to show
        //mAdapter = new ListAdapter(mWeightData, getActivity());
        //mRecyclerView.setAdapter(mAdapter);

        mAdapter.notifyDataSetChanged();
    }
}
