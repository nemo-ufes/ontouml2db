/**
 * A node represents the existence of a class in the model, its properties and 
 * associations. A node is the composition of properties and associations, therefore, 
 * the node serves as an interface for manipulating properties and associations. The 
 * node has the capacity to tell which nodes it has been transformed to.
 * 
 * Author: Gustavo L. Guidoni
 * 
 */
package br.ufes.inf.nemo.ontouml2db.graph;

public interface INode extends IAssociationContainer, IPropertyContainer, INodeTracker{

	/**
	 * Informs the node name.
	 * 
	 * @param name. Name of the node.
	 */
	public void setName(String name);
	
	/**
	 * Returns the node name.
	 * 
	 * @return The name of the node.
	 */
	public String getName();
	
	/**
	 * Informs the node stereotype.
	 * 
	 * @param stereotype. The stereotype name.
	 */
	public void setStereotype(String stereotype);
	
	/**
	 * Returns the stereotype name. 
	 * 
	 * @return The name of the stereotype.
	 */
	public String getStereotype();
	
	/**
	 * Creates a new node with the same properties values.
	 * 
	 * @return A new node identical to the current one.
	 */
	public INode clone();
	
	/**
	 * Informs that the node has been resolved. Used in the transformation 
	 * processes to walk in the graph produced by the Nodes.
	 * 
	 * @param flag. True reports that the node was resolved, false not.
	 */
	public void setResolved(boolean flag);
	
	/**
	 * Returns if the node was resolved.
	 * 
	 * @return A boolean indicating whether the node has been resolved.
	 */
	public boolean isResolved();
	
	/**
	 * Returns a string containing the description of the trace.
	 * 
	 * @return A string with tracking of the node.
	 */
	public String trackingToString();

}
