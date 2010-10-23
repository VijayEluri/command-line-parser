package com.hapiware.util.cmdlineparser.publicApiTest;

import static junit.framework.Assert.assertEquals;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.hapiware.util.cmdlineparser.AnnotatedFieldSetException;
import com.hapiware.util.cmdlineparser.Argument;
import com.hapiware.util.cmdlineparser.CommandLineParser;
import com.hapiware.util.cmdlineparser.CommandNotFoundException;
import com.hapiware.util.cmdlineparser.ConfigurationException;
import com.hapiware.util.cmdlineparser.Description;
import com.hapiware.util.cmdlineparser.IllegalCommandLineArgumentException;
import com.hapiware.util.cmdlineparser.Option;
import com.hapiware.util.cmdlineparser.OptionArgument;
import com.hapiware.util.cmdlineparser.constraint.ConstraintException;

public class TwoOptionalArgumentsTest
	extends
		TestBase
{
	private CommandLineParser _parser;

	@BeforeMethod
	public void init()
	{
		_parser =
			new CommandLineParser(
				TwoOptionalArgumentsTest.class,
				new Description().description("Main description.")
			);
		_parser.add(Integer.class, new Argument<Integer>("PID") {{
			description("Description for PID.");
		}});
		_parser.add(Integer.class, new Argument<Integer>("TYPE") {{
			description("Description for TYPE.");
		}});
		_parser.add(Integer.class, new Argument<Integer>("ACTION") {{
			optional(-300);
			description("Description for ACTION.");
		}});
		_parser.add(Integer.class, new Argument<Integer>("LEVEL") {{
			optional(-400);
			description("Description for LEVEL.");
		}});
		_parser.add(new Option("a") {{
			description("Description for -a.");
			set(Integer.class, new OptionArgument<Integer>() {{
				optional(-11);
			}});
		}});
		_parser.add(new Option("b") {{
			description("Description for -b.");
			set(Integer.class, new OptionArgument<Integer>());
		}});
		_parser.add(new Option("c") {{
			description("Description for -c.");
		}});
	}

	@Test(
		expectedExceptions = {ConfigurationException.class},
		expectedExceptionsMessageRegExp = 
			"If there is more than one optional argument they must be the last arguments\\. "
				+ "The first conflicting argument is 'LEVEL'\\. "
				+ "A single optional argument can have any position\\."
	)
	public void misplacedOptionalArguments()
	{
		CommandLineParser p =
			new CommandLineParser(
				TwoOptionalArgumentsTest.class,
				new Description().description("Main description.")
			);
		p.add(Integer.class, new Argument<Integer>("PID") {{
			description("Description for PID.");
		}});
		p.add(Integer.class, new Argument<Integer>("TYPE") {{
			optional(-200);
			description("Description for TYPE.");
		}});
		p.add(Integer.class, new Argument<Integer>("ACTION") {{
			optional(-300);
			description("Description for ACTION.");
		}});
		p.add(Integer.class, new Argument<Integer>("LEVEL") {{
			description("Description for LEVEL.");
		}});
	}
	
	@Test(
		expectedExceptions = {ConfigurationException.class},
		expectedExceptionsMessageRegExp = 
			"If there is more than one optional argument they must be the last arguments\\. "
				+ "The first conflicting argument is 'LEVEL'\\. "
				+ "A single optional argument can have any position\\."
	)
	public void misplacedOptionalArguments2()
	{
		CommandLineParser p =
			new CommandLineParser(
				TwoOptionalArgumentsTest.class,
				new Description().description("Main description.")
			);
		p.add(Integer.class, new Argument<Integer>("PID") {{
			description("Description for PID.");
		}});
		p.add(Integer.class, new Argument<Integer>("TYPE") {{
			optional(-200);
			description("Description for TYPE.");
		}});
		p.add(Integer.class, new Argument<Integer>("ACTION") {{
			description("Description for ACTION.");
		}});
		p.add(Integer.class, new Argument<Integer>("LEVEL") {{
			optional(-400);
			description("Description for LEVEL.");
		}});
	}
	
	@Test(
		expectedExceptions = {ConfigurationException.class},
		expectedExceptionsMessageRegExp = 
			"If there is more than one optional argument they must be the last arguments\\. "
				+ "The first conflicting argument is 'LEVEL'\\. "
				+ "A single optional argument can have any position\\."
	)
	public void misplacedOptionalArguments3()
	{
		CommandLineParser p =
			new CommandLineParser(
				TwoOptionalArgumentsTest.class,
				new Description().description("Main description.")
			);
		p.add(Integer.class, new Argument<Integer>("PID") {{
			description("Description for PID.");
		}});
		p.add(Integer.class, new Argument<Integer>("TYPE") {{
			optional(-200);
			description("Description for TYPE.");
		}});
		p.add(Integer.class, new Argument<Integer>("ACTION") {{
			description("Description for ACTION.");
		}});
		p.add(Integer.class, new Argument<Integer>("LEVEL") {{
			optional(-400);
			description("Description for LEVEL.");
		}});
		p.add(Integer.class, new Argument<Integer>("INFO") {{
			optional(-500);
			description("Description for INFO.");
		}});
	}
	
	@Test(
		expectedExceptions = {ConfigurationException.class},
		expectedExceptionsMessageRegExp = 
			"If there is more than one optional argument they must be the last arguments\\. "
				+ "The first conflicting argument is 'INFO'\\. "
				+ "A single optional argument can have any position\\."
	)
	public void misplacedOptionalArguments4()
	{
		CommandLineParser p =
			new CommandLineParser(
				TwoOptionalArgumentsTest.class,
				new Description().description("Main description.")
			);
		p.add(Integer.class, new Argument<Integer>("PID") {{
			description("Description for PID.");
		}});
		p.add(Integer.class, new Argument<Integer>("TYPE") {{
			optional(-200);
			description("Description for TYPE.");
		}});
		p.add(Integer.class, new Argument<Integer>("ACTION") {{
			optional(-300);
			description("Description for ACTION.");
		}});
		p.add(Integer.class, new Argument<Integer>("LEVEL") {{
			optional(-400);
			description("Description for LEVEL.");
		}});
		p.add(Integer.class, new Argument<Integer>("INFO") {{
			description("Description for INFO.");
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
			new String[] { "-a11", "-b22", "-c", "100", "200", "300", "400" }
		);
		assertEquals(true, _parser.optionExists("-a"));
		assertEquals(11, _parser.getOptionValue("-a"));
		assertEquals(true, _parser.optionExists("-b"));
		assertEquals(22, _parser.getOptionValue("-b"));
		assertEquals(true, _parser.optionExists("-c"));
		assertEquals(100, _parser.getArgumentValue("PID"));
		assertEquals(200, _parser.getArgumentValue("TYPE"));
		assertEquals(300, _parser.getArgumentValue("ACTION"));
		assertEquals(400, _parser.getArgumentValue("LEVEL"));
		
		_parser.parse(
			new String[] { "-a", "11", "-b", "22", "-c", "100", "200", "300", "400" }
		);
		assertEquals(true, _parser.optionExists("-a"));
		assertEquals(11, _parser.getOptionValue("-a"));
		assertEquals(true, _parser.optionExists("-b"));
		assertEquals(22, _parser.getOptionValue("-b"));
		assertEquals(true, _parser.optionExists("-c"));
		assertEquals(100, _parser.getArgumentValue("PID"));
		assertEquals(200, _parser.getArgumentValue("TYPE"));
		assertEquals(300, _parser.getArgumentValue("ACTION"));
		assertEquals(400, _parser.getArgumentValue("LEVEL"));
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
			new String[] { "100", "200", "300", "400" }
		);
		assertEquals(false, _parser.optionExists("-a"));
		assertEquals(null, _parser.getOptionValue("-a"));
		assertEquals(false, _parser.optionExists("-b"));
		assertEquals(null, _parser.getOptionValue("-b"));
		assertEquals(false, _parser.optionExists("-c"));
		assertEquals(100, _parser.getArgumentValue("PID"));
		assertEquals(200, _parser.getArgumentValue("TYPE"));
		assertEquals(300, _parser.getArgumentValue("ACTION"));
		assertEquals(400, _parser.getArgumentValue("LEVEL"));
	}

	
	@Test(
		expectedExceptions = { IllegalCommandLineArgumentException.class },
		expectedExceptionsMessageRegExp =
			"Too few command line arguments\\. Expected min: 2 but was: 1\\."
	)
	public void noOptionsAndTooFewArguments()
		throws
			ConstraintException,
			AnnotatedFieldSetException,
			CommandNotFoundException,
			IllegalCommandLineArgumentException
	{
		_parser.parse(
			new String[] { "100" }
		);
	}
	
	@Test(
		expectedExceptions = { IllegalCommandLineArgumentException.class },
		expectedExceptionsMessageRegExp =
			"Too many command line arguments\\. Expected max: 4 but was: 5\\."
	)
	public void noOptionsAndTooManyArguments()
		throws
			ConstraintException,
			AnnotatedFieldSetException,
			CommandNotFoundException,
			IllegalCommandLineArgumentException
	{
		_parser.parse(
			new String[] { "100", "200", "300", "400", "500" }
		);
	}
	
	@Test
	public void noOptionsAndMandatoryArgumentsOnly()
		throws
			ConstraintException,
			AnnotatedFieldSetException,
			CommandNotFoundException,
			IllegalCommandLineArgumentException
	{
		_parser.parse(
			new String[] { "100", "200" }
		);
		assertEquals(false, _parser.optionExists("-a"));
		assertEquals(null, _parser.getOptionValue("-a"));
		assertEquals(false, _parser.optionExists("-b"));
		assertEquals(null, _parser.getOptionValue("-b"));
		assertEquals(false, _parser.optionExists("-c"));
		assertEquals(100, _parser.getArgumentValue("PID"));
		assertEquals(200, _parser.getArgumentValue("TYPE"));
		assertEquals(-300, _parser.getArgumentValue("ACTION"));
		assertEquals(-400, _parser.getArgumentValue("LEVEL"));
	}
	
	@Test
	public void noOptionsAndMandatoryArgumentsAndOneOptionalArgument()
		throws
			ConstraintException,
			AnnotatedFieldSetException,
			CommandNotFoundException,
			IllegalCommandLineArgumentException
	{
		_parser.parse(
			new String[] { "100", "200", "300" }
		);
		assertEquals(false, _parser.optionExists("-a"));
		assertEquals(null, _parser.getOptionValue("-a"));
		assertEquals(false, _parser.optionExists("-b"));
		assertEquals(null, _parser.getOptionValue("-b"));
		assertEquals(false, _parser.optionExists("-c"));
		assertEquals(100, _parser.getArgumentValue("PID"));
		assertEquals(200, _parser.getArgumentValue("TYPE"));
		assertEquals(300, _parser.getArgumentValue("ACTION"));
		assertEquals(-400, _parser.getArgumentValue("LEVEL"));
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
			new String[] { "-a", "-b22", "-c", "100", "200", "300", "400" }
		);
		assertEquals(true, _parser.optionExists("-a"));
		assertEquals(-11, _parser.getOptionValue("-a"));
		assertEquals(true, _parser.optionExists("-b"));
		assertEquals(22, _parser.getOptionValue("-b"));
		assertEquals(true, _parser.optionExists("-c"));
		assertEquals(100, _parser.getArgumentValue("PID"));
		assertEquals(200, _parser.getArgumentValue("TYPE"));
		assertEquals(300, _parser.getArgumentValue("ACTION"));
		assertEquals(400, _parser.getArgumentValue("LEVEL"));
	}
	
	@Test
	public void allOptionsAWithDefaultArgumentAndMandatoryArgumentsOnly()
		throws
			ConstraintException,
			AnnotatedFieldSetException,
			CommandNotFoundException,
			IllegalCommandLineArgumentException
	{
		_parser.parse(
			new String[] { "-a", "-b22", "-c", "100", "200" }
		);
		assertEquals(true, _parser.optionExists("-a"));
		assertEquals(-11, _parser.getOptionValue("-a"));
		assertEquals(true, _parser.optionExists("-b"));
		assertEquals(22, _parser.getOptionValue("-b"));
		assertEquals(true, _parser.optionExists("-c"));
		assertEquals(100, _parser.getArgumentValue("PID"));
		assertEquals(200, _parser.getArgumentValue("TYPE"));
		assertEquals(-300, _parser.getArgumentValue("ACTION"));
		assertEquals(-400, _parser.getArgumentValue("LEVEL"));
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
			new String[] { "-a11", "100", "200", "300", "400" }
		);
		assertEquals(true, _parser.optionExists("-a"));
		assertEquals(11, _parser.getOptionValue("-a"));
		assertEquals(false, _parser.optionExists("-b"));
		assertEquals(null, _parser.getOptionValue("-b"));
		assertEquals(false, _parser.optionExists("-c"));
		assertEquals(100, _parser.getArgumentValue("PID"));
		assertEquals(200, _parser.getArgumentValue("TYPE"));
		assertEquals(300, _parser.getArgumentValue("ACTION"));
		assertEquals(400, _parser.getArgumentValue("LEVEL"));
	}
	
	@Test
	public void optionAOnlyAndMandatoryArgumentsOnly()
		throws
			ConstraintException,
			AnnotatedFieldSetException,
			CommandNotFoundException,
			IllegalCommandLineArgumentException
	{
		_parser.parse(
			new String[] { "-a11", "100", "200" }
		);
		assertEquals(true, _parser.optionExists("-a"));
		assertEquals(11, _parser.getOptionValue("-a"));
		assertEquals(false, _parser.optionExists("-b"));
		assertEquals(null, _parser.getOptionValue("-b"));
		assertEquals(false, _parser.optionExists("-c"));
		assertEquals(100, _parser.getArgumentValue("PID"));
		assertEquals(200, _parser.getArgumentValue("TYPE"));
		assertEquals(-300, _parser.getArgumentValue("ACTION"));
		assertEquals(-400, _parser.getArgumentValue("LEVEL"));
	}
	
	@Test(
		expectedExceptions = { IllegalCommandLineArgumentException.class },
		expectedExceptionsMessageRegExp =
			"Too few command line arguments\\. Expected min: 2 but was: 1\\."
	)
	public void optionAWithDefaultArgumentAndMandatoryArgumentsOnly()
		throws
			ConstraintException,
			AnnotatedFieldSetException,
			CommandNotFoundException,
			IllegalCommandLineArgumentException
	{
		_parser.parse(
			new String[] { "-a", "100", "200" }
		);
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
			new String[] { "-a", "100", "200", "300", "400" }
		);
		assertEquals(true, _parser.optionExists("-a"));
		assertEquals(100, _parser.getOptionValue("-a"));
		assertEquals(false, _parser.optionExists("-b"));
		assertEquals(null, _parser.getOptionValue("-b"));
		assertEquals(false, _parser.optionExists("-c"));
		assertEquals(200, _parser.getArgumentValue("PID"));
		assertEquals(300, _parser.getArgumentValue("TYPE"));
		assertEquals(400, _parser.getArgumentValue("ACTION"));
		assertEquals(-400, _parser.getArgumentValue("LEVEL"));
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
			new String[] { "-b", "22", "-c", "100", "200", "300", "400", "-a", "11" }
		);
		assertEquals(true, _parser.optionExists("-a"));
		assertEquals(11, _parser.getOptionValue("-a"));
		assertEquals(true, _parser.optionExists("-b"));
		assertEquals(22, _parser.getOptionValue("-b"));
		assertEquals(true, _parser.optionExists("-c"));
		assertEquals(100, _parser.getArgumentValue("PID"));
		assertEquals(200, _parser.getArgumentValue("TYPE"));
		assertEquals(300, _parser.getArgumentValue("ACTION"));
		assertEquals(400, _parser.getArgumentValue("LEVEL"));
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
			new String[] { "-b", "22", "100", "200", "300", "400", "-a", "11", "-c" }
		);
		assertEquals(true, _parser.optionExists("-a"));
		assertEquals(11, _parser.getOptionValue("-a"));
		assertEquals(true, _parser.optionExists("-b"));
		assertEquals(22, _parser.getOptionValue("-b"));
		assertEquals(true, _parser.optionExists("-c"));
		assertEquals(100, _parser.getArgumentValue("PID"));
		assertEquals(200, _parser.getArgumentValue("TYPE"));
		assertEquals(300, _parser.getArgumentValue("ACTION"));
		assertEquals(400, _parser.getArgumentValue("LEVEL"));
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
			new String[] { "-b", "22", "100", "200", "300", "400", "-a", "-c" }
		);
		assertEquals(true, _parser.optionExists("-a"));
		assertEquals(-11, _parser.getOptionValue("-a"));
		assertEquals(true, _parser.optionExists("-b"));
		assertEquals(22, _parser.getOptionValue("-b"));
		assertEquals(true, _parser.optionExists("-c"));
		assertEquals(100, _parser.getArgumentValue("PID"));
		assertEquals(200, _parser.getArgumentValue("TYPE"));
		assertEquals(300, _parser.getArgumentValue("ACTION"));
		assertEquals(400, _parser.getArgumentValue("LEVEL"));
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
			new String[] { "-c", "100", "200", "300", "400", "-a", "11", "-b", "22" }
		);
		assertEquals(true, _parser.optionExists("-a"));
		assertEquals(11, _parser.getOptionValue("-a"));
		assertEquals(true, _parser.optionExists("-b"));
		assertEquals(22, _parser.getOptionValue("-b"));
		assertEquals(true, _parser.optionExists("-c"));
		assertEquals(100, _parser.getArgumentValue("PID"));
		assertEquals(200, _parser.getArgumentValue("TYPE"));
		assertEquals(300, _parser.getArgumentValue("ACTION"));
		assertEquals(400, _parser.getArgumentValue("LEVEL"));
	}
	
	@Test
	public void someOptionsAfterArgumentsAndMandatoryArgumentsOnlyAWithDefaultValue()
		throws
			ConstraintException,
			AnnotatedFieldSetException,
			CommandNotFoundException,
			IllegalCommandLineArgumentException
	{
		_parser.parse(
			new String[] { "-b", "22", "100", "200", "-a", "-c" }
		);
		assertEquals(true, _parser.optionExists("-a"));
		assertEquals(-11, _parser.getOptionValue("-a"));
		assertEquals(true, _parser.optionExists("-b"));
		assertEquals(22, _parser.getOptionValue("-b"));
		assertEquals(true, _parser.optionExists("-c"));
		assertEquals(100, _parser.getArgumentValue("PID"));
		assertEquals(200, _parser.getArgumentValue("TYPE"));
		assertEquals(-300, _parser.getArgumentValue("ACTION"));
		assertEquals(-400, _parser.getArgumentValue("LEVEL"));
	}
	
	@Test
	public void someOptionsAfterArgumentsAndMandatoryArgumentsAndOneOptionalArgumentAndOnlyAWithDefaultValue()
		throws
			ConstraintException,
			AnnotatedFieldSetException,
			CommandNotFoundException,
			IllegalCommandLineArgumentException
	{
		_parser.parse(
			new String[] { "-b", "22", "100", "200", "300", "-a", "-c" }
		);
		assertEquals(true, _parser.optionExists("-a"));
		assertEquals(-11, _parser.getOptionValue("-a"));
		assertEquals(true, _parser.optionExists("-b"));
		assertEquals(22, _parser.getOptionValue("-b"));
		assertEquals(true, _parser.optionExists("-c"));
		assertEquals(100, _parser.getArgumentValue("PID"));
		assertEquals(200, _parser.getArgumentValue("TYPE"));
		assertEquals(300, _parser.getArgumentValue("ACTION"));
		assertEquals(-400, _parser.getArgumentValue("LEVEL"));
	}
	
	// TODO: Test options between arguments.
	// TODO: The system does not recognize options between arguments (which is illegal). Figure out the algorithm.
	@Test(
		expectedExceptions = { IllegalCommandLineArgumentException.class },
		expectedExceptionsMessageRegExp =
			"Command line argument '400' is at the wrong position\\. All the arguments must "
				+ "be sequentially positioned \\(i\\.e\\. options cannot be between arguments\\)\\."
	)
	public void optionBBetweenArgumentsAndMandatoryArgumentsOnly()
		throws
			ConstraintException,
			AnnotatedFieldSetException,
			CommandNotFoundException,
			IllegalCommandLineArgumentException
	{
		_parser.parse(
			new String[] { "100", "200", "300", "-b22", "400" }
		);
	}
	
	@Test(
		expectedExceptions = { IllegalCommandLineArgumentException.class },
		expectedExceptionsMessageRegExp =
			"Command line argument '300' is at the wrong position\\. All the arguments must "
				+ "be sequentially positioned \\(i\\.e\\. options cannot be between arguments\\)\\."
	)
	public void optionBBetweenArgumentsAndMandatoryArgumentsOnly2()
		throws
			ConstraintException,
			AnnotatedFieldSetException,
			CommandNotFoundException,
			IllegalCommandLineArgumentException
	{
		_parser.parse(
			new String[] { "100", "200", "-b22", "300", "400" }
		);
	}
	
	@Test(
		expectedExceptions = { IllegalCommandLineArgumentException.class },
		expectedExceptionsMessageRegExp =
			"Too few command line arguments\\. Expected min: 2 but was: 1\\."
				+ " Check that there are no options between arguments\\."
	)
	public void optionBBetweenArgumentsAndMandatoryArgumentsOnly3()
		throws
			ConstraintException,
			AnnotatedFieldSetException,
			CommandNotFoundException,
			IllegalCommandLineArgumentException
	{
		_parser.parse(
			new String[] { "100", "-b22", "200", "300", "400" }
		);
	}
	
	@Test(
		expectedExceptions = { IllegalCommandLineArgumentException.class },
		expectedExceptionsMessageRegExp =
			"Too few command line arguments\\. Expected min: 2 but was: 1\\."
				+ " Check that there are no options between arguments\\."
	)
	public void optionCBetweenArgumentsAndMandatoryArgumentsOnly()
		throws
			ConstraintException,
			AnnotatedFieldSetException,
			CommandNotFoundException,
			IllegalCommandLineArgumentException
	{
		_parser.parse(
			new String[] { "100", "-c", "200", "300", "400" }
		);
	}
	
}
