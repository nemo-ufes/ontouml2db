package br.ufes.inf.nemo.ontouml2db.graph.impl;


/**
 * The Node is the class responsible for storing only the triples of the classes that 
 * make up the diagram. In this way, Node and Class can be understood as synonyms. In 
 * addition to the classes, the Node stores all the associations that a class has, 
 * regardless of if the class is the origin or destination of the association. In this 
 * way, the graph formed is bidirectional. 
 * In order to maintain the traceability of the classes of the source diagram with the 
 * destination diagram, the current Node also stores a reference to the destination 
 * diagram Node.
 * 
 * Author: Gustavo L. Guidoni 
 */

import java.util.ArrayList;

import br.ufes.inf.nemo.ontouml2db.graph.AssociationType;
import br.ufes.inf.nemo.ontouml2db.graph.IAssociationContainer;
import br.ufes.inf.nemo.ontouml2db.graph.INode;
import br.ufes.inf.nemo.ontouml2db.graph.INodeTracker;
import br.ufes.inf.nemo.ontouml2db.graph.IOntoAssociation;
import br.ufes.inf.nemo.ontouml2db.graph.IOntoGeneralizationSet;
import br.ufes.inf.nemo.ontouml2db.graph.IOntoProperty;
import br.ufes.inf.nemo.ontouml2db.graph.IPropertyContainer;
import br.ufes.inf.nemo.ontouml2db.graph.ITracker;


public class Node implements INode{
	
	private String name;
	private String stereotype;
	private IPropertyContainer propertyContainer;
	private IAssociationContainer associationContainer;
	private INodeTracker nodeTracker;
	private boolean visited;//used to transformation
	
	public Node() {
		propertyContainer = new PropertyContainer(this);
		associationContainer = new AssociationContainer(this);
		nodeTracker = new NodeTracker(this);
		visited = false;
	}
	
	public Node(String name, String stereotype) {
		this();
		this.name = name;
		this.stereotype = stereotype;
	}
	
	private Node(String name, String stereotype, IPropertyContainer propertyContainer, INode sourceNode) {
		this(name, stereotype);
		this.propertyContainer = propertyContainer.clonePropertyContainer(this);
		addTracking(sourceNode);
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public String getStereotype() {
		return stereotype;
	}
	
	public void setStereotype(String stereotype) {
		this.stereotype = stereotype;
	}
	
	public void setResolved(boolean flag) {
		visited = flag;
		associationContainer.setResolved(false);
	}
	
	public boolean isResolved() {
		return visited;
	}
	
	public INode clone() {
		//INode node = new Node(name, stereotype, propertyContainer.clonePropertyContainer(), this );
		INode node = new Node(name, stereotype, propertyContainer, this );
		//The current class starts tracking the new node.
		addTracking(node);
		return node;
	}
	
	public String toString() {
		String msg = "\n" + name + " <<" + stereotype + ">>";
		
		msg += propertyContainer.toString();
		
		msg += associationContainer.toString();
		
		//msg += nodeTracker.toString();
		
		return msg;
	}

	public String trackingToString() {
		return nodeTracker.toString();//"Source: "+ name + Util.getSpaces(name, 18) + nodeTracker.toString();
	}

	public void addTracking(INode newNodeReference) {
		nodeTracker.addTracking(newNodeReference);
	}

	public void addTracking(ArrayList<ITracker> trackers) {
		nodeTracker.addTracking(trackers);
	}

	public void addSourceTracking(INode newNodeReference) {
		nodeTracker.addSourceTracking(newNodeReference);
	}

	public void removeSourceTracking() {
		nodeTracker.removeSourceTracking();
	}

	public void removeTracking(INode source) {
		nodeTracker.removeTracking(source);
	}

	public void changeSourceTracking(INode newNodeReference) {
		nodeTracker.changeSourceTracking(newNodeReference);
	}

	public void changeTracking(INode oldNodeReference, INode newNodeReference) {
		nodeTracker.changeTracking(oldNodeReference, newNodeReference);
	}

	public void setSourceTrackerField(IOntoProperty property, Object value) {
		nodeTracker.setSourceTrackerField(property, value);
	}

	public void setTrackerField(INode newNodeReference, IOntoProperty property, Object value) {
		nodeTracker.setTrackerField(newNodeReference, property, value);
	}

	public void setSourcePropertyLinkedAtNode(INode linkedNode) {
		nodeTracker.setSourcePropertyLinkedAtNode(linkedNode);
	}

	public void setPropertyLinkedAtNode(INode newNodeReference, INode linkedNode) {
		nodeTracker.setPropertyLinkedAtNode(newNodeReference, linkedNode);
	}

	public void removeSourcePropertyLinkedAtNode(String nodeName) {
		nodeTracker.removeSourcePropertyLinkedAtNode(nodeName);
	}

	public void removePropertyLinkedAtNode(String nodeName) {
		nodeTracker.removePropertyLinkedAtNode(nodeName);
	}

	public int getAmountNodesTracked() {
		return nodeTracker.getAmountNodesTracked();
	}

	public String getTargetColumnName(String field) {
		return nodeTracker.getTargetColumnName(field);
	}

	public String getTargetPKName() {
		return nodeTracker.getTargetPKName();
	}

	public ArrayList<ITracker> getTrackers() {
		return nodeTracker.getTrackers();
	}
	
	//****************************************************************************
	public IAssociationContainer getAssociationContainer() {
		return associationContainer;
	}
	
	public void addAssociation(IOntoAssociation association) {
		associationContainer.addAssociation(association);
	}
	
	public ArrayList<IOntoAssociation> getAssociations(){
		return associationContainer.getAssociations();
	}

	public void removeAssociationWith(INode node) {
		associationContainer.removeAssociationWith(node);
	}
	
	public void removeAssociation( IOntoAssociation association) {
		associationContainer.removeAssociation(association);
	}
	
	public boolean existsGeneralization( IOntoAssociation generalization ) {
		return associationContainer.existsGeneralization(generalization);
	}
	
	public IOntoGeneralizationSet getGeneralizationSetWith(ArrayList<INode> nodes) {
		return associationContainer.getGeneralizationSetWith(nodes);
	}
	
	public void removeSpecializationWith(INode node) {
		associationContainer.removeSpecializationWith(node);
	}
	
	public boolean hasSpecialization() {
		return associationContainer.hasSpecialization();
	}
	
	public boolean isSpecialization() {
		return associationContainer.isSpecialization();
	}
	
	public ArrayList<IOntoAssociation> getAssociationsOfType(AssociationType type){
		return associationContainer.getAssociationsOfType(type);
	}
	
	public void removeAndDestroyAssociations() {
		associationContainer.removeAndDestroyAssociations();
	}

	public INode getNodeAssociated(String enumTableName) {
		return associationContainer.getNodeAssociated(enumTableName);
	}
	
	//****************************************************************************	
	public void addProperty(IOntoProperty ontoProperty) {
		propertyContainer.addProperty(ontoProperty);
	}
	
	public void addProperty(ArrayList<IOntoProperty> properties) {
		propertyContainer.addProperty(properties);
	}
	
	public void addPropertyAt(int index, IOntoProperty ontoProperty) {
		propertyContainer.addPropertyAt(index, ontoProperty);
	}

	public void addPropertyAt(int index, ArrayList<IOntoProperty> properties) {
		propertyContainer.addPropertyAt(index, properties);
	}
	
	public ArrayList<IOntoProperty> getProperties(){
		return propertyContainer.getProperties();
	}
	
	public void removeProperty(IOntoProperty property) {
		propertyContainer.removeProperty(property);
	}
	
	public IOntoProperty getProperty(String propertyName) {
		return propertyContainer.getProperty(propertyName);
	}
	
	public IPropertyContainer clonePropertyContainer(INode sourceNode) {
		return propertyContainer.clonePropertyContainer(sourceNode);
	}

	public IOntoProperty getPrimaryKey() {
		return propertyContainer.getPrimaryKey();
	}

	public String getPKName() {
		return propertyContainer.getPKName();
	}

	public boolean existsPropertyName(String propertyName) {
		return propertyContainer.existsPropertyName(propertyName);
	}
	
}
