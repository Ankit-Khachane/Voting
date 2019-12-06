package com.jforce.voting.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.jforce.voting.R;
import com.jforce.voting.api.Candidates;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CandidateAdapter extends BaseAdapter {

    private Context mContext;
    private Drawable avatar;
    private List<Candidates> candidatesList;

    public CandidateAdapter(Context mContext, List<Candidates> candidatesList) {
        this.mContext = mContext;
        this.candidatesList = candidatesList;
        avatar = mContext.getDrawable(R.drawable.ic_avatar);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder mHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_candiate_row, parent, false);
            mHolder = new ViewHolder(convertView);
            convertView.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) convertView.getTag();
        }

        Candidates candidates = candidatesList.get(position);
        mHolder.candidatenameTv.setText(candidates.getCandidateName());
        mHolder.radioBtn.setChecked(false);
        mHolder.imageView.setImageDrawable(avatar);

        return convertView;
    }

    @Override
    public int getCount() {
        return candidatesList.size();
    }

    @Override
    public Object getItem(int position) {
        return candidatesList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static
    class ViewHolder {
        @BindView(R.id.radio_btn)
        RadioButton radioBtn;
        @BindView(R.id.imageView)
        ImageView imageView;
        @BindView(R.id.candidatename_tv)
        TextView candidatenameTv;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
