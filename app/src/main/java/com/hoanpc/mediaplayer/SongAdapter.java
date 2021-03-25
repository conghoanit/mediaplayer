package com.hoanpc.mediaplayer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongHolder> {
    private final List<SongEntity> mData;
    private final Context mContext;
    private SongEntity currentItem;
    private OnSongCallBack callBack;

    public SongAdapter(List<SongEntity> mData, Context mContext) {
        this.mData = mData;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public SongHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.item_song, parent, false);
        return new SongHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SongHolder holder, int position) {
        SongEntity item = mData.get(position);
        holder.tvSong.setText(item.getName());
        holder.tvSinger.setText(item.getAlbum());
        holder.tvSong.setTag(item);

        if (item.equals(currentItem)) {
            holder.lnSong.setBackgroundResource(R.color.gray_light);
        } else {
            holder.lnSong.setBackgroundResource(R.color.white);
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setCallBack(OnSongCallBack callBack) {
        this.callBack = callBack;
    }

    public void updateCurrent(SongEntity song) {
        currentItem = song;
        notifyDataSetChanged();
    }

    public interface OnSongCallBack {
        void songSelected(SongEntity item);
    }

    public class SongHolder extends RecyclerView.ViewHolder {
        TextView tvSong, tvSinger;
        View lnSong;

        public SongHolder(@NonNull View itemView) {
            super(itemView);
            lnSong = itemView.findViewById(R.id.ln_song);
            lnSong.setOnClickListener(v -> {
                currentItem = (SongEntity) tvSong.getTag();
                notifyDataSetChanged();
                if (callBack != null) {
                    callBack.songSelected(currentItem);
                }
            });
            tvSong = itemView.findViewById(R.id.tv_song);
            tvSinger = itemView.findViewById(R.id.tv_singer);
        }
    }
}
