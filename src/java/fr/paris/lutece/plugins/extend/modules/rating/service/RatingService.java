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

import fr.paris.lutece.plugins.extend.business.extender.history.ResourceExtenderHistory;
import fr.paris.lutece.plugins.extend.business.extender.history.ResourceExtenderHistoryFilter;
import fr.paris.lutece.plugins.extend.modules.rating.business.IRatingDAO;
import fr.paris.lutece.plugins.extend.modules.rating.business.Rating;
import fr.paris.lutece.plugins.extend.modules.rating.business.RatingHistory;
import fr.paris.lutece.plugins.extend.modules.rating.service.extender.RatingResourceExtender;
import fr.paris.lutece.plugins.extend.service.extender.history.IResourceExtenderHistoryService;
import fr.paris.lutece.portal.service.security.LuteceUser;
import fr.paris.lutece.portal.service.security.SecurityService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

import javax.servlet.http.HttpServletRequest;


/**
 *
 * RatingService
 *
 */
public class RatingService implements IRatingService
{
    /** The Constant BEAN_SERVICE. */
    public static final String BEAN_SERVICE = "extend-rating.ratingService";
    @Inject
    private IRatingDAO _ratingDAO;
    @Inject
    private IResourceExtenderHistoryService _resourceExtenderHistoryService;
    @Inject
    private IRatingHistoryService _ratingHistoryService;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional( RatingPlugin.TRANSACTION_MANAGER )
    public void create( Rating rating )
    {
        _ratingDAO.insert( rating, RatingPlugin.getPlugin(  ) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional( RatingPlugin.TRANSACTION_MANAGER )
    public void update( Rating rating )
    {
        _ratingDAO.store( rating, RatingPlugin.getPlugin(  ) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional( RatingPlugin.TRANSACTION_MANAGER )
   synchronized public void doVote( String strIdExtendableResource, String strExtendableResourceType, double nVoteValue,
        HttpServletRequest request )
    {
    	Rating rating = findByResource( strIdExtendableResource, strExtendableResourceType );

        // Create the rating if not exists
        if ( rating == null )
        {
            rating = new Rating(  );
            rating.setIdExtendableResource( strIdExtendableResource );
            rating.setExtendableResourceType( strExtendableResourceType );
            rating.setVoteCount( 1 );
            rating.setScoreValue( nVoteValue );
            if ( nVoteValue == 1 )
            {
            	rating.setScorePositifsVotes( rating.getScorePositifsVotes( ) + 1 );
            }
            else
            {
            	rating.setScoreNegativesVotes( rating.getScoreNegativesVotes( ) + 1 );
            }
            create( rating );
        }
        else
        {
            rating.setVoteCount( rating.getVoteCount(  ) + 1 );
            rating.setScoreValue( rating.getScoreValue(  ) + nVoteValue );
            if ( nVoteValue == 1 )
            {
            	rating.setScorePositifsVotes( rating.getScorePositifsVotes( ) + 1 );
            }
            else
            {
            	rating.setScoreNegativesVotes( rating.getScoreNegativesVotes( ) + 1 );
            }
            update( rating );
        }
        
        ResourceExtenderHistory history = _resourceExtenderHistoryService.create( RatingResourceExtender.RESOURCE_EXTENDER,
                strIdExtendableResource, strExtendableResourceType, request );

        RatingHistory ratingHistory = new RatingHistory(  );
        ratingHistory.setIdExtenderHistory( history.getIdHistory(  ) );
        ratingHistory.setVoteValue( nVoteValue );
        _ratingHistoryService.create( ratingHistory );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional( RatingPlugin.TRANSACTION_MANAGER )
    synchronized  public void doCancelVote( LuteceUser user, String strIdExtendableResource, String strExtendableResourceType )
    {
        ResourceExtenderHistoryFilter resourceExtenderHistoryFilter = new ResourceExtenderHistoryFilter(  );
        resourceExtenderHistoryFilter.setUserGuid( user.getName(  ) );
        resourceExtenderHistoryFilter.setIdExtendableResource( strIdExtendableResource );
        resourceExtenderHistoryFilter.setExtenderType( RatingResourceExtender.RESOURCE_EXTENDER );
        
        List<ResourceExtenderHistory> histories = _resourceExtenderHistoryService.findByFilter( resourceExtenderHistoryFilter );
        boolean bDecrementCount = false ;
        
        if ( CollectionUtils.isNotEmpty( histories ) )
        {
        	for(ResourceExtenderHistory history : histories)
        	{
        		RatingHistory ratingHistory = _ratingHistoryService.findByHistoryExtenderId( history.getIdHistory(  ) );
	            if ( ratingHistory != null )
	            {
		            _ratingHistoryService.remove( ratingHistory.getIdRatingHistory(  ) );
		            
		            Rating rating = findByResource( strIdExtendableResource, strExtendableResourceType );
		            rating.setVoteCount( rating.getVoteCount(  ) - 1 );
		            rating.setScoreValue( rating.getScoreValue(  ) - ratingHistory.getVoteValue(  ) );
		            if ( ! bDecrementCount && ratingHistory.getVoteValue( ) > 0 )
		            {
		            	rating.setScorePositifsVotes( rating.getScorePositifsVotes( ) - 1 );
		            	bDecrementCount = true ; 
		            }
		            if ( ! bDecrementCount && ratingHistory.getVoteValue( ) < 0 )
		            {
		            	rating.setScoreNegativesVotes( rating.getScoreNegativesVotes( ) - 1 );
		            	bDecrementCount = true ; 
		            }
		            update( rating );
	            }
	            _resourceExtenderHistoryService.remove( Integer.valueOf( "" + history.getIdHistory(  ) ) );
	            	         
        	}
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional( RatingPlugin.TRANSACTION_MANAGER )
    public void remove( int nIdRating )
    {
        _ratingDAO.delete( nIdRating, RatingPlugin.getPlugin(  ) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional( RatingPlugin.TRANSACTION_MANAGER )
    public void removeByResource( String strIdExtendableResource, String strExtendableResourceType )
    {
        _ratingDAO.deleteByResource( strIdExtendableResource, strExtendableResourceType, RatingPlugin.getPlugin(  ) );
    }

    // GET

    /**
     * {@inheritDoc}
     */
    @Override
    public Rating findByPrimaryKey( int nIdRating )
    {
        return _ratingDAO.load( nIdRating, RatingPlugin.getPlugin(  ) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Rating findByResource( String strIdExtendableResource, String strExtendableResourceType )
    {
        return _ratingDAO.loadByResource( strIdExtendableResource, strExtendableResourceType, RatingPlugin.getPlugin(  ) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Integer> findIdMostRatedResources( String strExtendableResourceType, int nItemsOffset,
        int nMaxItemsNumber )
    {
        return _ratingDAO.findIdMostRatedResources( strExtendableResourceType, nItemsOffset, nMaxItemsNumber,
            RatingPlugin.getPlugin(  ) );
    }
}
