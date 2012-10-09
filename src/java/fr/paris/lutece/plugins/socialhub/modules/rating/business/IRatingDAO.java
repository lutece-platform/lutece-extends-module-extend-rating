/*
 * Copyright (c) 2002-2012, Mairie de Paris
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
package fr.paris.lutece.plugins.socialhub.modules.rating.business;

import fr.paris.lutece.portal.service.plugin.Plugin;


/**
 * ICommentDAO.
 */
public interface IRatingDAO
{
    /**
     * Delete.
     *
     * @param nIdRating the n id rating
     * @param plugin the plugin
     */
    void delete( int nIdRating, Plugin plugin );

    /**
     * Delete by id hub resource.
     *
     * @param strIdExtendableResource the str id extendable resource
     * @param strExtendableResourceType the str extendable resource type
     * @param plugin the plugin
     */
    void deleteByResource( String strIdExtendableResource, String strExtendableResourceType, Plugin plugin );

    /**
     * Insert.
     *
     * @param rating the rating
     * @param plugin the plugin
     */
    void insert( Rating rating, Plugin plugin );

    /**
     * Load.
     *
     * @param nIdRating the n id rating
     * @param plugin the plugin
     * @return the rating
     */
    Rating load( int nIdRating, Plugin plugin );

    /**
     * Store.
     *
     * @param rating the rating
     * @param plugin the plugin
     */
    void store( Rating rating, Plugin plugin );

    /**
     * Select by id hub resource.
     *
     * @param strIdExtendableResource the str id extendable resource
     * @param strExtendableResourceType the str extendable resource type
     * @param plugin the plugin
     * @return the rating
     */
    Rating loadByResource( String strIdExtendableResource, String strExtendableResourceType, Plugin plugin );
}
