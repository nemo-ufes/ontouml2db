package br.ufes.inf.nemo.ontouml2db.graph.factory;

import br.ufes.inf.nemo.ontouml2db.graph.IGraph;

public interface GraphFactory {
	
	public IGraph createNewGraph( String path );
}
