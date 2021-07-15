--
-- Init  table core_admin_right
--
INSERT INTO core_admin_right (id_right,name,level_right,admin_url,description,is_updatable,plugin_name,id_feature_group,icon_url, documentation_url) 
VALUES ('EXTEND_VOTE_TYPES_MANAGEMENT','module.extend.rating.adminFeature.vote_types_management.name',2,'jsp/admin/plugins/extend/modules/rating/ManageVoteTypes.jsp','module.extend.rating.adminFeature.vote_types_management.description',0,'extend-rating','CONTENT','images/admin/skin/plugins/extend/modules/rating/extend-rating.png', 'jsp/admin/documentation/AdminDocumentation.jsp?doc=admin-extend-rating');

--
-- Init  table core_user_right
--
INSERT INTO core_user_right (id_right,id_user) VALUES ('EXTEND_VOTE_TYPES_MANAGEMENT',1);
INSERT INTO core_user_right (id_right,id_user) VALUES ('EXTEND_VOTE_TYPES_MANAGEMENT',2);

--
-- Init core_template
--

INSERT INTO core_template VALUES ('extend_rating_vote_type_star',
'<#assign averageScore = 0 />
<#assign averageScoreRoundToHalf = 0 />
<#assign voteCount = 0 />
<#if rating??>
    <#assign averageScore = rating.averageScore />
    <#assign averageScoreRoundToHalf = rating.averageScoreRoundToHalf />
    <#assign voteCount = rating.voteCount />
</#if>
<#if show == \"all\" || show == \"vote\">
    <p>
        <img src=\"images/local/skin/plugins/extend/modules/rating/stars_${averageScoreRoundToHalf!}.png\" alt=\"Note : ${averageScore!}\" title=\"Note : ${averageScore!}\" />
        (${voteCount!}) 
    </p>
</#if>
<#if canDeleteVote><div class=\"cancel\"><a href=\"jsp/site/plugins/extend/modules/rating/DoCancelVote.jsp?idExtendableResource=${idExtendableResource!}&extendableResourceType=${extendableResourceType!}\" > </a></div></#if>
<#if show == \"all\" || show == \"actionVote\">
    <#if canVote>
        <div> Votez : </div>
        <div class=\"resource-vote-star-rating\" style=\"display:none;\">
            <form name=\"resource_vote_form\" action=\"jsp/site/plugins/extend/modules/rating/DoVote.jsp?idExtendableResource=${idExtendableResource!}&extendableResourceType=${extendableResourceType}\" method=\"post\" >
                <input type=\"hidden\" name=\"voteValue\" value=\"1\" />
                <input type=\"hidden\" id=\"ratingType\" name=\"ratingType\" value=\"extend_rating_vote_type_star\"/>
                <input class=\"star-rating {split:2}\" type=\"radio\" name=\"voteValue_${extendableResourceType}_${idExtendableResource!}\" value=\"0.5\" />
                <input class=\"star-rating {split:2}\" type=\"radio\" name=\"voteValue_${extendableResourceType}_${idExtendableResource!}\" value=\"1\" />
                <input class=\"star-rating {split:2}\" type=\"radio\" name=\"voteValue_${extendableResourceType}_${idExtendableResource!}\" value=\"1.5\" />
                <input class=\"star-rating {split:2}\" type=\"radio\" name=\"voteValue_${extendableResourceType}_${idExtendableResource!}\" value=\"2\" />
                <input class=\"star-rating {split:2}\" type=\"radio\" name=\"voteValue_${extendableResourceType}_${idExtendableResource!}\" value=\"2.5\" />
                <input class=\"star-rating {split:2}\" type=\"radio\" name=\"voteValue_${extendableResourceType}_${idExtendableResource!}\" value=\"3\" />
                <input class=\"star-rating {split:2}\" type=\"radio\" name=\"voteValue_${extendableResourceType}_${idExtendableResource!}\" value=\"3.5\" />
                <input class=\"star-rating {split:2}\" type=\"radio\" name=\"voteValue_${extendableResourceType}_${idExtendableResource!}\" value=\"4\" />
            </form>
        </div>
        <div class=\"resource-vote-star-rating-javascript-disable\" >
            <div class=\"star {split:2}\">
                <a href=\"jsp/site/plugins/extend/modules/rating/DoVote.jsp?idExtendableResource=${idExtendableResource!}&extendableResourceType=${extendableResourceType!}&voteValue=0.5\" ></a>
            </div >
            <div class=\"star {split:2}\">
                <a href=\"jsp/site/plugins/extend/modules/rating/DoVote.jsp?idExtendableResource=${idExtendableResource!}&extendableResourceType=${extendableResourceType!}&voteValue=1\" > </a>
            </div>
            <div class=\"star {split:2}\">
                <a href=\"jsp/site/plugins/extend/modules/rating/DoVote.jsp?idExtendableResource=${idExtendableResource!}&extendableResourceType=${extendableResourceType!}&voteValue=1.5\" > </a>
            </div>
            <div class=\"star {split:2}\">
                <a href=\"jsp/site/plugins/extend/modules/rating/DoVote.jsp?idExtendableResource=${idExtendableResource!}&extendableResourceType=${extendableResourceType!}&voteValue=2\" > </a>
            </div>
            <div class=\"star {split:2}\">
                <a href=\"jsp/site/plugins/extend/modules/rating/DoVote.jsp?idExtendableResource=${idExtendableResource!}&extendableResourceType=${extendableResourceType!}&voteValue=2.5\" ></a>
            </div >
            <div class=\"star {split:2}\">
                <a href=\"jsp/site/plugins/extend/modules/rating/DoVote.jsp?idExtendableResource=${idExtendableResource!}&extendableResourceType=${extendableResourceType!}&voteValue=3\" > </a>
            </div>
            <div class=\"star {split:2}\">
                <a href=\"jsp/site/plugins/extend/modules/rating/DoVote.jsp?idExtendableResource=${idExtendableResource!}&extendableResourceType=${extendableResourceType!}&voteValue=3.5\" > </a>
            </div>
            <div class=\"star {split:2}\">
                <a href=\"jsp/site/plugins/extend/modules/rating/DoVote.jsp?idExtendableResource=${idExtendableResource!}&extendableResourceType=${extendableResourceType!}&voteValue=4\" > </a>
            </div>
        </div>
        <br/>
    </#if>
</#if>'
);
INSERT INTO core_template VALUES ('extend_rating_vote_type_thumb',
'<#assign scoreValue = 0 />
<#assign voteCount = 0 />
<#if rating??>
    <#assign scoreValue = rating.scoreValue />
    <#assign voteCount = rating.voteCount />
</#if>
<#if show == \"all\" || show == \"vote\">
    <p>
        <#if ( scoreValue < 0 )>
            <img src=\"images/local/skin/plugins/extend/modules/rating/vote_against.png\" title=\"Voter contre\" alt=\"Voter contre\"/>
            ${scoreValue!}
        <#else>
            <img src=\"images/local/skin/plugins/extend/modules/rating/vote_for.png\" title=\"Note\" alt=\"Note\"/>
            ${scoreValue!}
        </#if>
        (${voteCount!}) 
    </p>
    <p> J''aime : ${scoreValue!}</p>
    <p> J''aime pas : ${scoreValue!}</p>
    <#if voteValue?? && voteValue != 0>		<p> Votre dernier vote : 		<#if voteValue == 1>
            <img src=\"images/local/skin/plugins/extend/modules/rating/vote_for.png\" title=\"Note\" alt=\"Note\"/>		</#if>		<#if voteValue ==-1>
            <img src=\"images/local/skin/plugins/extend/modules/rating/vote_against.png\" title=\"Voter contre\" alt=\"Voter contre\"/>		</#if>		</p>
    </#if></#if>
<#if canDeleteVote><div class=\"cancel\"><a href=\"jsp/site/plugins/extend/modules/rating/DoCancelVote.jsp?idExtendableResource=${idExtendableResource!}&extendableResourceType=${extendableResourceType!}\" > </a></div></#if>
<#if show == \"all\" || show == \"actionVote\">
    <#if canVote>
        <div>
            <div class=\"extend-rating-vote-title\">
                <label for=\"\">Votez :</label></div>
            <span class=\"extend-rating-vote-span\">
		<#if (voteValue?? && voteValue=1) || voteValue=0 ><a href=\"jsp/site/plugins/extend/modules/rating/DoVote.jsp?idExtendableResource=${idExtendableResource!}&extendableResourceType=${extendableResourceType!}&voteValue=-1&ratingType=extend_rating_vote_type_thumb\">
                <img src=\"images/local/skin/plugins/extend/modules/rating/vote_against.png\" title=\"Voter contre\" alt=\"Voter contre\"/>
            </a>
        </#if><#if (voteValue?? && voteValue=-1) || voteValue=0 ><a href=\"jsp/site/plugins/extend/modules/rating/DoVote.jsp?idExtendableResource=${idExtendableResource!}&extendableResourceType=${extendableResourceType!}&voteValue=1&ratingType=extend_rating_vote_type_thumb\">
                        <img src=\"images/local/skin/plugins/extend/modules/rating/vote_for.png\" title=\"Voter pour\" alt=\"Voter pour\"/>
                    </a>
                </#if></span>
        </div>
    </#if>
</#if>'
);

INSERT INTO core_template VALUES ('extend_rating_vote_type_simple',
'<#if !voteClosed>
    <#if canDeleteVote>
        <div>
            <a href=\"jsp/site/plugins/extend/modules/rating/DoCancelVote.jsp?idExtendableResource=${idExtendableResource!}&extendableResourceType=${extendableResourceType!}\" >J''annule mon vote</a>
        </div>
    </#if>
    <#if show == \"all\" || show == \"actionVote\">
        <#if canVote>
            <div>
            <div class=\"extend-rating-vote-title\">
                <label for=\"\">Votez :</label>
            </div>
            <span>
                <a href=\"jsp/site/plugins/extend/modules/rating/DoVote.jsp?idExtendableResource=${idExtendableResource!}&extendableResourceType=${extendableResourceType!}&voteValue=1&ratingType=extend_rating_vote_type_simple\">Je vote pour</a>
            </span>
            </div>
        </#if>
    </#if>
</#if>');

INSERT INTO core_template VALUES ('extend_rating_vote_type_majority',
'<#assign averageScore = 0 />
<#assign averageScoreRoundToHalf = 0 />
<#assign voteCount = 0 />
<#if rating??>
    <#assign averageScore = rating.averageScore />
    <#assign averageScoreRoundToHalf = rating.averageScoreRoundToHalf />
    <#assign voteCount = rating.voteCount />
    <#assign scoreValue = rating.scoreValue />
</#if>
<#if canDeleteVote><div class=\"cancel\"><a href=\"jsp/site/plugins/extend/modules/rating/DoCancelVote.jsp?idExtendableResource=${idExtendableResource!}&extendableResourceType=${extendableResourceType!}\" > </a></div></#if>
<#if show == \"all\" || show == \"vote\">
    <p>
        Mention choisie : ${voteValue!}
    </p>
</#if>
<#if show == \"all\" || show == \"actionVote\">
    <#if canVote>
        <div> Votez : </div>
        <div>
            <form name=\"resource_vote_form\" action=\"jsp/site/plugins/extend/modules/rating/DoVote.jsp\" method=\"get\" >
                <input type=\"hidden\" id=\"idExtendableResource\" name=\"idExtendableResource\" value=\"${idExtendableResource!}\"/>
                <input type=\"hidden\" id=\"extendableResourceType\" name=\"extendableResourceType\" value=\"${extendableResourceType!}\"/>
                <input type=\"hidden\" id=\"ratingType\" name=\"ratingType\" value=\"extend_rating_vote_type_majority\"/>
                <input type=\"radio\" id=\"voteValue_${extendableResourceType}_${idExtendableResource!}\" name=\"voteValue\" value=\"0\" onchange=\"this.form.submit()\" />
                <label>Mention0</label><br>
                <input type=\"radio\" id=\"voteValue_${extendableResourceType}_${idExtendableResource!}\" name=\"voteValue\" value=\"1\" onchange=\"this.form.submit()\" />
                <label>Mention1</label><br>
                <input type=\"radio\" id=\"voteValue_${extendableResourceType}_${idExtendableResource!}\" name=\"voteValue\" value=\"2\" onchange=\"this.form.submit()\" />
                <label>Mention2</label><br>
                <input type=\"radio\" id=\"voteValue_${extendableResourceType}_${idExtendableResource!}\" name=\"voteValue\" value=\"3\" onchange=\"this.form.submit()\" />
                <label>Mention3</label><br>
                <input type=\"radio\" id=\"voteValue_${extendableResourceType}_${idExtendableResource!}\" name=\"voteValue\" value=\"4\" onchange=\"this.form.submit()\" />
                <label>Mention4</label><br>
                <input type=\"radio\" id=\"voteValue_${extendableResourceType}_${idExtendableResource!}\" name=\"voteValue\" value=\"5\" onchange=\"this.form.submit()\" />
                <label>Mention5</label><br>
                <input type=\"radio\" id=\"voteValue_${extendableResourceType}_${idExtendableResource!}\" name=\"voteValue\" value=\"6\" onchange=\"this.form.submit()\" />
                <label>Mention6</label><br>
            </form>
        </div>
        <br/>
    </#if>
</#if>');
