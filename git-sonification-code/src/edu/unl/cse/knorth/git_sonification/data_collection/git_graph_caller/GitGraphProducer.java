package edu.unl.cse.knorth.git_sonification.data_collection.git_graph_caller;

import edu.unl.cse.knorth.git_sonification.utility.StreamGobbler;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class GitGraphProducer {
    public GitGraph produceGitGraph(String repositoryLocation)
            throws IOException {
        List<String> rawGraphData = callGitLog(repositoryLocation);
        List<GitGraphRow> rows = processRows(rawGraphData);
        return new GitGraph(rows);
    }
    
    public GitGraph produceGitGraph(List<String> rawGraphData) {
        return new GitGraph(processRows(rawGraphData));
    }
    
    private List<String> callGitLog(String repositoryLocation)
            throws IOException {
        File workingDirectory = new File(repositoryLocation);
        
        String commandLineString = "git log --format=%H --graph"
                + " --author-date-order --no-color";
        Runtime rt = Runtime.getRuntime();
        Process pr = rt.exec(commandLineString.trim().split("\\s+"), null,
                workingDirectory);

        System.out.println("Running `" + commandLineString + "` in order to "
                + "find git commits...");
        
        // Output the process's output to STDOUT so the end user can see it
        StreamGobbler errorGobbler = new StreamGobbler(pr.getErrorStream());
        errorGobbler.start();
        
        try {            
            List<String> rawGraphData = new LinkedList<String>();
            InputStreamReader isr = new InputStreamReader(pr.getInputStream());
            BufferedReader br = new BufferedReader(isr);
            String line;
            while ( (line = br.readLine()) != null)
                rawGraphData.add(line);

            return rawGraphData;
        } finally {
            try {
                errorGobbler.join(100);
            } catch(InterruptedException err) { /* Swallow on purpose */ }
        }
    }
    
    private List<GitGraphRow> processRows(List<String> rawGraphData) {
        LinkedList<GitGraphRow> rows = new LinkedList<>();
        
        String currentHash = null;
        int currentPosition = 1;
        ArrayList<GitGraphLine> currentLines = null;
        
        for(String rawRow : rawGraphData) {
            String[] split = rawRow.split("\\s+");
            String hash = split[split.length - 1];
            if(hash.matches("[a-fA-F0-9]+")) {
                if(hash.equals("bdbec85")) {
                    int i = 0;
                }
                
                HashPositionAndLines result =
                        processRawRowWithHash(rawRow, hash);
                
                // We only add new rows once we now for sure what their incoming
                // lines are, which isn't possible until we've reached the next
                // row with a hash.
                if(currentHash != null) {
//                    removeDuplicateLines(currentLines);
                    rows.add(new GitGraphRow(currentHash, currentPosition,
                            currentLines));
                }
                
                currentHash = result.getHash();
                currentPosition = result.getPosition();
                currentLines = result.getLines();
            } else {
                ArrayList<GitGraphLine> newLines =
                        processRawRowWithoutHash(rawRow);
                currentLines = updateCurrentLinesWithNewLines(currentLines,
                        newLines, currentPosition);
            }
        }

        // Be sure to include the final row
        if(currentHash != null) {
            // Because it's the last row, the final row won't have any incoming
            // lines!
            currentLines = new ArrayList<>();
            rows.add(new GitGraphRow(currentHash, currentPosition,
                    currentLines));
        }
        
        return rows;
    }

    private HashPositionAndLines processRawRowWithHash(String rawRow, String hash) {
        int endIndex = rawRow.length() - hash.length();
        String rowWithoutHash = rawRow.substring(0, endIndex);

        // '*' indicates an actual commit in git log --graph's output, so we
        // get the commit's position and then replace it with a character
        // indicating that the branch continues straght through the commit.
        int strPosition = rowWithoutHash.indexOf('*');
        // Account for whitespace in the string and the string 0-indexing
        int branchPosition = (strPosition / 2) + 1;
        rowWithoutHash = rowWithoutHash.replace('*', '|');
        
        ArrayList<GitGraphLine> lines =
                processRawRowWithoutHash(rowWithoutHash);
        return new HashPositionAndLines(hash, branchPosition, lines);
    }

    private ArrayList<GitGraphLine> processRawRowWithoutHash(String rawRow) {
        ArrayList<GitGraphLine> lines = new ArrayList<>(rawRow.length() / 2);
        
        boolean processingHorizontalLine = false;
        boolean isHorizonalLineOutgoingPositionSet = false;
        GitGraphLine horizontalLine = null;
        
        for(int i = 0; i < rawRow.length(); i++) {
            char c = rawRow.charAt(i);

            if(c == ' ') {
                if(processingHorizontalLine) {
                    // A space can indicate the end of a horizontal line, i.e.
                    // | | \_|_|_| |
                    processingHorizontalLine = false;
                    lines.add(horizontalLine);
                }
            } else if(c == '|') {
                // A pipe indicates that a branch continues along the same
                // position
                int branchPosition = (i / 2) + 1;
                lines.add(new GitGraphLine(branchPosition, branchPosition));
            } else if(c == '\\') {
                // A backslash can mean one of two things:
                // 1. A branch is moving (or merging) one position to the left,
                // i.e.
                // | | |\|  \  |
                // 2. Cap a horizontal line to the left, i.e.
                // | |\|_|_|_| |
                int outgoingBranchPosition = ((i - 1) / 2) + 1;
                int incomingBranchPosition = ((i + 1) / 2) + 1;
                GitGraphLine line = new GitGraphLine(incomingBranchPosition,
                    outgoingBranchPosition);
                
                boolean startsHorizontalLine;
                
                if(i + 2 >= rawRow.length()) {
                    startsHorizontalLine = false;
                } else {
                    char lookahead = rawRow.charAt(i + 2);
                    startsHorizontalLine = (lookahead == '_');
                }
                
                if(startsHorizontalLine) {
                    processingHorizontalLine = true;
                    isHorizonalLineOutgoingPositionSet = true;
                    horizontalLine = line;
                } else {
                    lines.add(line);
                }
            } else if(c == '/') {
                // A forwards slash can mean one of two things:
                // 1. A branch is moving (or branching) one position to the
                // right, i.e.
                // | | |/|  /  |
                // 2. Cap a horizontal line to the right, i.e.
                // | | |_|_|_|/|
                int outgoingBranchPosition = ((i + 1) / 2) + 1;
                int incomingBranchPosition = ((i - 1) / 2) + 1;
                
                if(processingHorizontalLine) {
                    horizontalLine.setToBranch(outgoingBranchPosition);
                    processingHorizontalLine = false;
                    lines.add(horizontalLine);
                } else {
                    lines.add(new GitGraphLine(incomingBranchPosition,
                            outgoingBranchPosition));
                }
            } else if(c == '_' || c == '-') {
                int leftBranchPosition = ((i - 1) / 2) + 1;
                int rightBranchPosition = ((i + 1) / 2) + 1;
                // Underscores continue horizontal lines
                if(processingHorizontalLine) {
                    if(isHorizonalLineOutgoingPositionSet) {
                        horizontalLine.setFromBranch(rightBranchPosition);
                    } else {
                        horizontalLine.setToBranch(rightBranchPosition);
                    }
                } else {
                    // When '_' shows up without a backslash in front of it,
                    // the line goes to the right, i.e.
                    // | |_|_|/
                    //
                    // But when '-' shows up without a '.' in front of it, the
                    // line goes to the left, i.e.
                    // |----.
                    if(c == '_') {
                        processingHorizontalLine = true;
                        horizontalLine = new GitGraphLine(leftBranchPosition,
                                rightBranchPosition);
                        isHorizonalLineOutgoingPositionSet = false;
                    } else {
                        processingHorizontalLine = true;
                        horizontalLine = new GitGraphLine(rightBranchPosition,
                                leftBranchPosition);
                        isHorizonalLineOutgoingPositionSet = true;                        
                    }
                }
            } else if(c == '.') {
                // In rare cases, git log --graph will use a '.' and '-'s to
                // indicate the end of a horizontal line, i.e.
                // | |-.
                // instead of
                // | |\_
                if(processingHorizontalLine) {
                    processingHorizontalLine = false;
                    lines.add(horizontalLine);
                } else {
                    // If a period starts a horizontal line, treat it like a
                    // forward slash, i.e.
                    // | .-|
                    // is the same as
                    // |  /|
                    //(|/  |)
                    processingHorizontalLine = true;                    
                    isHorizonalLineOutgoingPositionSet = false;
                    int branchPosition = (i / 2) + 1;
                    horizontalLine = new GitGraphLine(branchPosition,
                            branchPosition);
                }
            }
        }
        
        // We might still be processing a horizontal line, i.e.
        // | |\|_|_|_|_   <----
        // | | | | | | \
        // | | | | | | |
        if(processingHorizontalLine) {
            lines.add(horizontalLine);
        }
        
        return lines;
    }

    private ArrayList<GitGraphLine> updateCurrentLinesWithNewLines(
            ArrayList<GitGraphLine> topLines,
            ArrayList<GitGraphLine> bottomLines,
            int branchWithCommit) {
        // In this algorithm, we do a lot of operations that are O(n^2), but the
        // overwhelming majority of Git repositories have at the most 20-30
        // simulaneous branches in any row of their Git graph, so the actual
        // cost in this situation is negligable.
        ArrayList<GitGraphLine> unusedTopLines = new ArrayList<>(topLines);
        ArrayList<GitGraphLine> unusedBottomLines =
                new ArrayList<>(bottomLines);
        ArrayList<GitGraphLine> updatedLines = new ArrayList<>(topLines.size()
                + bottomLines.size());
        
        LinkedList<GitGraphLine> topLinesToRemove = new LinkedList<>();
        LinkedList<GitGraphLine> bottomLinesToRemove = new LinkedList<>();
        for(GitGraphLine topLine : unusedTopLines) {
            if(topLine.getFromBranch() == topLine.getToBranch()) {
                int branchNum = topLine.getToBranch();
                for(GitGraphLine bottomLine : unusedBottomLines) {
                    if((bottomLine.getFromBranch() == bottomLine.getToBranch())
                           && (bottomLine.getToBranch() == branchNum)) {
                        updatedLines.add(topLine);
                        topLinesToRemove.add(topLine);
                        bottomLinesToRemove.add(bottomLine);
                    }
                }
            }
        }
        
        for(GitGraphLine topLine : topLinesToRemove) {
            unusedTopLines.remove(topLine);
        }
        for(GitGraphLine bottomLine : bottomLinesToRemove) {
            unusedBottomLines.remove(bottomLine);
        }
        
        for(GitGraphLine bottomLine : unusedBottomLines) {
            boolean usedBottomLine = false;
            for(GitGraphLine topLine : unusedTopLines) {
                if(bottomLine.getToBranch() == topLine.getFromBranch()) {
                    GitGraphLine newLine = new GitGraphLine(
                            bottomLine.getFromBranch(), topLine.getToBranch());
                    updatedLines.add(newLine);
                    usedBottomLine = true;
                    break;
                }
            }
            
            if(!usedBottomLine) {
                // Whenever there's a merge of more than 2 commits at once,
                // the top line will have one long horizontal line for all of
                // the multimerged branches, but the next line will have
                // multiple slashes. We detect this situation by finding bottom
                // lines that don't match to anything in the top line, then
                // putting them all into the merged branch.
                /* i.e.
                   *-----.    <-------
                   |\ \ \ \   <-------
                   | | | | *
                   | | | * |
                   | | | |/
                   | | * |
                   | | |/
                   | * |
                   | |/
                   * |
                   |/ 
                   * */
                GitGraphLine newLine = new GitGraphLine(
                            bottomLine.getFromBranch(), branchWithCommit);
                    updatedLines.add(newLine);
            }
        }
        
        return updatedLines;
    }

//    private void removeDuplicateLines(ArrayList<GitGraphLine> currentLines) {
//        Iterator<GitGraphLine> iter = currentLines.iterator();
//
//        int i = 0;
//        while(iter.hasNext()) {
//            GitGraphLine line1 = iter.next();
//            boolean foundMatch = false;
//            
//            for(int j = i + 1; j < currentLines.size(); j++) {
//                GitGraphLine line2 = currentLines.get(j);
//                
//                if((line1.getFromBranch() == line2.getFromBranch())
//                        && (line1.getToBranch() == line2.getToBranch())) {
//                    foundMatch = true;
//                    break;
//                }
//            }
//            
//            if(foundMatch) {
//                iter.remove();
//            }
//            
//            i++;
//        }
//    }
    
    private static class HashPositionAndLines {
        private String hash;
        private int position;
        private ArrayList<GitGraphLine> lines;

        public HashPositionAndLines(String hash, int position,
                Collection<GitGraphLine> lines) {
            this.hash = hash;
            this.position = position;
            this.lines = new ArrayList<>(lines);
        }

        public String getHash() {
            return hash;
        }

        public void setHash(String hash) {
            this.hash = hash;
        }

        public int getPosition() {
            return position;
        }

        public void setPosition(int position) {
            this.position = position;
        }
        
        public ArrayList getLines() {
            return lines;
        }

        public void setLines(ArrayList<GitGraphLine> lines) {
            this.lines = lines;
        }
    }
}