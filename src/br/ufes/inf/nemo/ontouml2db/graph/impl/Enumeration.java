package br.ufes.inf.nemo.ontouml2db.graph.impl;

import java.util.ArrayList;

import br.ufes.inf.nemo.ontouml2db.graph.IEnumeration;

public class Enumeration extends OntoProperty implements IEnumeration{

	private ArrayList<String> values;
	
	public Enumeration(String name, String dataType, boolean isNull, boolean multValues) {
		super(name, dataType, isNull, multValues);
		values = new ArrayList<String>();
	}
	
	protected Enumeration(String name, String dataType, boolean isPK, boolean isFK, String foreignNodeName, 
			boolean isNull, boolean multivalued, ArrayList<String> values, String columnName, Object defaultValue) {
		super(name, dataType, isPK, isFK, foreignNodeName, isNull, multivalued, columnName, defaultValue);
		this.values = values;
	}

	public void addValue(String value) {
		values.add(value);		
	}

	public ArrayList<String> getValues() {
		return values;
	}
	
	public OntoProperty clone() {
		return new Enumeration(
				getName(),
				getDataType(), 
				isPrimaryKey(), 
				isForeignKey(),
				getForeingKeyNodeName(),
				isNull(), 
				isMultivalued(),
				values,
				getColumnName(),
				getDefaultValue()
				);
	}

	public String toString() {
		String result = getColumnName()+ ": " + getDataType() + "[";
		
		for(String str : values) {
			result += str + " | ";
		}
		result += "]";
		return result;
	}

}
