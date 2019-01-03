

public class Echo extends Command {

	void execute(String[] input, FileSystem fileSystem) {
		String content = input[1];
		Directory currentDir = fileSystem.getCurrentDir();

		// If OUTFILE is not provided, echo will print STRING to shell
		if (input.length < 3) {
			System.out.println(content);
			return;
		}

		String fileName = input[3];

		/*
		 * echo command will overwrite content of file if the filename already
		 * exists, otherwise, it will create a new file with the inputed
		 * filename
		 */
		if (input[2].equals(">")) {
			if (currentDir.containsFile(fileName)) {
				currentDir.getChildrenFile().get(currentDir.getIndex(fileName))
						.setContent(content);
			} else {
				File newFile = new File(fileName, content);
				currentDir.addFile(newFile);
			}

			/*
			 * echo will append the STRING instead of overwriting if the
			 * filename already exists, if not, echo will create a new file with
			 * the inputed content
			 */
		} else if (input[2].equals(">>")) {
			if (currentDir.containsFile(fileName)) {
				currentDir.getChildrenFile().get(currentDir.getIndex(fileName))
						.addContent(content);
			} else {
				File newFile = new File(fileName, content);
				currentDir.addFile(newFile);
			}
		}
	}
}