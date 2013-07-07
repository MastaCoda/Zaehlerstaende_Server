package de.roman.meter;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.google.appengine.api.datastore.Key;

@PersistenceCapable
public class Metr
{
	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key key;

	/*@OneToMany(mappedBy="meter", cascade = CascadeType.ALL)
	private List<MeterCount> meterCounts;*/

	@Persistent
	private User userl;

//	@Persistent
//	@Enumerated(EnumType.STRING)
//	private MeterTypes types;

//	@Persistent
//	@Enumerated(EnumType.STRING)
//	private Units units;

//	@Persistent
	private String name;

	@Persistent
	private String description;


	/*public List<MeterCount> getMeterCounts()
	{
		return meterCounts;
	}

	public void setMeterCounts(List<MeterCount> meterCounts)
	{
		this.meterCounts = meterCounts;
	}*/

	public Key getKey()
	{
		return key;
	}

	public void setKey(Key key)
	{
		this.key = key;
	}

//	public User getUser()
//	{
//		return userl;
//	}
//
//	public void setUser(User userl)
//	{
//		this.userl = userl;
//	}

//	public MeterTypes getTypes()
//	{
//		return types;
//	}
//
//	public void setTypes(MeterTypes types)
//	{
//		this.types = types;
//	}

//	public Units getUnits()
//	{
//		return units;
//	}
//
//	public void setUnits(Units units)
//	{
//		this.units = units;
//	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}
}
