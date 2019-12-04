package com.jforce.voting.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jforce.voting.R;

import java.util.List;

public class VotersAdapter extends RecyclerView.Adapter<VotersAdapter.Holder> {
    private Context mContext;
    private List<String> voterNames;

    public VotersAdapter(Context mContext, List<String> voterNames) {
        this.mContext = mContext;
        this.voterNames = voterNames;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Holder mHolder;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View row = inflater.inflate(R.layout.voterlist_row_item, viewGroup, false);
        mHolder = new Holder(row);
        return mHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int i) {
        String name = voterNames.get(i);
        holder.voterName.setText(name);
    }

    @Override
    public int getItemCount() {
        return voterNames.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        private TextView voterName;

        public Holder(@NonNull View itemView) {
            super(itemView);
            voterName = itemView.findViewById(R.id.votername_tv);

        }
    }
}
