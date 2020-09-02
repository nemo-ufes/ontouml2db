/**
 * Groups the actions needed to handle a generalization.
 * 
 * Author: Gustavo L. Guidoni
 */
package br.ufes.inf.nemo.ontouml2db.graph;

public interface IOntoGeneralization extends IOntoAssociation{
	
	/**
	 * Tells which generalization set the current generalization is associated with.
	 * 
	 * @param gs. The generalization set to be associated.
	 */
	public void setLinkedGeneralizationSet(IOntoGeneralizationSet gs);
	
	/**
	 * Returns the associated generalization set.
	 * 
	 * @return Returns the reference to the associated OntoGeneralizationSet.
	 */
	public IOntoGeneralizationSet getLinkedGeneralizationSet();
	
	/**
	 * Reports whether the generalization is associated with a generalization set.
	 * 
	 * @return True if it is associated with a generalization set, otherwise false.
	 */
	public boolean isLInkedToGeneralizationSet();

	/**
	 * Checks whether the current generalization generalized and specialized the same 
	 * nodes as the generalization passed as a parameter. The validation is performed 
	 * by the name of the nodes.
	 * 
	 * @param generalization. The generalization with which the current generalization 
	 * will be evaluated.
	 * 
	 * @return boolean. True if it has the same nodes, otherwise false.
	 */
	public boolean isTheSameThat(IOntoAssociation association);
}
