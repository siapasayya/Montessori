package com.example.montessori.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.montessori.R;
import com.example.montessori.model.PostMember;

import java.util.ArrayList;
import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {
    private final Context context;
    private final ArrayList<PostMember> list = new ArrayList<>();

    public PostAdapter(Context context) {
        this.context = context;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setList(List<PostMember> list) {
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Log.d("FragmentAdapter", "Data: " + list.get(position));
        holder.bind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView ivPost;
        private final TextView tvDesc;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivPost = itemView.findViewById(R.id.ivPost);
            tvDesc = itemView.findViewById(R.id.tvDesc);
        }

        public void bind(PostMember item) {
            Glide.with(ivPost).load(item.getPostUri()).into(ivPost);
            tvDesc.setText(item.getDesc());
        }
    }
}