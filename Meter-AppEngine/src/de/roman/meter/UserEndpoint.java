package de.roman.meter;

import de.roman.meter.EMF;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.response.CollectionResponse;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.datanucleus.query.JPACursorHelper;

import com.google.appengine.api.users.User;

import java.util.List;

import javax.annotation.Nullable;
import javax.inject.Named;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.persistence.EntityManager;
import javax.persistence.Query;

@Api(name = "userendpoint", namespace = @ApiNamespace(ownerDomain = "roman.de", ownerName = "roman.de", packagePath = "meter"), clientIds =
{ ClientIds.WEB_CLIENT_ID, ClientIds.ANDROID_CLIENT_ID }, audiences =
{ ClientIds.ANDROID_AUDIENCE })

public class UserEndpoint
{

	/**
	 * This method lists all the entities inserted in datastore. It uses HTTP
	 * GET method and paging support.
	 * 
	 * @return A CollectionResponse class containing the list of all entities
	 *         persisted and a cursor to the next page.
	 */
	@SuppressWarnings(
	{ "unchecked", "unused" })
	@ApiMethod(name = "listUser")
	public CollectionResponse<UserEntity> listUser(
			@Nullable @Named("cursor") String cursorString,
			@Nullable @Named("limit") Integer limit)
	{

		EntityManager mgr = null;
		Cursor cursor = null;
		List<UserEntity> execute = null;

		try
		{
			mgr = getEntityManager();
			Query query = mgr.createQuery("select from User as User");
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

			execute = (List<UserEntity>) query.getResultList();
			cursor = JPACursorHelper.getCursor(execute);
			if (cursor != null)
				cursorString = cursor.toWebSafeString();

			// Tight loop for fetching all entities from datastore and
			// accomodate
			// for lazy fetch.
			for (UserEntity obj : execute)
				;
		} finally
		{
			mgr.close();
		}

		return CollectionResponse.<UserEntity> builder().setItems(execute)
				.setNextPageToken(cursorString).build();
	}

	/**
	 * This method gets the entity having primary key id. It uses HTTP GET
	 * method.
	 * 
	 * @param id
	 *            the primary key of the java bean.
	 * @return The entity with primary key id.
	 */
	@ApiMethod(name = "getUser")
	public UserEntity getUser(@Named("id") Long id)
	{
		EntityManager mgr = getEntityManager();
		UserEntity user = null;
		try
		{
			user = mgr.find(UserEntity.class, id);
		} finally
		{
			mgr.close();
		}
		return user;
	}

	/**
	 * This inserts a new entity into App Engine datastore. If the entity
	 * already exists in the datastore, an exception is thrown. It uses HTTP
	 * POST method.
	 * 
	 * @param userEnt
	 *            the entity to be inserted.
	 * @return The inserted entity.
	 */
	@ApiMethod(name = "insertUser")
	public UserEntity insertUser(UserEntity userEnt/*, User user*/)
	{
		EntityManager mgr = getEntityManager();
		try
		{
			if (containsUser(userEnt))
			{
				throw new EntityExistsException("Object already exists");
			}
			mgr.persist(userEnt);
		} finally
		{
			mgr.close();
		}
		return userEnt;
	}

	/**
	 * This method is used for updating an existing entity. If the entity does
	 * not exist in the datastore, an exception is thrown. It uses HTTP PUT
	 * method.
	 * 
	 * @param userEnt
	 *            the entity to be updated.
	 * @return The updated entity.
	 */
	@ApiMethod(name = "updateUser")
	public UserEntity updateUser(UserEntity userEnt)
	{
		EntityManager mgr = getEntityManager();
		try
		{
			if (!containsUser(userEnt))
			{
				throw new EntityNotFoundException("Object does not exist");
			}
			mgr.persist(userEnt);
		} finally
		{
			mgr.close();
		}
		return userEnt;
	}

	/**
	 * This method removes the entity with primary key id. It uses HTTP DELETE
	 * method.
	 * 
	 * @param id
	 *            the primary key of the entity to be deleted.
	 * @return The deleted entity.
	 */
	@ApiMethod(name = "removeUser")
	public UserEntity removeUser(@Named("id") Long id)
	{
		EntityManager mgr = getEntityManager();
		UserEntity user = null;
		try
		{
			user = mgr.find(UserEntity.class, id);
			mgr.remove(user);
		} finally
		{
			mgr.close();
		}
		return user;
	}

	private boolean containsUser(UserEntity userEnt)
	{
		if (userEnt.getId() == null)
		{
			return false;
		}
		EntityManager mgr = getEntityManager();
		boolean contains = true;
		try
		{
			UserEntity item = mgr.find(UserEntity.class, userEnt.getId());
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
