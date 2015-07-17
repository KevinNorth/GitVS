package edu.unl.cse.knorth.git_sonification.data_collection.conflict_data;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Parses a file containing Bakhtiar's conflict data for Voldemort.
 */
public class ConflictDataParser {
    /**
     * Parses a file that contains information about past conflicts in the
     * Voldemort project.
     * 
     * <p>This file is expected to be in the following format:</p>
     * 
     * <p><code>
     * abcdef 123456<br/>
     * aaaaaa 111111<br/>
     * </code></p>
     *
     * <p>Where each line contains a single commit, the first string on each
     * line contains the hash of the conflict that introduces the conflict, and
     * the second string on each line contains the hash of the conflict that
     * resolves the conflict. Each line should be newline delimited (either
     * <code>'\r'</code> or <code>"\n\r"</code>), and the two strings on each
     * line should be delimited by any whitespace that matches the regex /\s+/.
     * </p>
     * @param filepath The path to the file containing the conflict data.
     * @return A list of <code>Conflict</code>s representing the conflict data.
     * @throws IOException If there were problems reading the file.
     */
    public List<Conflict> parseConflictData(String filepath)
            throws IOException {
        List<Conflict> conflicts = new LinkedList<>();
        
        try(BufferedReader reader = new BufferedReader(
                new FileReader(filepath))) {
            String line = reader.readLine();
            
            while(line != null) {
                conflicts.add(parseSingleConflict(line));
                line = reader.readLine();
            }
        }
        
        return conflicts;
    }
    
    /**
     * Takes a single line of the file describing the conflicts in Voldermort
     * and parses it into a Conflict.
     * @param rawData The line to parse
     * @return A Conflict with the line's information.
     */
    private Conflict parseSingleConflict(String rawData) {
        String[] split = rawData.split("\\s+");
        return new Conflict(split[0], split[1]);
    }
}