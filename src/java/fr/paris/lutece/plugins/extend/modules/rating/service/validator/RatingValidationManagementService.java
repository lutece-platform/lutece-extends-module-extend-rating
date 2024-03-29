/*
 * Copyright (c) 2002-2014, Mairie de Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.extend.modules.rating.service.validator;

import fr.paris.lutece.portal.service.security.LuteceUser;
import fr.paris.lutece.portal.service.spring.SpringContextService;

import org.apache.commons.lang3.StringUtils;
import javax.servlet.http.HttpServletRequest;


/**
 * Service to manage ration validation
 */
public final class RatingValidationManagementService
{
	
	private RatingValidationManagementService ()
	{
		
	}
    /**
     * Check if a user is allowed to validate a resource
     * @param request The request
     * @param user The user that wants to rate a resource
     * @param strIdResource the id of the resource
     * @param strResourceTypeKey The resource type key
     * @param fRatingValue The rating value
     * @return The URL to redirect the user if he is not allowed to rate the
     *         resource, or null if he is allowed
     */
    public static String validateRating( HttpServletRequest request, LuteceUser user, String strIdResource,
        String strResourceTypeKey, float fRatingValue )
    {
        for ( IRatingValidationService validationService : SpringContextService.getBeansOfType( 
                IRatingValidationService.class ) )
        {
            String strUrlError = validationService.validateRating( request, user, strIdResource, strResourceTypeKey,
            		fRatingValue );

            if ( StringUtils.isNotBlank( strUrlError ) )
            {
                return strUrlError;
            }
        }

        return null;
    }
    /**
     * check if the rating can be done
     * @param strIdResource the id of the resource
     * @param strResourceTypeKey The resource type key
     * @param user The user that wants to rate a resource
     * @return true if the rating can be done
     */
    public static boolean canRating( String strIdExtendableResource, String strExtendableResourceType, LuteceUser user )
    {   	
    	for ( IRatingValidator validatorService : SpringContextService.getBeansOfType( 
                    IRatingValidator.class ) )
        {
             if(!validatorService.canRating( strIdExtendableResource, strExtendableResourceType, user )){
               	return false;
             }                    
        }            
    	return true;

    }
    /**
     * check if the rating can be done
     * @param strIdResource the id of the resource
     * @param strResourceTypeKey The resource type key
     * @param user The user that wants to rate a resource
     * @return true if the rating can be done
     */
    public static boolean canCancelRating( String strIdExtendableResource, String strExtendableResourceType, LuteceUser user )
    {   	
    	for ( IRatingValidator validatorService : SpringContextService.getBeansOfType( 
                    IRatingValidator.class ) )
        {
             if(!validatorService.canCancelRating( strIdExtendableResource, strExtendableResourceType, user )){
               	return false;
             }                    
        }
    	return true;
    }
}
