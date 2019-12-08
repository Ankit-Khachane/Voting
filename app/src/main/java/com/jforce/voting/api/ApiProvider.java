package com.jforce.voting.api;

import android.util.Log;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ApiProvider {
    private static final String TAG = "ApiProvider";

    public boolean register(String name, String email, String password) {
        boolean[] isRegistered = {false};
        BackendlessUser user = new BackendlessUser();
        user.setEmail(email);
        user.setPassword(password);
        user.setProperty(Keys.User.NAME, name);
        Backendless.UserService
                .register(user, new AsyncCallback<BackendlessUser>() {
                    @Override
                    public void handleResponse(BackendlessUser response) {
                        if (response != null) {
                            isRegistered[0] = true;
                            Log.i(TAG, "register(): Success");
                        }
                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {
                        Log.e(TAG, "register() : Failed " + fault);


                    }
                });
        return isRegistered[0];
    }

    public boolean login(String email, String password) {
        boolean[] isAuthorized = {false};
        Backendless.UserService
                .login(email, password, new AsyncCallback<BackendlessUser>() {
                    @Override
                    public void handleResponse(BackendlessUser response) {
                        if (response != null) {
                            isAuthorized[0] = true;
                            Log.i(TAG, "login() : Success");
                        }
                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {
                        Log.e(TAG, "login() : Failed " + fault);

                    }
                });
        return isAuthorized[0];
    }

    public List<Candidates> getAllCandidates() {
        List<Candidates> candidates = new ArrayList<>();
        Backendless.Data
                .of(Candidates.class)
                .find(new AsyncCallback<List<Candidates>>() {
                    @Override
                    public void handleResponse(List<Candidates> response) {
                        if (response != null) {
                            candidates.addAll(response);
                        } else {
                            Log.e(TAG, "getAllCandidates() : fetching all candidate failed");
                        }
                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {
                        Log.e(TAG, "getAllCandidates() : " + fault);
                    }
                });
        return candidates;
    }

    public boolean updateVoteStatus(BackendlessUser user, String objectId) {
        boolean[] voteUpdated = {false};
        boolean[] voteTotalIncremented = {false};

        user.setProperty(Keys.User.IS_VOTED, true);
        user.setProperty(Keys.User.VOTED_FOR, objectId);
        Backendless.UserService
                .update(user,
                        new AsyncCallback<BackendlessUser>() {
                            @Override
                            public void handleResponse(BackendlessUser response) {
                                if (response != null) {
                                    voteUpdated[0] = true;
                                    voteTotalIncremented[0] = incrementVoteTotal(objectId);
                                    Log.i(TAG, "updateVote() : Success");
                                } else {
                                    Log.e(TAG, "updateVote() : Failed");
                                }
                            }

                            @Override
                            public void handleFault(BackendlessFault fault) {
                                Log.e(TAG, "updateVote() : Failed" + fault);
                            }
                        });
        return voteUpdated[0];
    }

    public boolean incrementVoteTotal(String selectedCandidateId) {
        boolean[] voteIncremented = {false};
        Backendless.Data
                .of(Candidates.class)
                .findById(selectedCandidateId, new AsyncCallback<Candidates>() {
                    @Override
                    public void handleResponse(Candidates response) {
                        if (response != null) {
                            voteIncremented[0] = true;
                            Log.i(TAG, "incrementVoteTotal() : Success");
                        } else {
                            Log.e(TAG, "incrementVoteTotal() : Failed");
                        }
                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {
                        Log.e(TAG, "incrementVoteTotal() : Failed" + fault);
                    }
                });
        return voteIncremented[0];
    }

    public List<String> getVoter(String objectId) {
        List<String> mVoters = new ArrayList<>();
        DataQueryBuilder queryBuilder = DataQueryBuilder
                .create()
                .setWhereClause(String.format("votedFor='%s'", objectId));

        Backendless.Data.of(Keys.User.TABLE)
                .find(queryBuilder, new AsyncCallback<List<Map>>() {
                    @Override
                    public void handleResponse(List<Map> users) {
                        if (users != null) {
                            for (int i = 0; i < users.size(); i++) {
                                Map user = users.get(i);
                                String name = (String) user.get("name");
                                mVoters.add(name);
                                Log.i(TAG, "getVoter() : " + user.get("name"));
                            }
                        } else {
                            Log.i(TAG, "getVoter() : No User Exists");
                        }
                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {
                        Log.e(TAG, "getVoter() : Failed" + fault);
                    }
                });
        return mVoters;
    }

}
