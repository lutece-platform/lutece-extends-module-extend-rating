<%@ page errorPage="../../../../ErrorPage.jsp" %>
<jsp:useBean id="voteTypes" scope="session" class="fr.paris.lutece.plugins.socialhub.modules.rating.web.type.VoteTypeJspBean" />

<% 
	voteTypes.init( request, voteTypes.RIGHT_MANAGE_VOTE_TYPES );
 	response.sendRedirect( voteTypes.doModifyVoteType( request) );
%>
