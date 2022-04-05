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
import fr.paris.lutece.plugins.extend.modules.rating.business.RatingExtenderFilter;
import fr.paris.lutece.plugins.extend.modules.rating.business.RatingHome;
import fr.paris.lutece.plugins.extend.modules.rating.business.config.RatingExtenderConfig;
import fr.paris.lutece.plugins.extend.modules.rating.service.RatingService;
import fr.paris.lutece.plugins.extend.modules.rating.service.extender.RatingResourceExtender;
import fr.paris.lutece.plugins.extend.modules.rating.service.validator.RatingValidationManagementService;
import fr.paris.lutece.plugins.extend.modules.rating.util.constants.RatingConstants;
import fr.paris.lutece.plugins.extend.service.extender.IResourceExtenderService;
import fr.paris.lutece.plugins.extend.service.extender.config.IResourceExtenderConfigService;
import fr.paris.lutece.plugins.extend.service.extender.history.IResourceExtenderHistoryService;
import fr.paris.lutece.plugins.extend.service.extender.history.ResourceExtenderHistoryService;
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


/**
 *
 * RatingSecurityService
 *
 */
public class RatingSecurityService implements IRatingSecurityService
{
    /** The Constant BEAN_SERVICE. */
    private static final String FILTER_SORT_BY_DATE_VOTE = " date_creation ";
    @Inject
    @Named( ResourceExtenderHistoryService.BEAN_SERVICE )
    private IResourceExtenderHistoryService _resourceExtenderHistoryService;  
    @Inject
    @Named( RatingConstants.BEAN_CONFIG_SERVICE )
    private IResourceExtenderConfigService _configService;
    @Inject
    private IResourceExtenderService _extenderService;

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean canVote( LuteceUser user, String strIdExtendableResource, String strExtendableResourceType )
        throws UserNotSignedException
    {
        // Check if the config exists
        RatingExtenderConfig config = _configService.find( RatingResourceExtender.RESOURCE_EXTENDER,
                strIdExtendableResource, strExtendableResourceType );

       return canVote( config, user, strIdExtendableResource, strExtendableResourceType );
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean  canVote( RatingExtenderConfig config, LuteceUser user, String strIdExtendableResource, String strExtendableResourceType )
            throws UserNotSignedException 
    {
    	
    	 if ( config == null  || isVoteClosed(config) || extenderIsNotActivated(strExtendableResourceType, strIdExtendableResource ))
         {
             return false;
         }       
         // Only connected user can vote
         if ( config.isLimitedConnectedUser(  ) && SecurityService.isAuthenticationEnable(  ) && user == null )
         {       
                 throw new UserNotSignedException(  );
         }
         // Check if we can vote
         if ( !RatingValidationManagementService.canRating(strIdExtendableResource, strExtendableResourceType,  user ) )
         {
             return false;
         }
         
         // If it is set as unlimited vote, then the user can vote anytime
         if ( config.isUnlimitedVote(  ) || RatingService.INSTANCE.findByResource( strIdExtendableResource, strExtendableResourceType)  == null )
         {
             return true;
         }
        
         // User can vote a limited time per ressource
         if ( ( config.isUniqueVote(  ) || config.getNbVotePerUser(  ) > 0 ) && user != null )
         {            
         
        	return canIfRatingIsLimited( config,  user,  strIdExtendableResource, strExtendableResourceType);
	          
         } 
         
         
         return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean canDeleteVote( LuteceUser user, String strIdExtendableResource,
        String strExtendableResourceType )
    {   
        // Check if the config exists
        RatingExtenderConfig config = _configService.find( RatingResourceExtender.RESOURCE_EXTENDER,
                strIdExtendableResource, strExtendableResourceType );

        return canDeleteVote( config, user, strIdExtendableResource, strExtendableResourceType );
       
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean canDeleteVote( RatingExtenderConfig config, LuteceUser user, String strIdExtendableResource,
        String strExtendableResourceType )
    {
        if ( config == null  || isVoteClosed(config))
        {					
            return false;
        }
        // Only connected user can delete vote
        if ( config.isDeleteVote(  ) && SecurityService.isAuthenticationEnable(  ) && RatingValidationManagementService.canCancelRating( strIdExtendableResource, strExtendableResourceType, user )
         )
        {
            return hasAlreadyVoted(user, strIdExtendableResource, strExtendableResourceType);
        }
               
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
    
    /**
     * {@inheritDoc}
     */
    @Override
	public boolean hasAlreadyVoted(LuteceUser user, String strIdExtendableResource, String strExtendableResourceType ) 
    
    {
        if ( user == null )
        {
            return false;
        }
        RatingExtenderFilter filter = new RatingExtenderFilter(  );

         filter.setExtendableResourceType( strExtendableResourceType );
         filter.setUserGuid( user.getName(  ) );
         filter.setIdExtendableResource( strIdExtendableResource );

         return  CollectionUtils.isNotEmpty(RatingHome.findRatingByFilter( filter ));
         
	}
    
    private List<ResourceExtenderHistory> findListHistories(String strExtendableResourceType, LuteceUser user)
    {
    	 ResourceExtenderHistoryFilter filter = new ResourceExtenderHistoryFilter(  );

         filter.setExtendableResourceType( strExtendableResourceType );

         filter.setExtenderType( RatingResourceExtender.RESOURCE_EXTENDER );
         filter.setUserGuid( user.getName(  ) );
         filter.setSortedAttributeName( FILTER_SORT_BY_DATE_VOTE );
         filter.setAscSort( false );

         return _resourceExtenderHistoryService.findByFilter( filter );
     
    } 
    
    private boolean extenderIsNotActivated(String strExtendableResourceType, String strIdExtendableResource ) {
    	
    	ResourceExtenderDTOFilter extenderFilter = new ResourceExtenderDTOFilter(  );
        extenderFilter.setFilterExtendableResourceType( strExtendableResourceType );
        extenderFilter.setFilterExtenderType( RatingResourceExtender.RESOURCE_EXTENDER );

        List<ResourceExtenderDTO> extenders = _extenderService.findByFilter( extenderFilter );

        if ( CollectionUtils.isNotEmpty( extenders ) )
        {
            for ( ResourceExtenderDTO extender : extenders )
            {
                if ( (extender.getIdExtendableResource().equals(ResourceExtenderDTOFilter.WILDCARD_ID_RESOURCE) 
                		|| extender.getIdExtendableResource().equals( strIdExtendableResource )) 
                		&& extender.isIsActive(  ))
                {
                    return false;
                }
            }
        }
        return true;
    }
    
    private boolean canIfRatingIsLimited(RatingExtenderConfig config, LuteceUser user, String strIdExtendableResource,  String strExtendableResourceType) 
    {
    	RatingExtenderFilter filter= new RatingExtenderFilter();
		filter.setExtendableResourceType(strExtendableResourceType);
		filter.setIdExtendableResource(strIdExtendableResource);
		filter.setUserGuid(user.getName( ));
		filter.setRatingType(config.getRatingType( ));
		   
		List<Rating> listRating= RatingHome.findRatingByFilter(  filter );
	
        if ( ( listRating != null ) && !listRating.isEmpty(  ) )
        {
          // If unique vote, then the user is prohibited to vote
          if ( (config.isUniqueVote(  ) && listRating.stream().anyMatch(rating -> rating.getIdExtendableResource().equals(strIdExtendableResource)))
          		|| (config.isLimitVote() && listRating.size(  ) >= config.getNbVotePerUser(  )) )
          {
              return false;
          }                 
         
          if( config.getNbDaysToVote() > 0 ) {
         	 
	             ResourceExtenderHistory ratingHistory= null;
	             // Get the last vote history or User has already use all is vote
	             for(ResourceExtenderHistory hist: findListHistories( strExtendableResourceType, user) )
	             {
	            	 if( listRating.stream().anyMatch(  ra-> ra.getIdHistory( ) == hist.getIdHistory( ) ) )
	            	 {
	            		 ratingHistory = hist;
	            		 break;
	            	 }
	            }
	            
	            if( ratingHistory != null && ratingHistory.getDateCreation( ).isPresent( )) {
	            	 
	            	 Calendar calendarToday = new GregorianCalendar(  );
		             Calendar calendarVote = new GregorianCalendar(  );
		             Date dateRating = ratingHistory.getDateCreation( ).get();
		             calendarVote.setTimeInMillis( dateRating.getTime(  ) );
		             calendarVote.add( Calendar.DATE, config.getNbDaysToVote(  ) );
		
		             // The date of last vote must be < today
		             if ( calendarToday.getTimeInMillis(  ) < calendarVote.getTimeInMillis(  ) )
		             {
		                 return false;
		             }  
	             }
          	}
        }
        
        return true;
    }
}
