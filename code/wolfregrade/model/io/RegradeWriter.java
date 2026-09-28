/**
 * 
 */
package edu.ncsu.csc216.wolf_regrade.model.io;

import java.io.File;
import java.io.PrintWriter;

import edu.ncsu.csc216.wolf_regrade.model.course.Assignment;
import edu.ncsu.csc216.wolf_regrade.model.course.Course;
import edu.ncsu.csc216.wolf_regrade.model.regrade.RegradeRequest;
import edu.ncsu.csc216.wolf_regrade.model.util.Iterator;

/**
 * This class writes a Course to a file
 * @author Josue Lopez
 */
public class RegradeWriter {
	
	/**
	 * constructor not used
	 */
	public RegradeWriter() {
		//empty
	}

	/**
	 * Writes a Course to a file
	 * @param file to write to
	 * @param course course given
	 */
	public static void writeRegradeFile(File file, Course course) {
		try (PrintWriter pw = new PrintWriter(file)) {
			
            pw.println(course.getCourseName());
            
            for (int i = 0; i < course.getAssignmentCount(); i++) {
                Assignment assignment = course.getAssignment(i);
                pw.println("# " + assignment.getAssignmentName());
 
                Iterator<RegradeRequest> pendRequests = assignment.getPendingRequests().iterator();
                for (int j = 0; j < assignment.getPendingSize(); j++) {
                    RegradeRequest r = pendRequests.next();
                    pw.println("* P|" + r.toString());
                } 
                
                Iterator<RegradeRequest> completeRequests = assignment.getCompletedRequests().iterator();
                
                for (int k = 0; k < assignment.getCompletedRequests().size(); k++) {
                	RegradeRequest r = completeRequests.next();
                    pw.println("* C|" + r.toString());
                } 
            }
            course.setChanged(false);
		} catch (Exception e) {
			throw new IllegalArgumentException("Unable to save file.");
		}
	}

}
