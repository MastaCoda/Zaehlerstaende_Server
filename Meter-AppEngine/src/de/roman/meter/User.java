package de.roman.meter;

import java.util.List;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.Join;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

@PersistenceCapable
public class User
{
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key id;
	
	//@Persistent(mappedBy = "userl")
    //private Metr meter;

	@Persistent(mappedBy = "userl")
	private List<Metr> metersl;

	
	public List<Metr> getMetersl()
	{
		return metersl;
	}

	public void setMetersl(List<Metr> metersl)
	{
		this.metersl = metersl;
	}

	@Persistent
	private String name;

	@Persistent
	private String password;

	// public List<Metr> getMeters()
	// {
	// return metersl;
	// }

	// public void setMeters(List<Metr> metersl)
	// {
	// this.metersl = metersl;
	// }

	// public Long getId()
	// {
	// return id;
	// }

//	public Metr getMetersl()
//	{
//		return metersl;
//	}

	public Key getId()
	{
		return id;
	}

	public void setId(Key id)
	{
		this.id = id;
	}

//	public void setMetersl(Metr metersl)
//	{
//		this.metersl = metersl;
//	}
//
	// public void setId(Long id)
	// {
	// this.id = id;
	// }

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}
}
