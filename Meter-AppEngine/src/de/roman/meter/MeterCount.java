package de.roman.meter;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.google.appengine.api.datastore.Key;

@Entity
public class MeterCount
{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	//private Long id;
	private Key key;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "meterId")
	private Meter meter;
	
	private float value;
	
	private Date date;

	/*public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}
*/
	public Meter getMeter()
	{
		return meter;
	}

	public Key getKey()
	{
		return key;
	}

	public void setKey(Key key)
	{
		this.key = key;
	}

	public void setMeter(Meter meter)
	{
		this.meter = meter;
	}

	public float getValue()
	{
		return value;
	}

	public void setValue(float value)
	{
		this.value = value;
	}

	public Date getDate()
	{
		return date;
	}

	public void setDate(Date date)
	{
		this.date = date;
	}
}
