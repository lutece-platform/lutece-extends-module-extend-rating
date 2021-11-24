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
package fr.paris.lutece.plugins.extend.modules.rating.web.component;

import com.fasterxml.jackson.databind.node.ObjectNode;
import fr.paris.lutece.plugins.extend.business.extender.ResourceExtenderDTO;
import fr.paris.lutece.plugins.extend.business.extender.config.IExtenderConfig;
import fr.paris.lutece.plugins.extend.business.extender.history.ResourceExtenderHistory;
import fr.paris.lutece.plugins.extend.business.extender.history.ResourceExtenderHistoryFilter;
import fr.paris.lutece.plugins.extend.modules.rating.business.Rating;
import fr.paris.lutece.plugins.extend.modules.rating.business.RatingHistory;
import fr.paris.lutece.plugins.extend.modules.rating.business.config.RatingExtenderConfig;
import fr.paris.lutece.plugins.extend.modules.rating.business.type.VoteType;
import fr.paris.lutece.plugins.extend.modules.rating.service.IRatingHistoryService;
import fr.paris.lutece.plugins.extend.modules.rating.service.IRatingService;
import fr.paris.lutece.plugins.extend.modules.rating.service.extender.RatingResourceExtender;
import fr.paris.lutece.plugins.extend.modules.rating.service.security.IRatingSecurityService;
import fr.paris.lutece.plugins.extend.modules.rating.service.type.IVoteTypeService;
import fr.paris.lutece.plugins.extend.modules.rating.util.constants.RatingConstants;
import fr.paris.lutece.plugins.extend.service.extender.config.IResourceExtenderConfigService;
import fr.paris.lutece.plugins.extend.service.extender.history.IResourceExtenderHistoryService;
import fr.paris.lutece.plugins.extend.util.ExtendErrorException;
import fr.paris.lutece.plugins.extend.util.JSONUtils;
import fr.paris.lutece.plugins.extend.web.component.AbstractResourceExtenderComponent;
import fr.paris.lutece.portal.service.admin.AdminUserService;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.mailinglist.AdminMailingListService;
import fr.paris.lutece.portal.service.security.LuteceUser;
import fr.paris.lutece.portal.service.security.SecurityService;
import fr.paris.lutece.portal.service.security.UserNotSignedException;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.date.DateUtil;
import fr.paris.lutece.util.html.HtmlTemplate;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;


/**
 *
 * RatingResourceExtenderComponent
 *
 */
public class RatingResourceExtenderComponent extends AbstractResourceExtenderComponent
{
    // TEMPLATES
    private static final String TEMPLATE_RATING = "skin/plugins/extend/modules/rating/rating.html";
    private static final String TEMPLATE_RATING_CONFIG = "admin/plugins/extend/modules/rating/rating_config.html";
    private static final String TEMPLATE_RATING_INFO = "admin/plugins/extend/modules/rating/rating_info.html";
    private static final String MARK_LOCALE = "locale";
	// CONSTANTS
    private static final String ORDER_BY_DATE_CREATION = " date_creation ";
    
    @Inject
    private IRatingService _ratingService;
    @Inject
    @Named( RatingConstants.BEAN_CONFIG_SERVICE )
    private IResourceExtenderConfigService _configService;
    @Inject
    private IVoteTypeService _voteTypeService;
    @Inject
    private IRatingSecurityService _ratingSecurityService;
    @Inject 
    private IResourceExtenderHistoryService _resourceExtenderHistoryService ;
    @Inject 
    private IRatingHistoryService _ratingHistoryService ;
    /**
     * {@inheritDoc}
     */
    @Override
    public void buildXmlAddOn( String strIdExtendableResource, String strExtendableResourceType, String strParameters,
        StringBuffer strXml )
    {
        // Nothing yet
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPageAddOn( String strIdExtendableResource, String strExtendableResourceType, String strParameters,
        HttpServletRequest request )
    {
        RatingExtenderConfig config = _configService.find( RatingResourceExtender.RESOURCE_EXTENDER,
                strIdExtendableResource, strExtendableResourceType );
        LuteceUser user = SecurityService.getInstance(  ).getRegisteredUser( request );
       

        if ( config != null )
        {
            VoteType voteType = _voteTypeService.findByPrimaryKey( config.getIdVoteType(  ), true );

            if ( voteType != null )
            {
                Rating rating = _ratingService.findByResource( strIdExtendableResource, strExtendableResourceType );
                double dVoteValue = 0 ;

                if ( user != null )
                {
                 ResourceExtenderHistoryFilter filter = new ResourceExtenderHistoryFilter(  );
                 
                 filter.setExtenderType( RatingResourceExtender.RESOURCE_EXTENDER );
                 filter.setExtendableResourceType( strExtendableResourceType );
                 filter.setIdExtendableResource( strIdExtendableResource );
                 filter.setUserGuid( user.getName(  ) );
                 filter.setAscSort( false );
                 filter.setSortedAttributeName( ORDER_BY_DATE_CREATION );

                 List<ResourceExtenderHistory> listHistories = _resourceExtenderHistoryService.findByFilter( filter );
                 
                 if ( CollectionUtils.isNotEmpty( listHistories ) && listHistories.get( 0 ) != null )
                 {
                	 long lHistoryExtenderId = listHistories.get( 0 ).getIdHistory( ) ;
                	 RatingHistory ratingHistory = _ratingHistoryService.findByHistoryExtenderId( lHistoryExtenderId ) ;
                	 
                	 if( ratingHistory != null )
                	 {
                		 dVoteValue = ratingHistory.getVoteValue( ) ;
                	 }
                 }
                }
                
                Map<String, Object> model = new HashMap<String, Object>(  );
                model.put( RatingConstants.MARK_RATING, rating );
                model.put( RatingConstants.MARK_ID_EXTENDABLE_RESOURCE, strIdExtendableResource );
                model.put( RatingConstants.MARK_EXTENDABLE_RESOURCE_TYPE, strExtendableResourceType );
                model.put( RatingConstants.MARK_SHOW, fetchShowParameter( strParameters ) );
                model.put( RatingConstants.MARK_VOTE_VALUE, dVoteValue ) ;

                if( !_ratingSecurityService.isVoteClosed(config))
                {
                
	                try
	                {
	                    model.put( RatingConstants.MARK_CAN_VOTE,
	                        _ratingSecurityService.canVote( request, strIdExtendableResource, strExtendableResourceType ) );
	                }
	                catch ( UserNotSignedException e )
	                {
	                    // In case of user not signed, he can vote but will be redirected to login page
	                    model.put( RatingConstants.MARK_CAN_VOTE, true );
	                }
                
	                model.put( RatingConstants.MARK_CAN_DELETE_VOTE,
	                    _ratingSecurityService.canDeleteVote( request, strIdExtendableResource, strExtendableResourceType ) );
	                
	                model.put( RatingConstants.MARK_VOTE_CLOSED, false );
                }
                else
                {
                	model.put( RatingConstants.MARK_VOTE_CLOSED, true );
                }
	                
	             
                model.put( RatingConstants.MARK_RATING_HTML_CONTENT,
		                    AppTemplateService.getTemplateFromStringFtl( voteType.getTemplateContent(  ),
		                        request.getLocale(  ), model ).getHtml(  ) ); 
                
                HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_RATING, request.getLocale(  ), model );

                return template.getHtml(  );
            }
        }

        return StringUtils.EMPTY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getConfigHtml( ResourceExtenderDTO resourceExtender, Locale locale, HttpServletRequest request )
    {
        ReferenceList listIdsMailingList = new ReferenceList(  );
        listIdsMailingList.addItem( -1,
            I18nService.getLocalizedString( RatingConstants.PROPERTY_RATING_CONFIG_LABEL_NO_MAILING_LIST, locale ) );
        listIdsMailingList.addAll( AdminMailingListService.getMailingLists( AdminUserService.getAdminUser( request ) ) );

        Map<String, Object> model = new HashMap<String, Object>(  );
        model.put( RatingConstants.MARK_RATING_CONFIG, _configService.find( resourceExtender.getIdExtender(  ) ) );
        model.put( RatingConstants.MARK_LIST_IDS_MAILING_LIST, listIdsMailingList );
        model.put( RatingConstants.MARK_LIST_IDS_VOTE_TYPE, _voteTypeService.findAll(  ) );
        model.put( MARK_LOCALE, request.getLocale(  ) );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_RATING_CONFIG, request.getLocale(  ), model );

        return template.getHtml(  );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IExtenderConfig getConfig( int nIdExtender )
    {
        return _configService.find( nIdExtender );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getInfoHtml( ResourceExtenderDTO resourceExtender, Locale locale, HttpServletRequest request )
    {
        if ( resourceExtender != null )
        {
            Map<String, Object> model = new HashMap<String, Object>(  );
            model.put( RatingConstants.MARK_RATING,
                _ratingService.findByResource( resourceExtender.getIdExtendableResource(  ),
                    resourceExtender.getExtendableResourceType(  ) ) );

            HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_RATING_INFO, request.getLocale(  ), model );

            return template.getHtml(  );
        }

        return StringUtils.EMPTY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void doSaveConfig( HttpServletRequest request, IExtenderConfig config )
        throws ExtendErrorException
    {
        if ( request.getParameter( "limitedConnectedUser" ) == null )
        {
            ( (RatingExtenderConfig) config ).setLimitedConnectedUser( false );
            ( (RatingExtenderConfig) config ).setDeleteVote( false );
        }

        if ( request.getParameter( "deleteVote" ) == null )
        {
            ( (RatingExtenderConfig) config ).setDeleteVote( false );
        }

        if ( StringUtils.isNotBlank( request.getParameter( "date_start" ) ) )
        {
            ( (RatingExtenderConfig) config ).setDateStart( DateUtil.formatTimestamp( request.getParameter( 
                        "date_start" ), request.getLocale(  ) ) );
        }
        else
        {
            ( (RatingExtenderConfig) config ).setDateStart( null );
        }

        if ( StringUtils.isNotBlank( request.getParameter( "date_end" ) ) )
        {
            ( (RatingExtenderConfig) config ).setDateEnd( DateUtil.formatTimestamp( request.getParameter( "date_end" ),
                    request.getLocale(  ) ) );
        }
        else
        {
            ( (RatingExtenderConfig) config ).setDateEnd( null );
        }

        _configService.update( config );
    }

    /**
     * Fetch show parameter.
     *
     * @param strParameters the str parameters
     * @return the string
     */
    private String fetchShowParameter( String strParameters )
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
}
