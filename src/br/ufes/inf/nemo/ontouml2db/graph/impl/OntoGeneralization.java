package br.ufes.inf.nemo.ontouml2db.graph.impl;

/**
 * Class responsible for storing generalization association data.
 * 
 * Author: Gustavo L. Guidoni
 */

import java.util.ArrayList;

import br.ufes.inf.nemo.ontouml2db.graph.AssociationType;
import br.ufes.inf.nemo.ontouml2db.graph.Cardinality;
import br.ufes.inf.nemo.ontouml2db.graph.INode;
import br.ufes.inf.nemo.ontouml2db.graph.IOntoAssociation;
import br.ufes.inf.nemo.ontouml2db.graph.IOntoGeneralization;
import br.ufes.inf.nemo.ontouml2db.graph.IOntoGeneralizationSet;
import br.ufes.inf.nemo.ontouml2db.util.Util;

public class OntoGeneralization implements IOntoGeneralization{
	
	private String name;
	private INode generalizationNode;
	private INode specializationNode;
	private IOntoGeneralizationSet linkedGeneralizationSet;
	private boolean resolved;
	
	public OntoGeneralization(String name, INode generalizationNode, INode specializationNode, IOntoGeneralizationSet linkedGeneralizationSet ) {
		this.name = name;
		this.generalizationNode = generalizationNode;
		this.specializationNode = specializationNode;
		this.linkedGeneralizationSet = linkedGeneralizationSet;
		this.resolved = false;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public AssociationType getAssociationType() {
		return AssociationType.GENERALIZATION;
	}
	
	public boolean isAssociationType(AssociationType associationType) {
		return associationType == AssociationType.GENERALIZATION;
	}	
	
	public IOntoAssociation cloneChangingReferencesTo(ArrayList<INode> nodes) {
		
		INode newGeneralizationNode;
		INode newSpecializationNode;
		IOntoGeneralizationSet gs;
		ArrayList<INode> array = new ArrayList<INode>();
		
		
		newGeneralizationNode = Util.findNode( generalizationNode.getName(), nodes);
		newSpecializationNode = Util.findNode( specializationNode.getName(), nodes);
		
		array.add(newSpecializationNode);
		
		gs = newGeneralizationNode.getGeneralizationSetWith(array);
		
		IOntoAssociation generalization = new OntoGeneralization(getName(), newGeneralizationNode, newSpecializationNode, gs);
		
		if( generalizationNode.existsGeneralization( generalization ) ) {
			newGeneralizationNode.addAssociation(generalization);
		}
		
		if ( specializationNode.existsGeneralization( generalization ) ) {
			newSpecializationNode.addAssociation(generalization);
		}
		
		return generalization;
	}
	
	public void removeAssociation() {
		generalizationNode.removeAssociationWith(specializationNode);
		specializationNode.removeAssociationWith(generalizationNode);
		generalizationNode = null;
		specializationNode = null;
	}
	
	public boolean isTheSameThat(IOntoAssociation association) {
		if(association.getAssociationType() != AssociationType.GENERALIZATION) {
			return false;
		}
		
		INode gen;
		INode spe;
		
		gen = association.getSourceNode();
		spe = association.getTargetNode();
		if ( 	generalizationNode.getName().equalsIgnoreCase(gen.getName()) &&
				specializationNode.getName().equalsIgnoreCase(spe.getName())
			){
			return true;
		}
		return false;
	}
	
	public void setSourceNode(INode sourceNode) {
		this.generalizationNode = sourceNode;		
	}

	/**
	 * Indicates the subclass Node.
	 * 
	 * @param specializatinNode
	 */
	public void setTargetNode(INode targetNode) {
		this.specializationNode = targetNode;
	}
	
	/**
	 * Returns the generalization Node.
	 *   
	 * @return Node
	 */
	public INode getSourceNode(){
		return generalizationNode;
	}
	
	/**
	 * Returns the generalization Node.
	 *   
	 * @return Node
	 */
	public INode getGeneralizationNode(){
		return getSourceNode();
	}
	
	/**
	 * Returns the specialization Node.
	 * 
	 * @return Node
	 */
	public INode getTargetNode() {
		return specializationNode;
	}
	
	/**
	 * Returns the specialization Node.
	 * 
	 * @return Node
	 */
	public INode getSpecializationNode() {
		return getTargetNode();
	}
	
	/**
	 * Returns true if the node passed as an argument belongs to the 
	 * generalization set.
	 */
	public boolean isSpecialization(INode node) {
		return specializationNode == node;
	}
	
	public boolean hasAssociationWithNode(INode node) {
		return generalizationNode == node || specializationNode == node;
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

	public String toString() {
		String gs = "";
		if(linkedGeneralizationSet != null) {
			gs = " (GS: " + linkedGeneralizationSet.getName() + ")";
		}
		
		return "\r\t : " + generalizationNode.getName() + " <- " + specializationNode.getName() +  gs;
	}

	public void setLinkedGeneralizationSet(IOntoGeneralizationSet gs) {
		linkedGeneralizationSet = gs;
	}

	public IOntoGeneralizationSet getLinkedGeneralizationSet() {
		return linkedGeneralizationSet;
	}
	
	public boolean isLInkedToGeneralizationSet() {
		return linkedGeneralizationSet != null; //if different of null, is linked to a generalization set
	}
	
	public IOntoGeneralization clone() {
		IOntoGeneralization generalization = new OntoGeneralization(name, generalizationNode, specializationNode, linkedGeneralizationSet);
		generalization.setSourceNode(generalizationNode);
		generalization.setTargetNode(specializationNode);
		return generalization;
	}

	public void setResolved(boolean flag) {
		resolved = flag;
	}

	public boolean isResolved() {
		return resolved;
	}
}

