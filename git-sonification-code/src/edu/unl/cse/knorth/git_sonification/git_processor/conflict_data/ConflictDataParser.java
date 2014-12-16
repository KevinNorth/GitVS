package edu.unl.cse.knorth.git_sonification.git_processor.conflict_data;

import java.io.IOException;
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
     * line should be delimited by exactly one space.</p>
     * @param filepath The path to the file containing the conflict data.
     * @return A list of <code>Conflict</code>s representing the conflict data.
     * @throws IOException If there were problems reading the file.
     */
    public List<Conflict> parseConflictData(String filepath)
            throws IOException {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}