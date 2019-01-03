

public class FileSystem {

	private Directory currentDir;
	private Directory root;
	
	//Initialize a file system with a root directory.
	public FileSystem() {
		root = new Directory("");
		currentDir = root;
	}

	//Get the current directory.
	public Directory getCurrentDir() {
		return currentDir;
	}
	
	//Get the root.
	public Directory getRoot(){
		return root;
	}

	//Set the current directory to be currenDir.
	public void setCurrentDir(Directory dir) {
		currentDir = dir;
	}
	
}
