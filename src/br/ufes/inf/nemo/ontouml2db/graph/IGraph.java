/**
 * The process turns the model of the UML file into a graph. This interface 
 * groups all the methods necessary to manipulate the graph.
 * 
 * Author: Gustavo L. Guidoni
 */
package br.ufes.inf.nemo.ontouml2db.graph;

import java.util.ArrayList;

import br.ufes.inf.nemo.ontouml2db.graph.impl.Graph;

public interface IGraph {
	
	/**
	 * Returns the name of the graph. The name of the UML file is assigned 
	 * as the name of the graph.
	 * 
	 * @return A string with the graph name.
	 */
	public String getGraphName();
	
	/**
	 * Returns all nodes of the graph.
	 * 
	 * @return An ArrayList with all nodes.
	 */
	public ArrayList<INode> getNodes();
	
	/**
	 * Returns the node instance.
	 * 
	 * @param name. The name of the node (class) to be searched.
	 * @return INode with the same name passed as argument. If not find, 
	 * returns null. 
	 */
	public INode getNode(String name);
	
	/**
	 * Adds a new node (class) on the graph.
	 * 
	 * @param INode. Node to be stored.
	 */
	public void addNode( INode newNode );
	
	/**
	 * Verifies whether the node exists on the graph.
	 * 
	 * @param node. Node to be verified.
	 * @return True if the node exists in the graph, otherwise false.
	 */
	public boolean existsNode(INode node);
	
	/**
	 * Adds a new Association on the graph.
	 * 
	 * @param association. The association to be stored.
	 */
	public void addAssociation( IOntoAssociation association );
	
	/**
	 * Adds a new generalization on the graph.
	 * 
	 * @param generalization. The generalization to be stored.
	 */
	public void addGeneralization( IOntoAssociation generalization );
	
	/**
	 * Adds a Generalization Set. If there is a generalization set in the 
	 * supernode that has any of the specialization nodes in the generalization 
	 * set passed as argument, the subnodes of the generalization set passed as 
	 * argument will be added to the existing generalization set of the supernode.
	 * 
	 * @param generalizationSet. The generalization set to be stored.
	 */
	public void addGeneralizationSet( IOntoGeneralizationSet generalizationSet );
	
	/**
	 * Checks whether the generalization set exists on the graph.
	 * 
	 * @param newGeneralizationSet. Generalization set to be checked.
	 * @return True if the generalization set exists on the graph, otherwise false.
	 */
	public boolean existsGeneralizationSet(IOntoGeneralizationSet newGeneralizationSet);
	
	/**
	 * Returns a top-level non-sortal in a package and remove it from array.
	 * 
	 * @return An INode with the top-level non-sortal from the graph, or null 
	 * if none can be found
	 */
	public INode getToplevelNonSortal();
	
	/**
	 * Returns the first generalization association of a leaf node sortal non-kind that find.
	 * 
	 * @return An IOntoAssociation with the generalization of some leaf node (the first that find).
	 */
	public IOntoAssociation getGeneralizationAssociatoinOfLeafSortalNonKind();
	
	/**
	 * Returns the first node that it finds to be non-kind.
	 * 
	 * @return An INode non-kind.
	 */
	public INode getLeafSortalNonKind();
	
	/**
	 * Removes an node and its associations of the graph.
	 * 
	 * @param node. Node to be removed.
	 */
	public void removesNodeAndItsAssociations(INode node);
	
	/**
	 * Removes the node of the graph. The properties and associations continue to reference it.
	 * 
	 * @param node. Node to be removed.
	 */
	public void removeNode(INode node);
	
	/**
	 * Removes the association of the graph. The association will be removed from the nodes. 
	 * 
	 * @param association
	 */
	public void removeAssociation(IOntoAssociation association);
	
	/**
	 * Clone the graph by establishing a reference between the nodes of the current 
	 * graph for the cloned graph.
	 * 
	 * @return An graph identical to the current graph.
	 */
	public Graph clone();
	
	/**
	 * Returns all associations of the graph.
	 * 
	 * @return An ArrayList with all associations.
	 */
	public ArrayList<IOntoAssociation> getAssociations();
	
	/**
	 * Show the graph nodes.
	 */
	public void showNodes();
	
	/**
	 * Show the tracking nodes of the graph.
	 */
	public void showTracking();
	
	/**
	 * Marks all nodes as unsolved.
	 */
	public void setAllNodesUnsolved();

}
