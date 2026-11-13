/*
 * Copyright (c) 2002-2026, City of Paris
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
package fr.paris.lutece.plugins.extend.modules.rating.web;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.junit.jupiter.api.Test;

import freemarker.cache.FileTemplateLoader;
import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.StringTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.core.HTMLOutputFormat;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;

/**
 * Renders the module-extend-rating templates migrated to HTML auto-escaping (LUT-33153): the vote forms post to the
 * action URL written once escaped, their params maps write the vote values, the data around them is escaped; the
 * configuration form checks its options from the booleans of the configuration (no captured block read as a boolean)
 * and writes its numbers with ?c, whatever the locale. The core macros the templates call are replaced by stand-ins
 * that write their arguments (escaped) and their nested content: only the FreeMarker engine is needed.
 */
public class RatingTemplatesRenderTest
{
    private static final String TEMPLATES_ROOT = "webapp/WEB-INF/templates";
    private static final String TEMPLATE_STAR = "skin/plugins/extend/modules/rating/extend_rating_star_type.html";
    private static final String TEMPLATE_THUMB = "skin/plugins/extend/modules/rating/extend_rating_thumb_type.html";
    private static final String TEMPLATE_SIMPLE = "skin/plugins/extend/modules/rating/extend_rating_simple_type.html";
    private static final String TEMPLATE_CONFIG = "admin/plugins/extend/modules/rating/rating_config.html";

    /** Stand-ins of the core macros the templates call. The macros taking params write them, which must be a map. */
    private static final String MACROS = "macros.ftl";
    private static final String MACROS_SOURCE = "<#macro attrs params><#list params as key, param> ${key}=\"${param}\"</#list></#macro>"
            + "<#macro cText rest...><p><#nested></p></#macro>"
            + "<#macro cImg src='' alt='' id='' params={} rest...><img src=\"${src}\" alt=\"${alt}\" id=\"${id}\"<@attrs params />></#macro>"
            + "<#macro cForm action='' rest...><form action=\"${action}\"><#nested></form></#macro>"
            + "<#macro cInput name='' value='' rest...><input name=\"${name}\" value=\"${value}\"></#macro>"
            + "<#macro cBtn label='' params={} rest...><button<@attrs params />>${label}<#nested></button></#macro>"
            + "<#macro row rest...><#nested></#macro><#macro columns rest...><#nested></#macro>"
            + "<#macro formGroup id='' rest...><div class=\"form-group\" id=\"${id}\"><#nested></div></#macro>"
            + "<#macro inputGroup rest...><#nested></#macro><#macro inputGroupItem rest...><#nested></#macro><#macro icon rest...><i></i></#macro>"
            + "<#macro select name='' default_value='' rest...><select name=\"${name}\" data-default=\"${default_value}\"></select></#macro>"
            + "<#macro input name='' value='' rest...><input name=\"${name}\" value=\"${value}\"></#macro>"
            + "<#macro radioButton id='' value='' checked=false rest...><input type=\"radio\" id=\"${id}\" value=\"${value}\"<#if checked> checked</#if>></#macro>"
            + "<#macro checkBox id='' checked=false rest...><input type=\"checkbox\" id=\"${id}\"<#if checked> checked</#if>></#macro>"
            + "<#macro extendScript><script><#nested></script></#macro>";

    private static final String HOSTILE_ID = "42\"><script>";
    private static final String HOSTILE_ID_ESCAPED = "42&quot;&gt;&lt;script&gt;";

    /**
     * Renders a template of the module with the core configuration of the output format, in French: a number written
     * without ?c would get a decimal comma and a grouping space.
     *
     * @param strTemplate
     *            the template path
     * @param model
     *            the model
     * @return the HTML
     * @throws IOException
     *             if the template cannot be read
     * @throws TemplateException
     *             if the rendering fails
     */
    private static String render( String strTemplate, Map<String, Object> model ) throws IOException, TemplateException
    {
        StringTemplateLoader macros = new StringTemplateLoader( );
        macros.putTemplate( MACROS, MACROS_SOURCE );
        Configuration cfg = new Configuration( Configuration.VERSION_2_3_0 );
        cfg.setOutputFormat( HTMLOutputFormat.INSTANCE );
        cfg.setAutoEscapingPolicy( Configuration.ENABLE_IF_DEFAULT_AUTO_ESCAPING_POLICY );
        cfg.setLocale( Locale.FRANCE );
        cfg.setTemplateLoader( new MultiTemplateLoader( new TemplateLoader [ ] {
                macros, new FileTemplateLoader( new File( TEMPLATES_ROOT ) )
        } ) );
        cfg.addAutoInclude( MACROS );
        StringWriter writer = new StringWriter( );
        cfg.getTemplate( strTemplate ).process( model, writer );

        return writer.toString( );
    }

    /**
     * The model of a front office vote form.
     *
     * @param rating
     *            the rating of the resource
     * @return the model
     */
    private static Map<String, Object> voteModel( Map<String, Object> rating )
    {
        Map<String, Object> model = new HashMap<>( );
        model.put( "show", "all" );
        model.put( "rating", rating );
        model.put( "ratingList", List.of( Map.of( "ratingValue", 2.5 ) ) );
        model.put( "canVote", true );
        model.put( "canDeleteVote", true );
        model.put( "voteClosed", false );
        model.put( "idExtendableResource", HOSTILE_ID );
        model.put( "extendableResourceType", "PAGE" );
        model.put( "voteToken", "tok" );
        model.put( "cancelVoteToken", "ctok" );

        return model;
    }

    /**
     * The star vote form posts to the action URL escaped once, writes each vote value through the params map of its
     * button, and escapes the resource id.
     *
     * @throws Exception
     *             if the rendering fails
     */
    @Test
    public void testStarVoteForm( ) throws Exception
    {
        String strHtml = render( TEMPLATE_STAR,
                voteModel( Map.of( "averageScore", 2.5, "averageScoreRoundToHalf", 2.5, "ratingCount", 1200 ) ) );

        assertTrue( strHtml.contains( "<form action=\"jsp/site/Portal.jsp?page=extend-rating&amp;action=doRating\">" ), strHtml );
        assertTrue( strHtml.contains( "<form action=\"jsp/site/Portal.jsp?page=extend-rating&amp;action=doCancelRating\">" ), strHtml );
        assertFalse( strHtml.contains( "&amp;amp;" ), strHtml );
        assertTrue( strHtml.contains( "<button name=\"ratingValue\" value=\"0.5\"" ), strHtml );
        assertTrue( strHtml.contains( "<button name=\"ratingValue\" value=\"4\"" ), strHtml );
        assertTrue( strHtml.contains( "src=\"images/local/skin/plugins/extend/modules/rating/stars_2,5.png\"" ), strHtml );
        assertTrue( strHtml.contains( "<input name=\"ratingValue\" value=\"2.5\">" ), strHtml );
        assertTrue( strHtml.contains( "value=\"" + HOSTILE_ID_ESCAPED + "\"" ), strHtml );
        assertFalse( strHtml.contains( "<script>" ), strHtml );
    }

    /**
     * The thumb vote form writes the vote values through the params map of its buttons and escapes the resource id.
     *
     * @throws Exception
     *             if the rendering fails
     */
    @Test
    public void testThumbVoteForm( ) throws Exception
    {
        String strHtml = render( TEMPLATE_THUMB,
                voteModel( Map.of( "scoreValue", 3, "ratingCount", 5, "scoreLike", 4, "scoreDislike", 1, "ratingValue", 1 ) ) );

        assertTrue( strHtml.contains( "<form action=\"jsp/site/Portal.jsp?page=extend-rating&amp;action=doRating\">" ), strHtml );
        assertTrue( strHtml.contains( "<button name=\"ratingValue\" value=\"-1\" title=\"#i18n{module.extend.rating.rating.labelVoteAgainst}\">" ),
                strHtml );
        assertTrue( strHtml.contains( "<button name=\"ratingValue\" value=\"1\" title=\"#i18n{module.extend.rating.rating.labelVoteFor}\">" ), strHtml );
        assertTrue( strHtml.contains( "id=\"extend-rating-score-" + HOSTILE_ID_ESCAPED + "\"" ), strHtml );
        assertFalse( strHtml.contains( "<script>" ), strHtml );
    }

    /**
     * The simple vote form posts to the action URL escaped once.
     *
     * @throws Exception
     *             if the rendering fails
     */
    @Test
    public void testSimpleVoteForm( ) throws Exception
    {
        Map<String, Object> model = voteModel( Map.of( "ratingCount", 3 ) );
        model.put( "canDeleteVote", false );

        String strHtml = render( TEMPLATE_SIMPLE, model );

        assertTrue( strHtml.contains( "<form action=\"jsp/site/Portal.jsp?page=extend-rating&amp;action=doRating\">" ), strHtml );
        assertFalse( strHtml.contains( "&amp;amp;" ), strHtml );
    }

    /**
     * The model of the configuration form.
     *
     * @param nNbVotePerUser
     *            the number of votes per user, 0 for no limit
     * @param bConnectedOnly
     *            whether only signed-in users vote, and may delete their vote
     * @return the model
     */
    private static Map<String, Object> configModel( int nNbVotePerUser, boolean bConnectedOnly )
    {
        Map<String, Object> config = new HashMap<>( );
        config.put( "ratingType", "fr.paris.lutece.plugins.extend.modules.rating.business.StarRating" );
        config.put( "idMailingList", 1500 );
        config.put( "dateStartString", "2026-11-13" );
        config.put( "dateEndString", "" );
        config.put( "uniqueVote", false );
        config.put( "nbDaysToVote", 1000 );
        config.put( "limitedConnectedUser", bConnectedOnly );
        config.put( "deleteVote", bConnectedOnly );
        config.put( "nbVotePerUser", nNbVotePerUser );

        Map<String, Object> model = new HashMap<>( );
        model.put( "ratingConfig", config );
        model.put( "listIdsVoteType", List.of( ) );
        model.put( "listIdsMailingList", List.of( ) );

        return model;
    }

    /**
     * The configuration form checks its options from the configuration and writes its numbers with ?c.
     *
     * @throws Exception
     *             if the rendering fails
     */
    @Test
    public void testConfigurationForm( ) throws Exception
    {
        String strHtml = render( TEMPLATE_CONFIG, configModel( 0, true ) );

        assertTrue( strHtml.contains( "<select name=\"idMailingList\" data-default=\"1500\">" ), strHtml );
        assertTrue( strHtml.contains( "<input name=\"nbDaysToVote\" value=\"1000\">" ), strHtml );
        assertTrue( strHtml.contains( "<input name=\"nbVotePerUser\" value=\"0\">" ), strHtml );
        assertTrue( strHtml.contains( "id=\"extend-rating-temporal-vote\" value=\"false\" checked>" ), strHtml );
        assertTrue( strHtml.contains( "id=\"extend-rating-unique-vote\" value=\"true\">" ), strHtml );
        assertTrue( strHtml.contains( "id=\"extend-rating-limited-connected-user-vote\" checked>" ), strHtml );
        assertTrue( strHtml.contains( "id=\"deleteVote\" checked>" ), strHtml );
        assertTrue( strHtml.contains( "id=\"extend-rating-no-limit-vote-user\" value=\"false\" checked>" ), strHtml );
        assertTrue( strHtml.contains( "id=\"extend-rating-limit-vote-user\" value=\"true\">" ), strHtml );

        strHtml = render( TEMPLATE_CONFIG, configModel( 12, false ) );

        assertTrue( strHtml.contains( "<input name=\"nbVotePerUser\" value=\"12\">" ), strHtml );
        assertTrue( strHtml.contains( "id=\"extend-rating-limited-connected-user-vote\">" ), strHtml );
        assertTrue( strHtml.contains( "id=\"deleteVote\">" ), strHtml );
        assertTrue( strHtml.contains( "id=\"extend-rating-no-limit-vote-user\" value=\"false\">" ), strHtml );
        assertTrue( strHtml.contains( "id=\"extend-rating-limit-vote-user\" value=\"true\" checked>" ), strHtml );
    }
}
