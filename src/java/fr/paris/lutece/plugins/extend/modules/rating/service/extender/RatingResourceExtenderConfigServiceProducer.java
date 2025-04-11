package fr.paris.lutece.plugins.extend.modules.rating.service.extender;

import fr.paris.lutece.plugins.extend.modules.rating.business.config.RatingExtenderConfig;
import fr.paris.lutece.plugins.extend.service.extender.config.IResourceExtenderConfigService;
import fr.paris.lutece.plugins.extend.service.extender.config.ResourceExtenderConfigService;
import fr.paris.lutece.plugins.extend.business.extender.config.IExtenderConfigDAO;
import fr.paris.lutece.plugins.extend.service.extender.IResourceExtenderService;

import fr.paris.lutece.portal.service.cache.Lutece107Cache;
import fr.paris.lutece.portal.service.cache.LuteceCache;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.enterprise.inject.Produces;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class RatingResourceExtenderConfigServiceProducer {

    @Inject
    @LuteceCache(cacheName = "extendConfigCache", keyType = String.class, valueType = Object.class, enable = true)
    Lutece107Cache<String, Object> _extendConfigCache;

    @Inject 
    @Named( "extend-rating.ratingExtenderConfigDAO" ) 
    IExtenderConfigDAO<RatingExtenderConfig> ratingExtenderConfigDAO;

    @Inject
    IResourceExtenderService resourceExtenderService;
        
    @Produces
    @ApplicationScoped
    @Named( "extend-rating.ratingExtenderConfigService" )
    public IResourceExtenderConfigService produceRatingResourceExtenderConfigService( )
    {
        ResourceExtenderConfigService ratingExtenderConfigService = new ResourceExtenderConfigService( );

        ratingExtenderConfigService.setExtenderConfigDAO( ( IExtenderConfigDAO ) ratingExtenderConfigDAO );
        ratingExtenderConfigService.setExtenderService( resourceExtenderService );
        ratingExtenderConfigService.setExtenderCache( _extendConfigCache );

        return ratingExtenderConfigService;
    }

}
