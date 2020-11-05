package br.ufes.inf.nemo.ontouml2db.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import br.ufes.inf.nemo.ontouml2db.graph.Cardinality;
import br.ufes.inf.nemo.ontouml2db.graph.IGraph;
import br.ufes.inf.nemo.ontouml2db.graph.INode;
import br.ufes.inf.nemo.ontouml2db.graph.IOntoAssociation;
import br.ufes.inf.nemo.ontouml2db.graph.IOntoProperty;

public class Util {

	public static INode findNode(String name, ArrayList<INode> nodes) {
		for(INode node: nodes) {
			if(node.getName().equalsIgnoreCase(name)) {
				return node;
			}
		}
		return null;
	}
	
	public static void destroyNodes(ArrayList<INode> nodesToDestroy, IGraph graph) {
		for(INode node : nodesToDestroy) {
			graph.removeNode(node);
		}
	}
	
	public static void destroyNodesAndReferences(ArrayList<INode> nodesToDestroy, IGraph graph) {
		for(INode node : nodesToDestroy) {
			graph.removesNodeAndItsAssociations(node);
		}
	}
	
	public static void addNodes(ArrayList<INode> nodesToAdd, IGraph graph) {
		for(INode node : nodesToAdd) {
			graph.addNode(node);
		}
	}
	
	public static void removeProperties(INode node, ArrayList<IOntoProperty> propertiesToRemove) {
		while( !propertiesToRemove.isEmpty() ) {
			node.removeProperty(propertiesToRemove.get(0));
			propertiesToRemove.remove(0);
		}
	}
	
	public static void removeAssociations(INode node, ArrayList<IOntoAssociation> associationsToRemove){
		while( !associationsToRemove.isEmpty() ) {
			node.removeAssociation(associationsToRemove.get(0));
			associationsToRemove.remove(0);
		}
	}
	
	public static String getSpaces(String name, int qtd) {
		int tam = name.length();
		StringBuilder spaces = new StringBuilder();
		
		spaces.append(" ");
		qtd--;
		
		while(tam < qtd) {
			spaces.append(" ");
			tam++;
		}
		return spaces.toString();
	}
	
	public static void writeToFile(String data, String filepPath) {
		try {
		      FileWriter myWriter = new FileWriter(filepPath);
		      myWriter.write(data);
		      myWriter.close();

	    } catch (IOException e) {
	      e.printStackTrace();
	    }
	}
	
	public static String removeExtention(String file) {
	    File f = new File(file);
	    // if it's a directory, don't remove the extention
	    if (f.isDirectory()) 
	    	return f.getName();
	    String name = f.getName();
	    // if it is a hidden file
	    if (name.startsWith(".")) {
	        // if there is no extn, do not rmove one...
	        if (name.lastIndexOf('.') == name.indexOf('.')) 
	        	return name;
	    }
	    // if there is no extention, don't do anything
	    if (!name.contains(".")) 
	    	return name;
	    // Otherwise, remove the last 'extension type thing'
	    return name.substring(0, name.lastIndexOf('.'));
	}
	
	public static String getFileName(String path) {
		File file = new File( path );
		return file.getName();
	}
	
	public static boolean isLowCardinalityOfNode(INode node, IOntoAssociation association) {
		
		if(		association.getSourceNode() == node &&
				(	association.getSourceCardinality() == Cardinality.C0_1 ||
					association.getSourceCardinality() == Cardinality.C1
				)
			) {
			return true;
		}
		
		if(		association.getTargetNode() == node &&
				(	association.getTargetCardinality() == Cardinality.C0_1 ||
					association.getTargetCardinality() == Cardinality.C1
				)
			) {
			return true;
		}
		return false;
	}
	
	public static boolean isHighCardinalityOfNode(INode node, IOntoAssociation association) {
		
		if(		association.getSourceNode() == node &&
				(	association.getSourceCardinality() == Cardinality.C0_N ||
					association.getSourceCardinality() == Cardinality.C1_N
				)
			) {
			return true;
		}
		
		if(		association.getTargetNode() == node &&
				(	association.getTargetCardinality() == Cardinality.C0_N ||
					association.getTargetCardinality() == Cardinality.C1_N
				)
			) {
			return true;
		}
		return false;
	}
}
