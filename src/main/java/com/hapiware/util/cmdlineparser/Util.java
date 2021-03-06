package com.hapiware.util.cmdlineparser;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import com.hapiware.util.cmdlineparser.annotation.Id;
import com.hapiware.util.cmdlineparser.constraint.ConstraintException;


/**
 * An utility class for command line parser.
 * 
 * @author <a href="http://www.hapiware.com" target="_blank">hapi</a>
 *
 */
public class Util
{
	private static final String BASE_NAME_PATTERN = "\\p{Alpha}[-_\\p{Alnum}]";
	private static final String CR = "\n";

	public static final String NEGATIVE_NUMBER_PATTERN = "^-\\p{Digit}+$";
	public static final String NAME_PATTERN = "^" + BASE_NAME_PATTERN + "*$";
	public static final String OPTION_LONG_NAME_PATTERN = BASE_NAME_PATTERN + "+";
	

	
	/**
	 * Writes right justified and left indented {@code text} to an output stream (usually
	 * {@code System.out}) creating new lines when necessary. Indenting the left side is done
	 * by adding space characters until the left {@code column} is reached. {@code width} tells
	 * the width of the write area (e.g. screen).
	 * <p>
	 * For example if {@code width} is 80 and {@code column} is 10 then every line has ten (10)
	 * space characters (one in every column between 0 - 9) and characters from {@code text} are
	 * written to columns 10 - 79.
	 * 
	 * @param text
	 * 		Text to be written.
	 * 
	 * @param column
	 * 		A zero-based position of the leftmost characters (in number of characters).
	 * 
	 * @param width
	 * 		Width of the write area in characters.
	 * 
	 * @param outStream
	 * 		A stream where the justified text is to be written. Most usually {@code System.out}.
	 * 
	 * @throws RuntimeException
	 * 		If something goes wrong with writing to {@code outStream}. In practice this just
	 * 		re-throws {@link IOException}.
	 */
	public static void write(String text, int column, int width, OutputStream outStream)
	{
		write(text, column, column, width, outStream);
	}
	
	
	/**
	 * Writes right justified and left indented {@code text} to an output stream (usually
	 * {@code System.out}) creating new lines when necessary. The first line can have different
	 * indentation column than the remainder of lines. Indenting the left side is done by adding
	 * space characters until the left {@code columnFirstLine} or {@code columnRemainderLines}
	 * are reached. {@code width} tells the width of the write area (e.g. screen).
	 * <p>
	 * For example if {@code width} is 80, {@code columnFirstLine} is 10 and
	 * {@code columnRemainderLines} is 5 then the first line will have ten (10) space characters
	 * (one in every column between 0 - 9) and characters from {@code text} are written to columns
	 * 10 - 79. The remainder of lines will have five (5) space characters (columns 0 - 4) and
	 * characters from {@code text} are written to columns 5 - 79.
	 * 
	 * @param text
	 * 		Text to be written.
	 * 
	 * @param columnFirstLine
	 * 		A zero-based position of the leftmost characters (in number of characters) of
	 * 		the first line.
	 * 
	 * @param columnRemainderLines
	 * 		A zero-based position of the leftmost characters (in number of characters) of
	 * 		the remainder of lines if they exists.
	 * 
	 * @param width
	 * 		Width of the write area in characters.
	 * 
	 * @param outStream
	 * 		A stream where the justified text is to be written. Most usually {@code System.out}.
	 * 
	 * @throws RuntimeException
	 * 		If something goes wrong with writing to {@code outStream}. In practice this just
	 * 		re-throws {@link IOException}.
	 */
	public static void write(
		String text,
		int columnFirstLine,
		int columnRemainderLines,
		int width,
		OutputStream outStream
	)
	{
		if(text == null)
			throw new NullPointerException("'text' must have a value.");
		if(outStream == null)
			throw new NullPointerException("'outStream' must have a value.");
		if(columnFirstLine < 0)
			throw
				new IllegalArgumentException(
					"'columnFirstLine' must be greater or equal than zero (0)."
				);
		if(columnRemainderLines < 0)
			throw
				new IllegalArgumentException(
					"'columnRemainderLines' must be greater or equal than zero (0)."
				);
		if(width < 0)
			throw
				new IllegalArgumentException(
					"'width' must be greater or equal than zero (0)."
				);
		if(columnFirstLine >= width)
			throw new IllegalArgumentException("'width' must be greater than 'columnFirstLine'.");
		if(columnRemainderLines >= width)
			throw
				new IllegalArgumentException(
					"'width' must be greater than 'columnRemainderLines'."
				);
		
		StringBuilder firstLineTab = new StringBuilder();
		for(int i = 0; i < columnFirstLine; i++)
			firstLineTab.append(" ");
		StringBuilder remainderLinesTab = new StringBuilder();
		for(int i = 0; i < columnRemainderLines; i++)
			remainderLinesTab.append(" ");

		StringBuilder toWrite = new StringBuilder(firstLineTab);
		StringTokenizer tokenizer = new StringTokenizer(text);
		int columnUnderWork = columnFirstLine;
		int pos = columnUnderWork;
		while(tokenizer.hasMoreTokens()) {
			String token = tokenizer.nextToken();
			if(pos + token.length() > width) {
				if(pos != columnUnderWork) {
					if(toWrite.charAt(toWrite.length() - 1) == ' ')
						toWrite.deleteCharAt(toWrite.length() - 1);
					toWrite.append(CR).append(remainderLinesTab);
					columnUnderWork = columnRemainderLines;
					pos = columnUnderWork;
				}
				if(token.length() > width - columnUnderWork) {
					String rest = token;
					while(rest.length() > width - columnUnderWork) {
						toWrite.append(
							rest.substring(0, width - columnUnderWork)
						).append(CR).append(remainderLinesTab);
						rest = rest.substring(width - columnUnderWork);
						columnUnderWork = columnRemainderLines;
						pos = columnUnderWork;
					}
					token = rest;
				}
			}
			pos += token.length();
			toWrite.append(token);
			if(tokenizer.hasMoreTokens()) {
				pos++;
				toWrite.append(" ");
			}
		}
		try {
			outStream.write(toWrite.toString().getBytes());
		}
		catch(IOException e) {
			throw new RuntimeException("Writing to an output stream failed.", e);
		}
	}

	
	/**
	 * Checks the name pattern. {@code name} must match this RE pattern:
	 * <code>^\\p{Alpha}[-_\\p{Alnum}]*$</code>.
	 * 
	 * @param name
	 * 		A name to be checked.
	 * 
	 * @return
	 * 		{@code true} if {@code name} matches the pattern. {@code false} otherwise.
	 */
	public static boolean checkName(String name)
	{
		return Pattern.matches(NAME_PATTERN, name);
	}
	
	/**
	 * Checks the option naming. {@code optionName} must match this RE pattern:
	 * <code>^-\\p{Alpha}|--\\p{Alpha}[-_\\p{Alnum}]+$</code>.
	 * 
	 * @param optionName
	 * 		An option name with preceding minus(es) to be checked.
	 * 
	 * @return
	 * 		{@code true} if {@code optionName} matches the pattern. {@code false} otherwise.
	 */
	public static boolean checkOptionNaming(String optionName)
	{
		return Pattern.matches("^-\\p{Alpha}|--" + OPTION_LONG_NAME_PATTERN + "$", optionName);
	}
	
	static void checkOptionName(String optionName)
	{
		if(!checkOptionNaming(optionName)) {
			String msg = "'" + optionName + "' must have the preceding minus character(s).";
			throw new IllegalArgumentException(msg);
		}
	}
	
	
	static boolean checkOption(
		String arg,
		List<String> cmdLineArgs,
		Map<String, Option.Internal> definedOptions,
		Map<String, String> definedOptionAlternatives,
		Set<Option.Internal> nonMultipleOptionCheckSet,
		List<Option.Internal> cmdLineOptions
	) throws ConstraintException, IllegalCommandLineArgumentException
	{
		Option.Internal option = definedOptions.get(definedOptionAlternatives.get(arg));
		if(option != null)
			option = new Option.Internal(option);
		if(option != null && !option.multiple()) {
			if(nonMultipleOptionCheckSet.contains(option)) {
				String msg = "Option '" + option.name() + "' can occur only once.";
				throw new IllegalCommandLineArgumentException(msg);
			}
			else
				nonMultipleOptionCheckSet.add(option);
		}
		if(option != null && option.parse(cmdLineArgs)) {
			// Option found.
			if(option.argument() != null)
				option.argument().checkConstraints();
			cmdLineOptions.add(option);
			return true;
		}
		return false;
	}
	
	@SuppressWarnings("unchecked")
	static boolean checkArguments(
		String commandName,
		List<String> cmdLineArgs,
		Map<String, Argument.Internal<?>> definedArguments,
		List<Argument.Internal<?>> outputArguments
	) throws ConstraintException, IllegalCommandLineArgumentException
	{
		int numberOfOptionalArguments = 0;
		Set<Entry<String, Argument.Internal<?>>> entrySet = definedArguments.entrySet();
		for(Iterator<?> it = entrySet.iterator(); it.hasNext();) {
			Entry<String, Argument.Internal<?>> entry = (Entry<String, Argument.Internal<?>>)it.next();
			if(entry.getValue().optional())
				numberOfOptionalArguments++;
		}

		int numberOfMandatoryArguments = entrySet.size() - numberOfOptionalArguments;
		int numberOfMaximumArguments = numberOfMandatoryArguments + numberOfOptionalArguments;
		int mandatoryOptionalDiff = numberOfMaximumArguments - numberOfMandatoryArguments;
		int numberOfCmdLineArguments = 0;
		
		// There cannot be options between command arguments. Only before or after
		// all the command arguments.
		for(String cmdLineArg : cmdLineArgs) {
			if(cmdLineArg.startsWith("-") && !Pattern.matches(NEGATIVE_NUMBER_PATTERN, cmdLineArg))
				break;
			else
				numberOfCmdLineArguments++;
		}
		
		if(numberOfCmdLineArguments < numberOfMandatoryArguments) {
			String msg =
				"Too few command line arguments"
					+ (commandName != null ? " for command '" + commandName + "'" : "")
					+ ". Expected min: " + numberOfMandatoryArguments
					+ " but was: " + numberOfCmdLineArguments + "."
					+ (
						cmdLineArgs.size() > numberOfCmdLineArguments ?
						" Check that there are no options between arguments." :
						""
					);
			throw new IllegalCommandLineArgumentException(msg);
		}
		if(numberOfCmdLineArguments > numberOfMaximumArguments) {
			String msg =
				"Too many command line arguments"
					+ (commandName != null ? " for command '" + commandName + "'" : "")
					+ ". Expected max: " + numberOfMaximumArguments
					+ " but was: " + numberOfCmdLineArguments + ".";
			throw new IllegalCommandLineArgumentException(msg);
		}

		for(Iterator<?> it = entrySet.iterator(); it.hasNext();) {
			Entry<String, Argument.Internal<?>> entry = (Entry<String, Argument.Internal<?>>)it.next();
			Argument.Internal<?> argument = entry.getValue();
			if(argument.optional() && numberOfCmdLineArguments < entrySet.size())
				if(mandatoryOptionalDiff == 1) {
					// Adds a default value to one optional argument.
					if(it.hasNext() || cmdLineArgs.size() == 0)
						((LinkedList<String>)cmdLineArgs).addFirst(argument.defaultValueAsString());
					else
						break;
				}
				else
					// Adds default values to the rest of the optional arguments
					// (which must be at end of the command definition).
					if(cmdLineArgs.size() == 0)
						((LinkedList<String>)cmdLineArgs).addFirst(argument.defaultValueAsString());
			
			if(argument.parse(cmdLineArgs)) {
				argument.checkConstraints();
				outputArguments.add(argument);
			}
		}
		return true;
	}
	

	static void setAnnotatedOptions(
		Object callerObject,
		Class<?> callerClass,
		List<Option.Internal> cmdLineOptions
	)
		throws AnnotatedFieldSetException
	{
		Map<String, List<Object>> multipleOptions =
			new HashMap<String, List<Object>>();
		for(Option.Internal cmdLineOption : cmdLineOptions) {
			if(cmdLineOption.argument() != null) {
				String id = cmdLineOption.argument().id();
				if(cmdLineOption.multiple()) {
					List<Object> multipleOptionValues = multipleOptions.get(id);
					if(multipleOptionValues == null) {
						multipleOptionValues = new ArrayList<Object>();
						multipleOptions.put(id, multipleOptionValues);
					}
					multipleOptionValues.add(cmdLineOption.argument().value());
				}
				else
					Util.setAnnotatedValue(
						callerObject,
						callerClass,
						cmdLineOption.argument().value(),
						id
					);
			}
			else {
				// If an option does not have any arguments defined
				// then only its existence is marked and in this case
				// the preceding minus characters must be removed.
				Util.setAnnotatedValue(
					callerObject,
					callerClass,
					true,
					removeOptionMinusFromId(cmdLineOption.id())
				);
			}
		}
		for(Map.Entry<String, List<Object>> multiOption : multipleOptions.entrySet()) {
			Util.setAnnotatedValue(
				callerObject,
				callerClass,
				multiOption.getValue().toArray(new Object[0]),
				multiOption.getKey()
			);
		}
	}

	
	static void setAnnotatedArguments(
		Object callerObject,
		Class<?> callerClass,
		List<Argument.Internal<?>> cmdLineArguments
	)
		throws
			AnnotatedFieldSetException
	{
		for(Argument.Internal<?> cmdLineArgument : cmdLineArguments)
			setAnnotatedValue(callerObject, callerClass, cmdLineArgument.value(), cmdLineArgument.id());
	}
	
	
	static <T> void setAnnotatedValue(
		Object callerObject,
		Class<?> callerClass,
		T value,
		String valueId
	)
		throws
			AnnotatedFieldSetException
	{
		if(callerObject != null)
			callerClass = callerObject.getClass();
		if(callerClass == null)
			throw new NullPointerException("'callerClass' (or 'callerObject') must have a value.");

		setValues(callerObject, callerClass.getDeclaredFields(), value, valueId);
		setValues(callerObject, callerClass.getFields(), value, valueId);
	}
	
	private static <T> void setValues(
		Object obj,
		Field[] fields,
		T value,
		String valueId
	)
		throws
			AnnotatedFieldSetException
	{
		try {
			for(final Field f : fields) {
				Id id = f.getAnnotation(Id.class);
				if(id != null && id.value().equals(valueId)) {
					AccessController.doPrivileged(
						new PrivilegedAction<T>()
						{
							public T run()
							{
								f.setAccessible(true);
								return null;
							}
						}
					);
					if(f.getType().isArray()) {
						int length = Array.getLength(value);
						if(length > 0) {
							Object[] origArray = (Object[])value;
							Object array = Array.newInstance(f.getType().getComponentType(), length);
							for(int i = 0; i < length; i++)
								Array.set(array, i, origArray[i]);
							f.set(obj, array);
						}
					}
					else
						f.set(obj, value);
				}
			}
		}
		catch(NullPointerException e) {
			if(obj == null) {
				String msg =
					"Object reference for the field annotated '" + valueId + "' "
						+ "is missing while required. Most probably the default parse(String[]), "
						+ "parsec(String[]) or parsech(String[]) method failed finding the correct "
						+ "object reference. Use either parse[ch](Object,String[]) or "
						+ "parse[ch](Class<?>,String[]).";
				throw new AnnotatedFieldSetException(msg, e);
			}
			else
				throw e;
		}
		catch(SecurityException e) {
			String msg =
				"Security is turned on and the field annotated '" + valueId + "' cannot be accessed. "
					+ "Grant access to 'java.lang.reflect.ReflectPermission \"suppressAccessChecks\"'.";
			throw new AnnotatedFieldSetException(msg, e);
		}
		catch(IllegalArgumentException e) {
			String msg =
				"[" + value + "] is an illegal argument for the field annotated '"
					+ valueId + "'. " + e.getMessage();
			throw new AnnotatedFieldSetException(msg, e);
		}
		catch(IllegalAccessException e) {
			String msg = "Should not be here but here we are...";
			throw new AnnotatedFieldSetException(msg, e);
		}
	}
	
	static String removeOptionMinusFromId(String id)
	{
		if(id.startsWith("-"))
			return (id.length() > 2 ? id.substring(2) : id.substring(1));
		else
			return id;
	}
	
	static Object valueOf(String valueAsString, Class<?> argumentTypeClass)
	{
		if(argumentTypeClass == Integer.class)
			return Integer.valueOf(valueAsString);
		if(argumentTypeClass == Long.class)
			return Long.valueOf(valueAsString);
		if(argumentTypeClass == Byte.class)
			return Byte.valueOf(valueAsString);
		if(argumentTypeClass == Short.class)
			return Short.valueOf(valueAsString);
		if(argumentTypeClass == Double.class)
			return Double.valueOf(valueAsString);
		if(argumentTypeClass == Float.class)
			return Float.valueOf(valueAsString);
		if(argumentTypeClass == BigDecimal.class)
			return new BigDecimal(valueAsString);
		if(argumentTypeClass == BigInteger.class)
			return new BigInteger(valueAsString);
		return valueAsString;
	}
}
