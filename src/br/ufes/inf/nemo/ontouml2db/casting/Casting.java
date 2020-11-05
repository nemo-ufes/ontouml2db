package br.ufes.inf.nemo.ontouml2db.casting;

import br.ufes.inf.nemo.ontouml2db.casting.schema.DBMS;
import br.ufes.inf.nemo.ontouml2db.graph.IGraph;

public class Casting {
	
	public static void generateDatabaseSchema(IGraph graph, DBMS dbms, String file, String fileTargetName) {
		
		ToEntityRelationship.run(graph);				
		
		file += "\\"+fileTargetName+".sql";
		
		ToRelationalSchema.generateSchema(graph, dbms, file);	
		
	}			
}
