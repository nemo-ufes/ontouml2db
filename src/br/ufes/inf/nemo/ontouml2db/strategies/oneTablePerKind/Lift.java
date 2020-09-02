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
import br.ufes.inf.nemo.ontouml2db.graph.IEnumeration;
import br.ufes.inf.nemo.ontouml2db.graph.IGraph;
import br.ufes.inf.nemo.ontouml2db.graph.INode;
import br.ufes.inf.nemo.ontouml2db.graph.IOntoAssociation;
import br.ufes.inf.nemo.ontouml2db.graph.IOntoGeneralization;
import br.ufes.inf.nemo.ontouml2db.graph.IOntoGeneralizationSet;
import br.ufes.inf.nemo.ontouml2db.graph.IOntoProperty;
import br.ufes.inf.nemo.ontouml2db.graph.impl.Enumeration;
import br.ufes.inf.nemo.ontouml2db.graph.impl.Node;
import br.ufes.inf.nemo.ontouml2db.graph.impl.OntoAssociation;
import br.ufes.inf.nemo.ontouml2db.graph.impl.OntoProperty;

public class Lift {
	
	public static int num;
	
	private static int getNextNum() {
		return num++;
	}
	
	public static void doLifting(IGraph graph) {
		num = 1;
		
		INode node= graph.getLeafSortalNonKind();

		while (node != null) {
			liftNode(node, graph);
			graph.removeNode( node );
			node = graph.getLeafSortalNonKind();
		}
	}
	
	private static void liftNode(INode node, IGraph graph) {
		
		resolveGeneralization( node );
		
		resolveGeneralizationSet( node, graph );
		
		liftAtributes( node );
		
		remakeReference( node, graph );
	}

	private static void resolveGeneralization( INode node) {
		IOntoProperty newProperty;
		node.setResolved(true);
		//here, can have only one generaization
		IOntoGeneralization generalization = (IOntoGeneralization) node.getAssociationsOfType(AssociationType.GENERALIZATION).get(0); //getGeneralization(node);
		
		//Generalization Sets are resolved by "resolveGeneralizatinSet
		if(generalization.isLInkedToGeneralizationSet())
			return;
		
		//create a boolean for the specialization
		newProperty = new OntoProperty(
				"is" + generalization.getTargetNode().getName(), 
				"boolean",
				false,
				false);
		newProperty.setDefaultValue(false);
		
		//The new property is put in the generalizaiton node node by liftAttribute method.
		node.addProperty( newProperty );
		
		node.setSourceTrackerField(newProperty, true);
	}
	
	//must be called after creating all attributes on the specialization nodes.
	private static void liftAtributes(INode node) {
		//here, can have only one generaization
		IOntoGeneralization generalization = (IOntoGeneralization) node.getAssociationsOfType(AssociationType.GENERALIZATION).get(0); //getGeneralization(node);
		
		if(generalization == null) 
			return;
		
		ArrayList<IOntoProperty> prop = generalization.getTargetNode().getProperties();
		
		for(IOntoProperty property : prop) {
			if(property.getDefaultValue() == null)//Does not change nullability for columns with default values (eg is_employee default false)
				property.setNullable(true);
		}
		generalization.getSourceNode().addProperty(prop);
	}
	
	private static void resolveGeneralizationSet( INode node, IGraph graph ) {
		IOntoGeneralizationSet gs = null;
		String associationName;
		String enumFieldName;
		String enumTableName;
		IEnumeration newEnumeration;
		INode newNode;
		IOntoAssociation newAssociation;
		IOntoAssociation generalization;
		INode superNode;
		ArrayList<IOntoAssociation> associations;
		//ArrayList<IOntoAssociation> generalizations;
		
		//generalizations = node.getAssociationsOfType(AssociationType.GENERALIZATION);
		//if( generalizations.isEmpty() )
		//	return;
		generalization = node.getAssociationsOfType(AssociationType.GENERALIZATION).get(0);
		superNode = generalization.getSourceNode();
		associations = superNode.getAssociationsOfType(AssociationType.GENERALIZATION_SET);
		
		for(IOntoAssociation association : associations) {
			gs = (IOntoGeneralizationSet) association;
			
			//The Generalization Set is resolved as soon as it is identified and marked as resolved. This is  
			//necessary because the "lifting" process will call the other subclasses to resolve their attributes 
			//and associations, not being able to repeat the process of solving the generalization set.
			if( ! gs.isResolved() ) {
			
				associationName = "enum_" + getNextNum();
				
				enumTableName = getEnumName(gs);
				enumFieldName = enumTableName + "Enum" ;
				
				newNode = superNode.getNodeAssociated(enumTableName);
				
				if(newNode == null) {
					newEnumeration = new Enumeration(enumFieldName, "INT", false, false );
					newNode = new Node( enumTableName, "enumeration" );
					newNode.addProperty(newEnumeration);
					
					newAssociation = new OntoAssociation(
							associationName, 
							newNode, 
							getNewSourceCardinality(gs),
							superNode,  
							Cardinality.C0_N);
					
					superNode.addAssociation(newAssociation);
					newNode.addAssociation(newAssociation);
					
					graph.addNode(newNode);
				}
				else {
					newEnumeration = (IEnumeration) newNode.getProperty(enumFieldName);
				}
				
				for( INode specializationNode : gs.getSpecializationNodes() ) {
					newEnumeration.addValue(specializationNode.getName());
					specializationNode.setSourceTrackerField( newEnumeration, specializationNode.getName());
					specializationNode.setSourcePropertyLinkedAtNode(newNode);
				}
				gs.setResolved(true);
			}
				
			if( isSpecializationResolved(gs) ) {
				superNode.removeAssociation(gs);
			}
		}
	}
	
	private static Cardinality getNewSourceCardinality(IOntoGeneralizationSet gs) {
		
		if( gs.isDisjoint() && gs.isCovering() ) {
			return Cardinality.C1;
		}
		else if( gs.isDisjoint() && !gs.isCovering() ) {
			return Cardinality.C0_1;
		}
		else if( !gs.isDisjoint() && gs.isCovering() ) {
			return Cardinality.C1_N;
		}
		else if( !gs.isDisjoint() && !gs.isCovering() ) {
			return Cardinality.C0_N;
		}
		return null;
	}
	
	private static String getEnumName(IOntoGeneralizationSet gs) {
		String name;
		
		if(gs.getName() == null || gs.getName().trim().equals(""))
			name = "Enum"+ getNextNum();
		else name = gs.getName();
		
		return name;
	}
	
	private static void remakeReference( INode node, IGraph graph) {
		//here, can have only one generalization
		IOntoGeneralization generalization = (IOntoGeneralization) node.getAssociationsOfType(AssociationType.GENERALIZATION).get(0);
		
		//if( ! generalization.isLInkedToGeneralizationSet() )
		//	remakeReferenceGeneralization(node, graph);
		//else remakeReferenceGeneralizationSet(node, graph);
		
		INode superNode = generalization.getSourceNode();
		
		for(IOntoAssociation association : node.getAssociationsOfType(AssociationType.ASSOCIATION)) {
			if( association.getSourceNode() == node ) {
				association.setSourceNode(superNode);
				association.setTargetCardinality( getNewCardinality( association.getTargetCardinality() ) );
			}
			else {
				association.setTargetNode(superNode);
				association.setSourceCardinality( getNewCardinality( association.getSourceCardinality() ) );
			}
			superNode.addAssociation(association);
			node.removeAssociation(association);
		}
		
		node.removeAssociation(generalization);
		superNode.removeAssociation(generalization);
		
		node.changeSourceTracking(superNode);
		
	}
	
	private static Cardinality getNewCardinality(Cardinality oldCardinality) {
		if( oldCardinality == Cardinality.C1_N ) {
			return Cardinality.C0_N;
		}
		else if( oldCardinality == Cardinality.C1 ) {
			return Cardinality.C0_1;
		}
		else return oldCardinality;
	}
	
	private static boolean isSpecializationResolved(IOntoGeneralizationSet gs) {
		for(INode node : gs.getSpecializationNodes()) {
			if( !node.isResolved()) {
				return false;
			}
		}
		return true;
	}
	
}

