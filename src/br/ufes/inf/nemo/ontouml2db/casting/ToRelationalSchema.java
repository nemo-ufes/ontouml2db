package br.ufes.inf.nemo.ontouml2db.casting;

import br.ufes.inf.nemo.ontouml2db.casting.schema.CastToGenericDBMS;
import br.ufes.inf.nemo.ontouml2db.casting.schema.CastToH2;
import br.ufes.inf.nemo.ontouml2db.casting.schema.CastToMySql;
import br.ufes.inf.nemo.ontouml2db.casting.schema.DBMS;
import br.ufes.inf.nemo.ontouml2db.casting.schema.ICastSGBD;
import br.ufes.inf.nemo.ontouml2db.graph.IGraph;

public class ToRelationalSchema {

	public static void generateSchema(IGraph graph, DBMS dbms, String file) {
		
		ICastSGBD targetSGBD;
		
		switch (dbms) {
		case MYSQL: {
			targetSGBD = new CastToMySql();
			break;
		}
		case H2: {
			targetSGBD = new CastToH2();
			break;
		}
		default:
			targetSGBD = new CastToGenericDBMS();
			break;
		}
		
		targetSGBD.generateSchema(graph, file);
	}
}
