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

import fr.paris.lutece.plugins.extend.business.extender.ResourceExtenderDTO;
import fr.paris.lutece.plugins.extend.business.extender.ResourceExtenderDTOFilter;
import fr.paris.lutece.plugins.extend.business.extender.history.ResourceExtenderHistory;
import fr.paris.lutece.plugins.extend.business.extender.history.ResourceExtenderHistoryFilter;
import fr.paris.lutece.plugins.extend.modules.rating.business.Rating;
import fr.paris.lutece.plugins.extend.modules.rating.business.config.RatingExtenderConfig;
import fr.paris.lutece.plugins.extend.modules.rating.service.IRatingService;
import fr.paris.lutece.plugins.extend.modules.rating.service.extender.RatingResourceExtender;
import fr.paris.lutece.plugins.extend.modules.rating.util.constants.RatingConstants;
import fr.paris.lutece.plugins.extend.service.extender.IResourceExtenderService;
import fr.paris.lutece.plugins.extend.service.extender.config.IResourceExtenderConfigService;
import fr.paris.lutece.plugins.extend.service.extender.history.IResourceExtenderHistoryService;
import fr.paris.lutece.portal.service.security.LuteceUser;
import fr.paris.lutece.portal.service.security.SecurityService;
import fr.paris.lutece.portal.service.security.UserNotSignedException;

import org.apache.commons.collections.CollectionUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import javax.servlet.http.HttpServletRequest;


/**
 *
 * RatingSecurityService
 *
 */
public class RatingSecurityService implements IRatingSecurityService
{
    /** The Constant BEAN_SERVICE. */
    public static final String BEAN_SERVICE = "extend-rating.ratingSecurityService";
    private static final String FILTER_SORT_BY_DATE_VOTE = " date_creation ";
    @Inject
    private IRatingService _ratingService;
    @Inject
    private IResourceExtenderHistoryService _resourceExtenderHistoryService;
    @Inject
    @Named( RatingConstants.BEAN_CONFIG_SERVICE )
    private IResourceExtenderConfigService _configService;
    @Inject
    private IResourceExtenderService _extenderService;

    /**
     * {@inheritDoc}
     * @throws UserNotSignedException
     */
    @Override
    public boolean canVote( HttpServletRequest request, String strIdExtendableResource, String strExtendableResourceType )
        throws UserNotSignedException
    {
        // Check if the config exists
        RatingExtenderConfig config = _configService.find( RatingResourceExtender.RESOURCE_EXTENDER,
                strIdExtendableResource, strExtendableResourceType );

        if ( config == null  || isVoteClosed(config))
        {
            return false;
        }

      
        // Only connected user can vote
        if ( config.isLimitedConnectedUser(  ) && SecurityService.isAuthenticationEnable(  ) )
        {
            LuteceUser user = SecurityService.getInstance(  ).getRegisteredUser( request );

            if ( user == null )
            {
                throw new UserNotSignedException(  );
            }
        }

        // User can vote a limited time per ressource
        if ( config.getNbVotePerUser(  ) > 0 )
        {
            ResourceExtenderHistoryFilter filter = new ResourceExtenderHistoryFilter(  );

            filter.setExtendableResourceType( strExtendableResourceType );

            if ( SecurityService.isAuthenticationEnable(  ) )
            {
                LuteceUser user = SecurityService.getInstance(  ).getRegisteredUser( request );

                if ( user != null )
                {
                    filter.setUserGuid( user.getName(  ) );
                }
            }
            else
            {
            	filter.setIpAddress( request.getRemoteAddr(  ) );
            }

            List<ResourceExtenderHistory> listHistories = _resourceExtenderHistoryService.findByFilter( filter );

            if ( listHistories.size(  ) >= config.getNbVotePerUser(  ) )
            {
                // User has already use all is vote
                return false;
            }
        }

        ResourceExtenderDTOFilter extenderFilter = new ResourceExtenderDTOFilter(  );
        extenderFilter.setFilterExtendableResourceType( strExtendableResourceType );

        List<ResourceExtenderDTO> extenders = _extenderService.findByFilter( extenderFilter );

        if ( CollectionUtils.isNotEmpty( extenders ) )
        {
            for ( ResourceExtenderDTO extender : extenders )
            {
                if ( !extender.isIsActive(  ) )
                {
                    return false;
                }
            }
        }

        Rating rating = _ratingService.findByResource( strIdExtendableResource, strExtendableResourceType );

        // Check if the rating exists
        if ( rating == null )
        {
            // It is the first time the ressource is being voted
            return true;
        }

        // If it is set as unlimited vote, then the user can vote anytime
        if ( config.isUnlimitedVote(  ) )
        {
            return true;
        }

        // Search the voting histories of the user
        ResourceExtenderHistoryFilter filter = new ResourceExtenderHistoryFilter(  );
        filter.setIdExtendableResource( rating.getIdExtendableResource(  ) );

        if ( SecurityService.isAuthenticationEnable(  ) )
        {
            LuteceUser user = SecurityService.getInstance(  ).getRegisteredUser( request );

            if ( user != null )
            {
                filter.setUserGuid( user.getName(  ) );
            }
        }
        else
        {
        	filter.setIpAddress( request.getRemoteAddr(  ) );
        }
        filter.setSortedAttributeName( FILTER_SORT_BY_DATE_VOTE );
        filter.setAscSort( false );

        List<ResourceExtenderHistory> listHistories = _resourceExtenderHistoryService.findByFilter( filter );

        if ( ( listHistories != null ) && !listHistories.isEmpty(  ) )
        {
            // If unique vote, then the user is prohibited to vote
            if ( config.isUniqueVote(  ) )
            {
                return false;
            }

            // Get the last vote history
            ResourceExtenderHistory ratingHistory = listHistories.get( 0 );

            Calendar calendarToday = new GregorianCalendar(  );
            Calendar calendarVote = new GregorianCalendar(  );
            Date dateVote = ratingHistory.getDateCreation(  );
            calendarVote.setTimeInMillis( dateVote.getTime(  ) );
            calendarVote.add( Calendar.DATE, config.getNbDaysToVote(  ) );

            // The date of last vote must be < today
            if ( calendarToday.getTimeInMillis(  ) < calendarVote.getTimeInMillis(  ) )
            {
                return false;
            }
        }

        // No history found, then it is the first time the user is voting the resource
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean canDeleteVote( HttpServletRequest request, String strIdExtendableResource,
        String strExtendableResourceType )
    {
        // Check if the config exists
        RatingExtenderConfig config = _configService.find( RatingResourceExtender.RESOURCE_EXTENDER,
                strIdExtendableResource, strExtendableResourceType );

        if ( config == null  || isVoteClosed(config))
        {
            return false;
        }

        // Only connected user can delete vote
        if ( config.isDeleteVote(  ) && SecurityService.isAuthenticationEnable(  ) )
        {
            LuteceUser user = SecurityService.getInstance(  ).getRegisteredUser( request );

            if ( user == null )
            {
                return false;
            }

            ResourceExtenderHistoryFilter filter = new ResourceExtenderHistoryFilter(  );

            filter.setExtendableResourceType( strExtendableResourceType );
            filter.setUserGuid( user.getName(  ) );
            filter.setIdExtendableResource( strIdExtendableResource );

            List<ResourceExtenderHistory> listHistories = _resourceExtenderHistoryService.findByFilter( filter );

            if ( CollectionUtils.isNotEmpty( listHistories ) )
            {
                // User has already vote and so can delete it
                return true;
            }
        }

        // No history found, then it is the first time the user is voting the resource
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
	public boolean isVoteClosed(RatingExtenderConfig config) {
		
    	if ( ( config.getDateStart(  ) != null ) || ( config.getDateEnd(  ) != null ) )
         {
             // Check activation date
             if ( ( config.getDateStart(  ) != null ) && ( config.getDateStart(  ).compareTo( new Date(  ) ) > 0 ) )
             {
                 return true;
             }
             else if ( config.getDateEnd(  ) != null )
             {
                 Calendar cal = Calendar.getInstance(  );
                 cal.setTime( config.getDateEnd(  ) );
                 cal.add( Calendar.DAY_OF_WEEK, 1 );

                 if ( cal.getTime(  ).compareTo( new Date(  ) ) < 0 )
                 {
                     return true;
                 }
             }
         }
    	 
    	 return false;
	}
}
