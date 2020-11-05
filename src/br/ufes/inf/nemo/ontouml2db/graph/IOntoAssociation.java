/**
 * Groups the set of behaviors that an association has. An association is 
 * the fundamental relationship between two nodes. Any type of relationship 
 * in the graph must inherit from an Association.
 * 
 * Author: Gustavo L. Gudioni
 */
package br.ufes.inf.nemo.ontouml2db.graph;

import java.util.ArrayList;


public interface IOntoAssociation{
	
	/**
	 * Indicates the association name.
	 * 
	 * @param name. Association name.
	 */
	public void setName(String name);
	
	/**
	 * Returns the association name.
	 * 
	 * @return A string with the association name.
	 */
	public String getName();
	
	/**
	 * Indicates the source node.
	 * 
	 * @param sourceNode. Association source node.
	 */
	public void setSourceNode(INode sourceNode);
	
	/**
	 * Returns the source node.
	 * 
	 * @return Association source node.
	 */
	public INode getSourceNode();
	
	/**
	 * Indicates the target node.
	 * 
	 * @param targetNode. Association target node.
	 */
	public void setTargetNode(INode targetNode);
		
	/**
	 * Returns the target node.
	 * 
	 * @return Association target node.
	 */
	public INode getTargetNode();
	
	/**
	 * Returns the source cardinality.
	 * 
	 * @return A Cardinality type with the source cardinality.
	 */
	public Cardinality getSourceCardinality();
	
	/**
	 * Returns the target cardinality.
	 * 
	 * @return A Cardinality type with the target cardinality.
	 */
	public Cardinality getTargetCardinality();
	
	/**
	 * Indicates the source cardinality.
	 * 
	 * @param sourceCardinality. The source cardinality of the association.
	 */
	public void setSourceCardinality(Cardinality sourceCardinality);
	
	/**
	 * Indicates the target cardinality.
	 * 
	 * @param targetCardinality. The target cardinality of the association.
	 */
	public void setTargetCardinality(Cardinality targetCardinality);
	
	/**
	 * Return the association type.
	 * 
	 * @return gUFOType. The UFO type of the associaiton.
	 */
	public AssociationType getAssociationType();
	
	/**
	 * Checks if the current association is the same type as the association 
	 * passed as a parameter.
	 * 
	 * @param associationType. Association to be tested.
	 * @return boolean. Returns true if are the same, outerwise false.
	 */
	public boolean isAssociationType(AssociationType associationType);
	
	/**
	 * Clone the association by referencing the current nodes.
	 */
	public IOntoAssociation clone();
	
	/**
	 * Clone the association changing the associated nodes to the nodes in 
	 * the array. This method generally used to clone the node.
	 * 
	 * @param nodes. New nodes to be linked.
	 * @return Association
	 */
	public IOntoAssociation cloneChangingReferencesTo(ArrayList<INode> nodes);
	
	/**
	 * Remove the current association from the source and target node.
	 */
	public void removeAssociation();
	
	/**
	 * Checks whether the association refers to the node passed as a parameter.
	 * 
	 * @param node. Node to be analyzed.
	 * @return True if the node is referenced by the association, otherwise not.
	 */
	public boolean hasAssociationWithNode(INode node);
	
	/**
	 * Marks the association as visited or not.
	 * 
	 * @param flag. True indicates that the association was visited and false 
	 * as not visited.
	 */
	public void setResolved(boolean flag);

	/**
	 * Indicates whether the association has already been visited.
	 * 
	 * @return True if the association was visited, otherwise false.
	 */
	public boolean isResolved();
}
