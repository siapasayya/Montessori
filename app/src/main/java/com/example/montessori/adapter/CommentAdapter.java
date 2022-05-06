package com.example.montessori.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.montessori.R;
import com.example.montessori.model.Comment;
import com.example.montessori.util.Helper;

import java.util.ArrayList;
import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    private final Context context;
    private final ArrayList<Comment> list = new ArrayList<>();

    public CommentAdapter(Context context) {
        this.context = context;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setList(List<Comment> list) {
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setLastComment(List<Comment> list) {
        this.list.clear();
        this.list.addAll(Helper.getLastItem(list));
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comments, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Comment data = list.get(position);
        holder.bind(data);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvName;
        private final TextView tvComment;
        private final TextView tvTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            tvComment = itemView.findViewById(R.id.tv_comment);
            tvTime = itemView.findViewById(R.id.tv_time);
        }

        public void bind(Comment item) {
            tvName.setText(item.getName());
            tvComment.setText(item.getComment());
            tvTime.setText(item.getDate());
        }
    }
}