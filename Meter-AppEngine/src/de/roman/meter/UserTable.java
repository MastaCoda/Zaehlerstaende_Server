package de.roman.meter;


import java.util.ArrayList;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;


@Entity
public class UserTable
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@JoinTable(name="USER_METERS")
	@JoinColumn(name="METER_ID_OID")
    //@InverseJoinColumn(name="ADDRESS_ID_EID")
	@OneToMany(mappedBy="user", cascade = CascadeType.ALL/*, fetch = FetchType.LAZY*/)
	private List<Meter> meters = new ArrayList<Meter>();

	
	private String name;

	private String password;
	
	public List<Meter> getMeters()
	{
		return meters;
	}



	public void setMeters(List<Meter> meters)
	{
		this.meters = meters;
	}


	public Long getId()
	{
		return id;
	}



	public void setId(Long id)
	{
		this.id = id;
	}

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
