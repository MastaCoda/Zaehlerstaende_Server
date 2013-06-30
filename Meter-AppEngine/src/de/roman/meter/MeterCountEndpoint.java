package de.roman.meter;

import de.roman.meter.EMF;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.response.CollectionResponse;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.datanucleus.query.JPACursorHelper;

import java.util.List;

import javax.annotation.Nullable;
import javax.inject.Named;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.persistence.EntityManager;
import javax.persistence.Query;

@Api(name = "metercountendpoint", namespace = @ApiNamespace(ownerDomain = "roman.de", ownerName = "roman.de", packagePath = "meter"))
public class MeterCountEndpoint
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
	@ApiMethod(name = "listMeterCount")
	public CollectionResponse<MeterCount> listMeterCount(
			@Nullable @Named("cursor") String cursorString,
			@Nullable @Named("limit") Integer limit)
	{

		EntityManager mgr = null;
		Cursor cursor = null;
		List<MeterCount> execute = null;

		try
		{
			mgr = getEntityManager();
			Query query = mgr
					.createQuery("select from MeterCount as MeterCount");
			if (cursorString != null && cursorString != "")
			{
				cursor = Cursor.fromWebSafeString(cursorString);
				query.setHint(JPACursorHelper.CURSOR_HINT, cursor);
			}

			if (limit != null)
			{
				query.setFirstResult(0);
				query.setMaxResults(limit);
			}

			execute = (List<MeterCount>) query.getResultList();
			cursor = JPACursorHelper.getCursor(execute);
			if (cursor != null)
				cursorString = cursor.toWebSafeString();

			// Tight loop for fetching all entities from datastore and accomodate
			// for lazy fetch.
			for (MeterCount obj : execute)
				;
		} finally
		{
			mgr.close();
		}

		return CollectionResponse.<MeterCount> builder().setItems(execute)
				.setNextPageToken(cursorString).build();
	}

	/**
	 * This method gets the entity having primary key id. It uses HTTP GET method.
	 *
	 * @param id the primary key of the java bean.
	 * @return The entity with primary key id.
	 */
	@ApiMethod(name = "getMeterCount")
	public MeterCount getMeterCount(@Named("id") Long id)
	{
		EntityManager mgr = getEntityManager();
		MeterCount metercount = null;
		try
		{
			metercount = mgr.find(MeterCount.class, id);
		} finally
		{
			mgr.close();
		}
		return metercount;
	}

	/**
	 * This inserts a new entity into App Engine datastore. If the entity already
	 * exists in the datastore, an exception is thrown.
	 * It uses HTTP POST method.
	 *
	 * @param metercount the entity to be inserted.
	 * @return The inserted entity.
	 */
	@ApiMethod(name = "insertMeterCount")
	public MeterCount insertMeterCount(MeterCount metercount)
	{
		EntityManager mgr = getEntityManager();
		try
		{
			if (containsMeterCount(metercount))
			{
				throw new EntityExistsException("Object already exists");
			}
			mgr.persist(metercount);
		} finally
		{
			mgr.close();
		}
		return metercount;
	}

	/**
	 * This method is used for updating an existing entity. If the entity does not
	 * exist in the datastore, an exception is thrown.
	 * It uses HTTP PUT method.
	 *
	 * @param metercount the entity to be updated.
	 * @return The updated entity.
	 */
	@ApiMethod(name = "updateMeterCount")
	public MeterCount updateMeterCount(MeterCount metercount)
	{
		EntityManager mgr = getEntityManager();
		try
		{
			if (!containsMeterCount(metercount))
			{
				throw new EntityNotFoundException("Object does not exist");
			}
			mgr.persist(metercount);
		} finally
		{
			mgr.close();
		}
		return metercount;
	}

	/**
	 * This method removes the entity with primary key id.
	 * It uses HTTP DELETE method.
	 *
	 * @param id the primary key of the entity to be deleted.
	 * @return The deleted entity.
	 */
	@ApiMethod(name = "removeMeterCount")
	public MeterCount removeMeterCount(@Named("id") Long id)
	{
		EntityManager mgr = getEntityManager();
		MeterCount metercount = null;
		try
		{
			metercount = mgr.find(MeterCount.class, id);
			mgr.remove(metercount);
		} finally
		{
			mgr.close();
		}
		return metercount;
	}

	private boolean containsMeterCount(MeterCount metercount)
	{
		EntityManager mgr = getEntityManager();
		boolean contains = true;
		try
		{
			MeterCount item = mgr.find(MeterCount.class, metercount.getId());
			if (item == null)
			{
				contains = false;
			}
		} finally
		{
			mgr.close();
		}
		return contains;
	}

	private static EntityManager getEntityManager()
	{
		return EMF.get().createEntityManager();
	}

}
