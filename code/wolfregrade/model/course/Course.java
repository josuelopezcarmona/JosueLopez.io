/**
 * 
 */
package edu.ncsu.csc216.wolf_regrade.model.course;

import java.io.File;

import edu.ncsu.csc216.wolf_regrade.model.io.RegradeWriter;
import edu.ncsu.csc216.wolf_regrade.model.regrade.RegradeRequest;
import edu.ncsu.csc216.wolf_regrade.model.util.IBackLog;
import edu.ncsu.csc216.wolf_regrade.model.util.ISortedList;
import edu.ncsu.csc216.wolf_regrade.model.util.SortedList;

/**
 * This class represents a course which holds different assignments, and requests 
 * of those assignments
 * @author Josue Lopez
 */
public class Course {
	
	/** name of Course **/
	private String courseName;
	
	/** tracks if data has been modified **/
	private boolean isChanged;
	
	/** list of Assignment s **/
	private ISortedList<Assignment> assignments;

	/**
	 * Constructor for the Course class
	 * @param courseName name of Course
	 * @throws IllegalArgumentException if the name is null or empty
	 */
	public Course(String courseName) {
		setCourseName(courseName);
		setChanged(true);
		
		this.assignments = new SortedList<Assignment>();
	}

	/**
	 * Getter for the courseName field
	 * @return the courseName
	 */
	public String getCourseName() {
		return courseName;
	}

	/**
	 * Setter for the courseName field
	 * @param courseName the courseName to set
	 */
	public void setCourseName(String courseName) {
		if(courseName == null || courseName.isEmpty() || courseName.isBlank()) throw new IllegalArgumentException("Invalid name.");
		
		this.courseName = courseName.trim();
	}

	/**
	 * Getter for the field isChanged
	 * @return the isChanged
	 */
	public boolean isChanged() {
		return isChanged;
	}

	/**
	 * Setter for the field isChanged 
	 * @param isChanged the isChanged to set
	 */
	public void setChanged(boolean isChanged) {
		this.isChanged = isChanged;
	}
	
	/**
	 * adds an Assignment to assignments 
	 * @param a Assignment to ass
	 * @throws IllegalArgumentException if name is null, empty, or a duplicate
	 */
	public void addAssignment(String a) {
		Assignment assignment = new Assignment(a);
		
        assignments.add(assignment);
        
        isChanged = true;
	}
	
	/**
	 * Removes Assignment from assignments given the index
	 * @param idx of Assignment to remove
	 * @return Assignment removed
	 */
	public Assignment removeAssignment(int idx) {
		Assignment removed = assignments.remove(idx);
        isChanged = true;
        
        return removed;
	}
	
	/**
	 * Edits an Assignment by replacing it with another one 
	 * @param idx index of Assignment to edit in assignments
	 * @param newName new name given to Assignment that will substitute
	 * @throws IllegalArgumentException if newName is null, empty or blank
	 */
	public void editAssignment(int idx, String newName) {
		if(newName == null || newName.isEmpty() || newName.isBlank()) throw new IllegalArgumentException("Invalid assignment name.");
		
		Assignment original = assignments.remove(idx);
		String ogName = original.getAssignmentName();
		
        try {
            original.setAssignmentName(newName);
            assignments.add(original); 
        } catch (IllegalArgumentException e) {
            original.setAssignmentName(ogName);
            assignments.add(original);
            
            throw new IllegalArgumentException("Invalid assignment name.");
        }
        isChanged = true;
	}
	
	/**
	 * Gets Assignment in assignments given the index
	 * @param idx index of Assignment
	 * @return Assignment in assignments
	 */
	public Assignment getAssignment(int idx) {
		return assignments.get(idx);
	}
	
	/**
	 * gets the size of assignments list
	 * @return size of assignments
	 */
	public int getAssignmentCount() {
		return assignments.size();
	}
	
	/**
	 * Formats assignments in an array
	 * @return String array of assignments
	 */
	public String[] getAssignmentsAsArray() {
        String[] assignmentNames = new String[assignments.size()];
        
        for (int i = 0; i < assignments.size(); i++) {
            assignmentNames[i] = assignments.get(i).getAssignmentName();
        }
        
        return assignmentNames;
	}
	
	/**
	 * Formats assignments in an array in detail
	 * @return String 2d array of assignments
	 */
	public String[][] getAssignmentsAsDetailArray() {
        String[][] assignmentsInDetail = new String[assignments.size()][5];
        
        for (int i = 0; i < assignments.size(); i++) {
            Assignment a = assignments.get(i);
            assignmentsInDetail[i][0] = a.getAssignmentName();
            assignmentsInDetail[i][1] = a.getPendingSize() + "";
            assignmentsInDetail[i][2] = a.getCompletedSize() + "";
            assignmentsInDetail[i][3] = a.getTotalSize() + "";
            assignmentsInDetail[i][4] = a.getCompletedPercentage() + "%";
        }
        
        return assignmentsInDetail;
	}

	/**
	 * Gets assignments field
	 * @return the assignments
	 */
	public ISortedList<Assignment> getAssignments() {
		return assignments;
	}
	
	/**
	 * adds a PendingRequest to assignments
	 * @param idx index to insert the PendingRequest in the list 
	 * @param request to add
	 */
	public void addPendingRequest(int idx, RegradeRequest request) {
		assignments.get(idx).addPendingRequest(request);
        isChanged = true;
	}
	
	/**
	 * edits a PendingRequest in assignments
	 * @param editIdx in PendingRequests list
	 * @param assignmentIdx index of assignment to edit 
	 * @param request to substitute
	 */
	public void editPendingRequest(int assignmentIdx, int editIdx, RegradeRequest request) {
		assignments.get(assignmentIdx).editPendingRequest(editIdx, request);
        isChanged = true;
	}
	
	/**
	 * Removes a PendingRequest in assignments
	 * @param assignmentIdx index of the PendingRequets list
	 * @param requestIdx index of assignments
	 * @return PendingRequest removed
	 */
	public RegradeRequest removePendingRequest(int assignmentIdx, int requestIdx) {
		RegradeRequest removed = assignments.get(assignmentIdx).removePendingRequest(requestIdx);
        isChanged = true;
        
        return removed;
	}
	
	/**
	 * Completes a pendingRequest
	 * @param assignmentIdx index of list of PendingRequest
	 * @param requestIdx index of assignment 
	 * @param resolution name of request
	 * @param gradeChanged if the request is changed
	 */
	public void completeRequest(int assignmentIdx, int requestIdx,  String resolution, boolean gradeChanged) {
		assignments.get(assignmentIdx).completeRequest(requestIdx, resolution, gradeChanged);
        isChanged = true;
	}
	
	/**
	 * Un-dos last completed request
	 * @param assignmentIdx index of the assignment list
	 */
	public void undoLastCompletion(int assignmentIdx) {
		assignments.get(assignmentIdx).undoLastCompletion();
        isChanged = true;
	}
	
	/**
	 * Formats requests by grader
	 * @param grader name if grader
	 * @return String[][] of requests
	 */
	public String[][] filterByGrader(String grader) {
		String[][] list;
		
		SortedList<RegradeRequest> graderList = new SortedList<RegradeRequest>();
		
        for (int i = 0; i < assignments.size(); i++) {
            IBackLog<RegradeRequest> pending = assignments.get(i).getPendingRequests();
            
            for(int j = 0; j < pending.size(); j++) {
            	if(grader.equalsIgnoreCase(pending.get(j).getGrader())) graderList.add(pending.get(j));
            }
        }
        
        list = new String[graderList.size()][5];
        for(int i = 0; i < graderList.size(); i++) {
        	RegradeRequest r = graderList.get(i);
            list[i][0] = r.getType();
            list[i][1] = r.getAssignmentName();
            list[i][2] = r.getStudentName();
            list[i][3] = r.getUnityId();
            list[i][4] = r.getGrader();
        }
 
        return list;
	}
	
	/**
	 * Saves re-grades to a file
	 * @param file name of file
	 */
	public void saveCourseRegrades(File file) {
		RegradeWriter.writeRegradeFile(file, this);
	}
	
}
