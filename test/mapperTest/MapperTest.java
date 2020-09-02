package mapperTest;


import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import br.ufes.inf.nemo.ontouml2db.Mapper;
import br.ufes.inf.nemo.ontouml2db.casting.schema.DBMS;
import br.ufes.inf.nemo.ontouml2db.graph.Cardinality;
import br.ufes.inf.nemo.ontouml2db.strategies.Strategy;
import util.RelationshipTest;
import util.GraphTest;
import util.TableTest;
import util.TrackerTest;

public class MapperTest {

	private static String path = "\\test\\umlFiles\\";
	private static String pathOutput = "c:\\_Results\\";
	private static DBMS dbms = DBMS.H2;
	
	@BeforeClass
    public static void setup() {
		String curDir = System.getProperty("user.dir");
		path = curDir + path;
    }
	
	@Test
	public void SimpleFlattening() {
		try {
			Mapper map = new Mapper.Builder()
					.setUmlFile(path+"Test_01.uml")
					.setDestinationPath(pathOutput)
					.setStrategy(Strategy.ONE_TABLE_PER_KIND)
					.setDBMS(dbms)
					.build();
			
			map.doMapping();
			
			GraphTest test = new GraphTest.Builder()
					.setSourceGraph(map.getSourceGraph())
					.setTargetGraph(map.getTargetGraph())
					.addTable(new TableTest("person", new String[] {"person_id", "name", "birth_date"}) )
					.addTracker(new TrackerTest("NamedEntity", "Person"))
					.addTracker(new TrackerTest("Person", "Person"))
					.build();
			
			String result = test.match();
			
			if( !result.isEmpty() ) {
				map.showSourceNodes();
				map.showTargetNodes();
				map.showSourceTracking();
				System.out.println(result);
				fail("SimpleFlattening: "+ result);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			fail("SimpleFlattening");
		}
	}
	
	@Test
	public void FlattingDuplicateAttribures() {
		try {
			Mapper map = new Mapper.Builder()
					.setUmlFile(path+"Test_02.uml")
					.setDestinationPath(pathOutput)
					.setStrategy(Strategy.ONE_TABLE_PER_KIND)
					.setDBMS(dbms)
					.build();
			
			map.doMapping();
			
			GraphTest test = new GraphTest.Builder()
					.setSourceGraph(map.getSourceGraph())
					.setTargetGraph(map.getTargetGraph())
					.addTable(new TableTest("person", new String[] {"person_id", "x1", "x2", "x3"}) )
					.addTracker(new TrackerTest("NamedEntity", "Person"))
					.addTracker(new TrackerTest("Person", "Person"))
					.build();
			
			String result = test.match();
			
			if( !result.isEmpty() ) {
				map.showSourceNodes();
				map.showTargetNodes();
				map.showSourceTracking();
				System.out.println(result);
				fail("FlattingDuplicateAttribures: "+ result);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			fail("FlattingDuplicateAttribures");
		}
	}
	
	@Test
	public void FlattingGeneralizationSet() {
		try {
			Mapper map = new Mapper.Builder()
					.setUmlFile(path+"Test_03.uml")
					.setDestinationPath(pathOutput)
					.setStrategy(Strategy.ONE_TABLE_PER_KIND)
					.setDBMS(dbms)
					.build();
			
			map.doMapping();
			
			GraphTest test = new GraphTest.Builder()
					.setSourceGraph(map.getSourceGraph())
					.setTargetGraph(map.getTargetGraph())
					.addTable(new TableTest("organization", new String[] {"organization_id", "name", "address"}) )
					.addTable(new TableTest("person", new String[] {"person_id", "name", "birth_date"}) )
					.addTracker(new TrackerTest("NamedEntity", "Person"))
					.addTracker(new TrackerTest("NamedEntity", "Organization"))
					.addTracker(new TrackerTest("Person", "Person"))
					.addTracker(new TrackerTest("Organization", "Organization"))
					.build();
			
			String result = test.match();
			
			if( !result.isEmpty() ) {
				map.showSourceNodes();
				map.showTargetNodes();
				map.showSourceTracking();
				System.out.println(result);
				fail("GeneralizationSet: "+ result);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			fail("FlattingGeneralizationSet");
		}
	}
	
	@Test
	public void FlattingMultiplesGeneralizations() {
		try {
			Mapper map = new Mapper.Builder()
					.setUmlFile(path+"Test_04.uml")
					.setDestinationPath(pathOutput)
					.setStrategy(Strategy.ONE_TABLE_PER_KIND)
					.setDBMS(dbms)
					.build();
			
			map.doMapping();
			
			GraphTest test = new GraphTest.Builder()
					.setSourceGraph(map.getSourceGraph())
					.setTargetGraph(map.getTargetGraph())
					.addTable(new TableTest("organization", new String[] {"organization_id", "name", "address"}) )
					.addTable(new TableTest("person", new String[] {"person_id", "name", "birth_date"}) )
					.addTable(new TableTest("test", new String[] {"test_id", "name"}) )
					.addTracker(new TrackerTest("NamedEntity", "Person"))
					.addTracker(new TrackerTest("NamedEntity", "Organization"))
					.addTracker(new TrackerTest("NamedEntity", "Test"))
					.addTracker(new TrackerTest("Person", "Person"))
					.addTracker(new TrackerTest("Organization", "Organization"))
					.addTracker(new TrackerTest("Test", "Test"))
					.build();
			
			String result = test.match();
			
			if( !result.isEmpty() ) {
				map.showSourceNodes();
				map.showTargetNodes();
				map.showSourceTracking();
				System.out.println(result);
				fail("OrthogonalGeneralizations: "+ result);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			fail("FlattingMultiplesGeneralizations");
		}
	}
	
	@Test
	public void FlattingOrthogonalGeneralizationSets() {
		try {
			Mapper map = new Mapper.Builder()
					.setUmlFile(path+"Test_05.uml")
					.setDestinationPath(pathOutput)
					.setStrategy(Strategy.ONE_TABLE_PER_KIND)
					.setDBMS(dbms)
					.build();
			
			map.doMapping();
			
			GraphTest test = new GraphTest.Builder()
					.setSourceGraph(map.getSourceGraph())
					.setTargetGraph(map.getTargetGraph())
					.addTable(new TableTest("organization", new String[] {"organization_id", "name", "address"}) )
					.addTable(new TableTest("person", new String[] {"person_id", "name", "birth_date"}) )
					.addTable(new TableTest("organization_x", new String[] {"organization_x_id", "name"}) )
					.addTable(new TableTest("person_x", new String[] {"person_x_id", "name"}) )
					.addTable(new TableTest("test_x", new String[] {"test_x_id", "name", "test"}) )
					.addTracker(new TrackerTest("NamedEntity", "Person"))
					.addTracker(new TrackerTest("NamedEntity", "Organization"))
					.addTracker(new TrackerTest("NamedEntity", "Person_X"))
					.addTracker(new TrackerTest("NamedEntity", "Organization_X"))
					.addTracker(new TrackerTest("NamedEntity", "Test_X"))
					.addTracker(new TrackerTest("Person", "Person"))
					.addTracker(new TrackerTest("Organization", "Organization"))
					.addTracker(new TrackerTest("PersonX", "Person_X"))
					.addTracker(new TrackerTest("OrganizationX", "Organization_X"))
					.addTracker(new TrackerTest("TestX", "Test_X"))
					.build();
			
			String result = test.match();
			
			if( !result.isEmpty() ) {
				map.showSourceNodes();
				map.showTargetNodes();
				map.showSourceTracking();
				System.out.println(result);
				fail("FlattingOrthogonalGeneralizationSets: "+ result);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			fail("FlattingOrthogonalGeneralizationSets");
		}
		
	}
	
	@Test
	public void FlattingCascadingGeneralizationSets() {
		try {
			Mapper map = new Mapper.Builder()
					.setUmlFile(path+"Test_06.uml")
					.setDestinationPath(pathOutput)
					.setStrategy(Strategy.ONE_TABLE_PER_KIND)
					.setDBMS(dbms)
					.build();
			
			map.doMapping();
			
			GraphTest test = new GraphTest.Builder()
					.setSourceGraph(map.getSourceGraph())
					.setTargetGraph(map.getTargetGraph())
					.addTable(new TableTest("organization", new String[] {"organization_id", "name", "name_a", "address"}) )
					.addTable(new TableTest("person", new String[] {"person_id", "name", "name_b", "birth_date"}) )
					.addTracker(new TrackerTest("NamedEntity", "Person"))
					.addTracker(new TrackerTest("NamedEntity", "Organization"))
					.addTracker(new TrackerTest("NamedEntityA", "Organization"))
					.addTracker(new TrackerTest("NamedEntityB", "Person"))
					.addTracker(new TrackerTest("Person", "Person"))
					.addTracker(new TrackerTest("Organization", "Organization"))
					.build();
			
			String result = test.match();
			
			if( !result.isEmpty() ) {
				map.showSourceNodes();
				map.showTargetNodes();
				map.showSourceTracking();
				System.out.println(result);
				fail("CascadingGeneralizationSets: "+ result);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			fail("FlattingCascadingGeneralizationSets");
		}
	}
	
	@Test
	public void FlattingCategoryWithoutspecialization() {
		try {
			Mapper map = new Mapper.Builder()
					.setUmlFile(path+"Test_07.uml")
					.setDestinationPath(pathOutput)
					.setStrategy(Strategy.ONE_TABLE_PER_KIND)
					.setDBMS(dbms)
					.build();
			
			map.doMapping();
			
			GraphTest test = new GraphTest.Builder()
					.setSourceGraph(map.getSourceGraph())
					.setTargetGraph(map.getTargetGraph())
					.addTable(new TableTest("organization", new String[] {"organization_id", "name", "address"}) )
					.addTable(new TableTest("person", new String[] {"person_id", "name", "birth_date"}) )
					// De acordo com as regras da UFO "NamedEntityA" não poderia existir no modelo da 
					// forma como foi modelado. Entretanto, se ela está no modelo, irá para o BD
					.addTable(new TableTest("named_entity_a", new String[] {"named_entity_a_id", "name", "name_a"}) )
					.addTracker(new TrackerTest("NamedEntity", "Person"))
					.addTracker(new TrackerTest("NamedEntity", "Organization"))
					.addTracker(new TrackerTest("NamedEntity", "Named_Entity_A"))
					.addTracker(new TrackerTest("NamedEntityA", "Named_Entity_A"))
					.addTracker(new TrackerTest("Person", "Person"))
					.addTracker(new TrackerTest("Organization", "Organization"))
					.build();
			
			String result = test.match();
			
			if( !result.isEmpty() ) {
				map.showSourceNodes();
				map.showTargetNodes();
				map.showSourceTracking();
				System.out.println(result);
				fail("FlattingCategoryWithoutspecialization: "+ result);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			fail("FlattingCategoryWithoutspecialization");
		}
	}
	
	@Test
	public void FlattingRootAssociation() {
		try {
			Mapper map = new Mapper.Builder()
					.setUmlFile(path+"Test_08.uml")
					.setDestinationPath(pathOutput)
					.setStrategy(Strategy.ONE_TABLE_PER_KIND)
					.setDBMS(dbms)
					.build();
			
			map.doMapping();
			
			GraphTest test = new GraphTest.Builder()
					.setSourceGraph(map.getSourceGraph())
					.setTargetGraph(map.getTargetGraph())
					.addTable(new TableTest("organization", new String[] {"organization_id", "name", "address"}) )
					.addTable(new TableTest("person", new String[] {"person_id", "name", "birth_date"}) )
					.addTable(new TableTest("test", new String[] {"test_id", "organization_id", "person_id"}) )
					.addRelationship(new RelationshipTest("Organization", Cardinality.C0_1, "Test", Cardinality.C0_N) )
					.addRelationship(new RelationshipTest("Person", Cardinality.C0_1, "Test", Cardinality.C0_N) )
					.addTracker(new TrackerTest("NamedEntity", "Person"))
					.addTracker(new TrackerTest("NamedEntity", "Organization"))
					.addTracker(new TrackerTest("Person", "Person"))
					.addTracker(new TrackerTest("Organization", "Organization"))
					.addTracker(new TrackerTest("Test", "Test"))
					.build();
			
			String result = test.match();
			
			if( !result.isEmpty() ) {
				map.showSourceNodes();
				map.showTargetNodes();
				map.showSourceTracking();
				System.out.println(result);
				fail("FlattingRootAssociation: "+ result);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			fail("FlattingRootAssociation");
		}
	}
	
	@Test
	public void FlattingCascadingAssociation() {
		try {
			Mapper map = new Mapper.Builder()
					.setUmlFile(path+"Test_09.uml")
					.setDestinationPath(pathOutput)
					.setStrategy(Strategy.ONE_TABLE_PER_KIND)
					.setDBMS(dbms)
					.build();
			
			map.doMapping();
						
			GraphTest test = new GraphTest.Builder()
					.setSourceGraph(map.getSourceGraph())
					.setTargetGraph(map.getTargetGraph())
					.addTable(new TableTest("organization", new String[] {"organization_id", "name", "address"}) )
					.addTable(new TableTest("person", new String[] {"person_id", "name", "birth_date"}) )
					.addTable(new TableTest("test", new String[] {"test_id", "person_b_id", "organization_b_id", "organization_id", "person_id"}) )
					.addTable(new TableTest("organization_b", new String[] {"organization_b_id", "name"}) )
					.addTable(new TableTest("person_b", new String[] {"person_b_id", "name"}) )
					.addRelationship(new RelationshipTest("Organization", Cardinality.C0_1, "Test", Cardinality.C0_N) )
					.addRelationship(new RelationshipTest("Person",Cardinality.C0_1, "Test", Cardinality.C0_N) )
					.addRelationship(new RelationshipTest("Organization_B", Cardinality.C0_1, "Test", Cardinality.C0_N) )
					.addRelationship(new RelationshipTest("Person_B", Cardinality.C0_1, "Test", Cardinality.C0_N) )
					.addTracker(new TrackerTest("NamedEntity", "Person"))
					.addTracker(new TrackerTest("NamedEntity", "Organization"))
					.addTracker(new TrackerTest("NamedEntity", "Person_B"))
					.addTracker(new TrackerTest("NamedEntity", "Organization_B"))
					.addTracker(new TrackerTest("NamedEntityB", "Person_B"))
					.addTracker(new TrackerTest("NamedEntityB", "Organization_B"))
					.addTracker(new TrackerTest("Person", "Person"))
					.addTracker(new TrackerTest("Organization", "Organization"))
					.addTracker(new TrackerTest("PersonB", "Person_B"))
					.addTracker(new TrackerTest("OrganizationB", "Organization_B"))
					.addTracker(new TrackerTest("Test", "Test"))
					.build();
			
			String result = test.match();			
			
			if( !result.isEmpty() ) {
				map.showSourceNodes();
				map.showTargetNodes();
				map.showSourceTracking();
				System.out.println(result);
				fail("FlattingCascadingAssociation: "+ result);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			fail("FlattingCascadingAssociation");
		}
	}
	
	@Test
	public void FlattingCascadingAssociationWithMultiplesGeneralization() {
		try {
			Mapper map = new Mapper.Builder()
					.setUmlFile(path+"Test_10.uml")
					.setDestinationPath(pathOutput)
					.setStrategy(Strategy.ONE_TABLE_PER_KIND)
					.setDBMS(dbms)
					.build();
			
			map.doMapping();
						
			GraphTest test = new GraphTest.Builder()
					.setSourceGraph(map.getSourceGraph())
					.setTargetGraph(map.getTargetGraph())
					.addTable(new TableTest("organization_a", new String[] {"organization_a_id", "name", "address"}) )
					.addTable(new TableTest("organization_b", new String[] {"organization_b_id", "name"}) )
					.addTable(new TableTest("organization_c", new String[] {"organization_c_id", "name"}) )
					.addTable(new TableTest("organization_d", new String[] {"organization_d_id", "name"}) )
					.addTable(new TableTest("test", new String[] {"test_id", "test1", "organization_d_id", "organization_c_id", "organization_b_id", "organization_a_id"}) )
					.addRelationship(new RelationshipTest("Organization_A", Cardinality.C0_1, "Test", Cardinality.C0_N) )
					.addRelationship(new RelationshipTest("Organization_B", Cardinality.C0_1, "Test", Cardinality.C0_N) )
					.addRelationship(new RelationshipTest("Organization_C", Cardinality.C0_1, "Test", Cardinality.C0_N) )
					.addRelationship(new RelationshipTest("Organization_D", Cardinality.C0_1, "Test", Cardinality.C0_N) )
					.addTracker(new TrackerTest("NamedEntity", "Organization_A"))
					.addTracker(new TrackerTest("NamedEntity", "Organization_B"))
					.addTracker(new TrackerTest("NamedEntity", "Organization_C"))
					.addTracker(new TrackerTest("NamedEntity", "Organization_D"))
					.addTracker(new TrackerTest("OrganizationA", "Organization_A"))
					.addTracker(new TrackerTest("OrganizationB", "Organization_B"))
					.addTracker(new TrackerTest("OrganizationC", "Organization_C"))
					.addTracker(new TrackerTest("OrganizationD", "Organization_D"))
					.addTracker(new TrackerTest("Test", "Test"))
					.build();
			
			String result = test.match();
			
			if( !result.isEmpty() ) {
				map.showSourceNodes();
				map.showTargetNodes();
				map.showSourceTracking();
				System.out.println(result);
				fail("FlattingCascadingAssociationWithMultiplesGeneralization: "+ result);
			}
		} catch (Exception e) {
			e.printStackTrace();
			fail("FlattingCascadingAssociationWithMultiplesGeneralization");
		}
	}
	
	@Test
	public void FlattingCascadingAssociationWithMultiplesGeneralizationSet() {
		try {
			Mapper map = new Mapper.Builder()
					.setUmlFile(path+"Test_11.uml")
					.setDestinationPath(pathOutput)
					.setStrategy(Strategy.ONE_TABLE_PER_KIND)
					.setDBMS(dbms)
					.build();
			
			map.doMapping();
						
			GraphTest test = new GraphTest.Builder()
					.setSourceGraph(map.getSourceGraph())
					.setTargetGraph(map.getTargetGraph())
					.addTable(new TableTest("organization_a", new String[] {"organization_a_id", "name"}) )
					.addTable(new TableTest("organization_c", new String[] {"organization_c_id", "name", "address"}) )
					.addTable(new TableTest("organization_d", new String[] {"organization_d_id", "name"}) )
					.addTable(new TableTest("organization_e", new String[] {"organization_e_id", "name"}) )
					.addTable(new TableTest("test", new String[] {"test_id", "organization_e_id", "organization_d_id", "organization_c_id", "organization_a_id"}) )
					.addRelationship(new RelationshipTest("Organization_A", Cardinality.C0_1, "Test", Cardinality.C0_N) )
					.addRelationship(new RelationshipTest("Organization_C", Cardinality.C0_1, "Test", Cardinality.C0_N) )
					.addRelationship(new RelationshipTest("Organization_D", Cardinality.C0_1, "Test", Cardinality.C0_N) )
					.addRelationship(new RelationshipTest("Organization_E", Cardinality.C0_1, "Test", Cardinality.C0_N) )
					.addTracker(new TrackerTest("NamedEntity", "Organization_A"))
					.addTracker(new TrackerTest("NamedEntity", "Organization_C"))
					.addTracker(new TrackerTest("NamedEntity", "Organization_D"))
					.addTracker(new TrackerTest("NamedEntity", "Organization_E"))
					.addTracker(new TrackerTest("OrganizationA", "Organization_A"))
					.addTracker(new TrackerTest("OrganizationC", "Organization_C"))
					.addTracker(new TrackerTest("OrganizationD", "Organization_D"))
					.addTracker(new TrackerTest("OrganizationE", "Organization_E"))
					.addTracker(new TrackerTest("Test", "Test"))
					.build();
			
			String result = test.match();
			
			if( !result.isEmpty() ) {
				map.showSourceNodes();
				map.showTargetNodes();
				map.showSourceTracking();
				System.out.println(result);
				fail("FlattingCascadingAssociationWithMultiplesGeneralizationSet: "+ result);
			}
		} catch (Exception e) {
			e.printStackTrace();
			fail("FlattingCascadingAssociationWithMultiplesGeneralizationSet");
		}
	}
	
	@Test
	public void SimpleLifting() {
		try {
			Mapper map = new Mapper.Builder()
					.setUmlFile(path+"Test_12.uml")
					.setDestinationPath(pathOutput)
					.setStrategy(Strategy.ONE_TABLE_PER_KIND)
					.setDBMS(dbms)
					.build();
			
			map.doMapping();
						
			GraphTest test = new GraphTest.Builder()
					.setSourceGraph(map.getSourceGraph())
					.setTargetGraph(map.getTargetGraph())
					.addTable(new TableTest("person", new String[] {"person_id", "birth_date", "is_employee", "test"}) )
					.addTracker(new TrackerTest("Person", "Person"))
					.addTracker(new TrackerTest("Employee", "Person"))
					.build();
			
			String result = test.match();
			
			if( !result.isEmpty() ) {
				map.showSourceNodes();
				map.showTargetNodes();
				map.showSourceTracking();
				System.out.println(result);
				fail("SimpleLifting: "+ result);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			fail("SimpleLifting");
		}
	}
	
	@Test
	public void LiftingCascadeGeneralization() {
		try {
			Mapper map = new Mapper.Builder()
					.setUmlFile(path+"Test_13.uml")
					.setDestinationPath(pathOutput)
					.setStrategy(Strategy.ONE_TABLE_PER_KIND)
					.setDBMS(dbms)
					.build();
			
			map.doMapping();
						
			GraphTest test = new GraphTest.Builder()
					.setSourceGraph(map.getSourceGraph())
					.setTargetGraph(map.getTargetGraph())
					.addTable(new TableTest("person", new String[] {"person_id", "birth_date", "is_role_x", "is_employee", "test_employee", "test_role"}) )
					.addTracker(new TrackerTest("Person", "Person"))
					.addTracker(new TrackerTest("RoleX", "Person"))
					.addTracker(new TrackerTest("Employee", "Person"))
					.build();
			
			String result = test.match();
			
			if( !result.isEmpty() ) {
				map.showSourceNodes();
				map.showTargetNodes();
				map.showSourceTracking();
				System.out.println(result);
				fail("LiftingCascadeGeneralization: "+ result);
			}
		} catch (Exception e) {
			e.printStackTrace();
			fail("LiftingCascadeGeneralization");
		}
	}
	
	@Test
	public void LiftingMultiplesGeneralization() {
		try {
			Mapper map = new Mapper.Builder()
					.setUmlFile(path+"Test_14.uml")
					.setDestinationPath(pathOutput)
					.setStrategy(Strategy.ONE_TABLE_PER_KIND)
					.setDBMS(dbms)
					.build();
			
			map.doMapping();
						
			GraphTest test = new GraphTest.Builder()
					.setSourceGraph(map.getSourceGraph())
					.setTargetGraph(map.getTargetGraph())
					.addTable(new TableTest("person", new String[] {"person_id", "birth_date", "is_employee", "is_role_x", "is_role_y", "test1", "test2", "test3"}) )
					.addTracker(new TrackerTest("Person", "Person"))
					.addTracker(new TrackerTest("Employee", "Person"))
					.addTracker(new TrackerTest("RoleX", "Person"))
					.addTracker(new TrackerTest("RoleY", "Person"))
					.build();
			
			String result = test.match();
			
			if( !result.isEmpty() ) {
				map.showSourceNodes();
				map.showTargetNodes();
				map.showSourceTracking();
				System.out.println(result);
				fail("LiftingMultiplesGeneralization: "+ result);
			}
		} catch (Exception e) {
			e.printStackTrace();
			fail("LiftingMultiplesGeneralization");
		}
	}
	
	@Test
	public void LiftingGeneralizationSet() {
		try {
			Mapper map = new Mapper.Builder()
					.setUmlFile(path+"Test_15.uml")
					.setDestinationPath(pathOutput)
					.setStrategy(Strategy.ONE_TABLE_PER_KIND)
					.setDBMS(dbms)
					.build();
			
			map.doMapping();
						
			GraphTest test = new GraphTest.Builder()
					.setSourceGraph(map.getSourceGraph())
					.setTargetGraph(map.getTargetGraph())
					.addTable(new TableTest("person", new String[] {"person_id", "birth_date", "test1", "test2"})
							.addEnumeration("person_type_enum", new String[] {"CHILD", "ADULT"}) )
					.addTracker(new TrackerTest("Person", "Person"))
					.addTracker(new TrackerTest("Adult", "Person"))
					.addTracker(new TrackerTest("Child", "Person"))
					.build();
			
			String result = test.match();
			
			if( !result.isEmpty() ) {
				map.showSourceNodes();
				map.showTargetNodes();
				map.showSourceTracking();
				System.out.println(result);
				fail("LiftingGeneralizationSet: "+ result);
			}
		} catch (Exception e) {
			e.printStackTrace();
			fail("LiftingGeneralizationSet");
		}
	}
	
	@Test
	public void LiftingCascadeGeneralizationWithAssociation() {
		try {
			Mapper map = new Mapper.Builder()
					.setUmlFile(path+"Test_16.uml")
					.setDestinationPath(pathOutput)
					.setStrategy(Strategy.ONE_TABLE_PER_KIND)
					.setDBMS(dbms)
					.build();
			
			map.doMapping();
						
			GraphTest test = new GraphTest.Builder()
					.setSourceGraph(map.getSourceGraph())
					.setTargetGraph(map.getTargetGraph())
					.addTable(new TableTest("person", new String[] {"person_id", "birth_date", "is_adult", "is_employee", "test"}) )
					.addTable(new TableTest("employment", new String[] {"employment_id", "person_id", "salary"}) )
					.addRelationship(new RelationshipTest("Person", Cardinality.C1, "Employment", Cardinality.C0_N ))
					.addTracker(new TrackerTest("Person", "Person"))
					.addTracker(new TrackerTest("Adult", "Person"))
					.addTracker(new TrackerTest("Employee", "Person"))
					.addTracker(new TrackerTest("Employment", "Employment"))
					.build();
			
			String result = test.match();
			
			if( !result.isEmpty() ) {
				map.showSourceNodes();
				map.showTargetNodes();
				map.showSourceTracking();
				System.out.println(result);
				fail("LiftingCascadeGeneralizationWithAssociation: "+ result);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			fail("LiftingCascadeGeneralizationWithAssociation");
		}
	}
	
	@Test
	public void LiftingGeneralizationSetWithAssociation() {
		try {
			Mapper map = new Mapper.Builder()
					.setUmlFile(path+"Test_17.uml")
					.setDestinationPath(pathOutput)
					.setStrategy(Strategy.ONE_TABLE_PER_KIND)
					.setDBMS(dbms)
					.build();
			
			map.doMapping();
						
			GraphTest test = new GraphTest.Builder()
					.setSourceGraph(map.getSourceGraph())
					.setTargetGraph(map.getTargetGraph())
					.addTable(new TableTest("person", new String[] {"person_id", "birth_date", "is_employee"})
							.addEnumeration("person_phase_enum", new String[] {"ADULT", "CHILD"}) )
					.addTable(new TableTest("employment", new String[] {"employment_id", "person_id", "salary"}) )
					.addRelationship( new RelationshipTest("Person", Cardinality.C1, "Employment", Cardinality.C0_N))
					.addTracker(new TrackerTest("Person", "Person"))
					.addTracker(new TrackerTest("Adult", "Person"))
					.addTracker(new TrackerTest("Child", "Person"))
					.addTracker(new TrackerTest("Employee", "Person"))
					.addTracker(new TrackerTest("Employment", "Employment"))
					.build();
			
			String result = test.match();
			
			if( !result.isEmpty() ) {
				map.showSourceNodes();
				map.showTargetNodes();
				map.showSourceTracking();
				System.out.println(result);
				fail("LiftingGeneralizationSetWithAssociation: "+ result);
			}
		} catch (Exception e) {
			e.printStackTrace();
			fail("LiftingGeneralizationSetWithAssociation");
		}
	}
	
	@Test
	public void LiftingOrthogonalGeneralizationSet() {
		try {
			Mapper map = new Mapper.Builder()
					.setUmlFile(path+"Test_18.uml")
					.setDestinationPath(pathOutput)
					.setStrategy(Strategy.ONE_TABLE_PER_KIND)
					.setDBMS(dbms)
					.build();
			
			map.doMapping();
						
			GraphTest test = new GraphTest.Builder()
					.setSourceGraph(map.getSourceGraph())
					.setTargetGraph(map.getTargetGraph())
					.addTable(new TableTest("person", new String[] {"person_id", "birth_date", "ci", "rg"})
							.addEnumeration("person_phase_enum", new String[] {"ADULT", "CHILD", "TEENAGER"}) )
					.addTable(new TableTest("nationality", new String[] {"nationality_id", "person_id"})
							.addEnumeration("nationality_enum", new String[] {"BRAZILIANCITIZEN", "ITALIANCITIZEN"}))
					.addRelationship(new RelationshipTest("Nationality", Cardinality.C0_N, "Person", Cardinality.C1))
					.addTracker(new TrackerTest("Person", "Person"))
					.addTracker(new TrackerTest("BrazilianCitizen", "nationality"))
					.addTracker(new TrackerTest("ItalianCitizen", "nationality"))
					.addTracker(new TrackerTest("Adult", "Person"))
					.addTracker(new TrackerTest("Child", "Person"))
					.addTracker(new TrackerTest("Teenager", "Person"))
					.build();
			
			String result = test.match();
			
			if( !result.isEmpty() ) {
				map.showSourceNodes();
				map.showTargetNodes();
				map.showSourceTracking();
				System.out.println(result);
				fail("LiftingOrthogonalGeneralizationSet: "+ result);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			fail("LiftingOrthogonalGeneralizationSet");
		}
	}
	
	@Test
	public void LiftingGeneralizationAndGeneralizationSet() {
		try {
			Mapper map = new Mapper.Builder()
					.setUmlFile(path+"Test_19.uml")
					.setDestinationPath(pathOutput)
					.setStrategy(Strategy.ONE_TABLE_PER_KIND)
					.setDBMS(dbms)
					.build();
			
			map.doMapping();
			
			GraphTest test = new GraphTest.Builder()
					.setSourceGraph(map.getSourceGraph())
					.setTargetGraph(map.getTargetGraph())
					.addTable(new TableTest("person", new String[] {"person_id", "birth_date", "ci", "rg", "is_brazilian_citizen", "is_italian_citizen"})
						.addEnumeration("person_phase_enum", new String[] {"ADULT", "CHILD", "TEENAGER"}) )
					.addTracker(new TrackerTest("Person", "Person"))
					.addTracker(new TrackerTest("BrazilianCitizen", "Person"))
					.addTracker(new TrackerTest("ItalianCitizen", "Person"))
					.addTracker(new TrackerTest("Adult", "Person"))
					.addTracker(new TrackerTest("Child", "Person"))
					.addTracker(new TrackerTest("Teenager", "Person"))
					.build();
			
			String result = test.match();
			
			if( !result.isEmpty() ) {
				map.showSourceNodes();
				map.showTargetNodes();
				map.showSourceTracking();
				System.out.println(result);
				fail("LiftingGeneralizationAndGeneralizationSet: "+ result);
			}
		} catch (Exception e) {
			e.printStackTrace();
			fail("LiftingGeneralizationAndGeneralizationSet");
		}
	}
	
	@Test
	public void LiftingHierarchyGeneralizationSet() {
		try {
			Mapper map = new Mapper.Builder()
					.setUmlFile(path+"Test_20.uml")
					.setDestinationPath(pathOutput)
					.setStrategy(Strategy.ONE_TABLE_PER_KIND)
					.setDBMS(dbms)
					.build();
			
			map.doMapping();
						
			GraphTest test = new GraphTest.Builder()
					.setSourceGraph(map.getSourceGraph())
					.setTargetGraph(map.getTargetGraph())
					.addTable(new TableTest("person", new String[] {"person_id", "birth_date", "test_teenager_b", "test_adult_a", "is_employee"}) 
							.addEnumeration("person_phase_enum", new String[] {"ADULT", "CHILD", "TEENAGER"})
							.addEnumeration("adult_phase_enum", new String[] {"ADULTA", "ADULTB"})
							.addEnumeration("teenager_phase_enum", new String[] {"TEENAGERA", "TEENAGERb"}))
					.addTable(new TableTest("employment", new String[] {"employment_id", "person_id", "salary"}) )
					.addRelationship(new RelationshipTest("Person", Cardinality.C1, "Employment", Cardinality.C0_N))
					.addTracker(new TrackerTest("Person", "Person"))
					.addTracker(new TrackerTest("Child", "Person"))
					.addTracker(new TrackerTest("Adult", "Person"))
					.addTracker(new TrackerTest("Teenager", "Person"))
					.addTracker(new TrackerTest("AdultA", "Person"))
					.addTracker(new TrackerTest("AdultB", "Person"))
					.addTracker(new TrackerTest("TeenagerA", "Person"))
					.addTracker(new TrackerTest("TeenagerB", "Person"))
					.addTracker(new TrackerTest("Employee", "Person"))
					.addTracker(new TrackerTest("Employment", "Employment"))
					.build();
			
			String result = test.match();
			
			if( !result.isEmpty() ) {
				map.showSourceNodes();
				map.showTargetNodes();
				map.showSourceTracking();
				System.out.println(result);
				fail("LiftingHierarchyGeneralizationSet: "+ result);
			}
		} catch (Exception e) {
			e.printStackTrace();
			fail("LiftingHierarchyGeneralizationSet");
		}
	}
	
	@Test
	public void LiftingGeneralizationAndGeneralizationSetWithAssociation() {
		try {
			Mapper map = new Mapper.Builder()
					.setUmlFile(path+"Test_21.uml")
					.setDestinationPath(pathOutput)
					.setStrategy(Strategy.ONE_TABLE_PER_KIND)
					.setDBMS(dbms)
					.build();
			
			map.doMapping();
						
			GraphTest test = new GraphTest.Builder()
					.setSourceGraph(map.getSourceGraph())
					.setTargetGraph(map.getTargetGraph())
					.addTable(new TableTest("person", new String[] {"person_id", "is_test1", "is_test2", "teste2"})
							.addEnumeration("person_phase_enum", new String[] {"ADULT", "CHILD", "TEENAGER"}))
					.addTable(new TableTest("employment_a", new String[] {"employment_a_id", "person_id"}) )
					.addTable(new TableTest("employment_b", new String[] {"employment_b_id", "person_id"}) )
					.addRelationship(new RelationshipTest("Person", Cardinality.C1, "Employment_A", Cardinality.C0_N))
					.addRelationship(new RelationshipTest("Person", Cardinality.C1, "Employment_B", Cardinality.C0_N))
					.addTracker(new TrackerTest("Person", "Person"))
					.addTracker(new TrackerTest("Child", "Person"))
					.addTracker(new TrackerTest("Teenager", "Person"))
					.addTracker(new TrackerTest("Adult", "Person"))
					.addTracker(new TrackerTest("Test1", "Person"))
					.addTracker(new TrackerTest("Test2", "Person"))
					.addTracker(new TrackerTest("EmploymentA", "Employment_A"))
					.addTracker(new TrackerTest("EmploymentB", "Employment_B"))
					.build();
			
			String result = test.match();
			
			if( !result.isEmpty() ) {
				map.showSourceNodes();
				map.showTargetNodes();
				map.showSourceTracking();
				System.out.println(result);
				fail("LiftingGeneralizationAndGeneralizationSetWithAssociation: "+ result);
			}
		} catch (Exception e) {
			e.printStackTrace();
			fail("LiftingGeneralizationAndGeneralizationSetWithAssociation");
		}
	}
	
	@Test
	public void LiftingGSDisjointComplete() {
		try {
			Mapper map = new Mapper.Builder()
					.setUmlFile(path+"Test_22.uml")
					.setDestinationPath(pathOutput)
					.setStrategy(Strategy.ONE_TABLE_PER_KIND)
					.setDBMS(dbms)
					.build();
			
			map.doMapping();
						
			GraphTest test = new GraphTest.Builder()
					.setSourceGraph(map.getSourceGraph())
					.setTargetGraph(map.getTargetGraph())
					.addTable(new TableTest("super_class", new String[] {"super_class_id"})
							.addEnumeration("super_class_type_enum", new String[] {"SUBCLASS1", "SUBCLASS2"}))
					.addTable(new TableTest("associated_class", new String[] {"associated_class_id", "super_class_id"}) )
					.addRelationship(new RelationshipTest("Super_Class", Cardinality.C1, "Associated_Class", Cardinality.C0_N))
					.addTracker(new TrackerTest("SuperClass", "Super_Class"))
					.addTracker(new TrackerTest("SubClass1", "Super_Class"))
					.addTracker(new TrackerTest("SubClass2", "Super_Class"))
					.addTracker(new TrackerTest("AssociatedClass", "Associated_Class"))
					.build();
			
			String result = test.match();
			
			if( !result.isEmpty() ) {
				map.showSourceNodes();
				map.showTargetNodes();
				map.showSourceTracking();
				System.out.println(result);
				fail("LiftingGSDisjointComplete: "+ result);
			}
		} catch (Exception e) {
			e.printStackTrace();
			fail("LiftingGSDisjointComplete");
		}
	}
	
	@Test
	public void LiftingGSDisjointIncomplete() {
		try {
			Mapper map = new Mapper.Builder()
					.setUmlFile(path+"Test_23.uml")
					.setDestinationPath(pathOutput)
					.setStrategy(Strategy.ONE_TABLE_PER_KIND)
					.setDBMS(dbms)
					.build();
			
			map.doMapping();
						
			GraphTest test = new GraphTest.Builder()
					.setSourceGraph(map.getSourceGraph())
					.setTargetGraph(map.getTargetGraph())
					.addTable(new TableTest("super_class", new String[] {"super_class_id"})
							.addEnumeration("super_class_type_enum", new String[] {"SUBCLASS1", "SUBCLASS2"}))
					.addTable(new TableTest("Associated_class", new String[] {"associated_class_id", "super_class_id"}) )
					.addRelationship(new RelationshipTest("Super_Class", Cardinality.C1, "Associated_Class", Cardinality.C0_N))
					.addTracker(new TrackerTest("SuperClass", "Super_Class"))
					.addTracker(new TrackerTest("SubClass1", "Super_Class"))
					.addTracker(new TrackerTest("SubClass2", "Super_Class"))
					.addTracker(new TrackerTest("AssociatedClass", "Associated_Class"))
					.build();
			
			String result = test.match();
			
			if( !result.isEmpty() ) {
				map.showSourceNodes();
				map.showTargetNodes();
				map.showSourceTracking();
				System.out.println(result);
				fail("LiftingGSDisjointIncomplete: "+ result);
			}
		} catch (Exception e) {
			e.printStackTrace();
			fail("LiftingGSDisjointIncomplete");
		}
	}
	
	@Test
	public void LiftingGSOverlappingComplete() {
		try {
			Mapper map = new Mapper.Builder()
					.setUmlFile(path+"Test_24.uml")
					.setDestinationPath(pathOutput)
					.setStrategy(Strategy.ONE_TABLE_PER_KIND)
					.setDBMS(dbms)
					.build();
			
			map.doMapping();
						
			GraphTest test = new GraphTest.Builder()
					.setSourceGraph(map.getSourceGraph())
					.setTargetGraph(map.getTargetGraph())
					.addTable(new TableTest("super_class", new String[] {"super_class_id"}) )
					.addTable(new TableTest("super_class_type", new String[] {"super_class_type_id", "super_class_id"})
							.addEnumeration("super_class_type_enum", new String[] {"SUBCLASS1", "SUBCLASS2"}) )
					.addTable(new TableTest("associated_class", new String[] {"associated_class_id", "super_class_id"}) )
					.addRelationship(new RelationshipTest("Super_Class", Cardinality.C1, "Associated_Class", Cardinality.C0_N))
					.addRelationship(new RelationshipTest("Super_Class_Type", Cardinality.C1_N, "Super_Class", Cardinality.C1))
					.addTracker(new TrackerTest("SuperClass", "Super_Class"))
					.addTracker(new TrackerTest("SubClass1", "super_class_type"))
					.addTracker(new TrackerTest("SubClass2", "super_class_type"))
					.addTracker(new TrackerTest("AssociatedClass", "Associated_Class"))
					.build();
			
			String result = test.match();
			
			if( !result.isEmpty() ) {
				map.showSourceNodes();
				map.showTargetNodes();
				map.showSourceTracking();
				System.out.println(result);
				fail("LiftingGSOverlappingComplete: "+ result);
			}
		} catch (Exception e) {
			e.printStackTrace();
			fail("LiftingGSOverlappingComplete");
		}
	}
	
	@Test
	public void LiftingGSOverlappingIncomplete() {
		try {
			Mapper map = new Mapper.Builder()
					.setUmlFile(path+"Test_25.uml")
					.setDestinationPath(pathOutput)
					.setStrategy(Strategy.ONE_TABLE_PER_KIND)
					.setDBMS(dbms)
					.build();
			
			map.doMapping();
						
			GraphTest test = new GraphTest.Builder()
					.setSourceGraph(map.getSourceGraph())
					.setTargetGraph(map.getTargetGraph())
					.addTable(new TableTest("super_class", new String[] {"super_class_id"}) )
					.addTable(new TableTest("super_class_type", new String[] {"super_class_type_id", "super_class_id"})
							.addEnumeration("super_class_type_enum", new String[] {"SUBCLASS1", "SUBCLASS2"}) )
					.addTable(new TableTest("associated_class", new String[] {"associated_class_id", "super_class_id"}) )
					.addRelationship(new RelationshipTest("Super_Class", Cardinality.C1, "Associated_Class", Cardinality.C0_N))
					.addRelationship(new RelationshipTest("Super_Class_Type", Cardinality.C0_N, "Super_Class", Cardinality.C1))
					.addTracker(new TrackerTest("SuperClass", "Super_Class"))
					.addTracker(new TrackerTest("SubClass1", "super_class_type"))
					.addTracker(new TrackerTest("SubClass2", "super_class_type"))
					.addTracker(new TrackerTest("AssociatedClass", "Associated_Class"))
					.build();
			
			String result = test.match();
			
			if( !result.isEmpty() ) {
				map.showSourceNodes();
				map.showTargetNodes();
				map.showSourceTracking();
				System.out.println(result);
				fail("LiftingGSOverlappingIncomplete: "+ result);
			}
		} catch (Exception e) {
			e.printStackTrace();
			fail("LiftingGSOverlappingIncomplete");
		}
	}
	
	@Test
	public void FlattingTopClassAssociation() {
		try {
			Mapper map = new Mapper.Builder()
					.setUmlFile(path+"Test_26.uml")
					.setDestinationPath(pathOutput)
					.setStrategy(Strategy.ONE_TABLE_PER_KIND)
					.setDBMS(dbms)
					.build();
			
			map.doMapping();
						
			GraphTest test = new GraphTest.Builder()
					.setSourceGraph(map.getSourceGraph())
					.setTargetGraph(map.getTargetGraph())
					.addTable(new TableTest("person", new String[] {"person_id"}) )
					.addTable(new TableTest("associated_class1", new String[] {"associated_class1_id"}) )
					.addTable(new TableTest("associated_class2", new String[] {"associated_class2_id"}) )
					.addTable(new TableTest("associated_class3", new String[] {"associated_class3_id"}) )
					.addTable(new TableTest("associated_class4", new String[] {"associated_class4_id"}) )
					.addRelationship(new RelationshipTest("Person", Cardinality.C0_N, "Associated_Class1", Cardinality.C0_N))
					.addRelationship(new RelationshipTest("Person", Cardinality.C0_N, "Associated_Class2", Cardinality.C1_N))
					.addRelationship(new RelationshipTest("Person", Cardinality.C0_1, "Associated_Class3", Cardinality.C0_1))
					.addRelationship(new RelationshipTest("Person", Cardinality.C0_1, "Associated_Class4", Cardinality.C1))
					.addTracker(new TrackerTest("NamedEntity", "Person"))
					.addTracker(new TrackerTest("Person", "Person"))
					.addTracker(new TrackerTest("AssociatedClass1", "Associated_Class1"))
					.addTracker(new TrackerTest("AssociatedClass2", "Associated_Class2"))
					.addTracker(new TrackerTest("AssociatedClass3", "Associated_Class3"))
					.addTracker(new TrackerTest("AssociatedClass4", "Associated_Class4"))
					.build();
			
			String result = test.match();
			
			if( !result.isEmpty() ) {
				map.showSourceNodes();
				map.showTargetNodes();
				map.showSourceTracking();
				System.out.println(result);
				fail("FlattingTopClassAssociation: "+ result);
			}
		} catch (Exception e) {
			e.printStackTrace();
			fail("FlattingTopClassAssociation");
		}
	}
	
	@Test
	public void test_28() {
		try {
			Mapper map = new Mapper.Builder()
					.setUmlFile(path+"Test_28.uml")
					.setDestinationPath(pathOutput)
					.setStrategy(Strategy.ONE_TABLE_PER_KIND)
					.setDBMS(dbms)
					.build();
			
			map.doMapping();
			
			GraphTest test = new GraphTest.Builder()
					.setSourceGraph(map.getSourceGraph())
					.setTargetGraph(map.getTargetGraph())
					.addTable(new TableTest("person", new String[] {"person_id", "is_employee", "is_personal_customer"})
							.addEnumeration("life_phase_enum",  new String[] {"Child", "Adult"}))
					.addTable(new TableTest("employment", new String[] {"employment_id", "person_id"}) )
					.addRelationship(new RelationshipTest("Employment", Cardinality.C0_N, "Person", Cardinality.C1) )
					
					.addTracker(new TrackerTest("Person", "Person"))
					.addTracker(new TrackerTest("Child", "Person"))
					.addTracker(new TrackerTest("Adult", "Person"))
					.addTracker(new TrackerTest("Employee", "Person"))
					.addTracker(new TrackerTest("PersonalCustomer", "Person"))
					.addTracker(new TrackerTest("Employment", "Employment"))
					.build();
			
			String result = test.match();
			
			if( !result.isEmpty() ) {
				map.showSourceNodes();
				map.showTargetNodes();
				map.showSourceTracking();
				System.out.println(result);
				fail("runExample: "+ result);
			}
		} catch (Exception e) {
			e.printStackTrace();
			fail("runExample");
		}
	}
	
	
}


