package fr.paris.lutece.plugins.extend.modules.rating.business;


import java.util.List;
import java.util.Optional;

import fr.paris.lutece.plugins.extend.modules.rating.service.RatingPlugin;
import fr.paris.lutece.portal.service.spring.SpringContextService;

public class RatingHome {
	
	
	    private static IRatingDAO _ratingDAO = SpringContextService.getBean( "extend-rating.ratingDAO" );
	    
	    /** Default constructor **/
	    private RatingHome ()
	    {
	    	
	    }
	    /**
	     * Insert.
	     *
	     * @param rating the rating
	     */
	    public static Rating  create( Rating rating )
	    {
	        _ratingDAO.insert( rating, RatingPlugin.getPlugin(  ) );
	        return rating;
	        
	    }
   
	    /**
	     * Delete.
	     *
	     * @param nIdRating the n id rating
	     */
	    public static void remove( int nIdRating )
	    {
	    	
	        _ratingDAO.delete( nIdRating, RatingPlugin.getPlugin(  ) );
	    }
	    /**
	     * Delete By Id extender History.
	     * @param nId the Id extender History
	     */
	    public static void removeByIdExtenderHistory( long nIdHistory )
	    {
	    	
	        _ratingDAO.deleteByIdExtenderHistory( nIdHistory, RatingPlugin.getPlugin(  ) );
	    }
	    /**
	     * Delete By list Id extender History.
	     * @param listIdExtenderHistory the list Id extender History
	     */
	    public static void removeByListExtenderHistory( List<Long> listIdExtenderHistory ) 
	    {
	    	_ratingDAO.deleteByListExtenderHistory( listIdExtenderHistory, RatingPlugin.getPlugin(  ) );
	    }
	    /**
	     * Delete by resource
	     * @param strIdExtendableResource the Extendable resource id
	     * @param strExtendableResourceType the Extendable Resourcer type
	     */
	    public static void removeByResource( String strIdExtendableResource, String strExtendableResourceType )
	    {
	    	_ratingDAO.deleteByResource(strIdExtendableResource, strExtendableResourceType, RatingPlugin.getPlugin( ));
	    }
	    /**
	     * Load.
	     *
	     * @param nIdRating the n id rating
	     * @return the rating
	     */
	    public static Optional<Rating> findByPrimaryKey( int nIdRating )
	    {
	        return  _ratingDAO.load( nIdRating, RatingPlugin.getPlugin(  ) ) ;
	    }

	    /**
	     * load by primary key
	     * @param strIdExtendableResource the str id extendable resource
	     * @param strExtendableResourceType the str extendable resource type
	     * @param ratingType the rating type
	     * @param value the rating value
	     * @return the rating
	    */
	    public static Optional<Rating> findRating( String strIdExtendableResource, String strExtendableResourceType, String ratingType, double value, String userGuid )
	    {
	    	return  _ratingDAO.load(strIdExtendableResource, strExtendableResourceType, ratingType, value,  userGuid, RatingPlugin.getPlugin( ));
	    }
	     /**
	     * Select by resource.
	     *
	     * @param strIdExtendableResource the str id extendable resource
	     * @param strExtendableResourceType the str extendable resource type
	     * @return the rating list
	     */
	    public static List<Rating> findByResource( String strIdExtendableResource, String strExtendableResourceType )
	    {
	        return  _ratingDAO.loadByResource( strIdExtendableResource, strExtendableResourceType, RatingPlugin.getPlugin(  )) ;
	    }
	    /**
	     * Find a list {@link Rating}
	     * @param listResource the list of extender resourceid
	     * @param plugin the plugin
	     * @return the corresponding list {@link Rating}
	     */
		public static List<Rating> findByResourceAndRatingType( List<String>  listIdsResource, String strExtendableResourceType, String ratingType) {
			
			 return _ratingDAO.findByResourceAndRatingType(listIdsResource , strExtendableResourceType, ratingType,  RatingPlugin.getPlugin( ));
		}
		/**
	     * Find a list {@link Rating} by history ids list
	     * @param listIdHistory the list of  history id
	     * @param plugin the plugin
	     * @return the corresponding list {@link Rating}
	     */
		public static List<Rating> findByHistoryExtenderIds(List<Long> listIdHistory ) {
			
			 return _ratingDAO.findByHistoryExtenderIds(listIdHistory,  RatingPlugin.getPlugin(  ));
		}
		/**
		 * Find rating value of resource list
		 * @param listIdResource the ids resource list
		 * @param strExtendableResourceType the  resource type
		 * @param strRatingType the rating type
		 * @return array of rating value
		 */
	   public static float[] findRatingValue(List<String> listIdResource, String strExtendableResourceType , String strRatingType )
	   {
		   return  _ratingDAO.selectRatingValue( listIdResource,  strExtendableResourceType ,  strRatingType, RatingPlugin.getPlugin( ) ); 

	   }
	   /**
	    * Find the list of rating
	    * @param filter the RatingExtenderFilter
	    * @return rating object list
	    */
	   public static List<Rating> findRatingByFilter( RatingExtenderFilter filter )
		{
			return  _ratingDAO.selectRatingByFilter(filter, RatingPlugin.getPlugin( ));
		}

}
