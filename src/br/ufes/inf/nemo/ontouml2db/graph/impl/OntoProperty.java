package br.ufes.inf.nemo.ontouml2db.graph.impl;

import br.ufes.inf.nemo.ontouml2db.graph.INode;
import br.ufes.inf.nemo.ontouml2db.graph.IOntoProperty;

public class OntoProperty implements IOntoProperty {

	private String name;
	private String columnName;
	private String dataType;
	private boolean isPrimaryKey;
	private boolean isForeignKey;
	private String foreignNodeName;
	private boolean isNull;
	private boolean multivalued;
	private Object defaultValue;
	public boolean resolved;
	
	public OntoProperty(String name, String dataType,boolean isNull, boolean multivalued){
		this.name = name;
		this.columnName = name;
		this.dataType = dataType;
		this.isPrimaryKey = false;
		this.isForeignKey = false;
		this.isNull = isNull;
		this.multivalued = multivalued;
		this.defaultValue = null;
		this.resolved = false;
	}
	
	protected OntoProperty(String name, String dataType, boolean isPK, boolean isFK, 
			String foreignNodeName, boolean isNull, boolean multivalued, String columnName, Object defaultValue){
		this.name = name;
		this.dataType = dataType;
		this.isPrimaryKey = isPK;
		this.isForeignKey = isFK;
		this.foreignNodeName = foreignNodeName;
		this.isNull = isNull;
		this.multivalued = multivalued;
		this.columnName = columnName;
		this.defaultValue = defaultValue;
		this.resolved = false;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public void setColumnName(String name) {
		columnName = name;
	}
	
	public String getColumnName() {
		return columnName;
	}
	
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	
	public String getDataType() {
		return dataType;
	}
	
	public void setPrimeryKey(boolean flag) {
		isPrimaryKey = flag;
		isNull = false;
	}
	
	public boolean isPrimaryKey() {
		return isPrimaryKey;
	}
	
	public void setForeignKey(INode foreignNode){
		if( foreignNode != null) {
			this.isForeignKey = true;
			this.foreignNodeName = foreignNode.getName();
		}
		else {
			this.isForeignKey = false;
			this.foreignNodeName = null;
		}
	}
	
	public String getForeingKeyNodeName() {
		return foreignNodeName;
	}
	
	public boolean isForeignKey() {
		return isForeignKey;
	}
	
	public void setNullable(boolean flag) {
		isNull = flag;
	}
	
	public boolean isNull() {
		return isNull;
	}
	
	public boolean isMultivalued() {
		return multivalued;
	}
	
	public void setMultivalued(boolean flag) {
		multivalued = flag;
	}
	
	public OntoProperty clone() {
		return new OntoProperty(name, dataType, isPrimaryKey, isForeignKey, foreignNodeName, isNull, multivalued, columnName, defaultValue);
	}
	
	public String toString() {
		return columnName + ": " + dataType + ", " + ((isNull == true) ? "NULL" : "NOT NULL") ;
	}

	public void setDefaultValue(Object value) {
		defaultValue = value;
	}

	public Object getDefaultValue() {
		return defaultValue;
	}

	public void setResolved(boolean flag) {
		resolved = flag;
	}
	
	public boolean isResolved() {
		return resolved;
	}
}
