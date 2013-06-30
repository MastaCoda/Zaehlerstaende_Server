package de.roman.meter;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Meter
{
@Id
//@GeneratedValue(strategy = GenerationType.IDENTITY)
private int id;

@ManyToOne
@JoinColumn(name = "userId")
private User user;

@Enumerated(EnumType.STRING)
private MeterTypes types;


@Enumerated(EnumType.STRING)
private Units units;

private String name;

private String description;

public int getId()
{
	return id;
}

public void setId(int id)
{
	this.id = id;
}

public User getUser()
{
	return user;
}

public void setUser(User user)
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
