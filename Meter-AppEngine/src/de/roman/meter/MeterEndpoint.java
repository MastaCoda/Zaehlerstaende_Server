package de.roman.meter;

import de.roman.meter.PMF;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.config.ApiMethod.HttpMethod;
import com.google.api.server.spi.response.CollectionResponse;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.datanucleus.query.JDOCursorHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Nullable;
import javax.inject.Named;
import javax.persistence.EntityExistsException;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;

@Api(name = "metrendpoint", namespace = @ApiNamespace(ownerDomain = "roman.de", ownerName = "roman.de", packagePath = "meter"))
public class MeterEndpoint
{

	/**
	 * This method lists all the entities inserted in datastore.
	 * It uses HTTP GET method and paging support.
	 *
	 * @return A CollectionResponse class containing the list of all entities
	 * persisted and a cursor to the next page.
	 */
	@SuppressWarnings(
	{ "unchecked", "unused" })
	@ApiMethod(name = "listMetr")
	public CollectionResponse<Meter> listMetr(
			@Nullable @Named("cursor") String cursorString,
			@Nullable @Named("limit") Integer limit)
	{

		PersistenceManager mgr = null;
		Cursor cursor = null;
		List<Meter> execute = null;

		try
		{
			mgr = getPersistenceManager();
			Query query = mgr.newQuery(Meter.class);
			if (cursorString != null && cursorString != "")
			{
				cursor = Cursor.fromWebSafeString(cursorString);
				HashMap<String, Object> extensionMap = new HashMap<String, Object>();
				extensionMap.put(JDOCursorHelper.CURSOR_EXTENSION, cursor);
				query.setExtensions(extensionMap);
			}

			if (limit != null)
			{
				query.setRange(0, limit);
			}

			execute = (List<Meter>) query.execute();
			cursor = JDOCursorHelper.getCursor(execute);
			if (cursor != null)
				cursorString = cursor.toWebSafeString();

			// Tight loop for fetching all entities from datastore and accomodate
			// for lazy fetch.
			for (Meter obj : execute)
				;
		} finally
		{
			mgr.close();
		}

		return CollectionResponse.<Meter> builder().setItems(execute)
				.setNextPageToken(cursorString).build();
	}
	
	

	/**
	 * This method gets the entity having primary key id. It uses HTTP GET method.
	 *
	 * @param id the primary key of the java bean.
	 * @return The entity with primary key id.
	 */
	@ApiMethod(name = "getMetr")
	public Meter getMetr(@Named("id") Long id)
	{
		PersistenceManager mgr = getPersistenceManager();
		Meter meter = null;
		try
		{
			meter = mgr.getObjectById(Meter.class, id);
		} finally
		{
			mgr.close();
		}
		return meter;
	}
	
	/**
	 * This method gets the meterCounts list from the entity having primary key id. It
	 * uses HTTP GET method.
	 * 
	 * @param id
	 *            the primary key of the java bean.
	 * @return List of meters.
	 */
	@ApiMethod(name = "getMeterCountListWithUserId", path = "getMeterCountListWithUserId", httpMethod = HttpMethod.GET)
	public List<MeterCount> getMeterCountListWithUserId(@Named("UserId") Long userId, @Named("MeterId") Long meterId)
	{
		PersistenceManager mgr = getPersistenceManager();
		
		// create id key
		Key keyparent = KeyFactory.createKey("User", userId);
		Key meterKey = KeyFactory.createKey(keyparent, "Meter", meterId);
		
		Meter meter = null;
		MeterCount meterCount = null;
		List<MeterCount> meterCountKeys;
		List<MeterCount> meterCounts = new ArrayList<MeterCount>();
		Key meterCountKey;

		try
		{
			meter = mgr.getObjectById(Meter.class, meterKey);
			meterCountKeys = meter.getMetercounts();
		
			for (int i = 0; i < meterCountKeys.size(); i++) {
				meterCountKey = meterCountKeys.get(i).getKey();
				meterCount = mgr.getObjectById(MeterCount.class, meterCountKey);
				meterCounts.add(meterCount);
			}
		} finally
		{
			mgr.close();
		}
		return meterCounts;
	}

	/**
	 * This inserts a new entity into App Engine datastore. If the entity already
	 * exists in the datastore, an exception is thrown.
	 * It uses HTTP POST method.
	 *
	 * @param meter the entity to be inserted.
	 * @return The inserted entity.
	 */
	@ApiMethod(name = "insertMetr")
	public Meter insertMetr(Meter meter)
	{
		PersistenceManager mgr = getPersistenceManager();
		try
		{
			if (containsMetr(meter))
			{
				throw new EntityExistsException("Object already exists");
			}
			mgr.makePersistent(meter);
		} finally
		{
			mgr.close();
		}
		return meter;
	}

	/**
	 * This inserts a new Meter entity to an existing User into App Engine
	 * datastore. If the entity already exists in the datastore, an exception is
	 * thrown. It uses HTTP POST method.
	 * 
	 * @param key
	 *            meter key to which the MeterCount should be added.
	 * @param meterValue
	 *            metercount value
	 *
	 * @return The inserted entity.
	 */
	@ApiMethod(name = "insertMeterCountToUser", path = "insertMeterCountToUser", httpMethod = HttpMethod.POST)
	public MeterCount insertMeterCountToUser(@Named("UserId")long userId, @Named("MeterId")long meterId, @Named("metercountvalue") String mtrCntValue)
	{
		
		// create id key
		Key keyparent = KeyFactory.createKey("User", userId);
		Key meterKey = KeyFactory.createKey(keyparent, "Meter", meterId);
		PersistenceManager mgr = getPersistenceManager();
		Meter meter = null;
		MeterCount mtrCount = null;
		Float value = Float.parseFloat(mtrCntValue);
		Date date = new Date();
	
		try
		{
			// create new meterCount entity
			meter = mgr.getObjectById(Meter.class, meterKey);
			mtrCount = new MeterCount();
			mtrCount.setValue(value);
			mtrCount.setDate(date);
			meter.getMetercounts().add(mtrCount);
			mgr.makePersistent(meter);
			mgr.currentTransaction().begin();
			mgr.currentTransaction().commit();
			
			// update meter with new meterCount data
			updateMetr(userId, meterId, mtrCntValue, date);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (mgr.currentTransaction().isActive())
			{
				mgr.currentTransaction().rollback();
			}
			mgr.close();
		}
		return mtrCount;
	}
	
	/**
	 * This method is used for updating an existing entity. If the entity does not
	 * exist in the datastore, an exception is thrown.
	 * It uses HTTP PUT method.
	 *
	 * @param meter the entity to be updated.
	 * @return The updated entity.
	 */
	@ApiMethod(name = "updateMetr")
	public Meter updateMetr(@Named("UserId")long userId, @Named("MeterId")long meterId, @Named("Value")String mtrCntValue, @Named("Date")Date date)
	{
		PersistenceManager mgr = getPersistenceManager();
		
		// create id key		
		Key keyparent = KeyFactory.createKey("User", userId);
		Key meterKey = KeyFactory.createKey(keyparent, "Meter", meterId);
		Float value = Float.parseFloat(mtrCntValue);
		Meter meter= null;
		try
		{
			meter = mgr.getObjectById(Meter.class, meterKey);
			
			meter.setLastCount(value);
			meter.setLastCountDate(date);
			
			mgr.makePersistent(meter);
		} finally
		{
			mgr.close();
		}
		return meter;
	}

	/**
	 * This method removes the entity with primary key id.
	 * It uses HTTP DELETE method.
	 *
	 * @param id the primary key of the entity to be deleted.
	 * @return The deleted entity.
	 */
	@ApiMethod(name = "removeMetr")
	public Meter removeMetr(@Named("id") Long id)
	{
		PersistenceManager mgr = getPersistenceManager();
		Meter meter = null;
		try
		{
			meter = mgr.getObjectById(Meter.class, id);
			mgr.deletePersistent(meter);
		} finally
		{
			mgr.close();
		}
		return meter;
	}

	private boolean containsMetr(Meter meter)
	{
		if (meter.getKey() == null)
		{
			return false;
		}
		PersistenceManager mgr = getPersistenceManager();
		boolean contains = true;
		try
		{
			mgr.getObjectById(Meter.class, meter.getKey());
		} catch (javax.jdo.JDOObjectNotFoundException ex)
		{
			contains = false;
		} finally
		{
			mgr.close();
		}
		return contains;
	}

	private static PersistenceManager getPersistenceManager()
	{
		return PMF.get().getPersistenceManager();
	}

}
