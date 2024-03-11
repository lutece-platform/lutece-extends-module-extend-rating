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
package fr.paris.lutece.plugins.extend.modules.rating.service.security;

import fr.paris.lutece.plugins.extend.modules.rating.business.config.RatingExtenderConfig;
import fr.paris.lutece.portal.service.security.LuteceUser;
import fr.paris.lutece.portal.service.security.UserNotSignedException;



/**
 *
 * IRatingSecurityService
 *
 */
public interface IRatingSecurityService
{
    public static final String BEAN_SERVICE = "extend-rating.ratingSecurityService";

    /**
     * Check if the given user (authenticated or not) can vote or not.
     * @param user the LuteceUser
     * @param strIdExtendableResource the id extendable resource
     * @param strExtendableResourceType the extendable resource type
     * @return true, if successful 
     * @throws UserNotSignedException the UserNotSignedException
     */
    boolean canVote( LuteceUser user, String strIdExtendableResource, String strExtendableResourceType )
        throws UserNotSignedException;
    /**
     * Check if the given user (authenticated or not) can vote or not.
     * @param config the RatingExtenderConfig
     * @param user the LuteceUser
     * @param strIdExtendableResource the id extendable resource
     * @param strExtendableResourceType the extendable resource type
     * @return true, if successful 
     * @throws UserNotSignedException the UserNotSignedException
     */
    default boolean canVote( RatingExtenderConfig config, LuteceUser user, String strIdExtendableResource, String strExtendableResourceType )
        throws UserNotSignedException
    {   	
       return canVote( user, strIdExtendableResource, strExtendableResourceType );
    }

    /**
     * Check if the given user (authenticated) can delete his vote.
     *
     * @param user the LuteceUser
     * @param strIdExtendableResource the id extendable resource
     * @param strExtendableResourceType the extendable resource type
     * @return true, if successful 
     */
    boolean canDeleteVote( LuteceUser user, String strIdExtendableResource, String strExtendableResourceType );
    
    /**
     * Check if the given user (authenticated) can delete his vote.
     * 
     * @param config the RatingExtenderConfig
     * @param user the LuteceUser
     * @param strIdExtendableResource the id extendable resource
     * @param strExtendableResourceType the extendable resource type
     * @return true, if successful 
     */
    default boolean canDeleteVote( RatingExtenderConfig config, LuteceUser user, String strIdExtendableResource, String strExtendableResourceType )
    {
    	return canDeleteVote( user,  strIdExtendableResource, strExtendableResourceType );
    }

    /**
     * Check if the Votes are closed 
     * @param config the rating config
     * @return true if the votes are closed
     */
    boolean isVoteClosed( RatingExtenderConfig config);
    
    /**
     * true if the use has already Voted
     * @param request
     * @param strIdExtendableResource the str id extendable resource
     * @param strExtendableResourceType the str extendable resource type
     */
    boolean hasAlreadyVoted(LuteceUser user, String strIdExtendableResource, String strExtendableResourceType );
    
    /**
     * Check if any user rating process in progress
     * @param Lutece user
     * @return true if no rating in progress, false if User rating in progress
     */
    boolean canAccess(LuteceUser user );
    
    /**
     * free the user rating process
     * @param Lutece user
     */
    void freeAccess(LuteceUser user );
    
}
