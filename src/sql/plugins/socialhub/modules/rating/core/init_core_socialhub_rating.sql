--
-- Init  table core_admin_right
--
INSERT INTO core_admin_right (id_right,name,level_right,admin_url,description,is_updatable,plugin_name,id_feature_group,icon_url, documentation_url) 
VALUES ('SOCIALHUB_VOTE_TYPES_MANAGEMENT','module.socialhub.rating.adminFeature.vote_types_management.name',2,'jsp/admin/plugins/socialhub/modules/rating/ManageVoteTypes.jsp','module.socialhub.rating.adminFeature.vote_types_management.description',0,'socialhub-rating','CONTENT','images/admin/skin/plugins/socialhub/modules/rating/socialhub-rating.png', 'jsp/admin/documentation/AdminDocumentation.jsp?doc=admin-socialhub-rating');

--
-- Init  table core_user_right
--
INSERT INTO core_user_right (id_right,id_user) VALUES ('SOCIALHUB_VOTE_TYPES_MANAGEMENT',1);
INSERT INTO core_user_right (id_right,id_user) VALUES ('SOCIALHUB_VOTE_TYPES_MANAGEMENT',2);

--
-- Init core_template
--
INSERT INTO core_template VALUES ('socialhub_rating_vote_type_star', '
<#assign averageScore = 0 />
<#assign voteCount = 0 />
<#if rating??>
	<#assign averageScore = rating.averageScore />
	<#assign voteCount = rating.voteCount />
</#if>
<#if show == "all" || show == "vote">
	<p>
		<label for="">#i18n{module.socialhub.rating.rating.labelScore} : </label>
		<img src="images/local/skin/plugins/socialhub/modules/rating/stars_${averageScore!}.png" alt="#i18n{module.socialhub.rating.rating.labelScore}" title="#i18n{module.socialhub.rating.rating.labelScore}" />
		(${voteCount!})
	</p>
</#if>
<#if show == "all" || show == "actionVote">
	<#if canVote>
		<div>
			#i18n{module.socialhub.rating.rating.labelVote} :
		</div>
		<div class="resource-vote-star-rating" style="display:none;">
			<form name="resource_vote_form" action="jsp/site/plugins/socialhub/modules/rating/DoVote.jsp?idExtendableResource=${idExtendableResource!}&amp;extendableResourceType=${extendableResourceType}" method="post" >
				<input class="star-rating" type="radio" name="voteValue" value="-2" />
				<input class="star-rating" type="radio" name="voteValue" value="-1" />
				<input class="star-rating" type="radio" name="voteValue" value="1" />
				<input class="star-rating" type="radio" name="voteValue" value="2" />
			</form>
		</div>
		<div class="resource-vote-star-rating-javascript-disable" >
		 	<div class="star">
				<a href="jsp/site/plugins/socialhub/modules/rating/DoVote.jsp?idExtendableResource=${idExtendableResource!}&amp;extendableResourceType=${extendableResourceType!}&amp;voteValue=-2" >
				</a>
			</div >
			<div class="star">
				<a href="jsp/site/plugins/socialhub/modules/rating/DoVote.jsp?idExtendableResource=${idExtendableResource!}&amp;extendableResourceType=${extendableResourceType!}&amp;voteValue=-1" >
				</a>
			</div>
			<div class="star">
				<a href="jsp/site/plugins/socialhub/modules/rating/DoVote.jsp?idExtendableResource=${idExtendableResource!}&amp;extendableResourceType=${extendableResourceType!}&amp;voteValue=1" >
				</a>
			</div>
			<div class="star">
				<a href="jsp/site/plugins/socialhub/modules/rating/DoVote.jsp?idExtendableResource=${idExtendableResource!}&amp;extendableResourceType=${extendableResourceType!}&amp;voteValue=2" >
				</a>
			</div>
		</div>
		<br/>
	</#if>
</#if>
');
INSERT INTO core_template VALUES ('socialhub_rating_vote_type_thumb', '
<#assign scoreValue = 0 />
<#assign voteCount = 0 />
<#if rating??>
	<#assign scoreValue = rating.scoreValue />
	<#assign voteCount = rating.voteCount />
</#if>
<#if show == "all" || show == "vote">
	<p>
		<#if ( scoreValue < 0 )>
			<img src="images/local/skin/plugins/socialhub/modules/rating/vote_against.png" title="#i18n{module.socialhub.rating.rating.labelVoteAgainst}" alt="#i18n{module.socialhub.rating.rating.labelVoteAgainst}"/>
			<span class="alert">${scoreValue!}</span>
		<#else>
			<img src="images/local/skin/plugins/socialhub/modules/rating/vote_for.png" title="#i18n{module.socialhub.rating.rating.labelScore}" alt="#i18n{module.socialhub.rating.rating.labelScore}"/>
			${scoreValue!}
		</#if>
		(${voteCount!})
	</p>
</#if>
<#if show == "all" || show == "actionVote">
	<#if canVote>
		<div>
			<div class="socialhub-rating-vote-title">
				<label for="">#i18n{module.socialhub.rating.rating.labelVote} :</label></div>
				<span class="socialhub-rating-vote-span">
					<a href="jsp/site/plugins/socialhub/modules/rating/DoVote.jsp?idExtendableResource=${idExtendableResource!}&amp;extendableResourceType=${extendableResourceType!}&amp;voteValue=-1">
						<img src="images/local/skin/plugins/socialhub/modules/rating/vote_against.png" title="#i18n{module.socialhub.rating.rating.labelVoteAgainst}" alt="#i18n{module.socialhub.rating.rating.labelVoteAgainst}"/> 
					</a>
				</span>
				<span class="socialhub-rating-vote-span">
					<a href="jsp/site/plugins/socialhub/modules/rating/DoVote.jsp?idExtendableResource=${idExtendableResource!}&amp;extendableResourceType=${extendableResourceType!}&amp;voteValue=1">
						<img src="images/local/skin/plugins/socialhub/modules/rating/vote_for.png" title="#i18n{module.socialhub.rating.rating.labelVoteFor}" alt="#i18n{module.socialhub.rating.rating.labelVoteFor}"/>
					</a>
				</span>
		</div>
	</#if>
</#if>
');