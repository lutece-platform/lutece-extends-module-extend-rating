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

import javax.validation.constraints.NotNull;


/**
 *
 * Rating
 *
 */
public abstract class Rating {
    protected int _nIdRating;
    @NotNull
    protected String _strIdExtendableResource;
    @NotNull
    protected String _strExtendableResourceType;
    @NotNull
    protected String _strRatingType;
    protected int _nVoteCount;
    protected double _dScoreValue;
    

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
     *
     * @return rating type
     */
    public String getRatingType() {
        return _strRatingType;
    }

    /**
     * @param ratingType the nIdRating to set
     */
    public void setRatingType(String ratingType) {
        _strRatingType = ratingType;
    }

    /**
    * @return the strIdExtendableResource
    */
    public String getIdExtendableResource(  )
    {
        return _strIdExtendableResource;
    }

    /**
     * @param strIdExtendableResource the strIdExtendableResource to set
     */
    public void setIdExtendableResource( String strIdExtendableResource )
    {
        _strIdExtendableResource = strIdExtendableResource;
    }

    /**
     * @return the extendableResourceType
     */
    public String getExtendableResourceType(  )
    {
        return _strExtendableResourceType;
    }

    /**
     * @param strExtendableResourceType the extendableResourceType to set
     */
    public void setExtendableResourceType( String strExtendableResourceType )
    {
        _strExtendableResourceType = strExtendableResourceType;
    }

    /**
     * @return the nVoteCount
     */
    public int getVoteCount(  )
    {
        return _nVoteCount;
    }

    /**
     * @param nVoteCount the nVoteCount to set
     */
    public void setVoteCount( int nVoteCount )
    {
        _nVoteCount = nVoteCount;
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
    public void setScoreValue( double dScoreValue )
    {
        _dScoreValue = dScoreValue;
    }

	/**
     * Calculate the score (min : 1 - max : 4).
     *
     * @return the average score
     */
    public double getAverageScore(  )
    {
        double dScore = 0;

        if ( _nVoteCount > 0 )
        {
            double averageScore = _dScoreValue / ( double ) _nVoteCount;
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

        if ( _nVoteCount > 0 )
        {
            double averageScore = _dScoreValue / (double) _nVoteCount;
            dScore =  Math.round( averageScore * 2 ) * 0.5;
        }

        return dScore;
    }
}
