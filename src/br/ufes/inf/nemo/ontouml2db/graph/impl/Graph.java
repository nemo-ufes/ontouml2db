package br.ufes.inf.nemo.ontouml2db.graph.impl;

/**
 * This class is responsible for keeping in memory the same graph found 
 * in the Turtle file.
 * 
 * Author: Gustavo L. Guidoni
 */

import java.util.ArrayList;

import br.ufes.inf.nemo.ontouml2db.graph.AssociationType;
import br.ufes.inf.nemo.ontouml2db.graph.IGraph;
import br.ufes.inf.nemo.ontouml2db.graph.INode;
import br.ufes.inf.nemo.ontouml2db.graph.IOntoAssociation;
import br.ufes.inf.nemo.ontouml2db.graph.IOntoGeneralization;
import br.ufes.inf.nemo.ontouml2db.graph.IOntoGeneralizationSet;
import br.ufes.inf.nemo.ontouml2db.graph.UfoStereotype;

public class Graph implements IGraph{

	private ArrayList<INode> nodes;
	private String graphName;
	
	public Graph(String graphName) {
		this.nodes = new ArrayList<INode>();
		this.graphName = graphName;
	}
	
	public Graph(ArrayList<INode> nodes, String graphName) {
		this.nodes = nodes;
		this.graphName = graphName;
	}
	
	
	public ArrayList<INode> getNodes(){
		return nodes;
	}
	
	public INode getNode(String name) {
		for( INode node : nodes) {
			if( node.getName().equalsIgnoreCase(name) ) {
				return node;
			}
		}
		return null;
	}
	
	public void addNode( INode newNode ) {
		if( !existsNode(newNode) ) {
			nodes.add( newNode );
		}
		else {
			System.out.println("[ERROR] The node "+ newNode.getName() + " already exists.");
		}
	}
	
	public boolean existsNode(INode newNode) {
		for(INode node : nodes){
			if( node.getName().equalsIgnoreCase(newNode.getName()) ) {
				return true;
			}
		}
		return false;
	}
	
	public void addAssociation( IOntoAssociation association ) {
		
		INode sourceNode = association.getSourceNode();
		INode targetNode = association.getTargetNode();
		
		sourceNode.addAssociation(association);
		targetNode.addAssociation(association);
	}
	
	public void addGeneralization( IOntoAssociation generalization ) {
		INode sourceNode = generalization.getSourceNode();
		INode targetNode = generalization.getTargetNode();
		
		sourceNode.addAssociation(generalization);
		targetNode.addAssociation(generalization);
	}
	
	public void addGeneralizationSet( IOntoGeneralizationSet generalizationSet ) {
		if (existsGeneralizationSet(generalizationSet)) return;
		
		INode generalizationNode = generalizationSet.getGeneralizationNode();
		
		generalizationNode.addAssociation(generalizationSet);
		
		for(INode node : generalizationSet.getSpecializationNodes()) {
			setLinkedGeneralizationSetOf(generalizationSet);
			generalizationNode.removeSpecializationWith(node);
		}
	}
	
	private void setLinkedGeneralizationSetOf(IOntoGeneralizationSet generalizationSet) {
		IOntoGeneralization generalization;
		
		for(INode node : generalizationSet.getSpecializationNodes()) {
			
			for(IOntoAssociation association : node.getAssociations()) {
				
				if(association.getAssociationType() == AssociationType.GENERALIZATION) {
					if( 	association.getSourceNode() == node ||
							association.getTargetNode() == node) {
						generalization = (IOntoGeneralization) association;
						generalization.setLinkedGeneralizationSet(generalizationSet);
					}
				}
			}
		}
	}
	
	public boolean existsGeneralizationSet(IOntoGeneralizationSet newGeneralizationSet) {
		INode generalizationNode = newGeneralizationSet.getGeneralizationNode();
		IOntoGeneralizationSet storedGS;
		
		
		for( IOntoAssociation association : generalizationNode.getAssociations() ) {// can have orthogonal generalization set
			if( association.getAssociationType() == AssociationType.GENERALIZATION_SET ) {
				storedGS = (IOntoGeneralizationSet) association;
				
				for( INode storedNode : storedGS.getSpecializationNodes() ) {
					for( INode avaliableNode : newGeneralizationSet.getSpecializationNodes() ) {
						if ( storedNode.getName().equalsIgnoreCase(avaliableNode.getName()) ) {
							//A node cannot exist in two generalization sets for the same parent node.
							return true;
						}
					}
				}
			}
		}
		return false;
	}
	
	public INode getToplevelNonSortal() {
		
		for(INode node : nodes) {
			if( 	UfoStereotype.isNonSortal(node.getStereotype()) &&
					!node.isSpecialization() &&
					node.hasSpecialization()//Allows the generation of a table with a "non-sortal" without heirs.
					) {
				return node;
			}
		}
		return null;
	}
	
	public IOntoAssociation getGeneralizationAssociatoinOfLeafSortalNonKind() {
		for( INode node : nodes ) {
			if ( 	UfoStereotype.isSortalNonKind(node.getStereotype()) && //is a subkind, phase or role
					!node.hasSpecialization() ){ //is a leaf node
					return getGenelizationAssociationOf(node);
			}
		}
		return null;
	}
	
	private IOntoAssociation getGenelizationAssociationOf(INode node) {
		for(IOntoAssociation association : node.getAssociations()) {
			if( 	(association.getAssociationType() == AssociationType.GENERALIZATION ||
					association.getAssociationType() == AssociationType.GENERALIZATION_SET) 
					&&
					(association.getSourceNode() == node ||
					association.getTargetNode() == node) ){
				return association;
			}
		}
		return null;
	}
	
	public INode getLeafSortalNonKind() {
		for( INode node : nodes ) {
			if ( 	UfoStereotype.isSortalNonKind(node.getStereotype()) && //is a subkind, phase or role
					! node.hasSpecialization() ) { //is a leaf node
				 	return node;
			}
		}
		return null;
	}
	
	public void removesNodeAndItsAssociations(INode node) {
		node.removeAndDestroyAssociations();
		removeNode(node);
	}
	
	public void removeNode(INode node) {
		int i = 0;
		INode obj;
		while ( i < nodes.size() ){
			obj = nodes.get(i);
			if( obj == node) {
				nodes.remove(i);
				return;
			}
			i++;
		}
	}
	
	public void removeAssociation(IOntoAssociation association) {
		if( association.getAssociationType() == AssociationType.GENERALIZATION_SET ) {
			IOntoGeneralizationSet gs = (IOntoGeneralizationSet)association;
			gs.getGeneralizationNode().removeAssociation(association);
			
			for( INode node : gs.getSpecializationNodes() ) {
				node.removeAssociation(association);
			}
		}
		else {
			association.getSourceNode().removeAssociation(association);
			association.getTargetNode().removeAssociation(association);
		}
	}
	
	@Override
	public Graph clone() {
		ArrayList<INode> newNodes = new ArrayList<INode>();
		ArrayList<IOntoAssociation> associations = getAssociations();
		
		for(INode node : nodes) {
 			newNodes.add(node.clone());
		}
		
		for (IOntoAssociation obj : associations) {
			obj.cloneChangingReferencesTo(newNodes);
		}
		
		return new Graph(newNodes, graphName);
	}
	
	public ArrayList<IOntoAssociation> getAssociations(){
		ArrayList<IOntoAssociation> associations = new ArrayList<IOntoAssociation>();
		
		for(INode node : nodes) {
			
			for(IOntoAssociation association : node.getAssociations()) {
				
				if( ! existsAssociation(association, associations)) {
					associations.add(association);
				}
			}
		}
		return associations;
	}
	
	
	private boolean existsAssociation(IOntoAssociation relation, ArrayList<IOntoAssociation> associations) {
		for(IOntoAssociation obj : associations) {
			if( obj == relation ) {
				return true;
			}
		}
		return false;
	}
	
	public void showNodes() {
		System.out.println("\n---- GRAPH");
		
		for (INode node : nodes) {
			System.out.println(node);
		}
	}
	
	public void showTracking() {
		System.out.println("\n---- TRACKING");
		for (INode node : nodes) {
			System.out.println( node.trackingToString() );
		}
	}

	public void setAllNodesUnsolved() {
		for(INode node : nodes) {
			node.setResolved(false);;
		}
	}

	public String getGraphName() {
		return graphName;
	}
}
