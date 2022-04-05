package fr.paris.lutece.plugins.extend.modules.rating.service.facade;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;

import javax.servlet.http.HttpServletRequest;

import fr.paris.lutece.plugins.extend.modules.rating.business.Rating;
import fr.paris.lutece.plugins.extend.modules.rating.business.config.RatingExtenderConfig;
import static java.util.Objects.requireNonNull;

public class RatingTypeImpl implements RatingType {

	/**
	 *  Rating extender Title
	 */
	protected String _strTitle; 
	/**
	 * Rating type (SimpleRatin, StarRating...)
	 */
	protected Class<? extends Rating> _type;
	/**
	 * Consumer to rating service 
	 */
	protected Consumer< Rating > _doRating ;
	/**
	 * Consumer to cancel rating service
	 */
	protected Consumer<Rating > _cancelRating  ;
	/**
	 * QuinquaFunction to get template content to rating
	 */
	protected QuinquaFunction< RatingExtenderConfig , String, String, String, HttpServletRequest ,String > _getPageAddOn ;
	/**
	 * BinaryOperator to get the information value associate to the ExtenderType to export in the file export
	 */
	protected BinaryOperator<String> _getInfoForExport;
	/**
	 * BinaryOperator to get the information value associate to the ExtenderType to write in the recap
	 */
	protected BinaryOperator<String> _getInfoForRecap;
	/**
	 * BiFunction to get the list of extender type information object
	 * @param <T> the type of the output arguments of the getInfoExtender
	 */
	protected BiFunction<String, String, List< Rating > > _getInfoExtender;
	
	/**
	 * BiFunction to get the list of extender type information object
	 * @param <T> the type of the output arguments of the getInfoExtenderByList
	 */
	protected BiFunction<List<String>, String,  List<Rating> > _getInfoExtenderByList;
	
	/**
	 * Constructor
	 * @param builder the builder 
	 * @param defaultTypeRating the default rating implementation
	 */
	public RatingTypeImpl(RatingTypeBuilderImpl builder, DefaultRatingTypeImpl defaultTypeRating) {
		
		requireNonNull( builder._type );
		requireNonNull( builder._strTitle );
		
		this._type= builder._type;
		this._strTitle = builder._strTitle;
		
		
		if( builder._cancelRating == null )
		{
			this._cancelRating = defaultTypeRating::cancelRating;
		}
		else
		{
			this._cancelRating = builder._cancelRating ;
    	}
    	if( builder._doRating == null ) {
    		
    		 this._doRating = defaultTypeRating::doRating ;
    	}
    	else
    	{
    		 this._doRating = builder._doRating;
    	}
    	if( builder._getPageAddOn == null ) {
    		
    		this._getPageAddOn = defaultTypeRating::getPageAddOn;
    	}
    	else {
    		
    		this._getPageAddOn = builder._getPageAddOn ;
    	}    	
    	if( builder._getInfoExtender == null ) {
    		
    		 this._getInfoExtender= defaultTypeRating::getInfoExtender;
    	}
    	else {
    		
    		 this._getInfoExtender= builder._getInfoExtender;
    	}
    	if(  builder._getInfoExtenderByList == null ) {
    		
    		this._getInfoExtenderByList= defaultTypeRating::getInfoExtenderByList;
    	}
    	else
    	{
    		this._getInfoExtenderByList = builder._getInfoExtenderByList;
    	}
    	
    	if( builder._getInfoForExport == null ) {
    		
    		 this._getInfoForExport = defaultTypeRating::getInfoForExport;
    		 
    	}else {
    		
      		 this._getInfoForExport = builder._getInfoForExport ;
    	}
    	
    	if(  builder._getInfoForRecap == null ) {
    		
    		this._getInfoForRecap= defaultTypeRating::getInfoForRecap;
    	}else {
    		
    		this._getInfoForRecap= builder._getInfoForRecap;
    	}
					
	}

	
    private static final class RatingTypeFacadeFactory {
    	
    	static RatingType createRatingType( RatingTypeImpl impl) {
    		
    		RatingFacadeFactory.addRatingType( impl );
    		return  impl ;
        }
    }
    /**
     * Build type rating 
     * @param builder the builder
     * @return rating type instance
     */
	static RatingType create( RatingTypeBuilderImpl builder) {
		
		DefaultRatingTypeImpl defaultTypeRating = new DefaultRatingTypeImpl( builder._type, builder._strTitle );
		RatingTypeImpl ratingTypeImpl =new RatingTypeImpl( builder,  defaultTypeRating );
        return RatingTypeFacadeFactory.createRatingType( ratingTypeImpl );
    }
	/**
     * {@inheritDoc}
     */
	public void doRating( Rating rating  ){
		
		 _doRating.accept( rating ); 
	}
	/**
     * {@inheritDoc}
     */
	public void cancelRating( Rating rating  ){
		
		_cancelRating.accept( rating ); 
	}
	
	/**
     * {@inheritDoc}
     */
	public String getPageAddOn( RatingExtenderConfig ratingExtenderConfig, String strIdExtendableResource, String strExtendableResourceType, String strParameters,
	        HttpServletRequest request ){
		
		return _getPageAddOn.apply( ratingExtenderConfig, strIdExtendableResource , strExtendableResourceType, strParameters, request ); 
	}
	/**
     * {@inheritDoc}
     */
	public Class<? extends Rating> getType( ){
		
		return _type;
	}
	/**
     * {@inheritDoc}
     */
	public String getTypeName( ){
		
		return _type.getName( );
	}
	/**
     * {@inheritDoc}
     */
	public String getTitle( ){
		
		return _strTitle;
	}
	/**
     * {@inheritDoc}
     */
	public String getInfoForExport( String strIdExtendableResource , String strExtendableResourceType  )
	{
		return _getInfoForExport.apply( strIdExtendableResource, strExtendableResourceType );
	}
	/**
     * {@inheritDoc}
     */
	public String getInfoForRecap ( String strIdExtendableResource , String strExtendableResourceType )
	{
		return _getInfoForRecap.apply( strIdExtendableResource , strExtendableResourceType );
	}
	/**
     * {@inheritDoc}
     */
	public List<Rating> getInfoExtender ( String strIdExtendableResource , String strExtendableResourceType )
	{
		return _getInfoExtender.apply( strIdExtendableResource , strExtendableResourceType );
	}
	/**
     * {@inheritDoc}
     */
	public  List<Rating> getInfoExtenderByList (List<String> listIdExtendableResource , String strExtendableResourceType )
	{
		return _getInfoExtenderByList.apply( listIdExtendableResource , strExtendableResourceType );
	}
}
