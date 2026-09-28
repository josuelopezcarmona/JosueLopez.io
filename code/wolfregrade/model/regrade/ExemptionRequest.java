/**
 * 
 */
package edu.ncsu.csc216.wolf_regrade.model.regrade;

/**
 * This class represents a RegradeRequest in the ExemptionRequest form
 * @author Josue Lopez
 */
public class ExemptionRequest extends RegradeRequest {
	
	/** RegradeRequest type **/
	public static final String TYPE = "Exemption";
	
	/** open text name **/
	public static final String OPEN_TEXT_NAME = "Circumstance Description";
	
	/** policy reference **/
	private String policyReference;

	/**
	 * Constructor for the class ExemptionRequest
	 * @param studentName name of the class
	 * @param unityId unity ID of student
	 * @param grader of ExemptionRequest
	 * @param policyReference policy reference
	 * @param circumstanceDescription circumstance description
	 * @throws IllegalArgumentException if policyReference is null or empty
	 */
	public ExemptionRequest(String studentName, String unityId, String grader, String policyReference, String circumstanceDescription) {
		super(studentName, unityId, grader, circumstanceDescription);
		setPolicyReference(policyReference);
	}

	/**
	 * Getter for the policyReference field
	 * @return the policyReference
	 */
	public String getPolicyReference() {
		return policyReference;
	}

	/**
	 * Setter for the policyReference field
	 * @param policyReference the policyReference to set
	 */
	public void setPolicyReference(String policyReference) {
		if(policyReference == null || policyReference.isEmpty() || policyReference.isBlank()) throw new IllegalArgumentException("Invalid regrade request.");
		
		this.policyReference = policyReference.trim();
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
		return "|" + getPolicyReference();
	}

}
