package br.ufes.inf.nemo.ontouml2db.graph.impl;

import java.util.ArrayList;

import br.ufes.inf.nemo.ontouml2db.graph.INode;
import br.ufes.inf.nemo.ontouml2db.graph.IOntoProperty;
import br.ufes.inf.nemo.ontouml2db.graph.IPropertyContainer;

public class PropertyContainer implements IPropertyContainer{

	private ArrayList<IOntoProperty> properties;
	private INode sourceNode;
	
	public PropertyContainer(INode sourceNode) {
		this.sourceNode = sourceNode;
		this.properties = new ArrayList<IOntoProperty>();
	}
	
	private PropertyContainer(INode sourceNode, ArrayList<IOntoProperty> properties) {
		this.sourceNode = sourceNode;
		this.properties = properties;
	}
	
	public ArrayList<IOntoProperty> getProperties(){
		return properties;
	}
	
	public void addProperty( IOntoProperty property ) {
		if( !existsProperty(property) ) {
			properties.add(property);
		}
	}
	
	public void addPropertyAt(int index, IOntoProperty property ) {
		if( !existsProperty(property) ) {
			if( index < properties.size() ) {
				properties.add(index, property);
			}
			else {
				properties.add(property);
			}
		}
	}
	
	public void addProperty(ArrayList<IOntoProperty> properties) {
		for(IOntoProperty property : properties) {
			addProperty(property);
		}
	}
	
	public void addPropertyAt(int index, ArrayList<IOntoProperty> properties) {
		for(IOntoProperty property : properties) {
			addPropertyAt(index, property);
			index++;
		}
	}

	public void removeProperty(IOntoProperty property) {
		int index = 0;
		IOntoProperty currentProperty;
		while( index < properties.size() ) {
			currentProperty = properties.get(index);
			if( currentProperty == property ) {
				properties.remove(index);
			}
			else {
				index++;
			}
		}
	}
	
	private boolean existsProperty(IOntoProperty newProperty) {
		for(IOntoProperty property : properties) {
			if( property.getName().equalsIgnoreCase(newProperty.getName()) ) {
				return true;
			}
		}
		return false;
	}
	
	public String toString() {
		String msg = "";
		
		msg += "\n\t : [ ";
		for(IOntoProperty property : properties) {
			msg += property + "; ";
		}
		msg += "]";
		
		return msg;
	}
	
	public PropertyContainer clonePropertyContainer(INode sourceNode) {
		ArrayList<IOntoProperty> newProperties = new ArrayList<IOntoProperty>();
		for(IOntoProperty property : properties) {
			newProperties.add( property.clone());
		}
		return new PropertyContainer(sourceNode, newProperties );
	}
	
	public IOntoProperty getProperty(String propertyName) {
		for(IOntoProperty property : properties) {
			if( property.getName().equalsIgnoreCase(propertyName) ) {
				return property;
			}
		}
		return null;
	}
	
	public IOntoProperty getPrimaryKey() {
		for(IOntoProperty property : properties) {
			if( property.isPrimaryKey() )
				return property;
		}
		return null;
	}
	
	public String getPKName() {
		for(IOntoProperty property : properties) {
			if( property.isPrimaryKey() ) {
				return property.getColumnName();
			}
		}
		return "[NULL in Util.getPKName for class "+ sourceNode.getName() +"]";
	}

	public boolean existsPropertyName(String propertyName) {
		for(IOntoProperty property : properties) {
			if( property.getName().equalsIgnoreCase(propertyName) )
				return true;
		}
		return false;
	}
}
