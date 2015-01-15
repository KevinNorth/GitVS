package edu.unl.cse.knorth.git_sonification;

import edu.unl.cse.knorth.git_sonification.git_processor.conflict_data.Conflict;
import edu.unl.cse.knorth.git_sonification.git_processor.conflict_data.ConflictDataParser;
import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        ConflictDataParser parser = new ConflictDataParser();
        List<Conflict> conflicts =
                parser.parseConflictData("data/conflict_data.dat");
        
        for(Conflict conflict : conflicts) {
            System.out.println(conflict);
        }
    }
}
