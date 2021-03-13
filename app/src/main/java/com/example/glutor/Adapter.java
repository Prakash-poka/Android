package com.example.glutor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import ru.embersoft.expandabletextview.ExpandableTextView;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    private ArrayList<Item> items;
    private Context context;

    public  Adapter(ArrayList<Item> items,Context context) {
        this.items = items;
        this.context = context;
    }
    @NonNull
    @Override
    public Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter.ViewHolder holder, int position) {
        final Item item = items.get(position);
        holder.tittleTextView.setText(item.getTitle());
        holder.descTextview.setText(item.getDesc());
        holder.descTextview.setOnStateChangeListener(new ExpandableTextView.OnStateChangeListener() {
            @Override
            public void onStateChange(boolean isShrink) {
                Item contentItem = items.get(position);
                contentItem.setShrink(isShrink);
                items.set(position,contentItem);

            }
        });
        holder.descTextview.setText(item.getDesc());
        holder.descTextview.resetState(item.isShrink());

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ExpandableTextView descTextview;
        TextView tittleTextView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tittleTextView = itemView.findViewById(R.id.exptextTitle);
            descTextview = itemView.findViewById(R.id.descTextView);

        }
    }
}
