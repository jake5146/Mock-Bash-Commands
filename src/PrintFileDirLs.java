import java.util.ArrayList;

public class PrintFileDirLs extends Mkdir {

	private String[] dirFilePath;
	private ArrayList<String> flag = new ArrayList<String>();

	void execute(String[] dirFilePath, FileSystem system) {
		// Invalid character that must not be included in directory name.
		// Difference from super class: "." is allowed as file name.
		super.invalidChar = new String[] { "!", "@", "$", "&", "*", "(", ")",
				"?", ":", "[", "]", "\"", "<", ">", "'", "`", "|", "=", "{",
				"}", "\\", "/", ",", ";" };
		int len = dirFilePath.length;
		if (len > 2 && (dirFilePath[len - 2].equals(">") || 
				dirFilePath[len - 2].equals(">>"))) {
			constructAndExecute(dirFilePath, system);
		} else {
			executeConstructor(dirFilePath, system);
			executeMainMethod();
		}
		// Clear path and newDir for next execution.
		path.clear();
		newDir.clear();
		flag.clear();
	}
	
	/* A2B: This method is a part of redirection process.
	 * Just execute constructor. This is an overlapping method with parent's.
	 * It filters out flag if exists.
	 */
	protected void executeConstructor(String[] dirFilePath, FileSystem system) {
		super.constructDirCd(dirFilePath, system);
		ArrayList<String> lowerFlag = new ArrayList<String>();
		lowerFlag.add("-r");
		ArrayList<String> upperFlag = new ArrayList<String>();
		upperFlag.add("-R");
		//If flag is given as argument at the position right after command,
		//move it to 'flag'(instance variable) and remove it from 'path'.
		if (path.size() > 0 && (path.get(0).equals(lowerFlag) 
				|| path.get(0).equals(upperFlag))) {
			flag.addAll(path.get(0));
			path.remove(0);
		}
		String[] newDirFilePath = new String[dirFilePath.length - 1];
		newDirFilePath[0] = "ls";
		for (int i = 1; i < newDirFilePath.length; i++) {
			newDirFilePath[i] = dirFilePath[i + 1];
		}
		constructLs(newDirFilePath, system);
	}
	
	/* A2B: This method is a part of redirection process.
	 * Execute printContents method. This is overlapping method with parent's.
	 * It won't execute if  at least one of command contains invalid character.
	 */
	protected void executeMainMethod() {
		if (newDir.size() == path.size()) {
			printContents();
		}
	}

	/*
	 * Constructor method. Construct using constructor method from super class.
	 */
	private void constructLs(String[] dirFilePath, FileSystem system) {
		this.system = system;
		this.dirFilePath = dirFilePath;
		ArrayList<String> emptyArray = new ArrayList<String>();
		int i = 0;
		// Since root directory initially exists, we can't create root
		// directory.
		while (i < path.size()) {
			if (path.get(i).equals(emptyArray)) {
				path.get(i).add("");
				newDir.add("");
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
			}
			i += 1;
		}
	}

	/* A2B: Revised for recursion problem.
	 * When full path of directory is given, it is root or non-root. Check
	 * whether it is root and print appropriate contents.
	 * @param1: Working directory. It can be either root or non-root directory.
	 * @param2: Name of directory with full path.
	 */
	private void rootOrNonRecOrNon(Directory workingDir, String dirName) {
		// Print directory name and colon following instruction.
		System.out.println(dirName + ":");
		// Print all directories in printThisDir.
		for (Directory eachDir : workingDir.getChildrenDir()) {
			System.out.println(eachDir.getName());
			if (flag.size() == 1) {
				rootOrNonRecOrNon(eachDir, dirName + "/" + eachDir.getName());
			}
		}
		// Print all files in printThisDir.
		for (File eachFile : workingDir.getChildrenFile()) {
			System.out.println(eachFile.getName());
		}
		// Then print extra newline.
		System.out.println();
	}

	/*
	 * Print contents of current directory. Otherwise, call
	 * printFileDirOfFullPath method for the other cases.
	 */
	private void printContents() {
		// Print contents in current directory.
		// If length of dirFilePath is 1, then the user types only "ls".
		if (this.dirFilePath.length == 1) {
			// Print all directories in current directory.
			for (Directory eachDir : this.system.getCurrentDir()
					.getChildrenDir()) {
				System.out.println(eachDir.getName());
			}
			// Print all files in current directory.
			for (File eachFile : this.system.getCurrentDir().getChildrenFile()) 
			{
				System.out.println(eachFile.getName());
			}
			// Then print extra newline.
			System.out.println();
		} else {
			// If argument(s) is(are) given after "ls", execute it.
			printFileDirOfFullPath();

		}
	}

	/*
	 * Print appropriate contents following given arguments by the user. Case1:
	 * Print file name existed in either related path or full path. Case2: Print
	 * contents of directory, and path of directory can be either related to
	 * current directory or full. If path does not exist, or none of cases fits,
	 * print error message.
	 */
	private void printFileDirOfFullPath() {
		// Set original directory to come back later.
		Directory originalCur = this.system.getCurrentDir();
		// Read over all arguments and print appropriate contents.
		for (int i = 0; i < path.size(); i++) {
			// Check if path exists.
			if (distinctAndCheck(i)) {
				// Print file name in given path.
				if (system.getCurrentDir().containsFile(newDir.get(i))) {
					// dirFilePath[i+1] represents actual file name from
					// argument.
					System.out.println(this.dirFilePath[i + 1]);
					// Then print extra newline.
					System.out.println();
					// Print contents of directory in given path.
				} else if (this.system.getCurrentDir().containsDir(
						newDir.get(i))) {
					rootOrNonRecOrNon(this.system.getCurrentDir().
							searchDir(newDir.get(i)), this.dirFilePath[i + 1]);
					//Print all contents in root directory.
				} else if (this.system.getCurrentDir().getName().equals("")) {
					rootOrNonRecOrNon(this.system.getCurrentDir(),
							this.dirFilePath[i + 1]);
				} else {
					this.system.setCurrentDir(originalCur);
					System.out.printf(errorMsg[3]);
					break;
				}
				// If path does not exists, print error message.
			} else {
				this.system.setCurrentDir(originalCur);
				System.out.printf(errorMsg[4]);
				break;
			}
			// Go back to original directory for next creation of directory.
			this.system.setCurrentDir(originalCur);
		}
	}
	
	public static void main(String[] args) {
		FileSystem system  = new FileSystem();
		PrintFileDirLs ls = new PrintFileDirLs();
		ls.execute(new String[]{"ls",  "-r", "/"}, system);
	}
}
