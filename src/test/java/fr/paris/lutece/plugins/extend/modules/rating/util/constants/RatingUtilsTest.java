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
package fr.paris.lutece.plugins.extend.modules.rating.util.constants;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Locale;

import org.junit.jupiter.api.Test;

import fr.paris.lutece.plugins.extend.modules.rating.business.SimpleRating;
import fr.paris.lutece.plugins.extend.modules.rating.business.StarRating;
import fr.paris.lutece.plugins.extend.modules.rating.business.ThumbRating;

/**
 * Tests the rating utilities.
 */
public class RatingUtilsTest
{
    /**
     * Each rating type renders its own template, an unknown one the default template.
     */
    @Test
    public void testGetRatingTemplatePath( )
    {
        assertEquals( RatingUtils.TEMPLATE_SIMPLE_RATING_TYPE, RatingUtils.getRatingTemplatePaht( SimpleRating.class.getName( ) ), "simple" );
        assertEquals( RatingUtils.TEMPLATE_STAR_RATING_TYPE, RatingUtils.getRatingTemplatePaht( StarRating.class.getName( ) ), "star" );
        assertEquals( RatingUtils.TEMPLATE_THUMB_RATING_TYPE, RatingUtils.getRatingTemplatePaht( ThumbRating.class.getName( ) ), "thumb" );
        assertEquals( RatingUtils.TEMPLATE_DEFAULT_RATING_TYPE, RatingUtils.getRatingTemplatePaht( "unknown" ), "default" );
    }

    /**
     * The show parameter is read from the extender parameters, quoted or not.
     */
    @Test
    public void testFetchShowParameter( )
    {
        assertEquals( "all", RatingUtils.fetchShowParameter( "{\"show\":\"all\"}" ), "quoted key" );
        assertEquals( "vote", RatingUtils.fetchShowParameter( "{show:\"vote\"}" ), "unquoted key" );
        assertEquals( "", RatingUtils.fetchShowParameter( null ), "no parameter" );
        assertEquals( "", RatingUtils.fetchShowParameter( "{\"other\":\"x\"}" ), "no show key" );
    }

    /**
     * A score keeps one decimal at most and drops a trailing zero.
     */
    @Test
    public void testFormatScore( )
    {
        assertEquals( "1", RatingUtils.formatScore( 1.0, Locale.FRENCH ), "whole" );
        assertEquals( "3,5", RatingUtils.formatScore( 3.5000000000000004, Locale.FRENCH ), "average" );
        assertEquals( "-2", RatingUtils.formatScore( -2.0, Locale.FRENCH ), "negative" );
        assertEquals( "1250", RatingUtils.formatScore( 1250.0, Locale.FRENCH ), "no grouping" );
    }
}
