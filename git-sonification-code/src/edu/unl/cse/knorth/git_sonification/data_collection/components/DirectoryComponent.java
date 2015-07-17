package edu.unl.cse.knorth.git_sonification.data_collection.components;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This Component assumes that the target software's directory structure closely
 * mirrors the software's design. It uses a regular expression that matches
 * a directory or set of directories to represent the component in those
 * directories, then includes any files in the component which have file paths
 * that match the regular expression.
 * <p/>
 * To be strict, this Component includes files that have paths matching an
 * arbitrary regex, selected when the Component is constructed. Users are
 * recommended to select regexes corresponding to important directories, but
 * they can be clever if they want to and select regexes representing other
 * things.
 */
public class DirectoryComponent extends Component {
    private final Pattern directoryRegex;
    
    /**
     * @param directoryRegex A String representing a regex, probably one that
     * matches an important directory or set of directories in the target
     * software's directory structure. Any files with paths that match this
     * regex will be counted as belonging to this Component.
     */
    public DirectoryComponent(String directoryRegex) {
        super(directoryRegex);
        this.directoryRegex = Pattern.compile(directoryRegex);
    }

    /**
     * Determines whether or not a file belongs to this Component in the target
     * software's design by checking the file's path against the regex this
     * Component was constructed with.
     * @param filePath The path, relative to the Git repository's root, of the
     * file to check.
     * @return <code>true</code> if <code>filePath</code> matches this
     * Component's regex. <code>false</code> otherwise.
     */    @Override
    public boolean includesFile(String filePath) {
        Matcher matcher = directoryRegex.matcher(filePath);
        return matcher.find();
    }
}