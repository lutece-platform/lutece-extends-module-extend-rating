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

import fr.paris.lutece.plugins.extend.modules.rating.service.facade.RatingFacadeFactory;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.sql.DAOUtil;

import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;

/**
 * This class provides Data Access methods for Rating objects.
 */
public class RatingDAO implements IRatingDAO
{
    private static final String SQL_QUERY_INSERT = " INSERT INTO extend_rating ( rating_type, id_extender_history, id_resource, resource_type, user_guid, rating_value ) VALUES ( ?, ?, ?, ?, ?, ? ) ";
    private static final String SQL_QUERY_SELECT_ALL = " SELECT rating_type, id_rating, id_extender_history, id_resource, resource_type, user_guid, rating_value FROM extend_rating ";
    private static final String SQL_QUERY_SELECT = SQL_QUERY_SELECT_ALL + " WHERE id_rating = ? ";
    private static final String SQL_QUERY_SELECT_RATING = SQL_QUERY_SELECT_ALL + " WHERE id_resource = ? and resource_type = ? and rating_type = ? and rating_value = ? and user_guid= ? ";
    private static final String SQL_QUERY_SELECT_BY_RESOURCE = SQL_QUERY_SELECT_ALL + " WHERE id_resource = ? and resource_type = ? " ;
    private static final String SQL_QUERY_SELECT_BY_HISTORY_EXTENDER_ID = SQL_QUERY_SELECT_ALL + " WHERE id_extender_history = ? ";
    private static final String SQL_QUERY_DELETE = " DELETE FROM extend_rating WHERE id_rating = ? ";
    private static final String SQL_QUERY_DELETE_BY_RESOURCE = "  DELETE FROM extend_rating WHERE id_resource = ? and resource_type = ? ";

    private static final String SQL_QUERY_DELETE_BY_EXTENDER_HISTORY_ID = "  DELETE FROM extend_rating WHERE id_extender_history = ?  ";
    private static final String SQL_QUERY_DELETE_BY_LIST_EXTENDER_HISTORY_ID = "  DELETE FROM extend_rating WHERE id_extender_history IN (  ";

    private static final String SQL_QUERY_FIND_BY_EXTENDER_RESOURCE_ID_LIST_AND_TYPE = " SELECT rating_type, id_rating, id_extender_history, id_resource, resource_type, user_guid, rating_value FROM extend_rating WHERE rating_type= ? and resource_type = ? and  id_resource IN ( ";
    private static final String SQL_QUERY_FIND_BY_EXTENDER_HISTORY_ID_LIST = " SELECT rating_type, id_rating, id_extender_history, id_resource, resource_type, user_guid, rating_value FROM extend_rating WHERE id_extender_history IN ( ";

    private static final String SQL_QUERY_SELECT_RATING_VALUE = " SELECT rating_value FROM extend_rating WHERE rating_type= ? and resource_type = ? and  id_resource IN ( ";

    private static final String SQL_FILTER_ID_RESOURCE = " id_resource = ? ";
    private static final String SQL_FILTER_RESOURCE_TYPE = " resource_type = ? ";
    private static final String SQL_FILTER_RATING_TYPE = " rating_type = ? ";
    private static final String SQL_FILTER_RATING_VALUE = " rating_value = ? ";
    private static final String SQL_FILTER_USER_GUID = " user_guid = ? ";
    public static final String CONSTANT_WHERE = " WHERE ";
    public static final String CONSTANT_AND = " AND ";

    

    /**
     * {@inheritDoc}
     */
    @Override
    public void insert( Rating rating, Plugin plugin ) 
    {
        
    	try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, Statement.RETURN_GENERATED_KEYS, plugin ))
        {	
	        int nIndex = 1;
	        
	        daoUtil.setString( nIndex++, rating.getRatingType( ));
	        daoUtil.setLong( nIndex++, rating.getIdHistory( ) );
	        daoUtil.setString( nIndex++, rating.getIdExtendableResource( ));
	        daoUtil.setString( nIndex++, rating.getExtendableResourceType( ));
	        daoUtil.setString( nIndex++, rating.getUserGuid( ));
	        daoUtil.setFloat( nIndex++, rating.getRatingValue( ) );
	      

	        daoUtil.executeUpdate(  );
	        if ( daoUtil.nextGeneratedKey( ) )
            {
	        	rating.setIdRating( daoUtil.getGeneratedKeyInt( 1 ) );
            }
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public List<Rating> loadByResource( String strIdExtendableResource, String strExtendableResourceType, Plugin plugin )
    {
		List<Rating> listRating= new ArrayList<>( );
        int nIndex = 1;
        try( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_BY_RESOURCE, plugin )){
        	
	        daoUtil.setString( nIndex++, strIdExtendableResource );
	        daoUtil.setString( nIndex, strExtendableResourceType );
	        daoUtil.executeQuery(  );
	
	        while ( daoUtil.next(  ) )
	        {
	        	 listRating.add( buildRating(  daoUtil ) );
	        }
        }

        return listRating;
    }

    @Override
	public Rating findByHistoryExtenderId(long lIdHistoryExtenderId, Plugin plugin) {
    	try( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_BY_HISTORY_EXTENDER_ID, plugin ) )
    	{
	        daoUtil.setLong( 1, lIdHistoryExtenderId );
	        daoUtil.executeQuery(  );
	        Rating rating= null;
	        
	        if ( daoUtil.next(  ) )
	        {
	           rating= buildRating(  daoUtil  );
	        }
	        return rating;
    	}
	}


    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Rating> load( int nIdRating, Plugin plugin )
    {
    	try( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT, plugin ) )
    	{
	        daoUtil.setInt( 1, nIdRating );
	        daoUtil.executeQuery(  );
	
	        Rating rating = null;
	
	        if ( daoUtil.next(  ) )
	        {
	           rating=  buildRating(  daoUtil );
	        }
	        
	        return Optional.ofNullable(rating);
    	}
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Rating> load( String strIdExtendableResource, String strExtendableResourceType, String ratingType, double value, String userGuid, Plugin plugin )
    {
    	try( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_RATING, plugin ) )
    	{
    		daoUtil.setString(1, strIdExtendableResource);
    		daoUtil.setString(2, strExtendableResourceType);
    		daoUtil.setString(3, ratingType);
	        daoUtil.setDouble(4, value );
	        daoUtil.setString(5, userGuid );
	        daoUtil.executeQuery(  );
	
	        Rating rating = null;
	
	        if ( daoUtil.next(  ) )
	        {
	           rating=  buildRating(  daoUtil );
	        }
	        return Optional.ofNullable(rating);
    	}
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete( int nIdRating, Plugin plugin )
    {
        try (  DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, plugin ) )
        {
	        daoUtil.setInt( 1, nIdRating );
	        daoUtil.executeUpdate(  );
        }
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteByResource( String strIdResource, String strResourceType , Plugin plugin )
    {
        try (  DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE_BY_RESOURCE, plugin ) )
        {
	        daoUtil.setString( 1, strIdResource );
	        daoUtil.setString( 2, strResourceType );

	        daoUtil.executeUpdate(  );
        }
    }
   
    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteByIdExtenderHistory( long nIdExtenderHistory, Plugin plugin )
    {
        try (  DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE_BY_EXTENDER_HISTORY_ID, plugin ) )
        {
	        daoUtil.setLong( 1, nIdExtenderHistory );
	        daoUtil.executeUpdate(  );
        }
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteByListExtenderHistory( List<Long> listIdExtenderHistory, Plugin plugin )
    {
    	StringBuilder sbSql = new StringBuilder( SQL_QUERY_DELETE_BY_LIST_EXTENDER_HISTORY_ID );
	    if ( CollectionUtils.isNotEmpty( listIdExtenderHistory ) )
	    {
	        sbSql.append( listIdExtenderHistory.stream( ).map( s -> "?" ).collect( Collectors.joining( "," ) ) );
	        sbSql.append( ")" );
	    }
	    
        try (  DAOUtil daoUtil = new DAOUtil( sbSql.toString( ), plugin ) )
        {
        	int nIndex= 0;
 	       for ( long id : listIdExtenderHistory )
 	       {
 	           daoUtil.setLong( ++nIndex, id );
 	       }
 	       daoUtil.executeUpdate(  );
        }
    }
  
	@Override
	public List<Rating> findByResourceAndRatingType(List<String> listIdResource, String strExtendableResourceType , String strRatingType, Plugin plugin) {
	
		List<Rating> listRating= new ArrayList<>( );
		StringBuilder sbSql = new StringBuilder( SQL_QUERY_FIND_BY_EXTENDER_RESOURCE_ID_LIST_AND_TYPE );
	    if ( CollectionUtils.isNotEmpty( listIdResource ) )
	    {
	        sbSql.append( listIdResource.stream( ).map( s -> "?" ).collect( Collectors.joining( "," ) ) );
	        sbSql.append( ")" );
	    }
		       
	   try ( DAOUtil daoUtil = new DAOUtil( sbSql.toString() , plugin ))
	   {
	   	int nIndex= 0;
	       daoUtil.setString( ++nIndex, strRatingType );	
	       daoUtil.setString( ++nIndex, strExtendableResourceType );
	       for ( String id : listIdResource )
	       {
	           daoUtil.setString( ++nIndex, id );
	       }
	       daoUtil.executeQuery(  );
	
	       while ( daoUtil.next(  ) )
	       {
	       
	        listRating.add(buildRating(  daoUtil  ) );
	       }
	       return listRating;
	   }
	}
	
	@Override
	public float[] selectRatingValue(List<String> listIdResource, String strExtendableResourceType , String strRatingType, Plugin plugin) {
	
		List<Float> listRatingValue= new ArrayList<>( );
		
		
		
		StringBuilder sbSql = new StringBuilder( SQL_QUERY_SELECT_RATING_VALUE );
	    if ( CollectionUtils.isNotEmpty( listIdResource ) )
	    {
	        sbSql.append( listIdResource.stream( ).map( s -> "?" ).collect( Collectors.joining( "," ) ) );
	        sbSql.append( ")" );
	    }
		       
	   try ( DAOUtil daoUtil = new DAOUtil( sbSql.toString() , plugin ))
	   {
	   	int nIndex= 0;
	       daoUtil.setString( ++nIndex, strRatingType );	
	       daoUtil.setString( ++nIndex, strExtendableResourceType );
	       for ( String id : listIdResource )
	       {
	           daoUtil.setString( ++nIndex, id );
	       }
	       daoUtil.executeQuery(  );
	       while ( daoUtil.next(  ) )
	       {
	       
	    	   listRatingValue.add( daoUtil.getFloat( 1 ) );
	       }
	     
	       return ArrayUtils.toPrimitive( listRatingValue.toArray(new Float[listRatingValue.size()]) );
	   }
	}
	@Override
	public List<Rating> findByHistoryExtenderIds(List<Long> lIdHistoryExtenderId, Plugin plugin) {
	
		List<Rating> listRating= new ArrayList<>( );
		StringBuilder sbSql = new StringBuilder( SQL_QUERY_FIND_BY_EXTENDER_HISTORY_ID_LIST );
	    if ( CollectionUtils.isNotEmpty( lIdHistoryExtenderId ) )
	    {
	        sbSql.append( lIdHistoryExtenderId.stream( ).map( s -> "?" ).collect( Collectors.joining( "," ) ) );
	        sbSql.append( ")" );
	    }
		       
	   try ( DAOUtil daoUtil = new DAOUtil( sbSql.toString() , plugin ))
	   {
	   	int nIndex= 0;
	       for ( long id : lIdHistoryExtenderId )
	       {
	           daoUtil.setLong( ++nIndex, id );
	       }
	       daoUtil.executeQuery(  );
	
	       while ( daoUtil.next(  ) )
	       {
	       	 
	           listRating.add( buildRating(  daoUtil ) );
	       }
	       return listRating;
	   }
	}
	
	
	public List<Rating> selectRatingByFilter( RatingExtenderFilter filter, Plugin plugin )
    {
		List<Rating> listRating= new ArrayList<>( );
        List<String> listStrFilter = new ArrayList<>( );

        if ( filter.containsIdExtendableResource( ) )
        {
            listStrFilter.add( SQL_FILTER_ID_RESOURCE );
        }

        if ( filter.containsExtendableResourceType() )
        {
            listStrFilter.add( SQL_FILTER_RESOURCE_TYPE );
        }

        if ( filter.containsRatingType( ) )
        {
            listStrFilter.add( SQL_FILTER_RATING_TYPE );
        }
        if ( filter.containsRatingValue() )
        {
            listStrFilter.add( SQL_FILTER_RATING_VALUE );
        }
        if ( filter.containsUserGuid() )
        {
            listStrFilter.add( SQL_FILTER_USER_GUID );
        }        
        
        String strSQL = buildRequestWithFilter( SQL_QUERY_SELECT_ALL, listStrFilter );
        try ( DAOUtil daoUtil = new DAOUtil( strSQL, plugin ) )
        {
            int nPos = 0;
            if ( filter.containsIdExtendableResource( ) )
            {
            	daoUtil.setString( ++nPos, filter.getIdExtendableResource( ) );            
            }
            if ( filter.containsExtendableResourceType() )
            {
            	daoUtil.setString( ++nPos, filter.getExtendableResourceType( ) );            
            }

            if ( filter.containsRatingType( ) )
            {
            	daoUtil.setString( ++nPos, filter.getRatingType( ) );            

            }
            if ( filter.containsRatingValue() )
            {
            	daoUtil.setFloat( ++nPos, filter.getRatingValue( ) );

            }
            if ( filter.containsUserGuid() )
            {
            	daoUtil.setString( ++nPos, filter.getUserGuid() );
            }
            
            daoUtil.executeQuery( );

            while ( daoUtil.next( ) )
            {
              
                listRating.add( buildRating(  daoUtil ) );
            }
        }
        return listRating;
    }
	
	private Rating buildRating( DAOUtil daoUtil ) {
		 int nIndex =1;
		 Rating rating= RatingFacadeFactory.getRatingInstance(daoUtil.getString( 1 ));	            
         rating.setRatingType(daoUtil.getString( nIndex++ ) );
         rating.setIdRating( daoUtil.getInt( nIndex++ ) );
         rating.setIdHistory(daoUtil.getLong( nIndex++ ));
         rating.setIdExtendableResource(daoUtil.getString( nIndex++ ));
         rating.setExtendableResourceType(daoUtil.getString( nIndex++ ));
         rating.setUserGuid(daoUtil.getString( nIndex++ ));
         rating.setRatingValue(daoUtil.getFloat( nIndex ));

         return rating;
	}
	
	/**
     * Builds a query with filters placed in parameters
     * 
     * @param strSelect
     *            the select of the query
     * @param listStrFilter
     *            the list of filter to add in the query
     * @return a query
     */
    private static String buildRequestWithFilter( String strSelect, List<String> listStrFilter )
    {
        StringBuilder strBuilder = new StringBuilder( );
        strBuilder.append( strSelect );

        int nCount = 0;

        for ( String strFilter : listStrFilter )
        {
            if ( ++nCount == 1 )
            {
                strBuilder.append( CONSTANT_WHERE );
            }

            strBuilder.append( strFilter );

            if ( nCount != listStrFilter.size( ) )
            {
                strBuilder.append( CONSTANT_AND );
            }
        }

        return strBuilder.toString( );
    }

	
}
