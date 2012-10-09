<%@page import="fr.paris.lutece.portal.web.pluginaction.IPluginActionResult"%>

<jsp:useBean id="voteTypes" scope="session" class="fr.paris.lutece.plugins.socialhub.modules.rating.web.type.VoteTypeJspBean" />

<% 
	voteTypes.init( request, voteTypes.RIGHT_MANAGE_VOTE_TYPES );
	IPluginActionResult result = voteTypes.getModifyVoteType( request, response );
	if ( result.getRedirect(  ) != null )
	{
		response.sendRedirect( result.getRedirect(  ) );
	}
	else if ( result.getHtmlContent(  ) != null )
	{
%>
		<%@ page errorPage="../../../../ErrorPage.jsp" %>
		<jsp:include page="../../../../AdminHeader.jsp" />

		<%= result.getHtmlContent(  ) %>

		<%@ include file="../../../../AdminFooter.jsp" %>
<%
	}
%>
