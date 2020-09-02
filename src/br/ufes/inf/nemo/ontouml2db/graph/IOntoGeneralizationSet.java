/**
 * Groups the actions needed to handle a generalization set.
 * 
 * Author: Gustavo L. Guidoni
 */
package br.ufes.inf.nemo.ontouml2db.graph;

import java.util.ArrayList;

public interface IOntoGeneralizationSet extends IOntoAssociation{

	/**
	 * Returns the generalization set supernode.
	 * 
	 * @return INode with the supernode.
	 */
	public INode getGeneralizationNode();
	
	/**
	 * Returns the specializations linked to the genrealization set.
	 * 
	 * @return An ArrayList with all the specialization nodes.
	 */
	public ArrayList<INode> getSpecializationNodes();
	
	/**
	 * Checks whether the informed node is among the specializations 
	 * of the generalization set.
	 * 
	 * @param nodeSpecialization. Node to be checked.
	 * @return True if the node is a specialization of the generalization 
	 * set, otherwise false.
	 */
	public boolean existsSpecializationOf(INode nodeSpecialization);
	
	/**
	 * Checks whether the generalization set is disjoint.
	 * 
	 * @return True if the generalization set is disjoint and false if it 
	 * is overlapping.
	 */
	public boolean isDisjoint();
	
	/**
	 * Checks whether the generalization set is classified as incomplete.
	 * 
	 * @return True if the generalization set is classified as complete 
	 * and false if it is incomplete.
	 */
	public boolean isCovering();
}
