package com.satyajit.readscreencontenthack.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.satyajit.readscreencontenthack.R;
import com.satyajit.readscreencontenthack.models.SingleItemModel;

import java.util.List;

public class TextsAdapter extends RecyclerView.Adapter<TextsAdapter.MyViewHolder> {

    private List<SingleItemModel> itemsList;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView textTv;

        public MyViewHolder(View view) {
            super(view);
            textTv =  view.findViewById(R.id.text);
        }
    }


    public TextsAdapter(List<SingleItemModel> itemsList, Context context) {
        this.itemsList = itemsList;
        this.context = context;
    }

    @Override
    public TextsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_items, parent, false);

        return new TextsAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TextsAdapter.MyViewHolder holder, int position) {

        final SingleItemModel list = itemsList.get(position);

        holder.textTv.setText(list.getText());

        holder.textTv.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);


    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }
}