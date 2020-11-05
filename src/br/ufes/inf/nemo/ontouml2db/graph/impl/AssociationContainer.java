package br.ufes.inf.nemo.ontouml2db.graph.impl;

import java.util.ArrayList;

import br.ufes.inf.nemo.ontouml2db.graph.AssociationType;
import br.ufes.inf.nemo.ontouml2db.graph.IAssociationContainer;
import br.ufes.inf.nemo.ontouml2db.graph.INode;
import br.ufes.inf.nemo.ontouml2db.graph.IOntoAssociation;
import br.ufes.inf.nemo.ontouml2db.graph.IOntoGeneralization;
import br.ufes.inf.nemo.ontouml2db.graph.IOntoGeneralizationSet;

public class AssociationContainer implements IAssociationContainer {

	private ArrayList<IOntoAssociation> associations;
	private INode parentNode;
	
	public AssociationContainer(INode parentNode) {
		associations = new ArrayList<IOntoAssociation>();
		this.parentNode = parentNode;
	}
	
	public void addAssociation(IOntoAssociation association) {
		associations.add(association);
	}
	
	public ArrayList<IOntoAssociation> getAssociations(){
		return associations;
	}
	
	public ArrayList<IOntoAssociation> getAssociationsOfType(AssociationType type) {
		ArrayList<IOntoAssociation> array = new ArrayList<IOntoAssociation>();
		
		for(IOntoAssociation association : associations) {
			if(association.getAssociationType() == type)
				array.add(association);
		}
		return array;
	}
	
	public void removeAndDestroyAssociations() {
		IOntoAssociation association;
		while( ! associations.isEmpty() ) {
			association = associations.get(0);
			associations.remove(0);
			association.removeAssociation();
		}
	}
	
	public void removeAssociationWith(INode node) {
		int i = 0;
		IOntoAssociation association;
		while ( i < associations.size()) {
			association = associations.get(i);
			if(association.getAssociationType() != AssociationType.GENERALIZATION_SET) {
				if( 	association.getSourceNode() == node ||
						association.getTargetNode() == node) {
					associations.remove(i);
				}
			}
			i++;
		}	
	}
	
	public void removeAssociation( IOntoAssociation association) {
		int i = 0;
		IOntoAssociation obj;
		while ( i < associations.size()) {
			obj = associations.get(i);
			if( obj == association ) {
				associations.remove(i);
				return;
			}
			i++;
		}
	}
	
	public void removeSpecializationWith(INode node) {
		int i = 0;
		IOntoAssociation association;
		
		while(i < associations.size()) {
			association = associations.get(i);
			if(association.getAssociationType() == AssociationType.GENERALIZATION) {
				if ( ((OntoGeneralization)association).getTargetNode()== node) {
					associations.remove(i);
					i--;//the array size decreased
				}
			}
			i++;
		}
	}
	
	public boolean existsGeneralization( IOntoAssociation generalization ) {
		
		for (IOntoAssociation association : associations) {
			if ( association.isAssociationType(AssociationType.GENERALIZATION )) {
				if ( ( (IOntoGeneralization)association ).isTheSameThat(generalization) ) {
					return true;
				}
			}
		}
		return false;
	}
	
	private IOntoGeneralizationSet getGeneralizationSetWith(INode specialization) {
		//locking for nodes with generalization set association (can be more the one)   
		for (IOntoAssociation association : associations) {
			if(association.isAssociationType(AssociationType.GENERALIZATION_SET)) {
				if ( ((IOntoGeneralizationSet)association).existsSpecializationOf(specialization) ) {
					return (IOntoGeneralizationSet)association;
				}
			}
		}
		return null;
	}
	
	public IOntoGeneralizationSet getGeneralizationSetWith(ArrayList<INode> nodes) {
		IOntoGeneralizationSet gs = null;
		
		for(INode obj : nodes) {
			gs = getGeneralizationSetWith(obj);
			if( gs != null )
				return gs;
		}
		return null;
	}
	
	public boolean isSpecialization() {
		
		for(IOntoAssociation association : associations) {
			if ( association.getAssociationType() == AssociationType.GENERALIZATION ) {
				if( ((OntoGeneralization)association).isSpecialization(parentNode) ) {
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean hasSpecialization() {
		
		for (IOntoAssociation association : associations) {
			if( association.getAssociationType() == AssociationType.GENERALIZATION  ||
				association.getAssociationType() == AssociationType.GENERALIZATION_SET
				){
				//If it is the generalist node, it has specialization
				if( association.getSourceNode() == parentNode) {
					return true;
				}
			}
		}
		return false;
	}
	
	public INode getNodeAssociated(String nodeAssociated) {
		
		for (IOntoAssociation association : associations) {
			if( association.getAssociationType() == AssociationType.ASSOCIATION	){
				
				if( association.getSourceNode().getName().equalsIgnoreCase(nodeAssociated) )
					return association.getSourceNode();
				else if( association.getTargetNode().getName().equalsIgnoreCase(nodeAssociated) )
					return association.getSourceNode();
			}
			if( association.getAssociationType() == AssociationType.GENERALIZATION_SET	){
				IOntoGeneralizationSet gs = (IOntoGeneralizationSet) association;
				
				for(INode node : gs.getSpecializationNodes()) {
					if(node.getName().equalsIgnoreCase(nodeAssociated))
						return node;
				}
			}
		}
		return null;
	}
	
	public String toString() {
		String msg = "";
		for (IOntoAssociation association : associations) {
			msg += association;
		}	
		return msg;
	}

	public void setResolved(boolean flag) {
		for(IOntoAssociation association : associations) {
			association.setResolved(flag);
		}
	}
}
