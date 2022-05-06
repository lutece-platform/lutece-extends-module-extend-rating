/*
 * Copyright (c) 2002-2014, Mairie de Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.extend.modules.rating.service;

import fr.paris.lutece.portal.service.security.LuteceUser;
import fr.paris.lutece.portal.service.util.AppLogService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service to manage listeners over ratings
 */
public class RatingListenerService
{
    /**
     * Constant that represents every extendable resource type
     */
    public static final String CONSTANT_EVERY_EXTENDABLE_RESOURCE_TYPE = "*";

    private static Map<String, List<IRatingListener>> _mapListeners = new HashMap< >( );
    private static boolean _bHasListeners;

    /**
     * Private constructor
     */
    private RatingListenerService( )
    {

    }

    /**
     * Register a rating listener.
     * @param strExtendableResourceType The extendable resource type associated
     *            with the listener. Use
     *            {@link #CONSTANT_EVERY_EXTENDABLE_RESOURCE_TYPE} to associated
     *            the listener with every resource type.
     * @param listener The listener to register
     */
    public static synchronized void registerListener( String strExtendableResourceType, IRatingListener listener )
    {
        List<IRatingListener> listListeners = _mapListeners.get( strExtendableResourceType );
        if ( listListeners == null )
        {
            listListeners = new ArrayList< >( );
            _mapListeners.put( strExtendableResourceType, listListeners );
        }
        listListeners.add( listener );
        _bHasListeners = true;
    }

    /**
     * Check if there is listeners to notify
     * @return True if there is at last one listener, false otherwise
     */
    public static boolean hasListener( )
    {
        return _bHasListeners;
    }

    /**
     * Notify to listeners the creation of a rating. Only listeners associated
     * with the extendable resource type of the rating are notified.
     * @param strExtendableResourceType The extendable resource type of the created rating
     * @param strIdExtendableResource The extendable resource id of the rating
     */
    public static void createRating( String strExtendableResourceType, String strIdExtendableResource, LuteceUser user )
    {
        List<IRatingListener> listListeners = _mapListeners.get( strExtendableResourceType );
        if ( listListeners != null )
        {
            for ( IRatingListener listener : listListeners )
            {
                listener.rating( strIdExtendableResource, strExtendableResourceType, user );
            }
        }
        listListeners = _mapListeners.get( CONSTANT_EVERY_EXTENDABLE_RESOURCE_TYPE );
        
        if ( listListeners != null )
        {
            for ( IRatingListener listener : listListeners )
            {
                listener.rating( strIdExtendableResource, strExtendableResourceType, user );
            }
        }
    }

  

  
    /**
     * Notify to listeners the modification of a rating. Only listeners
     * associated with the extendable resource type of the rating are notified.
     * @param strExtendableResourceType The extendable resource type of the
     *            removed rating
     * @param strIdExtendableResource The extendable resource id of the rating
     * @param listIdRemovedrating The list of ids of removed ratings
     */
    public static void deleteRating( String strExtendableResourceType, String strIdExtendableResource,
    		LuteceUser user )
    {
        try
        {
            List<IRatingListener> listListeners = _mapListeners.get( strExtendableResourceType );
            if ( listListeners != null )
            {
                for ( IRatingListener listener : listListeners )
                {
                    listener.cancelRating( strIdExtendableResource, strExtendableResourceType, user );
                }
            }
            listListeners = _mapListeners.get( CONSTANT_EVERY_EXTENDABLE_RESOURCE_TYPE );
            if ( listListeners != null )
            {
                for ( IRatingListener listener : listListeners )
                {
                    listener.cancelRating( strIdExtendableResource, strExtendableResourceType, user );
                }
            }
        }
        catch ( Exception e )
        {
            AppLogService.error( e.getMessage( ), e );
        }
    }
    

}
