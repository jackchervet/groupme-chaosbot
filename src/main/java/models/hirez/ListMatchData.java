package models.hirez;

import models.actions.BeforeResult;

import java.util.List;

public class ListMatchData extends BeforeResult {
    private final List<MatchData> data;

    public ListMatchData(List<MatchData> data) {
        this.data = data;
    }

    public List<MatchData> getData() {
        return data;
    }
}
