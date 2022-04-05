package fr.paris.lutece.plugins.extend.modules.rating.service.validator;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import fr.paris.lutece.plugins.extend.modules.rating.business.config.RatingExtenderConfig;
import fr.paris.lutece.plugins.extend.modules.rating.service.extender.RatingResourceExtender;
import fr.paris.lutece.plugins.extend.modules.rating.util.constants.RatingConstants;
import fr.paris.lutece.plugins.extend.modules.rating.util.constants.RatingUtils.RatingType;
import fr.paris.lutece.plugins.extend.service.extender.config.IResourceExtenderConfigService;
import fr.paris.lutece.portal.service.security.LuteceUser;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;

public class RatingValidationService implements IRatingValidationService {

  private static final String PROPERTIES_ERROR_URL="rating.errorUrl";
  @Inject
  @Named( RatingConstants.BEAN_CONFIG_SERVICE )
  private IResourceExtenderConfigService _configService;
  
  
  

  @Override
  public String validateRating(HttpServletRequest request, LuteceUser user, String strIdResource,
			String strResourceTypeKey, float fRatingValue) {
		
			
        RatingExtenderConfig config = _configService.find( RatingResourceExtender.RESOURCE_EXTENDER,
        		strIdResource, strResourceTypeKey );
       if( config.getRatingType().equals( RatingType.SIMPLE_RATING_TYPE.getType( ) ))
       {
    	   if (fRatingValue != 1 ) 
    	   {
    		   
    		   AppLogService.error("The value {} for type SIMPLE_RATING_TYPE of evaluation is not valid", fRatingValue, new RuntimeException(" SIMPLE_RATING_TYPE Exception "));
    		   return getErrorUrl( request );
    	   }
    			   
       }else if( config.getRatingType( ).equals( RatingType.STAR_RATING_TYPE.getType( )))
       {
    	   if (fRatingValue <= 0 || fRatingValue > 4 || fRatingValue % 0.5 != 0 ) 
    	   {
    		   AppLogService.error("The value {} for type STAR_RATING_TYPE of evaluation is not valid", fRatingValue, new RuntimeException(" STAR_RATING_TYPE Exception "));
    		   return getErrorUrl( request );
    	   }
    	   
       }else if( config.getRatingType( ).equals(RatingType.THUMB_RATING_TYPE.getType( )))
       {
    	   if (fRatingValue != 1 && fRatingValue != -1) 
    	   {
    		   AppLogService.error("The value {} for type THUMB_RATING_TYPE of evaluation is not valid", fRatingValue, new RuntimeException(" THUMB_RATING_TYPE Exception "));

    		   return getErrorUrl( request );
    	   }
       }
        
        return null;
	}
  
  private String getErrorUrl( HttpServletRequest request ) {
	  
	  String strDefaultErrorUrl = AppPathService.getBaseUrl( request ) + AppPropertiesService.getProperty( PROPERTIES_ERROR_URL );
	  if ( StringUtils.isEmpty( strDefaultErrorUrl ) )
	  {
	      // Just make sure it's not empty because empty means no errors !
	      strDefaultErrorUrl = "/";
	  }
	  
	  return strDefaultErrorUrl;
  }

}
