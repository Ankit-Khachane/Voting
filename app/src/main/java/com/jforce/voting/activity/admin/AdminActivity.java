package com.jforce.voting.activity.admin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.jforce.voting.R;
import com.jforce.voting.adapters.AdminAdapter;
import com.jforce.voting.api.Candidates;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;

public class AdminActivity extends AppCompatActivity {
    public static final String SELECTED_cANDIDATE_ID_KEY = "candidateId_key";
    private static final String TAG = "AdminActivity";
    @BindView(R.id.candidate_lst)
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

    @OnItemClick(R.id.candidate_lst)
    void OnCandidateClicked(int position) {
        Log.i(TAG, "OnCandidateClicked: clicked !");
        Intent intent = new Intent(getApplicationContext(), VotersActivity.class);
        Candidates mSelectedCandidate = mCandidates.get(position);
        Toast.makeText(this, "Selected " + mSelectedCandidate.getCandidateName(), Toast.LENGTH_SHORT).show();
        intent.putExtra(SELECTED_cANDIDATE_ID_KEY, mSelectedCandidate.getObjectId());
        startActivity(intent);
    }
}
