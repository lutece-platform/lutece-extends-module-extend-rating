/*
 * Copyright (c) 2002-2026, Mairie de Paris
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Timestamp;

import org.junit.jupiter.api.Test;

import fr.paris.lutece.test.LuteceTestCase;
import jakarta.inject.Inject;
import jakarta.inject.Named;

/**
 * Storage of the configuration of a rating extender
 */
public class RatingExtenderConfigDAOTest extends LuteceTestCase
{
    private static final int ID_EXTENDER = 987650;

    @Inject
    @Named( "extend-rating.ratingExtenderConfigDAO" )
    private RatingExtenderConfigDAO _dao;

    /**
     * Every field is written at the creation (a new extender copies the default configuration), and the limit of votes is read back
     */
    @Test
    public void testInsertAndLoadEveryField( )
    {
        RatingExtenderConfig config = new RatingExtenderConfig( );
        config.setIdExtender( ID_EXTENDER );
        config.setRatingType( "ThumbRating" );
        config.setNbDaysToVote( 0 );
        config.setUniqueVote( false );
        config.setLimitVote( true );
        config.setNbVotePerUser( 2 );
        config.setLimitedConnectedUser( true );
        config.setDeleteVote( true );
        Timestamp dateStart = Timestamp.valueOf( "2027-03-05 00:00:00" );
        Timestamp dateEnd = Timestamp.valueOf( "2027-11-17 00:00:00" );
        config.setDateStart( dateStart );
        config.setDateEnd( dateEnd );
        _dao.insert( config );
        try
        {
            RatingExtenderConfig loaded = _dao.load( ID_EXTENDER );
            assertEquals( 2, loaded.getNbVotePerUser( ) );
            assertTrue( loaded.isLimitVote( ) );
            assertTrue( loaded.isLimitedConnectedUser( ) );
            assertTrue( loaded.isDeleteVote( ) );
            assertEquals( dateStart, loaded.getDateStart( ) );
            assertEquals( dateEnd, loaded.getDateEnd( ) );

            loaded.setLimitVote( false );
            _dao.store( loaded );
            loaded = _dao.load( ID_EXTENDER );
            assertFalse( loaded.isLimitVote( ) );
            assertEquals( 0, loaded.getNbVotePerUser( ) );
        }
        finally
        {
            _dao.delete( ID_EXTENDER );
        }
    }
}
