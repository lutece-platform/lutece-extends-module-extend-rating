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
package fr.paris.lutece.plugins.extend.modules.rating.web;

import fr.paris.lutece.plugins.extend.modules.rating.business.Rating;
import fr.paris.lutece.plugins.extend.modules.rating.business.config.RatingExtenderConfig;
import fr.paris.lutece.plugins.extend.modules.rating.service.extender.RatingResourceExtender;
import fr.paris.lutece.plugins.extend.modules.rating.service.facade.RatingFacadeFactory;
import fr.paris.lutece.plugins.extend.modules.rating.service.security.RatingException;
import fr.paris.lutece.plugins.extend.modules.rating.service.validator.RatingValidationManagementService;
import fr.paris.lutece.plugins.extend.modules.rating.util.constants.RatingConstants;
import fr.paris.lutece.plugins.extend.service.ExtendPlugin;
import fr.paris.lutece.plugins.extend.service.extender.IResourceExtenderService;
import fr.paris.lutece.plugins.extend.service.extender.config.IResourceExtenderConfigService;
import fr.paris.lutece.portal.business.mailinglist.Recipient;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.mail.MailService;
import fr.paris.lutece.portal.service.mailinglist.AdminMailingListService;
import fr.paris.lutece.portal.service.message.SiteMessage;
import fr.paris.lutece.portal.service.message.SiteMessageException;
import fr.paris.lutece.portal.service.message.SiteMessageService;
import fr.paris.lutece.portal.service.portal.PortalService;
import fr.paris.lutece.portal.service.security.LuteceUser;
import fr.paris.lutece.portal.service.security.SecurityService;
import fr.paris.lutece.portal.service.security.UserNotSignedException;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.util.mvc.commons.annotations.Action;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.portal.util.mvc.xpage.MVCApplication;
import fr.paris.lutece.portal.util.mvc.xpage.annotations.Controller;
import fr.paris.lutece.portal.web.xpages.XPage;
import fr.paris.lutece.util.bean.BeanUtil;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.url.UrlItem;

import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletRequest;

/**
 * Front office application receiving the votes and the vote cancellations.
 */
@RequestScoped
@Named( RatingApp.BEAN_NAME )
@Controller( xpageName = RatingApp.XPAGE_NAME, securityTokenEnabled = true )
public class RatingApp extends MVCApplication
{
    public static final String XPAGE_NAME = "extend-rating";
    public static final String BEAN_NAME = "extend-rating.xpage." + XPAGE_NAME;

    private static final long serialVersionUID = 1L;

    private static final String VIEW_HOME = "home";
    private static final String TEMPLATE_RATING_NOTIFY_MESSAGE = "skin/plugins/extend/modules/rating/rating_notify_message.html";
    private static final String CONSTANT_HTTP = "http";

    @Inject
    @Named( "extend-rating.ratingExtenderConfigService" )
    private IResourceExtenderConfigService _configService;

    @Inject
    private IResourceExtenderService _resourceExtenderService;

    /**
     * Redirects to the portal home: this application has no page of its own.
     *
     * @param request The HTTP request
     * @return the redirection
     */
    @View( value = VIEW_HOME, defaultView = true )
    public XPage getHome( HttpServletRequest request )
    {
        return redirect( request, getPortalHomeUrl( request ) );
    }

    /**
     * Records a vote, then redirects to the page of the resource.
     *
     * @param request The HTTP request
     * @return the redirection
     * @throws SiteMessageException if the vote is refused
     * @throws UserNotSignedException if the vote requires a signed-in user
     */
    @Action( RatingConstants.ACTION_DO_RATING )
    public XPage doRating( HttpServletRequest request ) throws SiteMessageException, UserNotSignedException
    {
        RatingExtenderConfig config = findConfig( request );
        Rating rating = RatingFacadeFactory.getRatingInstance( config.getRatingType( ) );
        LuteceUser user = SecurityService.getInstance( ).getRegisteredUser( request );
        BeanUtil.populate( rating, request, request.getLocale( ) );
        rating.setUser( user );
        rating.setUserGuid( user != null ? user.getName( ) : null );

        String strIdExtendableResource = rating.getIdExtendableResource( );
        String strExtendableResourceType = rating.getExtendableResourceType( );
        float fRatingValue = rating.getRatingValue( );

        if ( StringUtils.isBlank( strIdExtendableResource ) || StringUtils.isBlank( strExtendableResourceType ) )
        {
            SiteMessageService.setMessage( request, RatingConstants.MESSAGE_ERROR_NOT_RATABLE, SiteMessage.TYPE_STOP );
        }

        String strSessionKeyNextUrl = getSessionKeyUrlRedirect( strIdExtendableResource, strExtendableResourceType );
        String strNextUrl = (String) request.getSession( ).getAttribute( strSessionKeyNextUrl );

        if ( StringUtils.isEmpty( strNextUrl ) )
        {
            strNextUrl = getRefererUrl( request );
        }
        else
        {
            request.getSession( ).removeAttribute( strSessionKeyNextUrl );
        }

        String strErrorUrl = RatingValidationManagementService.validateRating( request, user, strIdExtendableResource, strExtendableResourceType,
                fRatingValue );

        if ( StringUtils.isNotEmpty( strErrorUrl ) )
        {
            if ( !strErrorUrl.startsWith( CONSTANT_HTTP ) )
            {
                strErrorUrl = AppPathService.getBaseUrl( request ) + strErrorUrl;
            }

            request.getSession( ).setAttribute( strSessionKeyNextUrl, strNextUrl );

            return redirect( request, strErrorUrl );
        }

        try
        {
            RatingFacadeFactory.doRating( config, rating );
        }
        catch( UserNotSignedException e )
        {
            request.getSession( ).setAttribute( strSessionKeyNextUrl, strNextUrl );

            throw e;
        }
        catch( RatingException e )
        {
            SiteMessageService.setCustomMessage( request, e.getMessage( ), SiteMessage.TYPE_STOP );
        }

        sendNotification( request, config, strIdExtendableResource, strExtendableResourceType, fRatingValue );

        return redirect( request, strNextUrl );
    }

    /**
     * Cancels a vote of the signed-in user, then redirects to the page of the resource.
     *
     * @param request The HTTP request
     * @return the redirection
     * @throws SiteMessageException if the cancellation is refused
     * @throws UserNotSignedException if no user is signed in
     */
    @Action( RatingConstants.ACTION_CANCEL_RATING )
    public XPage doCancelRating( HttpServletRequest request ) throws SiteMessageException, UserNotSignedException
    {
        LuteceUser user = SecurityService.getInstance( ).getRegisteredUser( request );

        if ( user == null )
        {
            throw new UserNotSignedException( );
        }

        RatingExtenderConfig config = findConfig( request );
        Rating rating = RatingFacadeFactory.getRatingInstance( config.getRatingType( ) );
        BeanUtil.populate( rating, request, request.getLocale( ) );
        rating.setUser( user );
        rating.setUserGuid( user.getName( ) );

        if ( StringUtils.isBlank( rating.getIdExtendableResource( ) ) || StringUtils.isBlank( rating.getExtendableResourceType( ) ) )
        {
            SiteMessageService.setMessage( request, RatingConstants.MESSAGE_ERROR_NOT_RATABLE, SiteMessage.TYPE_STOP );
        }

        try
        {
            RatingFacadeFactory.cancelRating( config, rating );
        }
        catch( RatingException e )
        {
            SiteMessageService.setCustomMessage( request, e.getMessage( ), SiteMessage.TYPE_STOP );
        }

        return redirect( request, getRefererUrl( request ) );
    }

    /**
     * Finds the rating configuration of the resource the request names.
     *
     * @param request The HTTP request
     * @return the configuration
     * @throws SiteMessageException if the resource has no rating configuration
     */
    private RatingExtenderConfig findConfig( HttpServletRequest request ) throws SiteMessageException
    {
        RatingExtenderConfig config = _configService.find( RatingResourceExtender.RESOURCE_EXTENDER,
                request.getParameter( RatingConstants.PARAMETER_ID_EXTENDABLE_RESOURCE ),
                request.getParameter( RatingConstants.PARAMETER_EXTENDABLE_RESOURCE_TYPE ) );

        if ( config == null || config.getRatingType( ) == null )
        {
            SiteMessageService.setMessage( request, RatingConstants.MESSAGE_ERROR_NOT_RATABLE, SiteMessage.TYPE_STOP );
        }

        return config;
    }

    /**
     * Gives the page the request comes from, carrying the origin URL kept in session, or the portal home.
     *
     * @param request The HTTP request
     * @return the URL to go back to
     */
    private String getRefererUrl( HttpServletRequest request )
    {
        String strReferer = request.getHeader( RatingConstants.PARAMETER_HTTP_REFERER );

        if ( strReferer == null )
        {
            return getPortalHomeUrl( request );
        }

        UrlItem url = new UrlItem( strReferer );
        String strFromUrl = (String) request.getSession( ).getAttribute( ExtendPlugin.PLUGIN_NAME + RatingConstants.PARAMETER_FROM_URL );

        if ( StringUtils.isNotEmpty( strFromUrl ) && !url.getUrl( ).contains( RatingConstants.PARAMETER_FROM_URL ) )
        {
            url.addParameter( RatingConstants.PARAMETER_FROM_URL, strFromUrl.replace( "%", "%25" ) );
        }

        return url.getUrl( );
    }

    /**
     * Gives the absolute URL of the portal home.
     *
     * @param request The HTTP request
     * @return the URL
     */
    private static String getPortalHomeUrl( HttpServletRequest request )
    {
        return AppPathService.getBaseUrl( request ) + AppPathService.getPortalUrl( );
    }

    /**
     * Sends the vote notification to the mailing list of the configuration.
     *
     * @param request The HTTP request
     * @param config The rating configuration
     * @param strIdExtendableResource the id of the resource
     * @param strExtendableResourceType the type of the resource
     * @param fRatingValue the value of the vote
     */
    private void sendNotification( HttpServletRequest request, RatingExtenderConfig config, String strIdExtendableResource,
            String strExtendableResourceType, float fRatingValue )
    {
        Collection<Recipient> listRecipients = AdminMailingListService.getRecipients( config.getIdMailingList( ) );
        String strSenderName = PortalService.getSiteName( );
        String strSenderEmail = MailService.getNoReplyEmail( );

        for ( Recipient recipient : listRecipients )
        {
            Map<String, Object> model = new HashMap<>( );
            String strResourceName = _resourceExtenderService.getExtendableResourceName( strIdExtendableResource, strExtendableResourceType );

            Object [ ] params = {
                    strResourceName
            };
            String strSubject = I18nService.getLocalizedString( RatingConstants.MESSAGE_NOTIFY_SUBJECT, params, request.getLocale( ) );

            model.put( RatingConstants.MARK_RESOURCE_EXTENDER_NAME, strResourceName );
            model.put( RatingConstants.MARK_VOTE_VALUE, fRatingValue );

            HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_RATING_NOTIFY_MESSAGE, request.getLocale( ), model );

            MailService.sendMailHtml( recipient.getEmail( ), strSenderName, strSenderEmail, strSubject, template.getHtml( ) );
        }
    }

    /**
     * Get the session key of the URL to redirect the user to after he has voted for the resource
     *
     * @param strIdResource
     *            The id of the resource
     * @param strResourceType
     *            The type of the resource
     * @return The session key
     */
    public static String getSessionKeyUrlRedirect( String strIdResource, String strResourceType )
    {
        return ExtendPlugin.PLUGIN_NAME + RatingConstants.PARAMETER_FROM_URL + strResourceType + strIdResource;
    }
}
