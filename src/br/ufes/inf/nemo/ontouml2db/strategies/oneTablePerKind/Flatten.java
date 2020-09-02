/**
 * 
 * 
 * Author: João Paulo A. Almeida; Gustavo L. Guidoni
 * 
 */

package br.ufes.inf.nemo.ontouml2db.strategies.oneTablePerKind;

import java.util.ArrayList;

import br.ufes.inf.nemo.ontouml2db.graph.AssociationType;
import br.ufes.inf.nemo.ontouml2db.graph.Cardinality;
import br.ufes.inf.nemo.ontouml2db.graph.IGraph;
import br.ufes.inf.nemo.ontouml2db.graph.INode;
import br.ufes.inf.nemo.ontouml2db.graph.IOntoAssociation;
import br.ufes.inf.nemo.ontouml2db.graph.IOntoProperty;
import br.ufes.inf.nemo.ontouml2db.graph.impl.OntoAssociation;
import br.ufes.inf.nemo.ontouml2db.graph.impl.OntoGeneralization;
import br.ufes.inf.nemo.ontouml2db.graph.impl.OntoGeneralizationSet;

public class Flatten {
	

	public static void doFlattening(IGraph graph) {
		INode node;
		
		// flattens all top-level non-sortals
		node = graph.getToplevelNonSortal();
		while (node != null) {
			flattenNode(node);
			graph.removesNodeAndItsAssociations(node);
			graph.removeNode(node);
			node = graph.getToplevelNonSortal();
			
		}
	}
	
	private static void flattenNode(INode node) {
		
		for(IOntoAssociation association : node.getAssociations()) {
			
			if( association.getAssociationType() == AssociationType.GENERALIZATION ) {
				flattenGeneralization( (OntoGeneralization)association );
			}
			
			if( association.getAssociationType() == AssociationType.GENERALIZATION_SET ) {
				flattenGeneralizationSet( (OntoGeneralizationSet)association );
			}
			
			if( association.getAssociationType() == AssociationType.ASSOCIATION ) {
				flattenAssociation(node, (OntoAssociation)association );
			}
		}
	}
	
	private static void flattenGeneralization(OntoGeneralization generalization){
		
		generalization.getSpecializationNode().addPropertyAt(
				0,
				generalization.getGeneralizationNode().getProperties()
				);
		
		//for tracking between graphs
		generalization.getGeneralizationNode().addSourceTracking( generalization.getSpecializationNode() );
		generalization.getGeneralizationNode().removeSourceTracking( );
	}
	
	private static void flattenGeneralizationSet(OntoGeneralizationSet generalizationSet) {
		
		ArrayList<IOntoProperty> properties = generalizationSet.getGeneralizationNode().getProperties();
		
		for(INode specializationNode : generalizationSet.getSpecializationNodes()) {
			specializationNode.addPropertyAt(0, properties);
			generalizationSet.getGeneralizationNode().addSourceTracking(specializationNode);
			specializationNode.addTracking(generalizationSet.getGeneralizationNode().getTrackers());
		}
		generalizationSet.getGeneralizationNode().removeSourceTracking( );
	}
	
	private static void flattenAssociation(INode flattenNode, OntoAssociation association ) {
		
		if( association.getSourceNode() == flattenNode )
			association.setSourceCardinality( getNewCardinality( association.getSourceCardinality() ) );
		else association.setTargetCardinality( getNewCardinality( association.getTargetCardinality() ) );
		
		for(IOntoAssociation associationFlatten : flattenNode.getAssociations()) {
			//flatten to all generalizations.
			if( associationFlatten.getAssociationType() == AssociationType.GENERALIZATION ) {
				flattenAssociationWith( flattenNode, ((OntoGeneralization)associationFlatten).getSpecializationNode(), association );
			}
			
			//faltten to all generalizations sets.
			if( associationFlatten.getAssociationType() == AssociationType.GENERALIZATION_SET ) {
				flattenAssociationWith( flattenNode, ((OntoGeneralizationSet)associationFlatten).getSpecializationNodes(), association );
			}	
		}
	}
	
	private static Cardinality getNewCardinality(Cardinality oldCardinality) {
		if( oldCardinality == Cardinality.C1 ) {
			return Cardinality.C0_1;
		}
		else if( oldCardinality == Cardinality.C1_N ) {
			return Cardinality.C0_N;
		}
		else return oldCardinality; 
		
	}
	
	private static void flattenAssociationWith (INode flattenNode, INode toNode, IOntoAssociation association ) {
		IOntoAssociation newAssociation = association.clone();
		//newAssociation.setMadeByFlatten(true);
		newAssociation.setName( flattenNode.getName().trim() );
		
		if( newAssociation.getSourceNode() == flattenNode ) {
			newAssociation.setSourceNode(toNode);
			newAssociation.getTargetNode().addAssociation(newAssociation);
			newAssociation.getTargetNode().removeAssociation(association);
		}
		else {
			newAssociation.setTargetNode(toNode);
			newAssociation.getSourceNode().addAssociation(newAssociation);
			newAssociation.getSourceNode().removeAssociation(association);
		}		
		toNode.addAssociation(newAssociation);
	}
	
	private static void flattenAssociationWith(INode flattenNode, ArrayList<INode> targetNodes, OntoAssociation association) {
		IOntoAssociation newAssociation;
		for(INode obj : targetNodes) {
			
			newAssociation = association.clone();
			
			//Not remove the falttenNode association.
			if( association.getSourceNode() == flattenNode ) {
				association.getTargetNode().removeAssociation(association);
				association.getTargetNode().addAssociation(newAssociation);
			}
			else {
				association.getSourceNode().removeAssociation(association);
				association.getSourceNode().addAssociation(newAssociation);
			}
			
			flattenAssociationWith(flattenNode, obj, newAssociation);
		}
	}
}
