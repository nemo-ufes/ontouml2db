package util;

import java.util.HashMap;

import br.ufes.inf.nemo.ontouml2db.graph.IEnumeration;
import br.ufes.inf.nemo.ontouml2db.graph.IOntoProperty;

public class EnumColumn implements IColumn{
	
	private String name;
	private HashMap<String, String> values;
	
	public EnumColumn(String name, String[] data) {
		this.name = name;
		
		values = new HashMap<String, String>();
		int i = 0;
		while ( i < data.length) {
			values.put(data[i], data[i]);
			i++;
		}
	}
	
	public String getName() {
		return name;
	}

	public boolean isEquals(IOntoProperty property) {
		IEnumeration aux;
		if(property instanceof IEnumeration) {
			aux = (IEnumeration) property;
		}
		else {
			System.out.println("Is not instance of IEnumeration [EnumColumn]");
			return false;
		}
		
		for(String value : aux.getValues()) {
			if( ! values.containsKey(value) )
				return false;
		}
		return false;
	}
}