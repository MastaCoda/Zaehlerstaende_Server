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

@Api(name = "metercountendpoint", namespace = @ApiNamespace(ownerDomain = "roman.de", ownerName = "roman.de", packagePath = "meter"))
public class MeterCountEndpoint
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
    @ApiMethod(name = "listMeterCount")
    public CollectionResponse<MeterCount> listMeterCount(
            @Nullable @Named("cursor") String cursorString,
            @Nullable @Named("limit") Integer limit)
    {

        PersistenceManager mgr = null;
        Cursor cursor = null;
        List<MeterCount> execute = null;

        try
        {
            mgr = getPersistenceManager();
            Query query = mgr.newQuery(MeterCount.class);
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

            execute = (List<MeterCount>) query.execute();
            cursor = JDOCursorHelper.getCursor(execute);
            if (cursor != null)
                cursorString = cursor.toWebSafeString();

            // Tight loop for fetching all entities from datastore and
            // accomodate
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
     * This method gets the entity having primary key id. It uses HTTP GET
     * method.
     * 
     * @param id
     *            the primary key of the java bean.
     * @return The entity with primary key id.
     */
    @ApiMethod(name = "getMeterCount")
    public MeterCount getMeterCount(@Named("id") Long id)
    {
        PersistenceManager mgr = getPersistenceManager();
        MeterCount metercount = null;
        try
        {
            metercount = mgr.getObjectById(MeterCount.class, id);
        } finally
        {
            mgr.close();
        }
        return metercount;
    }

    /**
     * This inserts a new entity into App Engine datastore. If the entity
     * already exists in the datastore, an exception is thrown. It uses HTTP
     * POST method.
     * 
     * @param metercount
     *            the entity to be inserted.
     * @return The inserted entity.
     */
    @ApiMethod(name = "insertMeterCount")
    public MeterCount insertMeterCount(MeterCount metercount)
    {
        PersistenceManager mgr = getPersistenceManager();
        try
        {
            if (containsMeterCount(metercount))
            {
                throw new EntityExistsException("Object already exists");
            }
            mgr.makePersistent(metercount);
        } finally
        {
            mgr.close();
        }
        return metercount;
    }

    /**
     * This method is used for updating an existing entity. If the entity does
     * not exist in the datastore, an exception is thrown. It uses HTTP PUT
     * method.
     * 
     * @param metercount
     *            the entity to be updated.
     * @return The updated entity.
     */
    @ApiMethod(name = "updateMeterCount")
    public MeterCount updateMeterCount(MeterCount metercount)
    {
        PersistenceManager mgr = getPersistenceManager();
        try
        {
            if (!containsMeterCount(metercount))
            {
                throw new EntityNotFoundException("Object does not exist");
            }
            mgr.makePersistent(metercount);
        } finally
        {
            mgr.close();
        }
        return metercount;
    }

    /**
     * This method removes the entity with primary key id. It uses HTTP DELETE
     * method.
     * 
     * @param id
     *            the primary key of the entity to be deleted.
     * @return The deleted entity.
     */
    @ApiMethod(name = "removeMeterCount")
    public MeterCount removeMeterCount(@Named("id") Long id)
    {
        PersistenceManager mgr = getPersistenceManager();
        MeterCount metercount = null;
        try
        {
            metercount = mgr.getObjectById(MeterCount.class, id);
            mgr.deletePersistent(metercount);
        } finally
        {
            mgr.close();
        }
        return metercount;
    }

    private boolean containsMeterCount(MeterCount metercount)
    {
        PersistenceManager mgr = getPersistenceManager();
        boolean contains = true;
        try
        {
            mgr.getObjectById(MeterCount.class, metercount.getKey());
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
