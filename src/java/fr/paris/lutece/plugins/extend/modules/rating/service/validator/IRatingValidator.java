package fr.paris.lutece.plugins.extend.modules.rating.service.validator;

import fr.paris.lutece.portal.service.security.LuteceUser;

public interface IRatingValidator {

	 
	 /**
     * check if the rating can be done
     * @param strIdResource the id of the resource
     * @param strResourceTypeKey The resource type key
     * @param user The user that wants to rate a resource (user = null if the authentication is note enabled)
     * @return true if the rating can be done else return false
     */
	  boolean canRating( String strIdExtendableResource, String strExtendableResourceType, LuteceUser user );
	  
	  /**
	     * check if the rating can be canceled
	     * @param strIdResource the id of the resource
	     * @param strResourceTypeKey The resource type key
	     * @param user The user that wants to rate a resource 
	     * @return true if the rating can be done else return false
	   */
	 boolean canCancelRating( String strIdExtendableResource, String strExtendableResourceType, LuteceUser user );
		   
	    
}
