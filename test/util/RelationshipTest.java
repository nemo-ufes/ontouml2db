package util;

import br.ufes.inf.nemo.ontouml2db.graph.Cardinality;

public class RelationshipTest {

	private String sourceNode;
	private Cardinality sourceCardinality;
	private String targetNode;
	private Cardinality targetCardinality;
	
	public RelationshipTest(String sourceNode, Cardinality sourceCardinality, String targetNode, Cardinality targetCardinality) {
		this.sourceNode = sourceNode;
		this.sourceCardinality = sourceCardinality;
		this.targetNode = targetNode;
		this.targetCardinality = targetCardinality;
	}
	
	public String getSourceNode() {
		return sourceNode;
	}
	
	public Cardinality getSourceCardinality() {
		return sourceCardinality;
	}
	
	public String getTargetNode() {
		return targetNode;
	}
	
	public Cardinality getTargetCardinality() {
		return targetCardinality;
	}
}
