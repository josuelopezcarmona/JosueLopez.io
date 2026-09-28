/**
 * 
 */
package edu.ncsu.csc216.wolf_regrade.model.regrade;

import edu.ncsu.csc216.wolf_regrade.model.course.Assignment;

/**
 * This class represents a regrade request in the WolfRegrade system.
 * @author Josue Lopez
 */
public abstract class RegradeRequest implements Comparable<RegradeRequest> {
	
	/** resolution for the request has a value of: Closed **/
	public static final String RESOLUTION_CLOSED = "Closed";
	
	/** resolution for the request has a value of: Duplicate **/
	public static final String RESOLUTION_DUPLICATE = "Duplicate";
	
	/** resolution for the request has a value of: AdditionalInfo **/
	public static final String RESOLUTION_ADDITIONAL_INFO = "AdditionalInfo";
	
	/** name of student **/
	private String studentName;
	
	/** unity ID of student **/
	private String unityId;
	
	/** grader of the RegradeRequest **/
	private String grader;
	
	/** resolution of RegradeRequest **/
	private String resolution;
	
	/** if RegradeRequest changed grade **/
	private boolean gradeChanged;
	
	/** open text of RegradeRequest**/
	private String openText;
	
	/** Back-reference to the Assignment for RegradeRequest **/
	private Assignment assignment;

	/**
	 * Constructor for RegradeRequest
	 * @param studentName name of student 
	 * @param unityId unity ID of student
	 * @param grader of RegradeRequest
	 * @param openText open text of RegradeRequest
	 * @throws IllegalArgumentException if any string parameter is null or empty
	 */
	public RegradeRequest(String studentName, String unityId, String grader, String openText) {
		setStudentName(studentName);
        setUnityId(unityId);
        setGrader(grader);
        setOpenText(openText);
        
        this.resolution = null;
        this.gradeChanged = false;
        this.assignment = null;
	}
	
	/**
	 * gets the request type identifier
	 * @return String as request type identifier
	 */
	public abstract String getType();
	
	/**
	 * gets a descriptive label for the open-text field
	 * @return String as a descriptive label for the open-text field
	 */
	public abstract String getOpenTextName();
	
	/**
	 * gets a pipe-delimited string of the subtype-specific fields for use in toString
	 * @return String for the subtype-specific fields
	 */
	protected abstract String getSubtypeFields();

	/**
	 * Getter for the field: studentName
	 * @return the studentName
	 */
	public String getStudentName() {
		return studentName;
	}

	/**
	 * Setter for the field: studentName
	 * @param studentName the studentName to set
	 * @throws IllegalArgumentException if studentName is null or empty
	 */
	public void setStudentName(String studentName) {
		if(studentName == null || studentName.isEmpty() || studentName.isBlank()) throw new IllegalArgumentException("Invalid regrade request.");
		
		this.studentName = studentName.trim();
	}

	/**
	 * Getter for the field: unityId
	 * @return the unityId
	 */
	public String getUnityId() {
		return unityId;
	}

	/**
	 * Setter for the field: unityId
	 * @param unityId the unityId to set
	 * @throws IllegalArgumentException if unityId is null or empty
	 */
	public void setUnityId(String unityId) {
		if(unityId == null || unityId.isEmpty() || unityId.isBlank()) throw new IllegalArgumentException("Invalid regrade request.");
		
		this.unityId = unityId.trim();
	}

	/**
	 * Getter for the field: grader
	 * @return the grader
	 */
	public String getGrader() {
		return grader;
	}

	/**
	 * Setter for the field: grader
	 * @param grader the grader to set
	 * @throws IllegalArgumentException if grader is null or empty
	 */
	public void setGrader(String grader) {
		if(grader == null || grader.isEmpty() || grader.isBlank()) throw new IllegalArgumentException("Invalid regrade request.");
		this.grader = grader.trim();
	}

	/**
	 * Getter for the field: resolution
	 * @return the resolution
	 */
	public String getResolution() {
		return resolution;
	}

	/**
	 * Setter for the field: resolution
	 * @param resolution the resolution to set
	 * @throws IllegalArgumentException if resolution isn't null or any of
	 * the valid constants
	 */
	public void setResolution(String resolution) {
		if(resolution != null && !RESOLUTION_CLOSED.equals(resolution)
				&& !RESOLUTION_DUPLICATE.equals(resolution) && !RESOLUTION_ADDITIONAL_INFO.equals(resolution)) {
            throw new IllegalArgumentException("Invalid resolution.");
        }
		
		this.resolution = resolution;
	}

	/**
	 * Getter for the field: gradeChanged
	 * @return the gradeChanged
	 */
	public boolean isGradeChanged() {
		return gradeChanged;
	}

	/**
	 * Setter for the field: gradeChanged
	 * @param gradeChanged the gradeChanged to set
	 * @throws IllegalArgumentException if gradeChanged is true and resolution is not "Closed"
	 */
	public void setGradeChanged(boolean gradeChanged) {
		if(gradeChanged && !RESOLUTION_CLOSED.equals(resolution)) throw new IllegalArgumentException("Grade can only be changed with a Closed resolution.");
		
		this.gradeChanged = gradeChanged;
	}

	/**
	 * Getter for the field: openText
	 * @return the openText
	 */
	public String getOpenText() {
		return openText;
	}

	/**
	 * Setter for the field: openText
	 * @param openText the openText to set
	 * @throws IllegalArgumentException if openText is empty or null
	 */
	public void setOpenText(String openText) {
		if(openText == null || openText.isEmpty() || openText.isBlank()) throw new IllegalArgumentException("Invalid regrade request.");
		
		this.openText = openText.trim();
	}

	/**
	 * Getter for the field: assignment
	 * @return the assignment
	 */
	public Assignment getAssignment() {
		return assignment;
	}

	/**
	 * Setter for the field: assignment
	 * @param assignment the assignment to set
	 * @throws IllegalArgumentException if the request is already assigned to a different Assignment
	 */
	public void setAssignment(Assignment assignment) {
		if(this.assignment != null && this.assignment != assignment)
			throw new IllegalArgumentException("Request already belongs to an assignment.");
		
		this.assignment = assignment;
	}
	
	/**
	 * Gets the name of assignment field
	 * @return name of assignment or "" if null
	 */
	public String getAssignmentName() {
		return assignment != null ? assignment.getAssignmentName() : "";
	}

	/**
	 * Compares another RegradeRequest to this
	 * @param r RegradeRequest to compare
	 * @return int negative, zero or positive
	 */
	@Override
	public int compareTo(RegradeRequest r) {
		String thisAss = getAssignmentName();
		String otherAss = r.getAssignmentName();
		
		return thisAss.compareToIgnoreCase(otherAss) == 0 ? 
				getStudentName().compareToIgnoreCase(r.getStudentName()) : thisAss.compareToIgnoreCase(otherAss);
	}

	/**
	 * Formats RegradeRequest information for file-saving purposes
	 * @return String representation of RegradeRequest
	 */
	@Override
	public String toString() {
		String rr = getType() + "|" 
				+ getStudentName() + "|"
				+ getUnityId() + "|"
				+ getGrader()
				+ getSubtypeFields();
		
		if(resolution != null) rr += "|" + getResolution() + "|" + isGradeChanged();
		
		rr += "\n" + getOpenText();
		return rr;
	}
	
}
