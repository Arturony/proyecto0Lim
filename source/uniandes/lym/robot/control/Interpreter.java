package uniandes.lym.robot.control;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.HashMap;

import javax.print.attribute.standard.OutputDeviceAssigned;
import javax.swing.SwingUtilities;

import com.sun.org.apache.xerces.internal.impl.xpath.regex.ParseException;
import com.sun.xml.internal.ws.api.databinding.DatabindingConfig;

import uniandes.lym.robot.kernel.*;



/**
 * Receives commands and relays them to the Robot. 
 */

public class Interpreter   {

	/**
	 * Robot's world
	 */
	private RobotWorldDec world;

	private static final String routine = "ROBOT_R";

	private static final String vars = "VARS";

	private static final String begin = "BEGIN";

	private static final String end = "END";

	private static final String left = "LEFT";

	private static final String right = "RIGHT";

	private static final String around = "AROUND";

	private static final String block = "BLOCK";

	private LinearHashTable<String, Integer> variables;
	
	private HashMap<String, Block> bloques;

	public Interpreter()
	{
	}


	/**
	 * Creates a new interpreter for a given world
	 * @param world 
	 */


	public Interpreter(RobotWorld mundo)
	{
		this.world =  (RobotWorldDec) mundo;
		variables = new LinearHashTable<String, Integer>();
		bloques = new HashMap<String, Block>();
	}


	/**
	 * sets a the world
	 * @param world 
	 */

	public void setWorld(RobotWorld m) 
	{
		world = (RobotWorldDec) m;
		variables = new LinearHashTable<String, Integer>();
		bloques = new HashMap<String, Block>();
	}



	/**
	 *  Processes a sequence of commands. A command is a letter  followed by a ";"
	 *  The command can be:
	 *  M:  moves forward
	 *  R:  turns right
	 *  
	 * @param input Contiene una cadena de texto enviada para ser interpretada
	 */

	public String process(String input) throws Error
	{   
		Date start = new Date(); 
		StringBuffer output = new StringBuffer("SYSTEM RESPONSE: -->\n");	
		input = input.replaceAll("\\s+", "");
		String content = input;
		String[] var;
		try
		{
			if(content.startsWith(routine))
			{
				content = content.split("(?<=)"+routine)[1];
			}
			else
			{
				throw new Exception("The instruction didn't have the correct keyword");
			}
			if(content.contains(vars))
			{
				content = content.split("(?<=)"+vars)[1];
				String actual1 = content.split("(?<=)"+block)[0];
				var = actual1.split(",");
				if(!verificarVar(var))
				{
					throw new Exception("The variable doesn't start with a letter");
				}
				content = content.substring(content.indexOf(block), content.length());
			}
			String[] blocks = content.split(block);
			for(String block : blocks) {
				if(!block.endsWith(end)) {
					throw new Exception("");
				}
				String nombre = block.substring(0,block.indexOf(begin));
				String instruc = block.substring((block.indexOf(begin)+begin.length()), block.indexOf(end));
				Block bloque = new Block(instruc);
				bloques.put(nombre, bloque);
			}
			
			for(Block block: bloques.values())
			{
				block.process(this, output);
			}
		}
		catch (Exception e) 
		{
			output.append("Error!!! " + e.getMessage());
		}
		Date finish = new Date();
		output.append("Progam finished in " + (finish.getTime() - start.getTime()) + " ms");
		return output.toString();
	}

	private boolean verificarVar(String[] array)
	{		
		for(int i = 0; i < array.length; i++)
		{
			char test = array[i].toCharArray()[0];
			if(!Character.isLetter(test))
			{
				return false;
			}
			variables.put(array[i], 0);
		}

		return true;
	}


	public RobotWorldDec getWorld() {
		return world;
	}

	public LinearHashTable<String, Integer> getVariables() {
		return variables;
	}
	
	public HashMap<String, Block> getBloques() {
		return bloques;
	}
	
	public static String getBegin() {
		return begin;
	}


	public static String getEnd() {
		return end;
	}


	public static String getLeft() {
		return left;
	}


	public static String getRight() {
		return right;
	}


	public static String getAround() {
		return around;
	}


	public static String getBlock() {
		return block;
	}

	/*
	int i;
   int n;
   n= input.length();

    i  = 0;
    try	    {
    while (i < n &&  ok) {
    	switch (input.charAt(i)) {
    		case 'M': world.moveForward(1); output.append("move \n");break;
    		case 'R': world.turnRight(); output.append("turnRignt \n");break;
    		case 'C': world.putChips(1); output.append("putChip \n");break;
    		case 'B': world.putBalloons(1); output.append("putBalloon \n");break;
    		case  'c': world.pickChips(1); output.append("getChip \n");break;
    		case  'b': world.grabBalloons(1); output.append("getBalloon \n");break;
    		default: output.append(" Unrecognized command:  "+ input.charAt(i)); ok=false;
          }

    	  if (ok) {
    		 if  (i+1 == n)  { output.append("expected ';' ; found end of input; ");  ok = false ;}
    		 else if (input.charAt(i+1) == ';') 
    		 {
    			 i= i+2;
    			 try {
    			        Thread.sleep(1000);
    			    } catch (InterruptedException e) {
    			        System.err.format("IOException: %s%n", e);
    			    }

    		 }
    		 else {output.append(" Expecting ;  found: "+ input.charAt(i+1)); ok=false;
    	  }
    	 }


    }

    }
 catch (Error e ){
 output.append("Error!!!  "+e.getMessage());

		}
    return output.toString();
	 */

}
