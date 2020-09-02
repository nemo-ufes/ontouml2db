/**
 * 
 * 
 * Author: João Paulo A. Almeida; Gustavo L. Guidoni
 * 
 */
package br.ufes.inf.nemo.ontouml2db.graph;

public enum UfoStereotype {
	
	CATEGORY("CATEGORY"), 
	ROLEMIXIN("ROLEMIXIN"),
	PHASEMIXIN("PHASEMIXIN"), 
	MIXIN("MIXIN"),
	
	ROLE("ROLE"), 
	PHASE("PHASE"),
	SUBKIND("SUBKIND"),
	
	KIND("KIND"), 
	QUANTITY("QUANTITY"), 
	COLLECTIVE("COLLECTIVE"), 
	RELATOR("RELATOR"), 
	MODE("MODE");
//	PERCEIVABLE_QUALITY, 
//	NONPERCEIVABLE_QUALITY, 
//	NOMINAL_QUALITY,
	
	private String name;

	UfoStereotype(String name)	{
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public static boolean isUFOClass(String value) {
		for (UfoStereotype item : UfoStereotype.values()) {
			if(item.getName().compareToIgnoreCase(value)==0)
				return true;
		}
		return false;
	}
	
	/**
	 * Checks whether stereotype is a non-sortal.
	 * 
	 * @param stereotype. Stereotype to be checked.
	 * @return True if the class is a kind, otherwise false.
	 */
	public static boolean isNonSortal(String stereotype) {
		return 	stereotype.equalsIgnoreCase("CATEGORY") || 
				stereotype.equalsIgnoreCase("ROLEMIXIN") ||
				stereotype.equalsIgnoreCase("PHASEMIXIN") || 
				stereotype.equalsIgnoreCase("MIXIN");
	}
	
	/**
	 * Checks whether stereotype is a sortal non-kind.
	 * 
	 * @param stereotype. Stereotype to be checked
	 * @return True if the class is a sortal non-kind, otherwise false.
	 */
	public static boolean isSortalNonKind(String stereotype) {
		return 	stereotype.equalsIgnoreCase("ROLE") || 
				stereotype.equalsIgnoreCase("PHASE") || 
				stereotype.equalsIgnoreCase("SUBKIND");
	}
}
