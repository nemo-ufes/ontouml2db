/**
 * A ContainerProperty is intended to group all the properties of a node. 
 * This interface groups together all methods needed to manipulate the node's properties. 
 * 
 * Author: Gustavo L. Guidoni
 */
package br.ufes.inf.nemo.ontouml2db.graph;

import java.util.ArrayList;

public interface IPropertyContainer {
	
	/**
	 * Adds a new property in the container.
	 * 
	 * @param property. Property to be added.
	 */
	public void addProperty(IOntoProperty property);
	
	/**
	 * Adds a new property in the container in a specific position.
	 * 
	 * @param index. Position in which the property will be added. 
	 * @param property. Property to be added.
	 */
	public void addPropertyAt(int index, IOntoProperty property );
	
	/**
	 * Adds a set of property in the container.
	 * 
	 * @param properties. An ArrayList with the properties to be added.
	 */
	public void addProperty(ArrayList<IOntoProperty> properties);
	
	/**
	 * Adds a set of property in the container from a specific position
	 * 
	 * @param index. Initial position to be added to the properties.
	 * @param properties. Propertues to be added.
	 */
	public void addPropertyAt(int index, ArrayList<IOntoProperty> properties );
	
	/**
	 * Returns all properties of the container. 
	 * 
	 * @return An ArrayList with all properties.
	 */
	public ArrayList<IOntoProperty> getProperties();
	
	/**
	 * Clone the container and indicate which node it belongs to.
	 * 
	 * @param sourceNode. Node to which the container belongs.
	 * @return A new container with new properties.
	 */
	public IPropertyContainer clonePropertyContainer(INode sourceNode);
	
	/**
	 * Removes a specific property of the container.
	 * 
	 * @param property. Property to be removed.
	 */
	public void removeProperty(IOntoProperty property);
	
	/**
	 * Returns a property of the container.
	 * 
	 * @param propertyName. Property name to be searched.
	 * @return The property instance stored in the container.
	 */
	public IOntoProperty getProperty(String propertyName);
	
	/**
	 * Returns the property marked as the primary key.
	 * 
	 * @return The primary key property
	 */
	public IOntoProperty getPrimaryKey();
	
	/**
	 * Finds the property marked as primary key and returns its name.
	 *  
	 * @return A string with the primary key name.
	 */
	public String getPKName();
	
	/**
	 * Checks if there is any property with the given name.
	 * 
	 * @param propertyName. Property name to be searched.
	 * @return True if the property name exists in the container, otherwise false.
	 */
	public boolean existsPropertyName(String propertyName);
	
}
