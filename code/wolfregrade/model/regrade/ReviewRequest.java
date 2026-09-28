/**
 * 
 */
package edu.ncsu.csc216.wolf_regrade.model.regrade;

/**
 * This class represents a RegradeRequest in the ReviewRequest form
 * @author Josue Lopez
 */
public class ReviewRequest extends RegradeRequest {
	
	/** Item Type **/
	public static final String TYPE = "Review";
	
	/** open text name **/
	public static final String OPEN_TEXT_NAME = "Rationale";
	
	/**  item the student wants reviewed **/
	private String reviewItem;

	/**
	 * Constructor for the ReviewRequest
	 * @param studentName name of student
	 * @param unityId unity id of student
	 * @param grader reviewing the request
	 * @param reviewItem review item 
	 * @param rationale for the review
	 * @throws IllegalArgumentException if reviewItem in null or empty
	 */
	public ReviewRequest(String studentName, String unityId, String grader, String reviewItem, String rationale) {
		super(studentName, unityId, grader, rationale);
		setReviewItem(reviewItem);
	}

	
	/**
	 * Getter for the field: reviewItem
	 * @return the reviewItem
	 */
	public String getReviewItem() {
		return reviewItem;
	}


	/**
	 * Setter for the filed: reviewItem
	 * @param reviewItem the reviewItem to set
	 */
	public void setReviewItem(String reviewItem) {
		if(reviewItem == null || reviewItem.isEmpty() || reviewItem.isBlank()) throw new IllegalArgumentException("Invalid regrade request.");
		
		
		this.reviewItem = reviewItem.trim();
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
	 * @return String as  a descriptive label for the open-text field
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
		return "|" + getReviewItem();
	}

}
