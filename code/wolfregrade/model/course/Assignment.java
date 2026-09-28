package edu.ncsu.csc216.wolf_regrade.model.course;

import edu.ncsu.csc216.wolf_regrade.model.regrade.RegradeRequest;
import edu.ncsu.csc216.wolf_regrade.model.util.BackLog;
import edu.ncsu.csc216.wolf_regrade.model.util.CompletedRegradesStack;
import edu.ncsu.csc216.wolf_regrade.model.util.IBackLog;
import edu.ncsu.csc216.wolf_regrade.model.util.Iterator;

/**
 * This class is responsible for managing the life-cycle of requests
 * @author Josue Lopez-Carmona
 */
public class Assignment implements Comparable<Assignment> {
	
	/** name of Assignment **/
	private String assignmentName;
	
	/** list of pending requests **/
	private IBackLog<RegradeRequest> pendingRequests;
	
	/** list of completed requests **/
	private CompletedRegradesStack completedRequests;

	/**
	 * Constructor for Assignment
	 * @param assignmentName name of assignment
	 * @throws IllegalArgumentException if assignmentName is null or empty
	 */
	public Assignment(String assignmentName) {
		setAssignmentName(assignmentName);
		
		this.pendingRequests = new BackLog<>();
		this.completedRequests = new CompletedRegradesStack();
	}

	/**
	 * Getter for the field assignmentName
	 * @return the assignmentName
	 */
	public String getAssignmentName() {
		return assignmentName;
	}


	/**
	 * Setter for the assignmentName field
	 * @param assignmentName the assignmentName to set
	 * @throws IllegalArgumentException if assignmentName is null or empty
	 */
	public void setAssignmentName(String assignmentName) {
		if(assignmentName == null || assignmentName.isEmpty() || assignmentName.isBlank()) throw new IllegalArgumentException("Invalid assignment name.");
		
		this.assignmentName = assignmentName.trim();
	}

	/**
	 * Sets the request’s back-reference to this Assignment and adds it to the pending list
	 * @param request to add
	 */
	public void addPendingRequest(RegradeRequest request) {
		request.setAssignment(this);
        pendingRequests.add(request);
	}
	
	/**
	 * Sets the request’s back-reference to this Assignment and pushes it onto the completed stack
	 * @param request to add
	 */
	public void addCompletedRequest(RegradeRequest request) {
		request.setAssignment(this);
		completedRequests.push(request);
	}
	
	/**
	 * Removes the pending request at the given index, and pushes it onto the completed stack
	 * @param idx index in the pendingRequest list
	 * @param resolution of the request
	 * @param gradeChanged if grade has changed
	 */
	public void completeRequest(int idx, String resolution, boolean gradeChanged) {
		 RegradeRequest request = pendingRequests.remove(idx);
		 
	     request.setResolution(resolution);
	     request.setGradeChanged(gradeChanged);
	     
	     completedRequests.push(request);
	     
	}
	
	/**
	 * Un-dos the last completed request from list
	 */
	public void undoLastCompletion() {
		if(completedRequests.isEmpty()) throw new IllegalArgumentException("No completed requests to undo.");
		
		RegradeRequest request = completedRequests.pop();
        request.setGradeChanged(false);
        
        request.setResolution(null);
        pendingRequests.add(request);
	}
	
	/**
	 * Replaces the pending request at the given index with the replacement request
	 * @param idx index of the pending request in list
	 * @param replacement RegradeReuqest that will replace it
	 */
	public void editPendingRequest(int idx, RegradeRequest replacement) {
		pendingRequests.set(idx, replacement);
		pendingRequests.get(idx).setAssignment(this);
	}
	
	/**
	 * Removes and returns the pending request at the given index
	 * @param idx of request to remove
	 * @return request removed
	 */
	public RegradeRequest removePendingRequest(int idx) {
		return pendingRequests.remove(idx);
	}
	
	/**
	 * Formats pending requests in an array
	 * @return String 2d array of pending requests
	 */
	public String[][] getPendingRequestsAsArray() {
        String[][] result = new String[pendingRequests.size()][5];
        Iterator<RegradeRequest> pendingI = pendingRequests.iterator();
        
        for (int i = 0; i < pendingRequests.size(); i++) {
            RegradeRequest r = pendingI.next();
            result[i][0] = r.getType();
            result[i][1] = assignmentName;
            result[i][2] = r.getStudentName();
            result[i][3] = r.getUnityId();
            result[i][4] = r.getGrader();
        }
        
        return result;
	}
	
	/**
	 * Formats completedRequests info in a 2D array
	 * @return String[][] with seven columns: type, assignment name, 
	 * student name, unity ID, grader, resolution, grade changed 
	 */
	public String[][] getCompletedRequestsAsArray() {
		String[][] result = new String[completedRequests.size()][7];
		
		Iterator<RegradeRequest> it = completedRequests.iterator();
		
		for(int i = 0; i < completedRequests.size(); i++) {
			RegradeRequest r = it.next();
			
			result[i][0] = r.getType();
            result[i][1] = assignmentName;
            result[i][2] = r.getStudentName();
            result[i][3] = r.getUnityId();
            result[i][4] = r.getGrader();
            result[i][5] = r.getResolution();
            result[i][6] = r.isGradeChanged() + "";
		}
		
		return result;
	}
	
	/**
	 * gets PendingRequest in the list given an index
	 * @param idx index of PendingReuqest
	 * @return PendingRequest in list
	 */
	public RegradeRequest getPendingRequest(int idx) {
		return pendingRequests.get(idx);
	}
	
	/**
	 * Gets the size of list of PendingRequests
	 * @return size of list of PendingRequests as an integer
	 */
	public int getPendingSize() {
		return pendingRequests.size();
	}
	
	/**
	 * Gets size of completedRequest list
	 * @return size of list of CompletedRequests as an integer
	 */
	public int getCompletedSize() {
		return completedRequests.size();
	}
	
	/**
	 * Gets total size of both lists combined
	 * @return size of both lists combined
	 */
	public int getTotalSize() {
		return pendingRequests.size() + completedRequests.size();
	}
	
	/**
	 * Computes the percentage of completed requests relative to
	 * the entire amount of requests
	 * @return percentage of completed requests
	 */
	public int getCompletedPercentage() {
		return getTotalSize() == 0 ? 0 : (int) Math.round(completedRequests.size() * 100.0 / getTotalSize());
	}
	
	/**
	 * Getter for the pendingRequests field
	 * @return the pendingRequests
	 */
	public IBackLog<RegradeRequest> getPendingRequests() {
		return pendingRequests;
	}

	/**
	 * Getter for the completedRequests field
	 * @return the completedRequests
	 */
	public CompletedRegradesStack getCompletedRequests() {
		return completedRequests;
	}

	/**
	 * Compares an Assignment to this
	 * @param a Assignment to compare
	 * @return positive/negative or 0 depending on this
	 */
	@Override
	public int compareTo(Assignment a) {
		return this.assignmentName.compareToIgnoreCase(a.assignmentName);
	}

	/**
	 * Formats Assignment information into a String for file-saving purposes
	 * @return String representation of Assignment
	 */
	@Override
	public String toString() {
		return getAssignmentName();
	}
	
}
