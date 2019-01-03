
import java.util.ArrayList;

public class Directory {

	private String name;
	private String path;
	private Directory parent;
	private ArrayList<Directory> childrenDir;
	private ArrayList<File> childrenFile;

	// Create a Directory object with name n.
	public Directory(String n) {
		name = n;
		path = "/";
		childrenDir = new ArrayList<Directory>();
		childrenFile = new ArrayList<File>();
	}

	// Get the name of the directory.
	public String getName() {
		return name;
	}

	// Get the path of the directory.
	public String getPath() {
		return path;
	}

	// Set the path of the directory.
	public void setPath(String path) {
		this.path = path;
	}

	// Get the parent directory.
	public Directory getParent() {
		return parent;
	}

	// Set the parent directory.
	public void setParent(Directory parent) {
		this.parent = parent;
	}

	// Add a directory to the directory.
	public void addDir(Directory dir) {
		childrenDir.add(dir);
		dir.setParent(this);
		dir.setPath(path + dir.getName() + "/");
	}

	// Get the directories contained by the directory.
	public ArrayList<Directory> getChildrenDir() {
		return childrenDir;
	}

	// Add a file to the directory.
	public void addFile(File file) {
		childrenFile.add(file);
	}

	// Get the files contained by the directory.
	public ArrayList<File> getChildrenFile() {
		return childrenFile;
	}

	// Check if the directory contains a directory with name name.
	public boolean containsDir(String name) {
		for (int i = 0; i < childrenDir.size(); i++) {
			Directory dir = childrenDir.get(i);
			String dirName = dir.getName();
			if (dirName.equals(name)) {
				return true;
			}
		}
		return false;
	}

	// Check if the directory contains a file with name name.
	public boolean containsFile(String name) {
		for (int i = 0; i < childrenFile.size(); i++) {
			File file = childrenFile.get(i);
			String fileName = file.getName();
			if (fileName.equals(name)) {
				return true;
			}
		}
		return false;
	}

	// Remove a directory from the directory.
	public void removeDir(Directory dir) {
		childrenDir.remove(dir);
	}

	// Remove a file from the directory.
	public void removeFile(File file) {
		childrenFile.remove(file);
	}

	// If there's a file named name, return that file. Otherwise, return null.
	public File searchFile(String name) {
		for (int i = 0; i < childrenFile.size(); i++) {
			File file = childrenFile.get(i);
			String fileName = file.getName();
			if (fileName.equals(name)) {
				return file;
			}
		}
		return null;
	}

	/*
	 * If there's a directory named name, return that directory. Otherwise,
	 * return null.
	 */
	public Directory searchDir(String name) {
		for (int i = 0; i < childrenDir.size(); i++) {
			Directory dir = childrenDir.get(i);
			String dirName = dir.getName();
			if (dirName.equals(name)) {
				return dir;
			}
		}
		return null;
	}

	// Retrieves the index from the File Array, otherwise, return -1
	public int getIndex(String name) {
		int index = -1;
		for (int i = 0; i < childrenFile.size(); i++) {
			File file = childrenFile.get(i);
			String fileName = file.getName();
			if (fileName.equals(name)) {
				index = i;
				break;
			}
		}
		return index;
	}
}