package edu.ncsu.csc316.ride.manager;

import static org.junit.jupiter.api.Assertions.*;

import java.io.FileNotFoundException;

import org.junit.jupiter.api.Test;

import edu.ncsu.csc316.dsa.map.Map;
import edu.ncsu.csc316.ride.data.Driver;
import edu.ncsu.csc316.ride.data.LiveLogEvent;
import edu.ncsu.csc316.ride.dsa.DataStructure;

/**
 * Tests the RideShareManager class
 * 
 * @author Josue Lopez-Carmona
 */
class RideShareManagerTest {
	
	/** drivers file **/
	private static final String DRIVERS_FILE = "input/drivers.csv";
	/** log events for drivers file **/
	private static final String EVENTS_FILE = "input/events.csv";
	/** an empty file **/
	private static final String EVENTS_EMPTY_FILE = "input/events_empty.csv";

	/** manager to use **/
    private RideShareManager manager;
    
    /**
     * Tests when using a specified type of map instead of default one
     */
    @Test
    public void testExplicitMapType() {
    	try {
            RideShareManager m = new RideShareManager(DRIVERS_FILE, EVENTS_FILE, DataStructure.SKIPLIST);
            assertEquals(8, m.getDrivers().size());
		} catch (FileNotFoundException e) {
			fail();
		}
    }
	
    /**
     * Tests get drivers as well as information about them. testing that 
     * the constructor logic
     * 
     * @throws FileNotFoundException if file cannot be opened/found
     */
    @Test
    void testGetDrivers() throws FileNotFoundException {
    	manager = new RideShareManager(DRIVERS_FILE, EVENTS_FILE);
    	
    	assertEquals(8, manager.getDrivers().size());
        assertNull(manager.getDrivers().get("D-9999"));
    	
    	Driver d1 = manager.getDrivers().get("D-0001");
        assertEquals("Alice Smith", d1.getName());
        assertEquals("D-0001", d1.getId());
        assertEquals(2, (int) d1.getTrips());
        assertEquals(25.50, d1.getEarnings(), 0.001);
        
        Driver d2 = manager.getDrivers().get("D-0002");
        assertEquals(1, (int) d2.getTrips());
        assertEquals(55.50, d2.getEarnings(), 0.001);
        
        Driver d3 = manager.getDrivers().get("D-0003");
        assertEquals(0, (int) d3.getTrips());
        assertEquals(0.00, d3.getEarnings(), 0.001);
        
        Driver d4 = manager.getDrivers().get("D-0004");
        assertEquals(1, (int) d4.getTrips());
        assertEquals(25.50, d4.getEarnings(), 0.001);
        
        assertEquals(0, (int) manager.getDrivers().get("D-0005").getTrips());
        assertEquals(0, (int) manager.getDrivers().get("D-0006").getTrips());
        assertEquals(0, (int) manager.getDrivers().get("D-0007").getTrips());
        assertEquals(0, (int) manager.getDrivers().get("D-0008").getTrips());
    }
    
    /**
     * Tests when there is an empty registry of drivers
     * (driver file and log file are both empty ) 
     * 
     * @throws FileNotFoundException if file cannot be opened/found
     */
    @Test
    void testEmptyDrivers() throws FileNotFoundException {
    	RideShareManager m = new RideShareManager(EVENTS_EMPTY_FILE, EVENTS_EMPTY_FILE);
    	
    	assertEquals(0, m.getDrivers().size());
    }

	/**
	 * Tests the get eventClassification method as well as it logic in the constructor 
	 * 
	 * @throws FileNotFoundException if file cannot be opened/found
	 */
	@Test
	void testGetEventClassifications() throws FileNotFoundException {
		manager = new RideShareManager(DRIVERS_FILE, EVENTS_FILE);
		
		assertEquals(13, manager.getEventClassifications().size());
		
		Map<LiveLogEvent, String> classifications = manager.getEventClassifications();
        
        RideShareManager m = new RideShareManager(EVENTS_EMPTY_FILE, EVENTS_FILE);
        classifications = m.getEventClassifications();
        assertEquals(13, classifications.size());        
	}

}
