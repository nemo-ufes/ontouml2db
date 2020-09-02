package util;

import java.util.HashMap;

import br.ufes.inf.nemo.ontouml2db.graph.IEnumeration;
import br.ufes.inf.nemo.ontouml2db.graph.INode;
import br.ufes.inf.nemo.ontouml2db.graph.IOntoProperty;

public class TableTest {
	
	private String name;
	private HashMap<String, IColumn> columns;
	
	public TableTest(String name, String[] attributes) {
		columns = new HashMap<String, IColumn>();
		this.name = name;
		
		int i = 0;
		while ( i < attributes.length) {
			columns.put(attributes[i], new SingleColumn(attributes[i]) );
			i++;
		}
	}
	
	public String getName() {
		return name;
	}
	
	public TableTest addEnumeration(String name, String[] values) {
		columns.put(name, new EnumColumn(name, values));
		return this;
	}
	
	public boolean match(INode node) {
		
		if( !name.equalsIgnoreCase(node.getName()) ) {
			return false;
		}
		
		if( node.getProperties().size() != columns.size() )
			return false;
		
		for( IOntoProperty property : node.getProperties() ) {
		if( !columns.containsKey(property.getColumnName() ) )
				return false;
			
			if( property instanceof  IEnumeration) {
				columns.get(property.getColumnName()).isEquals(property);
			}
		}
		
		return true;
	}
}
