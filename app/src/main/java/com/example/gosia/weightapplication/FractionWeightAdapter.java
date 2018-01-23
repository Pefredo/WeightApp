package com.example.gosia.weightapplication;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class FractionWeightAdapter extends RecyclerView.Adapter<FractionWeightAdapter.ViewHolder> {
    private List<Integer> values;

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView fractionWeight;
        public View layout;

        private ViewHolder(View v) {
            super(v);
            layout = v;
            fractionWeight = v.findViewById(R.id.second_line);
        }
    }


    public FractionWeightAdapter(List<Integer> myDataset) {
        values = myDataset;
    }


    @Override
    public FractionWeightAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.fraction_weight_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Integer name = values.get(position % values.size());
        holder.fractionWeight.setText(String.valueOf(name));
        holder.fractionWeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return values == null ? 0 : values.size() * 2;

    }
}
