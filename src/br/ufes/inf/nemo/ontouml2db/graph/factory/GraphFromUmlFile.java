package br.ufes.inf.nemo.ontouml2db.graph.factory;

/**
 *
 * Author: Gustavo L. Guidoni
 */

import java.io.File;

import br.ufes.inf.nemo.ontouml2db.graph.Cardinality;
import br.ufes.inf.nemo.ontouml2db.graph.IGraph;
import br.ufes.inf.nemo.ontouml2db.graph.INode;
import br.ufes.inf.nemo.ontouml2db.graph.IOntoAssociation;
import br.ufes.inf.nemo.ontouml2db.graph.impl.Graph;
import br.ufes.inf.nemo.ontouml2db.graph.impl.Node;
import br.ufes.inf.nemo.ontouml2db.graph.impl.OntoAssociation;
import br.ufes.inf.nemo.ontouml2db.graph.impl.OntoGeneralization;
import br.ufes.inf.nemo.ontouml2db.graph.impl.OntoGeneralizationSet;
import br.ufes.inf.nemo.ontouml2db.graph.impl.OntoProperty;
import br.ufes.inf.nemo.ontouml2db.util.Util;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Generalization;
import org.eclipse.uml2.uml.GeneralizationSet;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.PackageableElement;
import org.eclipse.uml2.uml.resources.util.UMLResourcesUtil;

public class GraphFromUmlFile implements GraphFactory{
	
	private int num;
	private IGraph graph;
	private Package pack;
	
	private int getNextNum() {
		return num++;
	}
	
	public IGraph createNewGraph( String path ) {
		num = 1;
		return createGraph( path );
	}
	
	private IGraph createGraph( String path ) {
		
		pack = getPackageFromFile( path );
		
		graph = new Graph( Util.getFileName(path) );
		
		putClasses( );
		
		putAssociations( );
		
		putGeneralizations( );
		
		putGeneralizationSet( );
		
		return graph;
	}
	
	private Package getPackageFromFile( String path ) {
		File sourceFile = new File( path );
		if (!sourceFile.isFile()) {
			System.out.println("Error accessing: " + sourceFile.getAbsolutePath());
			System.exit(1);
		}
		
		try {
			Resource resource;
			ResourceSet resourceSet = new ResourceSetImpl();
			UMLResourcesUtil.init(resourceSet);
	
			URI uri = URI.createFileURI(sourceFile.getAbsolutePath());

		
			resource = resourceSet.getResource(uri, true);

			return (Package) resource.getContents().get(0);
		}
		catch(Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		return null;
	}
	
	/**
	 * Add the classes in the graph.
	 * 
	 * @param graph
	 * @param triples
	 */
	private void putClasses() {
		INode node;
		boolean nullable;
		boolean multValues;
		
		for (PackageableElement element : pack.getPackagedElements()) {
			if( element instanceof Class ) {
				Class newClass = (Class) element;
				node = new Node( newClass.getName(), newClass.getAppliedStereotypes().get(0).getName() );
				
				for (Property property : newClass.getOwnedAttributes()) {
					
					if(property.getType() == null) {
						System.out.println("Class: "+  newClass.getName()+  ". Atribute :" + property.getName() + ". Unidentified type.");
						System.exit(0);
					}
					
					if ( property.getLower() == 0) 
						nullable = true;
					else nullable = false;
					
					if ( property.getUpper() > 1) 
						multValues = true;
					else multValues = false;
					
					node.addProperty( 
							new OntoProperty( property.getName(), 
									property.getType().getName(),
									nullable,
									multValues) 
							);
				}
				graph.addNode(node);
			}
		}
	}
	
	/**
	 * Add the associations in the graph.
	 * 
	 * @param graph
	 * @param triples
	 */
	private void putAssociations() {
		Association association;
		IOntoAssociation ontoAssociation;
		
		for (PackageableElement element : pack.getPackagedElements()) {
			if( element instanceof Association ) {
				association = (Association) element;
				
				ontoAssociation = new OntoAssociation(
						//association.getName()
						getName(association)
						, graph.getNode(association.getMemberEnds().get(0).getName())			//sourceNode
						, Cardinality.getCardinality(										//sourceCardinality
								association.getMemberEnds().get(0).getLower(), 
								association.getMemberEnds().get(0).getUpper()) 
						, graph.getNode(association.getMemberEnds().get(1).getName())		//targetNode
						, Cardinality.getCardinality(										//targetCardinality
								association.getMemberEnds().get(1).getLower(), 
								association.getMemberEnds().get(1).getUpper()) 
						);
				
				graph.addAssociation(ontoAssociation);
			}
		}
	}
	
	private String getName(Association association) {
		if( association.getName() == null )
			return "unamed_" + getNextNum();
		else return association.getName().trim();
	}
	
	/**
	 * Add the generalizations in the graph.
	 * @param graph
	 * @param triples
	 */
 	private void putGeneralizations() {
		Class classElement;
		INode generalizationNode;
		INode specializationNode;
		String gName;
		
 		for (PackageableElement packElement : pack.getPackagedElements()) {
			if( packElement instanceof Class ) {
				classElement = (Class) packElement;
				
				for (Generalization generalization : classElement.getGeneralizations()) {
					
					generalizationNode = graph.getNode(generalization.getGeneral().getName());// getNodeByName( graph, generalization.getGeneral().getName()); 
					specializationNode = graph.getNode(generalization.getSpecific().getName()); //getNodeByName( graph, generalization.getSpecific().getName()); 
					
					gName = "Generalization_"+ getNextNum();
					graph.addGeneralization(new OntoGeneralization(gName,  generalizationNode, specializationNode, null) );
					
				}
			}
 		}
	}
 	
 	/**
 	 * Add generalization set in the graph.
 	 * 
 	 * @param graph
 	 * @param pack
 	 */
	private void putGeneralizationSet() {
		Class classElement;
		INode generalizationNode;
		INode specializationNode;
		OntoGeneralizationSet newGeneralizationSet;
		
		for (PackageableElement packElement : pack.getPackagedElements()) {
			if( packElement instanceof Class ) {
				classElement = (Class) packElement;
				
				for (Generalization generalizationOfClass : classElement.getGeneralizations() ) {
					
					if (generalizationOfClass.getGeneralizationSets().size() > 0) {
						
						generalizationNode = graph.getNode(generalizationOfClass.getGeneral().getName());//getNodeByName( graph, generalizationOfClass.getGeneral().getName()); 
						
						for (GeneralizationSet generalizatinoSet : generalizationOfClass.getGeneralizationSets() ) {
						
							newGeneralizationSet = new OntoGeneralizationSet( 
									generalizatinoSet.getName(),  
									generalizationNode,
									generalizatinoSet.isDisjoint(),
									generalizatinoSet.isCovering());
							
							for( Generalization generalizationOfGS : generalizatinoSet.getGeneralizations() ) {
								specializationNode = graph.getNode(generalizationOfGS.getSpecific().getName());//getNodeByName( graph, generalizationOfGS.getSpecific().getName()); 
								newGeneralizationSet.addSpecializationIfNotExists( specializationNode );
							}
							
							graph.addGeneralizationSet( newGeneralizationSet );
						}
					}
				}
			}
 		}
	}
}
