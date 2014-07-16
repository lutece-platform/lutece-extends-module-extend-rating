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
package fr.paris.lutece.plugins.extend.modules.rating.business.config;

import fr.paris.lutece.plugins.extend.business.extender.config.ExtenderConfig;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;


/**
 * RatingExtenderConfig
 */
public class RatingExtenderConfig extends ExtenderConfig
{
    private int _nIdMailingList = -1;
    private int _nIdVoteType = 1;
    private boolean _bUniqueVote;
    private boolean _bLimitVote;
    private boolean _bLimitedConnectedUser;
    private boolean _bDeleteVote;
    private int _nNbDaysToVote;
    private int _nNbVotePerUser;
    private Timestamp _dateStart;
    private Timestamp _dateEnd;

    /**
     * @return the nIdMailingList
     */
    public int getIdMailingList( )
    {
        return _nIdMailingList;
    }

    /**
     * @param nIdMailingList the nIdMailingList to set
     */
    public void setIdMailingList( int nIdMailingList )
    {
        _nIdMailingList = nIdMailingList;
    }

    /**
     * @return the nIdVoteType
     */
    public int getIdVoteType( )
    {
        return _nIdVoteType;
    }

    /**
     * @param nIdVoteType the nIdVoteType to set
     */
    public void setIdVoteType( int nIdVoteType )
    {
        _nIdVoteType = nIdVoteType;
    }

    /**
     * @return the bUniqueVote
     */
    public boolean isUniqueVote( )
    {
        return _bUniqueVote;
    }

    /**
     * @param bUniqueVote the bUniqueVote to set
     */
    public void setUniqueVote( boolean bUniqueVote )
    {
        _bUniqueVote = bUniqueVote;
    }

    /**
     * This number of days before one can vote again.
     * This attribute is used only if the vote type is temporal (!= unique).
     * @return the nNbDaysToVote
     */
    public int getNbDaysToVote( )
    {
        return _nNbDaysToVote;
    }

    /**
     * This number of days before one can vote again.
     * This attribute is used only if the vote type is temporal (!= unique). <br />
     * Set 0 if there are no limits.
     * @param nNbDaysToVote the nNbDaysToVote to set
     */
    public void setNbDaysToVote( int nNbDaysToVote )
    {
        _nNbDaysToVote = nNbDaysToVote;
    }

    /**
     * Checks if it is an unlimited vote system.
     * To enable this system, the extension must have the option "Temporal vote"
     * and have 0 day as the number of day to vote.
     * 
     * @return true, if is unlimited vote
     */
    public boolean isUnlimitedVote( )
    {
        return !_bUniqueVote && ( _nNbDaysToVote == 0 );
    }

    /**
     * @return the _nNbVotePerUser
     */
    public int getNbVotePerUser( )
    {
        return _nNbVotePerUser;
    }

    /**
     * @param nNbVotePerUser the _nNbVotePerUser to set
     */
    public void setNbVotePerUser( int nNbVotePerUser )
    {
        this._nNbVotePerUser = nNbVotePerUser;
    }

    /**
     * @return the _bLimitVote
     */
    public boolean isLimitVote( )
    {
        return _bLimitVote;
    }

    /**
     * @param bLimitVote the _bLimitVote to set
     */
    public void setLimitVote( boolean bLimitVote )
    {
        this._bLimitVote = bLimitVote;
    }

    /**
     * @return the _bLimitedConnectedUser
     */
    public boolean isLimitedConnectedUser( )
    {
        return _bLimitedConnectedUser;
    }

    /**
     * @param bLimitedConnectedUser the _bLimitedConnectedUser to set
     */
    public void setLimitedConnectedUser( boolean bLimitedConnectedUser )
    {
        this._bLimitedConnectedUser = bLimitedConnectedUser;
    }

    /**
     * @return the _bDeleteVote
     */
    public boolean isDeleteVote( )
    {
        return _bDeleteVote;
    }

    /**
     * @param bDeleteVote the _bDeleteVote to set
     */
    public void setDeleteVote( boolean bDeleteVote )
    {
        this._bDeleteVote = bDeleteVote;
    }

    /**
     * @return the _dateStart
     */
    public Timestamp getDateStart( )
    {
        return _dateStart;
    }

    /**
     * @param dateStart the _dateStart to set
     */
    public void setDateStart( Timestamp dateStart )
    {
        this._dateStart = dateStart;
    }

    /**
     * @return the _dateEnd
     */
    public Timestamp getDateEnd( )
    {
        return _dateEnd;
    }

    /**
     * @param dateEnd the _dateEnd to set
     */
    public void setDateEnd( Timestamp dateEnd )
    {
        this._dateEnd = dateEnd;
    }

    /**
     * @return the _dateEnd
     */
    public String getDateEndString( )
    {
        if ( _dateEnd != null )
        {
            return new SimpleDateFormat( "dd/MM/yyyy" ).format( _dateEnd );
        }
        return null;
    }

    /**
     * @return the _dateStart
     */
    public String getDateStartString( )
    {
        if ( _dateStart != null )
        {
            return new SimpleDateFormat( "dd/MM/yyyy" ).format( _dateStart );
        }
        return null;
    }
}
