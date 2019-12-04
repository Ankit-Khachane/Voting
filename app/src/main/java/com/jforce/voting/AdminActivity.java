package com.jforce.voting;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.jforce.voting.api.Candidates;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemSelected;

public class AdminActivity extends AppCompatActivity {
    private static final String TAG = "AdminActivity";

    @BindView(R.id.candiate_lst)
    ListView mCandidatesView;
    private AdminAdapter mAdapter;
    private List<Candidates> mCandidates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        ButterKnife.bind(this);
        mCandidates = new ArrayList<>();
        mAdapter = new AdminAdapter(getApplicationContext(), mCandidates);
        mCandidatesView.setAdapter(mAdapter);
        getCandidates();
    }

    private void getCandidates() {
        Backendless.Data.of(Candidates.class).find(
                new AsyncCallback<List<Candidates>>() {
                    @Override
                    public void handleResponse(List<Candidates> response) {
                        if (response != null) {
                            //mCandidatesList.addAll(response);
                            for (Candidates candidate : response) {
                                mCandidates.add(new Candidates(
                                        candidate.getObjectId(),
                                        candidate.getCandidateName(),
                                        candidate.getVoteCount()));
                            }
                            if (mCandidates != null) {
                                mAdapter.notifyDataSetChanged();
                            } else {
                                Log.i(TAG, "getCandidates() : mCandidatesList " + mCandidates.size());
                            }
                        }
                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {
                        Log.i(TAG, "getAllCandidates () : " + fault.getMessage());

                    }
                }
        );

    }

    @OnItemSelected(value = R.id.candiate_lst, callback = OnItemSelected.Callback.ITEM_SELECTED)
    public void OnCandidateSelected(int position) {
        Intent intent = new Intent(getApplicationContext(), VotersActivity.class);
        Candidates mSelectedCandidate = mCandidates.get(position);
        Toast.makeText(this, "Selected " + mSelectedCandidate.getCandidateName(), Toast.LENGTH_SHORT).show();
        intent.putExtra("candidateId", mSelectedCandidate.getObjectId());
    }
}
