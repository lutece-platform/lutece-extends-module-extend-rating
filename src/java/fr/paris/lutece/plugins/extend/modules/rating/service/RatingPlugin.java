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
package fr.paris.lutece.plugins.extend.modules.rating.service;

import fr.paris.lutece.plugins.extend.modules.rating.business.Rating;
import fr.paris.lutece.plugins.extend.modules.rating.business.SimpleRating;
import fr.paris.lutece.plugins.extend.modules.rating.business.StarRating;
import fr.paris.lutece.plugins.extend.modules.rating.business.ThumbRating;
import fr.paris.lutece.plugins.extend.modules.rating.service.extender.RatingResourceExtender;
import fr.paris.lutece.plugins.extend.modules.rating.service.facade.RatingFacadeFactory;
import fr.paris.lutece.plugins.extend.modules.rating.service.facade.RatingType;
import fr.paris.lutece.plugins.extend.service.extender.facade.ResourceExtenderServiceFacade;
import fr.paris.lutece.plugins.extend.service.extender.facade.ExtenderType;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginDefaultImplementation;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.web.l10n.LocaleService;


/**
 *
 * RatingPlugin
 *
 */
public class RatingPlugin extends PluginDefaultImplementation
{
    /** The Constant PLUGIN_NAME. */
    public static final String PLUGIN_NAME = "extend-rating";

    /** The Constant TRANSACTION_MANAGER. */
    public static final String TRANSACTION_MANAGER = PLUGIN_NAME + ".transactionManager";
    
   

    /**
     * Gets the plugin.
     *
     * @return the plugin
     */
    public static Plugin getPlugin(  )
    {
        return PluginService.getPlugin( PLUGIN_NAME );
    }
    @Override
    public void init( )
    {
        super.init( );
        //Create and initialize rating type
    	RatingType.newBuilder( SimpleRating.class, I18nService.getLocalizedString(SimpleRating.RATING_TITLE, LocaleService.getDefault( ))).build();
		RatingType.newBuilder( ThumbRating.class, I18nService.getLocalizedString(ThumbRating.RATING_TITLE, LocaleService.getDefault( ))).build();
		RatingType.newBuilder( StarRating.class, I18nService.getLocalizedString(StarRating.RATING_TITLE, LocaleService.getDefault( ))).build();
		
		//Addition of rating for the exploitation of rating information from the extend plugin
        ResourceExtenderServiceFacade.addExtenderType(
        		new ExtenderType< >(
        			Rating.class,
               		RatingResourceExtender.RESOURCE_EXTENDER,
               		RatingFacadeFactory::getInfoRating,
               		RatingFacadeFactory::getInfoRatingByList,
               		RatingFacadeFactory::getInfoForExport,
               		RatingFacadeFactory::getInfoForRecap )
        );
    }
}
