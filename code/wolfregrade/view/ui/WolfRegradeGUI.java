package edu.ncsu.csc216.wolf_regrade.view.ui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import edu.ncsu.csc216.wolf_regrade.model.course.Assignment;
import edu.ncsu.csc216.wolf_regrade.model.course.Course;
import edu.ncsu.csc216.wolf_regrade.model.io.RegradeReader;
import edu.ncsu.csc216.wolf_regrade.model.regrade.ClarifyRequest;
import edu.ncsu.csc216.wolf_regrade.model.regrade.ExemptionRequest;
import edu.ncsu.csc216.wolf_regrade.model.regrade.RegradeRequest;
import edu.ncsu.csc216.wolf_regrade.model.regrade.ResubmitRequest;
import edu.ncsu.csc216.wolf_regrade.model.regrade.ReviewRequest;

/**
 * GUI for the WolfRegrade application. Provides the view layer for managing
 * course regrade requests organized by assignment using Java Swing.
 *
 * @author Dr. Sarah Heckman
 * 
 * Anthropic Claude Opus 4.6 was used to develop this GUI iteratively under Dr. Heckman's
 * supervision.  The AI development utilized the requirements, design, and implementation.
 * 
 * Why was GenAI used in this case? GUI development is not a direct learning outcome for the 
 * course; system testing is.  The GUI provides the opportunity for you to system test your
 * program. Additionally, Dr. Heckman has over 25 years of Java development experience. She
 * has written many, many, many GUIs and decided her time would be better spent on other 
 * tasks rather than writing yet another GUI. Why can't you use GenAI for task other than 
 * debugging (see https://courses.csc.ncsu.edu/csc216/syllabi/S26_CSC216_Syllabus/#ai-policy)? 
 * Because you need to build up that knowledge and experience by actually writing code and 
 * tests so that you can later supervise GenAI tools in your professional work.
 */
public class WolfRegradeGUI extends JFrame {

	/** Serial version UID */
	private static final long serialVersionUID = 1L;

	/** The course regrades model */
	private Course courseRegrades;

	/** Course name label */
	private JLabel lblCourseName;

	// Assignments
	/** Assignment table model */
	private DefaultTableModel assignmentTableModel;
	/** Assignment table */
	private JTable assignmentTable;

	// Pending requests
	/** Pending requests table model */
	private DefaultTableModel pendingTableModel;
	/** Pending requests table */
	private JTable pendingTable;

	// Completed requests
	/** Completed requests table model */
	private DefaultTableModel completedTableModel;
	/** Completed requests table */
	private JTable completedTable;

	// Filter
	/** Grader filter text field */
	private JTextField txtFilterGrader;

	/** Assignment table column names */
	private static final String[] ASSIGNMENT_COLUMNS = {"Assignment", "Pending", "Completed", "Total", "% Complete"};
	/** Pending table column names */
	private static final String[] PENDING_COLUMNS = {"Type", "Assignment", "Student", "Unity ID", "Grader"};
	/** Completed table column names */
	private static final String[] COMPLETED_COLUMNS = {"Type", "Assignment", "Student", "Unity ID", "Grader", "Resolution", "Grade Changed"};

	/**
	 * Constructs the WolfRegradeGUI and sets up all components.
	 */
	public WolfRegradeGUI() {
		setTitle("WolfRegrade");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(1300, 750);

		setJMenuBar(buildMenuBar());
		getContentPane().setLayout(new BorderLayout());

		// Course name at top
		lblCourseName = new JLabel("Course Name: (none)");
		JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		topPanel.add(lblCourseName);
		getContentPane().add(topPanel, BorderLayout.NORTH);

		// Center: assignments (left) | pending + completed (right)
		JSplitPane mainSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		mainSplit.setLeftComponent(buildAssignmentPanel());

		JPanel requestsPanel = new JPanel(new GridLayout(1, 2, 5, 0));
		requestsPanel.add(buildPendingPanel());
		requestsPanel.add(buildCompletedPanel());
		mainSplit.setRightComponent(requestsPanel);
		mainSplit.setDividerLocation(300);

		getContentPane().add(mainSplit, BorderLayout.CENTER);
		
		setVisible(true);
	}

	/**
	 * Builds the menu bar with File menu.
	 * @return the menu bar
	 */
	private JMenuBar buildMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("File");

		JMenuItem newItem = new JMenuItem("New Course Regrades");
		newItem.addActionListener(e -> doNew());
		fileMenu.add(newItem);

		JMenuItem loadItem = new JMenuItem("Load Course Regrades");
		loadItem.addActionListener(e -> doLoad());
		fileMenu.add(loadItem);

		JMenuItem saveItem = new JMenuItem("Save Course Regrades");
		saveItem.addActionListener(e -> doSave());
		fileMenu.add(saveItem);

		fileMenu.addSeparator();

		JMenuItem quitItem = new JMenuItem("Quit");
		quitItem.addActionListener(e -> doQuit());
		fileMenu.add(quitItem);

		menuBar.add(fileMenu);
		return menuBar;
	}

	/**
	 * Builds the assignment management panel.
	 * @return the assignment panel
	 */
	private JPanel buildAssignmentPanel() {
		JPanel panel = new JPanel(new BorderLayout(5, 5));
		panel.setBorder(BorderFactory.createTitledBorder("Assignments"));

		assignmentTableModel = new DefaultTableModel(ASSIGNMENT_COLUMNS, 0) {
			private static final long serialVersionUID = 1L;
			/**
			 * Returns true if the cell is editable
			 * @param row row index
			 * @param column column index
			 * @return true if editable
			 */
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		assignmentTable = new JTable(assignmentTableModel);
		assignmentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		assignmentTable.getSelectionModel().addListSelectionListener(e -> {
			if (!e.getValueIsAdjusting()) {
				onAssignmentSelected();
			}
		});
		panel.add(new JScrollPane(assignmentTable), BorderLayout.CENTER);

		JPanel btnPanel = new JPanel(new FlowLayout());
		JButton btnAdd = new JButton("Add Assignment");
		btnAdd.addActionListener(e -> doAddAssignment());
		btnPanel.add(btnAdd);
		JButton btnEdit = new JButton("Edit Assignment");
		btnEdit.addActionListener(e -> doEditAssignment());
		btnPanel.add(btnEdit);
		JButton btnRemove = new JButton("Remove Assignment");
		btnRemove.addActionListener(e -> doRemoveAssignment());
		btnPanel.add(btnRemove);
		panel.add(btnPanel, BorderLayout.SOUTH);

		return panel;
	}

	/**
	 * Builds the pending requests panel.
	 * @return the pending requests panel
	 */
	private JPanel buildPendingPanel() {
		JPanel panel = new JPanel(new BorderLayout(5, 5));
		panel.setBorder(BorderFactory.createTitledBorder("Pending Regrade Requests"));

		// Filter row
		JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		txtFilterGrader = new JTextField(10);
		filterPanel.add(new JLabel("Grader:"));
		filterPanel.add(txtFilterGrader);
		JButton btnFilterGrader = new JButton("Filter by Grader");
		btnFilterGrader.addActionListener(e -> doFilterByGrader());
		filterPanel.add(btnFilterGrader);

		JButton btnShowAll = new JButton("Clear Filter");
		btnShowAll.addActionListener(e -> {
			int idx = assignmentTable.getSelectedRow();
			if (idx < 0 || courseRegrades == null) {
				pendingTableModel.setRowCount(0);
				completedTableModel.setRowCount(0);
				return;
			}
			Assignment a = courseRegrades.getAssignment(idx);
			loadPendingTable(a.getPendingRequestsAsArray());
			loadCompletedTable(a.getCompletedRequestsAsArray());
		});
		filterPanel.add(btnShowAll);

		panel.add(filterPanel, BorderLayout.NORTH);

		// Table
		pendingTableModel = new DefaultTableModel(PENDING_COLUMNS, 0) {
			private static final long serialVersionUID = 1L;
			/**
			 * Returns true if the cell is editable
			 * @param row row index
			 * @param column column index
			 * @return true if editable
			 */
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		pendingTable = new JTable(pendingTableModel);
		pendingTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		panel.add(new JScrollPane(pendingTable), BorderLayout.CENTER);

		// Buttons
		JPanel btnPanel = new JPanel(new GridLayout(2, 1, 5, 5));

		JPanel row1 = new JPanel(new FlowLayout());
		JButton btnAdd = new JButton("Add Request");
		btnAdd.addActionListener(e -> doAddRequest());
		row1.add(btnAdd);
		JButton btnEdit = new JButton("Edit Request");
		btnEdit.addActionListener(e -> doEditRequest());
		row1.add(btnEdit);
		JButton btnRemove = new JButton("Remove Request");
		btnRemove.addActionListener(e -> doRemoveRequest());
		row1.add(btnRemove);
		btnPanel.add(row1);

		JPanel row2 = new JPanel(new FlowLayout());
		JButton btnComplete = new JButton("Complete Request");
		btnComplete.addActionListener(e -> doCompleteRequest());
		row2.add(btnComplete);
		btnPanel.add(row2);

		panel.add(btnPanel, BorderLayout.SOUTH);
		return panel;
	}

	/**
	 * Builds the completed requests panel.
	 * @return the completed requests panel
	 */
	private JPanel buildCompletedPanel() {
		JPanel panel = new JPanel(new BorderLayout(5, 5));
		panel.setBorder(BorderFactory.createTitledBorder("Completed Regrade Requests"));

		completedTableModel = new DefaultTableModel(COMPLETED_COLUMNS, 0) {
			private static final long serialVersionUID = 1L;
			/**
			 * Returns true if the cell is editable
			 * @param row row index
			 * @param column column index
			 * @return true if editable
			 */
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		completedTable = new JTable(completedTableModel);
		completedTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		panel.add(new JScrollPane(completedTable), BorderLayout.CENTER);

		JPanel btnPanel = new JPanel(new FlowLayout());
		JButton btnUndo = new JButton("Undo Last Completion");
		btnUndo.addActionListener(e -> doUndoLastCompletion());
		btnPanel.add(btnUndo);
		panel.add(btnPanel, BorderLayout.SOUTH);

		return panel;
	}

	// -----------------------------------------------------------------------
	// File menu actions
	// -----------------------------------------------------------------------

	/**
	 * Creates new course regrades (UC1).
	 */
	private void doNew() {
		if (courseRegrades != null && courseRegrades.isChanged()) {
			int result = JOptionPane.showConfirmDialog(this,
					"Current course regrades have unsaved changes. Continue?",
					"Unsaved Changes", JOptionPane.YES_NO_OPTION);
			if (result != JOptionPane.YES_OPTION) {
				return;
			}
		}

		JTextField nameField = new JTextField();

		Object[] message = {
				"Course Name:", nameField
		};

		while (true) {
			int option = JOptionPane.showConfirmDialog(this, message,
					"New Course Regrades", JOptionPane.OK_CANCEL_OPTION);
			if (option != JOptionPane.OK_OPTION) {
				return;
			}

			try {
				String name = nameField.getText();
				courseRegrades = new Course(name);
				updateAll();
				return;
			} catch (IllegalArgumentException e) {
				JOptionPane.showMessageDialog(this, e.getMessage(), "Error",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	/**
	 * Loads course regrades from file (UC2).
	 */
	private void doLoad() {
		if (courseRegrades != null && courseRegrades.isChanged()) {
			int result = JOptionPane.showConfirmDialog(this,
					"Current course regrades have unsaved changes. Continue?",
					"Unsaved Changes", JOptionPane.YES_NO_OPTION);
			if (result != JOptionPane.YES_OPTION) {
				return;
			}
		}

		JFileChooser chooser = new JFileChooser(".");
		int returnVal = chooser.showOpenDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			try {
				courseRegrades = RegradeReader.readRegradeFile(chooser.getSelectedFile());
				updateAll();
			} catch (IllegalArgumentException e) {
				JOptionPane.showMessageDialog(this, "Unable to load file.",
						"Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	/**
	 * Saves course regrades to file (UC3).
	 */
	private void doSave() {
		if (courseRegrades == null) {
			JOptionPane.showMessageDialog(this, "No course regrades to save.",
					"Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		JFileChooser chooser = new JFileChooser(".");
		int returnVal = chooser.showSaveDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			try {
				courseRegrades.saveCourseRegrades(chooser.getSelectedFile());
			} catch (IllegalArgumentException e) {
				JOptionPane.showMessageDialog(this, "Unable to save file.",
						"Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	/**
	 * Quits the application.
	 */
	private void doQuit() {
		if (courseRegrades != null && courseRegrades.isChanged()) {
			int result = JOptionPane.showConfirmDialog(this,
					"Current course regrades have unsaved changes. Save before quitting?",
					"Unsaved Changes", JOptionPane.YES_NO_CANCEL_OPTION);
			if (result == JOptionPane.YES_OPTION) {
				doSave();
			} else if (result == JOptionPane.CANCEL_OPTION) {
				return;
			}
		}
		System.exit(0);
	}

	// -----------------------------------------------------------------------
	// Assignment actions
	// -----------------------------------------------------------------------

	/**
	 * Adds an assignment.
	 */
	private void doAddAssignment() {
		if (courseRegrades == null) {
			return;
		}
		String name = JOptionPane.showInputDialog(this, "Assignment Name:");
		if (name != null) {
			try {
				courseRegrades.addAssignment(name);
				updateAll();
			} catch (IllegalArgumentException e) {
				JOptionPane.showMessageDialog(this, e.getMessage(), "Error",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	/**
	 * Edits an assignment name.
	 */
	private void doEditAssignment() {
		if (courseRegrades == null) {
			return;
		}
		int idx = assignmentTable.getSelectedRow();
		if (idx < 0) {
			JOptionPane.showMessageDialog(this, "No assignment selected.",
					"Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		String current = courseRegrades.getAssignment(idx).getAssignmentName();
		String newName = JOptionPane.showInputDialog(this, "New Assignment Name:", current);
		if (newName != null) {
			try {
				courseRegrades.editAssignment(idx, newName);
				updateAll();
			} catch (IllegalArgumentException e) {
				JOptionPane.showMessageDialog(this, e.getMessage(), "Error",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	/**
	 * Removes an assignment and all its requests.
	 */
	private void doRemoveAssignment() {
		if (courseRegrades == null) {
			return;
		}
		int idx = assignmentTable.getSelectedRow();
		if (idx < 0) {
			JOptionPane.showMessageDialog(this, "No assignment selected.",
					"Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		Assignment a = courseRegrades.getAssignment(idx);
		String msg = "Remove assignment '" + a.getAssignmentName()
				+ "' and ALL its " + a.getTotalSize() + " requests?";
		int result = JOptionPane.showConfirmDialog(this, msg, "Confirm Remove",
				JOptionPane.YES_NO_OPTION);
		if (result == JOptionPane.YES_OPTION) {
			courseRegrades.removeAssignment(idx);
			updateAll();
		}
	}

	/**
	 * Called when an assignment is selected in the assignment table.
	 * Updates the pending and completed tables for the selected assignment.
	 */
	private void onAssignmentSelected() {
		int idx = assignmentTable.getSelectedRow();
		if (idx < 0 || courseRegrades == null) {
			pendingTableModel.setRowCount(0);
			completedTableModel.setRowCount(0);
			return;
		}
		Assignment a = courseRegrades.getAssignment(idx);
		loadPendingTable(a.getPendingRequestsAsArray());
		loadCompletedTable(a.getCompletedRequestsAsArray());
	}

	// -----------------------------------------------------------------------
	// Regrade request actions
	// -----------------------------------------------------------------------

	/**
	 * Adds a new regrade request.
	 */
	private void doAddRequest() {
		if (courseRegrades == null) {
			return;
		}
		int assignmentIdx = assignmentTable.getSelectedRow();
		if (assignmentIdx < 0) {
			JOptionPane.showMessageDialog(this,
					"Please select an assignment before adding a request.",
					"Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		RequestDialog dialog = new RequestDialog(this, "Add Regrade Request", null, -1);
		dialog.setVisible(true);
		if (dialog.isConfirmed()) {
			updateAll();
		}
	}

	/**
	 * Edits an existing regrade request.
	 */
	private void doEditRequest() {
		if (courseRegrades == null) {
			return;
		}
		int assignmentIdx = assignmentTable.getSelectedRow();
		int requestIdx = pendingTable.getSelectedRow();
		if (assignmentIdx < 0 || requestIdx < 0) {
			JOptionPane.showMessageDialog(this, "No request selected.",
					"Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		RegradeRequest request = courseRegrades.getAssignment(assignmentIdx).getPendingRequest(requestIdx);
		RequestDialog dialog = new RequestDialog(this, "Edit Regrade Request", request, requestIdx);
		dialog.setVisible(true);
		if (dialog.isConfirmed()) {
			updateAll();
		}
	}

	/**
	 * Removes a pending regrade request.
	 */
	private void doRemoveRequest() {
		if (courseRegrades == null) {
			return;
		}
		int assignmentIdx = assignmentTable.getSelectedRow();
		int requestIdx = pendingTable.getSelectedRow();
		if (assignmentIdx < 0 || requestIdx < 0) {
			JOptionPane.showMessageDialog(this, "No request selected.",
					"Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		int result = JOptionPane.showConfirmDialog(this,
				"Remove this regrade request?", "Confirm Remove",
				JOptionPane.YES_NO_OPTION);
		if (result == JOptionPane.YES_OPTION) {
			courseRegrades.removePendingRequest(assignmentIdx, requestIdx);
			updateAll();
		}
	}

	/**
	 * Completes a pending regrade request.
	 */
	private void doCompleteRequest() {
		if (courseRegrades == null) {
			return;
		}
		int assignmentIdx = assignmentTable.getSelectedRow();
		int requestIdx = pendingTable.getSelectedRow();
		if (assignmentIdx < 0 || requestIdx < 0) {
			JOptionPane.showMessageDialog(this, "No request selected.",
					"Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		JComboBox<String> resolutionCombo = new JComboBox<>(new String[]{
				RegradeRequest.RESOLUTION_CLOSED,
				RegradeRequest.RESOLUTION_DUPLICATE,
				RegradeRequest.RESOLUTION_ADDITIONAL_INFO
		});
		JCheckBox gradeChangedBox = new JCheckBox("Grade Changed");

		Object[] message = {
				"Resolution:", resolutionCombo,
				gradeChangedBox
		};

		int option = JOptionPane.showConfirmDialog(this, message,
				"Complete Request", JOptionPane.OK_CANCEL_OPTION);
		if (option == JOptionPane.OK_OPTION) {
			try {
				String resolution = (String) resolutionCombo.getSelectedItem();
				boolean gradeChanged = gradeChangedBox.isSelected();
				courseRegrades.completeRequest(assignmentIdx, requestIdx, resolution, gradeChanged);
				updateAll();
			} catch (IllegalArgumentException e) {
				JOptionPane.showMessageDialog(this, e.getMessage(), "Error",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	// -----------------------------------------------------------------------
	// Completed actions
	// -----------------------------------------------------------------------

	/**
	 * Undoes the last completion for the selected assignment.
	 */
	private void doUndoLastCompletion() {
		if (courseRegrades == null) {
			return;
		}
		int assignmentIdx = assignmentTable.getSelectedRow();
		if (assignmentIdx < 0) {
			JOptionPane.showMessageDialog(this, "No assignment selected.",
					"Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		try {
			courseRegrades.undoLastCompletion(assignmentIdx);
			updateAll();
		} catch (IllegalArgumentException e) {
			JOptionPane.showMessageDialog(this, e.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	// -----------------------------------------------------------------------
	// Filter actions
	// -----------------------------------------------------------------------

	/**
	 * Filters pending requests by grader across all assignments.
	 */
	private void doFilterByGrader() {
		if (courseRegrades == null) {
			return;
		}
		String grader = txtFilterGrader.getText();
		if (grader == null || grader.trim().isEmpty()) {
			JOptionPane.showMessageDialog(this, "No grader specified.",
					"Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		try {
			String[][] filtered = courseRegrades.filterByGrader(grader.trim());
			loadPendingTable(filtered);
		} catch (IllegalArgumentException e) {
			JOptionPane.showMessageDialog(this, e.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	// -----------------------------------------------------------------------
	// UI refresh
	// -----------------------------------------------------------------------

	/**
	 * Refreshes all UI components from the model.
	 */
	private void updateAll() {
		if (courseRegrades == null) {
			lblCourseName.setText("Course Name: (none)");
			assignmentTableModel.setRowCount(0);
			pendingTableModel.setRowCount(0);
			completedTableModel.setRowCount(0);
			return;
		}

		lblCourseName.setText("Course Name: " + courseRegrades.getCourseName());

		// Assignments
		int selectedAssignment = assignmentTable.getSelectedRow();
		assignmentTableModel.setRowCount(0);
		String[][] assignments = courseRegrades.getAssignmentsAsDetailArray();
		for (String[] row : assignments) {
			assignmentTableModel.addRow(row);
		}
		// Restore selection
		if (selectedAssignment >= 0 && selectedAssignment < assignmentTableModel.getRowCount()) {
			assignmentTable.setRowSelectionInterval(selectedAssignment, selectedAssignment);
		}

		// Pending/Completed refresh based on selected assignment
		onAssignmentSelected();
	}

	/**
	 * Loads data into the pending table.
	 * @param data the 2D array of pending request data
	 */
	private void loadPendingTable(String[][] data) {
		pendingTableModel.setRowCount(0);
		for (String[] row : data) {
			pendingTableModel.addRow(row);
		}
	}

	/**
	 * Loads data into the completed table.
	 * @param data the 2D array of completed request data
	 */
	private void loadCompletedTable(String[][] data) {
		completedTableModel.setRowCount(0);
		for (String[] row : data) {
			completedTableModel.addRow(row);
		}
	}

	// -----------------------------------------------------------------------
	// Add/Edit Request Dialog
	// -----------------------------------------------------------------------

	/**
	 * Dialog for adding or editing a regrade request.
	 */
	private class RequestDialog extends JDialog {

		/** Serial version UID */
		private static final long serialVersionUID = 1L;

		/** Whether the dialog was confirmed */
		private boolean confirmed;

		// Common fields
		/** Student name field */
		private JTextField txtStudentName;
		/** Unity ID field */
		private JTextField txtUnityId;
		/** Grader text field */
		private JTextField txtGrader;

		// Type selection
		/** Review radio button */
		private JRadioButton rbReview;
		/** Resubmit radio button */
		private JRadioButton rbResubmit;
		/** Clarify radio button */
		private JRadioButton rbClarify;
		/** Exemption radio button */
		private JRadioButton rbExemption;

		// Review fields
		/** Review item field */
		private JTextField txtReviewItem;
		/** Rationale field */
		private JTextArea txtRationale;

		// Resubmit fields
		/** Repo link field */
		private JTextField txtRepoLink;
		/** Commit hash field */
		private JTextField txtCommitHash;
		/** Resubmit reason field */
		private JTextArea txtResubmitReason;

		// Clarify fields
		/** Feedback item field */
		private JTextField txtFeedbackItem;
		/** Question field */
		private JTextArea txtQuestion;

		// Exemption fields
		/** Policy reference field */
		private JTextField txtPolicyReference;
		/** Circumstance description field */
		private JTextArea txtCircumstanceDescription;

		/** Card layout for type-specific panels */
		private CardLayout cardLayout;
		/** Panel containing type-specific cards */
		private JPanel cardPanel;

		/** Edit index (-1 for add) */
		private int editIdx;
		/** Edit type (for locking type during edit) */
		private String editType;

		/**
		 * Constructs the request dialog.
		 * @param parent parent frame
		 * @param title dialog title
		 * @param existingRequest existing request for editing, or null for add
		 * @param editIdx index of request being edited, or -1 for add
		 */
		public RequestDialog(JFrame parent, String title, RegradeRequest existingRequest, int editIdx) {
			super(parent, title, true);
			this.editIdx = editIdx;
			this.editType = existingRequest != null ? existingRequest.getType() : null;
			this.confirmed = false;

			setSize(500, 500);
			setLocationRelativeTo(parent);
			setLayout(new BorderLayout(10, 10));

			add(buildCommonPanel(), BorderLayout.NORTH);
			add(buildTypePanel(), BorderLayout.CENTER);
			add(buildButtonPanel(), BorderLayout.SOUTH);

			if (existingRequest != null) {
				populateForEdit(existingRequest);
			}
		}

		/**
		 * Builds the common fields panel (type, student, unity ID, grader).
		 * @return the common panel
		 */
		private JPanel buildCommonPanel() {
			JPanel panel = new JPanel(new GridBagLayout());
			panel.setBorder(BorderFactory.createTitledBorder("Common Fields"));
			GridBagConstraints c = new GridBagConstraints();
			c.insets = new Insets(3, 5, 3, 5);
			c.fill = GridBagConstraints.HORIZONTAL;

			// Type selection
			c.gridx = 0; c.gridy = 0;
			panel.add(new JLabel("Type:"), c);

			JPanel typePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
			rbReview = new JRadioButton("Review");
			rbResubmit = new JRadioButton("Resubmit");
			rbClarify = new JRadioButton("Clarify");
			rbExemption = new JRadioButton("Exemption");
			ButtonGroup bg = new ButtonGroup();
			bg.add(rbReview);
			bg.add(rbResubmit);
			bg.add(rbClarify);
			bg.add(rbExemption);
			rbReview.setSelected(true);
			typePanel.add(rbReview);
			typePanel.add(rbResubmit);
			typePanel.add(rbClarify);
			typePanel.add(rbExemption);

			rbReview.addActionListener(e -> cardLayout.show(cardPanel, "review"));
			rbResubmit.addActionListener(e -> cardLayout.show(cardPanel, "resubmit"));
			rbClarify.addActionListener(e -> cardLayout.show(cardPanel, "clarify"));
			rbExemption.addActionListener(e -> cardLayout.show(cardPanel, "exemption"));

			c.gridx = 1; c.gridy = 0; c.weightx = 1;
			panel.add(typePanel, c);

			// Student name
			c.gridx = 0; c.gridy = 1; c.weightx = 0;
			panel.add(new JLabel("Student Name:"), c);
			txtStudentName = new JTextField();
			c.gridx = 1; c.weightx = 1;
			panel.add(txtStudentName, c);

			// Unity ID
			c.gridx = 0; c.gridy = 2; c.weightx = 0;
			panel.add(new JLabel("Unity ID:"), c);
			txtUnityId = new JTextField();
			c.gridx = 1; c.weightx = 1;
			panel.add(txtUnityId, c);

			// Grader (free-text field)
			c.gridx = 0; c.gridy = 3; c.weightx = 0;
			panel.add(new JLabel("Grader:"), c);
			txtGrader = new JTextField();
			c.gridx = 1; c.weightx = 1;
			panel.add(txtGrader, c);

			// Lock type during edit
			if (editType != null) {
				rbReview.setEnabled(false);
				rbResubmit.setEnabled(false);
				rbClarify.setEnabled(false);
				rbExemption.setEnabled(false);
			}

			return panel;
		}

		/**
		 * Builds the type-specific fields panel using CardLayout.
		 * @return the type panel
		 */
		private JPanel buildTypePanel() {
			cardLayout = new CardLayout();
			cardPanel = new JPanel(cardLayout);

			cardPanel.add(buildReviewCard(), "review");
			cardPanel.add(buildResubmitCard(), "resubmit");
			cardPanel.add(buildClarifyCard(), "clarify");
			cardPanel.add(buildExemptionCard(), "exemption");

			return cardPanel;
		}

		/**
		 * Builds the review-specific fields card.
		 * @return the review card panel
		 */
		private JPanel buildReviewCard() {
			JPanel panel = new JPanel(new GridBagLayout());
			panel.setBorder(BorderFactory.createTitledBorder("Review Fields"));
			GridBagConstraints c = new GridBagConstraints();
			c.insets = new Insets(3, 5, 3, 5);
			c.fill = GridBagConstraints.HORIZONTAL;

			c.gridx = 0; c.gridy = 0;
			panel.add(new JLabel("Question/Item:"), c);
			txtReviewItem = new JTextField();
			c.gridx = 1; c.weightx = 1;
			panel.add(txtReviewItem, c);

			c.gridx = 0; c.gridy = 1; c.weightx = 0;
			panel.add(new JLabel("Rationale:"), c);
			txtRationale = new JTextArea(4, 20);
			txtRationale.setLineWrap(true);
			c.gridx = 1; c.weightx = 1; c.weighty = 1;
			c.fill = GridBagConstraints.BOTH;
			panel.add(new JScrollPane(txtRationale), c);

			return panel;
		}

		/**
		 * Builds the resubmit-specific fields card.
		 * @return the resubmit card panel
		 */
		private JPanel buildResubmitCard() {
			JPanel panel = new JPanel(new GridBagLayout());
			panel.setBorder(BorderFactory.createTitledBorder("Resubmit Fields"));
			GridBagConstraints c = new GridBagConstraints();
			c.insets = new Insets(3, 5, 3, 5);
			c.fill = GridBagConstraints.HORIZONTAL;

			c.gridx = 0; c.gridy = 0;
			panel.add(new JLabel("Repo Link:"), c);
			txtRepoLink = new JTextField();
			c.gridx = 1; c.weightx = 1;
			panel.add(txtRepoLink, c);

			c.gridx = 0; c.gridy = 1; c.weightx = 0;
			panel.add(new JLabel("Commit Hash:"), c);
			txtCommitHash = new JTextField();
			c.gridx = 1; c.weightx = 1;
			panel.add(txtCommitHash, c);

			c.gridx = 0; c.gridy = 2; c.weightx = 0;
			panel.add(new JLabel("Reason:"), c);
			txtResubmitReason = new JTextArea(4, 20);
			txtResubmitReason.setLineWrap(true);
			c.gridx = 1; c.weightx = 1; c.weighty = 1;
			c.fill = GridBagConstraints.BOTH;
			panel.add(new JScrollPane(txtResubmitReason), c);

			return panel;
		}

		/**
		 * Builds the clarify-specific fields card.
		 * @return the clarify card panel
		 */
		private JPanel buildClarifyCard() {
			JPanel panel = new JPanel(new GridBagLayout());
			panel.setBorder(BorderFactory.createTitledBorder("Clarify Fields"));
			GridBagConstraints c = new GridBagConstraints();
			c.insets = new Insets(3, 5, 3, 5);
			c.fill = GridBagConstraints.HORIZONTAL;

			c.gridx = 0; c.gridy = 0;
			panel.add(new JLabel("Feedback Item:"), c);
			txtFeedbackItem = new JTextField();
			c.gridx = 1; c.weightx = 1;
			panel.add(txtFeedbackItem, c);

			c.gridx = 0; c.gridy = 1; c.weightx = 0;
			panel.add(new JLabel("Question:"), c);
			txtQuestion = new JTextArea(4, 20);
			txtQuestion.setLineWrap(true);
			c.gridx = 1; c.weightx = 1; c.weighty = 1;
			c.fill = GridBagConstraints.BOTH;
			panel.add(new JScrollPane(txtQuestion), c);

			return panel;
		}

		/**
		 * Builds the exemption-specific fields card.
		 * @return the exemption card panel
		 */
		private JPanel buildExemptionCard() {
			JPanel panel = new JPanel(new GridBagLayout());
			panel.setBorder(BorderFactory.createTitledBorder("Exemption Fields"));
			GridBagConstraints c = new GridBagConstraints();
			c.insets = new Insets(3, 5, 3, 5);
			c.fill = GridBagConstraints.HORIZONTAL;

			c.gridx = 0; c.gridy = 0;
			panel.add(new JLabel("Policy Reference:"), c);
			txtPolicyReference = new JTextField();
			c.gridx = 1; c.weightx = 1;
			panel.add(txtPolicyReference, c);

			c.gridx = 0; c.gridy = 1; c.weightx = 0;
			panel.add(new JLabel("Circumstances:"), c);
			txtCircumstanceDescription = new JTextArea(4, 20);
			txtCircumstanceDescription.setLineWrap(true);
			c.gridx = 1; c.weightx = 1; c.weighty = 1;
			c.fill = GridBagConstraints.BOTH;
			panel.add(new JScrollPane(txtCircumstanceDescription), c);

			return panel;
		}

		/**
		 * Builds the OK/Cancel button panel.
		 * @return the button panel
		 */
		private JPanel buildButtonPanel() {
			JPanel panel = new JPanel(new FlowLayout());
			JButton btnOk = new JButton("OK");
			btnOk.addActionListener(e -> doOk());
			panel.add(btnOk);
			JButton btnCancel = new JButton("Cancel");
			btnCancel.addActionListener(e -> dispose());
			panel.add(btnCancel);
			return panel;
		}

		/**
		 * Populates the dialog fields for editing an existing request.
		 * @param request the existing regrade request
		 */
		private void populateForEdit(RegradeRequest request) {
			txtStudentName.setText(request.getStudentName());
			txtUnityId.setText(request.getUnityId());
			txtGrader.setText(request.getGrader());

			String type = request.getType();
			if (ReviewRequest.TYPE.equals(type)) {
				rbReview.setSelected(true);
				cardLayout.show(cardPanel, "review");
				ReviewRequest r = (ReviewRequest) request;
				txtReviewItem.setText(r.getReviewItem());
				txtRationale.setText(request.getOpenText());
			} else if (ResubmitRequest.TYPE.equals(type)) {
				rbResubmit.setSelected(true);
				cardLayout.show(cardPanel, "resubmit");
				ResubmitRequest r = (ResubmitRequest) request;
				txtRepoLink.setText(r.getRepoLink());
				txtCommitHash.setText(r.getCommitHash());
				txtResubmitReason.setText(request.getOpenText());
			} else if (ClarifyRequest.TYPE.equals(type)) {
				rbClarify.setSelected(true);
				cardLayout.show(cardPanel, "clarify");
				ClarifyRequest r = (ClarifyRequest) request;
				txtFeedbackItem.setText(r.getFeedbackItem());
				txtQuestion.setText(request.getOpenText());
			} else if (ExemptionRequest.TYPE.equals(type)) {
				rbExemption.setSelected(true);
				cardLayout.show(cardPanel, "exemption");
				ExemptionRequest r = (ExemptionRequest) request;
				txtPolicyReference.setText(r.getPolicyReference());
				txtCircumstanceDescription.setText(request.getOpenText());
			}
		}

		/**
		 * Handles the OK button action.
		 */
		private void doOk() {
			try {
				String studentName = txtStudentName.getText();
				String unityId = txtUnityId.getText();
				String grader = txtGrader.getText();
				int assignmentIdx = WolfRegradeGUI.this.assignmentTable.getSelectedRow();

				if (assignmentIdx < 0) {
					JOptionPane.showMessageDialog(this,
							"No assignment selected.",
							"Error", JOptionPane.ERROR_MESSAGE);
					return;
				}

				RegradeRequest request = null;
				if (rbReview.isSelected()) {
					request = new ReviewRequest(studentName, unityId, grader,
							txtReviewItem.getText(), txtRationale.getText());
				} else if (rbResubmit.isSelected()) {
					request = new ResubmitRequest(studentName, unityId, grader,
							txtRepoLink.getText(), txtCommitHash.getText(),
							txtResubmitReason.getText());
				} else if (rbClarify.isSelected()) {
					request = new ClarifyRequest(studentName, unityId, grader,
							txtFeedbackItem.getText(), txtQuestion.getText());
				} else if (rbExemption.isSelected()) {
					request = new ExemptionRequest(studentName, unityId, grader,
							txtPolicyReference.getText(),
							txtCircumstanceDescription.getText());
				}

				if (request != null) {
					if (editIdx >= 0) {
						courseRegrades.editPendingRequest(assignmentIdx, editIdx, request);
					} else {
						courseRegrades.addPendingRequest(assignmentIdx, request);
					}
					confirmed = true;
				}
				dispose();
			} catch (IllegalArgumentException e) {
				JOptionPane.showMessageDialog(this, e.getMessage(), "Error",
						JOptionPane.ERROR_MESSAGE);
			}
		}

		/**
		 * Returns whether the dialog was confirmed.
		 * @return true if confirmed
		 */
		public boolean isConfirmed() {
			return confirmed;
		}
	}

	// -----------------------------------------------------------------------
	// Main
	// -----------------------------------------------------------------------

	/**
	 * Starts the WolfRegrade application.
	 * @param args command line arguments (not used)
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			WolfRegradeGUI gui = new WolfRegradeGUI();
			gui.setVisible(true);
		});
	}

}