package com.jforce.voting;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.jforce.voting.api.Candidates;
import com.jforce.voting.api.Keys;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class VotingActivity extends AppCompatActivity {
    private static final String TAG = "VotingActivity";

    @BindView(R.id.candiate_lst)
    ListView candidateLast;
    @BindView(R.id.vote_btn)
    Button voteBtn;

    private List<Candidates> mCandidatesList;
    private boolean isCandidateSelected, isUserVoted = false;
    private BackendlessUser mUser;
    private CandidateAdapter mAdapter;
    private Candidates selectedCandidates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voting);
        ButterKnife.bind(this);
        isCandidateSelected = false;
        voteBtn.setEnabled(false);
        mUser = Backendless.UserService.CurrentUser();
        if (mUser != null) {
            isUserVoted = (boolean) mUser.getProperty(Keys.User.IS_VOTED);
            if (isUserVoted) {
                startActivity(new Intent(getApplicationContext(), VotedActivity.class));
                this.finish();
            } else {
                Log.i(TAG, "onCreate: isUserVoted = " + isUserVoted);
                Log.i(TAG, "onCreate: mUser = " + mUser.getUserId());
            }
        } else {
            isUserVoted = false;
            Log.i(TAG, "onCreate: isUserVoted = " + false);
        }

        mCandidatesList = new ArrayList<Candidates>();
        mAdapter = new CandidateAdapter(getApplicationContext(), mCandidatesList);
        candidateLast.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        candidateLast.setAdapter(mAdapter);
        getCandidates();
        candidateLast.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RadioButton radioButton = view.findViewById(R.id.radio_btn);
                radioButton.setChecked(true);
                voteBtn.setEnabled(true);
                candidateLast.setEnabled(false);
                selectedCandidates = mCandidatesList.get(position);
                if (selectedCandidates != null) {
                    isCandidateSelected = true;
                }
                Toast.makeText(VotingActivity.this, "Candidate Selected", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getCandidates() {
        Backendless.Data.of(Candidates.class).find(
                new AsyncCallback<List<Candidates>>() {
                    @Override
                    public void handleResponse(List<Candidates> response) {
                        if (response != null) {
                            //mCandidatesList.addAll(response);
                            for (Candidates candidate : response) {
                                mCandidatesList.add(new Candidates(
                                        candidate.getObjectId(),
                                        candidate.getCandidateName(),
                                        candidate.getVoteCount()));
                            }
                            if (mCandidatesList != null) {
                                mAdapter.notifyDataSetChanged();
                            } else {
                                Log.i(TAG, "getCandidates() : mCandidatesList " + mCandidatesList.size());
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

    @OnClick(R.id.vote_btn)
    public void onViewClicked() {
        if (mUser != null && isCandidateSelected) {
            mUser.setProperty(Keys.User.IS_VOTED, true);
            mUser.setProperty(Keys.User.VOTED_FOR, selectedCandidates.getObjectId());
            Backendless.UserService.update(mUser,
                    new AsyncCallback<BackendlessUser>() {
                        @Override
                        public void handleResponse(BackendlessUser response) {
                            if (response != null) {
                                incrementVote();
                                Log.i(TAG, "Vote Button Clicked() : Vote Details Updated");
                                Toast.makeText(VotingActivity.this, "Vote Recorded on Server", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(VotingActivity.this, "Vote Failed", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void handleFault(BackendlessFault fault) {
                            Log.i(TAG, "Vote Button Clicked : Vote Updating Failed" + fault.getMessage());

                        }
                    });
        }
    }


    private void incrementVote() {
        Backendless.Data.of(Candidates.class).findById(selectedCandidates,
                new AsyncCallback<Candidates>() {
                    @Override
                    public void handleResponse(Candidates candidate) {
                        if (candidate != null) {
                            int voteCount = candidate.getVoteCount();
                            voteCount = voteCount + 1;
                            candidate.setVoteCount(voteCount);
                            Backendless.Persistence.save(candidate, new AsyncCallback<Candidates>() {
                                @Override
                                public void handleResponse(Candidates response) {
                                    if (response != null) {
                                        gotoVotedActivity();
                                        Log.i(TAG, "incrementVote() : VoteCount Updated for " + response.getCandidateName());
                                    }
                                }

                                @Override
                                public void handleFault(BackendlessFault fault) {
                                    Log.i(TAG, "incrementVote() : VoteCount Not Updated " + fault);
                                }
                            });
                        } else {
                            Log.i(TAG, "incrementVote() : Candidates is Null");
                        }
                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {
                        Log.i(TAG, "incrementVote() : increment block Problem : " + fault.getMessage());
                    }
                });
    }

    private void gotoVotedActivity() {
        Intent intent = new Intent(getApplicationContext(), VotedActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
