package br.ufes.inf.nemo.ontouml2db.casting.schema;

import br.ufes.inf.nemo.ontouml2db.graph.IEnumeration;
import br.ufes.inf.nemo.ontouml2db.graph.IGraph;
import br.ufes.inf.nemo.ontouml2db.graph.INode;
import br.ufes.inf.nemo.ontouml2db.graph.IOntoProperty;
import br.ufes.inf.nemo.ontouml2db.util.Util;

public class CastToGenericDBMS extends CastType implements ICastSGBD{
	
	protected final String  enumerationName = "enumeration";
	
	public CastToGenericDBMS(String fileTraceability) {
		super(fileTraceability);
	}
	
	public CastToGenericDBMS() {
		super("GENERIC");
	}
	
	public void generateSchema(IGraph target, String file) {
		
		
		StringBuilder ddl = new StringBuilder();
		
		createTables(ddl, target);
		
		createEnums(ddl, target);		
		
		createForeingKeys(ddl, target);
		
		//createInsertsForEnumerations(ddl, target);
		
		Util.writeToFile(ddl.toString(), file);
	}
	/*
	Used to transform an enumeration into a table.
	protected void createInsertsForEnumerations(StringBuilder ddl, Graph target) {
		int i = 1;
		for(INode node : target.getNodes()) {
			if(node.getStereotype().equalsIgnoreCase(enumerationName)) {
				ddl.append("\n");
				for(IOntoProperty property : node.getProperties()) {
					if(!property.isPrimaryKey()) {
						ddl.append("\nINSERT INTO "+ node.getName() + " VALUES( ");
						ddl.append( i + ", '"+ property.getColumnName() + "');" );
						i++;
					}
				}
			}
		}
	}*/
	
	protected void createForeingKeys(StringBuilder ddl, IGraph target) {
		
		for(INode node : target.getNodes()) {
			for(IOntoProperty property : node.getProperties() ) {
				if( property.isForeignKey() ) {
					ddl.append( "\n\nALTER TABLE " );
					ddl.append( node.getName() );
					ddl.append( " ADD FOREIGN KEY ( " );
					ddl.append( property.getColumnName() );
					ddl.append( " ) REFERENCES " );
					ddl.append( property.getForeingKeyNodeName().toLowerCase() );
					ddl.append( " ( " );
					ddl.append( getReferencePkTable( property.getForeingKeyNodeName(), target ) );
					ddl.append( " );" );
				}
			}
		}
	}
	
	private String getReferencePkTable( String nodeName, IGraph target ) {
		for(INode node : target.getNodes()) {
			if( node.getName().equalsIgnoreCase(nodeName) ) {
				for(IOntoProperty property : node.getProperties()) {
					if( property.isPrimaryKey() ) {
						return property.getColumnName();
					}
				}
			}
		}
		return "[Did not find the pk of the referenced table]";
	}
	
	protected void createTables(StringBuilder ddl, IGraph target) {
		for(INode node : target.getNodes()) {
			if( !node.getStereotype().equalsIgnoreCase(enumerationName) ) {
				createTable(ddl, node);
			}
		}
	}
	
	protected void createEnums(StringBuilder ddl, IGraph target) {
		for(INode node : target.getNodes()) {
			if( node.getStereotype().equalsIgnoreCase(enumerationName) ) {
				createEnumeration(ddl, node);
			}
		}
	}
	
	private void createTable(StringBuilder ddl, INode node) {
		boolean firstColumn = false;
		
		createTable(ddl, node.getName());
		
		firstColumn = true;
		for(IOntoProperty property : node.getProperties()) {
			createColumn(ddl, property, firstColumn);
			firstColumn = false;
		}
		ddl.append("\n); \n\n");
	}
	
	private void createEnumeration(StringBuilder ddl, INode node) {
		String pkName = getPKName(node);
		String columnName = node.getName().toLowerCase()+"_name";
		
		ddl.append("CREATE TABLE " + node.getName() + "( ");
		ddl.append(
				"\n\t" + 
				pkName + 
				Util.getSpaces(pkName, 16) + 
				"INT" +
				Util.getSpaces("INT", 8) + 
				"NOT NULL" +
				" PRIMARY KEY");
		
		ddl.append(
				"\n,\t" +
				columnName+ 
				Util.getSpaces(columnName, 16) + 
				"VARCHAR(20)" +
				Util.getSpaces("VARCHAR(20)", 8) + 
				"NOT NULL");
		
		ddl.append("\n);");
	}
	
	protected String getPKName(INode node) {
		for(IOntoProperty property : node.getProperties()) {
			if( property.isPrimaryKey() ) {
				return property.getColumnName();
			}
		}
		return "[Did not find the pk name for"+node.getName()+"]";
	}
	
	private void createColumn(StringBuilder ddl, IOntoProperty property, boolean firstColumn) {
		String comma;
		String tabName;
		String tabAfterComma;
		String tabProperty;
		String primaryKey = "";
		String nullable;
		String enumColumn = "";
		boolean first;
		
		if(firstColumn)
			comma = "\n";
		else comma = "\n,";
		
		tabAfterComma = Util.getSpaces(comma, 8);
		
		tabName = Util.getSpaces(property.getColumnName(), 23);
		
		tabProperty = Util.getSpaces( getCorrespondentFrom(property.getDataType()), 15 );
				
		if(property.isPrimaryKey()) 
			primaryKey = " AUTO_INCREMENT PRIMARY KEY";
		
		if( property.isNull() )
			nullable = " NULL ";
		else nullable = " NOT NULL ";
		
		if( property instanceof IEnumeration ) {
			enumColumn = "ENUM(";
			first = true;
			for(String value : ((IEnumeration)property).getValues()) {
				if(first) {
					enumColumn += "'"+value+"'";
					first = false;
				}
				else {
					enumColumn += ",'"+value+"'";
				}
			}
			enumColumn += ")";
		}
		
		ddl.append(comma); 
		ddl.append(tabAfterComma);
		ddl.append(property.getColumnName()); 
		ddl.append(tabName);
		
		if( enumColumn.length() > 1 ) {
			ddl.append(enumColumn);
		}
		else {
			ddl.append(getCorrespondentFrom(property.getDataType()));
			ddl.append(tabProperty);
		}
		
		if(property.getDefaultValue() != null) {
			ddl.append(" DEFAULT");
			ddl.append(" ");
			ddl.append(property.getDefaultValue());
		}
		
		ddl.append(nullable);
		ddl.append(primaryKey);
	}
	
	private void createTable(StringBuilder ddl, String name) {
		ddl.append("CREATE TABLE ");
		ddl.append(name);
		ddl.append(" ( ");
	}
}
