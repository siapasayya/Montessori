package com.example.montessori.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.montessori.R;
import com.example.montessori.model.PostMember;
import com.example.montessori.ui.user.PostDetailActivity;
import com.example.montessori.util.IntentNameExtra;

import java.util.ArrayList;
import java.util.List;

public class PostProfileAdapter extends RecyclerView.Adapter<PostProfileAdapter.ViewHolder> {
    private final Context context;
    private final ArrayList<PostMember> list = new ArrayList<>();

    public PostProfileAdapter(Context context) {
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
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post_profile, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PostMember data = list.get(position);
        holder.bind(data);
        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, PostDetailActivity.class);
            intent.putExtra(IntentNameExtra.POST_DATA, data);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView ivPost;
        private final TextView tvPem;
        private final TextView tvUmur;
        private final TextView tvDesc;
        private final TextView tvStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivPost = itemView.findViewById(R.id.ivPost);
            tvDesc = itemView.findViewById(R.id.tvDesc);
            tvPem = itemView.findViewById(R.id.tvPem);
            tvUmur = itemView.findViewById(R.id.tvUmur);
            tvStatus = itemView.findViewById(R.id.tvStatus);
        }

        public void bind(PostMember item) {
            Glide.with(ivPost)
                    .applyDefaultRequestOptions(RequestOptions.centerCropTransform())
                    .load(item.getPostUri())
                    .into(ivPost);
            if (item.isChecked() && item.isApproved()) {
                tvStatus.setText("Status : Disetujui");
            } else if (item.isChecked() && !item.isApproved()) {
                tvStatus.setText("Status : Ditolak");
            } else {
                tvStatus.setText("Status : Menunggu");
            }
            tvDesc.setText(item.getDesc());
            tvPem.setText(item.getPem());
            tvUmur.setText(item.getUmur());
        }
    }
}