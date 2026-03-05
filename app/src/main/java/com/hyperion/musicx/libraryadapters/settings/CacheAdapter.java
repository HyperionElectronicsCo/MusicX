package com.hyperion.musicx.libraryadapters.settings;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.support.v7.widget.RecyclerView;
import java.text.DecimalFormat;
import java.util.List;
import com.hyperion.musicx.R;


public class CacheAdapter extends RecyclerView.Adapter<CacheAdapter.ViewHolder> {
    private List<CacheItem> itemList;

    public CacheAdapter(List<CacheItem> itemList) { this.itemList = itemList; }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cache, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CacheItem item = itemList.get(position);
        holder.tvName.setText(item.getName());
        holder.tvSize.setText("Clear " + formatSize(item.getSizeInBytes()) + " of data");
    }

    @Override
    public int getItemCount() { return itemList.size(); }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvSize;
        public ViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tvItemName);
            tvSize = (TextView) itemView.findViewById(R.id.tvItemSize);
        }
    }

    private String formatSize(long bytes) {
        if (bytes <= 0) return "0 B";
        int digitGroups = (int) (Math.log10(bytes) / Math.log10(1024));
        return new DecimalFormat("#,##0.##").format(bytes / Math.pow(1024, digitGroups)) + " " + new String[]{"B", "KB", "MB", "GB"}[digitGroups];
    }
}

