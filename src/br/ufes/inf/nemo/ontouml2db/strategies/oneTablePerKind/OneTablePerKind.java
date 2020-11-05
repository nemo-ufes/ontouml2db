/**
 * Transforms an OntoUML model into a model ready for final transformation and its 
 * corresponding into a relational schema.
 * 
 * The one table per kind approach is used; all non-sortals are flattened to kinds, 
 * and sortals lifted to kinds.
 * 
 * Author: João Paulo A. Almeida; Gustavo L. Guidoni
 * 
 */

package br.ufes.inf.nemo.ontouml2db.strategies.oneTablePerKind;

import br.ufes.inf.nemo.ontouml2db.graph.IGraph;

public class OneTablePerKind {

	public static void transform(IGraph graph) {
		
		Flatten.doFlattening(graph);
		
		Lift.doLifting(graph);
	}	
}
