/*
 * Copyright (c) 2002-2022, City of Paris
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
package fr.paris.lutece.plugins.extend.modules.rating.dashboard;

import java.util.HashMap;
import java.util.Map;


import fr.paris.lutece.plugins.publicdashboard.service.PublicDashboardService;
import fr.paris.lutece.plugins.extend.business.extender.history.ResourceExtenderHistory;
import fr.paris.lutece.plugins.extend.business.extender.history.ResourceExtenderHistoryFilter;
import fr.paris.lutece.plugins.extend.service.extender.history.IResourceExtenderHistoryService;
import fr.paris.lutece.plugins.extend.service.extender.history.ResourceExtenderHistoryService;
import fr.paris.lutece.portal.service.dashboard.IPublicDashboardComponent;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.util.html.HtmlTemplate;

// TODO: Auto-generated Javadoc
/**
 * The Class MyPublicProjectCounterProfile.
 */
public class PublicDashboardRating implements IPublicDashboardComponent
{

	public static final String DASHBOARD_PROPERTIES_TITLE = "module.extend.rating.publicdashboard.bean.title";
	private String strIdComponent = "extend-rating.dashboardExtendsRating";
	private static final String TEMPLATE_MANAGE_FORMS = "/skin/plugins/publicdashboard/view_ratings.html";
	private static final String MARK_DASHBOARD_RATING = "dashboardrating";
	
	@Override
	public String getDashboardData( String user_id, Map<String,String> additionalParameters ) {

		Map<String, Object> model = new HashMap<String, Object>( );
		model.put( MARK_DASHBOARD_RATING, searchExtendCounter( user_id ) );
        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MANAGE_FORMS, I18nService.getDefaultLocale( ), model );

        return template.getHtml( );
	}

	@Override
	public String getComponentDescription( ) {
		return I18nService.getLocalizedString( DASHBOARD_PROPERTIES_TITLE, I18nService.getDefaultLocale( ) );
	}

	@Override
	public String getComponentId( ) {
		return strIdComponent;
	}

    /**
     * Search project counter.
     *
     * @param guid the guid
     * @return the list
     */
    private static Map<String, Integer> searchExtendCounter( String user_id )
    {

        IResourceExtenderHistoryService _resourceExtenderHistoryService = SpringContextService.getBean( ResourceExtenderHistoryService.BEAN_SERVICE );

        ResourceExtenderHistoryFilter filter = new ResourceExtenderHistoryFilter( );
        filter.setUserGuid( user_id );

        Map<String, Integer> mapExtendStatistics = new HashMap<>( );

        for ( ResourceExtenderHistory resourceExtenderHistory : _resourceExtenderHistoryService.findByFilter( filter ) )
        {
            Integer value = mapExtendStatistics.get( resourceExtenderHistory.getExtenderType( ) );

            if ( value != null && value > 0 )
            {
                mapExtendStatistics.put( resourceExtenderHistory.getExtenderType( ), value + 1 );
            }
            else
            {
                mapExtendStatistics.put( resourceExtenderHistory.getExtenderType( ), 1 );
            }
        }

        return mapExtendStatistics;

    }

}
