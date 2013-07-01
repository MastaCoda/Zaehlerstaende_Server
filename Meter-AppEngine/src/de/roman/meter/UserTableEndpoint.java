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

@Api(name = "usertableendpoint", namespace = @ApiNamespace(ownerDomain = "roman.de", ownerName = "roman.de", packagePath = "meter"))
public class UserTableEndpoint
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
	@ApiMethod(name = "listUserTable")
	public CollectionResponse<UserTable> listUserTable(
			@Nullable @Named("cursor") String cursorString,
			@Nullable @Named("limit") Integer limit)
	{

		EntityManager mgr = null;
		Cursor cursor = null;
		List<UserTable> execute = null;

		try
		{
			mgr = getEntityManager();
			Query query = mgr.createQuery("select from UserTable as UserTable");
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

			execute = (List<UserTable>) query.getResultList();
			cursor = JPACursorHelper.getCursor(execute);
			if (cursor != null)
				cursorString = cursor.toWebSafeString();

			// Tight loop for fetching all entities from datastore and accomodate
			// for lazy fetch.
			for (UserTable obj : execute)
				;
		} finally
		{
			mgr.close();
		}

		return CollectionResponse.<UserTable> builder().setItems(execute)
				.setNextPageToken(cursorString).build();
	}

	/**
	 * This method gets the entity having primary key id. It uses HTTP GET method.
	 *
	 * @param id the primary key of the java bean.
	 * @return The entity with primary key id.
	 */
	@ApiMethod(name = "getUserTable")
	public UserTable getUserTable(@Named("id") Long id)
	{
		EntityManager mgr = getEntityManager();
		UserTable usertable = null;
		try
		{
			usertable = mgr.find(UserTable.class, id);
		} finally
		{
			mgr.close();
		}
		return usertable;
	}
	
	/**
	 * This method gets the entity having primary key id. It uses HTTP GET method.
	 *
	 * @param name email address of user.
	 * @return The entity with primary key id.
	 */
	@ApiMethod(name = "getUserByEmail")
	public UserTable getUserByEmail(@Named("name") String name)
	{
		EntityManager mgr = getEntityManager();
		UserTable usertable = null;
		try
		{
			usertable = mgr.find(UserTable.class, name);
		} finally
		{
			mgr.close();
		}
		return usertable;
	}

	/**
	 * This inserts a new entity into App Engine datastore. If the entity already
	 * exists in the datastore, an exception is thrown.
	 * It uses HTTP POST method.
	 *
	 * @param usertable the entity to be inserted.
	 * @return The inserted entity.
	 */
	@ApiMethod(name = "insertUserTable")
	public UserTable insertUserTable(UserTable usertable)
	{
		EntityManager mgr = getEntityManager();
		try
		{
			if (containsUserTable(usertable))
			{
				throw new EntityExistsException("Object already exists");
			}
			mgr.persist(usertable);
		} finally
		{
			mgr.close();
		}
		return usertable;
	}

	/**
	 * This method is used for updating an existing entity. If the entity does not
	 * exist in the datastore, an exception is thrown.
	 * It uses HTTP PUT method.
	 *
	 * @param usertable the entity to be updated.
	 * @return The updated entity.
	 */
	@ApiMethod(name = "updateUserTable")
	public UserTable updateUserTable(UserTable usertable)
	{
		EntityManager mgr = getEntityManager();
		try
		{
			if (!containsUserTable(usertable))
			{
				throw new EntityNotFoundException("Object does not exist");
			}
			mgr.persist(usertable);
		} finally
		{
			mgr.close();
		}
		return usertable;
	}

	/**
	 * This method removes the entity with primary key id.
	 * It uses HTTP DELETE method.
	 *
	 * @param id the primary key of the entity to be deleted.
	 * @return The deleted entity.
	 */
	@ApiMethod(name = "removeUserTable")
	public UserTable removeUserTable(@Named("id") Long id)
	{
		EntityManager mgr = getEntityManager();
		UserTable usertable = null;
		try
		{
			usertable = mgr.find(UserTable.class, id);
			mgr.remove(usertable);
		} finally
		{
			mgr.close();
		}
		return usertable;
	}

	private boolean containsUserTable(UserTable usertable)
	{
		if (usertable.getId() == null)
		{
			return false;
		}
		EntityManager mgr = getEntityManager();
		boolean contains = true;
		try
		{
			UserTable item = mgr.find(UserTable.class, usertable.getId());
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
