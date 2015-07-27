package edu.unl.cse.knorth.git_sonification.data_collection.components.implementations;

import edu.unl.cse.knorth.git_sonification.data_collection.components.Component;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * A Component that matches all files that do not belong to any other Components
 * in the target software.
 */
public class MiscellaneousComponent extends Component {
    private final List<Component> allOtherComponents;
    
    /**
     * @param allOtherComponents A Collection of all of the other Components
     * that will be checked in order to sort files into components.
     */
    public MiscellaneousComponent(Collection<Component> allOtherComponents) {
        super("Other");
        this.allOtherComponents = new LinkedList<>(allOtherComponents);
    }
    
    /**
     * @param allOtherComponents An array of all of the other Components
     * that will be checked in order to sort files into components.
     */
    public MiscellaneousComponent(Component... allOtherComponents) {
        this(Arrays.asList(allOtherComponents));
    }

    /**
     * Determines whether or not a file belongs to this Component in the target
     * software's design. A file belongs to this Component only if it does not
     * belong to any of the other Components that this Component was constructed
     * with.
     * @param filePath The path, relative to the Git repository's root, of the
     * file to check.
     * @return <code>false</code> if the file belongs to any of the Components
     * that this Component was constructed with. <code>false</code> if the file
     * does not match any of those Components.
     */
    @Override
    public boolean includesFile(String filePath) {
        for(Component component : allOtherComponents) {
            if(component.includesFile(filePath)) {
                return false;
            }
        }
        
        return true;
    }
}