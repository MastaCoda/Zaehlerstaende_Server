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

@Api(name = "userentityendpoint", namespace = @ApiNamespace(ownerDomain = "roman.de", ownerName = "roman.de", packagePath = "meter"))
public class UserEntityEndpoint
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
	@ApiMethod(name = "listUserEntity")
	public CollectionResponse<UserEntity> listUserEntity(
			@Nullable @Named("cursor") String cursorString,
			@Nullable @Named("limit") Integer limit)
	{

		EntityManager mgr = null;
		Cursor cursor = null;
		List<UserEntity> execute = null;

		try
		{
			mgr = getEntityManager();
			Query query = mgr
					.createQuery("select from UserEntity as UserEntity");
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

			// Tight loop for fetching all entities from datastore and accomodate
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
	 * This method gets the entity having primary key id. It uses HTTP GET method.
	 *
	 * @param id the primary key of the java bean.
	 * @return The entity with primary key id.
	 */
	@ApiMethod(name = "getUserEntity")
	public UserEntity getUserEntity(@Named("id") Long id)
	{
		EntityManager mgr = getEntityManager();
		UserEntity userentity = null;
		try
		{
			userentity = mgr.find(UserEntity.class, id);
		} finally
		{
			mgr.close();
		}
		return userentity;
	}

	/**
	 * This inserts a new entity into App Engine datastore. If the entity already
	 * exists in the datastore, an exception is thrown.
	 * It uses HTTP POST method.
	 *
	 * @param userentity the entity to be inserted.
	 * @return The inserted entity.
	 */
	@ApiMethod(name = "insertUserEntity")
	public UserEntity insertUserEntity(UserEntity userentity)
	{
		EntityManager mgr = getEntityManager();
		try
		{
			if (containsUserEntity(userentity))
			{
				throw new EntityExistsException("Object already exists");
			}
			mgr.persist(userentity);
		} finally
		{
			mgr.close();
		}
		return userentity;
	}

	/**
	 * This method is used for updating an existing entity. If the entity does not
	 * exist in the datastore, an exception is thrown.
	 * It uses HTTP PUT method.
	 *
	 * @param userentity the entity to be updated.
	 * @return The updated entity.
	 */
	@ApiMethod(name = "updateUserEntity")
	public UserEntity updateUserEntity(UserEntity userentity)
	{
		EntityManager mgr = getEntityManager();
		try
		{
			if (!containsUserEntity(userentity))
			{
				throw new EntityNotFoundException("Object does not exist");
			}
			mgr.persist(userentity);
		} finally
		{
			mgr.close();
		}
		return userentity;
	}

	/**
	 * This method removes the entity with primary key id.
	 * It uses HTTP DELETE method.
	 *
	 * @param id the primary key of the entity to be deleted.
	 * @return The deleted entity.
	 */
	@ApiMethod(name = "removeUserEntity")
	public UserEntity removeUserEntity(@Named("id") Long id)
	{
		EntityManager mgr = getEntityManager();
		UserEntity userentity = null;
		try
		{
			userentity = mgr.find(UserEntity.class, id);
			mgr.remove(userentity);
		} finally
		{
			mgr.close();
		}
		return userentity;
	}

	private boolean containsUserEntity(UserEntity userentity)
	{
		if (userentity.getId() == null)
		{
			return false;
		}
		EntityManager mgr = getEntityManager();
		boolean contains = true;
		try
		{
			UserEntity item = mgr.find(UserEntity.class, userentity.getId());
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
