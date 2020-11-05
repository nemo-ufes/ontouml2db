package br.ufes.inf.nemo.ontouml2db.casting.schema;

import br.ufes.inf.nemo.ontouml2db.graph.IGraph;
import br.ufes.inf.nemo.ontouml2db.graph.INode;
import br.ufes.inf.nemo.ontouml2db.graph.IOntoProperty;

public class CastToMySql extends CastToGenericDBMS {
	
	public CastToMySql() {
		super( "MySql" );
	}
	
	@Override
	protected void createEnums(StringBuilder ddl, IGraph target) {
		boolean first = true;
		for(INode node : target.getNodes()) {
			if( node.getStereotype().equalsIgnoreCase(enumerationName) ) {
				ddl.append("\nCREATE TABLE " + node.getName() + "(");
				ddl.append( "\n\t" + getPKName(node) + " ENUM( ");
				first = true;
				for(IOntoProperty property : node.getProperties()) {
					if( !property.isPrimaryKey() ) {
						if(first) {
							ddl.append("'"+property.getName() + "'");
							first = false;
						}
						else {
							ddl.append(", '"+property.getName()+ "'");
						}
					}
				}
				ddl.append(") \n);");
			}
		}
	}
	
	//@Override
	//protected void createInsertsForEnumerations(StringBuilder ddl, Graph target) {
	//	//do nothing
	//}
}
