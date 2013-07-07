package de.roman.meter;

import de.roman.meter.PMF;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.response.CollectionResponse;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.datanucleus.query.JDOCursorHelper;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Nullable;
import javax.inject.Named;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;

@Api(name = "metrendpoint", namespace = @ApiNamespace(ownerDomain = "roman.de", ownerName = "roman.de", packagePath = "meter"))
public class MetrEndpoint
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
	public CollectionResponse<Metr> listMetr(
			@Nullable @Named("cursor") String cursorString,
			@Nullable @Named("limit") Integer limit)
	{

		PersistenceManager mgr = null;
		Cursor cursor = null;
		List<Metr> execute = null;

		try
		{
			mgr = getPersistenceManager();
			Query query = mgr.newQuery(Metr.class);
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

			execute = (List<Metr>) query.execute();
			cursor = JDOCursorHelper.getCursor(execute);
			if (cursor != null)
				cursorString = cursor.toWebSafeString();

			// Tight loop for fetching all entities from datastore and accomodate
			// for lazy fetch.
			for (Metr obj : execute)
				;
		} finally
		{
			mgr.close();
		}

		return CollectionResponse.<Metr> builder().setItems(execute)
				.setNextPageToken(cursorString).build();
	}

	/**
	 * This method gets the entity having primary key id. It uses HTTP GET method.
	 *
	 * @param id the primary key of the java bean.
	 * @return The entity with primary key id.
	 */
	@ApiMethod(name = "getMetr")
	public Metr getMetr(@Named("id") Long id)
	{
		PersistenceManager mgr = getPersistenceManager();
		Metr metr = null;
		try
		{
			metr = mgr.getObjectById(Metr.class, id);
		} finally
		{
			mgr.close();
		}
		return metr;
	}

	/**
	 * This inserts a new entity into App Engine datastore. If the entity already
	 * exists in the datastore, an exception is thrown.
	 * It uses HTTP POST method.
	 *
	 * @param metr the entity to be inserted.
	 * @return The inserted entity.
	 */
	@ApiMethod(name = "insertMetr")
	public Metr insertMetr(Metr metr)
	{
		PersistenceManager mgr = getPersistenceManager();
		try
		{
			if (containsMetr(metr))
			{
				throw new EntityExistsException("Object already exists");
			}
			mgr.makePersistent(metr);
		} finally
		{
			mgr.close();
		}
		return metr;
	}

	/**
	 * This method is used for updating an existing entity. If the entity does not
	 * exist in the datastore, an exception is thrown.
	 * It uses HTTP PUT method.
	 *
	 * @param metr the entity to be updated.
	 * @return The updated entity.
	 */
	@ApiMethod(name = "updateMetr")
	public Metr updateMetr(Metr metr)
	{
		PersistenceManager mgr = getPersistenceManager();
		try
		{
			if (!containsMetr(metr))
			{
				throw new EntityNotFoundException("Object does not exist");
			}
			mgr.makePersistent(metr);
		} finally
		{
			mgr.close();
		}
		return metr;
	}

	/**
	 * This method removes the entity with primary key id.
	 * It uses HTTP DELETE method.
	 *
	 * @param id the primary key of the entity to be deleted.
	 * @return The deleted entity.
	 */
	@ApiMethod(name = "removeMetr")
	public Metr removeMetr(@Named("id") Long id)
	{
		PersistenceManager mgr = getPersistenceManager();
		Metr metr = null;
		try
		{
			metr = mgr.getObjectById(Metr.class, id);
			mgr.deletePersistent(metr);
		} finally
		{
			mgr.close();
		}
		return metr;
	}

	private boolean containsMetr(Metr metr)
	{
		if (metr.getKey() == null)
		{
			return false;
		}
		PersistenceManager mgr = getPersistenceManager();
		boolean contains = true;
		try
		{
			mgr.getObjectById(Metr.class, metr.getKey());
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
