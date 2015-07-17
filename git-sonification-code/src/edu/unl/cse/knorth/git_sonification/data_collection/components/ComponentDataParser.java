package edu.unl.cse.knorth.git_sonification.data_collection.components;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Reads a text file containing regular expressions to use to identify
 * components in the target software's design.
 */
public class ComponentDataParser {
    /**
     * Reads a configuration file with a list of Java regexes. These regexes
     * will be used to categorize files into "Components" based on their
     * file paths. The idea is that the target software's directory structure
     * probably reflects the software's design in a way that the user is
     * familiar with, so they can select a few important directories to use for
     * organizing the files.
     * <p/>
     * The configuration file's format should be:
     * <ul><li>Each line should contain one regular expression.</li>
     * <li>Each regular expression will be parsed using Java's
     *  <code>Pattern.compile()</code> and compared to strings using
     *  <code>pattern.matcher()</code>.</li>
     * <li>Leading and trailing whitespace in lines is <i>not</i> ignored.</li>
     * <li>Empty lines are skipped. (Lines containing only whitespace are
     *  <i>not</i> skipped.)</li></ul>
     * @param filepath The path to the configuration file containing the regular
     * expressions.
     * @return A List of Components, one for each regular expression in the
     * configuration. In addition, the list of Components will include one
     * Component that will include any files that do not match any of the
     * other Component's regexes.
     * @throws IOException 
     */
    public List<Component> parseComponents(String filepath) throws IOException {
        List<Component> components = new LinkedList<>();
        
        try(BufferedReader reader = new BufferedReader(
                new FileReader(filepath))) {
            String line = reader.readLine();
            
            while(line != null) {
                // Note that according to the docs
                // (https://docs.oracle.com/javase/7/docs/api/java/io/BufferedReader.html#readLine()),
                // reader.readLine() does NOT include the line break at the end
                // of the line as part of the String it returns.
                if(!line.isEmpty()) {
                    components.add(parseSingleComponent(line));
                }
                line = reader.readLine();
            }
        }
        
        Component miscellaneousComponent
                = new MiscellaneousComponent(components);
        components.add(miscellaneousComponent);
        
        return components;
    }
    
    private Component parseSingleComponent(String line) {
        return new DirectoryComponent(line);
    }
}