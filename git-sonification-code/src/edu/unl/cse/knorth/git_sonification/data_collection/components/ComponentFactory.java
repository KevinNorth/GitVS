package edu.unl.cse.knorth.git_sonification.data_collection.components;

import edu.unl.cse.knorth.git_sonification.data_collection.components.factories.DirectoryRegexComponentFactory;
import edu.unl.cse.knorth.git_sonification.data_collection.components.factories.IndividualFileComponentFactory;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class ComponentFactory {
    public List<Component> createComponents(CreateComponentTechniques technique,
            File gitRepoRoot, String pathToDirectoryRegexConfigFile)
            throws IOException {
        switch(technique) {
            case DIRECTORY_REGEXES:
                return new DirectoryRegexComponentFactory()
                        .parseComponents(pathToDirectoryRegexConfigFile);
            case EACH_INDIVIDUAL_FILE:
                return new IndividualFileComponentFactory()
                        .createFileComponents(gitRepoRoot);
            default:
                throw new IllegalArgumentException();
        }
    }
}