package util;

import br.ufes.inf.nemo.ontouml2db.graph.IOntoProperty;

public class SingleColumn implements IColumn{

	private String name;
	
	public SingleColumn(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}

	public boolean isEquals(IOntoProperty property) {
		return name.equalsIgnoreCase(property.getColumnName()) ;
	}
	
	
}
