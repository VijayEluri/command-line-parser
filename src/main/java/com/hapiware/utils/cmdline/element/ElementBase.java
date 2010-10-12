package com.hapiware.utils.cmdline.element;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ElementBase
{
	public final static String END_PARAGRAPH = "\n\n";
	
	private String _name;
	private Set<String> _alternatives = new HashSet<String>();
	private String _id;
	private List<String> _description = new ArrayList<String>();
	

	public ElementBase()
	{
		// Does nothing.
	}
	
	public ElementBase(ElementBase elementBase)
	{
		// Description is not needed when copy constructor is used.
		
		_name = elementBase._name;
		_id = elementBase._id;
		_alternatives.addAll(elementBase._alternatives);
	}
	
	public void name(String name)
	{
		_name = name;
	}
	
	public void alternatives(String...alternatives)
	{
		for(String alternative : alternatives)
			_alternatives.add(alternative);
	}
	
	public void id(String id)
	{
		_id = id;
	}
	
	public void description(String description)
	{
		_description.add(description);
	}
	public void p()
	{
		_description.add(END_PARAGRAPH);
	}
	
	public String name()
	{
		return _name;
	}
	
	public boolean checkAlternative(String name)
	{
		return _alternatives.contains(name);
	}
	
	public Set<String> alternatives()
	{
		return _alternatives;
	}
	
	public String id()
	{
		return _id == null ? name() : _id;
	}
	
	public List<String> description()
	{
		return _description;
	}

	@Override
	public boolean equals(Object obj)
	{
		if(obj == this)
			return true;

		if(!(obj instanceof ElementBase))
			return false;
		ElementBase elementBase = (ElementBase)obj;
		
		return _name.equals(elementBase._name);
	}

	@Override
	public int hashCode()
	{
		int resultHash = 17;
		resultHash = 31 * resultHash + (_name == null ? 0 : _name.hashCode());
		return resultHash;
	}
}
