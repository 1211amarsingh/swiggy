package com.kv.swiggyaddress;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.kv.swiggyaddress.util.UserData;

import java.util.ArrayList;

import static com.kv.swiggyaddress.util.Utils.filterAddress;


public class SelectLocationAdapter extends RecyclerView.Adapter<SelectLocationAdapter.ViewHolder> {

    private AppCompatActivity activity;
    private ArrayList<UserData.Address> address;
    private boolean recent_add, expand;

    SelectLocationAdapter(AppCompatActivity activity, ArrayList<UserData.Address> address, boolean recent) {
        this.address = address;
        this.activity = activity;
        this.recent_add = recent;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_item_address, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder myViewHolder, int i) {
        myViewHolder.tvTitle.setText(address.get(i).getType());
        myViewHolder.tvAddress.setText(filterAddress(address.get(i)));
        myViewHolder.llParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onselectAddress(address.get(myViewHolder.getAdapterPosition()));
            }
        });
        if (recent_add) {
            myViewHolder.imageView.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_history_black));
        } else {
            if (address.get(i).getType().equalsIgnoreCase("Home")) {
                myViewHolder.imageView.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_home_black));
            } else if (address.get(i).getType().equalsIgnoreCase("Work")) {
                myViewHolder.imageView.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_office_black));
            } else {
                myViewHolder.imageView.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_add_location_black));
            }
        }
        if (i + 1 == address.size()) {
            myViewHolder.devider.setVisibility(View.GONE);
        }
    }

    private void onselectAddress(UserData.Address address) {
        Intent intent = new Intent();
        intent.putExtra("address", address);
        activity.setResult(Activity.RESULT_OK, intent);
        activity.finish();
    }

    @Override
    public int getItemCount() {
        if (expand) {
            return address.size();
        } else {
            return address.size() > 3 ? 3 : address.size();
        }
    }

    void setExpand() {
        this.expand = true;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        TextView tvAddress;
        LinearLayout llParent;
        ImageView imageView;
        View devider;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvAddress = itemView.findViewById(R.id.tv_address);
            llParent = itemView.findViewById(R.id.ll_parent);
            imageView = itemView.findViewById(R.id.img);
            devider = itemView.findViewById(R.id.view_devider);
        }
    }
}
