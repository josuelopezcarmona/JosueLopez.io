/**
 * 
 */
package edu.ncsu.csc216.wolf_regrade.model.regrade;

/**
 * This class represents a RegradeRequest in the ClarifyRequest form
 * @author Josue Lopez
 */
public class ClarifyRequest extends RegradeRequest {
	
	/** type of RegradeRequest*/ 
	public static final String TYPE = "Clarify";
	
	/** open text name **/
	public static final String OPEN_TEXT_NAME = "Question";
	
	/** item type **/
	private String feedbackItem;

	/**
	 * Constructor for the class ClarifyRequest
	 * @param studentName name of student
	 * @param unityId unity ID of student
	 * @param grader of ClarifyRequest
	 * @param feedbackItem feedback item
	 * @param question of ClarifyRequest
	 * @throws IllegalArgumentException if feedbackItem is null or empty
	 */
	public ClarifyRequest(String studentName, String unityId, String grader, String feedbackItem, String question) {
		super(studentName, unityId, grader, question);
		setFeedbackItem(feedbackItem);
	}

	/**
	 * Getter for the feedbackItem
	 * @return the feedbackItem
	 */
	public String getFeedbackItem() {
		return feedbackItem;
	}

	/**
	 * Setter for the field feedbackItem
	 * @param feedbackItem the feedbackItem to set
	 */
	public void setFeedbackItem(String feedbackItem) {
		if(feedbackItem == null || feedbackItem.isEmpty() || feedbackItem.isBlank()) throw new IllegalArgumentException("Invalid regrade request.");
		
		this.feedbackItem = feedbackItem.trim();
	}

	/**
	 * Setter for the resolution of ClarifyRequest
	 * @param resolution of ClarifyRequest
	 */
	@Override
	public void setResolution(String resolution) {
		if(resolution != null && !RESOLUTION_ADDITIONAL_INFO.equals(resolution) && !RESOLUTION_DUPLICATE.equals(resolution))
			throw new IllegalArgumentException("Invalid resolution.");
		
		super.setResolution(resolution);
	}

	/**
	 * gets the request type identifier
	 * @return String as request type identifier
	 */
	@Override
	public String getType() {
		return TYPE;
	}

	/**
	 * gets a descriptive label for the open-text field
	 * @return String as a descriptive label for the open-text field
	 */
	@Override
	public String getOpenTextName() {
		return OPEN_TEXT_NAME;
	}

	/**
	 * gets a pipe-delimited string of the subtype-specific fields for use in toString
	 * @return String for the subtype-specific fields
	 */
	@Override
	protected String getSubtypeFields() {
		return "|" + getFeedbackItem();
	}

}
