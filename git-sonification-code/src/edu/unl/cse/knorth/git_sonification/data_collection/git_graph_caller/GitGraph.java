package edu.unl.cse.knorth.git_sonification.data_collection.git_graph_caller;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class GitGraph {
    private final List<Row> rows;
    private final HashMap<String, Integer> rowsPositionTable;
    private final Comparator<String> hashComparator;
    
    public GitGraph(Collection<Row> rows) {
        this.rows = new LinkedList<>(rows);
        this.rowsPositionTable = new HashMap<>(rows.size());
        
        for(int i = 0; i < rows.size(); i++) {
            rowsPositionTable.put(this.rows.get(i).getCommitHash(), i);
        }
        
        final GitGraph self = this;
        this.hashComparator = new Comparator<String>() {
            @Override
            public int compare(String hash1, String hash2) {
                int hash1pos = self.rowsPositionTable.get(hash1);
                int hash2pos = self.rowsPositionTable.get(hash2);
                
                return Integer.compare(hash1pos, hash2pos);
            }
        };
    }
    
    public List<Row> getRows() {
        return rows;
    }
    
    public Comparator<String> getHashComparator() {
        return hashComparator;
    }
}