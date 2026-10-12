package fr.paris.lutece.plugins.extend.modules.rating.business;

import fr.paris.lutece.portal.service.i18n.I18nService;

public class ThumbRating extends Rating {

	 public static final String RATING_TITLE = "module.extend.rating.thumbRating.title";
	 private long _nScoreLike;
	 private long _nScoreDislike;
	 
	 
	 /**
	  * Sums the votes and counts the positive and the negative ones.
	  *
	  * @param ratingValue the values of the votes
	  */
	 @Override
	 public void setScoreValue( float[] ratingValue )
	 {
	    super.setScoreValue( ratingValue );
	    for ( float fValue : ratingValue )
	    {
	        if ( fValue > 0 )
	        {
	            _nScoreLike++;
	        }
	        else if ( fValue < 0 )
	        {
	            _nScoreDislike++;
	        }
	    }
	 }
	 /**
      * Returns the ScoreLike
      * @return The ScoreLike
      */ 
      public long getScoreLike()
      {
          return _nScoreLike;
      }
  
     /**
      * Sets the ScoreLike
      * @param nScoreLike The ScoreLike
      */ 
      public void setScoreLike( long nScoreLike )
      {
          _nScoreLike = nScoreLike;
      }
  
     /**
      * Returns the ScoreDislike
      * @return The ScoreDislike
      */ 
      public long getScoreDislike()
      {
          return _nScoreDislike;
      }
  
     /**
      * Sets the ScoreDislike
      * @param nScoreDislike The ScoreDislike
      */ 
      public void setScoreDislike( long nScoreDislike )
      {
          _nScoreDislike = nScoreDislike;
      }
	@Override
	public String getTitle() {

		return I18nService.getLocalizedString( RATING_TITLE, I18nService.getDefaultLocale( ) ) ;
	}
}
