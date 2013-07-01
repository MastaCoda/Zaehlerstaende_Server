package de.roman.meter;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.google.appengine.api.datastore.Key;

@Entity
public class Meter
{
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
//private long id;
private Key key;

@OneToMany(mappedBy="meter")
private List<MeterCount> meterCounts;


@ManyToOne(fetch=FetchType.LAZY)
@JoinColumn(name = "userId")
private UserTable user;

@Enumerated(EnumType.STRING)
private MeterTypes types;


@Enumerated(EnumType.STRING)
private Units units;

private String name;

private String description;



/*public long getId()
{
	return id;
}

public void setId(long id)
{
	this.id = id;
}*/


public Key getKey()
{
	return key;
}

public void setKey(Key key)
{
	this.key = key;
}

public UserTable getUser()
{
	return user;
}

public void setUser(UserTable user)
{
	this.user = user;
}

public MeterTypes getTypes()
{
	return types;
}

public void setTypes(MeterTypes types)
{
	this.types = types;
}

public Units getUnits()
{
	return units;
}

public void setUnits(Units units)
{
	this.units = units;
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
}
