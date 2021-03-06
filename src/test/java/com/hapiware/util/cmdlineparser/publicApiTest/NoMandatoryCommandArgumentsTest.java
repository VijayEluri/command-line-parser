package com.hapiware.util.cmdlineparser.publicApiTest;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.hapiware.util.cmdlineparser.AnnotatedFieldSetException;
import com.hapiware.util.cmdlineparser.Argument;
import com.hapiware.util.cmdlineparser.Command;
import com.hapiware.util.cmdlineparser.CommandLineParser;
import com.hapiware.util.cmdlineparser.CommandNotFoundException;
import com.hapiware.util.cmdlineparser.Description;
import com.hapiware.util.cmdlineparser.IllegalCommandLineArgumentException;
import com.hapiware.util.cmdlineparser.Option;
import com.hapiware.util.cmdlineparser.OptionArgument;
import com.hapiware.util.cmdlineparser.constraint.ConstraintException;

public class NoMandatoryCommandArgumentsTest
	extends
		TestBase
{
	private CommandLineParser _parser;

	@BeforeMethod
	public void init()
	{
		_parser =
			new CommandLineParser(
				NoMandatoryCommandArgumentsTest.class,
				new Description().description("Main description.")
			);
		_parser.add(new Command("pull", "Short desc for pull.") {{
			description("description");
			add(Integer.class, new Argument<Integer>("PID") {{
				optional(-100);
				description("Description for PID.");
			}});
			add(Integer.class, new Argument<Integer>("TYPE") {{
				optional(-200);
				description("Description for TYPE.");
			}});
			add(Integer.class, new Argument<Integer>("ACTION") {{
				optional(-300);
				description("Description for ACTION.");
			}});
			add(new Option("a") {{
				description("Description for -a.");
				set(Integer.class, new OptionArgument<Integer>() {{
					optional(-11);
				}});
			}});
			add(new Option("b") {{
				description("Description for -b.");
				set(Integer.class, new OptionArgument<Integer>());
			}});
			add(new Option("c") {{
				description("Description for -c.");
			}});
		}});
	}

	@Test
	public void allOptionsAndArguments()
		throws
			ConstraintException,
			AnnotatedFieldSetException,
			CommandNotFoundException,
			IllegalCommandLineArgumentException
	{
		_parser.parse(
			new String[] { "pull", "-a11", "-b22", "-c", "100", "200", "300" }
		);
		Command.Data command = _parser.getCommand();
		assertEquals(command.getName(), "pull");
		assertEquals(command.optionExists("-a"), true);
		assertEquals(command.getOptionValue("-a"), 11);
		assertEquals(command.optionExists("-b"), true);
		assertEquals(command.getOptionValue("-b"), 22);
		assertEquals(command.optionExists("-c"), true);
		assertEquals(command.getArgumentValue("PID"), 100);
		assertEquals(command.getArgumentValue("TYPE"), 200);
		assertEquals(command.getArgumentValue("ACTION"), 300);
		
		_parser.parse(
			new String[] { "pull", "-a", "11", "-b", "22", "-c", "100", "200", "300" }
		);
		command = _parser.getCommand();
		assertEquals(command.getName(), "pull");
		assertEquals(command.optionExists("-a"), true);
		assertEquals(command.getOptionValue("-a"), 11);
		assertEquals(command.optionExists("-b"), true);
		assertEquals(command.getOptionValue("-b"), 22);
		assertEquals(command.optionExists("-c"), true);
		assertEquals(command.getArgumentValue("PID"), 100);
		assertEquals(command.getArgumentValue("TYPE"), 200);
		assertEquals(command.getArgumentValue("ACTION"), 300);
	}
	
	@Test
	public void noOptionsAndAllArguments()
		throws
			ConstraintException,
			AnnotatedFieldSetException,
			CommandNotFoundException,
			IllegalCommandLineArgumentException
	{
		_parser.parse(
			new String[] { "pull", "100", "200", "300" }
		);
		Command.Data command = _parser.getCommand();
		assertEquals(command.getName(), "pull");
		assertEquals(command.optionExists("-a"), false);
		assertEquals(command.getOptionValue("-a"), null);
		assertEquals(command.optionExists("-b"), false);
		assertEquals(command.getOptionValue("-b"), null);
		assertEquals(command.optionExists("-c"), false);
		assertEquals(command.getArgumentValue("PID"), 100);
		assertEquals(command.getArgumentValue("TYPE"), 200);
		assertEquals(command.getArgumentValue("ACTION"), 300);
	}

	
	@Test(
		expectedExceptions = { IllegalCommandLineArgumentException.class },
		expectedExceptionsMessageRegExp =
			"Too many command line arguments for command 'pull'\\. Expected max: 3 but was: 5\\."
	)
	public void noOptionsAndTooManyArguments()
		throws
			ConstraintException,
			AnnotatedFieldSetException,
			CommandNotFoundException,
			IllegalCommandLineArgumentException
	{
		_parser.parse(
			new String[] { "pull", "100", "200", "300", "400", "500" }
		);
	}
	
	@Test
	public void noArguments()
		throws
			ConstraintException,
			AnnotatedFieldSetException,
			CommandNotFoundException,
			IllegalCommandLineArgumentException
	{
		_parser.parse(
			new String[] { "pull" }
		);
		Command.Data command = _parser.getCommand();
		assertEquals(command.getName(), "pull");
		assertEquals(command.optionExists("-a"), false);
		assertEquals(command.getOptionValue("-a"), null);
		assertEquals(command.optionExists("-b"), false);
		assertEquals(command.getOptionValue("-b"), null);
		assertEquals(command.optionExists("-c"), false);
		assertEquals(command.getArgumentValue("PID"), -100);
		assertEquals(command.getArgumentValue("TYPE"), -200);
		assertEquals(command.getArgumentValue("ACTION"), -300);
	}
	
	@Test
	public void noArgumentsAllOptions()
		throws
			ConstraintException,
			AnnotatedFieldSetException,
			CommandNotFoundException,
			IllegalCommandLineArgumentException
	{
		_parser.parse(
			new String[] { "pull", "-a", "11", "-b", "22", "-c" }
		);
		Command.Data command = _parser.getCommand();
		assertEquals(command.getName(), "pull");
		assertEquals(command.optionExists("-a"), true);
		assertEquals(command.getOptionValue("-a"), 11);
		assertEquals(command.optionExists("-b"), true);
		assertEquals(command.getOptionValue("-b"), 22);
		assertEquals(command.optionExists("-c"), true);
		assertEquals(command.getArgumentValue("PID"), -100);
		assertEquals(command.getArgumentValue("TYPE"), -200);
		assertEquals(command.getArgumentValue("ACTION"), -300);
	}
	
	@Test
	public void noOptionsAndOneOptionalArgument()
		throws
			ConstraintException,
			AnnotatedFieldSetException,
			CommandNotFoundException,
			IllegalCommandLineArgumentException
	{
		_parser.parse(
			new String[] { "pull", "100" }
		);
		Command.Data command = _parser.getCommand();
		assertEquals(command.getName(), "pull");
		assertEquals(command.optionExists("-a"), false);
		assertEquals(command.getOptionValue("-a"), null);
		assertEquals(command.optionExists("-b"), false);
		assertEquals(command.getOptionValue("-b"), null);
		assertEquals(command.optionExists("-c"), false);
		assertEquals(command.getArgumentValue("PID"), 100);
		assertEquals(command.getArgumentValue("TYPE"), -200);
		assertEquals(command.getArgumentValue("ACTION"), -300);
	}
	
	@Test
	public void noOptionsAndTwoOptionalArguments()
		throws
			ConstraintException,
			AnnotatedFieldSetException,
			CommandNotFoundException,
			IllegalCommandLineArgumentException
	{
		_parser.parse(
			new String[] { "pull", "100", "200" }
		);
		Command.Data command = _parser.getCommand();
		assertEquals(command.getName(), "pull");
		assertEquals(command.optionExists("-a"), false);
		assertEquals(command.getOptionValue("-a"), null);
		assertEquals(command.optionExists("-b"), false);
		assertEquals(command.getOptionValue("-b"), null);
		assertEquals(command.optionExists("-c"), false);
		assertEquals(command.getArgumentValue("PID"), 100);
		assertEquals(command.getArgumentValue("TYPE"), 200);
		assertEquals(command.getArgumentValue("ACTION"), -300);
	}
	
	@Test
	public void allOptionsAWithDefaultArgumentAndAllArguments()
		throws
			ConstraintException,
			AnnotatedFieldSetException,
			CommandNotFoundException,
			IllegalCommandLineArgumentException
	{
		_parser.parse(
			new String[] { "pull", "-a", "-b22", "-c", "100", "200", "300" }
		);
		Command.Data command = _parser.getCommand();
		assertEquals(command.getName(), "pull");
		assertEquals(command.optionExists("-a"), true);
		assertEquals(command.getOptionValue("-a"), -11);
		assertEquals(command.optionExists("-b"), true);
		assertEquals(command.getOptionValue("-b"), 22);
		assertEquals(command.optionExists("-c"), true);
		assertEquals(command.getArgumentValue("PID"), 100);
		assertEquals(command.getArgumentValue("TYPE"), 200);
		assertEquals(command.getArgumentValue("ACTION"), 300);
	}
	
	@Test
	public void allOptionsAWithDefaultArgumentAndTwoArgumentsOnly()
		throws
			ConstraintException,
			AnnotatedFieldSetException,
			CommandNotFoundException,
			IllegalCommandLineArgumentException
	{
		_parser.parse(
			new String[] { "pull", "-a", "-b22", "-c", "100", "200" }
		);
		Command.Data command = _parser.getCommand();
		assertEquals(command.getName(), "pull");
		assertEquals(command.optionExists("-a"), true);
		assertEquals(command.getOptionValue("-a"), -11);
		assertEquals(command.optionExists("-b"), true);
		assertEquals(command.getOptionValue("-b"), 22);
		assertEquals(command.optionExists("-c"), true);
		assertEquals(command.getArgumentValue("PID"), 100);
		assertEquals(command.getArgumentValue("TYPE"), 200);
		assertEquals(command.getArgumentValue("ACTION"), -300);
	}
	
	@Test
	public void optionAOnlyAndAllArguments()
		throws
			ConstraintException,
			AnnotatedFieldSetException,
			CommandNotFoundException,
			IllegalCommandLineArgumentException
	{
		_parser.parse(
			new String[] { "pull", "-a11", "100", "200", "300" }
		);
		Command.Data command = _parser.getCommand();
		assertEquals(command.getName(), "pull");
		assertEquals(command.optionExists("-a"), true);
		assertEquals(command.getOptionValue("-a"), 11);
		assertEquals(command.optionExists("-b"), false);
		assertEquals(command.getOptionValue("-b"), null);
		assertEquals(command.optionExists("-c"), false);
		assertEquals(command.getArgumentValue("PID"), 100);
		assertEquals(command.getArgumentValue("TYPE"), 200);
		assertEquals(command.getArgumentValue("ACTION"), 300);
	}
	
	@Test
	public void optionAOnlyAndTwoArgumentsOnly()
		throws
			ConstraintException,
			AnnotatedFieldSetException,
			CommandNotFoundException,
			IllegalCommandLineArgumentException
	{
		_parser.parse(
			new String[] { "pull", "-a11", "100", "200" }
		);
		Command.Data command = _parser.getCommand();
		assertEquals(command.getName(), "pull");
		assertEquals(command.optionExists("-a"), true);
		assertEquals(command.getOptionValue("-a"), 11);
		assertEquals(command.optionExists("-b"), false);
		assertEquals(command.getOptionValue("-b"), null);
		assertEquals(command.optionExists("-c"), false);
		assertEquals(command.getArgumentValue("PID"), 100);
		assertEquals(command.getArgumentValue("TYPE"), 200);
		assertEquals(command.getArgumentValue("ACTION"), -300);
	}
	
	@Test
	public void optionAOnlyAndOneArgumentOnly()
		throws
			ConstraintException,
			AnnotatedFieldSetException,
			CommandNotFoundException,
			IllegalCommandLineArgumentException
	{
		_parser.parse(
			new String[] { "pull", "-a", "11", "100" }
		);
		Command.Data command = _parser.getCommand();
		assertEquals(command.getName(), "pull");
		assertEquals(command.optionExists("-a"), true);
		assertEquals(command.getOptionValue("-a"), 11);
		assertEquals(command.optionExists("-b"), false);
		assertEquals(command.getOptionValue("-b"), null);
		assertEquals(command.optionExists("-c"), false);
		assertEquals(command.getArgumentValue("PID"), 100);
		assertEquals(command.getArgumentValue("TYPE"), -200);
		assertEquals(command.getArgumentValue("ACTION"), -300);
	}
	
	@Test
	public void optionAWithSeeminglyDefaultArgumentAndAllArguments()
		throws
			ConstraintException,
			AnnotatedFieldSetException,
			CommandNotFoundException,
			IllegalCommandLineArgumentException
	{
		_parser.parse(
			new String[] { "pull", "-a", "100", "200", "300" }
		);
		Command.Data command = _parser.getCommand();
		assertEquals(command.getName(), "pull");
		assertEquals(command.optionExists("-a"), true);
		assertEquals(command.getOptionValue("-a"), 100);
		assertEquals(command.optionExists("-b"), false);
		assertEquals(command.getOptionValue("-b"), null);
		assertEquals(command.optionExists("-c"), false);
		assertEquals(command.getArgumentValue("PID"), 200);
		assertEquals(command.getArgumentValue("TYPE"), 300);
		assertEquals(command.getArgumentValue("ACTION"), -300);
	}
	
	@Test
	public void someOptionsAfterArgumentsAndAllArguments()
		throws
			ConstraintException,
			AnnotatedFieldSetException,
			CommandNotFoundException,
			IllegalCommandLineArgumentException
	{
		_parser.parse(
			new String[] { "pull", "-b", "22", "-c", "100", "200", "300", "-a", "11" }
		);
		Command.Data command = _parser.getCommand();
		assertEquals(command.getName(), "pull");
		assertEquals(command.optionExists("-a"), true);
		assertEquals(command.getOptionValue("-a"), 11);
		assertEquals(command.optionExists("-b"), true);
		assertEquals(command.getOptionValue("-b"), 22);
		assertEquals(command.optionExists("-c"), true);
		assertEquals(command.getArgumentValue("PID"), 100);
		assertEquals(command.getArgumentValue("TYPE"), 200);
		assertEquals(command.getArgumentValue("ACTION"), 300);
	}
	
	@Test
	public void someOptionsAfterArgumentsAndAllArguments2()
		throws
			ConstraintException,
			AnnotatedFieldSetException,
			CommandNotFoundException,
			IllegalCommandLineArgumentException
	{
		_parser.parse(
			new String[] { "pull", "-b", "22", "100", "200", "300", "-a", "11", "-c" }
		);
		Command.Data command = _parser.getCommand();
		assertEquals(command.getName(), "pull");
		assertEquals(command.optionExists("-a"), true);
		assertEquals(command.getOptionValue("-a"), 11);
		assertEquals(command.optionExists("-b"), true);
		assertEquals(command.getOptionValue("-b"), 22);
		assertEquals(command.optionExists("-c"), true);
		assertEquals(command.getArgumentValue("PID"), 100);
		assertEquals(command.getArgumentValue("TYPE"), 200);
		assertEquals(command.getArgumentValue("ACTION"), 300);
	}
	
	@Test
	public void someOptionsAfterArgumentsAndAllArguments2AWithDefaultValue()
		throws
			ConstraintException,
			AnnotatedFieldSetException,
			CommandNotFoundException,
			IllegalCommandLineArgumentException
	{
		_parser.parse(
			new String[] { "pull", "-b", "22", "100", "200", "300", "-a", "-c" }
		);
		Command.Data command = _parser.getCommand();
		assertEquals(command.getName(), "pull");
		assertEquals(command.optionExists("-a"), true);
		assertEquals(command.getOptionValue("-a"), -11);
		assertEquals(command.optionExists("-b"), true);
		assertEquals(command.getOptionValue("-b"), 22);
		assertEquals(command.optionExists("-c"), true);
		assertEquals(command.getArgumentValue("PID"), 100);
		assertEquals(command.getArgumentValue("TYPE"), 200);
		assertEquals(command.getArgumentValue("ACTION"), 300);
	}
	
	@Test
	public void someOptionsAfterArgumentsAndAllArguments3()
		throws
			ConstraintException,
			AnnotatedFieldSetException,
			CommandNotFoundException,
			IllegalCommandLineArgumentException
	{
		_parser.parse(
			new String[] { "pull", "-c", "100", "200", "300", "-a", "11", "-b", "22" }
		);
		Command.Data command = _parser.getCommand();
		assertEquals(command.getName(), "pull");
		assertEquals(command.optionExists("-a"), true);
		assertEquals(command.getOptionValue("-a"), 11);
		assertEquals(command.optionExists("-b"), true);
		assertEquals(command.getOptionValue("-b"), 22);
		assertEquals(command.optionExists("-c"), true);
		assertEquals(command.getArgumentValue("PID"), 100);
		assertEquals(command.getArgumentValue("TYPE"), 200);
		assertEquals(command.getArgumentValue("ACTION"), 300);
	}
	
	@Test
	public void someOptionsAfterArgumentsAndTwoArgumentsOnlyAWithDefaultValue()
		throws
			ConstraintException,
			AnnotatedFieldSetException,
			CommandNotFoundException,
			IllegalCommandLineArgumentException
	{
		_parser.parse(
			new String[] { "pull", "-b", "22", "100", "200", "-a", "-c" }
		);
		Command.Data command = _parser.getCommand();
		assertEquals(command.getName(), "pull");
		assertEquals(command.optionExists("-a"), true);
		assertEquals(command.getOptionValue("-a"), -11);
		assertEquals(command.optionExists("-b"), true);
		assertEquals(command.getOptionValue("-b"), 22);
		assertEquals(command.optionExists("-c"), true);
		assertEquals(command.getArgumentValue("PID"), 100);
		assertEquals(command.getArgumentValue("TYPE"), 200);
		assertEquals(command.getArgumentValue("ACTION"), -300);
	}
	
	@Test
	public void someOptionsAfterArgumentsAndOneArgumentAndAWithDefaultValue()
		throws
			ConstraintException,
			AnnotatedFieldSetException,
			CommandNotFoundException,
			IllegalCommandLineArgumentException
	{
		_parser.parse(
			new String[] { "pull", "-b", "22", "100", "-a", "-c" }
		);
		Command.Data command = _parser.getCommand();
		assertEquals(command.getName(), "pull");
		assertEquals(command.optionExists("-a"), true);
		assertEquals(command.getOptionValue("-a"), -11);
		assertEquals(command.optionExists("-b"), true);
		assertEquals(command.getOptionValue("-b"), 22);
		assertEquals(command.optionExists("-c"), true);
		assertEquals(command.getArgumentValue("PID"), 100);
		assertEquals(command.getArgumentValue("TYPE"), -200);
		assertEquals(command.getArgumentValue("ACTION"), -300);
	}
	
	@Test(
		expectedExceptions = { IllegalCommandLineArgumentException.class },
		expectedExceptionsMessageRegExp =
			"Command line argument '400' for command 'pull' cannot be interpreted as a proper "
				+ "command line argument\\. All the arguments must be sequentially positioned\\. "
				+ "Check that there are no options between arguments\\."
	)
	public void optionBBetweenArgumentsAndMandatoryArgumentsOnly()
		throws
			ConstraintException,
			AnnotatedFieldSetException,
			CommandNotFoundException,
			IllegalCommandLineArgumentException
	{
		_parser.parse(
			new String[] { "pull", "100", "200", "300", "-b22", "400" }
		);
	}
	
	@Test(
		expectedExceptions = { IllegalCommandLineArgumentException.class },
		expectedExceptionsMessageRegExp =
			"Command line argument '300' for command 'pull' cannot be interpreted as a proper "
				+ "command line argument\\. All the arguments must be sequentially positioned\\. "
				+ "Check that there are no options between arguments\\."
	)
	public void optionBBetweenArgumentsAndMandatoryArgumentsOnly2()
		throws
			ConstraintException,
			AnnotatedFieldSetException,
			CommandNotFoundException,
			IllegalCommandLineArgumentException
	{
		_parser.parse(
			new String[] { "pull", "100", "200", "-b22", "300", "400" }
		);
	}
	
	@Test(
		expectedExceptions = { IllegalCommandLineArgumentException.class },
		expectedExceptionsMessageRegExp =
			"Command line argument '200' for command 'pull' cannot be interpreted as a proper "
				+ "command line argument\\. All the arguments must be sequentially positioned\\. "
				+ "Check that there are no options between arguments\\."
	)
	public void optionBBetweenArgumentsAndMandatoryArgumentsOnly3()
		throws
			ConstraintException,
			AnnotatedFieldSetException,
			CommandNotFoundException,
			IllegalCommandLineArgumentException
	{
		_parser.parse(
			new String[] { "pull", "100", "-b22", "200", "300" }
		);
	}
	
	@Test(
		expectedExceptions = { IllegalCommandLineArgumentException.class },
		expectedExceptionsMessageRegExp =
			"Command line argument '200' for command 'pull' cannot be interpreted as a proper "
				+ "command line argument\\. All the arguments must be sequentially positioned\\. "
				+ "Check that there are no options between arguments\\."
	)
	public void optionCBetweenArgumentsAndMandatoryArgumentsOnly()
		throws
			ConstraintException,
			AnnotatedFieldSetException,
			CommandNotFoundException,
			IllegalCommandLineArgumentException
	{
		_parser.parse(
			new String[] { "pull", "100", "-c", "200", "300" }
		);
	}
	
}

