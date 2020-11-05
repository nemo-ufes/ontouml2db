/**
 * Responsible for storing the tracking data. A node has one or more trackers.
 * 
 * Author: Gustavo L. Guidoni
 */
package br.ufes.inf.nemo.ontouml2db.graph;

public interface ITracker {
	
	/**
	 * Returns the tracker node.
	 * 
	 * @return A Node in the target graph.
	 */
	public INode getNode();

	/**
	 * Informs a node to be tracked in the target graph.
	 * 
	 * @param node. Node to be tracked.
	 */
	public void setNode(INode node);
	
	/**
	 * Returns the property on which the node is linked.
	 * 
	 * @return The property linked to the tracker node.
	 */
	public IOntoProperty getProperty();
	
	/**
	 * Informs the property linked to the tracker node.
	 * 
	 * @param property. Property to be linked to the tracker node.
	 */
	public void setProperty(IOntoProperty property);
	
	/**
	 * Returns the value of the node tracked in the property.
	 * 
	 * @return The node value in the property.
	 */
	public Object getValue();
	
	/**
	 * Informs the value of the node tracked in the property.
	 * 
	 * @param value. The node value in the property.
	 */
	public void setValue(Object value);
	
	/**
	 * Informs the node to which the property belongs.
	 * 
	 * @param node. The node linked to the property.
	 */
	public void setPropertyLinkedAtNode(INode node);
	
	
	/**
	 * Returns the node to which the property belongs.
	 * 
	 * @return The node linked to the property.
	 */
	public INode getPropertyLinkedAtNode();
}
