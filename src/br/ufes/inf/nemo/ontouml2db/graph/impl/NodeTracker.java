package br.ufes.inf.nemo.ontouml2db.graph.impl;

import java.util.ArrayList;
import java.util.HashMap;

import br.ufes.inf.nemo.ontouml2db.graph.INode;
import br.ufes.inf.nemo.ontouml2db.graph.INodeTracker;
import br.ufes.inf.nemo.ontouml2db.graph.IOntoProperty;
import br.ufes.inf.nemo.ontouml2db.graph.ITracker;
import br.ufes.inf.nemo.ontouml2db.util.Util;

public class NodeTracker implements INodeTracker {
	
	private INode sourceNode;
	private HashMap<String, ITracker> tracker;
	
	public NodeTracker(INode node) {
		sourceNode = node;
		tracker = new HashMap<String, ITracker>();
	}

	public String toString() {
		
		String msg = "Tracker => ";
		ITracker nodeTracker;
		for(String key : tracker.keySet() ) {
			nodeTracker = tracker.get(key);
			//msg += nodeTracker.getNode().getName() + " | ";
			msg += nodeTracker+ " | ";
		}
		//return msg;
		return "Source: "+ sourceNode.getName() + Util.getSpaces(sourceNode.getName(), 18) + msg;
	}
	
	public void addSourceTracking(INode newNodeTracker) {
		ITracker auxTracker;
		for(String key : tracker.keySet() ) {
			auxTracker = tracker.get(key);
			auxTracker.getNode().addTracking(newNodeTracker);
		}
	}
	
	public void addTracking(INode newNodeTracker) {
		String key = newNodeTracker.getName().toUpperCase();
		if( ! tracker.containsKey( key ) ) {
			tracker.put(key, new Tracker(newNodeTracker, null, null));
		}
	}
	
	public void addTracking(ArrayList<ITracker> trackers) {
		String key;
		for(ITracker obj : trackers) {
			key = obj.getNode().getName().toUpperCase();
			if( ! tracker.containsKey( key ) ) {
				tracker.put(key, obj );
			}
		}
	}
	
	public void removeSourceTracking(  ) {
		ITracker nodeTracker;
		for(String key : tracker.keySet() ) {
			nodeTracker = tracker.get(key);
			nodeTracker.getNode().removeTracking(sourceNode);
		}
	}
	
	public void removeTracking(INode node) {
		String key = node.getName().toUpperCase();
		tracker.remove(key);
	}
	
	public void changeSourceTracking(INode newNodeTracker) {
		ITracker nodeTracker;
		for(String key : tracker.keySet() ) {
			nodeTracker = tracker.get(key);
			nodeTracker.getNode().changeTracking(sourceNode, newNodeTracker);
			newNodeTracker.addTracking(nodeTracker.getNode());
		}
	}
	
	public void changeTracking(INode oldNodeTracker, INode newNodeTracker) {
		String key = oldNodeTracker.getName().toUpperCase();
		ITracker nodeTracker = tracker.remove(key);
		nodeTracker.setNode(newNodeTracker);
		key = newNodeTracker.getName().toUpperCase();
		tracker.put(key, nodeTracker);
	}
	
	public void setSourceTrackerField(IOntoProperty property, Object value) {
		ITracker nodeTracker;
		for(String key : tracker.keySet() ) {
			nodeTracker = tracker.get(key);
			if(nodeTracker != null) {
				//The attribute tracking  must be performed only for the respective 
				//node (this) and not for the inheritance hierarchy.
				if(nodeTracker.getNode().getName().equalsIgnoreCase( sourceNode.getName() ) ){
					nodeTracker.getNode().setTrackerField(sourceNode, property, value);
				}
			}
		}
	}
	
	public void setTrackerField(INode node, IOntoProperty property, Object value) {
		String key = node.getName().toUpperCase();
		ITracker nodeTracker = tracker.get(key);
		if(nodeTracker != null) {
			//The node reference must be changed by the lifting process.
			nodeTracker.setProperty(property);
			nodeTracker.setValue(value);
		}
	}
	
	public void setSourcePropertyLinkedAtNode(INode linkedNode) {
		ITracker nodeTracker;
		for(String key : tracker.keySet() ) {
			nodeTracker = tracker.get(key);
			if(nodeTracker != null) {
				//The attribute tracking  must be performed only for the respective 
				//node (this) and not for the inheritance hierarchy.
				if(nodeTracker.getNode().getName().equalsIgnoreCase( sourceNode.getName() ) ){
					nodeTracker.getNode().setPropertyLinkedAtNode(sourceNode, linkedNode);
				}
			}
		}
	}
	
	public void setPropertyLinkedAtNode(INode node, INode linkedNode) {
		String key = node.getName().toUpperCase();
		ITracker nodeTracker = tracker.get(key);
		if(nodeTracker != null) {
			//The node reference must be changed by the lifting process.
			nodeTracker.setPropertyLinkedAtNode(linkedNode);
		}
	}
	
	public void removeSourcePropertyLinkedAtNode(String nodeName) {
		ITracker nodeTracker;
		for(String key : tracker.keySet() ) {
			nodeTracker = tracker.get(key);
			if(nodeTracker != null) {
				nodeTracker.getNode().removePropertyLinkedAtNode(nodeName);
			}
		}
	}
	
	public void removePropertyLinkedAtNode(String nodeName) {
		ITracker nodeTracker;
		for(String key : tracker.keySet() ) {
			nodeTracker = tracker.get(key);
			if(nodeTracker.getPropertyLinkedAtNode() != null) {
				if(nodeTracker.getPropertyLinkedAtNode().getName().equalsIgnoreCase(nodeName)) {
					nodeTracker.setPropertyLinkedAtNode(null);
				}
			}
		}
	}
	
	public int getAmountNodesTracked() {
		return tracker.size();
	}
	
	public String getTargetColumnName(String field) {
		ITracker nodeTracker;
		for(String key : tracker.keySet() ) {
			nodeTracker = tracker.get(key);
			for(IOntoProperty property : nodeTracker.getNode().getProperties()) {
				if( property.getName().equalsIgnoreCase(field) )
					return property.getColumnName();
			}
		}
		return "[the column "+ field.toUpperCase() +" was not found]";
	}
	
	public String getTargetPKName() {
		ITracker nodeTracker;
		for(String key : tracker.keySet() ) {
			nodeTracker = tracker.get(key);
			for(IOntoProperty property : nodeTracker.getNode().getProperties()) {
				if( property.isPrimaryKey() )
					return property.getColumnName();
			}
		}
		return "[the primary key was not found for "+sourceNode.getName().toUpperCase()+" ]";
	}
	
	public ArrayList<ITracker> getTrackers(){
		ArrayList<ITracker> array = new ArrayList<ITracker>();
		ITracker nodeTracker;
		for(String key : tracker.keySet() ) {
			nodeTracker = tracker.get(key);
			array.add(nodeTracker);
		}
		return array;
	}
}
