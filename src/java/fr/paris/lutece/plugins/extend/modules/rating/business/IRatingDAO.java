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
package fr.paris.lutece.plugins.extend.modules.rating.business;

import fr.paris.lutece.portal.service.plugin.Plugin;

import java.util.List;
import java.util.Optional;


/**
 * IRatingDAO.
 */
public interface IRatingDAO
{
	 /**
     * Delete.
     * @param nId the n id rating
     * @param plugin the plugin
     */
    void delete( int nId, Plugin plugin );

    /**
     * Delete By Id extender History.
     * @param nId the Id extender History
     * @param plugin the plugin
     */
    void deleteByIdExtenderHistory( long nIdHistory, Plugin plugin );
    /**
     * Delete By list Id extender History.
     * @param listIdExtenderHistory the list Id extender History
     * @param plugin the plugin
     */
    void deleteByListExtenderHistory( List<Long> listIdExtenderHistory, Plugin plugin );
    /**
     * Delete by resource
     * @param strIdExtendableResource the Extendable resource id
     * @param strExtendableResourceType the Extendable Resourcer type
     * @param plugin the plugin 
     */
    void deleteByResource( String strIdExtendableResource, String strExtendableResourceType, Plugin plugin );


    /**
     * Insert.
     * @param rating the rating
     * @param plugin the plugin
     */
    void insert( Rating rating, Plugin plugin );

    /**
     * load by primary key
     * @param nIdRating the rating id
     * @param plugin the plugin
     * @return the corresponding {@link Rating}
     */
    Optional<Rating> load( int nIdRating, Plugin plugin );
    /**
     * load by primary key
     * @param strIdExtendableResource the str id extendable resource
     * @param strExtendableResourceType the str extendable resource type
     * @param ratingType the rating type
     * @param value the rating value
     * @param plugin the plugin
     * @return the rating
    */
    Optional<Rating> load( String strIdExtendableResource, String strExtendableResourceType, String ratingType, double value, String userGuid, Plugin plugin ); 
    /**
     * Select by resource.
     *
     * @param strIdExtendableResource the str id extendable resource
     * @param strExtendableResourceType the str extendable resource type
     * @param plugin the plugin
     * @return the rating list
     */
    List<Rating> loadByResource( String strIdExtendableResource, String strExtendableResourceType, Plugin plugin );

    /**
     * Find a {@link Rating}
     * @param lIdHistoryExtenderId the extender id
     * @param plugin the plugin
     * @return the corresponding {@link Rating}
     */
    Rating findByHistoryExtenderId( long lIdHistoryExtenderId, Plugin plugin );
    /**
     * Find a list {@link Rating}
     * @param listExtendableResource the list of extender resource id
     * @param strExtendableResourceType the str extendable resource type
     * @param ratingType the rating type
     * @param plugin the plugin
     * @return the corresponding list {@link Rating}
     */
    List<Rating> findByResourceAndRatingType( List<String> listExtendableResource ,String strExtendableResourceType, String ratingType, Plugin plugin );
   /**
    * Find a list {@link Rating}
    * @param lIdHistoryExtenderId the list of extender id
    * @param plugin the plugin
    * @return the corresponding list {@link Rating}
    */
   List<Rating> findByHistoryExtenderIds( List<Long> lIdHistoryExtenderId , Plugin plugin );
   
   float[] selectRatingValue(List<String> listIdResource, String strExtendableResourceType , String strRatingType, Plugin plugin); 
   
    List<Rating> selectRatingByFilter( RatingExtenderFilter filter, Plugin plugin );

}   
