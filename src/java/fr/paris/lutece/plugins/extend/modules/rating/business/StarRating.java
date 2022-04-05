package fr.paris.lutece.plugins.extend.modules.rating.business;


public class StarRating extends Rating {
	
	 public static final String RATING_TITLE = "module.extend.rating.starRating.title";

	@Override
	public double getScoreValue(  )
    {
        return getAverageScore( );
    }
	/**
     * Calculate the score (min : 1 - max : 4).
     *
     * @return the average score
     */
    public double getAverageScore(  )
    {
        double dScore = 0;

        if ( _nRatingCount > 0 )
        {
            double averageScore = _dScoreValue / ( double ) _nRatingCount;
            dScore =  Math.round( averageScore * 10 ) * 0.1;
        }

        return dScore;
    }
    
    /**
     *
     * @return the average score calculated with 0.5 precision
     */
    
    public double getAverageScoreRoundToHalf(  )
    {
        double dScore = 0;

        if ( _nRatingCount > 0 )
        {
            double averageScore = _dScoreValue / (double) _nRatingCount;
            dScore =  Math.round( averageScore * 2 ) * 0.5;
        }

        return dScore;
    }
    
}
