package fr.paris.lutece.plugins.extend.modules.rating.business;

import java.util.List;

public class ThumbRating extends Rating {

	 public static final String RATING_TITLE = "module.extend.rating.thumbRating.title";
	 private long _nScoreLike;
	 private long _nScoreDislike;
	 
	 
	 /**
	  * @param dScoreValue the dScoreValue to set
	  */
	 public void setScoreValue( List<Rating> listRating )
	 {
	    _dScoreValue= listRating.stream().mapToDouble( Rating::getScoreValue ).sum( );
	    _nScoreLike= listRating.stream().filter(rh-> rh.getRatingValue() < 0 ).count( ) ;
	    _nScoreDislike= listRating.stream().filter(rh-> rh.getRatingValue() > 0 ).count( );
	    	
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
}
