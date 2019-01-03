

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;

public class Mkdir extends DirCd {

	protected ArrayList<String> newDir = new ArrayList<String>();
	// Invalid character that must not be included in directory name.
	protected String[] invalidChar = new String[] { "!", "@", "$", "&", "*",
			"(", ")", "?", ":", "[", "]", "\"", "<", ">", "'", "`", "|", "=",
			"{", "}", "\\", "/", ",", ";", "." };
	//Instance variable needed for redirection.
	protected String[] echoVar = new String[]{"echo", "", "", ""};

	/*
	 * Difference from super class DirCd: 1. "/"(root path) is not available as
	 * full path, so it must be deleted. 2. Directory name positioned in the
	 * last index of full path is a name of directory that we need to
	 * create(which means it does not exist yet).
	 */
	void execute(String[] dirPath, FileSystem system) {
		int len = dirPath.length;
		if (len > 3 && (dirPath[len - 2].equals(">") || 
				dirPath[len - 2].equals(">>"))) {
			constructAndExecute(dirPath, system);
		} else {
			constructMkdir(dirPath, system);
			executeMainMethod();
		}
		// Clean path and newDir for next execution of this method(or class).
		path.clear();
		newDir.clear();
	}
	
	protected void constructAndExecute(String[] dirPath, FileSystem system) {
		int len = dirPath.length;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintStream out = System.out;
	    System.setOut(new PrintStream(baos));
	    echoVar[echoVar.length - 2] = dirPath[len - 2];
	    echoVar[echoVar.length - 1] = dirPath[len - 1];
	    String[] newDirPath = Arrays.copyOfRange(dirPath, 0, len - 2);
	    executeConstructor(newDirPath, system);
	    executeMainMethod();
	    String output = baos.toString();
	    System.setOut(out);
	    if (!isErrorMsg(output)) {
	    	echoVar[1] = output;
	    } else {
	    	System.out.println(output);
	    }
	    if (!echoVar[1].equals("")) {
	    	Echo echo = new Echo();
			echo.execute(echoVar, system);
	    }
	}
	
	/* Helper method for constructAndExecute to filter error messages which
	 * should not be redirected into file.
	 * @param1: String that is either error message or just output.
	 * @Return true if param1, errorOrNot, is error message. Otherwise, false.
	 */
	private boolean isErrorMsg(String errorOrNot) {
		for (int i = 0; i < errorMsg.length; i++) {
			if (errorMsg[i].equals(errorOrNot)) {
				return true;
			}
		}
		return false;
	}
	
	/* A2B: This method is a part of redirection process.
	 * Just execute constructor. It will be overlapped in subclass for use
	 * of constructAndExecute method(for redirection). It is helpful for
	 * preventing duplicated code of constructAndExecute used in all subclasses.
	 */
	protected void executeConstructor(String[] dirPath, FileSystem system) {
		constructMkdir(dirPath, system);
	}
	
	/* A2B: This method is a part of redirection process.
	 * Execute createDir method. It also will be overlapped in subclass.
	 * It is helpful for preventing duplicated code of constructAndExecute
	 *  used in all subclasses.
	 */
	protected void executeMainMethod() {
		if (newDir.size() == path.size()) {
			createDir();
		}
	}

	// Create method that calls constructor of super class since abstract
	// class does not allow it.
	protected void constructMkdir(String[] dirPath, FileSystem system) {
		super.constructDirCd(dirPath, system);
		this.system = system;
		ArrayList<String> emptyArray = new ArrayList<String>();
		int i = 0;
		// Since root directory initially exists, we can't create root
		// directory.
		while (i < path.size()) {
			if (path.get(i).equals(emptyArray)) {
				System.out.printf(errorMsg[1]); //DirCd's instance variable 
				break;
			} else {
				/*
				 * Collect last element(directory name we need to create) Then,
				 * remove it from the original ArrayList to use dirCheckAndMove
				 * method to reach the directory where new directory should
				 * actually be created.
				 */
				int last = (path.get(i).size() - 1);
				// Check whether name of new directory contains
				// invalid character. If it contains, break loop.
				if (checkInvalidChar(path.get(i).get(last))) {
					System.out.printf(errorMsg[2]);
					break;
				} else {
					newDir.add(path.get(i).get(last));
					path.get(i).remove(last);
				}
				i += 1;
			}
		}
	}

	/*
	 * Check whether directory name contains invalid character. If it contains
	 * at least one invalid character, return true. Param1: String Return:
	 * Boolean
	 */
	protected boolean checkInvalidChar(String dirName) {
		for (String invalid : invalidChar) {
			if (dirName.contains(invalid)) {
				return true;
			}
		}
		return false;
	}

	/*
	 * Helper method. Distinct related path argument and full path argument, and
	 * then execute dirCheckAndMove method.
	 * 
	 * @param1: index(position) of directory argument in path(ArrayList).
	 * 
	 * @Return true if related path/full path exists.
	 */
	protected boolean distinctAndCheck(int i) {
		// Create empty for comparison of full path and related path.
		ArrayList<String> empty = new ArrayList<String>();
		empty.add("");
		// Check which one of full path and related path we use.
		if (path.get(i).equals(empty) || path.get(i).contains("")) {
			// For full path, we start from root directory to move to the
			// other directory if needed.
			this.system.setCurrentDir(this.system.getRoot());
			return this.dirCheckAndMove(path.get(i), 1);
		} else {
			// For related path, we just start from original working
			// directory.
			return this.dirCheckAndMove(path.get(i), 0);
		}
	}

	/*
	 * Create directory following given directory arguments. Check existence of
	 * directory by dirCheckAndMove method from super class. After check, create
	 * directory and come back to original directory where we start from for the
	 * next creation of directory.
	 */
	private void createDir() {
		// Create empty for comparison of full path and related path.
		ArrayList<String> empty = new ArrayList<String>();
		empty.add("");
		// Set original directory to come back later.
		Directory originalCur = this.system.getCurrentDir();
		// For all directory given, we create directory.
		for (int i = 0; i < path.size(); i++) {
			if (distinctAndCheck(i)) {
				// If path exists, add new directory to the appropriate place.
				if (this.system.getCurrentDir().containsDir(newDir.get(i))) {
					System.out.printf(errorMsg[1]); //See DirCd
					break;
				} else {
					this.system.getCurrentDir().addDir(
							new Directory(newDir.get(i)));
				}
			} else {
				// If path doesn't exist, go back to original directory and
				// print an error. Then, break the for-loop.
				this.system.setCurrentDir(originalCur);
				System.out.printf(errorMsg[0]); //See DirCd
				break;
			}
			// Go back to original directory for next creation of directory.
			this.system.setCurrentDir(originalCur);
		}
	}
	
	public static void main(String[] args) {
		FileSystem system = new FileSystem();
		Mkdir mkdir = new Mkdir();
		mkdir.execute(new String[]{"mkdir", "test1"}, system);
		mkdir.execute(new String[]{"mkdir", "test1/test2/test3/", ">>", "file.txt"}, system);
	}
}