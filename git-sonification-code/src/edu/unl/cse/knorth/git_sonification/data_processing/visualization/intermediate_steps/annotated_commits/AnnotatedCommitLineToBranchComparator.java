package edu.unl.cse.knorth.git_sonification.data_processing.visualization.intermediate_steps.annotated_commits;

import java.util.Comparator;

public class AnnotatedCommitLineToBranchComparator
    implements Comparator<AnnotatedCommitLine> {

    @Override
    public int compare(AnnotatedCommitLine line1, AnnotatedCommitLine line2) {
        if(line1.getToBranch() < line2.getToBranch()) {
            return -1;
        } else if(line1.getToBranch() > line2.getToBranch()) {
            return 1;
        } else {
            return 0;
        }
    }
    
}