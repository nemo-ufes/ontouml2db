package util;

import br.ufes.inf.nemo.ontouml2db.graph.IOntoProperty;

public interface IColumn {

	public String getName();
	
	public boolean isEquals(IOntoProperty property);
}
