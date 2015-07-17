package edu.unl.cse.knorth.git_sonification.data_collection.components;

import java.util.Collection;

/**
 * Represents the logic needed to identify a component in the design of the
 * software in the Git repository we are analyzing.
 */
public abstract class Component {
    private final String name;
    
    /**
     * @param name A String representing the name of the component in the target
     * software's design.
     */
    public Component(String name) {
        this.name = name;
    }
    
    /**
     * @return A String representing the name of the component in the target
     * software's design.
     */
    public String getName() {
        return name;
    }
    
    /**
     * Determines whether or not a file belongs to this Component in the target
     * software's design.
     * @param filePath The path, relative to the Git repository's root, of the
     * file to check.
     * @return <code>true</code> if the file belongs to this component.
     * <code>false</code> otherwise.
     */
    public abstract boolean includesFile(String filePath);
    
    /**
     * Determines whether or not at least one file in a list belongs to this
     * Component in the target software's design.
     * @param filePaths A collection of paths, relative to the Git repository's
     * root, of the file to check.
     * @return <code>true</code> if at least one file in the Collection belongs
     * to this component. <code>false</code> otherwise.
     */
    public boolean includesAnyFile(Collection<String> filePaths) {
        for(String filePath : filePaths) {
            if(includesFile(filePath)) {
                return true;
            }
        }
        
        return false;
    }
    
    @Override
    public String toString() {
        return getName();
    }
}