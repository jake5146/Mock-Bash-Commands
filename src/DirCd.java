

import java.util.ArrayList;
import java.util.Arrays;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class DirCd extends Command {

	protected ArrayList<ArrayList<String>> path = 
			new ArrayList<ArrayList<String>>();
	protected FileSystem system;
	/* Array that contains all error messages from DirCd, Mkdir, PrintFileDirLs.
	 * If more error messages are created by later edition, add here.
	 * It is useful for redirection when we filter error message to not put
	 * it into file.
	 */
	protected String[] errorMsg = new String[]{
			"Directory does not exist. Please try again.\n", //errorMsg[0]
			"Directory already exists. Please try again.\n", //errorMsg[1]
			"Invalid command. Please try again.\n", //errorMsg[2]
			"Directory/File does not exist. Please try again\n", //errorMsg[3]
			"Path does not exist. Please try again.\n" //errorMsg[4]
	        };

	/*
	 * Constructor: Separate two types of paths which are path related to
	 * current directory and full path. Construct system, instance variable.
	 * 
	 * @param1: Array of directory name(s) parsed by class Parser.
	 * @param2: FileSystem
	 */
	void execute(String[] dirPath, FileSystem system) {
		constructDirCd(dirPath, system);
		changeDir();
		// clear path for next execution.
		path.clear();
	}

	/* Create method that acts like constructor to use in subclass.
	 * A2B: Since DirCd usually does not have output but just change directory,
	 *      > and >> commands don't append/replace outputs into files.
	 */
	protected void constructDirCd(String[] dirPath, FileSystem system) {
		this.system = system;
		ArrayList<String> eachPath = new ArrayList<String>();

		// If path contains multiple slashes, then it must be full path.
		// Otherwise, path relative to the current path.
		for (int i = 1; i < dirPath.length; i++) {
			String[] dir = dirPath[i].split("/");
			// put in ArrayList after split.
			for (String each : dir) {
				eachPath.add(each);
			}
			ArrayList<String> eachPathAdd = new ArrayList<String>();
			eachPathAdd.addAll(eachPath);
			System.out.println(eachPathAdd);
			// add eachPath in path
			path.add(eachPathAdd);
			// Clear eachPath for the next path.
			eachPath.clear();
		}
	}

	/*
	 * Method that does actual work. Set directory to root initially(only for
	 * full path) and change directory using dirCheckAndMove. If dirCheckAndMove
	 * is false, go back to original and print error message.
	 */
	private void changeDir() {
		ArrayList<String> empty = new ArrayList<String>();
		boolean existence = true;
		// Save the original directory preventing directory inexistent
		Directory originalCur = this.system.getCurrentDir();
		if (path.get(0).equals(empty) || path.get(0).get(0).equals("")) {
			// Since it is full path, start from root to check correctly.
			this.system.setCurrentDir(this.system.getRoot());
			// Check if path actually exists. Argument 1 means full path.
			existence = this.dirCheckAndMove(path.get(0), 1);
		} else {
			// Argument 0 means related path.
			existence = this.dirCheckAndMove(path.get(0), 0);
		}

		// If true, current directory has already changed by dirCheckAndMove
		// If false, set the current directory to the original directory.
		if (!existence) {
			this.system.setCurrentDir(originalCur);
			System.out.printf(errorMsg[0]); //refer to instance variable above.
		}
	}

	/*
	 * Return true if path actually exists. Otherwise, false. (note: This helper
	 * method also changes directory as it checks) -Stay if the user types "."
	 * in the current directory -Move to the parent directory if ".."(stay
	 * current if current is root).
	 * 
	 * @param1: Array of directory names of path. Following directory is a child
	 * directory of previous directory. example) directory at index 1 is a child
	 * directory of directory at index 0.
	 */
	protected boolean dirCheckAndMove(ArrayList<String> fullRel, int i) {
		ArrayList<String> fullRelCopy = new ArrayList<String>();
		// If full path is given as root(/)(but actually "" because of split),
		// return true.
		// To stop recursion for relative path, if size is 0, return true.
		if ((fullRel.size() == 1 && fullRel.get(0).equals(""))
				|| fullRel.size() == 0) {
			return true;
			/*
			 * If second index of directory exists, change to it and proceed to
			 * next Otherwise, return false. Note: first index is always empty
			 * string for full path. Exception: ".": stay in current; "..":
			 * change to parent.
			 */
		} else if (fullRel.size() >= 1) {
			if (this.system.getCurrentDir().containsDir(fullRel.get(i))) {
				this.system.setCurrentDir(this.system.getCurrentDir()
						.searchDir(fullRel.get(i)));
			} else if (fullRel.get(i).equals("..")) {
				if (!this.system.getCurrentDir().getName().equals("")) {
					this.system.setCurrentDir(this.system.getCurrentDir()
							.getParent());
				}
				// For ".", doing nothing is equivalent to staying in current.
			} else if (!fullRel.get(i).equals(".")) {
				return false;
			}
			fullRelCopy.addAll(fullRel);
			fullRelCopy.remove(i);
			return this.dirCheckAndMove(fullRelCopy, i);
		} else {
			return false;
		}
	}
}
