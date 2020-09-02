package br.ufes.inf.nemo.ontouml2db.graph.impl;

import br.ufes.inf.nemo.ontouml2db.graph.INode;
import br.ufes.inf.nemo.ontouml2db.graph.IOntoProperty;
import br.ufes.inf.nemo.ontouml2db.graph.ITracker;

public class Tracker implements ITracker{

	private INode node;
	private IOntoProperty property;
	private Object value;
	private INode propertyLinkedAtNode;
	
	public Tracker(INode node, IOntoProperty property, Object value) {
		this.node = node;
		this.property = property;
		this.value = value;
		this.propertyLinkedAtNode = null;
	}
	
	public INode getNode() {
		return node;
	}
	
	public void setNode(INode node) {
		this.node = node;
	}
	
	public IOntoProperty getProperty() {
		return property;
	}
	
	public void setProperty(IOntoProperty property) {
		this.property = property;
	}
	
	public Object getValue() {
		return value;
	}
	
	public void setValue(Object value) {
		this.value = value;
	}
	
	public void setPropertyLinkedAtNode(INode node) {
		propertyLinkedAtNode = node;
	}
	
	public INode getPropertyLinkedAtNode() {
		return propertyLinkedAtNode;
	}
	
	public String toString() {
		String field = "";
		
		field += (node == null) ? "" : node.getName();
		field += (propertyLinkedAtNode == null) ? "" : " => " + propertyLinkedAtNode.getName();
		
		if( property != null ) {
			field += " => "+ property.getName() + " = "+ value;
		}
		return field;		
	}
}
