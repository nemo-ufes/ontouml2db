package br.ufes.inf.nemo.ontouml2db.casting.schema;

import br.ufes.inf.nemo.ontouml2db.graph.IGraph;

public interface ICastSGBD {
	
	public void generateSchema(IGraph target, String file);
}
