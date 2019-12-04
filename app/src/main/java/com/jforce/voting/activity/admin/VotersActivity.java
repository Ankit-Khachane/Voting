package com.jforce.voting.activity.admin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;
import com.jforce.voting.R;
import com.jforce.voting.adapters.VotersAdapter;
import com.jforce.voting.api.Keys;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VotersActivity extends AppCompatActivity {
    private static final String TAG = "VotersActivity";

    @BindView(R.id.voterlist_rcv)
    RecyclerView voterlistRcv;
    private VotersAdapter mAdapter;
    private List<String> mVoters;
    private String objecetId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voters);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        objecetId = intent.getStringExtra(AdminActivity.SELECTED_cANDIDATE_ID_KEY);
        Log.i(TAG, "onCreate: objectID = " + objecetId);

        mVoters = new ArrayList<>();
        mAdapter = new VotersAdapter(getApplicationContext(), mVoters);
        voterlistRcv.setAdapter(mAdapter);
        voterlistRcv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        fetchVotedUsers();


    }

    private void fetchVotedUsers() {
        DataQueryBuilder queryBuilder = DataQueryBuilder.create().setWhereClause(String.format("votedFor='%s'", objecetId));
        Backendless.Data.of(Keys.User.TABLE).find(queryBuilder, new AsyncCallback<List<Map>>() {
            @Override
            public void handleResponse(List<Map> users) {
                if (users != null) {
                    for (int i = 0; i < users.size(); i++) {
                        Map user = users.get(i);
                        String name = (String) user.get("name");
                        mVoters.add(name);
                        Log.i(TAG, "handleResponse: " + user.get("name"));
                    }
                    mAdapter.notifyDataSetChanged();
                } else {
                    Log.i(TAG, "handleResponse: No User Exists");
                }
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Log.i(TAG, "handleFault: " + fault);
            }
        });


    }
}
