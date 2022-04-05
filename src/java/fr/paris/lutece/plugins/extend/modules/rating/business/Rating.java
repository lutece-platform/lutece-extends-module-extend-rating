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
package fr.paris.lutece.plugins.extend.modules.rating.business;

import fr.paris.lutece.plugins.extend.business.extender.history.ResourceExtenderHistory;
import fr.paris.lutece.plugins.extend.modules.rating.util.constants.RatingUtils;
import fr.paris.lutece.plugins.extend.service.extender.facade.IExtendableResourceResult;
import fr.paris.lutece.portal.service.security.LuteceUser;


/**
 *
 * Rating Class
 *
 */
public abstract class Rating  extends ResourceExtenderHistory implements IExtendableResourceResult
{
	private int _nIdRating;
    protected int _nRatingCount;
    protected double _dScoreValue;
    private float _fRatingValue;
    private String _strRatingType = this.getClass().getName( );
    private LuteceUser _user; 

    /**
     * @return the nIdRating
     */
    public int getIdRating(  )
    {
        return _nIdRating;
    }

    /**
     * @param nIdRating the nIdRating to set
     */
    
    public void setIdRating( int nIdRating )
    {
        _nIdRating = nIdRating;
    }

    /**
   

    /**
     * @return the nRatingCount
     */
    public int getRatingCount(  )
    {
        return _nRatingCount;
    }

    /**
     * @param nRatingCount the nRatingCount to set
     */
    public void setRatingCount( int nRatingCount )
    {
        _nRatingCount = nRatingCount;
    }

    /**
     * @return the dScoreValue
     */
    
    public double getScoreValue(  )
    {
        return _dScoreValue;
    }
    /**
     * @param dScoreValue the dScoreValue to set
     */
    public void setScoreValue(  float[] ratingValue )
    {
        for (float x : ratingValue) {
        	_dScoreValue += x;
        }  	
    }
    /**
     * @param dScoreValue the dScoreValue to set
     */
    public void setScoreValue(  double scoreValue )
    {
        _dScoreValue = scoreValue; 	
    }
    
    /**
     * Returns the RatingValue
     * @return The RatingValue
     */ 
     public float getRatingValue()
     {
         return _fRatingValue;
     }
 
    /**
     * Sets the RatingValue
     * @param dRatingValue The RatingValue
     */ 
     public void setRatingValue( float fRatingValue )
     {
         _fRatingValue = fRatingValue;
     }

    /**
     * Returns the RatingType
     * @return The RatingType
     */ 
     public String getRatingType()
     {
         return _strRatingType;
     }
 
    /**
     * Sets the RatingType
     * @param strRatingType The RatingType
     */ 
     public void setRatingType( String strRatingType )
     {
         _strRatingType = strRatingType;
     }
	
	public LuteceUser getUser( ) {
    	
		return _user;
	}
	/**
	 * Set the Lutece User
	 * @param user the lutece user
	 */
	public void setUser(LuteceUser user) {		
		_user= user;
		this.setUserGuid(user!=null? user.getName( ):null);
	}
	/**
	 * Set History properties
	 * @param history the history properties
	 */
	public void setHistory( ResourceExtenderHistory history) {
		
		this.setIdHistory(history.getIdHistory( ));
		this.setExtendableResourceType(history.getExtendableResourceType( ));
		this.setIdExtendableResource(history.getIdExtendableResource( ));
		this.setUserGuid(history.getUserGuid( ));
		this.setDateCreation(history.getDateCreation( ).isPresent()?history.getDateCreation( ).get():null);
		
	}
	/**
	 * Get the default template to use for rating resource 
	 * @return the defautl template
	 */
	public static String getTemplateContent(  ){
			
		return RatingUtils.TEMPLATE_DEFAULT_RATING_TYPE; 
	}
}
