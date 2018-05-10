package com.example.gosia.weightapplication;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gosia.weightapplication.model.WeightData;
import com.orhanobut.logger.Logger;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {

    private List<WeightData> mList;
    private Context mContext;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView date, weight;
        public ImageView image;

        public ViewHolder(View v) {
            super(v);
            date = v.findViewById(R.id.text_view_date);
            weight = v.findViewById(R.id.text_view_weight);
            image = v.findViewById(R.id.image);
        }
    }

    public ListAdapter(List<WeightData> plist, Context pContext) {
        this.mList = plist;
        this.mContext = pContext;
    }

    @Override
    public ListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_weight_history, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        //set data
        WeightData weightData = mList.get(position);
        holder.date.setText(weightData.getLastDayWeightMeasurement());
        holder.weight.setText(weightData.getWeight());


        //calculate image

        List<WeightData> data = SQLite.select().
                from(WeightData.class).queryList();


        if (position % 2 == 1) {
            holder.itemView.setBackgroundColor(mContext.getResources().getColor(R.color.colorBlack));
        } else {
            holder.itemView.setBackgroundColor(mContext.getResources().getColor(R.color.colorTransparentBlack));
        }


       /* if (data.size() > 1){
            holder.image.setImageResource(R.drawable.ic_arrow);

            for(int i = 0; i < data.size(); i++){
                for(int j = 0; j < data.size(); j++){
                    if( (Double.parseDouble(data.get(i).getWeight()) < Double.parseDouble(data.get(j).getWeight()))){
                        holder.image.setRotation(holder.image.getRotation() + 90);

                    }else{
                        holder.image.setRotation(holder.image.getRotation() + 180);

                    }
                }
            }
        }else{
            holder.image.setVisibility(View.INVISIBLE);
        }*/

        ImageView imageView = holder.image;


        if ((data.size() > 1)) {
            imageView.setImageResource(R.drawable.ic_arrow);

            for (int i = data.size() - 1; i > 0; i--) {
                if ((Double.parseDouble(data.get(i).getWeight()) < Double.parseDouble(data.get(i - 1).getWeight()))) {
                    imageView.setRotation(imageView.getRotation() + 90);

                    imageView.setColorFilter(R.color.colorGreen);


                    Logger.e(" I  First " + Double.parseDouble(data.get(i).getWeight()) + " Second: " + Double.parseDouble(data.get(i - 1).getWeight()));
                } else if ((Double.parseDouble(data.get(i).getWeight()) > Double.parseDouble(data.get(i - 1).getWeight()))) {
                    imageView.setRotation(imageView.getRotation() + 270);
                    imageView.setColorFilter(R.color.colorRed);


                    Logger.e(" II  First " + Double.parseDouble(data.get(i).getWeight()) + " Second: " + Double.parseDouble(data.get(i - 1).getWeight()));
                } else {
                    //holder.image.setVisibility(View.INVISIBLE);
                }
            }
        } else {
            holder.image.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}