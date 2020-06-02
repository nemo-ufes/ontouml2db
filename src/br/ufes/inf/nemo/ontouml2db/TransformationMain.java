/**
 * Transforms an OntoUML model into a model ready for final transformation into a relational schema.
 * 
 * The one table per kind approach is used; all non-sortals are flattened to kinds, and sortals lifted to kinds.
 * 
 * For use with OntoUML profile in https://github.com/nemo-ufes/ufo-types/tree/master/uml2-profile
 * 
 * Author: João Paulo A. Almeida 
 * 
 */

package br.ufes.inf.nemo.ontouml2db;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Enumeration;
import org.eclipse.uml2.uml.Generalization;
import org.eclipse.uml2.uml.GeneralizationSet;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Stereotype;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.PackageableElement;
import org.eclipse.uml2.uml.resources.util.UMLResourcesUtil;

public class TransformationMain {

	public static void main(String[] args) {

		if (args.length < 1) {
			System.out.println("usage: java br.ufes.inf.nemo.ontouml2db.TransformationMain source_file.uml [target_file.uml]");
			System.out.println("when target_file.uml is omitted, default is 'output.uml'");
			return;
		}

		String filename = args[0];
		String outputfilename = (args.length >= 2) ? args[1] : "output.uml";

		File sourceFile = new File(filename);
		if (!sourceFile.isFile()) {
			System.out.println("Error accessing: " + sourceFile.getAbsolutePath());
			System.exit(1);
		}
		
		Resource resource;
		ResourceSet resourceSet = new ResourceSetImpl();
		UMLResourcesUtil.init(resourceSet);

		URI uri = URI.createFileURI(sourceFile.getAbsolutePath());

		try {
			resource = resourceSet.getResource(uri, true);

			Package p = (Package) resource.getContents().get(0);
			handlePackage(p);

			File outputfile = new File(outputfilename);
			resource.save(new FileOutputStream(outputfile), null);
			System.out.println("Produced output.uml");

		} catch (Exception e) {
			System.out.println("This is a research prototype, under development.");
			e.printStackTrace();
			return;
		}
	}

	private static void handlePackage(Package p) {
		System.out.println("Handling package " + p.getName());

		// flattens all top-level non-sortals
		org.eclipse.uml2.uml.Class topns = findToplevelNonSortal(p);
		while (topns != null) {
			flattenClass(p, topns);
			topns = findToplevelNonSortal(p);
		}

		// lifts all leaf sortals
		org.eclipse.uml2.uml.Class s = findLeafSortalNonKind(p);
		while (s != null) {
			liftClass(p, s);
			s = findLeafSortalNonKind(p);
		}

	}

	/**
	 * Checks whether class is a sortal non-kind.
	 * 
	 * @param c the class to be checked
	 * @return true is class is a sortal non-kind, false otherwise
	 */
	private static boolean isSortalNonKind(Class c) {
		Stereotype s = c.getAppliedStereotypes().get(0);
		if (s == null)
			return false;
		else
			return s.getName().equals("role") || s.getName().equals("phase") || s.getName().equals("subkind");
	}

	/**
	 * Checks whether class is a non-sortal.
	 * 
	 * @param c the class to be checked
	 * @return true is class is a non-sortal, false otherwise
	 */
	private static boolean isNonSortal(Class c) {
		Stereotype s = c.getAppliedStereotypes().get(0);
		if (s == null)
			return false;
		else
			return s.getName().equals("category") || s.getName().equals("roleMixin") || s.getName().equals("phaseMixin")
					|| s.getName().equals("mixin");
	}

	
	/**
	 * Finds a leaf sortal (non-kind) in a package.
	 * 
	 * @param p the UML package where to search
	 * @return a leaf sortal from the package, or null if none can be found
	 */
	private static Class findLeafSortalNonKind(Package p) {
		for (PackageableElement e : p.getPackagedElements()) {
			if (e instanceof Class) {
				Class c = (Class) e;

				if (isSortalNonKind(c) && getSubClasses(p, c).isEmpty())
					return c;
			}
		}
		return null;
	}


	/**
	 * Finds a top-level non-sortal in a package.
	 * 
	 * @param p the UML package where to search
	 * @return a top-level non-sortalfrom the package, or null if none can be found
	 */
	private static Class findToplevelNonSortal(Package p) {
		for (PackageableElement e : p.getPackagedElements()) {
			if (e instanceof Class) {
				Class c = (Class) e;

				if (isNonSortal(c) && c.getGeneralizations().isEmpty())
					return c;
			}
		}
		return null;
	}

	/**
	 * Lifts the UML class.
	 * 
	 * All the superclasses of the class receive the attributes of the lifted class.
	 * Mandatory attributes become optional (lower bound 0). (The superclass may
	 * already have the attribute, in which case no duplicate is created).
	 * 
	 * Any association ends typed with the lifted class must be retyped to the
	 * superclass. In this case, the opposite end receives lower bound 0.
	 *
	 * When a generalization is part of a generalization sets, all siblings are
	 * lifted, and a discriminator is created. Otherwise, a boolean attribute is
	 * created to indicate whether the instance of the superclass is an instance of
	 * the lifted subclass.
	 * 
	 * In case of multiple inheritance, each superclass is treated alike.
	 * 
	 * The lifted class is finally removed.
	 * 
	 * @param p the UML package containing the class to be lifted
	 * @param c the UML class to be lifted
	 */
	public static void liftClass(Package p, org.eclipse.uml2.uml.Class c) {

		Set<Generalization> generalizationsToDestroy = new HashSet<>();
		for (Generalization g : c.getGeneralizations()) {

			if (g.getGeneral() instanceof Class) {
				generalizationsToDestroy.add(g);
				
				Class superclass = (Class) g.getGeneral();

				System.out.println("Lifting class " + c.getName() + " to " + superclass.getName());

				// attributes are copied
				Set<Property> propertiesToDestroy = new HashSet<>();
				for (Property property : c.getOwnedAttributes()) {

					// ignore attributes that are parts of associations
					if (property.getOtherEnd()==null) {
						if (superclass.getAttribute(property.getName(), property.getType()) == null)
							superclass.createOwnedAttribute(property.getName(), property.getType(), 0, property.getUpper());					
					}
				}
				for (Property prop : propertiesToDestroy) prop.destroy();
				
				// associations are processed
				Set<Association> associationsToDestroy = new HashSet<>();
				for (Association a : c.getAssociations()) {
					associationsToDestroy.add(a);
					
					// FIXME: check if association already exists
					// check if works for self-relationships
					if (a.getMemberEnds().get(1).getType().equals(c)) 
					{
						// if end 1 is the class being lifted, then we need to create the copy in the superclass
						// end 0 is the opposite end, which receives optional cardinality
						Association createdAssociation=superclass.createAssociation(
						a.getMemberEnds().get(0).isNavigable(),
						a.getMemberEnds().get(0).getAggregation(), 
						a.getMemberEnds().get(0).getName(), 
						0,
						a.getMemberEnds().get(0).getUpper(),
						a.getMemberEnds().get(0).getType(),
						a.getMemberEnds().get(1).isNavigable(),
						a.getMemberEnds().get(1).getAggregation(),
						a.getMemberEnds().get(1).getName(), 
						a.getMemberEnds().get(1).getLower(),
						a.getMemberEnds().get(1).getUpper());
						createdAssociation.setName(a.getName());
					} else
						// 
						if (a.getMemberEnds().get(0).getType().equals(c)) {
							// if end 0 is the class being lifted, then we need to create the copy in the 
							// other class involved in the association (the type of end 1)
							// end 1 is the opposite end, which receives optional cardinality
							Association createdAssociation=a.getMemberEnds().get(1).getType().createAssociation(							
									a.getMemberEnds().get(0).isNavigable(),
									a.getMemberEnds().get(0).getAggregation(), 
									a.getMemberEnds().get(0).getName(), 
									a.getMemberEnds().get(0).getLower(),
									a.getMemberEnds().get(0).getUpper(),
									superclass,
									a.getMemberEnds().get(1).isNavigable(),
									a.getMemberEnds().get(1).getAggregation(),
									a.getMemberEnds().get(1).getName(), 
									0,
									a.getMemberEnds().get(1).getUpper());
									createdAssociation.setName(a.getName());							
						}
					
					
				}
				for (Association a : associationsToDestroy) {
					a.getMemberEnds().get(0).destroy();
					a.getMemberEnds().get(0).destroy();
					a.destroy();
				}
				

				if (g.getGeneralizationSets().size() > 0) {
					// case involves a generalization set, and we need to create discriminator
					Set<GeneralizationSet> toDestroy=new HashSet<>();
					for (GeneralizationSet gs : g.getGeneralizationSets()) {
						String enumname = gs.getName().substring(0, 1).toUpperCase() + gs.getName().substring(1)
								+ "Enum";
						if (p.getOwnedType(enumname) == null) {
							Enumeration e = p.createOwnedEnumeration(enumname);
							for (Classifier subclass : gs.getGeneralizations().stream().map((x) -> x.getSpecific())
									.collect(Collectors.toList())) {
								e.createOwnedLiteral(subclass.getName().toUpperCase());
							}
							superclass.createOwnedAttribute(gs.getName(), e, gs.isCovering() ? 1 : 0,
									gs.isDisjoint() ? 1 : -1);
						
						}
						toDestroy.add(gs);
					}
					for (GeneralizationSet gs : toDestroy) { gs.destroy(); }

				} else {
					// no genset, no discriminator, only boolean attribute
					// FIXME there may be other imports that are not the primitive types package
					Package primitiveTypes = p.getPackageImports().get(0).getImportedPackage();
					Type booleanprimitivetype = primitiveTypes.getOwnedType("Boolean");
					superclass.createOwnedAttribute("is" + c.getName(), booleanprimitivetype, 1, 1);
				}

				c.destroy();
			}
		}
		for(Generalization g : generalizationsToDestroy) { g.destroy(); }
	}

	/**
	 * Flattens the UML class.
	 * 
	 * All its attributes are copied to the each subclass. (The subclass may already
	 * have the attribute, in which case no duplicate is created).
	 * 
	 * Associations that have an association end typed with the flattened class are
	 * recreated with an association end connecting to each subclass (with lower
	 * bound 0). The name of this association end receives a suffix with the name of
	 * the subclass.
	 * 
	 * (If there are multiple subclasses, all of them receive the flattened
	 * features).
	 * 
	 * The flattened class is finally removed.
	 * 
	 * @param p the UML package containing the class to be flattened
	 * @param c the UML class to be flattened
	 */
	public static void flattenClass(Package p, org.eclipse.uml2.uml.Class c) {
		for (Class subclass : getSubClasses(p, c)) {
			System.out.println("Flattening class " + c.getName()+" to "+subclass.getName());
			
			// attributes are copied
			for (Property property : c.getAttributes()) {
				// ignore attributes that are parts of associations
				if (property.getOtherEnd()==null) {
				if (subclass.getAttribute(property.getName(), property.getType()) == null)
					subclass.createOwnedAttribute(property.getName(), property.getType(), property.getLower(),
							property.getUpper());
				}				
			}
			
			// associations are processed
			Set<Association> associationsToDestroy = new HashSet<>();
			for (Association a : c.getAssociations()) {
				associationsToDestroy.add(a);
							
				// FIXME: check if association already exists
				// check if works for self-relationships
				if (a.getMemberEnds().get(1).getType().equals(c)) 
				{
					// if end 1 is the class being flattened, then we need to create the copy in the subclass
					// end 1 receives optional cardinality
					Association createdAssociation=subclass.createAssociation(
					a.getMemberEnds().get(0).isNavigable(),
					a.getMemberEnds().get(0).getAggregation(), 
					a.getMemberEnds().get(0).getName(), 
					a.getMemberEnds().get(0).getLower(),
					a.getMemberEnds().get(0).getUpper(),
					a.getMemberEnds().get(0).getType(),
					a.getMemberEnds().get(1).isNavigable(),
					a.getMemberEnds().get(1).getAggregation(),
					a.getMemberEnds().get(1).getName(), 
					0,
					a.getMemberEnds().get(1).getUpper());
					createdAssociation.setName(a.getName());
				} else
					// 
					if (a.getMemberEnds().get(0).getType().equals(c)) {
						// if end 0 is the class being flattened, then we need to create the copy in the 
						// other class involved in the association (the type of end 1)
						// end 0 receives optional cardinality
						Association createdAssociation=a.getMemberEnds().get(1).getType().createAssociation(							
								a.getMemberEnds().get(0).isNavigable(),
								a.getMemberEnds().get(0).getAggregation(), 
								a.getMemberEnds().get(0).getName(), 
								0,
								a.getMemberEnds().get(0).getUpper(),
								subclass,
								a.getMemberEnds().get(1).isNavigable(),
								a.getMemberEnds().get(1).getAggregation(),
								a.getMemberEnds().get(1).getName(), 
								a.getMemberEnds().get(1).getLower(),
								a.getMemberEnds().get(1).getUpper());
								createdAssociation.setName(a.getName());							
					}
			}
			for (Association a : associationsToDestroy) {
				a.getMemberEnds().get(0).destroy();
				a.getMemberEnds().get(0).destroy();
				a.destroy();
			}
			
			// now let's destroy the generalizations that point to this class
			// FIXME: if there are generalization sets, they must also be destroyed
			Set<Generalization> toDestroy=new HashSet<>();
			for (Generalization g : subclass.getGeneralizations()) 
			{
				if (g.getGeneral().equals(c)) { toDestroy.add(g); } 
			}
			for (Generalization g : toDestroy) { g.destroy(); }
		}
		// and then the flattened class can be destroyed
		c.destroy();
	}

	/**
	 * Obtains the set of subclasses of a class in a package.
	 * 
	 * @param p the package containing the candidate classes
	 * @param c the superclass
	 * @return the set of subclasses in the package
	 */
	public static Set<Class> getSubClasses(Package p, Class c) {
		// FIXME: could consider other packages
		Set<Class> subclasses = new HashSet<Class>();
		for (PackageableElement e : p.getPackagedElements()) {
			if (e instanceof Class) {
				Class x = (Class) e;
				if (x.getGenerals().contains(c))
					subclasses.add(x);
			}
		}
		return subclasses;
	}

}
