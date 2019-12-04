package com.jforce.voting.api;

import androidx.annotation.NonNull;

public class Candidates {
    public String objectId;

    public String candidateName;
    public int voteCount;

    public Candidates() {
    }

    public Candidates(String objectId, String candidateName, int voteCount) {
        this.objectId = objectId;
        this.candidateName = candidateName;
        this.voteCount = voteCount;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getCandidateName() {
        return candidateName;
    }

    public void setCandidateName(String candidateName) {
        this.candidateName = candidateName;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    @NonNull
    @Override
    public String toString() {
        return "objectId = " + objectId + "name = " + candidateName + "voteCount = " + voteCount;
    }
}
