# A Transformation from OntoUML into Relational Schemas

This project includes a transformation from OntoUML source model into a target model ready for final transformation into a relational schema. A DDL specification is produced.

The one table per kind approach is used; all non-sortals are flattened to kinds, and sortals lifted to kinds. A forthcoming publication describing the approach is planned.

The UML 2.x profile for class diagrams was implemented in  [Eclipse Papyrus for UML 4.5](https://www.eclipse.org/papyrus/) See https://github.com/nemo-ufes/ufo-types/tree/master/uml2-profile for instructions on installation of the profile.

