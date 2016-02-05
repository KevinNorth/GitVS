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
    
    @Test
    public void stormTest() {
        List<String> rawGraphData = new LinkedList<String>();
        rawGraphData.add("| | | * | | | | | | | | | | b5efe2c");
        rawGraphData.add("| | | * | | | | | | | | | | b60712f");
        rawGraphData.add("| | | | | | * | | | | | | | 3f55fee");
        rawGraphData.add("| | | | | | * | | | | | | | 97b2c9a");
        rawGraphData.add("| | | | | | * | | | | | | | 0d34abf");
        rawGraphData.add("| |_|_|_|_|/ / / / / / / /");
        rawGraphData.add("|/| | | | | | | | | | | |");
        rawGraphData.add("| | | * | | | | | | | | | a8894e6");
        rawGraphData.add("| | | * | | | | | | | | | 7e378c6");
        rawGraphData.add("| | | * | | | | | | | | | 82f10eb");
        rawGraphData.add("| | | * | | | | | | | | | 4d8cc41");
        rawGraphData.add("| | | * | | | | | | | | | 3940007");
        rawGraphData.add("| | | * | | | | | | | | | beeaee7");
        rawGraphData.add("| | | * | | | | | | | | | 31daf26");
        rawGraphData.add("| | | * | | | | | | | | | 5f97080");
        rawGraphData.add("| | | * | | | | | | | | | 0bb8e46");
        rawGraphData.add("| | | * | | | | | | | | | 915f135");
        rawGraphData.add("| | | * | | | | | | | | | 0a257f1");
        rawGraphData.add("| | | * | | | | | | | | | bd4f6dc");
        rawGraphData.add("| | | * | | | | | | | | | 4e2fe47");
        rawGraphData.add("| | | | | | * | | | | | |   c1a89d9");
        rawGraphData.add("| | | | | | |\\ \\ \\ \\ \\ \\ \\");
        rawGraphData.add("| | | * | | | | | | | | | | b6fa601");
        rawGraphData.add("| | | * | | | | | | | | | | 425280e");
        rawGraphData.add("| |_|/ / / / / / / / / / /");
        rawGraphData.add("|/| | | | | | | | | | | |");
        rawGraphData.add("* | | | | | | | | | | | | 6babbb0");
        rawGraphData.add("* | | | | | | | | | | | |   bdbec85");
        rawGraphData.add("|\\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\");
        rawGraphData.add("| |/ / / / / / / / / / / /");
        rawGraphData.add("|/| | | | | | | | | | | |");
        rawGraphData.add("* | | | | | | | | | | | |   ce2d49b");
        rawGraphData.add("|\\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\");
        rawGraphData.add("| * | | | | | | | | | | | | f6d84e9");
        rawGraphData.add("| * | | | | | | | | | | | | d2090d7");
        rawGraphData.add("|/ / / / / / / / / / / / /");
        rawGraphData.add("* | | | | | | | | | | | | 18f68f7");
        rawGraphData.add("* | | | | | | | | | | | | 2c762dd");
        rawGraphData.add("| |/ / / / / / / / / / /");
        rawGraphData.add("|/| | | | | | | | | | |");
        rawGraphData.add("* | | | | | | | | | | | 7041dd7");
        rawGraphData.add("* | | | | | | | | | | | 1059735");
        rawGraphData.add("* | | | | | | | | | | |   9b1e176");
        rawGraphData.add("|\\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\");
        rawGraphData.add("* | | | | | | | | | | | | 041fbe3");
        rawGraphData.add("* | | | | | | | | | | | |   cd00dde");
        rawGraphData.add("|\\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\");
        rawGraphData.add("| |_|_|/ / / / / / / / / /");
        rawGraphData.add("|/| | | | | | | | | | | |");
        rawGraphData.add("| | * | | | | | | | | | | c661e98");
        rawGraphData.add("| |/ / / / / / / / / / /");
        rawGraphData.add("|/| | | | | | | | | | |");
        rawGraphData.add("* | | | | | | | | | | | a55b050");
        rawGraphData.add("* | | | | | | | | | | |   e0f33e7");
        rawGraphData.add("|\\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\");
        rawGraphData.add("* \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\   224c846");
        rawGraphData.add("|\\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\");
        rawGraphData.add("| | | | * | | | | | | | | | 5105802");
        rawGraphData.add("* | | | | | | | | | | | | | dae377c");
        rawGraphData.add("* | | | | | | | | | | | | |   5d22270");
        rawGraphData.add("|\\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\");
        rawGraphData.add("| |_|_|_|_|_|_|_|_|_|_|/ / /");
        rawGraphData.add("|/| | | | | | | | | | | | |");
        rawGraphData.add("| | | | | * | | | | | | | | 6687bed");
        rawGraphData.add("| | | | | * | | | | | | | | 57240b4");
        rawGraphData.add("| | | | | * | | | | | | | | 7029aee");
        rawGraphData.add("| |_|_|_|/ / / / / / / / /");
        rawGraphData.add("|/| | | | | | | | | | | |");
        rawGraphData.add("* | | | | | | | | | | | | 0acc1ce");
        rawGraphData.add("* | | | | | | | | | | | |   b082e85");
        rawGraphData.add("|\\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\");
        rawGraphData.add("* \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\   cf4407f");
        rawGraphData.add("|\\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\");
        rawGraphData.add("* | | | | | | | | | | | | | | 2181433");
        rawGraphData.add("| * | | | | | | | | | | | | |   63026ee");
        rawGraphData.add("| |\\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\");
        rawGraphData.add("|/ / / / / / / / / / / / / / /");
        rawGraphData.add("* | | | | | | | | | | | | | | c7c367c");
        rawGraphData.add("* | | | | | | | | | | | | | |   0d8a99d");
        rawGraphData.add("|\\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\");
        rawGraphData.add("| * | | | | | | | | | | | | | | aa1e1ed");
        rawGraphData.add("* | | | | | | | | | | | | | | | 4c59de6");
        rawGraphData.add("* | | | | | | | | | | | | | | |   6d3bee9");
        rawGraphData.add("|\\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\");
        rawGraphData.add("* | | | | | | | | | | | | | | | | 01bab86");
        rawGraphData.add("| * | | | | | | | | | | | | | | | c1c5273");
        rawGraphData.add("* | | | | | | | | | | | | | | | |   352a284");
        rawGraphData.add("|\\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\");
        rawGraphData.add("| |/ / / / / / / / / / / / / / / /");
        rawGraphData.add("|/| | | | | | | | | | | | | | | |");
        rawGraphData.add("| | | | | | | * | | | | | | | | | cba0511");
        rawGraphData.add("| | | * | | | | | | | | | | | | | fc8c296");
        rawGraphData.add("| |_|/ / / / / / / / / / / / / /");
        rawGraphData.add("|/| | | | | | | | | | | | | | |");
        rawGraphData.add("* | | | | | | | | | | | | | | |   20a864d");
        rawGraphData.add("|\\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\");
        rawGraphData.add("* \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\   5a71ea0");
        rawGraphData.add("|\\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\");
        rawGraphData.add("| * | | | | | | | | | | | | | | | | e182624");
        rawGraphData.add("| * | | | | | | | | | | | | | | | |   05c7004");
        rawGraphData.add("| |\\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\");
        rawGraphData.add("|/ / / / / / / / / / / / / / / / / /");
        rawGraphData.add("| | | | | | | | * | | | | | | | | | f1fafdf");
        rawGraphData.add("| | | | | | | | * | | | | | | | | |   783a57a");
        rawGraphData.add("| | | | | | | | |\\ \\ \\ \\ \\ \\ \\ \\ \\ \\");
        rawGraphData.add("| |_|_|_|_|_|_|_|/ / / / / / / / / /");
        rawGraphData.add("|/| | | | | | | | | | | | | | | | |");
        rawGraphData.add("* | | | | | | | | | | | | | | | | | fc3b877");
        rawGraphData.add("| | | | | | | | * | | | | | | | | |   ad96bf9");
        rawGraphData.add("| | | | | | | | |\\ \\ \\ \\ \\ \\ \\ \\ \\ \\");
        rawGraphData.add("* | | | | | | | | | | | | | | | | | | e2d267f");
        rawGraphData.add("* | | | | | | | | | | | | | | | | | |   53a4604");
        rawGraphData.add("|\\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\");
        rawGraphData.add("| |_|_|_|_|_|_|_|_|/ / / / / / / / / /");
        rawGraphData.add("|/| | | | | | | | | | | | | | | | | |");
        rawGraphData.add("* | | | | | | | | | | | | | | | | | | f8a2d65");
        rawGraphData.add("* | | | | | | | | | | | | | | | | | |   6ebf247");
        rawGraphData.add("|\\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\");
        rawGraphData.add("* | | | | | | | | | | | | | | | | | | | 8eac4aa");
        rawGraphData.add("* | | | | | | | | | | | | | | | | | | |   c0c1462");
        rawGraphData.add("|\\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\");
        rawGraphData.add("* | | | | | | | | | | | | | | | | | | | | dc05a00");
        rawGraphData.add("* | | | | | | | | | | | | | | | | | | | |   609b11c");
        rawGraphData.add("|\\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\");
        rawGraphData.add("* \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\   fd75ca7");
        rawGraphData.add("|\\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\");
        rawGraphData.add("| | | | | | | | | | | | | * | | | | | | | | | 9482369");
        rawGraphData.add("| * | | | | | | | | | | | | | | | | | | | | | 74cd042");
        rawGraphData.add("| * | | | | | | | | | | | | | | | | | | | | |   8bcb2f2");
        rawGraphData.add("| |\\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\");
        rawGraphData.add("| | | | | | | | | | | | | | | * | | | | | | | | ca02690");
        rawGraphData.add("| | | | | | | | | | | | | | | * | | | | | | | | 117e818");
        rawGraphData.add("| | | | | | | | | | | | | | | * | | | | | | | | 56dc7b9");
        rawGraphData.add("| | | | | | * | | | | | | | | | | | | | | | | |   ba6ace8");
        rawGraphData.add("| | | | | | |\\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\");
        rawGraphData.add("| | * | | | | | | | | | | | | | | | | | | | | | | 9bc9350");
        rawGraphData.add("| | | | | | | | | * | | | | | | | | | | | | | | | 7a02703");
        rawGraphData.add("| | | | | | | | | | |_|_|_|_|_|_|/ / / / / / / /");
        rawGraphData.add("| | | | | | | | | |/| | | | | | | | | | | | | |");
        rawGraphData.add("| | | | | | | | | | | | | | | | * | | | | | | | 124f664");
        rawGraphData.add("| | | | | | | | | | | | | | | | * | | | | | | | d91c393");
        rawGraphData.add("| | | | | | | | | | | | | | | | * | | | | | | | a16eca7");
        rawGraphData.add("| | | | | | | | | | |_|_|_|_|_|/ / / / / / / /");
        rawGraphData.add("| | | | | | | | | |/| | | | | | | | | | | | |");
        rawGraphData.add("| | | | | | | | * | | | | | | | | | | | | | | 3c89f92");
        rawGraphData.add("| | | | | | | | | | * | | | | | | | | | | | | 35f1da7");
        rawGraphData.add("| | | | | | | | | |/ / / / / / / / / / / / /");
        rawGraphData.add("| | | | | | | | | | | * | | | | | | | | | | a5ca650");
        rawGraphData.add("| | | | | | | | | | | | |_|_|_|_|_|_|_|/ /");
        rawGraphData.add("| | | | | | | | | | | |/| | | | | | | | |");
        rawGraphData.add("| | | | | | | | | | | | | | * | | | | | | bc77f1f");
        rawGraphData.add("| | | | | | | | | | | | | | * | | | | | | d6d8551");
        rawGraphData.add("| |_|_|_|_|_|_|_|_|_|_|_|_|/ / / / / / /");
        rawGraphData.add("|/| | | | | | | | | | | | | | | | | | |");
        rawGraphData.add("* | | | | | | | | | | | | | | | | | | | e03b28c");
        rawGraphData.add("| | | | | | | | * | | | | | | | | | | | ef71646");
        rawGraphData.add("| |_|_|_|_|_|_|/ / / / / / / / / / / /");
        rawGraphData.add("|/| | | | | | | | | | | | | | | | | |");
        rawGraphData.add("| | | | | | | | | | | | * | | | | | | 5565c43");
        rawGraphData.add("| | | | | | | | | | | * | | | | | | | b039d33");
        rawGraphData.add("| | | | | | | | | | | |/ / / / / / /");
        rawGraphData.add("| | * | | | | | | | | | | | | | | | ccb8031");
        rawGraphData.add("| | * | | | | | | | | | | | | | | | dfc33ec");
        rawGraphData.add("| | | | | | | | | * | | | | | | | | 69b9cf5");
        rawGraphData.add("| | | | | | | | |/ / / / / / / / /");
        rawGraphData.add("| | * | | | | | | | | | | | | | | 07d9733");
        rawGraphData.add("| | * | | | | | | | | | | | | | | b0c3704");
        rawGraphData.add("| | * | | | | | | | | | | | | | | 4078d95");
        rawGraphData.add("| | * | | | | | | | | | | | | | | fe64642");
        rawGraphData.add("| | * | | | | | | | | | | | | | | 9cb8666");
        rawGraphData.add("| | * | | | | | | | | | | | | | | b03ce6b");
        rawGraphData.add("| | * | | | | | | | | | | | | | | 037cd00");
        rawGraphData.add("| |/ / / / / / / / / / / / / / /");
        rawGraphData.add("| * | | | | | | | | | | | | | | ccf3fd2");
        rawGraphData.add("| * | | | | | | | | | | | | | |   49353bc");
        rawGraphData.add("| |\\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\");
        rawGraphData.add("| | |_|_|_|_|/ / / / / / / / / /");
        rawGraphData.add("| |/| | | | | | | | | | | | | |");
        rawGraphData.add("| * | | | | | | | | | | | | | | 45792dd");
        rawGraphData.add("| * | | | | | | | | | | | | | |   8d4d72c");
        rawGraphData.add("| |\\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\");
        rawGraphData.add("| | |_|_|_|_|_|/ / / / / / / / /");
        rawGraphData.add("| |/| | | | | | | | | | | | | |");
        rawGraphData.add("| | * | | | | | | | | | | | | | e7a8c94");
        rawGraphData.add("| | * | | | | | | | | | | | | | 9bfb26c");
        rawGraphData.add("| | * | | | | | | | | | | | | | c2f1da0");
        rawGraphData.add("| | * | | | | | | | | | | | | | 61921bb");
        rawGraphData.add("| | * | | | | | | | | | | | | | aee4864");
        rawGraphData.add("| | | * | | | | | | | | | | | | c455560");
        rawGraphData.add("| | * | | | | | | | | | | | | | 711e36f");
        rawGraphData.add("| | * | | | | | | | | | | | | | 387232c");
        rawGraphData.add("| | * | | | | | | | | | | | | | ee5265d");
        rawGraphData.add("| | * | | | | | | | | | | | | | 9815986");
        rawGraphData.add("| | * | | | | | | | | | | | | | 8c1ad3f");
        rawGraphData.add("| | * | | | | | | | | | | | | | 37768ef");
        rawGraphData.add("| | * | | | | | | | | | | | | | b8f4056");
        rawGraphData.add("| | * | | | | | | | | | | | | | 099dc72");
        rawGraphData.add("| | * | | | | | | | | | | | | | 921db43");
        rawGraphData.add("| | * | | | | | | | | | | | | | d7b832a");
        rawGraphData.add("| | * | | | | | | | | | | | | | c93a0ff");
        rawGraphData.add("| | * | | | | | | | | | | | | | 0d98bbe");
        rawGraphData.add("| | | * | | | | | | | | | | | | 83eced3");
        rawGraphData.add("| | | | |_|_|_|_|_|_|_|/ / / /");
        rawGraphData.add("| | | |/| | | | | | | | | | |");
        rawGraphData.add("| | | | | * | | | | | | | | | 5cb4bf5");
        rawGraphData.add("| | * | | | | | | | | | | | |   4645c19");
        rawGraphData.add("| | |\\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\");
        rawGraphData.add("| | | | |_|/ / / / / / / / / /");
        rawGraphData.add("| | | |/| | | | | | | | | | |");
        rawGraphData.add("| | * | | | | | | | | | | | |   62d725a");
        rawGraphData.add("| | |\\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\");
        rawGraphData.add("| | | | | | | | * | | | | | | | bb266fb");
        rawGraphData.add("| | * | | | | | | | | | | | | | 80c60d8");
        rawGraphData.add("| | * | | | | | | | | | | | | | 25aae7b");
        rawGraphData.add("| | * | | | | | | | | | | | | | 3147955");
        rawGraphData.add("| | * | | | | | | | | | | | | | 2a782ce");
        rawGraphData.add("| | * | | | | | | | | | | | | | d59e936");
        rawGraphData.add("| | * | | | | | | | | | | | | | 33903de");
        rawGraphData.add("| | * | | | | | | | | | | | | | 34b1373");
        rawGraphData.add("| | * | | | | | | | | | | | | | a8ceb1c");
        rawGraphData.add("| | * | | | | | | | | | | | | |   7f9d00d");
        rawGraphData.add("| | |\\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\");
        rawGraphData.add("| | * | | | | | | | | | | | | | | 444ec05");
        rawGraphData.add("| | | | | | | | * | | | | | | | | 8cb3dc3");
        rawGraphData.add("| | | | | | | | * | | | | | | | | aff24fd");
        rawGraphData.add("| | |_|_|_|_|_|/ / / / / / / / /");
        rawGraphData.add("| |/| | | | | | | | | | | | | |");
        rawGraphData.add("| * | | | | | | | | | | | | | | a8d253a");
        rawGraphData.add("| * | | | | | | | | | | | | | |   ee867bd");
        rawGraphData.add("| |\\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\");
        rawGraphData.add("| * | | | | | | | | | | | | | | | 7fb80a1");
        rawGraphData.add("| * | | | | | | | | | | | | | | | cbf64fc");
        rawGraphData.add("| * | | | | | | | | | | | | | | |   0646286");
        rawGraphData.add("| |\\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\");
        rawGraphData.add("| | * | | | | | | | | | | | | | | | e58d254");
        rawGraphData.add("| | * | | | | | | | | | | | | | | | ca21dc7");
        rawGraphData.add("| | * | | | | | | | | | | | | | | | 2e56ede");
        rawGraphData.add("| | * | | | | | | | | | | | | | | | bac4a05");
        rawGraphData.add("| * | | | | | | | | | | | | | | | | ab5b2f9");
        rawGraphData.add("| * | | | | | | | | | | | | | | | |   5cbad19");
        rawGraphData.add("| |\\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\");
        rawGraphData.add("| | | | * | | | | | | | | | | | | | | 20374ce");
        rawGraphData.add("| | |_|/ / / / / / / / / / / / / / /");
        rawGraphData.add("| |/| | | | | | | | | | | | | | | |");
        rawGraphData.add("| | | * | | | | | | | | | | | | | | 71a5b1a");
        rawGraphData.add("| * | | | | | | | | | | | | | | | | ad0c4ef");
        rawGraphData.add("| * | | | | | | | | | | | | | | | |   fb31f95");
        rawGraphData.add("| |\\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\");
        rawGraphData.add("| | |_|_|_|_|_|_|_|_|_|/ / / / / / /");
        rawGraphData.add("| |/| | | | | | | | | | | | | | | |");
        rawGraphData.add("| | | | * | | | | | | | | | | | | | 515722d");
        rawGraphData.add("| | * | | | | | | | | | | | | | | | 0f8d119");
        rawGraphData.add("| | | | * | | | | | | | | | | | | | 89fff44");
        rawGraphData.add("| | * | | | | | | | | | | | | | | | fa64a65");
        rawGraphData.add("| | * | | | | | | | | | | | | | | | 1660da0");
        rawGraphData.add("| | * | | | | | | | | | | | | | | | 601efa5");
        rawGraphData.add("| | | |_|_|_|_|_|/ / / / / / / / /");
        rawGraphData.add("| | |/| | | | | | | | | | | | | |");
        rawGraphData.add("| | | | * | | | | | | | | | | | | ce7085b");
        rawGraphData.add("| | | | * | | | | | | | | | | | | 2d993c5");
        rawGraphData.add("| | | | * | | | | | | | | | | | | 6b12a0b");
        rawGraphData.add("| | | | * | | | | | | | | | | | | dda1c32");
        rawGraphData.add("| | | | * | | | | | | | | | | | | 400f215");
        rawGraphData.add("| | |_|/ / / / / / / / / / / / /");
        rawGraphData.add("| |/| | | | | | | | | | | | | |");
        rawGraphData.add("| * | | | | | | | | | | | | | | 1c07f1d");
        rawGraphData.add("| * | | | | | | | | | | | | | |   3612307");
        rawGraphData.add("| |\\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\");
        rawGraphData.add("| * | | | | | | | | | | | | | | | 6d7b6da");
        rawGraphData.add("| * | | | | | | | | | | | | | | |   45c4247");
        rawGraphData.add("| |\\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\");
        rawGraphData.add("| * | | | | | | | | | | | | | | | | fa8bf87");
        rawGraphData.add("| * | | | | | | | | | | | | | | | |   1308f89");
        rawGraphData.add("| |\\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\");
        rawGraphData.add("|/ / / / / / / / / / / / / / / / / /");
        rawGraphData.add("| * | | | | | | | | | | | | | | | | a305c14");
        rawGraphData.add("* | | | | | | | | | | | | | | | | | 1afa5a2");
        rawGraphData.add("* | | | | | | | | | | | | | | | | |   696ca47");
        rawGraphData.add("|\\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\");
        rawGraphData.add("| |_|_|_|/ / / / / / / / / / / / / /");
        rawGraphData.add("|/| | | | | | | | | | | | | | | | |");
        rawGraphData.add("| * | | | | | | | | | | | | | | | | 532a594");
        rawGraphData.add("| | * | | | | | | | | | | | | | | | 3857644");
        rawGraphData.add("| |/ / / / / / / / / / / / / / / /");
        rawGraphData.add("|/| | | | | | | | | | | | | | | |");
        rawGraphData.add("| * | | | | | | | | | | | | | | |   1496f27");
        rawGraphData.add("| |\\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\");
        rawGraphData.add("| |/ / / / / / / / / / / / / / / /");
        rawGraphData.add("|/| | | | | | | | | | | | | | | |");
        rawGraphData.add("| | * | | | | | | | | | | | | | | c1faba8");
        rawGraphData.add("| | * | | | | | | | | | | | | | | a5f0712");
        rawGraphData.add("| | | * | | | | | | | | | | | | | 8cf5932");
        rawGraphData.add("| |_|/ / / / / / / / / / / / / /");
        rawGraphData.add("|/| | | | | | | | | | | | | | |");
        rawGraphData.add("* | | | | | | | | | | | | | | | 5a79ba5");
        rawGraphData.add("* | | | | | | | | | | | | | | |   61ccb73");
        rawGraphData.add("|\\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\");
        rawGraphData.add("| |_|_|_|_|_|_|/ / / / / / / / /");
        rawGraphData.add("|/| | | | | | | | | | | | | | |");
        rawGraphData.add("| | * | | | | | | | | | | | | | 3dbfa10");
        rawGraphData.add("* | | | | | | | | | | | | | | | 77015f0");
        rawGraphData.add("* | | | | | | | | | | | | | | |   82c25ca");
        rawGraphData.add("|\\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\");
        rawGraphData.add("| * | | | | | | | | | | | | | | | 9e6a3d1");
        rawGraphData.add("* | | | | | | | | | | | | | | | | 1354c40");
        rawGraphData.add("* | | | | | | | | | | | | | | | |   cdb8a85");
        rawGraphData.add("|\\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\");
        rawGraphData.add("| | | | | * | | | | | | | | | | | | b64998f");
        rawGraphData.add("| | | | | * | | | | | | | | | | | |   d0575b9");
        rawGraphData.add("| | | | | |\\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\");
        rawGraphData.add("| | | * | | | | | | | | | | | | | | | 03b568f");
        rawGraphData.add("| | | * | | | | | | | | | | | | | | | 333df58");
        rawGraphData.add("| | | * | | | | | | | | | | | | | | | 120840d");
        rawGraphData.add("| | | * | | | | | | | | | | | | | | | a99016c");
        rawGraphData.add("| | * | | | | | | | | | | | | | | | | 8fd0116");
        rawGraphData.add("| | | * | | | | | | | | | | | | | | | e9f1e01");
        rawGraphData.add("| | | | |_|_|_|_|_|/ / / / / / / / /");
        rawGraphData.add("| | | |/| | | | | | | | | | | | | |");
        rawGraphData.add("| | | | | | | * | | | | | | | | | | 194aeca");
        rawGraphData.add("| | * | | | | | | | | | | | | | | |   124c62c");
        rawGraphData.add("| | |\\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\");
        rawGraphData.add("| | * | | | | | | | | | | | | | | | | 405b853");
        rawGraphData.add("| | * | | | | | | | | | | | | | | | | ea2c2d2");
        rawGraphData.add("| | | | | | | | * | | | | | | | | | | eb12941");
        rawGraphData.add("| | * | | | | | | | | | | | | | | | | e49e8c5");
        rawGraphData.add("| | | | | | | | * | | | | | | | | | | 0c2fce5");
        rawGraphData.add("| | | | | | | | * | | | | | | | | | | 77429d0");
        rawGraphData.add("| * | | | | | | | | | | | | | | | | | 1bdfd7e");
        rawGraphData.add("|/ / / / / / / / / / / / / / / / / /");
        rawGraphData.add("* | | | | | | | | | | | | | | | | | f4bd90a");
        rawGraphData.add("* | | | | | | | | | | | | | | | | |   fea9555");
        rawGraphData.add("|\\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\");
        rawGraphData.add("* | | | | | | | | | | | | | | | | | | 2057943");
        rawGraphData.add("* | | | | | | | | | | | | | | | | | |   f61f33d");
        rawGraphData.add("|\\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\");
        rawGraphData.add("| |_|_|_|_|_|_|/ / / / / / / / / / / /");
        rawGraphData.add("|/| | | | | | | | | | | | | | | | | |");
        rawGraphData.add("* | | | | | | | | | | | | | | | | | | 4eccb15");
        rawGraphData.add("* | | | | | | | | | | | | | | | | | |   69abc57");
        rawGraphData.add("|\\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\");
        rawGraphData.add("| | * | | | | | | | | | | | | | | | | | 3e40c9a");
        rawGraphData.add("| |/ / / / / / / / / / / / / / / / / /");
        rawGraphData.add("|/| | | | | | | | | | | | | | | | | |");
        rawGraphData.add("| * | | | | | | | | | | | | | | | | | c29c2b6");
        rawGraphData.add("| * | | | | | | | | | | | | | | | | | 7365078");
        rawGraphData.add("| | |_|_|/ / / / / / / / / / / / / /");
        rawGraphData.add("| |/| | | | | | | | | | | | | | | |");
        rawGraphData.add("| | * | | | | | | | | | | | | | | | 400dcfa");
        rawGraphData.add("| |/ / / / / / / / / / / / / / / /");
        rawGraphData.add("|/| | | | | | | | | | | | | | | |");
        rawGraphData.add("* | | | | | | | | | | | | | | | | 34cda0d");
        rawGraphData.add("* | | | | | | | | | | | | | | | |   46a4ede");
        rawGraphData.add("|\\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\");
        rawGraphData.add("* | | | | | | | | | | | | | | | | | 74b0a4f");
        rawGraphData.add("* | | | | | | | | | | | | | | | | | 2c93498");
        rawGraphData.add("* | | | | | | | | | | | | | | | | |   703ba7b");
        rawGraphData.add("|\\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\");
        rawGraphData.add("| |_|/ / / / / / / / / / / / / / / /");
        rawGraphData.add("|/| | | | | | | | | | | | | | | | |");
        rawGraphData.add("* | | | | | | | | | | | | | | | | | 2b6884b");
        rawGraphData.add("| |_|_|_|_|_|_|_|_|_|/ / / / / / /");
        rawGraphData.add("|/| | | | | | | | | | | | | | | |");
        rawGraphData.add("* | | | | | | | | | | | | | | | |   57a3b89");
        rawGraphData.add("|\\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\");
        rawGraphData.add("* | | | | | | | | | | | | | | | | | 2065dca");
        rawGraphData.add("* | | | | | | | | | | | | | | | | |   93815f3");
        rawGraphData.add("|\\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\");
        rawGraphData.add("* | | | | | | | | | | | | | | | | | | e25f28f");
        rawGraphData.add("* | | | | | | | | | | | | | | | | | |   1acadff");
        rawGraphData.add("|\\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\");
        rawGraphData.add("* | | | | | | | | | | | | | | | | | | | 5a4e1f8");
        rawGraphData.add("* | | | | | | | | | | | | | | | | | | |   a2ca314");
        rawGraphData.add("|\\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\");
        rawGraphData.add("| * | | | | | | | | | | | | | | | | | | | 2923a1c");
        rawGraphData.add("| * | | | | | | | | | | | | | | | | | | | 9916405");
        rawGraphData.add("| * | | | | | | | | | | | | | | | | | | | 6764a14");
        rawGraphData.add("| | | | * | | | | | | | | | | | | | | | | 5c818b0");
        rawGraphData.add("| |_|_|/ / / / / / / / / / / / / / / / /");
        rawGraphData.add("|/| | | | | | | | | | | | | | | | | | |");
        rawGraphData.add("| | | * | | | | | | | | | | | | | | | | 7cfa6e4");
        rawGraphData.add("| | | * | | | | | | | | | | | | | | | | c804139");
        rawGraphData.add("| * | | | | | | | | | | | | | | | | | | 2ed4478");
        rawGraphData.add("| * | | | | | | | | | | | | | | | | | | a3726a9");
        rawGraphData.add("| | | | * | | | | | | | | | | | | | | | 7fe5ac9");
        rawGraphData.add("| | | | * | | | | | | | | | | | | | | |   b3521c5");
        rawGraphData.add("| | | | |\\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\");
        rawGraphData.add("| | | | * | | | | | | | | | | | | | | | | be9799e");
        rawGraphData.add("| | * | | | | | | | | | | | | | | | | | | f0e4c6f");
        rawGraphData.add("| |/ / / / / / / / / / / / / / / / / / /");
        rawGraphData.add("|/| | | | | | | | | | | | | | | | | | |");
        rawGraphData.add("| * | | | | | | | | | | | | | | | | | | 18abd03");
        rawGraphData.add("| | | * | | | | | | | | | | | | | | | | b1fca5e");
        rawGraphData.add("| * | | | | | | | | | | | | | | | | | | 9e907dc");
        rawGraphData.add("| | | * | | | | | | | | | | | | | | | | f6268a0");
        rawGraphData.add("| | | * | | | | | | | | | | | | | | | | c90c574");
        rawGraphData.add("| | | * | | | | | | | | | | | | | | | | 5bd5cf9");
        rawGraphData.add("| * | | | | | | | | | | | | | | | | | | 22cf0ac");
        rawGraphData.add("|/ / / / / / / / / / / / / / / / / / /");
        rawGraphData.add("* | | | | | | | | | | | | | | | | | | c12e28c");
        rawGraphData.add("* | | | | | | | | | | | | | | | | | |   90ef005");
        rawGraphData.add("|\\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\");
        rawGraphData.add("| |_|_|_|_|_|/ / / / / / / / / / / / /");
        rawGraphData.add("|/| | | | | | | | | | | | | | | | | |");
        rawGraphData.add("* | | | | | | | | | | | | | | | | | | 14c7def");
        rawGraphData.add("* | | | | | | | | | | | | | | | | | |   6a0d13b");
        rawGraphData.add("|\\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\");
        rawGraphData.add("* | | | | | | | | | | | | | | | | | | | 2ef095e");
        rawGraphData.add("* | | | | | | | | | | | | | | | | | | |   2ed181c");
        rawGraphData.add("|\\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\");
        rawGraphData.add("* | | | | | | | | | | | | | | | | | | | | ed572d2");
        rawGraphData.add("* | | | | | | | | | | | | | | | | | | | |   f1ec3fe");
        rawGraphData.add("|\\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\");
        rawGraphData.add("* | | | | | | | | | | | | | | | | | | | | | fe65176");
        rawGraphData.add("* | | | | | | | | | | | | | | | | | | | | | 8fd08b8");
        rawGraphData.add("* | | | | | | | | | | | | | | | | | | | | |   2d8cd8d");
        rawGraphData.add("|\\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\");
        rawGraphData.add("* | | | | | | | | | | | | | | | | | | | | | | 4fe62b2");
        rawGraphData.add("* | | | | | | | | | | | | | | | | | | | | | |   e8bf07e");
        rawGraphData.add("|\\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\");
        rawGraphData.add("* | | | | | | | | | | | | | | | | | | | | | | | b57da7b");
        rawGraphData.add("* | | | | | | | | | | | | | | | | | | | | | | |   fa9c7a5");
        rawGraphData.add("|\\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\");
        rawGraphData.add("* | | | | | | | | | | | | | | | | | | | | | | | | 7f14dfd");
        rawGraphData.add("* | | | | | | | | | | | | | | | | | | | | | | | |   68d72b3");
        rawGraphData.add("|\\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\");
        rawGraphData.add("* \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\   795edec");
        rawGraphData.add("|\\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\");
        rawGraphData.add("* | | | | | | | | | | | | | | | | | | | | | | | | | | 1a94d9e");
        rawGraphData.add("* | | | | | | | | | | | | | | | | | | | | | | | | | |   e9fccb1");
        rawGraphData.add("|\\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\");
        rawGraphData.add("| | * | | | | | | | | | | | | | | | | | | | | | | | | | 8805443");
        rawGraphData.add("* | | | | | | | | | | | | | | | | | | | | | | | | | | | b24b0fe");
        rawGraphData.add("* | | | | | | | | | | | | | | | | | | | | | | | | | | |   6e76d4b");
        rawGraphData.add("|\\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\");
        rawGraphData.add("| | |/ / / / / / / / / / / / / / / / / / / / / / / / / /");
        rawGraphData.add("| |/| | | | | | | | | / / / / / / / / / / / / / / / / /");
        rawGraphData.add("| |_|_|_|_|_|_|_|_|_|/ / / / / / / / / / / / / / / / /");
        rawGraphData.add("|/| | | | | | | | | | | | | | | | | | | | | | | | | |");
        rawGraphData.add("| | | * | | | | | | | | | | | | | | | | | | | | | | | 024ff26");
        rawGraphData.add("| * | | | | | | | | | | | | | | | | | | | | | | | | | 0892f09");
        rawGraphData.add("| | | | | | | | | * | | | | | | | | | | | | | | | | | 34c7eb2");
        rawGraphData.add("| | |_|_|_|_|_|_|/ / / / / / / / / / / / / / / / / /");
        rawGraphData.add("| |/| | | | | | | | | | | | | | | | | | | | | | | |");
        rawGraphData.add("| | * | | | | | | | | | | | | | | | | | | | | | | | 23f75d9");
        rawGraphData.add("| | | | | | | | | * | | | | | | | | | | | | | | | | 58f0994");
        rawGraphData.add("| | | | | | | | | * | | | | | | | | | | | | | | | | faa33f9");
        rawGraphData.add("| | * | | | | | | | | | | | | | | | | | | | | | | | 3586bc3");
        rawGraphData.add("| | * | | | | | | | | | | | | | | | | | | | | | | | 8501184");
        rawGraphData.add("| | * | | | | | | | | | | | | | | | | | | | | | | | b98acd3");
        rawGraphData.add("| | | * | | | | | | | | | | | | | | | | | | | | | | 664b0ce");
        rawGraphData.add("| | | | * | | | | | | | | | | | | | | | | | | | | | ac25d6d");
        rawGraphData.add("| | | | | * | | | | | | | | | | | | | | | | | | | | 507f295");
        rawGraphData.add("| | | | |/ / / / / / / / / / / / / / / / / / / / /");
        rawGraphData.add("| | | | | | * | | | | | | | | | | | | | | | | | | fe34841");
        rawGraphData.add("| | | | | * | | | | | | | | | | | | | | | | | | | b8b2e16");
        rawGraphData.add("| |_|_|_|/ / / / / / / / / / / / / / / / / / / /");
        rawGraphData.add("|/| | | | | | | | | | | | | | | | | | | | | | |");
        rawGraphData.add("| | | | | * | | | | | | | | | | | | | | | | | | 76d73e9");
        rawGraphData.add("| |_|_|_|/ / / / / / / / / / / / / / / / / / /");
        rawGraphData.add("|/| | | | | | | | | | | | | | | | | | | | | |");
        rawGraphData.add("* | | | | | | | | | | | | | | | | | | | | | | d59eaac");
        rawGraphData.add("|/ / / / / / / / / / / / / / / / / / / / / /");
        rawGraphData.add("* | | | | | | | | | | | | | | | | | | | | | 90edd94");
        rawGraphData.add("* | | | | | | | | | | | | | | | | | | | | |   30b3598");
        rawGraphData.add("|\\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\");
        rawGraphData.add("* | | | | | | | | | | | | | | | | | | | | | | 8545ff9");
        rawGraphData.add("* | | | | | | | | | | | | | | | | | | | | | |   2575b49");
        rawGraphData.add("|\\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\");
        rawGraphData.add("* | | | | | | | | | | | | | | | | | | | | | | | 2bad9c6");
        rawGraphData.add("* | | | | | | | | | | | | | | | | | | | | | | |   a213cce");
        rawGraphData.add("|\\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\");
        rawGraphData.add("* | | | | | | | | | | | | | | | | | | | | | | | | 9e2b02d");
        rawGraphData.add("* | | | | | | | | | | | | | | | | | | | | | | | |   7a8e6ca");
        rawGraphData.add("|\\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\");
        rawGraphData.add("| |_|_|_|_|_|_|_|_|_|/ / / / / / / / / / / / / / /");
        rawGraphData.add("|/| | | | | | | | | | | | | | | | | | | | | | | |");
        rawGraphData.add("* | | | | | | | | | | | | | | | | | | | | | | | | 316b23e");
        rawGraphData.add("* | | | | | | | | | | | | | | | | | | | | | | | |   e9c2495");
        rawGraphData.add("|\\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\");
        rawGraphData.add("| | | * | | | | | | | | | | | | | | | | | | | | | | 77aa363");
        rawGraphData.add("| | | | |_|/ / / / / / / / / / / / / / / / / / / /");
        rawGraphData.add("| | | |/| | | | | | | | | | | | | | | | | | | | |");
        rawGraphData.add("* | | | | | | | | | | | | | | | | | | | | | | | |   4a57e50");
        rawGraphData.add("|\\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\");
        rawGraphData.add("| |_|_|/ / / / / / / / / / / / / / / / / / / / / /");
        rawGraphData.add("|/| | | | | | | | | | | | | | | | | | | | | | | |");
        rawGraphData.add("| | | | * | | | | | | | | | | | | | | | | | | | | 686e906");
        rawGraphData.add("* | | | | | | | | | | | | | | | | | | | | | | | | f3ed08b");
        rawGraphData.add("* | | | | | | | | | | | | | | | | | | | | | | | |   6e0fc9e");
        rawGraphData.add("|\\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\");
        rawGraphData.add("| |_|_|_|_|_|_|_|_|_|_|_|_|_|_|/ / / / / / / / / /");
        rawGraphData.add("|/| | | | | | | | | | | | | | | | | | | | | | | |");
        rawGraphData.add("| | | | * | | | | | | | | | | | | | | | | | | | | 2b859fb");
        rawGraphData.add("| |_|_|/ / / / / / / / / / / / / / / / / / / / /");
        rawGraphData.add("|/| | | | | | | | | | | | | | | | | | | | | | |");
        rawGraphData.add("| * | | | | | | | | | | | | | | | | | | | | | | a4876b2");
        rawGraphData.add("| | | | * | | | | | | | | | | | | | | | | | | | 8d6ab39");
        rawGraphData.add("| |_|_|/ / / / / / / / / / / / / / / / / / / /");
        rawGraphData.add("|/| | | | | | | | | | | | | | | | | | | | | |");
        rawGraphData.add("| | | | * | | | | | | | | | | | | | | | | | | f35ce60");
        rawGraphData.add("| * | | | | | | | | | | | | | | | | | | | | | 8d91ad9");
        rawGraphData.add("| | | | * | | | | | | | | | | | | | | | | | | 780a472");
        rawGraphData.add("| | | | | |_|_|_|/ / / / / / / / / / / / / /");
        rawGraphData.add("| | | | |/| | | | | | | | | | | | | | | | |");
        rawGraphData.add("| * | | | | | | | | | | | | | | | | | | | | 25f31a0");
        rawGraphData.add("| | | * | | | | | | | | | | | | | | | | | | 5c6368c");
        rawGraphData.add("* | | | | | | | | | | | | | | | | | | | | | f3568d7");
        rawGraphData.add("| * | | | | | | | | | | | | | | | | | | | | f697044");
        rawGraphData.add("| * | | | | | | | | | | | | | | | | | | | | d2f9305");
        rawGraphData.add("* | | | | | | | | | | | | | | | | | | | | |   ae5f785");
        rawGraphData.add("|\\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\");
        rawGraphData.add("| | * | | | | | | | | | | | | | | | | | | | | 0c2021e");
        rawGraphData.add("| | | |_|/ / / / / / / / / / / / / / / / / /");
        rawGraphData.add("| | |/| | | | | | | | | | | | | | | | | | |");
        rawGraphData.add("| * | | | | | | | | | | | | | | | | | | | | 5a509c4");
        rawGraphData.add("| | | * | | | | | | | | | | | | | | | | | | 698fff6");
        rawGraphData.add("| | | * | | | | | | | | | | | | | | | | | | acca572");
        rawGraphData.add("| | | | | | | | * | | | | | | | | | | | | | 2e1bedc");
        rawGraphData.add("| | | | | | | | * | | | | | | | | | | | | | 88f71b8");
        rawGraphData.add("| | | | | | | | | | | | | | | | * | | | | | 641300e");
        rawGraphData.add("| | | | * | | | | | | | | | | | | | | | | | 0f0d238");
        rawGraphData.add("| | | | * | | | | | | | | | | | | | | | | | 17df052");
        rawGraphData.add("| | | | | | | * | | | | | | | | | | | | | | cc53fe7");
        rawGraphData.add("| | | | | |_|/ / / / / / / / / / / / / / /");
        rawGraphData.add("| | | | |/| | | | | | | | | | | | | | | |");
        rawGraphData.add("| | | | | | | | * | | | | | | | | | | | | 62eeae4");
        rawGraphData.add("| | | | | | | * | | | | | | | | | | | | | 5c38b91");
        rawGraphData.add("| | | | | | | * | | | | | | | | | | | | | df417e0");
        rawGraphData.add("| | | | | | | | | | | | | | * | | | | | | a019d50");
        rawGraphData.add("| | | | | | | | | | | | | | | |/ / / / /");
        rawGraphData.add("| | | | | | | | | | | | | | |/| | | | |");
        rawGraphData.add("| * | | | | | | | | | | | | | | | | | |   f3d60bd");
        rawGraphData.add("| |\\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\ \\");
        rawGraphData.add("| * | | | | | | | | | | | | | | | | | | | ff1962a");
        rawGraphData.add("| | | | | | | | | | | * | | | | | | | | | f8118e0");
        rawGraphData.add("| | | | | | | | | | | | * | | | | | | | | 07722ff");
        rawGraphData.add("| | | | | | | | | | | | * | | | | | | | | 031b77c");
        rawGraphData.add("| | | | | | | | | | | | | | | | | | * | |   26b2f32");
        rawGraphData.add("| | | | | | | | | | | | | | | | | | |\\ \\ \\");
        rawGraphData.add("| | | | | | | | | | | | | | | | | | * | | | 506c242");
        rawGraphData.add("| | | | | | | | | | | | | | | | | | * | | | 7e36ce5");
        rawGraphData.add("| | | | | | * | | | | | | | | | | | | | | | af64a3a");
        rawGraphData.add("| |_|_|_|_|/ / / / / / / / / / / / / / / /");
        rawGraphData.add("|/| | | | | | | | | | | | | | | | | | | |");
        rawGraphData.add("* | | | | | | | | | | | | | | | | | | | | 7edf520");
        rawGraphData.add("* | | | | | | | | | | | | | | | | | | | | 6e29d27");
        rawGraphData.add("* | | | | | | | | | | | | | | | | | | | | 60d9f81");
        rawGraphData.add("* | | | | | | | | | | | | | | | | | | | | 4c09732");
        rawGraphData.add("* | | | | | | | | | | | | | | | | | | | |   fe88200");

        
        GitGraph gitGraph =
                new GitGraphProducer().produceGitGraph(rawGraphData);
        
        List<GitGraphRow> rows = gitGraph.getRows();
                
        Assert.assertEquals(347, rows.size());
        
        
        // | | | * | | | | | | | | | | b5efe2c
        GitGraphRow row = rows.get(0);
        String expectedHash = "b5efe2c";
        int expectedPosition = 4;
        LinkedList<GitGraphLine> expectedLines = new LinkedList<>();
        expectedLines.add(new GitGraphLine(1, 1));
        expectedLines.add(new GitGraphLine(2, 2));
        expectedLines.add(new GitGraphLine(3, 3));
        expectedLines.add(new GitGraphLine(4, 4));
        expectedLines.add(new GitGraphLine(5, 5));
        expectedLines.add(new GitGraphLine(6, 6));
        expectedLines.add(new GitGraphLine(7, 7));
        expectedLines.add(new GitGraphLine(8, 8));
        expectedLines.add(new GitGraphLine(9, 9));
        expectedLines.add(new GitGraphLine(10, 10));
        expectedLines.add(new GitGraphLine(11, 11));
        expectedLines.add(new GitGraphLine(12, 12));
        expectedLines.add(new GitGraphLine(13, 13));
        expectedLines.add(new GitGraphLine(14, 14));
        Assert.assertTrue(printAndValidateRowContents(row, expectedHash,
                expectedPosition, expectedLines));

        // | | | * | | | | | | | | | | b60712f
        row = rows.get(1);
        expectedHash = "b60712f";
        expectedPosition = 4;
        expectedLines = new LinkedList<>();
        expectedLines.add(new GitGraphLine(1, 1));
        expectedLines.add(new GitGraphLine(2, 2));
        expectedLines.add(new GitGraphLine(3, 3));
        expectedLines.add(new GitGraphLine(4, 4));
        expectedLines.add(new GitGraphLine(5, 5));
        expectedLines.add(new GitGraphLine(6, 6));
        expectedLines.add(new GitGraphLine(7, 7));
        expectedLines.add(new GitGraphLine(8, 8));
        expectedLines.add(new GitGraphLine(9, 9));
        expectedLines.add(new GitGraphLine(10, 10));
        expectedLines.add(new GitGraphLine(11, 11));
        expectedLines.add(new GitGraphLine(12, 12));
        expectedLines.add(new GitGraphLine(13, 13));
        expectedLines.add(new GitGraphLine(14, 14));
        Assert.assertTrue(printAndValidateRowContents(row, expectedHash,
                expectedPosition, expectedLines));
        
        // | | | | | | * | | | | | | | 3f55fee
        row = rows.get(2);
        expectedHash = "3f55fee";
        expectedPosition = 7;
        expectedLines = new LinkedList<>();
        expectedLines.add(new GitGraphLine(1, 1));
        expectedLines.add(new GitGraphLine(2, 2));
        expectedLines.add(new GitGraphLine(3, 3));
        expectedLines.add(new GitGraphLine(4, 4));
        expectedLines.add(new GitGraphLine(5, 5));
        expectedLines.add(new GitGraphLine(6, 6));
        expectedLines.add(new GitGraphLine(7, 7));
        expectedLines.add(new GitGraphLine(8, 8));
        expectedLines.add(new GitGraphLine(9, 9));
        expectedLines.add(new GitGraphLine(10, 10));
        expectedLines.add(new GitGraphLine(11, 11));
        expectedLines.add(new GitGraphLine(12, 12));
        expectedLines.add(new GitGraphLine(13, 13));
        expectedLines.add(new GitGraphLine(14, 14));
        Assert.assertTrue(printAndValidateRowContents(row, expectedHash,
                expectedPosition, expectedLines));
        
        // | | | | | | * | | | | | | | 97b2c9a
        row = rows.get(3);
        expectedHash = "97b2c9a";
        expectedPosition = 7;
        expectedLines = new LinkedList<>();
        expectedLines.add(new GitGraphLine(1, 1));
        expectedLines.add(new GitGraphLine(2, 2));
        expectedLines.add(new GitGraphLine(3, 3));
        expectedLines.add(new GitGraphLine(4, 4));
        expectedLines.add(new GitGraphLine(5, 5));
        expectedLines.add(new GitGraphLine(6, 6));
        expectedLines.add(new GitGraphLine(7, 7));
        expectedLines.add(new GitGraphLine(8, 8));
        expectedLines.add(new GitGraphLine(9, 9));
        expectedLines.add(new GitGraphLine(10, 10));
        expectedLines.add(new GitGraphLine(11, 11));
        expectedLines.add(new GitGraphLine(12, 12));
        expectedLines.add(new GitGraphLine(13, 13));
        expectedLines.add(new GitGraphLine(14, 14));
        Assert.assertTrue(printAndValidateRowContents(row, expectedHash,
                expectedPosition, expectedLines));
 

        // | | | | | | * | | | | | | | 0d34abf
        // | |_|_|_|_|/ / / / / / / /
        // |/| | | | | | | | | | | |
        row = rows.get(4);
        expectedHash = "0d34abf";
        expectedPosition = 7;
        expectedLines = new LinkedList<>();
        expectedLines.add(new GitGraphLine(1, 1));
        expectedLines.add(new GitGraphLine(1, 7));
        expectedLines.add(new GitGraphLine(2, 2));
        expectedLines.add(new GitGraphLine(3, 3));
        expectedLines.add(new GitGraphLine(4, 4));
        expectedLines.add(new GitGraphLine(5, 5));
        expectedLines.add(new GitGraphLine(6, 6));
        expectedLines.add(new GitGraphLine(7, 8));
        expectedLines.add(new GitGraphLine(8, 9));
        expectedLines.add(new GitGraphLine(9, 10));
        expectedLines.add(new GitGraphLine(10, 11));
        expectedLines.add(new GitGraphLine(11, 12));
        expectedLines.add(new GitGraphLine(12, 13));
        expectedLines.add(new GitGraphLine(13, 14));
        Assert.assertTrue(printAndValidateRowContents(row, expectedHash,
                expectedPosition, expectedLines));
        
        // | | | * | | | | | | | | | a8894e6
        row = rows.get(5);
        expectedHash = "a8894e6";
        expectedPosition = 4;
        expectedLines = new LinkedList<>();
        expectedLines.add(new GitGraphLine(1, 1));
        expectedLines.add(new GitGraphLine(2, 2));
        expectedLines.add(new GitGraphLine(3, 3));
        expectedLines.add(new GitGraphLine(4, 4));
        expectedLines.add(new GitGraphLine(5, 5));
        expectedLines.add(new GitGraphLine(6, 6));
        expectedLines.add(new GitGraphLine(7, 7));
        expectedLines.add(new GitGraphLine(8, 8));
        expectedLines.add(new GitGraphLine(9, 9));
        expectedLines.add(new GitGraphLine(10, 10));
        expectedLines.add(new GitGraphLine(11, 11));
        expectedLines.add(new GitGraphLine(12, 12));
        expectedLines.add(new GitGraphLine(13, 13));
        Assert.assertTrue(printAndValidateRowContents(row, expectedHash,
                expectedPosition, expectedLines));

        // | | | * | | | | | | | | | 7e378c6    6
        // | | | * | | | | | | | | | 82f10eb    7
        // | | | * | | | | | | | | | 4d8cc41    8
        // | | | * | | | | | | | | | 3940007    9
        // | | | * | | | | | | | | | beeaee7    10
        // | | | * | | | | | | | | | 31daf26    11
        // | | | * | | | | | | | | | 5f97080    12
        // | | | * | | | | | | | | | 0bb8e46    13
        // | | | * | | | | | | | | | 915f135    14
        // | | | * | | | | | | | | | 0a257f1    15
        // | | | * | | | | | | | | | bd4f6dc    16
        // | | | * | | | | | | | | | 4e2fe47    17
        // | | | | | | * | | | | | |   c1a89d9  18
        // | | | | | | |\ \ \ \ \ \ \
        row = rows.get(18);
        expectedHash = "c1a89d9";
        expectedPosition = 7;
        expectedLines = new LinkedList<>();
        expectedLines.add(new GitGraphLine(1, 1));
        expectedLines.add(new GitGraphLine(2, 2));
        expectedLines.add(new GitGraphLine(3, 3));
        expectedLines.add(new GitGraphLine(4, 4));
        expectedLines.add(new GitGraphLine(5, 5));
        expectedLines.add(new GitGraphLine(6, 6));
        expectedLines.add(new GitGraphLine(7, 7));
        expectedLines.add(new GitGraphLine(8, 7));
        expectedLines.add(new GitGraphLine(9, 8));
        expectedLines.add(new GitGraphLine(10, 9));
        expectedLines.add(new GitGraphLine(11, 10));
        expectedLines.add(new GitGraphLine(12, 11));
        expectedLines.add(new GitGraphLine(13, 12));
        expectedLines.add(new GitGraphLine(14, 13));
        Assert.assertTrue(printAndValidateRowContents(row, expectedHash,
                expectedPosition, expectedLines));
        
        // | | | * | | | | | | | | | | b6fa601  19
        // | | | * | | | | | | | | | | 425280e  20
        // | |_|/ / / / / / / / / / /
        // |/| | | | | | | | | | | |
        row = rows.get(20);
        expectedHash = "425280e";
        expectedPosition = 4;
        expectedLines = new LinkedList<>();
        expectedLines.add(new GitGraphLine(1, 1));
        expectedLines.add(new GitGraphLine(1, 4));
        expectedLines.add(new GitGraphLine(2, 2));
        expectedLines.add(new GitGraphLine(3, 3));
        expectedLines.add(new GitGraphLine(4, 5));
        expectedLines.add(new GitGraphLine(5, 6));
        expectedLines.add(new GitGraphLine(6, 7));
        expectedLines.add(new GitGraphLine(7, 8));
        expectedLines.add(new GitGraphLine(8, 9));
        expectedLines.add(new GitGraphLine(9, 10));
        expectedLines.add(new GitGraphLine(10, 11));
        expectedLines.add(new GitGraphLine(11, 12));
        expectedLines.add(new GitGraphLine(12, 13));
        expectedLines.add(new GitGraphLine(13, 14));
        Assert.assertTrue(printAndValidateRowContents(row, expectedHash,
                expectedPosition, expectedLines));        
        
        // * | | | | | | | | | | | | 6babbb0    21
        // * | | | | | | | | | | | |   bdbec85  22
        // |\ \ \ \ \ \ \ \ \ \ \ \ \
        // | |/ / / / / / / / / / / /
        // |/| | | | | | | | | | | |
        row = rows.get(22);
        expectedHash = "bdbec85";
        expectedPosition = 1;
        expectedLines = new LinkedList<>();
        expectedLines.add(new GitGraphLine(1, 1));
        expectedLines.add(new GitGraphLine(1, 2));
        expectedLines.add(new GitGraphLine(2, 1));
        expectedLines.add(new GitGraphLine(3, 3));
        expectedLines.add(new GitGraphLine(4, 4));
        expectedLines.add(new GitGraphLine(5, 5));
        expectedLines.add(new GitGraphLine(6, 6));
        expectedLines.add(new GitGraphLine(7, 7));
        expectedLines.add(new GitGraphLine(8, 8));
        expectedLines.add(new GitGraphLine(9, 9));
        expectedLines.add(new GitGraphLine(10, 10));
        expectedLines.add(new GitGraphLine(11, 11));
        expectedLines.add(new GitGraphLine(12, 12));
        expectedLines.add(new GitGraphLine(13, 13));
        Assert.assertTrue(printAndValidateRowContents(row, expectedHash,
                expectedPosition, expectedLines));        

        
        // * | | | | | | | | | | | |   ce2d49b
        // |\ \ \ \ \ \ \ \ \ \ \ \ \
        // | * | | | | | | | | | | | | f6d84e9
        // | * | | | | | | | | | | | | d2090d7
        // |/ / / / / / / / / / / / /
        // * | | | | | | | | | | | | 18f68f7
        // * | | | | | | | | | | | | 2c762dd
        // | |/ / / / / / / / / / /
        // |/| | | | | | | | | | |
        // * | | | | | | | | | | | 7041dd7
        // * | | | | | | | | | | | 1059735
        // * | | | | | | | | | | |   9b1e176
        // |\ \ \ \ \ \ \ \ \ \ \ \
        // * | | | | | | | | | | | | 041fbe3
        // * | | | | | | | | | | | |   cd00dde
        // |\ \ \ \ \ \ \ \ \ \ \ \ \
        // | |_|_|/ / / / / / / / / /
        // |/| | | | | | | | | | | |
        // | | * | | | | | | | | | | c661e98
        // | |/ / / / / / / / / / /
        // |/| | | | | | | | | | |
        // * | | | | | | | | | | | a55b050
        // * | | | | | | | | | | |   e0f33e7
        // |\ \ \ \ \ \ \ \ \ \ \ \
        // * \ \ \ \ \ \ \ \ \ \ \ \   224c846
        // |\ \ \ \ \ \ \ \ \ \ \ \ \
        // | | | | * | | | | | | | | | 5105802
        // * | | | | | | | | | | | | | dae377c
        // * | | | | | | | | | | | | |   5d22270
        // |\ \ \ \ \ \ \ \ \ \ \ \ \ \
        // | |_|_|_|_|_|_|_|_|_|_|/ / /
        // |/| | | | | | | | | | | | |
        // | | | | | * | | | | | | | | 6687bed
        // | | | | | * | | | | | | | | 57240b4
        // | | | | | * | | | | | | | | 7029aee
        // | |_|_|_|/ / / / / / / / /
        // |/| | | | | | | | | | | |
        // * | | | | | | | | | | | | 0acc1ce
        // * | | | | | | | | | | | |   b082e85
        // |\ \ \ \ \ \ \ \ \ \ \ \ \
        // * \ \ \ \ \ \ \ \ \ \ \ \ \   cf4407f
        // |\ \ \ \ \ \ \ \ \ \ \ \ \ \
        // * | | | | | | | | | | | | | | 2181433
        // | * | | | | | | | | | | | | |   63026ee
        // | |\ \ \ \ \ \ \ \ \ \ \ \ \ \
        // |/ / / / / / / / / / / / / / /
        // * | | | | | | | | | | | | | | c7c367c
        // * | | | | | | | | | | | | | |   0d8a99d
        // |\ \ \ \ \ \ \ \ \ \ \ \ \ \ \
        // | * | | | | | | | | | | | | | | aa1e1ed
        // * | | | | | | | | | | | | | | | 4c59de6
        // * | | | | | | | | | | | | | | |   6d3bee9
        // |\ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \
        // * | | | | | | | | | | | | | | | | 01bab86
        // | * | | | | | | | | | | | | | | | c1c5273
        // * | | | | | | | | | | | | | | | |   352a284
        // |\ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \
        // | |/ / / / / / / / / / / / / / / /
        // |/| | | | | | | | | | | | | | | |
        // | | | | | | | * | | | | | | | | | cba0511
        // | | | * | | | | | | | | | | | | | fc8c296
        // | |_|/ / / / / / / / / / / / / /
        // |/| | | | | | | | | | | | | | |
        // * | | | | | | | | | | | | | | |   20a864d
        // |\ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \
        // * \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \   5a71ea0
        // |\ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \
        // | * | | | | | | | | | | | | | | | | e182624
        // | * | | | | | | | | | | | | | | | |   05c7004
        // | |\ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \
        // |/ / / / / / / / / / / / / / / / / /
        // | | | | | | | | * | | | | | | | | | f1fafdf
        // | | | | | | | | * | | | | | | | | |   783a57a
        // | | | | | | | | |\ \ \ \ \ \ \ \ \ \
        // | |_|_|_|_|_|_|_|/ / / / / / / / / /
        // |/| | | | | | | | | | | | | | | | |
        // * | | | | | | | | | | | | | | | | | fc3b877
        // | | | | | | | | * | | | | | | | | |   ad96bf9
        // | | | | | | | | |\ \ \ \ \ \ \ \ \ \
        // * | | | | | | | | | | | | | | | | | | e2d267f
        // * | | | | | | | | | | | | | | | | | |   53a4604
        // |\ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \
        // | |_|_|_|_|_|_|_|_|/ / / / / / / / / /
        // |/| | | | | | | | | | | | | | | | | |
        // * | | | | | | | | | | | | | | | | | | f8a2d65
        // * | | | | | | | | | | | | | | | | | |   6ebf247
        // |\ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \
        // * | | | | | | | | | | | | | | | | | | | 8eac4aa
        // * | | | | | | | | | | | | | | | | | | |   c0c1462
        // |\ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \
        // * | | | | | | | | | | | | | | | | | | | | dc05a00
        // * | | | | | | | | | | | | | | | | | | | |   609b11c
        // |\ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \
        // * \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \   fd75ca7
        // |\ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \
        // | | | | | | | | | | | | | * | | | | | | | | | 9482369
        // | * | | | | | | | | | | | | | | | | | | | | | 74cd042
        // | * | | | | | | | | | | | | | | | | | | | | |   8bcb2f2
        // | |\ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \
        // | | | | | | | | | | | | | | | * | | | | | | | | ca02690
        // | | | | | | | | | | | | | | | * | | | | | | | | 117e818
        // | | | | | | | | | | | | | | | * | | | | | | | | 56dc7b9
        // | | | | | | * | | | | | | | | | | | | | | | | |   ba6ace8
        // | | | | | | |\ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \
        // | | * | | | | | | | | | | | | | | | | | | | | | | 9bc9350
        // | | | | | | | | | * | | | | | | | | | | | | | | | 7a02703
        // | | | | | | | | | | |_|_|_|_|_|_|/ / / / / / / /
        // | | | | | | | | | |/| | | | | | | | | | | | | |
        // | | | | | | | | | | | | | | | | * | | | | | | | 124f664
        // | | | | | | | | | | | | | | | | * | | | | | | | d91c393
        // | | | | | | | | | | | | | | | | * | | | | | | | a16eca7
        // | | | | | | | | | | |_|_|_|_|_|/ / / / / / / /
        // | | | | | | | | | |/| | | | | | | | | | | | |
        // | | | | | | | | * | | | | | | | | | | | | | | 3c89f92
        // | | | | | | | | | | * | | | | | | | | | | | | 35f1da7
        // | | | | | | | | | |/ / / / / / / / / / / / /
        // | | | | | | | | | | | * | | | | | | | | | | a5ca650
        // | | | | | | | | | | | | |_|_|_|_|_|_|_|/ /
        // | | | | | | | | | | | |/| | | | | | | | |
        // | | | | | | | | | | | | | | * | | | | | | bc77f1f
        // | | | | | | | | | | | | | | * | | | | | | d6d8551
        // | |_|_|_|_|_|_|_|_|_|_|_|_|/ / / / / / /
        // |/| | | | | | | | | | | | | | | | | | |
        // * | | | | | | | | | | | | | | | | | | | e03b28c
        // | | | | | | | | * | | | | | | | | | | | ef71646
        // | |_|_|_|_|_|_|/ / / / / / / / / / / /
        // |/| | | | | | | | | | | | | | | | | |
        // | | | | | | | | | | | | * | | | | | | 5565c43
        // | | | | | | | | | | | * | | | | | | | b039d33
        // | | | | | | | | | | | |/ / / / / / /
        // | | * | | | | | | | | | | | | | | | ccb8031
        // | | * | | | | | | | | | | | | | | | dfc33ec
        // | | | | | | | | | * | | | | | | | | 69b9cf5
        // | | | | | | | | |/ / / / / / / / /
        // | | * | | | | | | | | | | | | | | 07d9733
        // | | * | | | | | | | | | | | | | | b0c3704
        // | | * | | | | | | | | | | | | | | 4078d95
        // | | * | | | | | | | | | | | | | | fe64642
        // | | * | | | | | | | | | | | | | | 9cb8666
        // | | * | | | | | | | | | | | | | | b03ce6b
        // | | * | | | | | | | | | | | | | | 037cd00
        // | |/ / / / / / / / / / / / / / /
        // | * | | | | | | | | | | | | | | ccf3fd2
        // | * | | | | | | | | | | | | | |   49353bc
        // | |\ \ \ \ \ \ \ \ \ \ \ \ \ \ \
        // | | |_|_|_|_|/ / / / / / / / / /
        // | |/| | | | | | | | | | | | | |
        // | * | | | | | | | | | | | | | | 45792dd
        // | * | | | | | | | | | | | | | |   8d4d72c
        // | |\ \ \ \ \ \ \ \ \ \ \ \ \ \ \
        // | | |_|_|_|_|_|/ / / / / / / / /
        // | |/| | | | | | | | | | | | | |
        // | | * | | | | | | | | | | | | | e7a8c94
        // | | * | | | | | | | | | | | | | 9bfb26c
        // | | * | | | | | | | | | | | | | c2f1da0
        // | | * | | | | | | | | | | | | | 61921bb
        // | | * | | | | | | | | | | | | | aee4864
        // | | | * | | | | | | | | | | | | c455560
        // | | * | | | | | | | | | | | | | 711e36f
        // | | * | | | | | | | | | | | | | 387232c
        // | | * | | | | | | | | | | | | | ee5265d
        // | | * | | | | | | | | | | | | | 9815986
        // | | * | | | | | | | | | | | | | 8c1ad3f
        // | | * | | | | | | | | | | | | | 37768ef
        // | | * | | | | | | | | | | | | | b8f4056
        // | | * | | | | | | | | | | | | | 099dc72
        // | | * | | | | | | | | | | | | | 921db43
        // | | * | | | | | | | | | | | | | d7b832a
        // | | * | | | | | | | | | | | | | c93a0ff
        // | | * | | | | | | | | | | | | | 0d98bbe
        // | | | * | | | | | | | | | | | | 83eced3
        // | | | | |_|_|_|_|_|_|_|/ / / /
        // | | | |/| | | | | | | | | | |
        // | | | | | * | | | | | | | | | 5cb4bf5
        // | | * | | | | | | | | | | | |   4645c19
        // | | |\ \ \ \ \ \ \ \ \ \ \ \ \
        // | | | | |_|/ / / / / / / / / /
        // | | | |/| | | | | | | | | | |
        // | | * | | | | | | | | | | | |   62d725a
        // | | |\ \ \ \ \ \ \ \ \ \ \ \ \
        // | | | | | | | | * | | | | | | | bb266fb
        // | | * | | | | | | | | | | | | | 80c60d8
        // | | * | | | | | | | | | | | | | 25aae7b
        // | | * | | | | | | | | | | | | | 3147955
        // | | * | | | | | | | | | | | | | 2a782ce
        // | | * | | | | | | | | | | | | | d59e936
        // | | * | | | | | | | | | | | | | 33903de
        // | | * | | | | | | | | | | | | | 34b1373
        // | | * | | | | | | | | | | | | | a8ceb1c
        // | | * | | | | | | | | | | | | |   7f9d00d
        // | | |\ \ \ \ \ \ \ \ \ \ \ \ \ \
        // | | * | | | | | | | | | | | | | | 444ec05
        // | | | | | | | | * | | | | | | | | 8cb3dc3
        // | | | | | | | | * | | | | | | | | aff24fd
        // | | |_|_|_|_|_|/ / / / / / / / /
        // | |/| | | | | | | | | | | | | |
        // | * | | | | | | | | | | | | | | a8d253a
        // | * | | | | | | | | | | | | | |   ee867bd
        // | |\ \ \ \ \ \ \ \ \ \ \ \ \ \ \
        // | * | | | | | | | | | | | | | | | 7fb80a1
        // | * | | | | | | | | | | | | | | | cbf64fc
        // | * | | | | | | | | | | | | | | |   0646286
        // | |\ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \
        // | | * | | | | | | | | | | | | | | | e58d254
        // | | * | | | | | | | | | | | | | | | ca21dc7
        // | | * | | | | | | | | | | | | | | | 2e56ede
        // | | * | | | | | | | | | | | | | | | bac4a05
        // | * | | | | | | | | | | | | | | | | ab5b2f9
        // | * | | | | | | | | | | | | | | | |   5cbad19
        // | |\ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \
        // | | | | * | | | | | | | | | | | | | | 20374ce
        // | | |_|/ / / / / / / / / / / / / / /
        // | |/| | | | | | | | | | | | | | | |
        // | | | * | | | | | | | | | | | | | | 71a5b1a
        // | * | | | | | | | | | | | | | | | | ad0c4ef
        // | * | | | | | | | | | | | | | | | |   fb31f95
        // | |\ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \
        // | | |_|_|_|_|_|_|_|_|_|/ / / / / / /
        // | |/| | | | | | | | | | | | | | | |
        // | | | | * | | | | | | | | | | | | | 515722d
        // | | * | | | | | | | | | | | | | | | 0f8d119
        // | | | | * | | | | | | | | | | | | | 89fff44
        // | | * | | | | | | | | | | | | | | | fa64a65
        // | | * | | | | | | | | | | | | | | | 1660da0
        // | | * | | | | | | | | | | | | | | | 601efa5
        // | | | |_|_|_|_|_|/ / / / / / / / /
        // | | |/| | | | | | | | | | | | | |
        // | | | | * | | | | | | | | | | | | ce7085b
        // | | | | * | | | | | | | | | | | | 2d993c5
        // | | | | * | | | | | | | | | | | | 6b12a0b
        // | | | | * | | | | | | | | | | | | dda1c32
        // | | | | * | | | | | | | | | | | | 400f215
        // | | |_|/ / / / / / / / / / / / /
        // | |/| | | | | | | | | | | | | |
        // | * | | | | | | | | | | | | | | 1c07f1d
        // | * | | | | | | | | | | | | | |   3612307
        // | |\ \ \ \ \ \ \ \ \ \ \ \ \ \ \
        // | * | | | | | | | | | | | | | | | 6d7b6da
        // | * | | | | | | | | | | | | | | |   45c4247
        // | |\ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \
        // | * | | | | | | | | | | | | | | | | fa8bf87
        // | * | | | | | | | | | | | | | | | |   1308f89
        // | |\ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \
        // |/ / / / / / / / / / / / / / / / / /
        // | * | | | | | | | | | | | | | | | | a305c14
        // * | | | | | | | | | | | | | | | | | 1afa5a2
        // * | | | | | | | | | | | | | | | | |   696ca47
        // |\ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \
        // | |_|_|_|/ / / / / / / / / / / / / /
        // |/| | | | | | | | | | | | | | | | |
        // | * | | | | | | | | | | | | | | | | 532a594
        // | | * | | | | | | | | | | | | | | | 3857644
        // | |/ / / / / / / / / / / / / / / /
        // |/| | | | | | | | | | | | | | | |
        // | * | | | | | | | | | | | | | | |   1496f27
        // | |\ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \
        // | |/ / / / / / / / / / / / / / / /
        // |/| | | | | | | | | | | | | | | |
        // | | * | | | | | | | | | | | | | | c1faba8
        // | | * | | | | | | | | | | | | | | a5f0712
        // | | | * | | | | | | | | | | | | | 8cf5932
        // | |_|/ / / / / / / / / / / / / /
        // |/| | | | | | | | | | | | | | |
        // * | | | | | | | | | | | | | | | 5a79ba5
        // * | | | | | | | | | | | | | | |   61ccb73
        // |\ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \
        // | |_|_|_|_|_|_|/ / / / / / / / /
        // |/| | | | | | | | | | | | | | |
        // | | * | | | | | | | | | | | | | 3dbfa10
        // * | | | | | | | | | | | | | | | 77015f0
        // * | | | | | | | | | | | | | | |   82c25ca
        // |\ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \
        // | * | | | | | | | | | | | | | | | 9e6a3d1
        // * | | | | | | | | | | | | | | | | 1354c40
        // * | | | | | | | | | | | | | | | |   cdb8a85
        // |\ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \
        // | | | | | * | | | | | | | | | | | | b64998f
        // | | | | | * | | | | | | | | | | | |   d0575b9
        // | | | | | |\ \ \ \ \ \ \ \ \ \ \ \ \
        // | | | * | | | | | | | | | | | | | | | 03b568f
        // | | | * | | | | | | | | | | | | | | | 333df58
        // | | | * | | | | | | | | | | | | | | | 120840d
        // | | | * | | | | | | | | | | | | | | | a99016c
        // | | * | | | | | | | | | | | | | | | | 8fd0116
        // | | | * | | | | | | | | | | | | | | | e9f1e01
        // | | | | |_|_|_|_|_|/ / / / / / / / /
        // | | | |/| | | | | | | | | | | | | |
        // | | | | | | | * | | | | | | | | | | 194aeca
        // | | * | | | | | | | | | | | | | | |   124c62c
        // | | |\ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \
        // | | * | | | | | | | | | | | | | | | | 405b853
        // | | * | | | | | | | | | | | | | | | | ea2c2d2
        // | | | | | | | | * | | | | | | | | | | eb12941
        // | | * | | | | | | | | | | | | | | | | e49e8c5
        // | | | | | | | | * | | | | | | | | | | 0c2fce5
        // | | | | | | | | * | | | | | | | | | | 77429d0
        // | * | | | | | | | | | | | | | | | | | 1bdfd7e
        // |/ / / / / / / / / / / / / / / / / /
        // * | | | | | | | | | | | | | | | | | f4bd90a
        // * | | | | | | | | | | | | | | | | |   fea9555
        // |\ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \
        // * | | | | | | | | | | | | | | | | | | 2057943
        // * | | | | | | | | | | | | | | | | | |   f61f33d
        // |\ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \
        // | |_|_|_|_|_|_|/ / / / / / / / / / / /
        // |/| | | | | | | | | | | | | | | | | |
        // * | | | | | | | | | | | | | | | | | | 4eccb15
        // * | | | | | | | | | | | | | | | | | |   69abc57
        // |\ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \
        // | | * | | | | | | | | | | | | | | | | | 3e40c9a
        // | |/ / / / / / / / / / / / / / / / / /
        // |/| | | | | | | | | | | | | | | | | |
        // | * | | | | | | | | | | | | | | | | | c29c2b6
        // | * | | | | | | | | | | | | | | | | | 7365078
        // | | |_|_|/ / / / / / / / / / / / / /
        // | |/| | | | | | | | | | | | | | | |
        // | | * | | | | | | | | | | | | | | | 400dcfa
        // | |/ / / / / / / / / / / / / / / /
        // |/| | | | | | | | | | | | | | | |
        // * | | | | | | | | | | | | | | | | 34cda0d
        // * | | | | | | | | | | | | | | | |   46a4ede
        // |\ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \
        // * | | | | | | | | | | | | | | | | | 74b0a4f
        // * | | | | | | | | | | | | | | | | | 2c93498
        // * | | | | | | | | | | | | | | | | |   703ba7b
        // |\ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \
        // | |_|/ / / / / / / / / / / / / / / /
        // |/| | | | | | | | | | | | | | | | |
        // * | | | | | | | | | | | | | | | | | 2b6884b
        // | |_|_|_|_|_|_|_|_|_|/ / / / / / /
        // |/| | | | | | | | | | | | | | | |
        // * | | | | | | | | | | | | | | | |   57a3b89
        // |\ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \
        // * | | | | | | | | | | | | | | | | | 2065dca
        // * | | | | | | | | | | | | | | | | |   93815f3
        // |\ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \
        // * | | | | | | | | | | | | | | | | | | e25f28f
        // * | | | | | | | | | | | | | | | | | |   1acadff
        // |\ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \
        // * | | | | | | | | | | | | | | | | | | | 5a4e1f8
        // * | | | | | | | | | | | | | | | | | | |   a2ca314
        // |\ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \
        // | * | | | | | | | | | | | | | | | | | | | 2923a1c
        // | * | | | | | | | | | | | | | | | | | | | 9916405
        // | * | | | | | | | | | | | | | | | | | | | 6764a14
        // | | | | * | | | | | | | | | | | | | | | | 5c818b0
        // | |_|_|/ / / / / / / / / / / / / / / / /
        // |/| | | | | | | | | | | | | | | | | | |
        // | | | * | | | | | | | | | | | | | | | | 7cfa6e4
        // | | | * | | | | | | | | | | | | | | | | c804139
        // | * | | | | | | | | | | | | | | | | | | 2ed4478
        // | * | | | | | | | | | | | | | | | | | | a3726a9
        // | | | | * | | | | | | | | | | | | | | | 7fe5ac9
        // | | | | * | | | | | | | | | | | | | | |   b3521c5
        // | | | | |\ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \
        // | | | | * | | | | | | | | | | | | | | | | be9799e
        // | | * | | | | | | | | | | | | | | | | | | f0e4c6f
        // | |/ / / / / / / / / / / / / / / / / / /
        // |/| | | | | | | | | | | | | | | | | | |
        // | * | | | | | | | | | | | | | | | | | | 18abd03
        // | | | * | | | | | | | | | | | | | | | | b1fca5e
        // | * | | | | | | | | | | | | | | | | | | 9e907dc
        // | | | * | | | | | | | | | | | | | | | | f6268a0
        // | | | * | | | | | | | | | | | | | | | | c90c574
        // | | | * | | | | | | | | | | | | | | | | 5bd5cf9
        // | * | | | | | | | | | | | | | | | | | | 22cf0ac
        // |/ / / / / / / / / / / / / / / / / / /
        // * | | | | | | | | | | | | | | | | | | c12e28c
        // * | | | | | | | | | | | | | | | | | |   90ef005
        // |\ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \
        // | |_|_|_|_|_|/ / / / / / / / / / / / /
        // |/| | | | | | | | | | | | | | | | | |
        // * | | | | | | | | | | | | | | | | | | 14c7def
        // * | | | | | | | | | | | | | | | | | |   6a0d13b
        // |\ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \
        // * | | | | | | | | | | | | | | | | | | | 2ef095e
        // * | | | | | | | | | | | | | | | | | | |   2ed181c
        // |\ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \
        // * | | | | | | | | | | | | | | | | | | | | ed572d2
        // * | | | | | | | | | | | | | | | | | | | |   f1ec3fe
        // |\ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \
        // * | | | | | | | | | | | | | | | | | | | | | fe65176
        // * | | | | | | | | | | | | | | | | | | | | | 8fd08b8
        // * | | | | | | | | | | | | | | | | | | | | |   2d8cd8d
        // |\ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \
        // * | | | | | | | | | | | | | | | | | | | | | | 4fe62b2
        // * | | | | | | | | | | | | | | | | | | | | | |   e8bf07e
        // |\ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \
        // * | | | | | | | | | | | | | | | | | | | | | | | b57da7b
        // * | | | | | | | | | | | | | | | | | | | | | | |   fa9c7a5
        // |\ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \
        // * | | | | | | | | | | | | | | | | | | | | | | | | 7f14dfd
        // * | | | | | | | | | | | | | | | | | | | | | | | |   68d72b3
        // |\ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \
        // * \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \   795edec
        // |\ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \
        // * | | | | | | | | | | | | | | | | | | | | | | | | | | 1a94d9e
        // * | | | | | | | | | | | | | | | | | | | | | | | | | |   e9fccb1
        // |\ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \
        // | | * | | | | | | | | | | | | | | | | | | | | | | | | | 8805443
        // * | | | | | | | | | | | | | | | | | | | | | | | | | | | b24b0fe
        // * | | | | | | | | | | | | | | | | | | | | | | | | | | |   6e76d4b
        // |\ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \
        // | | |/ / / / / / / / / / / / / / / / / / / / / / / / / /
        // | |/| | | | | | | | | / / / / / / / / / / / / / / / / /
        // | |_|_|_|_|_|_|_|_|_|/ / / / / / / / / / / / / / / / /
        // |/| | | | | | | | | | | | | | | | | | | | | | | | | |
        // | | | * | | | | | | | | | | | | | | | | | | | | | | | 024ff26
        // | * | | | | | | | | | | | | | | | | | | | | | | | | | 0892f09
        // | | | | | | | | | * | | | | | | | | | | | | | | | | | 34c7eb2
        // | | |_|_|_|_|_|_|/ / / / / / / / / / / / / / / / / /
        // | |/| | | | | | | | | | | | | | | | | | | | | | | |
        // | | * | | | | | | | | | | | | | | | | | | | | | | | 23f75d9
        // | | | | | | | | | * | | | | | | | | | | | | | | | | 58f0994
        // | | | | | | | | | * | | | | | | | | | | | | | | | | faa33f9
        // | | * | | | | | | | | | | | | | | | | | | | | | | | 3586bc3
        // | | * | | | | | | | | | | | | | | | | | | | | | | | 8501184
        // | | * | | | | | | | | | | | | | | | | | | | | | | | b98acd3
        // | | | * | | | | | | | | | | | | | | | | | | | | | | 664b0ce
        // | | | | * | | | | | | | | | | | | | | | | | | | | | ac25d6d
        // | | | | | * | | | | | | | | | | | | | | | | | | | | 507f295
        // | | | | |/ / / / / / / / / / / / / / / / / / / / /
        // | | | | | | * | | | | | | | | | | | | | | | | | | fe34841
        // | | | | | * | | | | | | | | | | | | | | | | | | | b8b2e16
        // | |_|_|_|/ / / / / / / / / / / / / / / / / / / /
        // |/| | | | | | | | | | | | | | | | | | | | | | |
        // | | | | | * | | | | | | | | | | | | | | | | | | 76d73e9
        // | |_|_|_|/ / / / / / / / / / / / / / / / / / /
        // |/| | | | | | | | | | | | | | | | | | | | | |
        // * | | | | | | | | | | | | | | | | | | | | | | d59eaac
        // |/ / / / / / / / / / / / / / / / / / / / / /
        // * | | | | | | | | | | | | | | | | | | | | | 90edd94
        // * | | | | | | | | | | | | | | | | | | | | |   30b3598
        // |\ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \
        // * | | | | | | | | | | | | | | | | | | | | | | 8545ff9
        // * | | | | | | | | | | | | | | | | | | | | | |   2575b49
        // |\ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \
        // * | | | | | | | | | | | | | | | | | | | | | | | 2bad9c6
        // * | | | | | | | | | | | | | | | | | | | | | | |   a213cce
        // |\ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \
        // * | | | | | | | | | | | | | | | | | | | | | | | | 9e2b02d
        // * | | | | | | | | | | | | | | | | | | | | | | | |   7a8e6ca
        // |\ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \
        // | |_|_|_|_|_|_|_|_|_|/ / / / / / / / / / / / / / /
        // |/| | | | | | | | | | | | | | | | | | | | | | | |
        // * | | | | | | | | | | | | | | | | | | | | | | | | 316b23e
        // * | | | | | | | | | | | | | | | | | | | | | | | |   e9c2495
        // |\ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \
        // | | | * | | | | | | | | | | | | | | | | | | | | | | 77aa363
        // | | | | |_|/ / / / / / / / / / / / / / / / / / / /
        // | | | |/| | | | | | | | | | | | | | | | | | | | |
        // * | | | | | | | | | | | | | | | | | | | | | | | |   4a57e50
        // |\ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \
        // | |_|_|/ / / / / / / / / / / / / / / / / / / / / /
        // |/| | | | | | | | | | | | | | | | | | | | | | | |
        // | | | | * | | | | | | | | | | | | | | | | | | | | 686e906
        // * | | | | | | | | | | | | | | | | | | | | | | | | f3ed08b
        // * | | | | | | | | | | | | | | | | | | | | | | | |   6e0fc9e
        // |\ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \
        // | |_|_|_|_|_|_|_|_|_|_|_|_|_|_|/ / / / / / / / / /
        // |/| | | | | | | | | | | | | | | | | | | | | | | |
        // | | | | * | | | | | | | | | | | | | | | | | | | | 2b859fb
        // | |_|_|/ / / / / / / / / / / / / / / / / / / / /
        // |/| | | | | | | | | | | | | | | | | | | | | | |
        // | * | | | | | | | | | | | | | | | | | | | | | | a4876b2
        // | | | | * | | | | | | | | | | | | | | | | | | | 8d6ab39
        // | |_|_|/ / / / / / / / / / / / / / / / / / / /
        // |/| | | | | | | | | | | | | | | | | | | | | |
        // | | | | * | | | | | | | | | | | | | | | | | | f35ce60
        // | * | | | | | | | | | | | | | | | | | | | | | 8d91ad9
        // | | | | * | | | | | | | | | | | | | | | | | | 780a472
        // | | | | | |_|_|_|/ / / / / / / / / / / / / /
        // | | | | |/| | | | | | | | | | | | | | | | |
        // | * | | | | | | | | | | | | | | | | | | | | 25f31a0
        // | | | * | | | | | | | | | | | | | | | | | | 5c6368c
        // * | | | | | | | | | | | | | | | | | | | | | f3568d7
        // | * | | | | | | | | | | | | | | | | | | | | f697044
        // | * | | | | | | | | | | | | | | | | | | | | d2f9305
        // * | | | | | | | | | | | | | | | | | | | | |   ae5f785
        // |\ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \
        // | | * | | | | | | | | | | | | | | | | | | | | 0c2021e
        // | | | |_|/ / / / / / / / / / / / / / / / / /
        // | | |/| | | | | | | | | | | | | | | | | | |
        // | * | | | | | | | | | | | | | | | | | | | | 5a509c4
        // | | | * | | | | | | | | | | | | | | | | | | 698fff6
        // | | | * | | | | | | | | | | | | | | | | | | acca572
        // | | | | | | | | * | | | | | | | | | | | | | 2e1bedc
        // | | | | | | | | * | | | | | | | | | | | | | 88f71b8
        // | | | | | | | | | | | | | | | | * | | | | | 641300e
        // | | | | * | | | | | | | | | | | | | | | | | 0f0d238
        // | | | | * | | | | | | | | | | | | | | | | | 17df052
        // | | | | | | | * | | | | | | | | | | | | | | cc53fe7
        // | | | | | |_|/ / / / / / / / / / / / / / /
        // | | | | |/| | | | | | | | | | | | | | | |
        // | | | | | | | | * | | | | | | | | | | | | 62eeae4
        // | | | | | | | * | | | | | | | | | | | | | 5c38b91
        // | | | | | | | * | | | | | | | | | | | | | df417e0
        // | | | | | | | | | | | | | | * | | | | | | a019d50
        // | | | | | | | | | | | | | | | |/ / / / /
        // | | | | | | | | | | | | | | |/| | | | |
        // | * | | | | | | | | | | | | | | | | | |   f3d60bd
        // | |\ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \ \
        // | * | | | | | | | | | | | | | | | | | | | ff1962a
        // | | | | | | | | | | | * | | | | | | | | | f8118e0
        // | | | | | | | | | | | | * | | | | | | | | 07722ff
        // | | | | | | | | | | | | * | | | | | | | | 031b77c
        // | | | | | | | | | | | | | | | | | | * | |   26b2f32
        // | | | | | | | | | | | | | | | | | | |\ \ \
        // | | | | | | | | | | | | | | | | | | * | | | 506c242
        // | | | | | | | | | | | | | | | | | | * | | | 7e36ce5
        // | | | | | | * | | | | | | | | | | | | | | | af64a3a
        // | |_|_|_|_|/ / / / / / / / / / / / / / / /
        // |/| | | | | | | | | | | | | | | | | | | |
        // * | | | | | | | | | | | | | | | | | | | | 7edf520
        // * | | | | | | | | | | | | | | | | | | | | 6e29d27
        // * | | | | | | | | | | | | | | | | | | | | 60d9f81
        // * | | | | | | | | | | | | | | | | | | | | 4c09732
        // * | | | | | | | | | | | | | | | | | | | |   fe88200

    }
    
    private boolean printAndValidateRowContents(GitGraphRow row,
            String expectedHash, int expectedPosition,
            List<GitGraphLine> expectedLines) {
        boolean rowValidates = validateRowContents(row, expectedHash,
                expectedPosition, expectedLines);
        if(!rowValidates) {
            System.out.println(row.toString());
        }
        return rowValidates;
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
