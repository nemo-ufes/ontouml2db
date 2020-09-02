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

public class RunExample {

	private static String path = "\\test\\umlFiles\\";
	private static String pathOutput = "c:\\_Results\\";
	private static DBMS dbms = DBMS.H2;
	
	@BeforeClass
    public static void setup() {
		String curDir = System.getProperty("user.dir");
		path = curDir + path;
    }
	
	
	@Test
	public void runExample() {
		try {
			Mapper map = new Mapper.Builder()
					.setUmlFile(path+"RunExample.uml")
					.setDestinationPath(pathOutput)
					.setStrategy(Strategy.ONE_TABLE_PER_KIND)
					.setDBMS(dbms)
					.build();
			
			map.doMapping();
			
			GraphTest test = new GraphTest.Builder()
					.setSourceGraph(map.getSourceGraph())
					.setTargetGraph(map.getTargetGraph())
					.addTable(new TableTest("person", new String[] {"person_id", "name", "birth_date", "rg", "ci", "is_employee", "is_personal_customer", "credit_rating", "credit_card"})
							 .addEnumeration("life_phase_enum", new String[] {"CHILD", "ADULT"}))
					.addTable(new TableTest("organization", new String[] {"organization_id", "name", "address", "is_corporate_customer", "credit_rating", "credit_limit", "is_contractor", "playground_size", "capacity"})
							.addEnumeration("organization_type_enum", new String[] {"PRIMARYSCHOOL", "HOSPITAL"}))
					.addTable(new TableTest("employment", new String[] {"employment_id", "organization_id", "person_id", "salary"}) )
					.addTable(new TableTest("supply_contract", new String[] {"supply_contract_id", "organization_id", "organization_provides_contract_id", "person_id", "contract_value"}) )
					.addTable(new TableTest("enrollment", new String[] {"enrollment_id", "organization_id", "person_id", "grade"}) )
					.addTable(new TableTest("nationality", new String[] {"nationality_id", "person_id"})
							.addEnumeration("nationality_enum", new String[] {"BRAZILIANCITIZEN", "ITALIANCITIZEN"}) )

					.addRelationship(new RelationshipTest("Nationality", Cardinality.C0_N, "Person", Cardinality.C1) )
					.addRelationship(new RelationshipTest("Enrollment", Cardinality.C0_N, "Person", Cardinality.C1) )
					.addRelationship(new RelationshipTest("Employment", Cardinality.C0_N, "Person", Cardinality.C1) )
					.addRelationship(new RelationshipTest("Supply_Contract", Cardinality.C0_N, "Person", Cardinality.C0_1) )
					.addRelationship(new RelationshipTest("Organization", Cardinality.C1, "Employment", Cardinality.C0_N) )
					.addRelationship(new RelationshipTest("Organization", Cardinality.C1, "Supply_Contract", Cardinality.C0_N) )
					.addRelationship(new RelationshipTest("Organization", Cardinality.C0_1, "Supply_Contract", Cardinality.C0_N) )
					.addRelationship(new RelationshipTest("Organization", Cardinality.C1, "Enrollment", Cardinality.C0_N) )
										
					.addTracker(new TrackerTest("NamedEntity", "Person"))
					.addTracker(new TrackerTest("NamedEntity", "Organization"))
					.addTracker(new TrackerTest("Person", "Person"))
					.addTracker(new TrackerTest("Organization", "Organization"))
					.addTracker(new TrackerTest("BrazilianCitizen", "nationality"))
					.addTracker(new TrackerTest("ItalianCitizen", "nationality"))
					.addTracker(new TrackerTest("Child", "Person"))
					.addTracker(new TrackerTest("Adult", "Person"))
					.addTracker(new TrackerTest("Employee", "Person"))
					.addTracker(new TrackerTest("Customer", "Person"))
					.addTracker(new TrackerTest("Customer", "Organization"))
					.addTracker(new TrackerTest("PersonalCustomer", "Person"))
					.addTracker(new TrackerTest("CorporateCustomer", "Organization"))
					.addTracker(new TrackerTest("Employment", "Employment"))
					.addTracker(new TrackerTest("SupplyContract", "Supply_Contract"))
					.addTracker(new TrackerTest("Contractor", "Organization"))
					.addTracker(new TrackerTest("PrimarySchool", "Organization"))
					.addTracker(new TrackerTest("Hospital", "Organization"))
					.addTracker(new TrackerTest("Enrollment", "Enrollment"))
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
