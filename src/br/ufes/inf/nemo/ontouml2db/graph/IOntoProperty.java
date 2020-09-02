/**
 * This interface provides the necessary methods to manipulate a node property.
 * 
 * Author: Gustavo L. Guidoni
 */

package br.ufes.inf.nemo.ontouml2db.graph;

public interface IOntoProperty {
	
	/**
	 * Informs the property name.
	 * 
	 * @param name. Property name.
	 */
	public void setName(String name);
	
	/**
	 * Returns the property name;
	 * 
	 * @return String with the property name.
	 */
	public String getName();
	
	/**
	 * Informs the column name to be assigned to the property in the database.
	 * 
	 * @param name. Attribute name in the database.
	 */
	public void setColumnName(String name);
	
	/**
	 * Returns the column name of the property in the database.
	 * 
	 * @return String with the column name.
	 */
	public String getColumnName();
	
	/**
	 * Informs the property data type.
	 * 
	 * @param dataType. Name of the property type.
	 */
	public void setDataType(String dataType);
	
	/**
	 * Returns the property data type.
	 * 
	 * @return String with the property data type.
	 */
	public String getDataType();
	
	/**
	 * Returns a new property with the same values of the current property.
	 * 
	 * @return IOntoProperty.
	 */
	public IOntoProperty clone();
	
	/**
	 * Informs that the property is a primary key.
	 * 
	 * @param flag. If true, the property will be marked as a 
	 * primary key.
	 */
	public void setPrimeryKey(boolean flag);
	
	/**
	 * Returns whether the property is marked as primary key.
	 * 
	 * @return True if the property is a primary key, otherwise false.
	 */
	public boolean isPrimaryKey();
	
	/**
	 * Informs which node the property (marked as a foreign key) refers to. 
	 * This is necessary because the foreign key name may be different from 
	 * the primary key name of the referenced table.
	 * 
	 * @param foreignNode. Node to be referenced.
	 */
	public void setForeignKey(INode foreignNode);
	
	/**
	 * Returns the name of the node referenced as foreign key.
	 * 
	 * @return A string with the node name.
	 */
	public String getForeingKeyNodeName();
	
	/**
	 * Returns if the property is marked as a foreign key.
	 * 
	 * @return True if the property is a foreign key, otherwise false.
	 */
	public boolean isForeignKey();
	
	/**
	 * Returns whether the property accepts null.
	 * 
	 * @return True if the property accepts null, otherwise false.
	 */
	public boolean isNull();
	
	/**
	 * Informs whether the property accepts null.
	 * 
	 * @param flag. If true, the property accepts null.
	 */
	public void setNullable(boolean flag);
	
	
	/**
	 * Returns whether the property is multivalued.
	 * 
	 * @return True if the property is multivalued, otherwise false.
	 */
	public boolean isMultivalued();
	
	/**
	 * Informs whether the property is multivalued.
	 * 
	 * @param flag. If true, the property is multivalued.
	 */
	public void setMultivalued(boolean flag);
	
	/**
	 * Informs a default value for the property.
	 * 
	 * @param value. The default value.
	 */
	public void setDefaultValue(Object value);
	
	/**
	 * Returns the default value of the property.
	 * 
	 * @return An object with de default value.
	 */
	public Object getDefaultValue();
	
	/**
	 * Informs that the property has already been resolved. Important 
	 * for the graph operations. 
	 * 
	 * @param flag. True if the property has been resolved.
	 */
	public void setResolved(boolean flag);
	
	/**
	 * Returns whether the property has been resolved.
	 * 
	 * @return True if the property has been resolved.
	 */
	public boolean isResolved();
}
