/*
 * Copyright (c) 2002-2012, Mairie de Paris
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
package fr.paris.lutece.plugins.extend.modules.rating.web.type;

import fr.paris.lutece.plugins.extend.modules.rating.business.type.VoteType;
import fr.paris.lutece.plugins.extend.modules.rating.service.type.IVoteTypeService;
import fr.paris.lutece.plugins.extend.modules.rating.service.type.VoteTypeService;
import fr.paris.lutece.plugins.extend.modules.rating.util.constants.RatingConstants;
import fr.paris.lutece.plugins.extend.util.ExtendUtils;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.web.admin.PluginAdminPageJspBean;
import fr.paris.lutece.portal.web.constants.Messages;
import fr.paris.lutece.portal.web.pluginaction.DefaultPluginActionResult;
import fr.paris.lutece.portal.web.pluginaction.IPluginActionResult;
import fr.paris.lutece.util.beanvalidation.BeanValidationUtil;
import fr.paris.lutece.util.html.HtmlTemplate;

import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.validation.ConstraintViolation;


/**
 *
 * VoteTypeJspBean
 *
 */
public class VoteTypeJspBean extends PluginAdminPageJspBean
{
    /** The Constant RIGHT_MANAGE_VOTE_TYPES. */
    public static final String RIGHT_MANAGE_VOTE_TYPES = "EXTEND_VOTE_TYPES_MANAGEMENT";

    // TEMPLATES
    private static final String TEMPLATE_MANAGE_VOTE_TYPES = "admin/plugins/extend/modules/rating/type/manage_vote_types.html";
    private static final String TEMPLATE_MODIFY_VOTE_TYPE = "admin/plugins/extend/modules/rating/type/modify_vote_type.html";

    // JSP
    private static final String JSP_URL_MANAGE_VOTE_TYPES = "jsp/admin/plugins/extend/modules/rating/ManageVoteTypes.jsp";

    // SERVICES
    private IVoteTypeService _voteService = SpringContextService.getBean( VoteTypeService.BEAN_SERVICE );

    /**
     * Gets the manage vote types.
     *
     * @param request the request
     * @param response the response
     * @return the manage vote types
     * @throws AccessDeniedException the access denied exception
     */
    public IPluginActionResult getManageVoteTypes( HttpServletRequest request, HttpServletResponse response )
        throws AccessDeniedException
    {
        setPageTitleProperty( RatingConstants.PROPERTY_MANAGE_VOTE_TYPES_PAGE_TITLE );

        Map<String, Object> model = new HashMap<String, Object>(  );
        model.put( RatingConstants.MARK_LIST_VOTE_TYPES, _voteService.findAll( false ) );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MANAGE_VOTE_TYPES, request.getLocale(  ), model );

        IPluginActionResult result = new DefaultPluginActionResult(  );

        result.setHtmlContent( getAdminPage( template.getHtml(  ) ) );

        return result;
    }

    /**
     * Gets the modify vote type.
     *
     * @param request the request
     * @param response the response
     * @return the modify vote type
     * @throws AccessDeniedException the access denied exception
     */
    public IPluginActionResult getModifyVoteType( HttpServletRequest request, HttpServletResponse response )
        throws AccessDeniedException
    {
        setPageTitleProperty( RatingConstants.PROPERTY_MANAGE_VOTE_TYPES_PAGE_TITLE );

        String strHtml = StringUtils.EMPTY;
        IPluginActionResult result = new DefaultPluginActionResult(  );
        String strIdVoteType = request.getParameter( RatingConstants.PARAMETER_ID_VOTE_TYPE );

        if ( StringUtils.isNotBlank( strIdVoteType ) && StringUtils.isNumeric( strIdVoteType ) )
        {
            int nIdVoteType = Integer.parseInt( strIdVoteType );

            Map<String, Object> model = new HashMap<String, Object>(  );
            model.put( RatingConstants.MARK_VOTE_TYPE, _voteService.findByPrimaryKey( nIdVoteType, true ) );
            model.put( RatingConstants.MARK_WEBAPP_URL, AppPathService.getBaseUrl( request ) );
            model.put( RatingConstants.MARK_LOCALE, getLocale(  ) );

            HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MODIFY_VOTE_TYPE, request.getLocale(  ),
                    model );
            strHtml = template.getHtml(  );
        }

        if ( StringUtils.isNotBlank( strHtml ) )
        {
            result.setHtmlContent( strHtml );
        }
        else
        {
            result.setRedirect( AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS,
                    AdminMessage.TYPE_STOP ) );
        }

        return result;
    }

    /**
     * Do modify vote type.
     *
     * @param request the request
     * @return the string
     */
    public String doModifyVoteType( HttpServletRequest request )
    {
        String strCancel = request.getParameter( RatingConstants.PARAMETER_CANCEL );

        if ( StringUtils.isBlank( strCancel ) )
        {
            String strIdVoteType = request.getParameter( RatingConstants.PARAMETER_ID_VOTE_TYPE );

            if ( StringUtils.isBlank( strIdVoteType ) || !StringUtils.isNumeric( strIdVoteType ) )
            {
                return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
            }

            int nIdVoteType = Integer.parseInt( strIdVoteType );
            VoteType voteType = _voteService.findByPrimaryKey( nIdVoteType, true );

            if ( voteType == null )
            {
                return AdminMessageService.getMessageUrl( request, RatingConstants.MESSAGE_ERROR_GENERIC_MESSAGE,
                    AdminMessage.TYPE_STOP );
            }

            populate( voteType, request );

            // Check mandatory fields
            Set<ConstraintViolation<VoteType>> constraintViolations = BeanValidationUtil.validate( voteType );

            if ( constraintViolations.size(  ) > 0 )
            {
                Object[] params = { ExtendUtils.buildStopMessage( constraintViolations ) };

                return AdminMessageService.getMessageUrl( request, RatingConstants.MESSAGE_STOP_GENERIC_MESSAGE,
                    params, AdminMessage.TYPE_STOP );
            }

            _voteService.update( voteType );
        }

        return AppPathService.getBaseUrl( request ) + JSP_URL_MANAGE_VOTE_TYPES;
    }
}
