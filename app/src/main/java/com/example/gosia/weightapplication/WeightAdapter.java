package com.example.gosia.weightapplication;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class WeightAdapter extends RecyclerView.Adapter<WeightAdapter.ViewHolder> {
    private List<Integer> values;

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView entireWeight;
        public View layout;

        private ViewHolder(View v) {
            super(v);
            layout = v;
            entireWeight = v.findViewById(R.id.first_line);
        }
    }


    public WeightAdapter(List<Integer> myDataset) {
        values = myDataset;
    }


    @Override
    public WeightAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.entire_weight_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Integer name = values.get(position % values.size());
        holder.entireWeight.setText(String.valueOf(name));
        holder.entireWeight.setOnClickListener(new View.OnClickListener() {
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
