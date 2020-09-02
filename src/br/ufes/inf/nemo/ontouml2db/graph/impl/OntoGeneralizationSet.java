package br.ufes.inf.nemo.ontouml2db.graph.impl;


import java.util.ArrayList;

import br.ufes.inf.nemo.ontouml2db.graph.AssociationType;
import br.ufes.inf.nemo.ontouml2db.graph.Cardinality;
import br.ufes.inf.nemo.ontouml2db.graph.INode;
import br.ufes.inf.nemo.ontouml2db.graph.IOntoGeneralizationSet;
import br.ufes.inf.nemo.ontouml2db.util.Util;

public class OntoGeneralizationSet implements IOntoGeneralizationSet{
	
	private String name;
	private INode generalizationNode;
	private ArrayList<INode> specializationNodes;
	private boolean disjoint;
	private boolean covering;
	private boolean resolved;
	
	public OntoGeneralizationSet(String name, INode generalizationNode, boolean disjoint, boolean covering) {
		this.name = name;
		this.disjoint = disjoint;
		this.covering = covering;
		this.generalizationNode = generalizationNode;
		specializationNodes = new ArrayList<INode>();
		resolved = false;
	}
	
	private OntoGeneralizationSet(String name, INode generalizationNode, ArrayList<INode> specializationNodes, boolean disjoint, boolean covering) {
		this(name, generalizationNode, disjoint, covering);
		this.specializationNodes = specializationNodes;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public boolean isDisjoint() {
		return disjoint;
	}
	
	public boolean isCovering() {
		return covering;
	}
	
	public AssociationType getAssociationType() {
		return AssociationType.GENERALIZATION_SET;
	}
	
	public boolean isAssociationType(AssociationType associationType) {
		return associationType == AssociationType.GENERALIZATION_SET;
	}
	
	public void setSourceNode(INode sourceNode) {
		this.generalizationNode = sourceNode;
	}
	
	/**
	 * Returns the superclass Node.
	 * 
	 * @return Node
	 */
	public INode getGeneralizationNode() {
		return getSourceNode();
	}
	
	/**
	 * Returns the superclass Node.
	 * 
	 * @return Node
	 */
	public INode getSourceNode() {
		return generalizationNode;
	}
	
	public INode getTargetNode() {
		return null;
	}
	
	public ArrayList<INode> getSpecializationNodes(){
		return specializationNodes;
	}
	
	/**
	 * Adds a specialization node if it does not exist in the generalization 
	 * set.
	 * 
	 * @param specialization
	 */
	public void addSpecializationIfNotExists(INode specialization) {
		if( !existsSpecializationOf(specialization)) {
			specializationNodes.add(specialization);
		}
	}
	
	/**
	 * Identifies if the node passed as argument is present in its 
	 * specializations.
	 * 
	 * @return boolean
	 */
	public boolean existsSpecializationOf(INode nodeSpecialization) {
		for (INode node : specializationNodes) {
			if( node == nodeSpecialization)
				return true;
		}
		return false;
	}
	
	public boolean hasAssociationWithNode(INode node) {
		return generalizationNode == node || existsSpecializationOf( node );
	}

	public void setTargetNode(INode targetNode) {
		System.out.println("[ERROR] This method must not be called for the Generalization Set. See addSpecialization.");
	}
	
	public void removeAssociation() {
		INode node;
		while( !specializationNodes.isEmpty() ) {
			node = specializationNodes.get(0);
			specializationNodes.remove(0);
			node.removeAssociationWith(generalizationNode);
		}
		generalizationNode.removeAssociation(this);
		
		
	}

	public Cardinality getSourceCardinality() {
		System.out.println("Generalization has no source cardinality!!!!");
		return null;
	}

	public Cardinality getTargetCardinality() {
		System.out.println("Generalization has no target cardinality!!!!");
		return null;
	}

	public void setSourceCardinality(Cardinality sourceCardinality) {
		System.out.println("Generalization has no source cardinality!!!!");
	}

	public void setTargetCardinality(Cardinality targetCardinality) {
		System.out.println("Generalization has no target cardinality!!!!");
	}
	
	public IOntoGeneralizationSet cloneChangingReferencesTo( ArrayList<INode> newNodes ) {
		
		IOntoGeneralizationSet gs;
		ArrayList<INode> newTargetNodes = new ArrayList<INode>();
		INode newSourceNode;
		
		for(INode node : specializationNodes) {
			newTargetNodes.add( Util.findNode(node.getName(), newNodes) );
		}
		
		newSourceNode = Util.findNode(generalizationNode.getName(), newNodes);
		
		gs =  new OntoGeneralizationSet(name, newSourceNode, newTargetNodes, disjoint, covering);
		
		newSourceNode.addAssociation(gs);
		
		return gs;
	}
	
	public IOntoGeneralizationSet clone() {
		return null;
	}
	
	public String toString() {
		String aux = "(";
		if( disjoint ) 
			aux += "D";
		else aux += "O";
		
		if( covering )
			aux += "|C)";
		else aux += "|I)";
		
		String msg = "\n\t : " + generalizationNode.getName() + " <-GS"+aux+"- ";
		for (INode node : specializationNodes) {
			msg += node.getName() + " | ";
		}
		return msg;
	}

	public boolean isResolved() {
		return resolved;
	}

	public void setResolved(boolean flag) {
		resolved = flag;
	}
	
}

