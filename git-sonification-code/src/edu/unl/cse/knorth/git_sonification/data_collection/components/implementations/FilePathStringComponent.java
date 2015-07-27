package edu.unl.cse.knorth.git_sonification.data_collection.components.implementations;

import edu.unl.cse.knorth.git_sonification.data_collection.components.Component;

/**
 * A FilePathStringComponent matches a file if the file's path String exactly
 * matches a specified String. Multiple instances of this class can be used to
 * show all of the individual files that each commit modifies.
 */
public class FilePathStringComponent extends Component {
    private final String pathString;

    public FilePathStringComponent(String pathString) {
        super(pathString);
        this.pathString = pathString;
    }

    public String getPathString() {
        return pathString;
    }
    
    @Override
    public boolean includesFile(String filePath) {
        return filePath.equals(pathString);
    }
}