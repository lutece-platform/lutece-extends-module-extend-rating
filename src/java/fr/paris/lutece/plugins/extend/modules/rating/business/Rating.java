/*
 * Copyright (c) 2002-2013, Mairie de Paris
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
public class Rating
{
    private int _nIdRating;
    @NotNull
    private String _strIdExtendableResource;
    @NotNull
    private String _strExtendableResourceType;
    private int _nVoteCount;
    private int _nScoreValue;

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
     * @return the nScoreValue
     */
    public int getScoreValue(  )
    {
        return _nScoreValue;
    }

    /**
     * @param nScoreValue the nScoreValue to set
     */
    public void setScoreValue( int nScoreValue )
    {
        _nScoreValue = nScoreValue;
    }

    /**
     * Calculate the score (min : 1 - max : 4).
     * 
     * @return the calculated score
     */
    public int getAverageScore(  )
    {
        int nScore = 0;

        if ( _nVoteCount > 0 )
        {
            float averageScore = (float) _nScoreValue / (float) _nVoteCount;
            nScore = Math.round( averageScore );
            // The score value interval is [-2, 2], so the score must add 2
            // in order to have the interval [0, 4]
            nScore = nScore + 2;

            // We also add 1 to the score if the average score is negative
            // n order to have the interval [1, 4]
            if ( averageScore < 0f )
            {
                nScore++;
            }
        }

        return nScore;
    }
}
