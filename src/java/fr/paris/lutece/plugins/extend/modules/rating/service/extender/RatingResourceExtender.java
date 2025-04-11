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
package fr.paris.lutece.plugins.extend.modules.rating.service.extender;

import fr.paris.lutece.plugins.extend.business.extender.ResourceExtenderDTO;
import fr.paris.lutece.plugins.extend.modules.rating.business.IRatingDAO;
import fr.paris.lutece.plugins.extend.modules.rating.business.config.RatingExtenderConfig;
import fr.paris.lutece.plugins.extend.modules.rating.service.RatingService;
import fr.paris.lutece.plugins.extend.modules.rating.util.constants.RatingConstants;
import fr.paris.lutece.plugins.extend.service.extender.AbstractResourceExtender;
import fr.paris.lutece.plugins.extend.service.extender.config.IResourceExtenderConfigService;
import fr.paris.lutece.plugins.extend.modules.rating.web.component.RatingResourceExtenderComponent;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.plugins.extend.business.extender.config.IExtenderConfigDAO;

import org.apache.commons.lang3.StringUtils;

import java.util.Locale;

import jakarta.inject.Inject;
import jakarta.inject.Named;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.annotation.PostConstruct;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import jakarta.enterprise.inject.spi.CDI;

/**
 * RatingResourceExtender
 */
@ApplicationScoped
@Named( "extend-rating.ratingResourceExtender" )
public class RatingResourceExtender extends AbstractResourceExtender
{
    /** The Constant RESOURCE_EXTENDER. */
    public static final String RESOURCE_EXTENDER = "rating";

    @Inject
    @Named( "extend-rating.ratingExtenderConfigService" )
    private IResourceExtenderConfigService _configService;

    @Inject
    @ConfigProperty( name = "extend.rating.titleKey", defaultValue = "module.extend.rating.extender.rating.label" )
    private String titleKey;

    @Inject
    private RatingResourceExtenderComponent resourceExtenderComponent;

    RatingResourceExtender( )
    {

    }

    @PostConstruct
    public void producesRatingResourceExtender( )
    {
        setResourceExtenderComponent( resourceExtenderComponent );
        setKey( RESOURCE_EXTENDER );
        setI18nTitleKey( I18nService.getLocalizedString( titleKey, Locale.getDefault( ) ) );
        setConfigRequired( true );
        setHistoryEnable( false );
        setStateEnable( true );

    }
      
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isInvoked( String strExtenderType )
    {
        if ( StringUtils.isNotBlank( strExtenderType ) )
        {
            return getKey(  ).equals( strExtenderType );
        }

        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getContent( String strIdExtendableResource, String strExtendableResourceType, String strParameters,
        HttpServletRequest request )
    {
        return getResourceExtenderComponent(  )
                   .getPageAddOn( strIdExtendableResource, strExtendableResourceType, strParameters, request );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void doCreateResourceAddOn( ResourceExtenderDTO extender )
    {
        // Default values
        RatingExtenderConfig config = new RatingExtenderConfig(  );
        config.setIdExtender( extender.getIdExtender(  ) );

        RatingExtenderConfig defaultConfig = _configService.find( -1 );

        if ( defaultConfig != null )
        {
            config.setIdMailingList( defaultConfig.getIdMailingList(  ) );
            config.setRatingType(defaultConfig.getRatingType( ));
            config.setNbDaysToVote( defaultConfig.getNbDaysToVote(  ) );
            config.setUniqueVote( defaultConfig.isUniqueVote(  ) );
        }

        _configService.create( config );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void doDeleteResourceAddOn( ResourceExtenderDTO extender )
    {
        if ( extender.getIdExtender(  ) > 0 )
        {
            _configService.remove( extender.getIdExtender(  ) );
        }
        RatingService.INSTANCE.removeByResource( extender.getIdExtendableResource(  ), extender.getExtendableResourceType(  ) );
    }
}
