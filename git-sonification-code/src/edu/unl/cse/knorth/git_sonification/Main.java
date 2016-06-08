package edu.unl.cse.knorth.git_sonification;

import edu.unl.cse.knorth.git_sonification.display.view.two_dimensional.branch_view.BranchView;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        System.setProperty("apple.awt.application.name", "GitVS");
        BranchView.main(args);
    }
}
