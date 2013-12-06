package fr.paris.lutece.plugins.extend.modules.rating.service;

import fr.paris.lutece.plugins.extend.modules.rating.business.Rating;
import fr.paris.lutece.portal.business.resourceenhancer.IResourceDisplayManager;
import fr.paris.lutece.util.xml.XmlUtil;

import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;


/**
 * Manager for add on display
 */
public class RatingAddOnService implements IResourceDisplayManager
{
    public static final String PROPERTY_RESOURCE_TYPE = "document";
    private static final String TAG_RATING = "document-rating";
    private static final String TAG_NUMBER_RATING = "document-number-rating";

    @Inject
    @Named( RatingService.BEAN_SERVICE )
    private IRatingService _ratingService;

    @Override
    public void getXmlAddOn( StringBuffer strXml, String strResourceType, int nResourceId )
    {
        if ( PROPERTY_RESOURCE_TYPE.equals( strResourceType ) )
        {
            // Add on for document type
            Rating rating = _ratingService.findByResource( String.valueOf( nResourceId ), strResourceType );
            if ( rating != null )
            {
                XmlUtil.addElement( strXml, TAG_RATING, rating.getAverageScore( ) );
                XmlUtil.addElement( strXml, TAG_NUMBER_RATING, rating.getVoteCount( ) );
            }
        }
    }

    @Override
    public void buildPageAddOn( Map<String, Object> model, String strResourceType, int nIdResource,
            String strPortletId, HttpServletRequest request )
    {
        // TODO Auto-generated method stub
        return;
    }

}
