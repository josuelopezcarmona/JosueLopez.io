/**
 * 
 */
package edu.ncsu.csc216.wolf_regrade.model.io;

import java.io.File;
import java.nio.file.Files;
import java.util.Scanner;

import edu.ncsu.csc216.wolf_regrade.model.course.Assignment;
import edu.ncsu.csc216.wolf_regrade.model.course.Course;
import edu.ncsu.csc216.wolf_regrade.model.regrade.ClarifyRequest;
import edu.ncsu.csc216.wolf_regrade.model.regrade.ExemptionRequest;
import edu.ncsu.csc216.wolf_regrade.model.regrade.RegradeRequest;
import edu.ncsu.csc216.wolf_regrade.model.regrade.ResubmitRequest;
import edu.ncsu.csc216.wolf_regrade.model.regrade.ReviewRequest;

/**
 * This class reads a regrade file and returns a fully populated Course
 * @author Josue Lopez
 */
public class RegradeReader {

	/**
	 * Constructor not used
	 */
	public RegradeReader() {
		//empty
	}

	/**
	 * Reads a file of Regrades for a course
	 * @param file file to read
	 * @return Course with regrades
	 * @throws IllegalArgumentException if the file cannot be found or is otherwise invalid
	 */
	public static Course readRegradeFile(File file) {
		String content;  
		
		try {
			content = new String(Files.readString(file.toPath()));
		} catch(Exception e) {
            throw new IllegalArgumentException("Unable to load file.");
        }
        
        
        Scanner fileScanner = new Scanner(content);
        fileScanner.useDelimiter("\\r?\\n[#] ");
 
        if(!fileScanner.hasNext()) {
            fileScanner.close();
            throw new IllegalArgumentException("Unable to load file.");
        }
 
        String courseName = fileScanner.next().trim();
        Course course;
        
        try {
            course = new Course(courseName);
        } catch(IllegalArgumentException e) {
            fileScanner.close();
            throw new IllegalArgumentException("Unable to load file.");
        }
 
        while(fileScanner.hasNext()) {
            String assignmentSection = fileScanner.next();
            processAssignmentSection(course, assignmentSection);
        }
        
        fileScanner.close();
        course.setChanged(false);
        return course;
	}
	
	/**
	 * Processes an Assignment token
	 * @param course to add the assignments to
	 * @param section of the assignments
	 */
	private static void processAssignmentSection(Course course, String section) {
        Scanner sectionScanner = new Scanner(section);
        sectionScanner.useDelimiter("\\r?\\n[*] ");
 
        if(!sectionScanner.hasNext()) {
            sectionScanner.close();
            return;
        }
 
        String assignmentName = sectionScanner.next().trim();
 
        Assignment assignment;
        try {
            assignment = new Assignment(assignmentName);
        } catch(IllegalArgumentException e) {
            sectionScanner.close();
            return;
        }
 
        while(sectionScanner.hasNext()) {
            String requestToken = sectionScanner.next();
            RegradeRequest request = processRequestToken(requestToken);
            
            if(request != null) {
                if(requestToken.substring(0, 2).equals("C|")) assignment.addCompletedRequest(request);
                else assignment.addPendingRequest(request);
            }
        } 
        sectionScanner.close();
 
        try {
            course.getAssignments().add(assignment);
            course.setChanged(true);
        } catch (IllegalArgumentException e) {
        	//do nothing
        }
    }
	
	/**
	 * Processes a Request token form a string
	 * @param token as Request 
	 * @return a RegradeRequest
	 */
	private static RegradeRequest processRequestToken(String token) {
        int newlineIdx = token.indexOf('\n');
        if(newlineIdx < 0) return null;
 
        String fieldLine = token.substring(0, newlineIdx).trim();
        String openText = token.substring(newlineIdx + 1).trim();
 
        if(openText.isEmpty()) return null;
 
        if(fieldLine.endsWith("\r")) {
            fieldLine = fieldLine.substring(0, fieldLine.length() - 1);
        }
         
        String status = fieldLine.substring(0, 1);
        
        if(!"P".equals(status) && !"C".equals(status)) return null;
 
        String fields = fieldLine.substring(2);
        String[] parts = fields.split("\\|", -1);
 
        if(parts.length < 4) return null;
 
        String type = parts[0];
        String studentName = parts[1];
        String unityId = parts[2];
        String grader = parts[3];
 
        try {
            RegradeRequest request = buildRequest(type, studentName, unityId, grader, parts, openText);
            if(request == null) return null;
 
            if("C".equals(status)) {
                if (parts.length < 6) return null;
                
                request.setResolution(parts[parts.length - 2]);
                request.setGradeChanged(Boolean.parseBoolean(parts[parts.length - 1]));
            }
 
            return request;
        } catch(IllegalArgumentException e) {
            return null;
        }
    }

	/**
	 * Constructs a RegradeRequest
	 * @param type type of request 
	 * @param studentName name of student
	 * @param unityId unity id of student
	 * @param grader reviewing the request
	 * @param parts array with Request information
	 * @param openText of request
	 * @return a RegradeRequest
	 */
	private static RegradeRequest buildRequest(String type, String studentName, String unityId, String grader,
			String[] parts, String openText) {
        try {
            switch(type) {
            
            case ReviewRequest.TYPE:
                if(parts.length < 5) return null;
                return new ReviewRequest(studentName, unityId, grader, parts[4], openText);

            case ResubmitRequest.TYPE:
                if(parts.length < 6) return null;
                return new ResubmitRequest(studentName, unityId, grader,
                        parts[4], parts[5], openText);

            case ClarifyRequest.TYPE:
                if(parts.length < 5) return null;
                return new ClarifyRequest(studentName, unityId, grader, parts[4], openText);

            case ExemptionRequest.TYPE:
                if(parts.length < 5) return null;
                return new ExemptionRequest(studentName, unityId, grader, parts[4], openText);

            default: return null;
            }
        } catch(IllegalArgumentException e) {
            return null;
        }
	}
}
