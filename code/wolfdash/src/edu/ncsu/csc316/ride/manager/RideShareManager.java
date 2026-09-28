package edu.ncsu.csc316.ride.manager;

import java.io.FileNotFoundException;

import edu.ncsu.csc316.dsa.list.List;
import edu.ncsu.csc316.dsa.map.Map;
import edu.ncsu.csc316.dsa.sorter.Sorter;
import edu.ncsu.csc316.ride.data.Driver;
import edu.ncsu.csc316.ride.data.LiveLogEvent;
import edu.ncsu.csc316.ride.dsa.Algorithm;
import edu.ncsu.csc316.ride.dsa.DSAFactory;
import edu.ncsu.csc316.ride.dsa.DataStructure;
import edu.ncsu.csc316.ride.io.DriverReader;
import edu.ncsu.csc316.ride.io.LiveLogEventReader;

/**
 * Manages for user of RideShare
 * 
 * @author Josue Lopez-Carmona
 * 
 */
public class RideShareManager {

	/** A map of Driver objects **/
	private Map<String, Driver> drivers;
	
	/** Maps each LiveLogEvent to a Driver **/
	private Map<LiveLogEvent, String> eventClassifications;
	
	/** 
	 * Constructor for RideShareManager
	 * 
	 * @param pathToDriverFile file path to parse of drivers
	 * @param pathToLogFile file path of logs of drivers
	 * @throws FileNotFoundException if any of the files cannot be opened 
	 */
    public RideShareManager(String pathToDriverFile, String pathToLogFile) throws FileNotFoundException {
        this(pathToDriverFile, pathToLogFile, DataStructure.UNORDEREDLINKEDMAP);
    }

    /**
     * Constructor for RideShareManager
     * 
     * @param pathToDriverFile file path to parse of drivers
     * @param pathToLogFile file path of logs of drivers
     * @param mapType type of map used to organize drivers
     * @throws FileNotFoundException if any of the files cannot be opened 
     */
    public RideShareManager(String pathToDriverFile, String pathToLogFile, DataStructure mapType)
            throws FileNotFoundException {
        DSAFactory.setListType(DataStructure.SINGLYLINKEDLIST);
        DSAFactory.setComparisonSorterType(Algorithm.MERGESORT);
        DSAFactory.setNonComparisonSorterType(Algorithm.COUNTING_SORT);
        DSAFactory.setMapType(mapType);
        
        drivers = DSAFactory.getMap(null);
        eventClassifications = DSAFactory.getMap(null);
        
        List<Driver> driverList = DriverReader.readData(pathToDriverFile);
        for(int i = 0; i < driverList.size(); i++) {
        	Driver d = driverList.get(i);
        	d.setTrips(0);
            d.setEarnings(0.0);
        	drivers.put(d.getId(), d);
        }
        
        Map<String, Boolean> activeSesh = DSAFactory.getMap(null);
        List<LiveLogEvent> eventList = LiveLogEventReader.readData(pathToLogFile);
        LiveLogEvent[] eventArr = new LiveLogEvent[eventList.size()];
        
        for(int i = 0; i < eventList.size(); i++) {
        	eventArr[i] = eventList.get(i);
        }
        
        Sorter<LiveLogEvent> sorter = DSAFactory.getComparisonSorter(null);
        sorter.sort(eventArr);
        
        for(LiveLogEvent event: eventArr) {
        	String driverId = event.getDriverID();
        	Driver driver = drivers.get(driverId);
        	
        	if(driver == null) {
        		eventClassifications.put(event, "[VIOLATION: Unauthorized Driver]");
        		continue;
        	}
        	
        	boolean isActive = Boolean.TRUE.equals(activeSesh.get(driverId));
        	
        	if("DRIVER_LOGON".equals(event.getCommand())) {
        		if(isActive) eventClassifications.put(event, "[VIOLATION: Duplicate Active Session]");
        		else {
        			activeSesh.put(driverId, true);
        			eventClassifications.put(event, "[NORMAL]");
        		}
        	}
        	else if("LOCATION_UPDATE".equals(event.getCommand())) {
        		if(isActive) {
        			eventClassifications.put(event, "[NORMAL]");
        		}
        		else {
        			eventClassifications.put(event, "[WARNING: Missing Session]");
        		}
        	}
        	else if("TRIP_COMPLETED".equals(event.getCommand())) {
        		if(isActive) {
        			double fare = Double.parseDouble(event.getDetails());
        			driver.setEarnings(driver.getEarnings() + fare);
        			driver.setTrips(driver.getTrips() + 1);
        			eventClassifications.put(event, "[NORMAL]");
        		}
        		else {
        			eventClassifications.put(event, "[VIOLATION: Illegal Fare Processing]");
        		}
        	}
        	else if("DRIVER_LOGOFF".equals(event.getCommand())) {
        		if(isActive) {
        			activeSesh.put(driverId, false);
        			eventClassifications.put(event, "[NORMAL]");
        		}
        		else eventClassifications.put(event, "[WARNING: Redundant Logoff]");
        	}
        }
        
    }

    /**
     * Gets the eventClassifications field
     * 
     * @return Map with LiveLogEvent keys and String values
     */
    public Map<LiveLogEvent, String> getEventClassifications() {
        return eventClassifications;
    }

    /**
     * Gets drivers field 
     * 
     * @return Map of DriverId String as keys and Driver values
     */
    public Map<String, Driver> getDrivers() {
    	return drivers;
    }
    
    
}
