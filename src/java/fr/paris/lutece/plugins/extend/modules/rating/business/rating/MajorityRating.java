package fr.paris.lutece.plugins.extend.modules.rating.business.rating;

import fr.paris.lutece.plugins.extend.modules.rating.business.Rating;
import fr.paris.lutece.plugins.extend.modules.rating.util.constants.RatingConstants;

public class MajorityRating extends Rating {
    @Override
    public String getRatingType() {
        return RatingConstants.MAJORITY_RATING;
    }
}
