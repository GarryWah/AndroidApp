package com.example.admin.androidapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.admin.androidapp.models.Model;
import com.example.admin.androidapp.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 3/5/2017.
 */
public class CommunityFragmentAdapter extends RecyclerView.Adapter<CommunityFragmentAdapter.ItemViewHolder> {
    private List<Model> models;
    private Context context;
    private OnCommunityFragmentClickListener listener;

    public CommunityFragmentAdapter(List<Model> models, Context context) {
        this.models = models;
        this.context = context;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ItemViewHolder(inflater.inflate(R.layout.item_community, parent, false));
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        holder.bind(models.get(position));
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public void setOnClick(OnCommunityFragmentClickListener communityFragmentClickListener) {
        this.listener = communityFragmentClickListener;
    }

    public interface OnCommunityFragmentClickListener {
        void onItemClick(int position);
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView tvText;
        private TextView tvText2;
        private ImageView imageView;

        public ItemViewHolder(View itemView) {
            super(itemView);
            tvText = (TextView) itemView.findViewById(R.id.tvText);
            tvText2 = (TextView) itemView.findViewById(R.id.tvText2);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("Adapter", models.get(getAdapterPosition()).toString());
                    if (listener != null) {
                        listener.onItemClick(getAdapterPosition());

                    }
                }
            });

        }

        public void bind(Model model) {
            tvText.setText(model.getName());
            tvText2.setText(model.getDescription());
            Glide.with(context).load(model.getPhoto()).into(imageView);
        }

    }

}
