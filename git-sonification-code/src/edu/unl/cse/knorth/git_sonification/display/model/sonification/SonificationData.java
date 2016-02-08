package edu.unl.cse.knorth.git_sonification.display.model.sonification;

import java.util.List;

public class SonificationData {
    private final List<String> authorsInOrderOfCommitCounts;
  
    public SonificationData(List<String> authorsInOrderOfCommitCounts) {
      this.authorsInOrderOfCommitCounts = authorsInOrderOfCommitCounts;
    }

    public List<String> getAuthorsInOrderOfCommitCounts() {
        return authorsInOrderOfCommitCounts;
    }
}
