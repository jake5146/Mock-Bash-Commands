
public abstract class Command {

	//The input is a String array of each word that user typed in command.
	abstract void execute(String[] input, FileSystem fileSystem);
}
