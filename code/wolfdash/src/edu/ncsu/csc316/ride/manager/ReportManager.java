package edu.ncsu.csc316.ride.manager;

import java.io.FileNotFoundException;
import java.util.Comparator;

import edu.ncsu.csc316.dsa.list.List;
import edu.ncsu.csc316.dsa.map.Map;
import edu.ncsu.csc316.dsa.map.Map.Entry;
import edu.ncsu.csc316.dsa.sorter.Sorter;
import edu.ncsu.csc316.ride.data.Driver;
import edu.ncsu.csc316.ride.data.LiveLogEvent;
import edu.ncsu.csc316.ride.dsa.Algorithm;
import edu.ncsu.csc316.ride.dsa.DSAFactory;
import edu.ncsu.csc316.ride.dsa.DataStructure;

/**
 * This class reports information about drivers as a whole
 * 
 * @author Josue Lopez-Carmona
 */
public class ReportManager {

	/** manager that stores driver information and log information **/
    private RideShareManager manager;
    

    /**
     * Constructor for ReportManager using default map
     * 
     * @param pathToDriverFile file of drivers
     * @param pathToLogFile file of log events
     * @throws FileNotFoundException if file is not found/cannot be opened
     */
    public ReportManager(String pathToDriverFile, String pathToLogFile) throws FileNotFoundException {
        this(pathToDriverFile, pathToLogFile, DataStructure.UNORDEREDLINKEDMAP);
    }

    /**
     * Constructor for custom map
     * 
     * @param pathToDriverFile file of drivers
     * @param pathToLogFile file of log events
     * @param mapType type of map to use
     * @throws FileNotFoundException if file is not found/cannot be opened
     */
    public ReportManager(String pathToDriverFile, String pathToLogFile, DataStructure mapType) throws FileNotFoundException {
        manager = new RideShareManager(pathToDriverFile, pathToLogFile, mapType);
        DSAFactory.setListType(DataStructure.ARRAYBASEDLIST);
        DSAFactory.setComparisonSorterType(Algorithm.MERGESORT);
        DSAFactory.setNonComparisonSorterType(Algorithm.COUNTING_SORT);
        DSAFactory.setMapType(mapType);
    }

    /**
     * Gets compliance report
     * 
     * @return compliance report as a string
     */
    public String getComplianceReport() {
    	Map<LiveLogEvent, String> classifications = manager.getEventClassifications();
    	
    	List<Entry<LiveLogEvent, String>> flagged = DSAFactory.getIndexedList();
        for(Entry<LiveLogEvent, String> entry : classifications.entrySet()) {
        	
        	if(!entry.getValue().equals("[NORMAL]")) flagged.addLast(entry);
        	
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append("Compliance Report [\n");
        
        if(flagged.isEmpty()) sb.append(" No warnings or violations.\n");
        else {
        	@SuppressWarnings("unchecked")
			Entry<LiveLogEvent, String>[] entryArr = new Entry[flagged.size()];
        	
        	for(int i = 0; i < flagged.size(); i++) {
        		entryArr[i] = flagged.get(i);
        	}
        	
        	Sorter<Entry<LiveLogEvent, String>> sorter = DSAFactory.getComparisonSorter(null);
            sorter.sort(entryArr);
            
            for(Entry<LiveLogEvent, String> entry : entryArr) {
            	LiveLogEvent event = entry.getKey();
            	
            	sb.append(" ");
            	sb.append(entry.getValue());
            	sb.append(" Event at ");
            	sb.append(event.getTimestamp());
            	sb.append(": ");
            	sb.append(event.getCommand());
            	sb.append(" ");
            	sb.append(event.getDriverID());
            	sb.append(" ");
            	sb.append(event.getDetails());
            	sb.append("\n");
            }
            
        }
        sb.append("]");
        return sb.toString();
    }

    /**
     * Gets the drivers performance report in form of a string
     * 
     * @return string representation of DriverPerformanceReport
     */
    public String getDriverPerformanceReport() {
    	Map<String, Driver> drivers = manager.getDrivers();
    	
    	List<Driver> driverList = DSAFactory.getIndexedList();
    	for(Entry<String, Driver> entry : drivers.entrySet()) {
    		driverList.addLast(entry.getValue());
    	}
    	
    	StringBuilder sb = new StringBuilder();
    	sb.append("Driver Performance Report [\n");
    	
    	if(driverList.isEmpty()) sb.append(" No drivers\n");
    	else {
    		Driver[] driverArr = new Driver[driverList.size()];
    		
    		for(int i = 0; i < driverList.size(); i++) {
    			driverArr[i] = driverList.get(i);
    		}
    		
    		Sorter<Driver> sorter = DSAFactory.getComparisonSorter(new DriverComparator());
    		sorter.sort(driverArr);
    		
    		for(Driver d : driverArr) {
    			sb.append(" ");
    			sb.append(d.getId());
    			sb.append(" (");
    			sb.append(d.getName());
    			sb.append(") had ");
    			sb.append(d.getTrips());
    			sb.append(" trips and earned ");
    			sb.append(String.format("%.2f", d.getEarnings()));
    			sb.append("\n");
    		}
    		
    	}
    	sb.append("]");
		return sb.toString();
    }
    
    /**
     * INNER CLASS. 
     * This class compares and order drivers by earnings desc, then trips desc, then ID
     * 
     * @author Josue Lopez-Carmona
     */
    private static class DriverComparator implements Comparator<Driver> {

    	/**
    	 * Compares two Driver objects 
    	 * 
    	 * @param o1 Driver to compare 
    	 * @param o2 Driver to compare
    	 * @return -1 if o1 is less than o2, 1 if o1 is greater than o2
    	 */
		@Override
		public int compare(Driver o1, Driver o2) {
			int compareEarninga = Double.compare(o2.getEarnings(), o1.getEarnings());
			if(compareEarninga != 0) return compareEarninga;
			
			int compareTrips = Integer.compare(o2.getTrips(), o1.getTrips());
			if(compareTrips != 0) return compareTrips;
			
			return o1.getId().compareTo(o2.getId());
		}
    	
    }

}
