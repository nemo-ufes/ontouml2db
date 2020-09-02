package br.ufes.inf.nemo.ontouml2db.casting;

import java.util.ArrayList;

import br.ufes.inf.nemo.ontouml2db.graph.Cardinality;
import br.ufes.inf.nemo.ontouml2db.graph.IEnumeration;
import br.ufes.inf.nemo.ontouml2db.graph.IGraph;
import br.ufes.inf.nemo.ontouml2db.graph.INode;
import br.ufes.inf.nemo.ontouml2db.graph.IOntoAssociation;
import br.ufes.inf.nemo.ontouml2db.graph.IOntoProperty;
import br.ufes.inf.nemo.ontouml2db.graph.impl.OntoProperty;
import br.ufes.inf.nemo.ontouml2db.util.Util;

public class ToEntityRelationship {
	
	public static void run(IGraph targetGraph) {
		
		transformEnumerations(targetGraph);
		
		//resolveMultivaluedProperties(targetGraph);
		//resolveCardinalityNtoN( targetGraph );
		
		putPrimaryKey( targetGraph );
		
		putForeignKey( targetGraph );
		
		adjustNames(targetGraph);
	}
	
	private static void transformEnumerations(IGraph targetGraph) {
		ArrayList<INode> nodesToDestroy = new ArrayList<INode>();
		ArrayList<IOntoAssociation> associationsToRemove = new ArrayList<IOntoAssociation>();
		
		for(INode node : targetGraph.getNodes()) {
			if( node.getStereotype().equalsIgnoreCase("enumeration") ) {
				
				associationsToRemove.clear();
				for(IOntoAssociation association : node.getAssociations()) {
					
					if( Util.isLowCardinalityOfNode(node, association) ) {
						//Transforms the enumeration into a column.
						addEnumerationColumn(node, association);
						nodesToDestroy.add(node);
						associationsToRemove.add(association);

						//node.removeSourceTracking( node );
						//node.addSourceTracking(getTargetNode(node, association) );
					}
					else if( Util.isHighCardinalityOfNode(node, association) ) {
						// Transforms the enumeration into a table (N:1).
						// This table now represents the intermediate table of the N:N 
						//relationship. Cardinalide 1 on the side of this new table is 
						//in the enumeration within the table.
						node.setStereotype("table");
						if(association.getSourceNode() == node)
							association.setTargetCardinality(Cardinality.C1);
						else association.setSourceCardinality(Cardinality.C1);
					}
				}
				Util.removeAssociations(node, associationsToRemove);
				
			}
		}
		Util.destroyNodes(nodesToDestroy, targetGraph);
	}
	
	private static void addEnumerationColumn(INode enumNode, IOntoAssociation association) {
		INode targetNode;
		Cardinality cardinalityOfEnum;
		boolean isNull;
		boolean isMultivalued;
		
		targetNode = getTargetNode(enumNode, association);
		cardinalityOfEnum = getCardinalityOf(enumNode, association);
		
		if(cardinalityOfEnum == Cardinality.C0_1 || cardinalityOfEnum == Cardinality.C1) 
			isMultivalued = false;
		else isMultivalued = true;
		
		if(cardinalityOfEnum == Cardinality.C0_1 || cardinalityOfEnum == Cardinality.C0_N) 
			isNull = true;//accept null
		else isNull = false;//not accept null		
		
		
		for(IOntoProperty property : enumNode.getProperties()) {
			if( property instanceof IEnumeration ) {
				property.setNullable(isNull);
				property.setMultivalued(isMultivalued);
				targetNode.addProperty(property);
				
				targetNode.removeSourcePropertyLinkedAtNode(enumNode.getName()); 
			}
		}
		targetNode.removeAssociation(association);
	}
	
	private static Cardinality getCardinalityOf(INode node, IOntoAssociation association) {
		if( association.getSourceNode() == node ) {
			return association.getSourceCardinality();
		}
		else {
			return association.getTargetCardinality();
		}
	}
	
	private static INode getTargetNode(INode node, IOntoAssociation association) {
		if( association.getSourceNode() == node ) {
			return association.getTargetNode();
		}
		else {
			return association.getSourceNode();
		}
	}
	/*
	private static void resolveMultivaluedProperties(Graph targetGraph) {
		ArrayList<INode> nodesToAdd = new ArrayList<INode>();
		ArrayList<IOntoProperty> propertiesToRemove = new ArrayList<IOntoProperty>();
		
		for(INode node : targetGraph.getNodes()) {
			propertiesToRemove.clear();
			for(IOntoProperty property : node.getProperties()) {
				if(property.isMultivalued() ) {
					resolveMultivaluedProperty(targetGraph, node, property, nodesToAdd, propertiesToRemove);
				}
			}
			Util.removeProperties(node, propertiesToRemove);
		}
		Util.addNodes(nodesToAdd, targetGraph);
	}
	
	private static void resolveMultivaluedProperty(Graph targetGraph, INode node, IOntoProperty property, ArrayList<INode> nodesToAdd, ArrayList<IOntoProperty> propertiesToRemove ) {
		
		String aName;//association name
		
		INode newNode = new Node(property.getColumnName(), "table");
		//The cardinality 0 occurs when there are no records in the target table. When one insertion in 
		//the target table occurs, the Enum field must be filled.
		property.setNullable(false);
		aName = node.getName() + "_" + newNode.getName()+ "_"+ getNextNum();
		IOntoAssociation association = new OntoAssociation(aName, node, Cardinality.C1, newNode,  Cardinality.C1_N);
		
		newNode.addAssociation(association);
		newNode.addProperty(property);
		node.addAssociation(association);
		propertiesToRemove.add(property);
		
		nodesToAdd.add(newNode);
	}
	*/
	private static void putForeignKey( IGraph graph ) {
		for(IOntoAssociation association : graph.getAssociations()) {
			if( 	(association.getSourceCardinality() == Cardinality.C0_1 || 
					 association.getSourceCardinality() == Cardinality.C1  
					) &&
					(association.getTargetCardinality() == Cardinality.C0_N ||
					association.getTargetCardinality() == Cardinality.C1_N
					)
				){
				propagateKey(
						association.getSourceNode(), association.getSourceCardinality(), 
						association.getTargetNode(), association.getTargetCardinality(),
						association);
			}
			else if( 	(association.getTargetCardinality() == Cardinality.C0_1 || 
						 association.getTargetCardinality() == Cardinality.C1  
						) &&
						(association.getSourceCardinality() == Cardinality.C0_N ||
						association.getSourceCardinality() == Cardinality.C1_N
						)
					) {
				propagateKey(
						association.getTargetNode(), association.getTargetCardinality(), 
						association.getSourceNode(), association.getSourceCardinality(),
						association);
			}
		}
	}
	
	private static void propagateKey(INode from, Cardinality cardinalityFrom, INode to, Cardinality cardinalityTo, IOntoAssociation association) {
		IOntoProperty fk = from.getPrimaryKey().clone();
		String newPropertyName;
		
		if( to.existsPropertyName( fk.getName() ) ) {
			newPropertyName = adjustPropertyName ( getNewPropertyName( fk, association) );
			fk.setName(newPropertyName);
			fk.setColumnName(newPropertyName);
		}
		fk.setPrimeryKey(false);
		fk.setForeignKey(from);
		
		if(cardinalityFrom == Cardinality.C0_1 || cardinalityFrom == Cardinality.C0_N)
			fk.setNullable(true);
		else fk.setNullable(false);
		
		to.addPropertyAt(1, fk);
	}
	
	private static String getNewPropertyName(IOntoProperty prop, IOntoAssociation association) {
		String result;
		String associationName;
		
		//removes '_id' from the column
		result = prop.getName().substring(0, prop.getName().length() - 3);
		
		associationName = association.getName();
		
		associationName = associationName.trim();
		if( !associationName.equals("") ) {
			result += associationName.substring(0, 1).toUpperCase() + associationName.substring(1);
		}
		
		return result + "_id";
	}
	
	private static void putPrimaryKey(IGraph target) {
		String pkName;
		IOntoProperty property;
		
		for(INode node : target.getNodes()) {
			pkName = node.getName() + "_id";
			
			property = new OntoProperty( pkName, "integer", false, false );
			
			property.setPrimeryKey(true);
			
			node.addPropertyAt(0, property);
		}
	}
	
	private static void adjustNames(IGraph target) {
		for(INode node : target.getNodes()) {
			node.setName( adjustTableName( node.getName() ) );
			for(IOntoProperty property : node.getProperties()) {
				property.setColumnName( adjustPropertyName ( property.getName() ) );
				if( property instanceof IEnumeration ) {
					adjustEnumeration((IEnumeration)property);
				}
			}
		}
	}
	
	private static void adjustEnumeration(IEnumeration enumeration) {
		int index = 0;
		
		while (index < enumeration.getValues().size()) {
			enumeration.getValues().set(index, enumeration.getValues().get(index).toUpperCase());
			index++;
		}
		
	}
	
	private static String adjustTableName(String name) {
		String newName = "" + name.charAt(0);
		int index = 1;
		
		while( index < name.length() ) {
			if ((name.charAt(index) >= 'A') && (name.charAt(index) <= 'Z')) {
				newName += "_";
			}
			newName += name.charAt(index);
			index++;
		}
		return newName.toLowerCase();
	}
	
	private static String adjustPropertyName(String name) {
		String newName = "";
		int index = 0;
		
		//In order not to add "_" in the properties which are written in uppercase.
		while( index < name.length() && name.charAt(index) >= 'A' && name.charAt(index) <= 'Z' ) {
			newName += name.charAt(index);
			index++;
		}
		
		while( index < name.length() ) {
			if ((name.charAt(index) >= 'A') && (name.charAt(index) <= 'Z')) {
				newName += "_";
			}
			newName += name.charAt(index);
			index++;
		}
		
		return newName.toLowerCase();
	}
	/*
	private static void resolveCardinalityNtoN( Graph target ) {
		String tableName;
		String aName; //association name
		INode newNode;
		IOntoAssociation associationA;
		IOntoAssociation associationB;
		ArrayList<IOntoAssociation> associations = getNtoNAssociations(target);
		
		for(IOntoAssociation association : associations) {
			
			tableName = association.getSourceNode().getName() + "_" + association.getTargetNode().getName();
			newNode = new Node(tableName, "TABLE");
			
			aName = association.getSourceNode().getName() + "_" + newNode.getName()+ "_"+ getNextNum();
			associationA = new OntoAssociation(
					aName,
					association.getSourceNode(), 
					Cardinality.C1, 
					newNode,
					Cardinality.C0_N);
			
			association.getSourceNode().addAssociation(associationA);
			
			aName = association.getTargetNode().getName() + "_" + newNode.getName()+ "_"+ getNextNum();
			associationB = new OntoAssociation(
					aName,
					association.getTargetNode(),
					Cardinality.C1,
					newNode,
					Cardinality.C0_N);
			
			association.getTargetNode().addAssociation(associationB);
			
			newNode.addAssociation(associationA);
			newNode.addAssociation(associationB);
			
			association.removeAssociation();
			
			target.addNode(newNode);
		}
	}
	
	private static ArrayList<IOntoAssociation> getNtoNAssociations(Graph target){
		ArrayList<IOntoAssociation> associations = new ArrayList<IOntoAssociation>();
		
		for(INode node : target.getNodes()) {
			
			for(IOntoAssociation association : node.getAssociations()) {
				if( association.getAssociationType() == AssociationType.ASSOCIATION ) {
					if( 
							( association.getSourceCardinality() == Cardinality.C0_N ||
							  association.getSourceCardinality() == Cardinality.C1_N
							  ) &&
							( association.getTargetCardinality() == Cardinality.C0_N ||
							  association.getTargetCardinality() == Cardinality.C1_N
							)
						){
						addIfNotExists(associations, association);
					}
				}
			}
		}
		
		return associations;
	}
	
	private static void addIfNotExists(ArrayList<IOntoAssociation> associations, IOntoAssociation association) {
		for(IOntoAssociation element : associations) {
			if(element == association) {
				return;
			}
		}
		associations.add(association);
	}
	*/
}
