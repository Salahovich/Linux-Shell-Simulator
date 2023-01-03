package commandLine;

import java.io.*;
import java.nio.file.*;
import java.util.*;

/**
 * 
 * 
 * @author Mohamed Salah Abdallah 20190448	(CS)
 * @author Tarek Abd-Elhady Ahmed 20190274	(IT)
 */

public class Terminal {
	private Parser parser = new Parser();
	private final String homePath = "e:\\Test";
	private Path myPath = Paths.get(homePath);
	
	/**
	 * @return none
	 * @param none
	 * Public Constructor to check the validation of the home path,
	 * it terminates if the home path is invalid
	 */
	public Terminal(){
		if (!Files.exists(Paths.get(homePath))) {
			System.out.println("Invalid home path");
			System.exit(-1);
		}	
	}
	
	/**
	 * 
	 * @param reader
	 * @throws IOException
	 * utility method to read file and print it to the screen 
	 */
	private String readFile(FileReader reader) throws IOException {
		String data = "";
		int letter;
		while ((letter = reader.read()) != -1)
			data += (char) letter;
		reader.close();
		return data;
	}

	/**
	 * 
	 * @param from
	 * @param to
	 * @throws IOException
	 * utility method to copy the first file into the second
	 */
	private void copy(FileReader from, FileWriter to) throws IOException {
		int letter;
		String data = "";
		while ((letter = from.read()) != -1)
			data += (char) letter;
		to.write(data);

		from.close();
		to.close();
	}
	/**
	 * 
	 * @return String
	 * echo command implementation
	 */
	public String echo() {
		String output = "";
		if(parser.getArgs() == null) {
			return "Wrong Arguments";
		}
		for (int i = 0; i < parser.getArgs().length; i++)
			output += parser.getArgs()[i] + " ";
		return output;
	}
	/**
	 * 
	 * @return String
	 * pwd command Implementation
	 */
	public String pwd() {
		return this.myPath.toString();
	}
	
	/**
	 * cd command implementation
	 */
	public void cd() {
		try {
			
			if (parser.getArgs() == null)
				myPath = Paths.get(homePath);
			else if (parser.getArgs().length > 1)
				System.out.println("Invlaid arguments");
			else {
				if (parser.getArgs()[0].equals(".."))		// get the previous directory
					myPath = myPath.getParent();	
				else if(parser.getArgs()[0].contains("."))	// the path can not have files
					System.out.println("Invalid path");
				else {
					if (parser.getArgs()[0].contains(":")) {	// the absolute path case
						if (Files.exists(Paths.get(parser.getArgs()[0])))
							myPath = Paths.get(parser.getArgs()[0]);
						else
							System.out.println("Invalid Path");
					} else {													// the relative path case
						if (Files.exists(Paths.get(this.myPath.toString() + "\\" + parser.getArgs()[0])))
							myPath = Paths.get(this.myPath.toString() + "\\" + parser.getArgs()[0]);
						else
							System.out.println("Invalid Path");
					}
				}
			}
		} catch (InvalidPathException e) {
			System.out.println("Invalid Path");
		} catch (NullPointerException e) {
			System.out.println("Invalid Path");
		}
	}

	/** 
	 * 
	 * @return String[]
	 * ls command implementation
	 */
	public String[] ls() {
		if (parser.getArgs() != null) {
			System.out.println("Invlaid parameters, ls command must have no arguments");
			return null;
		} else {
			File myFile = new File(myPath.toString());
			String[] filesAndFolders = myFile.list();
			Arrays.sort(filesAndFolders);
			return filesAndFolders;
		}
	}
	/**
	 * 
	 * @return String[] 
	 * ls -r implementation
	 */
	public String[] lsr() {
		return ls();
	}

	/**
	 * mkdir command implementation
	 */
	public void mkdir() {
		try {
			
		File myFile;
		for (String folder : parser.getArgs()) {

			Path tempPath = Paths.get(folder);
			if (folder.contains("\\") && folder.contains(":")) {
				if (!Files.exists(tempPath)) {
					myFile = new File(folder);
					myFile.mkdir();
				} else
					System.out.println("The directory already exists");
			} else {
				if (!Files.exists(tempPath)) {
					myFile = new File(this.myPath.toString() + "\\" + folder);
					myFile.mkdir();
				} else
					System.out.println("The directory already exists");
			}
		}
		} catch (InvalidPathException e) {
			System.out.println("Invalid Path");
		}
	}
	
	/**
	 * rmdir command implementation
	 */
	public void rmdir() {
		try {
			
		if (parser.getArgs().length != 1)
			System.out.println("Invlaid arguments, rmdir takes only one argument");
		else if(parser.getArgs()[0].contains("."))
			System.out.println("No such directory");
		else {
			if (parser.getArgs()[0].equals("*")) {
				File myFile = new File(this.myPath.toString());
				String[] filesAndFolders = myFile.list();
				for (String folder : filesAndFolders) {
					File tempFile = new File(this.myPath.toString() + "\\" + folder);
					if(Files.exists(tempFile.toPath()) && !folder.contains("."))
						if (tempFile.list() != null) {
							if (tempFile.list().length == 0)
								tempFile.delete();
					} else {
						System.out.println("No such directory");
					}
				}
			} else {
				if (parser.getArgs()[0].contains(":")) {
					File myFile = new File(parser.getArgs()[0]);
					if (myFile.list().length == 0)
						myFile.delete();
					else
						System.out.println(parser.getArgs()[0] + "is not Empty");
				} else {
					File tempFile = new File(this.myPath + "\\" + parser.getArgs()[0]);
					if (tempFile.list().length == 0)
						tempFile.delete();
					else
						System.out.println(parser.getArgs()[0] + "is not Empty");
				}
			}
		}

		} catch(InvalidPathException e) {
			System.out.println("Invalid Path");
		} catch (NullPointerException e) {
			System.out.println("Invalid Path");
		}
	}

	/**
	 * 
	 * @throws IOException
	 * touch command implementation
	 */
	public void touch() throws IOException {
		try {
			if (parser.getArgs().length != 1)
				System.out.println("Invlaid arguments, touch take only one argument");
			else {
				if (parser.getArgs()[0].contains(":")) { // Full Path
					FileWriter myFile = new FileWriter(parser.getArgs()[0]);
					myFile.close();
				} else { 				// relative path
					FileWriter myFile = new FileWriter(this.myPath.toString() + "\\" + parser.getArgs()[0]);
					myFile.close();
				}
			}
		} catch (IOException e) {
			System.out.println("Invalid Path");
		}
	}
	/**
	 * 
	 * @throws FileNotFoundException
	 * @throws IOException
	 * cat command implementation
	 */
	public String cat() throws FileNotFoundException, IOException {
		String data="";
		try {
			if (parser.getArgs().length > 2 || parser.getArgs().length < 1)
				System.out.println("Invalid Arguments. cat command takes one or two arguments");
			else {
				for (String word : parser.getArgs()) {
					if (word.contains("\\") && word.contains(":"))
						data += readFile(new FileReader(word));
					else
						data += readFile(new FileReader(this.myPath.toString() + "\\" + word));
				}
			}
		} catch (IOException e) {
			System.out.println("Invalid Path");
		}
		return data;
	}

	/**
	 * rm command implementation
	 */
	public void rm() {
		if (parser.getArgs().length != 1)
			System.out.println("Invlaid Arguments. rm command take only the file name");
		else {
			File myFile = new File(this.myPath.toString());
			String[] filesAndFolders = myFile.list();
			for (String file : filesAndFolders) {
				if (file.equals(parser.getArgs()[0])) {
					new File(this.myPath.toString() + "\\" + parser.getArgs()[0]).delete();
					return;
				}
			}
			System.out.println(this.parser.getArgs()[0] + ": No such file");
		}
	}
	
	/**
	 * 
	 * @throws FileNotFoundException
	 * @throws IOException
	 * cp command implementation
	 */
	public void cp() throws FileNotFoundException, IOException {
		String fromPath, toPath;
		try {
			if (parser.getArgs().length != 2)
				System.out.println("Invlaid Arguments, cp takes two file paths");
			else {
				if (parser.getArgs()[0].contains(":"))
					fromPath = parser.getArgs()[0];
				else
					fromPath = this.myPath.toString() + "\\" + parser.getArgs()[0];

				if (parser.getArgs()[1].contains(":"))
					toPath = parser.getArgs()[1];
				else
					toPath = this.myPath.toString() + "\\" + parser.getArgs()[1];

				copy(new FileReader(fromPath), new FileWriter(toPath));
			}
		} catch (IOException e) {
			System.out.println(e);
		}

	}
	/**
	 * exit command implementation
	 */
	public void exit() {
		System.exit(0);
	}

	/**
	 * > and >> commands implementation
	 */
	public void angleBrackets() {
		String output = "";
		boolean doubleAngle = parser.isDoubleAngle();
		try {
			if (parser.getCommandName().equals("echo"))
				output = echo();
			else if (parser.getCommandName().equals("pwd"))
				output = pwd();
			else if (parser.getCommandName().equals("ls")) {
				for (String word : ls())
					output += word;
			}
			else if (parser.getCommandName().equals("ls -r")) {
				for (int i=ls().length-1; i>=0 ; i--)
					output += ls()[i];
			}
			else if(parser.getCommandName().equals("cat")) {
				output = cat();
			}
			String path = parser.getFilePath();
			if (path.contains(":")) {		// Absolute Path
				FileWriter myWriter = new FileWriter(path, doubleAngle);
				myWriter.write(output);
				myWriter.close();
			} else {						// relative path
				FileWriter myWriter = new FileWriter(this.myPath.toString() + "\\" + path, doubleAngle);
				myWriter.write(output);
				myWriter.close();
			}
		} catch (IOException e) {
			System.out.println("Invlaid Path");
		}

	}
	/**
	 * utility method for user interaction with the terminal
	 * @throws IOException
	 */
	public void chooseCommandAction() throws IOException {
		try {
		while (true) {
			System.out.print(this.myPath.toString() + ": ");
			Scanner myScanner = new Scanner(System.in);
			String userInput = myScanner.nextLine();

			if (!parser.parse(userInput)) {
				System.out.println("Wrong command name");
				continue;
			} else if (parser.isDoubleCommand()) {
				angleBrackets();
			} else if (parser.getCommandName().equals("echo")) {
				System.out.println(echo());
			} else if (parser.getCommandName().equals("pwd")) {
				System.out.println(pwd());
			} else if (parser.getCommandName().equals("cd")) {
				cd();
			} else if (parser.getCommandName().equals("ls")) {
				String[] filesAndFolders = ls();
				if(filesAndFolders == null)
					continue;
				for (String word : filesAndFolders)
					System.out.println(word);
			} else if (parser.getCommandName().equals("ls -r")) {
				String[] filesAndFolders = lsr();
				for (int i = filesAndFolders.length - 1; i >= 0; i--)
					System.out.println(filesAndFolders[i]);
			} else if (parser.getCommandName().equals("mkdir")) {
				mkdir();
			} else if (parser.getCommandName().equals("rmdir")) {
				rmdir();
			} else if (parser.getCommandName().equals("touch")) {
				touch();
			} else if (parser.getCommandName().equals("cp")) {
				cp();
			} else if (parser.getCommandName().equals("rm")) {
				rm();
			} else if (parser.getCommandName().equals("cat")) {
				System.out.println(cat());
			} else if (parser.getCommandName().equals("exit")) {
				exit();
				myScanner.close();
			}
		}} catch(NullPointerException e) {
			System.out.println("Invalid Path");
		}

	}

	public static void main(String[] args) throws IOException {
		Terminal t = new Terminal();
		t.chooseCommandAction();
		
	}
}
