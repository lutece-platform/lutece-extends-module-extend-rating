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

