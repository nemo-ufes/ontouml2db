package br.ufes.inf.nemo.ontouml2db;

import java.io.File;

import br.ufes.inf.nemo.ontouml2db.casting.Casting;
import br.ufes.inf.nemo.ontouml2db.casting.schema.DBMS;
import br.ufes.inf.nemo.ontouml2db.graph.IGraph;
import br.ufes.inf.nemo.ontouml2db.graph.factory.GraphFactory;
import br.ufes.inf.nemo.ontouml2db.graph.factory.GraphFromUmlFile;
import br.ufes.inf.nemo.ontouml2db.strategies.Strategy;
import br.ufes.inf.nemo.ontouml2db.strategies.oneTablePerKind.OneTablePerKind;
import br.ufes.inf.nemo.ontouml2db.util.Util;

public class Mapper {
	
	private IGraph graphSource;
	private IGraph graphTarget;
	
	//Configuratin variables
	private Strategy strategy;
	private String filePath;
	private String destinationPath;
	private String fileTargetName;
	private DBMS dbms;
	private GraphFactory factory;

	public Mapper(Builder builder) throws Exception {
		this.strategy = builder.strategy;
		this.filePath = builder.filePath;
		this.destinationPath = builder.destinationPath;
		this.dbms = builder.dbms;
		this.factory = builder.factory;
	}
	
	public static class Builder{
		private Strategy strategy;
		private String filePath;
		private String destinationPath;
		private DBMS dbms;
		private GraphFactory factory;
		
		public Builder() {
			strategy = Strategy.ONE_TABLE_PER_CLASS;
			filePath = null;
			destinationPath = null;
			dbms = null;
			factory = null;
		}
		
		public Builder setUmlFile(String path) {
			filePath = path;
			factory = new GraphFromUmlFile();
			return this;
		}
		public Builder setStrategy(Strategy strategy) {
			this.strategy = strategy;
			return this;
		}
		
		public Builder setDestinationPath(String path) {
			destinationPath = path;
			return this;
		}
		
		public Builder setDBMS(DBMS dbms) {
			this.dbms = dbms;
			return this;
		}
		
		/**
		 * 
		 * @return
		 * @throws Exception
		 */
		public Mapper build() throws Exception{
			return new Mapper(this);
		}
	}
	
	private void verifyParameters() {
		
		if( filePath == null) {
			System.out.println("[Mapper] The file with the diagram to be transformed was not informed.");
			System.exit(0);
		}else {
			File f = new File(filePath);
			fileTargetName = Util.removeExtention(f.getName());
		}
		
		if(destinationPath == null) {
			File f = new File(filePath);
			destinationPath = f.getParent();
		}else {
			File f = new File(destinationPath);
			if (!f.isDirectory()) {
				System.out.println("[Mapper] A valid destination directory was not entered.");
				System.exit(0);
			}
		}
		
		if( dbms == null) {
			System.out.println("[Mapper] The DBMS was not informed.");
			System.exit(0);
		}
		
		if( factory == null ) {
			System.out.println("[Mapper] The transformation strategy was not informed.");
			System.exit(0);
		}
	}
	
	public void doMapping() throws Exception{
		
		verifyParameters();
		
		graphSource = factory.createNewGraph( filePath );
		
		graphTarget = graphSource.clone();
		
		switch (strategy) {
		case  ONE_TABLE_PER_CLASS:
			//Do nothing. The target diagram is the same as the source diagram.
			break;
			
		case  ONE_TABLE_PER_KIND:
			OneTablePerKind.transform(graphTarget);
			break;

		default: 
			System.out.println("ERROR: The mapping strategy has not been defined.");
			break;
		}
		
		Casting.generateDatabaseSchema(
				graphTarget,
				dbms,
				destinationPath,
				fileTargetName
				);
		
	}
	
	public IGraph getSourceGraph(){
		return graphSource;
	}
	
	public IGraph getTargetGraph() {
		return graphTarget;
	}
	
	public void showSourceNodes() {
		graphSource.showNodes();
	}
	
	public void showTargetNodes() {
		graphTarget.showNodes();
	}
	
	public void showSourceTracking() {
		graphSource.showTracking();
	}
	
}