package fr.paris.lutece.plugins.extend.modules.rating.business;

import fr.paris.lutece.portal.service.i18n.I18nService;

public class SimpleRating extends Rating {

	public static final String RATING_TITLE = "module.extend.rating.simpleRating.title";

	@Override
	public String getTitle() {
		
		return I18nService.getLocalizedString( RATING_TITLE, I18nService.getDefaultLocale( ) ) ;
	}
	
}
