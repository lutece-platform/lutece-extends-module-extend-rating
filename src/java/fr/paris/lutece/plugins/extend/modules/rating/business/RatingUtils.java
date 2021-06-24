package fr.paris.lutece.plugins.extend.modules.rating.business;

import fr.paris.lutece.plugins.extend.modules.rating.business.rating.MajorityRating;
import fr.paris.lutece.plugins.extend.modules.rating.business.rating.SimpleRating;
import fr.paris.lutece.plugins.extend.modules.rating.business.rating.StarRating;
import fr.paris.lutece.plugins.extend.modules.rating.business.rating.ThumbRating;

import static fr.paris.lutece.plugins.extend.modules.rating.util.constants.RatingConstants.*;

public class RatingUtils {
    public static Rating ratingForType( String ratingType ) {
        switch (ratingType) {
            case SIMPLE_RATING:
                return new SimpleRating();
            case THUMB_RATING:
                return new ThumbRating();
            case MAJORITY_RATING:
                return new MajorityRating();
            case STAR_RATING:
                return new StarRating();
            //TODO create dedicated exception
            default: throw new RuntimeException("Rating type not handled");
        }
    }
}
