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

import fr.paris.lutece.plugins.extend.business.extender.ResourceExtenderDTO;
import fr.paris.lutece.plugins.extend.business.extender.config.IExtenderConfig;
import fr.paris.lutece.plugins.extend.modules.rating.business.Rating;
import fr.paris.lutece.plugins.extend.modules.rating.business.config.RatingExtenderConfig;
import fr.paris.lutece.plugins.extend.modules.rating.business.type.VoteType;
import fr.paris.lutece.plugins.extend.modules.rating.service.IRatingService;
import fr.paris.lutece.plugins.extend.modules.rating.service.extender.RatingResourceExtender;
import fr.paris.lutece.plugins.extend.modules.rating.service.security.IRatingSecurityService;
import fr.paris.lutece.plugins.extend.modules.rating.service.type.IVoteTypeService;
import fr.paris.lutece.plugins.extend.modules.rating.util.constants.RatingConstants;
import fr.paris.lutece.plugins.extend.service.extender.config.IResourceExtenderConfigService;
import fr.paris.lutece.plugins.extend.util.ExtendErrorException;
import fr.paris.lutece.plugins.extend.util.JSONUtils;
import fr.paris.lutece.plugins.extend.web.component.AbstractResourceExtenderComponent;
import fr.paris.lutece.portal.service.admin.AdminUserService;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.mailinglist.AdminMailingListService;
import fr.paris.lutece.portal.service.security.UserNotSignedException;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.date.DateUtil;
import fr.paris.lutece.util.html.HtmlTemplate;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
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
    @Inject
    private IRatingService _ratingService;
    @Inject
    @Named( RatingConstants.BEAN_CONFIG_SERVICE )
    private IResourceExtenderConfigService _configService;
    @Inject
    private IVoteTypeService _voteTypeService;
    @Inject
    private IRatingSecurityService _ratingSecurityService;

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

        if ( config != null )
        {
            VoteType voteType = _voteTypeService.findByPrimaryKey( config.getIdVoteType(  ), true );

            if ( voteType != null )
            {
                Rating rating = _ratingService.findByResource( strIdExtendableResource, strExtendableResourceType );
                Map<String, Object> model = new HashMap<String, Object>(  );
                model.put( RatingConstants.MARK_RATING, rating );
                model.put( RatingConstants.MARK_ID_EXTENDABLE_RESOURCE, strIdExtendableResource );
                model.put( RatingConstants.MARK_EXTENDABLE_RESOURCE_TYPE, strExtendableResourceType );
                model.put( RatingConstants.MARK_SHOW, fetchShowParameter( strParameters ) );

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
                
                if( !_ratingSecurityService.isVoteClosed(config))
                {
                
	                model.put( RatingConstants.MARK_CAN_DELETE_VOTE,
	                    _ratingSecurityService.canDeleteVote( request, strIdExtendableResource, strExtendableResourceType ) );
	                model.put( RatingConstants.MARK_RATING_HTML_CONTENT,
	                    AppTemplateService.getTemplateFromStringFtl( voteType.getTemplateContent(  ),
	                        request.getLocale(  ), model ).getHtml(  ) );
	                model.put( RatingConstants.MARK_VOTE_CLOSED, false );
                }
                else
                {
                	model.put( RatingConstants.MARK_VOTE_CLOSED, true );
                }

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
        JSONObject jsonParameters = JSONUtils.parseParameters( strParameters );

        if ( jsonParameters != null )
        {
            try
            {
                strShowParameter = jsonParameters.getString( RatingConstants.JSON_KEY_SHOW );
            }
            catch ( JSONException je )
            {
                AppLogService.debug( je.getMessage(  ), je );
            }
        }

        return strShowParameter;
    }
}
