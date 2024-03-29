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
package fr.paris.lutece.plugins.extend.modules.rating.util.constants;


/**
 *
 * CommentConstants
 *
 */
public final class RatingConstants
{
    // PROPERTIES
    public static final String PROPERTY_RATING_CONFIG_LABEL_NO_MAILING_LIST = "module.extend.rating.rating_config.labelNoMailingList";
    public static final String PROPERTY_WEBMASTER_EMAIL = "email.webmaster";
    public static final String PROPERTY_LUTECE_NAME = "lutece.name";
    public static final String PROPERTY_MANAGE_VOTE_TYPES_PAGE_TITLE = "module.extend.rating.manage_vote_types.pageTitle";

    // MESSAGES
    public static final String MESSAGE_NOTIFY_SUBJECT = "module.extend.rating.message.notify.subject";
    public static final String MESSAGE_ERROR_GENERIC_MESSAGE = "module.extend.rating.message.error.genericMessage";
    public static final String MESSAGE_STOP_GENERIC_MESSAGE = "module.extend.rating.message.stop.genericMessage";
    public static final String MESSAGE_CANNOT_VOTE = "module.extend.rating.message.cannotVote";

    // PARAMETERS
    public static final String PARAMETER_ID_EXTENDABLE_RESOURCE = "idExtendableResource";
    public static final String PARAMETER_EXTENDABLE_RESOURCE_TYPE = "extendableResourceType";
    public static final String PARAMETER_VOTE_VALUE = "ratingValue";
    public static final String PARAMETER_RATING_TYPE = "ratingType";
    public static final String PARAMETER_HTTP_REFERER = "referer";
    public static final String PARAMETER_ID_VOTE_TYPE = "idVoteType";
    public static final String PARAMETER_CANCEL = "cancel";

    // BEANS
    public static final String BEAN_CONFIG_SERVICE = "extend-rating.ratingExtenderConfigService";

    // MARKS
    public static final String MARK_RATING = "rating";
    public static final String MARK_RATING_LIST = "ratingList";
    public static final String MARK_ID_EXTENDABLE_RESOURCE = "idExtendableResource";
    public static final String MARK_EXTENDABLE_RESOURCE_TYPE = "extendableResourceType";
    public static final String MARK_RATING_CONFIG = "ratingConfig";
    public static final String MARK_LIST_IDS_MAILING_LIST = "listIdsMailingList";
    public static final String MARK_RESOURCE_EXTENDER_NAME = "resourceExtenderName";
    public static final String MARK_VOTE_VALUE = "ratingValue";
    public static final String MARK_LIST_IDS_VOTE_TYPE = "listIdsVoteType";
    public static final String MARK_RATING_HTML_CONTENT = "ratingHtmlContent";
    public static final String MARK_VOTE_TYPE = "voteType";
    public static final String MARK_LIST_VOTE_TYPES = "listVoteTypes";
    public static final String MARK_WEBAPP_URL = "webapp_url";
    public static final String MARK_LOCALE = "locale";
    public static final String MARK_SHOW = "show";
    public static final String MARK_CAN_VOTE = "canVote";
    public static final String MARK_CAN_DELETE_VOTE = "canDeleteVote";
    public static final String MARK_VOTE_CLOSED = "voteClosed";

    // CONSTANTS
    public static final String JSON_KEY_SHOW = "show";
    public static final String SHOW_ALL = "all";
    public static final String SHOW_VOTE = "vote";
    public static final String SHOW_VOTE_ACTION = "voteAction";
    public static final String PARAMETER_FROM_URL = "from_url";
    
    public static final String ORDER_BY_DATE_CREATION = " date_creation ";


    // RATING TYPES
    public static final String STAR_RATING = "extend_rating_vote_type_star";
    public static final String THUMB_RATING = "extend_rating_vote_type_thumb";
    public static final String SIMPLE_RATING = "extend_rating_vote_type_simple";
    public static final String MAJORITY_RATING = "extend_rating_vote_type_majority";

    
    /**
     * Instantiates a new comment constants.
     */
    private RatingConstants(  )
    {
    }
}
