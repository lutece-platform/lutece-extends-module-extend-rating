package fr.paris.lutece.plugins.extend.modules.rating.util.constants;


import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.databind.node.ObjectNode;

import fr.paris.lutece.plugins.extend.modules.rating.business.SimpleRating;
import fr.paris.lutece.plugins.extend.modules.rating.business.StarRating;
import fr.paris.lutece.plugins.extend.modules.rating.business.ThumbRating;

import fr.paris.lutece.plugins.extend.util.JSONUtils;
import fr.paris.lutece.portal.service.util.AppLogService;

public class RatingUtils {
	
    public static final String TEMPLATE_SIMPLE_RATING_TYPE = "skin/plugins/extend/modules/rating/extend_rating_simple_type.html";
    public static final String TEMPLATE_STAR_RATING_TYPE =   "skin/plugins/extend/modules/rating/extend_rating_star_type.html";
    public static final String TEMPLATE_THUMB_RATING_TYPE = "skin/plugins/extend/modules/rating/extend_rating_thumb_type.html";
    public static final String TEMPLATE_DEFAULT_RATING_TYPE = "skin/plugins/extend/modules/rating/extend_rating_default_type.html";


    
    public enum RatingType { 
    	 
    	SIMPLE_RATING_TYPE ( SimpleRating.class.getName( ), TEMPLATE_SIMPLE_RATING_TYPE ), 
    	THUMB_RATING_TYPE ( ThumbRating.class.getName( ), TEMPLATE_THUMB_RATING_TYPE ),
    	STAR_RATING_TYPE ( StarRating.class.getName( ), TEMPLATE_STAR_RATING_TYPE);
    	
     	private String _strType;
     	private String _strTemplatePath;

    	RatingType(String strType, String strTemplatePath) 
    	{
    		_strTemplatePath= strTemplatePath;
    		_strType= strType;
		}

		public String getTemplatePath( )
    	{
    		return _strTemplatePath;
    	}

		public String getType( )
    	{
    		return _strType;
    	}
    	
    }
 
    
	private RatingUtils () 
	{ 
		
	}

	
	
	    /**
	     * Fetch show parameter.
	     * 
	     * @param strParameters the str parameters
	     * @return the string
	     */
	    public static String fetchShowParameter( String strParameters )
	    {
	        String strShowParameter = StringUtils.EMPTY;

	        if( strParameters!=null &&  strParameters.contains(RatingConstants.JSON_KEY_SHOW))
	        {
	        	if(strParameters.length() > 5 && strParameters.charAt(1) != '"' && strParameters.contains(":")) {
		            StringBuilder stringBuilder = new StringBuilder(strParameters);
		            stringBuilder.insert(1, '"');
		            stringBuilder.insert(stringBuilder.indexOf(":"), '"');
		            strParameters = stringBuilder.toString();
		        }
		       
		        ObjectNode jsonParameters = JSONUtils.parseParameters( strParameters );
		        
		        if ( jsonParameters != null )
		        {
		            if ( jsonParameters.has( RatingConstants.JSON_KEY_SHOW ) )
		            {
		                strShowParameter = jsonParameters.get( RatingConstants.JSON_KEY_SHOW ).asText( );
		            }
		            else 
		            {
		                AppLogService.debug( "No " + RatingConstants.JSON_KEY_SHOW + " found in " + jsonParameters );
		            }
		        }
	        }

	        return strShowParameter;
	    }
	    
	    public static String getRatingTemplatePaht( String strType) {
	    	
	    	if ( RatingType.SIMPLE_RATING_TYPE.getType( ).equals( strType ))
	    	{
	    		return RatingType.SIMPLE_RATING_TYPE.getTemplatePath( );
	    	}
	    	else if( RatingType.STAR_RATING_TYPE.getType( ).equals( strType ) )
	    	{
	    		return RatingType.STAR_RATING_TYPE.getTemplatePath( );
	    		
	    	}else if( RatingType.THUMB_RATING_TYPE.getType().equals( strType )) 
	    	{
	    		return RatingType.THUMB_RATING_TYPE.getTemplatePath( );
	    	}
	    		
	    	return TEMPLATE_DEFAULT_RATING_TYPE;
	    }
}
