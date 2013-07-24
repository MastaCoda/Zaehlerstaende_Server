package de.roman.meter;

import java.util.Date;
import java.util.List;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;



@PersistenceCapable
public class Meter
{
	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key key;

	@Persistent
	private User user;

	@Persistent
	private String name;

	@Persistent
	private String description;
	
	@Persistent
    private MeterTypes type;
	
	@Persistent
	private Units unit;
	
	@Persistent
	private float lastCount;
	
	@Persistent
	private Date lastCountDate;
	
	
	@Persistent(mappedBy = "meter")
	private List<MeterCount> metercounts;


	public List<MeterCount> getMetercounts()
	{
		return metercounts;
	}

	public void setMetercounts(List<MeterCount> metercounts)
	{
		this.metercounts = metercounts;
	}

	public Key getKey()
	{
		return key;
	}

	public void setKey(Key key)
	{
		this.key = key;
	}

	public void setUser(User user)
	{
		this.user = user;
	}
	
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

	public MeterTypes getType()
	{
		return type;
	}

	public void setType(MeterTypes type)
	{
		this.type = type;
	}

	public Units getUnit()
	{
		return unit;
	}

	public void setUnit(Units unit)
	{
		this.unit = unit;
	}

	public float getLastCount()
	{
		return lastCount;
	}

	public void setLastCount(float lastCount)
	{
		this.lastCount = lastCount;
	}

	public Date getLastCountDate()
	{
		return lastCountDate;
	}

	public void setLastCountDate(Date lastCountDate)
	{
		this.lastCountDate = lastCountDate;
	}
}
