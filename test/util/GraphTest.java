package util;


import java.util.ArrayList;

import br.ufes.inf.nemo.ontouml2db.graph.IGraph;
import br.ufes.inf.nemo.ontouml2db.graph.INode;
import br.ufes.inf.nemo.ontouml2db.graph.IOntoAssociation;

public class GraphTest {
	
	private ArrayList<TableTest> nodesTest;
	private ArrayList<RelationshipTest> associationsTest;
	private ArrayList<TrackerTest> trackersTest;
	private IGraph targetGraph;
	private IGraph sourceGraph;

	public GraphTest(Builder builder){
		targetGraph = builder.targetGraph;
		sourceGraph = builder.sourceGraph;
		nodesTest = builder.nodes;
		associationsTest = builder.associations;
		trackersTest = builder.trackers;
	}
	
	public static class Builder{
		private IGraph targetGraph;
		private IGraph sourceGraph;
		private ArrayList<TableTest> nodes;
		private ArrayList<RelationshipTest> associations;
		private ArrayList<TrackerTest> trackers;
		
		public Builder() {
			nodes = new ArrayList<TableTest>();
			associations = new ArrayList<RelationshipTest>();
			trackers = new ArrayList<TrackerTest>();
		}
		
		public Builder setTargetGraph(IGraph targetGraph) {
			this.targetGraph = targetGraph;
			return this;
		}
		
		public Builder setSourceGraph(IGraph sourceGraph) {
			this.sourceGraph = sourceGraph;
			return this;
		}
		
		public Builder addTable(TableTest node) {
			nodes.add(node);
			return this;
		}
		
		public Builder addRelationship(RelationshipTest association) {
			associations.add(association);
			return this;
		}
		
		public Builder addTracker(TrackerTest tracker) {
			trackers.add(tracker);
			return this;
		}
		
		public GraphTest build() {
			return new GraphTest(this);
		}
	}
	
	public String match() {
		INode sourceNode;
		INode targetNode;
		
		//Checks the number of classes
		if( nodesTest.size() != targetGraph.getNodes().size() ) {
			return "The amount of TABLES does not match.";
		}
		
		//Checks whether the classes are the same.
		for(TableTest table : nodesTest) {
			if( !existsSourceNode( table, targetGraph.getNodes() )  ) {
				return "The table '"+ table.getName() +"' was not found or the columns not are the same.";
			}
		}
		
		//Checks the number of associations
		ArrayList<IOntoAssociation> sourceAssociations = targetGraph.getAssociations();
		if( associationsTest.size() != sourceAssociations.size() ) {
			return "The amount of RELATIONSHIPS does not match. Tested: " + associationsTest.size() + ". Graph: " + sourceAssociations.size();
		}
		
		//Checks whether the associations are the same.
		for( RelationshipTest associationTest : associationsTest ) {
			if( !existsAssociation( associationTest, sourceAssociations ) ) {
				return "The relationship '"+ associationTest.getSourceNode() +" - "+ associationTest.getTargetNode()+"' not exists or the cardinalities are not the same.";
			}
		}
		
		//Checks whether traceability is correct
		if(trackersTest.isEmpty()) return "Tracking has not been set.";
		
		for(TrackerTest tracker : trackersTest) {
			sourceNode = sourceGraph.getNode(tracker.getSourceNode());
			if( sourceNode == null ) return "Class "+tracker.getSourceNode()+" was not found.";
			
			targetNode = targetGraph.getNode(tracker.garTargetNode());
			if( targetNode == null ) return "Table "+tracker.garTargetNode()+" was not found.";
		}
		
		return ""; 
	}

	private boolean existsSourceNode(TableTest nodeTest, ArrayList<INode> targetNodes) {
		for(INode node : targetNodes) {
			if( nodeTest.match(node) ) {
				return true;
			}
		}
		return false;
	}
	
	private boolean existsAssociation(RelationshipTest associationTest, ArrayList<IOntoAssociation> sourceAssociations) {
		for( IOntoAssociation sourceAssociation : sourceAssociations) {
			if( (	associationTest.getSourceNode().equalsIgnoreCase(sourceAssociation.getSourceNode().getName()) &&
					associationTest.getSourceCardinality() == sourceAssociation.getSourceCardinality() &&
					associationTest.getTargetNode().equalsIgnoreCase(sourceAssociation.getTargetNode().getName()) &&
					associationTest.getTargetCardinality() == sourceAssociation.getTargetCardinality()
				) ||
				(	associationTest.getSourceNode().equalsIgnoreCase(sourceAssociation.getTargetNode().getName()) &&
					associationTest.getSourceCardinality() == sourceAssociation.getTargetCardinality() &&
					associationTest.getTargetNode().equalsIgnoreCase(sourceAssociation.getSourceNode().getName()) &&
					associationTest.getTargetCardinality() == sourceAssociation.getSourceCardinality()
				)
			) {
				return true;
			}
		}
		return false;
	}
	
}
