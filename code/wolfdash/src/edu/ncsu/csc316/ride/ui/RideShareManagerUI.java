/**
 * 
 */
package edu.ncsu.csc316.ride.ui;

import java.io.FileNotFoundException;
import java.util.Scanner;

import edu.ncsu.csc316.ride.manager.ReportManager;

/**
 * Command-line interface for RideShareManager
 * 
 * @author Josue Lopez-Carmona
 */
public class RideShareManagerUI {
	
	/**
	 * Main method of RideShareManagerUI
	 * 
	 * @param args arguments
	 * @throws FileNotFoundException if file cannot be found
	 */
	public void main(String[] args) throws FileNotFoundException {
		Scanner scan = new Scanner(System.in);
		
		System.out.print("Enter path to driver: ");
		String driverPath = scan.nextLine().trim();
		
		System.out.print("Enter path to live log events: ");
		String logPath = scan.nextLine().trim();
		
		ReportManager rm = new ReportManager(driverPath, logPath);
		
		boolean running = true;
		while(running) {
			
            System.out.println("\nSelect a report to display:");
            System.out.println("1. Compliance Report");
            System.out.println("2. Driver Performance Report");
            System.out.println("3. Quit");
            System.out.print("Enter choice: ");
            String choice = scan.nextLine().trim();
 
            if("1".equals(choice)) {
                System.out.println(rm.getComplianceReport());
            }
            else if("2".equals(choice)) {
                System.out.println(rm.getDriverPerformanceReport());
            }
            else if("3".equals(choice)) {
                running = false;
            } 
            else {
                System.out.println("Invalid choice");
            }
		}
		
		scan.close();
	}
}
