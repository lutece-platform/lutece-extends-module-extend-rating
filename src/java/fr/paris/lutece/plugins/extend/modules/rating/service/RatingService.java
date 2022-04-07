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

import fr.paris.lutece.plugins.extend.modules.rating.business.Rating;
import fr.paris.lutece.plugins.extend.modules.rating.business.RatingExtenderFilter;
import fr.paris.lutece.plugins.extend.modules.rating.business.RatingHome;
import fr.paris.lutece.plugins.extend.modules.rating.service.facade.RatingFacadeFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;


/**
 *
 * RatingService
 *
 */
public enum RatingService 
{
	INSTANCE;
    
	 /**
     * Remove rating by resource
     * @param strIdExtendableResource the Extendable resource id
     * @param strExtendableResourceType the Extendable Resourcer type
     */
    public void removeByResource( String strIdExtendableResource, String strExtendableResourceType )
    {        
    	RatingHome.removeByResource(strIdExtendableResource, strExtendableResourceType);
    	RatingListenerService.deleteRating( strExtendableResourceType, strIdExtendableResource, null );
    }

    /**
     * Find by primary key
     *
     * @param nIdRating the n id rating
     * @return the rating
     */
    public Optional<Rating> findByPrimaryKey( int nIdRating )
    {
    	return  RatingHome.findByPrimaryKey( nIdRating );
    }
    /**
     * find by primary key
     * @param strIdExtendableResource the str id extendable resource
     * @param strExtendableResourceType the str extendable resource type
     * @param ratingType the rating type
     * @param value the rating value
     * @return the rating
    */
    public Optional<Rating> findRating( String strIdExtendableResource, String strExtendableResourceType, String ratingType, double value, String userGuid )
    {
    	return RatingHome.findRating(strIdExtendableResource, strExtendableResourceType, ratingType, value, userGuid);
    }

    /**
     * Select by resource.
     *
     * @param strIdExtendableResource the str id extendable resource
     * @param strExtendableResourceType the str extendable resource type
     * @return the rating list
     */
    public List<Rating> findByResource( String strIdExtendableResource, String strExtendableResourceType )
    {    	
        return RatingHome.findByResource( strIdExtendableResource, strExtendableResourceType );
    }
    	
   /**
    *  load and build Rating Result
    * @param listIdExtendableResource the list of id resource
    * @param strExtendableResourceType the resource type
    * @param ratingType the rating type
    * @return rating result
    */
   public  List<Rating> findAndbuildRatingResult( List<String> listIdExtendableResource, String strExtendableResourceType, String  ratingType ) {
	  
	   List<Rating> ratings= new ArrayList<>( );
	   Map<String, List<Rating>> mapRating=RatingHome.findByResourceAndRatingType(listIdExtendableResource, strExtendableResourceType, ratingType).stream().collect(Collectors.groupingBy(Rating::getIdExtendableResource));
	   	   
	   for(Entry<String, List<Rating>> entry: mapRating.entrySet())
	   {  
		   Rating rating = entry.getValue().get( 0 );
		   rating.setRatingCount( entry.getValue().size() );
		   float[] ratinValue= new float[entry.getValue().size()];
		   int i= 0;
		   for( Rating rat :entry.getValue())
		   {
			   ratinValue[i]=rat.getRatingValue();
				   i++;
		   }
		   rating.setScoreValue( ratinValue );
		   ratings.add( rating );
	   }
	  
       return ratings;
   }
   /**
    *  load and build Rating Result
    * @param listIdExtendableResource the  of id resource
    * @param strExtendableResourceType the resource type
    * @param ratingType the rating type
    * @return rating result
    */
   public  Optional<Rating> findAndbuildRatingResult( String strIdExtendableResource, String strExtendableResourceType, String  ratingType ) {
	  
	   float[] ratinValue= RatingHome.findRatingValue( Arrays.asList(strIdExtendableResource) , strExtendableResourceType, ratingType);
	   if ( ratinValue != null && ratinValue.length > 0 )
       {
		  Rating rating = RatingFacadeFactory.getRatingInstance( ratingType );
		  rating.setExtendableResourceType(strExtendableResourceType);
		  rating.setExtenderType(ratingType);
		  rating.setIdExtendableResource( strIdExtendableResource );
		  rating.setRatingCount( ratinValue.length );
		  rating.setScoreValue( ratinValue );
	      	 
	      return Optional.ofNullable(rating);
        }
       return Optional.empty( );

   }
   
   /**
    *  load and build Rating Result
    * @param listIdExtendableResource the list of id resource
    * @param strExtendableResourceType the resource type
    * @param ratingType the rating type
    * @param  strGuid the guid 
    * @return rating result
    */
   public  List<Rating> findAndbuildRatingResult( String strIdExtendableResource, String strExtendableResourceType, String  strRatingType, String strUserGuid) {

	   Optional<Rating> optionalRating=findAndbuildRatingResult( strIdExtendableResource , strExtendableResourceType, strRatingType );
	   List<Rating> list= new ArrayList<>( );
	   if( optionalRating.isPresent( )) {
		   
		   Rating rat= optionalRating.get();
		   RatingExtenderFilter filter= new RatingExtenderFilter();
		   filter.setExtendableResourceType(strExtendableResourceType);
		   filter.setIdExtendableResource(strIdExtendableResource);
		   filter.setUserGuid(strUserGuid);
		   filter.setRatingType(strRatingType);
		   
		   List<Rating> listRating= RatingHome.findRatingByFilter(  filter );
		   listRating.forEach(rating -> 
			   {
				   rating.setRatingCount( rat.getRatingCount( ) );
				   rating.setScoreValue( rat.getScoreValue( ) );
				   list.add(rating);	   
			   }
		   );
	   
	   }
       return list ;     
   }
   
  
}
