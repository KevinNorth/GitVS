package edu.unl.cse.knorth.git_sonification.data_collection.git_graph_caller;

import edu.unl.cse.knorth.git_sonification.data_collection.intermediate_data.Commit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class GitGraph {
    private final List<GitGraphRow> rows;
    private final HashMap<String, GitGraphRow> rowsTable;
    private final HashMap<String, Integer> rowsPositionTable;
    private final Comparator<Commit> commitComparator;
    
    public GitGraph(Collection<GitGraphRow> rows) {
        this.rows = new ArrayList<>(rows);
        this.rowsTable = new HashMap<>(rows.size());
        this.rowsPositionTable = new HashMap<>(rows.size());
        
        for(int i = 0; i < rows.size(); i++) {
            GitGraphRow row = this.rows.get(i);
            rowsTable.put(row.getCommitHash(), row);
            rowsPositionTable.put(row.getCommitHash(), i);
        }
        
        final GitGraph self = this;
        this.commitComparator = (Commit c1, Commit c2) -> {
            int c1pos = self.rowsPositionTable.get(c1.getHash());
            int c2pos = self.rowsPositionTable.get(c2.getHash());
            
            return -Integer.compare(c1pos, c2pos);
        };
    }
    
    public List<GitGraphRow> getRows() {
        return rows;
    }
    
    public Comparator<Commit> getCommitComparator() {
        return commitComparator;
    }
    
    public int getPositionOfCommit(String hash) {
        if(rowsPositionTable.containsKey(hash)) {
            return rowsPositionTable.get(hash);
        } else {
            return -1;
        }
    }
    
    public GitGraphRow getRowForCommit(String hash) {
        if(rowsPositionTable.containsKey(hash)) {
            return rowsTable.get(hash);
        } else {
            return null;
        }
    }
}