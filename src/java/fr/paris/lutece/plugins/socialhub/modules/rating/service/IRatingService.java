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
package fr.paris.lutece.plugins.socialhub.modules.rating.service;

import fr.paris.lutece.plugins.socialhub.modules.rating.business.Rating;

import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;


/**
 * IRatingService.
 */
public interface IRatingService
{
    /**
     * Delete.
     *
     * @param nIdRating the n id rating
     */
    @Transactional( RatingPlugin.TRANSACTION_MANAGER )
    void remove( int nIdRating );

    /**
     * Delete by resource
     *
     * @param strIdExtendableResource the str id extendable resource
     * @param strExtendableResourceType the str extendable resource type
     */
    @Transactional( RatingPlugin.TRANSACTION_MANAGER )
    void removeByResource( String strIdExtendableResource, String strExtendableResourceType );

    /**
     * Insert.
     *
     * @param rating the rating
     */
    @Transactional( RatingPlugin.TRANSACTION_MANAGER )
    void create( Rating rating );

    /**
     * Store.
     *
     * @param rating the rating
     */
    @Transactional( RatingPlugin.TRANSACTION_MANAGER )
    void update( Rating rating );

    /**
     * Increment vote.
     *
     * @param strIdExtendableResource the str id extendable resource
     * @param strExtendableResourceType the str extendable resource type
     * @param nVoteValue the n vote value
     * @param request the request
     */
    @Transactional( RatingPlugin.TRANSACTION_MANAGER )
    void doVote( String strIdExtendableResource, String strExtendableResourceType, int nVoteValue,
        HttpServletRequest request );

    /**
     * Load.
     *
     * @param nIdRating the n id rating
     * @return the rating
     */
    Rating findByPrimaryKey( int nIdRating );

    /**
     * Select by resource.
     *
     * @param strIdExtendableResource the str id extendable resource
     * @param strExtendableResourceType the str extendable resource type
     * @return the rating
     */
    Rating findByResource( String strIdExtendableResource, String strExtendableResourceType );
}
