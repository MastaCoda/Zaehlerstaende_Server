package de.roman.meter;

import java.util.List;

import javax.jdo.annotations.IdGeneratorStrategy;
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

	@Persistent(mappedBy = "user")
	private List<Meter> meters;

	@Persistent
	private String email;


	public Key getId()
	{
		return id;
	}

	public void setId(Key id)
	{
		this.id = id;
	}
	
	public List<Meter> getMeters()
	{
		return meters;
	}

	public void setMeters(List<Meter> meters)
	{
		this.meters = meters;
	}

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}
}
