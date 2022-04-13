package fr.paris.lutece.plugins.extend.modules.rating.service.facade;

import static java.util.Objects.requireNonNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import fr.paris.lutece.plugins.extend.business.extender.history.ResourceExtenderHistory;
import fr.paris.lutece.plugins.extend.modules.rating.business.Rating;
import fr.paris.lutece.plugins.extend.modules.rating.business.RatingHome;
import fr.paris.lutece.plugins.extend.modules.rating.business.config.RatingExtenderConfig;
import fr.paris.lutece.plugins.extend.modules.rating.service.RatingListenerService;
import fr.paris.lutece.plugins.extend.modules.rating.service.RatingService;
import fr.paris.lutece.plugins.extend.modules.rating.service.extender.RatingResourceExtender;
import fr.paris.lutece.plugins.extend.modules.rating.service.security.IRatingSecurityService;
import fr.paris.lutece.plugins.extend.modules.rating.util.constants.RatingConstants;
import fr.paris.lutece.plugins.extend.modules.rating.util.constants.RatingUtils;
import fr.paris.lutece.plugins.extend.service.extender.history.IResourceExtenderHistoryService;
import fr.paris.lutece.plugins.extend.service.extender.history.ResourceExtenderHistoryService;
import fr.paris.lutece.portal.service.security.LuteceUser;
import fr.paris.lutece.portal.service.security.SecurityService;
import fr.paris.lutece.portal.service.security.UserNotSignedException;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.template.AppTemplateService;

public final class DefaultRatingTypeImpl implements RatingType {

   private IResourceExtenderHistoryService _resourceExtenderHistoryService;
   private IRatingSecurityService _ratingSecurityService ;

   /**
	 *  Rating extender Title
	 */
	protected String _strTitle; 
	/**
	 * Rating type (SimpleRatin, StarRating...)
	 */
	protected Class<? extends Rating> _type;
	/**
	 * Consumer to rating service 
	 */   
   protected DefaultRatingTypeImpl( Class<? extends Rating>  type, String strTitle )
	{		
		requireNonNull( type );
		requireNonNull( strTitle );
		this._type= type;
		this._strTitle= strTitle;
       	_resourceExtenderHistoryService = SpringContextService.getBean( ResourceExtenderHistoryService.BEAN_SERVICE );
    	_ratingSecurityService = SpringContextService.getBean( IRatingSecurityService.BEAN_SERVICE );

	}
	@Override
	public void doRating(Rating rating) {
		
        if ( _resourceExtenderHistoryService == null )
        {
    		_resourceExtenderHistoryService = SpringContextService.getBean( ResourceExtenderHistoryService.BEAN_SERVICE );

        }
		 ResourceExtenderHistory history = _resourceExtenderHistoryService.create( RatingResourceExtender.RESOURCE_EXTENDER,
	        		rating.getIdExtendableResource( ), rating.getExtendableResourceType( ),  rating.getUserGuid( ) );
		 rating.setHistory(history);      
	     RatingHome.create( rating );        
	     RatingListenerService.createRating( rating.getExtendableResourceType( ), rating.getIdExtendableResource( ), rating.getUser( ) );		
	}

	@Override
	public void cancelRating(Rating rating) {
		
		Optional<Rating> rat= RatingService.INSTANCE.findRating(rating.getIdExtendableResource(), rating.getExtendableResourceType(), this.getTypeName( ), rating.getRatingValue( ), rating.getUserGuid( ));
        if( rat.isPresent( ) )
        {
        	 if ( _resourceExtenderHistoryService == null )
             {
             	_resourceExtenderHistoryService = SpringContextService.getBean( ResourceExtenderHistoryService.BEAN_SERVICE );
             }
             _resourceExtenderHistoryService.remove( rat.get().getIdHistory(  )  );
             RatingHome.remove( rat.get().getIdRating( ) );
             if ( RatingListenerService.hasListener( ) )
 	           {
 	            	RatingListenerService.deleteRating( rating.getExtendableResourceType() , rating.getIdExtendableResource(),  rating.getUser( ) );
 	           }
    	  }else
    	  {
    		  throw new NoSuchElementException( "The rating with IdExtendableResource= "+rating.getIdExtendableResource()+" does not exist ");
    	  }
	}

	@Override
	public String getPageAddOn(RatingExtenderConfig ratingExtenderConfig, String strIdExtendableResource,
			String strExtendableResourceType, String strParameters, HttpServletRequest request) {
		if(ratingExtenderConfig != null )
		{
	        LuteceUser user = SecurityService.getInstance(  ).getRegisteredUser( request );
	        Map<String, Object> model = new HashMap< >(  );
	        String showParam= RatingUtils.fetchShowParameter( strParameters );
	        if( !"voteAction".equals( showParam )) 
        	{ 
        		Optional<Rating> rating= RatingService.INSTANCE.findAndbuildRatingResult(strIdExtendableResource, strExtendableResourceType, ratingExtenderConfig.getRatingType( ));;
        		if(user != null ) {        			       		
        			List<Rating> listRating= RatingService.INSTANCE.findAndbuildRatingResult(strIdExtendableResource, strExtendableResourceType, ratingExtenderConfig.getRatingType( ), user.getName());            		
        			model.put(RatingConstants.MARK_RATING_LIST, listRating);

        		}
        		model.put(RatingConstants.MARK_RATING, rating.isPresent()?rating.get( ):null );
        	}
	        model.put( RatingConstants.MARK_ID_EXTENDABLE_RESOURCE, strIdExtendableResource );
            model.put( RatingConstants.MARK_EXTENDABLE_RESOURCE_TYPE, strExtendableResourceType );
            model.put( RatingConstants.MARK_SHOW, showParam );                
	        if(_ratingSecurityService ==  null ) {
	        	_ratingSecurityService = SpringContextService.getBean( IRatingSecurityService.BEAN_SERVICE );
	        }
  
		    if( !_ratingSecurityService.isVoteClosed(ratingExtenderConfig) )
	        {
	            
	                try
	                {
	                    model.put( RatingConstants.MARK_CAN_VOTE,
	                        _ratingSecurityService.canVote( ratingExtenderConfig, user, strIdExtendableResource, strExtendableResourceType ) );
	                }
	                catch ( UserNotSignedException e )
	                {
	                    // In case of user not signed, he can vote but will be redirected to login page
	                    model.put( RatingConstants.MARK_CAN_VOTE, true );
	                }
	            
	                model.put( RatingConstants.MARK_CAN_DELETE_VOTE,
	                    _ratingSecurityService.canDeleteVote( ratingExtenderConfig, user, strIdExtendableResource, strExtendableResourceType ) );
	                
	                model.put( RatingConstants.MARK_VOTE_CLOSED, false );
	         }
             else
             {
                	model.put( RatingConstants.MARK_VOTE_CLOSED, true );
             }
               
             		return AppTemplateService.getTemplate( RatingUtils.getRatingTemplatePaht( ratingExtenderConfig.getRatingType( )), request.getLocale(  ), model ).getHtml( );
              }     
	        
		return StringUtils.EMPTY ;
	}

	@Override
	public Class<? extends Rating> getType() {
		
		return this._type;
	}

	@Override
	public String getTypeName() {
		
		return this._type.getName( );
	}

	@Override
	public String getTitle() {
		
		return this._strTitle;
	}

	@Override
	public String getInfoForExport(String strIdExtendableResource, String strExtendableResourceType) {
		
		Optional<Rating> rating= RatingService.INSTANCE.findAndbuildRatingResult(strIdExtendableResource, strExtendableResourceType, this.getTypeName());
		if( rating.isPresent( ) )
			return String.valueOf( rating.get().getScoreValue( ) );
		return StringUtils.EMPTY;
	}

	@Override
	public String getInfoForRecap(String strIdExtendableResource, String strExtendableResourceType) {
		
		return getInfoForExport( strIdExtendableResource,  strExtendableResourceType);
	}

	@Override
	public List<Rating> getInfoExtender(String strIdExtendableResource, String strExtendableResourceType) {
		
		return RatingService.INSTANCE.findAndbuildRatingResult(strIdExtendableResource, strExtendableResourceType, this.getTypeName( )).map(Collections::singletonList).orElseGet(Collections::emptyList);
	}

	@Override
	public List<Rating> getInfoExtenderByList(List<String> listIdExtendableResource,
			String strExtendableResourceType) {
		
		return RatingService.INSTANCE.findAndbuildRatingResult(listIdExtendableResource, strExtendableResourceType, this.getTypeName( ));
	}
}
