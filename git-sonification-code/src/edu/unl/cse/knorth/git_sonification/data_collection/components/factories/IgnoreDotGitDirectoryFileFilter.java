package edu.unl.cse.knorth.git_sonification.data_collection.components.factories;

import java.io.File;
import org.apache.commons.io.filefilter.AbstractFileFilter;

/**
 * Filters out the .git directory inside of a Git repository's working directory
 */
public class IgnoreDotGitDirectoryFileFilter extends AbstractFileFilter {
    @Override
    public boolean accept(File file) {
        if(file.getName().equals(".git")) {
            return false;
        }
        
        return true;
    }
}