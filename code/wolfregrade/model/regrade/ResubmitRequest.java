/**
 * 
 */
package edu.ncsu.csc216.wolf_regrade.model.regrade;

/**
 * This class represents a RegradeRequest in the ResubmitRequest form
 * @author Josue Lopez
 */
public class ResubmitRequest extends RegradeRequest {
	
	/** RegradeRequest type **/
	public static final String TYPE = "Resubmit";
	
	/** open text name **/
	public static final String OPEN_TEXT_NAME = "Resubmit Reason";
	
	/** repo link 0f submission **/
	private String repoLink;
	
	/** commit hash of submission **/
	private String commitHash;

	/**
	 * Constructor for the ResubmitRequest class
	 * @param studentName name of student
	 * @param unityId unity ID of student
	 * @param grader grader of ResubmitRequest
	 * @param repoLink of a submission
	 * @param commitHash of a submission
	 * @param resubmitReason reason for resubmitting
	 * @throws IllegalArgumentException if repoLink or commitHash is null or empty
	 */
	public ResubmitRequest(String studentName, String unityId, String grader, String repoLink, String commitHash, String resubmitReason) {
		super(studentName, unityId, grader, resubmitReason);
		setRepoLink(repoLink);
		setCommitHash(commitHash);
	}

	/**
	 * Getter for the field repoLink
	 * @return the repoLink
	 */
	public String getRepoLink() {
		return repoLink;
	}


	/**
	 * Setter for the field repoLink
	 * @param repoLink the repoLink to set
	 */
	public void setRepoLink(String repoLink) {
		if(repoLink == null || repoLink.isEmpty() || repoLink.isBlank()) throw new IllegalArgumentException("Invalid regrade request.");
		
		this.repoLink = repoLink.trim();
	}



	/**
	 * getter for the field commitHash
	 * @return the commitHash
	 */
	public String getCommitHash() {
		return commitHash;
	}



	/**
	 * Setter for the field commitHash
	 * @param commitHash the commitHash to set
	 */
	public void setCommitHash(String commitHash) {
		if(commitHash == null || commitHash.isEmpty() || commitHash.isBlank()) throw new IllegalArgumentException("Invalid regrade request.");
		
		this.commitHash = commitHash.trim();
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
		return "|" + getRepoLink() + "|" + getCommitHash();
	}

}
