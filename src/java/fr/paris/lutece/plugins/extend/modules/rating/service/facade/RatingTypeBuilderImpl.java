package fr.paris.lutece.plugins.extend.modules.rating.service.facade;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import javax.servlet.http.HttpServletRequest;

import fr.paris.lutece.plugins.extend.modules.rating.business.Rating;
import fr.paris.lutece.plugins.extend.modules.rating.business.config.RatingExtenderConfig;
import static java.util.Objects.requireNonNull;


public class RatingTypeBuilderImpl implements RatingType.Builder
{

	 String _strTitle; 
	 Class<? extends Rating> _type;

	 Consumer<Rating> _doRating;
	 Consumer<Rating> _cancelRating;
	 QuinquaFunction< RatingExtenderConfig , String, String, String, HttpServletRequest ,String > _getPageAddOn;
	/**
	 * BinaryOperator to get the information value associate to the ExtenderType to export in the file export
	 */
	 BinaryOperator<String> _getInfoForExport;
	/**
	 * BinaryOperator to get the information value associate to the ExtenderType to write in the recap
	 */
	 BinaryOperator<String> _getInfoForRecap;
	/**
	 * BiFunction to get the list of extender type information object
	 * @param <T> the type of the output arguments of the getInfoExtender
	 */
	 BiFunction<String, String, List< Rating > > _getInfoExtender;
	
	/**
	 * BiFunction to get the list of extender type information object
	 * @param <T> the type of the output arguments of the getInfoExtenderByList
	 */
	 BiFunction<List<String>, String,  List<Rating> > _getInfoExtenderByList;

		
	protected RatingTypeBuilderImpl( Class<? extends Rating>  type, String strTitle )
	{		
		requireNonNull( type );
		requireNonNull( strTitle );
		this._type= type;
		this._strTitle= strTitle;
	}
	public RatingTypeBuilderImpl doRating( final Consumer< Rating > doRating  ) 
	{	
		requireNonNull( doRating );
		this._doRating= doRating;
		return this;
	}
	public RatingTypeBuilderImpl cancelRating( final Consumer<Rating > cancelRating  )
	{
		requireNonNull( cancelRating );
		this._cancelRating= cancelRating;
		return this;	
	}
	public RatingTypeBuilderImpl getPageAddOn ( final QuinquaFunction< RatingExtenderConfig , String, String, String, HttpServletRequest ,String > getPageAddOn )
	{
		requireNonNull( getPageAddOn );
		this._getPageAddOn= getPageAddOn;
		return this;
	}
	
	public RatingTypeBuilderImpl getInfoForExport( final BinaryOperator<String> getInfoForExport )
	{
		requireNonNull( getInfoForExport );
		this._getInfoForExport= getInfoForExport;
		return this;
	}
	public RatingTypeBuilderImpl getInfoForRecap ( final BinaryOperator<String> getInfoForRecap )
	{
		requireNonNull( getInfoForRecap );
		this._getInfoForRecap= getInfoForRecap;
		return this;
	}
	public RatingTypeBuilderImpl getInfoExtender ( final BiFunction<String, String, List< Rating > > getInfoExtender )
	{
		requireNonNull( getInfoExtender );
		this._getInfoExtender= getInfoExtender;	
		return this;
	}
	public RatingTypeBuilderImpl getInfoExtenderByList ( final BiFunction<List<String>, String,  List<Rating> > getInfoExtenderByList )
	{
		requireNonNull( getInfoExtenderByList );
		this._getInfoExtenderByList= getInfoExtenderByList;
		return this;
	}
	 /**
     * Returns a new RatingType built from the current state of this
     * builder.
     *
     * @return a new RatingType
     */
    public RatingType build( )
    {
    	return RatingTypeImpl.create( this );
    }
    

}
