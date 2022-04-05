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
import fr.paris.lutece.plugins.extend.service.extender.ResourceExtenderService;
import fr.paris.lutece.plugins.extend.service.extender.config.IResourceExtenderConfigService;
import fr.paris.lutece.portal.business.mailinglist.Recipient;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.mail.MailService;
import fr.paris.lutece.portal.service.mailinglist.AdminMailingListService;
import fr.paris.lutece.portal.service.message.SiteMessage;
import fr.paris.lutece.portal.service.message.SiteMessageException;
import fr.paris.lutece.portal.service.message.SiteMessageService;
import fr.paris.lutece.portal.service.security.LuteceUser;
import fr.paris.lutece.portal.service.security.SecurityService;
import fr.paris.lutece.portal.service.security.UserNotSignedException;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.bean.BeanUtil;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.url.UrlItem;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * RatingJspBean
 */
public class RatingJspBean
{
    public static final String URL_JSP_DO_VOTE = "jsp/site/plugins/extend/modules/rating/DoVote.jsp";

    // TEMPLATES
    private static final String TEMPLATE_RATING_NOTIFY_MESSAGE = "skin/plugins/extend/modules/rating/rating_notify_message.html";
    private static final String CONSTANT_HTTP = "http";

    // SERVICES
    private IResourceExtenderConfigService _configService = SpringContextService.getBean( RatingConstants.BEAN_CONFIG_SERVICE );
    private IResourceExtenderService _resourceExtenderService = SpringContextService.getBean( ResourceExtenderService.BEAN_SERVICE );

    /**
     * Update the rating value an count.
     * This method is called in FO by the following JSP :
     * <strong>jsp/site/plugins/extend/modules/rating/DoVote.Jsp</strong>
     *
     * @param request The HTTP request
     * @param response The HTTP response
     * @throws IOException the io exception
     * @throws SiteMessageException the site message exception
     * @throws UserNotSignedException If the user has not signed in
     */
    public void doRating( HttpServletRequest request, HttpServletResponse response )
        throws IOException, SiteMessageException, UserNotSignedException
    {       
    	   RatingExtenderConfig config = _configService.find( RatingResourceExtender.RESOURCE_EXTENDER,
    			   request.getParameter( RatingConstants.PARAMETER_ID_EXTENDABLE_RESOURCE ), request.getParameter( RatingConstants.PARAMETER_EXTENDABLE_RESOURCE_TYPE ) );
    	   
    	if( config == null || config.getRatingType( )== null ) {
              SiteMessageService.setMessage( request, RatingConstants.MESSAGE_ERROR_GENERIC_MESSAGE, SiteMessage.TYPE_STOP );
    	}
       
        Rating rating =RatingFacadeFactory.getRatingInstance( config.getRatingType( ));
        LuteceUser user= SecurityService.getInstance(  ).getRegisteredUser( request );
        BeanUtil.populate( rating, request, request.getLocale( ) );
        rating.setUser( user );
        rating.setUserGuid( user != null ? user.getName(): null );
        
        String strIdExtendableResource = rating.getIdExtendableResource( );
        String strExtendableResourceType = rating.getExtendableResourceType( );
        float fRatinValue = rating.getRatingValue( );

        
        String strFromUrl = (String) request.getSession(  )
                                            .getAttribute( ExtendPlugin.PLUGIN_NAME +
                RatingConstants.PARAMETER_FROM_URL );

        if ( StringUtils.isBlank( strIdExtendableResource ) || StringUtils.isBlank( strExtendableResourceType )   )
        {
            SiteMessageService.setMessage( request, RatingConstants.MESSAGE_ERROR_GENERIC_MESSAGE, SiteMessage.TYPE_STOP );
        }

        String strSessionKeyNextUrl = getSessionKeyUrlRedirect( strIdExtendableResource, strExtendableResourceType );

        String strNextUrl = (String) request.getSession(  ).getAttribute( strSessionKeyNextUrl );

        if ( StringUtils.isEmpty( strNextUrl ) )
        {
            strNextUrl = request.getHeader( RatingConstants.PARAMETER_HTTP_REFERER );

            if ( strNextUrl != null )
            {
                UrlItem url = new UrlItem( strNextUrl );

                if ( StringUtils.isNotEmpty( strFromUrl ) )
                {
                    strFromUrl = strFromUrl.replaceAll( "%", "%25" );
                    if ( !url.getUrl().contains( RatingConstants.PARAMETER_FROM_URL ) )
                    {
                        url.addParameter( RatingConstants.PARAMETER_FROM_URL, strFromUrl );
                    }
                }

                strNextUrl = url.getUrl(  );
            }
            else
            {
                strNextUrl = AppPathService.getPortalUrl(  );
            }
        }
        else
        {
            request.getSession(  ).removeAttribute( strSessionKeyNextUrl );
        }
        String strErrorUrl = RatingValidationManagementService.validateRating( request,
                SecurityService.getInstance(  ).getRegisteredUser( request ), strIdExtendableResource,
                strExtendableResourceType, fRatinValue );

        if ( StringUtils.isNotEmpty( strErrorUrl ) )
        {
            if ( !strErrorUrl.startsWith( CONSTANT_HTTP ) )
            {
                strErrorUrl = AppPathService.getBaseUrl( request ) + strErrorUrl;
            }

            request.getSession(  ).setAttribute( strSessionKeyNextUrl, strNextUrl );

            response.sendRedirect( strErrorUrl );

            return;
        }

        try {
        	
        	RatingFacadeFactory.doRating(config, rating );
		
        } catch (UserNotSignedException e) {
			request.getSession(  )
            .setAttribute( ExtendPlugin.PLUGIN_NAME + RatingConstants.PARAMETER_FROM_URL +
            		strExtendableResourceType + strIdExtendableResource, strNextUrl );

			throw e;
		} catch (RatingException e) {
			
			SiteMessageService.setCustomMessage( request, e.getMessage( ), SiteMessage.TYPE_STOP );
		}

        sendNotification( request, strIdExtendableResource, strExtendableResourceType, fRatinValue );
        response.sendRedirect( strNextUrl );
    }

    /**
     * Cancel the rating value
     * This method is called in FO by the following JSP :
     * <strong>jsp/site/plugins/extend/modules/rating/DoCancelVote.Jsp</strong>
     * @param request The HTTP request
     * @param response The HTTP response
     * @throws IOException the io exception
     * @throws SiteMessageException the site message exception
     * @throws UserNotSignedException 
     */
    public void doCancelRating( HttpServletRequest request, HttpServletResponse response )
        throws IOException, SiteMessageException, UserNotSignedException
    {
           String strFromUrl = (String) request.getSession(  )
                                            .getAttribute( ExtendPlugin.PLUGIN_NAME +
                RatingConstants.PARAMETER_FROM_URL );
        LuteceUser user= SecurityService.getInstance(  ).getRegisteredUser( request );
        if( user == null ) {
        	
        	 throw new UserNotSignedException( );
        }
        RatingExtenderConfig config = _configService.find( RatingResourceExtender.RESOURCE_EXTENDER,
 			   request.getParameter( RatingConstants.PARAMETER_ID_EXTENDABLE_RESOURCE ), request.getParameter( RatingConstants.PARAMETER_EXTENDABLE_RESOURCE_TYPE ) );
 	   
 	    if( config == null || config.getRatingType( )== null ) {
           SiteMessageService.setMessage( request, RatingConstants.MESSAGE_ERROR_GENERIC_MESSAGE, SiteMessage.TYPE_STOP );
 	    }
        Rating rating =RatingFacadeFactory.getRatingInstance(config.getRatingType( ));
        BeanUtil.populate( rating, request, request.getLocale( ) );
        rating.setUser( user );
        rating.setUserGuid(user.getName( ));
        String strIdExtendableResource = rating.getIdExtendableResource( );
        String strExtendableResourceType = rating.getExtendableResourceType( );
       
        if ( StringUtils.isBlank( strIdExtendableResource ) || StringUtils.isBlank( strExtendableResourceType ) )
        {
            SiteMessageService.setMessage( request, RatingConstants.MESSAGE_ERROR_GENERIC_MESSAGE, SiteMessage.TYPE_STOP );
        }
       
        try {
        	RatingFacadeFactory.cancelRating( config, rating  );
			
		} catch (RatingException e) 
        {
			SiteMessageService.setCustomMessage( request, e.getMessage( ), SiteMessage.TYPE_STOP );
		}

        String strReferer = request.getHeader( RatingConstants.PARAMETER_HTTP_REFERER );

        if ( strReferer != null )
        {
            UrlItem url = new UrlItem( strReferer );

            if ( StringUtils.isNotEmpty( strFromUrl ) )
            {
                strFromUrl = strFromUrl.replaceAll( "%", "%25" );
                if ( !url.getUrl().contains( RatingConstants.PARAMETER_FROM_URL ) )
                {
                    url.addParameter( RatingConstants.PARAMETER_FROM_URL, strFromUrl );
                }
            }

            response.sendRedirect( url.getUrl(  ) );
        }
        else
        {
            response.sendRedirect( AppPathService.getPortalUrl(  ) );
        }
    }

    /**
     * Send notification.
     * @param request the request
     * @param strIdExtendableResource the str id extendable resource
     * @param strExtendableResourceType the str extendable resource type
     * @param fRatingValue the n rating value
     */
    private void sendNotification( HttpServletRequest request, String strIdExtendableResource,
        String strExtendableResourceType, float fRatingValue )
    {
        RatingExtenderConfig config = _configService.find( RatingResourceExtender.RESOURCE_EXTENDER,
                strIdExtendableResource, strExtendableResourceType );
        int nMailingListId = config.getIdMailingList(  );
        Collection<Recipient> listRecipients = AdminMailingListService.getRecipients( nMailingListId );

        for ( Recipient recipient : listRecipients )
        {
            Map<String, Object> model = new HashMap< >(  );

            String strSenderName = AppPropertiesService.getProperty( RatingConstants.PROPERTY_LUTECE_NAME );
            String strSenderEmail = AppPropertiesService.getProperty( RatingConstants.PROPERTY_WEBMASTER_EMAIL );
            String strResourceName = _resourceExtenderService.getExtendableResourceName( strIdExtendableResource,
                    strExtendableResourceType );

            Object[] params = { strResourceName };
            String strSubject = I18nService.getLocalizedString( RatingConstants.MESSAGE_NOTIFY_SUBJECT, params,
                    request.getLocale(  ) );

            model.put( RatingConstants.MARK_RESOURCE_EXTENDER_NAME, strResourceName );
            model.put( RatingConstants.MARK_VOTE_VALUE, fRatingValue );

            HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_RATING_NOTIFY_MESSAGE,
                    request.getLocale(  ), model );
            String strBody = template.getHtml(  );

            MailService.sendMailHtml( recipient.getEmail(  ), strSenderName, strSenderEmail, strSubject, strBody );
        }
    }

    /**
     * Get the session key of the URL to redirect the user to after he has voted
     * for the resource
     * @param strIdResource The id of the resource
     * @param strResourceType The type of the resource
     * @return The session key
     */
    public static String getSessionKeyUrlRedirect( String strIdResource, String strResourceType )
    {
        return ExtendPlugin.PLUGIN_NAME + RatingConstants.PARAMETER_FROM_URL + strResourceType + strIdResource;
    }
}
