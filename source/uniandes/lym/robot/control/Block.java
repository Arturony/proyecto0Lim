package uniandes.lym.robot.control;

import uniandes.lym.robot.kernel.RobotWorld;

public class Block {

	private String content;
	
	public Block(String content) {
		this.content = content;
	}
	
	public void process(Interpreter interpreter, StringBuffer output) throws Exception 
	{
		
		String[] ins = content.split(";");
		for (int i = 0; i < ins.length; i++) 
		{
			ins[i] = ins[i].replace("(", "");
			ins[i] = ins[i].replace(")", "");
			if(ins[i].startsWith("Assing"))
			{
				ins[i] = ins[i].split("(?<=Assing)")[1];
				String[] param = ins[i].split(",");
				try {
					interpreter.getVariables().put(param[0], Integer.parseInt(param[1]));
				} 
				catch (Exception e) {
					throw new Exception("Please inset a number in the parameters");
				}
				output.append("Variable " + param[0] + " assingned the value of " + interpreter.getVariables().get(param[0]) + "\n");
			}
			else if(ins[i].startsWith("Move"))
			{
				ins[i] = ins[i].split("(?<=Move)")[1];
				int n;
				if(!ins[i].contains(","))
				{
					try
					{
						n = Integer.parseInt(ins[i]);
					}
					catch (NumberFormatException e) 
					{
						if(!interpreter.getVariables().contains(ins[i]))
							throw new Exception("Enter a number or a valid variable");
						else
						{
							n =interpreter.getVariables().get(ins[i]);
						}
					}
					interpreter.getWorld().moveForward(n);
					output.append("Move " + n + " steps forward \n");
				}
				else
				{
					String[] par = ins[i].split(",");
					try
					{
						n = Integer.parseInt(par[0]);
					}
					catch (NumberFormatException e) 
					{
						if(!interpreter.getVariables().contains(par[0]))
							throw new Exception("Enter a number or a valid variable");
						else
						{
							n =interpreter.getVariables().get(par[0]);
						}
					}
					interpreter.getWorld().face(par[1]);
					output.append("Facing " + par[1] + " direction");
					interpreter.getWorld().moveForward(n);
					output.append("Move " + n + " steps forward \n");
				}
			}
			else if(ins[i].startsWith("Turn"))
			{
				ins[i] = ins[i].split("(?<=Turn)")[1];
				if(ins[i].equalsIgnoreCase(interpreter.getLeft()))
				{
					interpreter.getWorld().turnLeft();
					output.append("Turning " + interpreter.getLeft() + "direction\n");
				}
				else if(ins[i].equalsIgnoreCase(interpreter.getRight()))
				{
					interpreter.getWorld().turnRight();
					output.append("Turning " + interpreter.getRight() + "direction\n");
				}
				else if(ins[i].equalsIgnoreCase(interpreter.getAround()))
				{
					interpreter.getWorld().turnAround();
					output.append("Turning " + interpreter.getAround() + "direction\n");
				}
				else
					throw new Exception("Enter a valid direction");
				
			}
			else if(ins[i].startsWith("Face"))
			{
				ins[i] = ins[i].split("(?<=Face)")[1];
				output.append("Facing " + ins[i] + " direction\n");
				interpreter.getWorld().face(ins[i]);
			}
			else if(ins[i].startsWith("Put"))
			{
				ins[i] = ins[i].split("(?<=Put)")[1];
				String[] par = ins[i].split(",");
				int n;
				try
				{
					n = Integer.parseInt(par[1]);
				}
				catch (NumberFormatException e) 
				{
					if(!interpreter.getVariables().contains(par[1]))
						throw new Exception("Enter a number or a valid variable");
					else
					{
						n =interpreter.getVariables().get(par[1]);
					}
				}
				if(par[0].startsWith("B"))
				{
					interpreter.getWorld().putBalloons(n);
					output.append("Putting " + n + " balloons\n");
				}
				else if(par[0].startsWith("C"))
				{
					interpreter.getWorld().putChips(n);
					output.append("Putting " + n + " chips");
				}
				else
					throw new Exception("Enter a valid parameter");
			}
			
		}
		
		
	}
}
