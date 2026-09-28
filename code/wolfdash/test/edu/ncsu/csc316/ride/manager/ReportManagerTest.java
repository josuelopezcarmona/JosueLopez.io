/**
 * 
 */
package edu.ncsu.csc316.ride.manager;

import static org.junit.jupiter.api.Assertions.*;

import java.io.FileNotFoundException;

import org.junit.jupiter.api.Test;

import edu.ncsu.csc316.ride.dsa.DataStructure;

/**
 * Tests the ReportManager class
 * 
 * @author Josue Lopez-Carmona
 */
class ReportManagerTest {
	
	/** test file for drivers input **/
	private static final String DRIVERS_FILE = "input/drivers.csv";
	/** test file for event input **/
    private static final String EVENTS_FILE = "input/events.csv";
    /** test for empty file **/
    private static final String EVENTS_EMPTY_FILE = "input/events_empty.csv";

    /** expected driver performance output **/
    private static final String DRIVER_PERFORMANCE_REPORT =
            "Driver Performance Report [\n"
          + " D-0002 (Bob Jones) had 1 trips and earned 55.50\n"
          + " D-0001 (Alice Smith) had 2 trips and earned 25.50\n"
          + " D-0004 (Diana Prince) had 1 trips and earned 25.50\n"
          + " D-0003 (Charlie Brown) had 0 trips and earned 0.00\n"
          + " D-0005 (Evan Wright) had 0 trips and earned 0.00\n"
          + " D-0006 (Fiona Gallagher) had 0 trips and earned 0.00\n"
          + " D-0007 (George Clark) had 0 trips and earned 0.00\n"
          + " D-0008 (Hannah Abbott) had 0 trips and earned 0.00\n"
          + "]";
    /** empty drivers file **/
	private static final String DRIVERS_EMPTY_FILE = "input/drivers_empty.csv";
	/** test file with no event violations **/
	private static final String EVENTS_NO_VIOLATIONS_FILE = "input/events_no_violations.csv";
    
    /** ReportManager to test **/
    private ReportManager reportManager;
    
    
	/**
	 * Test method for ReportManager()
	 */
	@Test
	void testReportManager() {
		try {
            new ReportManager(DRIVERS_FILE, EVENTS_FILE);
        } catch (FileNotFoundException e) {
            fail();
        }
	}

	/**
	 * Test method for ReportManager with a file that does not exist
	 */
	@Test
	void testReportManagerInvalidFile() throws FileNotFoundException {
        assertThrows(FileNotFoundException.class, () -> new ReportManager("input/this_file_dne.csv", EVENTS_FILE));
    }
	
	/**
	 * Tests constructor for custom map
	 * 
	 * @throws FileNotFoundException if file not found
	 */
	@Test
    public void testReportManagerCustomMap() throws FileNotFoundException {
        ReportManager rm = new ReportManager(DRIVERS_FILE, EVENTS_FILE, DataStructure.SKIPLIST);
        
        assertEquals(DRIVER_PERFORMANCE_REPORT, rm.getDriverPerformanceReport());
    }

	/**
	 * Test method for getComplianceReport()
	 * 
	 * @throws FileNotFoundException if file cannot be found
	 */
	@Test
	void testGetComplianceReport() throws FileNotFoundException {
		reportManager = new ReportManager(DRIVERS_FILE, EVENTS_FILE);
		String report = reportManager.getComplianceReport();
		
		String expected = "Compliance Report [\n"
				+ " [VIOLATION: Unauthorized Driver] Event at Thu Jun 04 12:00:02 EDT 2026: DRIVER_LOGON D-9999 \n"
				+ " [VIOLATION: Duplicate Active Session] Event at Thu Jun 04 12:00:03 EDT 2026: DRIVER_LOGON D-0001 \n"
				+ " [WARNING: Missing Session] Event at Thu Jun 04 12:00:05 EDT 2026: LOCATION_UPDATE D-0002 Zone_CentennialCampus\n"
				+ " [VIOLATION: Illegal Fare Processing] Event at Thu Jun 04 12:00:07 EDT 2026: TRIP_COMPLETED D-0003 15.00\n"
				+ " [WARNING: Redundant Logoff] Event at Thu Jun 04 12:00:13 EDT 2026: DRIVER_LOGOFF D-0005 \n"
				+ "]";
		
		assertEquals(expected, report);
	}
	
	/**
	 * Test for ComplianceReport and their time stamps
	 * 
	 * @throws FileNotFoundException If file cannot be found
	 */
	@Test
    public void testComplianceReportTimeStamps() throws FileNotFoundException {
		reportManager = new ReportManager(DRIVERS_FILE, EVENTS_FILE);
        String report = reportManager.getComplianceReport();
        
        String expected = "Compliance Report [\n"
        		+ " [VIOLATION: Unauthorized Driver] Event at Thu Jun 04 12:00:02 EDT 2026: DRIVER_LOGON D-9999 \n"
        		+ " [VIOLATION: Duplicate Active Session] Event at Thu Jun 04 12:00:03 EDT 2026: DRIVER_LOGON D-0001 \n"
        		+ " [WARNING: Missing Session] Event at Thu Jun 04 12:00:05 EDT 2026: LOCATION_UPDATE D-0002 Zone_CentennialCampus\n"
        		+ " [VIOLATION: Illegal Fare Processing] Event at Thu Jun 04 12:00:07 EDT 2026: TRIP_COMPLETED D-0003 15.00\n"
        		+ " [WARNING: Redundant Logoff] Event at Thu Jun 04 12:00:13 EDT 2026: DRIVER_LOGOFF D-0005 \n"
        		+ "]";
 
        assertEquals(expected, report);
    }
	
	/**
	 * Test for when an empty log file is given as input
	 * 
	 * @throws FileNotFoundException if file cannot be found
	 */
	@Test
    public void testComplianceReportEmptyFile() throws FileNotFoundException {
        ReportManager rm = new ReportManager(DRIVERS_FILE, EVENTS_EMPTY_FILE);
        assertEquals("Compliance Report [\n No warnings or violations.\n]", rm.getComplianceReport());
    }

	/**
	 * Tests driverPerformanceReport with empty log file
	 * 
	 * @throws FileNotFoundException if file cannot be found
	 */
	@Test
	public void testDriverPerformanceReportEmtptyLog() throws FileNotFoundException {
		ReportManager rm = new ReportManager(DRIVERS_FILE, EVENTS_EMPTY_FILE);
		
        String expected = "Driver Performance Report [\n"
              + " D-0001 (Alice Smith) had 0 trips and earned 0.00\n"
              + " D-0002 (Bob Jones) had 0 trips and earned 0.00\n"
              + " D-0003 (Charlie Brown) had 0 trips and earned 0.00\n"
              + " D-0004 (Diana Prince) had 0 trips and earned 0.00\n"
              + " D-0005 (Evan Wright) had 0 trips and earned 0.00\n"
              + " D-0006 (Fiona Gallagher) had 0 trips and earned 0.00\n"
              + " D-0007 (George Clark) had 0 trips and earned 0.00\n"
              + " D-0008 (Hannah Abbott) had 0 trips and earned 0.00\n"
              + "]";
        
        assertEquals(expected, rm.getDriverPerformanceReport());
	}
	
	/**
	 * Test time stamp inner class comparator with a events file in reverse order 
	 * and a single duplicate time stamp
	 */
	@Test
	public void testComplianceReportTimeStamp() throws FileNotFoundException {
	    ReportManager rm = new ReportManager(DRIVERS_FILE, "input/events_reverse.csv");
	    String report = rm.getComplianceReport();
	    
	    String expectedReport = "Compliance Report [\n"
	    		+ " [VIOLATION: Unauthorized Driver] Event at Thu Jun 04 08:15:00 EDT 2026: DRIVER_LOGON D-9005 \n"
	    		+ " [VIOLATION: Unauthorized Driver] Event at Thu Jun 04 15:30:00 EDT 2026: DRIVER_LOGON D-9004 \n"
	    		+ " [VIOLATION: Unauthorized Driver] Event at Thu Jun 04 20:00:00 EDT 2026: DRIVER_LOGON D-9003 \n"
	    		+ " [VIOLATION: Unauthorized Driver] Event at Thu Jun 04 20:00:00 EDT 2026: DRIVER_LOGON D-9002 \n"
	    		+ " [VIOLATION: Unauthorized Driver] Event at Thu Jun 04 23:00:00 EDT 2026: DRIVER_LOGON D-9001 \n"
	    		+ "]";
	    
	    assertEquals(expectedReport, report);
	}
	
	/**
	 * Tests constructor with empty drivers file
	 * @throws FileNotFoundException if file cannot be found
	 */
	@Test
	public void testDriverPerformanceReporNoDrivers() throws FileNotFoundException {
        ReportManager rm = new ReportManager(DRIVERS_EMPTY_FILE, EVENTS_FILE);
        
        assertEquals("Driver Performance Report [\n No drivers\n]", rm.getDriverPerformanceReport());
    }
	
	/**
	 * Tests constructor with no event violations in file
	 * @throws FileNotFoundException if cannot be found 
	 */
	@Test
    public void testComplianceReportAllNormal() throws FileNotFoundException {
        ReportManager rm = new ReportManager(DRIVERS_FILE, EVENTS_NO_VIOLATIONS_FILE);
        assertEquals("Compliance Report [\n No warnings or violations.\n]", rm.getComplianceReport());
    }

}
