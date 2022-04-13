package fr.paris.lutece.plugins.extend.modules.rating.service.facade;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import fr.paris.lutece.plugins.extend.modules.rating.business.Rating;
import fr.paris.lutece.plugins.extend.modules.rating.business.config.RatingExtenderConfig;
import fr.paris.lutece.plugins.extend.modules.rating.service.security.IRatingSecurityService;
import fr.paris.lutece.plugins.extend.modules.rating.service.security.RatingException;
import fr.paris.lutece.plugins.extend.modules.rating.util.constants.RatingConstants;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.security.UserNotSignedException;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.web.l10n.LocaleService;



public class  RatingFacadeFactory {

    private static  List<RatingType> _listRatingType = new ArrayList< >();
	private static IRatingSecurityService _ratingSecurityService;
	
	
	protected RatingFacadeFactory( )
	{		
	}
	/**
	 * Do Rating
	 * @param rating the rating object
	 * @throws UserNotSignedException the UserNotSignedException
	 * @throws RatingException the RatingException
	 */
	public static void doRating( Rating rating  ) throws UserNotSignedException, RatingException
	{
		 if ( !_ratingSecurityService.canVote( rating.getUser(), rating.getIdExtendableResource( ), rating.getExtendableResourceType( ) ) )
	        {
	        	throw new RatingException( I18nService.getLocalizedString(RatingConstants.MESSAGE_CANNOT_VOTE, LocaleService.getDefault( ) ));
	        }
		   _listRatingType.stream().filter( rat ->  rat.getType( ).equals( rating.getClass( ) ) )
				.findAny().orElseThrow( RatingTypeException::new ).doRating( rating );		

	}
	
	/**
	 * Do Rating
	 * @param config the configuration of the rating extender
	 * @param rating the rating object
	 * @throws UserNotSignedException the UserNotSignedException
	 * @throws RatingException the RatingException
	 */
	public static void doRating(RatingExtenderConfig config,  Rating rating  ) throws UserNotSignedException, RatingException
	{
		// Check if the config exists
        
		 if ( !_ratingSecurityService.canVote( config, rating.getUser(), rating.getIdExtendableResource( ), rating.getExtendableResourceType( ) ) )
	        {
	        	throw new RatingException( I18nService.getLocalizedString(RatingConstants.MESSAGE_CANNOT_VOTE, LocaleService.getDefault( ) ));
	        }
		   _listRatingType.stream().filter( rat ->  rat.getType( ).equals( rating.getClass( ) ) )
				.findAny().orElseThrow( RatingTypeException::new ).doRating( rating );		

	}
	/**
	 * Do cancel rating
	 * @param rating the rating object
	 * @throws RatingException the RatingException
	 */
	public static void cancelRating( Rating rating ) throws RatingException
	{
		if ( !_ratingSecurityService.canDeleteVote( rating.getUser( ), rating.getIdExtendableResource(), rating.getExtendableResourceType( ) ) )
        {
    		throw new RatingException( RatingConstants.MESSAGE_CANNOT_VOTE );
        }
		 _listRatingType.stream().filter( rat ->  rat.getType( ).equals( rating.getClass( ) ) )
			.findAny().orElseThrow( RatingTypeException::new ).cancelRating( rating );	  		

	}
	/**
	 * Do cancel rating
	 * @param config the configuration of the rating extender
	 * @param rating the rating object
	 * @throws RatingException the RatingException
	 */
	public static void cancelRating( RatingExtenderConfig config,  Rating rating ) throws RatingException
	{
		if ( !_ratingSecurityService.canDeleteVote( config, rating.getUser( ), rating.getIdExtendableResource(), rating.getExtendableResourceType( ) ) )
        {
    		throw new RatingException( RatingConstants.MESSAGE_CANNOT_VOTE );
        }
		 _listRatingType.stream().filter( rat ->  rat.getType( ).equals( rating.getClass( ) ) )
			.findAny().orElseThrow( RatingTypeException::new ).cancelRating( rating );	  		

	}
	/**
	 * Get PageAddOn
	 * 
	 * @param config the configuration of the rating extender
	 * @param strIdExtendableResource
	 * 					the idExtendableResource
	 * @param strExtendableResourceType
	 * 				the extendableResourceType 
	 * @param strParameters
	 * 				the parameters 
	 * @param request the request
	 * @return Page builded
	 */
	public static String getPageAddOn( RatingExtenderConfig config, String strIdExtendableResource, String strExtendableResourceType, String strParameters,
	        HttpServletRequest request  ){
		
		return   _listRatingType.stream().filter( rat ->  rat.getTypeName( ).equals( config.getRatingType( ) ) )
				.findAny().orElseThrow( RatingTypeException::new ).getPageAddOn(  config, strIdExtendableResource, strExtendableResourceType, strParameters,
				         request);	  		
	}
	/**
	 * Get the rating list by id resources
	 * @param strIdExtendableResource
	 * 					the idExtendableResource
	 * @param strExtendableResourceType
	 * 				the extendableResourceType 
	 * @return the list of rating
	 */
	public static List<Rating> getInfoRatingByList(List<String> listIdExtendableResource, String strExtendableResourceType)
	{
		List<Rating> listRating= new ArrayList<>();
		_listRatingType.forEach( type -> listRating.addAll( type.getInfoExtenderByList( listIdExtendableResource, strExtendableResourceType)));
		return listRating;
	}
	/**
	 * Get rating list information object 
	 * @param strIdExtendableResource
	 * 					the idExtendableResource
	 * @param strExtendableResourceType
	 * 				the extendableResourceType 
	 * @return the list of rating
	 * 	 
	 * */
	public static List<Rating> getInfoRating(String strIdExtendableResource, String strExtendableResourceType )
	{
		List<Rating> listRating= new ArrayList<>();
		_listRatingType.forEach( type -> listRating.addAll( type.getInfoExtender( strIdExtendableResource, strExtendableResourceType))
		);
		return listRating;
	}
	/**
	 * Get the information value associate to the extenderType to export in the file export
	 * 
	 * @param strIdExtendableResource
	 * 					the idExtendableResource
	 * @param strExtendableResourceType
	 * 				the extendableResourceType 
	 * @return the information value associate to the extender rating to export in the file export
	 */
	public static String getInfoForExport( String strIdExtendableResource, String strExtendableResourceType )
	{
		StringBuilder builder= new StringBuilder( );
		 _listRatingType.forEach(type ->  
		 	{
		 		String info= type.getInfoForExport(strIdExtendableResource, strExtendableResourceType);
		 		if(StringUtils.isNoneEmpty( info )) {
		 			builder.append( type.getTitle( )).append(" ").append( info ).append(System.getProperty("line.separator"));
		 		}		
		 	}
		 );
			 
		 return builder.toString( );
				
	}
	/**
	 * Get the information value associate to the extenderType to write in the recap
	 * 
	 * @param strIdExtendableResource
	 * 					the idExtendableResource
	 * @param strExtendableResourceType
	 * 				the extendableResourceType 
	 * @return the information value associate to the extenderType to write in the recap
	 */
	public static String getInfoForRecap( String strIdExtendableResource, String strExtendableResourceType ) {
		
		StringBuilder builder= new StringBuilder( );
		 _listRatingType.forEach(type ->  
		 	{
		 		String info= type.getInfoForRecap(strIdExtendableResource, strExtendableResourceType);
		 		if(StringUtils.isNoneEmpty( info )) {		 			
		 			builder.append( type.getTitle( )).append(" ").append( info ).append(System.getProperty("line.separator"));
		 		}		
		 	}
		 );
			 
		return builder.toString( );
	}	
	/**
	 * Get an instance of rating 
	 * @param typeName the type name
	 * @return rating instance
	 */
	public static Rating getRatingInstance( String typeName  ) {
		try {
			return   _listRatingType.stream().filter( rat ->  rat.getTypeName().equals( typeName ) )
					.findAny().orElseThrow( RatingTypeException::new ).getType().newInstance( );
		
		} catch (InstantiationException | IllegalAccessException | RatingTypeException e) {
			
			AppLogService.error(e.getMessage(), e);
			throw new RatingTypeException( );
		}	  		

	}
	/**
	 * Appends the specified element to the end of this list (optional
     * operation).
	 * @param ratingType the rating type
	 * @return true if the element is added
	 */
	public static boolean addRatingType( RatingType ratingType) 
	{
		if(_ratingSecurityService == null ) {
			_ratingSecurityService=  SpringContextService.getBean( IRatingSecurityService.BEAN_SERVICE );
		}
		if(_listRatingType.stream().anyMatch(rating -> rating.getType().equals( ratingType.getType( ) ) )){
			
			return false;
		}
		return _listRatingType.add( ratingType );
	}
	/**
	 * Get the rating type 
	 * @param clazz the class of rating type 
	 * @return
	 */
	public static Optional<RatingType> getRatingType(  Class<? extends Rating> clazz ) 
	{		
		return _listRatingType.stream().filter(type -> type.getType().isAssignableFrom( clazz )).findAny();
	}
	/**
	 * Get rating type
	 * @param strType the type 
	 * @return RatingType 
	 */
	public static Optional<RatingType> getRatingType(  String strType  ) 
	{
		return _listRatingType.stream().filter(type -> type.getTypeName( ).equals( strType ) ).findAny();
	}
	/**
	 * Get all rating type
	 * @return the list of all rating type
	 */
	public static List<RatingType> getListRatingType(){
		
		return _listRatingType;
	}
}
