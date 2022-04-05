package fr.paris.lutece.plugins.extend.modules.rating.service.facade;


import java.util.List;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import javax.servlet.http.HttpServletRequest;

import fr.paris.lutece.plugins.extend.modules.rating.business.Rating;
import fr.paris.lutece.plugins.extend.modules.rating.business.config.RatingExtenderConfig;

/**
 * Rating type
 * This interface is exposed for the creation of the type of vote
 * Example of building an rating type
 * RatingType.newBuilder( SimpleRating.class, 
 * 						  I18nService.getLocalizedString(SimpleRating.RATING_TITLE, LocaleService.getDefault( )))
 * 						.build();
 *
 */

public interface RatingType {
	
	/**
	 * Build a new rating type
	 * @param type the type of rating 
	 * @param strTitle the title of rating type
	 * @return an instance of rating type
	 */
	public static RatingType newRatingType( Class<? extends Rating>  type, String strTitle ) {
		return newBuilder( type, strTitle ).build();
	}
	/**
	 * Get builder to building an rating type
	 * @param type the type of rating
	 * @param strTitle the title of the rating type
	 * @return builder
	 */
	public static Builder newBuilder( Class<? extends Rating>  type, String strTitle ) {
	    return new RatingTypeBuilderImpl( type, strTitle );
	}
	/**
	 * Builder Interface
	 *
	 */
	public interface Builder {
				
		/**
		 * Consumer to rate
		 * @param doRating the Consumer interface
		 * @return builder
		 */
		public Builder doRating(  Consumer< Rating > doRating  ) ;
		/**
		 * Do cancel rating 
		 * @param doCancelRating the Consumer interface
		 * @return builder
		 */
		public Builder cancelRating(  Consumer<Rating > doCancelRating  );
		/**
		 * GetPageAddOn
		 * @param getPageAddOn the QuinquaFunction interface
		 * @return builder
		 */
		public Builder getPageAddOn (  QuinquaFunction< RatingExtenderConfig , String, String, String, HttpServletRequest ,String > getPageAddOn );		
		/**
		 * BinaryOperator to get the information value associate to the rating Type to export in the file export
		 * @param getInfoForExport  the BinaryOperator
		 * @return builder
		 */
		public Builder getInfoForExport(  BinaryOperator<String> getInfoForExport );
		/**
		 * BinaryOperator to get the information value associate to the ExtenderType to write in the recap
		 * @param getInfoForRecap the BinaryOperator
		 * @return builder
		 */
		public Builder getInfoForRecap (  BinaryOperator<String> getInfoForRecap );
		/**
		 * BiFunction to get the list of extender type information object
		 * @param getInfoExtender the BiFunction
		 * @return builder
		 */
		public Builder getInfoExtender (  BiFunction<String, String, List< Rating > > getInfoExtender );
		/**
		 * BiFunction to get the list of extender type information object
		 * @param getInfoExtenderByList the BiFunction
		 * @return builder
		 */ 
		public Builder getInfoExtenderByList (  BiFunction<List<String>, String,  List<Rating> > getInfoExtenderByList );
		/**
		 * Build rating type
		 * @return the rating type
		 */
	    public RatingType build( );
	}
	/**
	 * Consumer to rate
	 * @param doRating the Consumer interface
	 */
	public  void doRating( Rating rating  );
	/**
	 * Do cancel rating 
	 * @param doCancelRating the Consumer interface
	 */
	public  void cancelRating( Rating rating  );	
	/**
	 * GetPageAddOn
	 * @param getPageAddOn the QuinquaFunction interface
	 */
	public  String getPageAddOn( RatingExtenderConfig ratingExtenderConfig, String strIdExtendableResource, String strExtendableResourceType, String strParameters,
	        HttpServletRequest request );
	/**
	 * Get type of this 
	 * @return type rating
	 */
	public  Class<? extends Rating> getType( );
	/**
	 * Get the name type of the rating type
	 * @return type name
	 */
	public  String getTypeName( );
	/**
	 * Get title of rating type
	 * @return the title of rating
	 */
	public  String getTitle( );
	/**
	 * BinaryOperator to get the information value associate to the rating Type to export in the file export
	 * @param getInfoForExport  the BinaryOperator
	 */
	public  String getInfoForExport( String strIdExtendableResource , String strExtendableResourceType  );
	/**
	 * BinaryOperator to get the information value associate to the ExtenderType to write in the recap
	 * @param getInfoForRecap the BinaryOperator
	 */
	public  String getInfoForRecap ( String strIdExtendableResource , String strExtendableResourceType );
	/**
	 * BiFunction to get the list of extender type information object
	 * @param getInfoExtender the BiFunction
	 */
	public  List<Rating> getInfoExtender ( String strIdExtendableResource , String strExtendableResourceType );
	/**
	 * BiFunction to get the list of extender type information object
	 * @param getInfoExtenderByList the BiFunction
	 */ 
	public  List<Rating> getInfoExtenderByList (List<String> listIdExtendableResource , String strExtendableResourceType );
	
}
