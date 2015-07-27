package edu.unl.cse.knorth.git_sonification.data_collection.components.factories;

import edu.unl.cse.knorth.git_sonification.data_collection.components.Component;
import edu.unl.cse.knorth.git_sonification.data_collection.components.implementations.MiscellaneousComponent;
import edu.unl.cse.knorth.git_sonification.data_collection.components.implementations.FilePathStringComponent;
import java.io.File;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;

/**
 * Produces an individual Component for each file in a directory. This lets us
 * show each individual file on its own layer in the final visualization.
 */
public class IndividualFileComponentFactory {
    public List<Component> createFileComponents(File rootDirectory) {
        List<Component> components = new LinkedList<>();
        
        Collection<File> files = FileUtils.listFiles(rootDirectory,
                TrueFileFilter.INSTANCE, new IgnoreDotGitDirectoryFileFilter());

        String rootPath = rootDirectory.getAbsolutePath();
        for(File file : files) {
            String absoluteFilePath = file.getAbsolutePath();
            String relativeFilePath =
                    absoluteFilePath.substring(rootPath.length() + 1);
            
            Component newComponent =
                    new FilePathStringComponent(relativeFilePath);
            components.add(newComponent);
        }
        
        Component miscellaneousComponent
            = new MiscellaneousComponent(components);
        components.add(miscellaneousComponent);
        
        return components;
    }
}