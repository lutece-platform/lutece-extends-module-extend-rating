--liquibase formatted sql

--
-- Removes the right EXTEND_VOTE_TYPES_MANAGEMENT: its page ManageVoteTypes.jsp exists in no release, so its admin
-- menu entry led to a 404. The 3.x releases created it with its user rights; 4.0.0 left the user rights alone.
--

--changeset extend-rating:update_db_extend_rating-4.0.1-4.0.2-admin-right
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(*) FROM core_admin_right WHERE id_right = 'EXTEND_VOTE_TYPES_MANAGEMENT' AND admin_url = 'jsp/admin/plugins/extend/modules/rating/ManageVoteTypes.jsp'
DELETE FROM core_admin_right WHERE id_right = 'EXTEND_VOTE_TYPES_MANAGEMENT' AND admin_url = 'jsp/admin/plugins/extend/modules/rating/ManageVoteTypes.jsp';

--changeset extend-rating:update_db_extend_rating-4.0.1-4.0.2-user-right
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM core_admin_right WHERE id_right = 'EXTEND_VOTE_TYPES_MANAGEMENT'
DELETE FROM core_user_right WHERE id_right = 'EXTEND_VOTE_TYPES_MANAGEMENT';
