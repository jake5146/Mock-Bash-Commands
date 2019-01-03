
public class File {

	private String name;
	private String content;
	
	//Create a File object with name n.
	public File(String n){
		name = n;
		content = "";
	}
	
	public File(String n, String c){
		name = n;
		content = c;
	}

	//Get the name of the file.
	public String getName() {
		return name;
	}
	
	//Get the content of the file.
	public String getContent() {
		return content;
	}

	//Set the content of the file to be content.
	public void setContent(String content) {
		this.content = content;
	}
	
	//Append a string s to the content.
	public void addContent(String s){
		this.content = content + s;
	}
}
