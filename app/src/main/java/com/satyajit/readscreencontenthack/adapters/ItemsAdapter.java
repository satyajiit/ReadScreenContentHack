package com.satyajit.readscreencontenthack.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.satyajit.readscreencontenthack.R;
import com.satyajit.readscreencontenthack.activity.AllCapturedTexts;
import com.satyajit.readscreencontenthack.models.SingleItemModel;

import java.util.List;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.MyViewHolder> {

    private List<SingleItemModel> itemsList;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView textTv;
        CardView clicker;

        public MyViewHolder(View view) {
            super(view);
            textTv =  view.findViewById(R.id.text);
            clicker =  view.findViewById(R.id.clicker);
        }
    }


    public ItemsAdapter(List<SingleItemModel> itemsList, Context context) {
        this.itemsList = itemsList;
        this.context = context;
    }

    @Override
    public ItemsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_items, parent, false);

        return new ItemsAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ItemsAdapter.MyViewHolder holder, int position) {

        final SingleItemModel list = itemsList.get(position);

        holder.textTv.setText(list.getText());
        holder.clicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                context.startActivity(new Intent(context,
                        AllCapturedTexts.class).putExtra("package_name",
                        list.getText()));
                ((AppCompatActivity)context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

            }
        });



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