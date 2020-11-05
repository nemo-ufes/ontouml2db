/**
 * A node is composed of a relationships set with other nodes. This interface contains the 
 * necessary methods for handling node associations.
 * 
 * Author: Gustavo L. Guidoni
 * 
 */
package br.ufes.inf.nemo.ontouml2db.graph;

import java.util.ArrayList;

public interface IAssociationContainer {
	
	/**
	 * Returns the associations with the node.
	 * 
	 * @return An ArrayList with all the associations that arrive and depart from the 
	 * respective node.
	 */
	public ArrayList<IOntoAssociation> getAssociations();
	
	/**
	 * Adds a new association to the node. Each node has a list of its associations. In 
	 * this way, a association is referenced by the origin and destination node, forming 
	 * a bidirectional graph.
	 * 
	 * @param IOntoAssociation
	 */
	public void addAssociation(IOntoAssociation association);
	
	/**
	 * Removes all connections present in the node, including removing the connection 
	 * from the destination node.
	 */
	public void removeAndDestroyAssociations();
	
	/**
	 * Removes only the association of the node (source node) that called the action. 
	 * The target node association does not change. Any association that has the node 
	 * passed as a parameter will be removed.
	 * 
	 * @param node with which the current node will no longer be associated.
	 */
	public void removeAssociationWith(INode node);
	
	/**
	 * Removes the association of the node (source node) that called the action, the 
	 * target node association does not change.
	 * 
	 * @param association that will be removed.
	 */
	public void removeAssociation( IOntoAssociation association);
	
	
	/**
	 * Checks if the generalization association passed as a parameter (superclass and 
	 * subclass) exists at the node.
	 * 
	 * @param generalization to be evaluated
	 * @return true if the generalization exists at the node, otherwise false.
	 */
	public boolean existsGeneralization( IOntoAssociation generalization );
	
	/**
	 * Returns the supernode that has the specializations.
	 * 
	 * @param nodes. Set of specialization nodes of the supernode.
	 * @return the supernode of the generalization set.
	 */
	public IOntoGeneralizationSet getGeneralizationSetWith(ArrayList<INode> nodes);
	
	/**
	 * Removes the generalization relationship that has the expert node passed as an 
	 * argument.
	 * 
	 * @param node. Specialist node of the generalization relationship to be removed.
	 */
	public void removeSpecializationWith(INode node);
	
	/**
	 * Checks whether the current node is a specialist node of some generalization. 
	 * Generalization sets are not considered in this method, that is, just simple 
	 * generalizations.
	 * 
	 * @return True if the node is a specialization of another node, otherwise false.
	 */
	public boolean isSpecialization();
	
	/**
	 * Checks whether the current node has any specialization, be it a simple 
	 * generalization or a generalization set.
	 * 
	 * @return True if the node has at last one specialization node, otherwise false.
	 */
	public boolean hasSpecialization( );
	
	/**
	 * Returns all associations belonging to the node. The returned ArrayList is not 
	 * part of the association's storage structure.
	 * 
	 * @param type. Type of association pertaining to the Node to be searched.
	 * @return Returns all associations of the given type.
	 */
	public ArrayList<IOntoAssociation> getAssociationsOfType(AssociationType type);
	
	/**
	 * Searches if the name of the informed node is part of any association (Association, 
	 * Generalization or Generalization Set) belonging to the current node. If yes, it 
	 * returns the Node, otherwise null.
	 * 
	 * @param nodeAssociated. Name of the node to be searched.
	 * @return The node instance if found, otherwise null.
	 */
	public INode getNodeAssociated(String nodeAssociated);
	
	/**
	 * Marks whether all associations were visited or not.
	 *  
	 * @param flag. True marks all associations visited, false as not visited.
	 */
	public void setResolved(boolean flag);
}
