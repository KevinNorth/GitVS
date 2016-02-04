package edu.unl.cse.knorth.git_sonification.data_collection.git_graph_caller;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;

public class GitGraphProducerTest {
    
    public GitGraphProducerTest() {
    }

    @Test
    public void testProduceGitGraph() {
        List<String> rawGraphData = new LinkedList<String>();
        rawGraphData.add("*-----.   a010bc783d595b65a3a2ac16834b09ed18c4f0c4");
        rawGraphData.add("|\\ \\ \\ \\ ");
        rawGraphData.add("| | | | * 81c83fac5fb97c89e4f74b2df0231aae7c4e83ee");
        rawGraphData.add("| |_|_|/");
        rawGraphData.add("|/| | |");
        rawGraphData.add("* | | | 2da4611d4e0c5c0311303681a6deb6a4c0adb963");
        rawGraphData.add("|\\|_| |");
        rawGraphData.add("| | |\\|");
        rawGraphData.add("| | | * 7d77a30151146358e7aa8f9a6ada21786cd6d727");
        rawGraphData.add("| | |/");
        rawGraphData.add("| | * 71565b5b1f02f67dbbe35bfc258087464e5655e4");
        rawGraphData.add("| * | 95dd79f0a266efc10fa648fc36b3ec834a46d3a5");
        rawGraphData.add("| |/  ");
        rawGraphData.add("* | 0d676aea47e1d1bf5b286944f1587692d9f7c5c0");
        rawGraphData.add("|/  ");
        rawGraphData.add("* 89e6efae9d4318bc208e30025ac26e08a76e5a18");
        rawGraphData.add("*   2e117eea811555bac4a864e2d4577bd1183002be");
        rawGraphData.add("|\\  ");
        rawGraphData.add("| * a0f7fa5fe6485729aad0dd9f656cd280c61716de");
        rawGraphData.add("| * b37c64283d7c3025b679bc0da3f3e2aa475e52f7");
        rawGraphData.add("* |   4114c5219e3395fd8e2744bd0a24cf25b2632d78");
        rawGraphData.add("|\\ \\  ");
        rawGraphData.add("| |/  ");
        rawGraphData.add("* | 94eb2099d0832dd126c6e90e6d035e5f75ce240b");
        rawGraphData.add("| * 81f4594cd22c1ecaeedd302fb872df8b90429601");
        rawGraphData.add("* | 5d3792fa4e454e99a743127ce129e81f73cb523a");
        rawGraphData.add("|/  ");
        rawGraphData.add("* 573c7ad54c2d40790964fe8781ebe535bf919fad");
        
        GitGraph gitGraph =
                new GitGraphProducer().produceGitGraph(rawGraphData);
        
        List<GitGraphRow> rows = gitGraph.getRows();
        
        for(GitGraphRow row : gitGraph.getRows()) {
            System.out.println(row.toString());
        }
        
        Assert.assertEquals(16, rows.size());
        
        // *-----.   a010bc783d595b65a3a2ac16834b09ed18c4f0c4
        // |\ \ \ \
        GitGraphRow row = rows.get(0);
        String expectedHash = "a010bc783d595b65a3a2ac16834b09ed18c4f0c4";
        int expectedPosition = 1;
        LinkedList<GitGraphLine> expectedLines = new LinkedList<>();
        expectedLines.add(new GitGraphLine(1, 1));
        expectedLines.add(new GitGraphLine(2, 1));
        expectedLines.add(new GitGraphLine(3, 1));
        expectedLines.add(new GitGraphLine(4, 1));
        expectedLines.add(new GitGraphLine(5, 1));
        Assert.assertTrue(validateRowContents(row, expectedHash,
                expectedPosition, expectedLines));
        
        // | | | | * 81c83fac5fb97c89e4f74b2df0231aae7c4e83ee
        // | |_|_|/
        // |/| | |
        row = rows.get(1);
        expectedHash = "81c83fac5fb97c89e4f74b2df0231aae7c4e83ee";
        expectedPosition = 5;
        expectedLines = new LinkedList<>();
        expectedLines.add(new GitGraphLine(1, 1));
        expectedLines.add(new GitGraphLine(2, 2));
        expectedLines.add(new GitGraphLine(3, 3));
        expectedLines.add(new GitGraphLine(4, 4));
        expectedLines.add(new GitGraphLine(1, 5));
        Assert.assertTrue(validateRowContents(row, expectedHash,
                expectedPosition, expectedLines));
        
        // * | | | 2da4611d4e0c5c0311303681a6deb6a4c0adb963
        // |\|_| |
        // | | |\|
        row = rows.get(2);
        expectedHash = "2da4611d4e0c5c0311303681a6deb6a4c0adb963";
        expectedPosition = 1;
        expectedLines = new LinkedList<>();
        expectedLines.add(new GitGraphLine(1, 1));
        expectedLines.add(new GitGraphLine(2, 2));
        expectedLines.add(new GitGraphLine(3, 3));
        expectedLines.add(new GitGraphLine(4, 4));
        expectedLines.add(new GitGraphLine(4, 1));
        Assert.assertTrue(validateRowContents(row, expectedHash,
                expectedPosition, expectedLines));

        // | | | * 7d77a30151146358e7aa8f9a6ada21786cd6d727
        // | | |/
        row = rows.get(3);
        expectedHash = "7d77a30151146358e7aa8f9a6ada21786cd6d727";
        expectedPosition = 4;
        expectedLines = new LinkedList<>();
        expectedLines.add(new GitGraphLine(1, 1));
        expectedLines.add(new GitGraphLine(2, 2));
        expectedLines.add(new GitGraphLine(3, 3));
        expectedLines.add(new GitGraphLine(3, 4));
        Assert.assertTrue(validateRowContents(row, expectedHash,
                expectedPosition, expectedLines));
        
        // | | * 71565b5b1f02f67dbbe35bfc258087464e5655e4
        row = rows.get(4);
        expectedHash = "71565b5b1f02f67dbbe35bfc258087464e5655e4";
        expectedPosition = 3;
        expectedLines = new LinkedList<>();
        expectedLines.add(new GitGraphLine(1, 1));
        expectedLines.add(new GitGraphLine(2, 2));
        expectedLines.add(new GitGraphLine(3, 3));
        Assert.assertTrue(validateRowContents(row, expectedHash,
                expectedPosition, expectedLines));

        // | * | 95dd79f0a266efc10fa648fc36b3ec834a46d3a5
        // | |/
        row = rows.get(5);
        expectedHash = "95dd79f0a266efc10fa648fc36b3ec834a46d3a5";
        expectedPosition = 2;
        expectedLines = new LinkedList<>();
        expectedLines.add(new GitGraphLine(1, 1));
        expectedLines.add(new GitGraphLine(2, 2));
        expectedLines.add(new GitGraphLine(2, 3));
        Assert.assertTrue(validateRowContents(row, expectedHash,
                expectedPosition, expectedLines));
        
        // * | 0d676aea47e1d1bf5b286944f1587692d9f7c5c0
        // |/
        row = rows.get(6);
        expectedHash = "0d676aea47e1d1bf5b286944f1587692d9f7c5c0";
        expectedPosition = 1;
        expectedLines = new LinkedList<>();
        expectedLines.add(new GitGraphLine(1, 1));
        expectedLines.add(new GitGraphLine(1, 2));
        Assert.assertTrue(validateRowContents(row, expectedHash,
                expectedPosition, expectedLines));

        // * 89e6efae9d4318bc208e30025ac26e08a76e5a18
        row = rows.get(7);
        expectedHash = "89e6efae9d4318bc208e30025ac26e08a76e5a18";
        expectedPosition = 1;
        expectedLines = new LinkedList<>();
        expectedLines.add(new GitGraphLine(1, 1));
        Assert.assertTrue(validateRowContents(row, expectedHash,
                expectedPosition, expectedLines));

        // *   2e117eea811555bac4a864e2d4577bd1183002be
        // |\
        row = rows.get(8);
        expectedHash = "2e117eea811555bac4a864e2d4577bd1183002be";
        expectedPosition = 1;
        expectedLines = new LinkedList<>();
        expectedLines.add(new GitGraphLine(1, 1));
        expectedLines.add(new GitGraphLine(2, 1));
        Assert.assertTrue(validateRowContents(row, expectedHash,
                expectedPosition, expectedLines));

        // | * a0f7fa5fe6485729aad0dd9f656cd280c61716de
        row = rows.get(9);
        expectedHash = "a0f7fa5fe6485729aad0dd9f656cd280c61716de";
        expectedPosition = 2;
        expectedLines = new LinkedList<>();
        expectedLines.add(new GitGraphLine(1, 1));
        expectedLines.add(new GitGraphLine(2, 2));
        Assert.assertTrue(validateRowContents(row, expectedHash,
                expectedPosition, expectedLines));

        // | * b37c64283d7c3025b679bc0da3f3e2aa475e52f7
        row = rows.get(10);
        expectedHash = "b37c64283d7c3025b679bc0da3f3e2aa475e52f7";
        expectedPosition = 2;
        expectedLines = new LinkedList<>();
        expectedLines.add(new GitGraphLine(1, 1));
        expectedLines.add(new GitGraphLine(2, 2));
        Assert.assertTrue(validateRowContents(row, expectedHash,
                expectedPosition, expectedLines));

        // * |   4114c5219e3395fd8e2744bd0a24cf25b2632d78
        // |\ \
        // | |/
        row = rows.get(11);
        expectedHash = "4114c5219e3395fd8e2744bd0a24cf25b2632d78";
        expectedPosition = 1;
        expectedLines = new LinkedList<>();
        expectedLines.add(new GitGraphLine(1, 1));
        expectedLines.add(new GitGraphLine(2, 1));
        expectedLines.add(new GitGraphLine(2, 2));
        Assert.assertTrue(validateRowContents(row, expectedHash,
                expectedPosition, expectedLines));
        
        // * | 94eb2099d0832dd126c6e90e6d035e5f75ce240b
        row = rows.get(12);
        expectedHash = "94eb2099d0832dd126c6e90e6d035e5f75ce240b";
        expectedPosition = 1;
        expectedLines = new LinkedList<>();
        expectedLines.add(new GitGraphLine(1, 1));
        expectedLines.add(new GitGraphLine(2, 2));
        Assert.assertTrue(validateRowContents(row, expectedHash,
                expectedPosition, expectedLines));

        // | * 81f4594cd22c1ecaeedd302fb872df8b90429601
        row = rows.get(13);
        expectedHash = "81f4594cd22c1ecaeedd302fb872df8b90429601";
        expectedPosition = 2;
        expectedLines = new LinkedList<>();
        expectedLines.add(new GitGraphLine(1, 1));
        expectedLines.add(new GitGraphLine(2, 2));
        Assert.assertTrue(validateRowContents(row, expectedHash,
                expectedPosition, expectedLines));

        // * | 5d3792fa4e454e99a743127ce129e81f73cb523a
        // |/
        row = rows.get(14);
        expectedHash = "5d3792fa4e454e99a743127ce129e81f73cb523a";
        expectedPosition = 1;
        expectedLines = new LinkedList<>();
        expectedLines.add(new GitGraphLine(1, 1));
        expectedLines.add(new GitGraphLine(1, 2));
        Assert.assertTrue(validateRowContents(row, expectedHash,
                expectedPosition, expectedLines));

        // * 573c7ad54c2d40790964fe8781ebe535bf919fad
        row = rows.get(15);
        expectedHash = "573c7ad54c2d40790964fe8781ebe535bf919fad";
        expectedPosition = 1;
        expectedLines = new LinkedList<>(); // Last row shouldn't have any lines
        Assert.assertTrue(validateRowContents(row, expectedHash,
                expectedPosition, expectedLines));
    }
    
    private boolean validateRowContents(GitGraphRow row, String expectedHash,
            int expectedPosition, List<GitGraphLine> expectedLines) {
        if(!row.getCommitHash().equals(expectedHash)) {
            return false;
        }
        if(row.getCommitBranchPosition() != expectedPosition) {
            return false;
        }
        if(row.getIncomingLines().size() != expectedLines.size()) {
            return false;
        }
        
        // Make sure that each of the expected lines appear only once
        for(GitGraphLine actualLine : row.getIncomingLines()) {
            boolean wasMatchFound = false;

            Iterator<GitGraphLine> expectedLinesIter = expectedLines.iterator();
            while(expectedLinesIter.hasNext()) {
                GitGraphLine expectedLine = expectedLinesIter.next();
                if((actualLine.getFromBranch() == expectedLine.getFromBranch())
                    && (actualLine.getToBranch() == expectedLine.getToBranch()))
                {
                    wasMatchFound = true;
                    expectedLinesIter.remove();
                    break;
                }
            }
            
            if(!wasMatchFound) {
                return false;
            }
        }
        
        return true;
    }
}
