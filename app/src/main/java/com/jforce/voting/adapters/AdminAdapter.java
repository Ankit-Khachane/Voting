package com.jforce.voting.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jforce.voting.R;
import com.jforce.voting.api.Candidates;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AdminAdapter extends BaseAdapter {
    private final Drawable avatar;
    private Context mContext;
    private List<Candidates> mCandidates = new ArrayList<>();

    public AdminAdapter(Context mContext, List<Candidates> mCandidates) {
        this.mContext = mContext;
        this.mCandidates = mCandidates;
        avatar = mContext.getDrawable(R.drawable.ic_avatar);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_admin_row, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Candidates candidate = mCandidates.get(position);
        holder.imageView.setImageDrawable(avatar);
        holder.candidatenameTv.setText(candidate.getCandidateName());
        holder.votecountTv.setText(String.valueOf(candidate.getVoteCount()));

        return convertView;
    }

    @Override
    public int getCount() {
        return mCandidates.size();
    }

    @Override
    public Object getItem(int position) {
        return mCandidates.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    static
    class ViewHolder {
        @BindView(R.id.imageView)
        ImageView imageView;
        @BindView(R.id.candidate_name_tv)
        TextView candidatenameTv;
        @BindView(R.id.vote_count_tv)
        TextView votecountTv;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
