package commandLine;

import java.util.ArrayList;
public class Parser {
	private String[] args, terminalCommands = {"echo","pwd","cd","ls","ls -r","mkdir","rmdir","touch","cp","cp -r","rm","cat","exit"};
	private String commandName, filePath;
	private boolean doubleCommand, doubleAngle;
	
	/**
	 * 
	 * @param myCommand
	 * @return boolean
	 * utility method to check if the command exists or not
	 */
	private boolean commandExists(String myCommand) {
		for(int i=0; i<terminalCommands.length; i++) {
			if(terminalCommands[i].equals(myCommand))
				return true;
		}
		return false;
	}
	
	/**
	 * 
	 * @param input
	 * @return boolean 
	 * 
	 * utility method to split the input to command and arguments
	 */
	public boolean parse(String input){
		commandName = "";
		args = null;
		doubleCommand = false;
		doubleAngle = false;
		boolean commandTurn=true, argumentTurn=false;
		String word="";
		ArrayList<String> arguments = new ArrayList<String>();
		for(int i=0; i<input.length(); i++) {
			if(input.charAt(i) == '>')		// double command case
				doubleCommand = true;	// cd c://dfsggdf/gf/fdgfdg/g/fdgd
			if(input.charAt(i) == ' ' && commandTurn) {	// commandName case
				commandName = word;
				word = "";
				commandTurn = false;
				argumentTurn = true;
				continue;
			}
			else if(input.charAt(i) == ' ' && argumentTurn) {	// arguments case
				arguments.add(word);
				word = "";
				continue;
			}
			word += input.charAt(i);
		}
		if(commandName.length() == 0)	// if the input has no spaces ' '
			commandName = word;
		else {
			arguments.add(word);	// last argument
			
			if(arguments.get(0).equals("-r")) {	// reverse command
				commandName += " -r";
				arguments.remove(0);
			}
			if(doubleCommand) {
				filePath = arguments.get(arguments.size()-1);	
				if(arguments.contains(">>"))	// append to file case
					doubleAngle = true;
				arguments.remove(">");		// delete the unnecessary strings
				arguments.remove(">>");
				arguments.remove(filePath);
			}
			if(arguments.size() != 0) {					// add the arguments to the args array
				args = new String[arguments.size()];
				int i = 0;
				for(String index : arguments) {
					args[i] = index;
					i++;
				}	
			}
			
		}
			
		return commandExists(commandName);
	}
	/**
	 * 
	 * @return String 
	 * getter method to get the command name
	 */
	public String getCommandName(){
		return this.commandName;
	}
	/**
	 * 
	 * @return boolean
	 * getter method to return the case of double angle command 
	 */
	public boolean isDoubleAngle() {
		return this.doubleAngle;
	}
	/**
	 * 
	 * @return boolean
	 * getter method to return the case of double command
	 */
	public boolean isDoubleCommand() {
		return this.doubleCommand;
	}
	/**
	 * 
	 * @return String
	 * getter method to get the file path of the double command
	 */
	public String getFilePath() {
		return this.filePath;
	}
	/**
	 * 
	 * @return String[] 
	 * getter method to get the args of command
	 */
	public String[] getArgs(){
		return this.args;
	}
}
