package br.ufes.inf.nemo.ontouml2db.graph;

public enum Cardinality {
	C1 ("1"),		//1
	C0_1("0..1"),	//0..1
	C1_N("1..*"),	//1..*
	C0_N("0..*"),	//0..*
	X("uninformed");		//uninformed
	
	private String name;
	
	Cardinality(String name) {
		this.name = name;
	}
	
	public static Cardinality getCardinality(int min, int max) {
		if(min == 1 && max == 1) return C1;
		if(min == 0 && max == 1) return C0_1;
		if(min == 1 && max == -1) return C1_N;
		if(min == 0 && max == -1) return C0_N;
		return X;
	}
	
	public String toString() {
		return name;
	}
	
}
