package br.ufes.inf.nemo.ontouml2db.graph.impl;


import java.util.ArrayList;

import br.ufes.inf.nemo.ontouml2db.graph.AssociationType;
import br.ufes.inf.nemo.ontouml2db.graph.Cardinality;
import br.ufes.inf.nemo.ontouml2db.graph.INode;
import br.ufes.inf.nemo.ontouml2db.graph.IOntoAssociation;
import br.ufes.inf.nemo.ontouml2db.util.Util;

public class OntoAssociation implements IOntoAssociation{
	
	private String name;
	private INode sourceNode;
	private INode targetNode;
	private boolean resolved;
	
	private Cardinality sourceCardinality;
	private Cardinality targetCardinality;
	
	private AssociationType associationType;
	
	/**
	 * Create a new Association for the association triple. The default association type is MEDIATION.
	 * 
	 * @param triple
	 * @param sourceCardinality
	 * @param targetCardinality
	 */
	public OntoAssociation(String name, INode sourceNode, Cardinality sourceCardinality, INode targetNode, Cardinality targetCardinality) {
		this.name = name;
		this.sourceNode = sourceNode;
		this.sourceCardinality = sourceCardinality;
		this.targetCardinality = targetCardinality;
		this.targetNode = targetNode;
		this.associationType = AssociationType.ASSOCIATION;
		this.resolved = false;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public AssociationType getAssociationType() {
		return associationType;
	}
	
	public boolean isAssociationType(AssociationType associationType) {
		return associationType == AssociationType.ASSOCIATION;
	}
	
	public void removeAssociation() {
		sourceNode.removeAssociationWith(targetNode);
		targetNode.removeAssociationWith(sourceNode);
	}
	
	public IOntoAssociation cloneChangingReferencesTo(ArrayList<INode> nodes) {
		INode source = Util.findNode(sourceNode.getName(), nodes);
		INode target = Util.findNode(targetNode.getName(), nodes);
		
		IOntoAssociation association = new OntoAssociation(
				name,
				source,
				sourceCardinality,
				target, 
				targetCardinality);
		
		source.addAssociation(association);
		target.addAssociation(association);
		
		return association;
	}
	
	public IOntoAssociation clone() {
		
		IOntoAssociation association = new OntoAssociation(name, sourceNode, sourceCardinality, targetNode, targetCardinality);
		
		association.setSourceNode(sourceNode);
		
		association.setTargetNode(targetNode);
		
		return association;
	}
	
	public void setSourceNode(INode sourceNode) {
		this.sourceNode = sourceNode;
	}
	
	public INode getSourceNode() {
		return sourceNode;
	}
	
	public void setTargetNode(INode targetNode) {
		this.targetNode = targetNode;
	}
	
	public INode getTargetNode() {
		return targetNode;
	}
	
	public Cardinality getSourceCardinality() {
		return sourceCardinality;
	}

	public void setSourceCardinality(Cardinality sourceCardinality) {
		this.sourceCardinality = sourceCardinality;
	}
	
	public Cardinality getTargetCardinality() {
		return targetCardinality;
	}
	
	public void setTargetCardinality(Cardinality targetCardinality) {
		this.targetCardinality = targetCardinality;
	}
	
	public boolean hasAssociationWithNode(INode node) {
		return sourceNode == node || targetNode == node;
	}
	
	public String toString() {
		return "\r\t : " + sourceNode.getName() +"(" + sourceCardinality +") - " + targetNode.getName() + "(" + targetCardinality + ")";
	}

	public void setResolved(boolean flag) {
		resolved = flag;
	}
	
	public boolean isResolved() {
		return resolved;
	}
	
}