<%@page import="fr.paris.lutece.portal.service.message.SiteMessageException"%>
<%@page import="fr.paris.lutece.portal.service.util.AppPathService"%>
<jsp:useBean id="ratingJspBean" scope="request" class="fr.paris.lutece.plugins.socialhub.modules.rating.web.RatingJspBean" />

<%
	try
	{
		ratingJspBean.doVote( request, response );
	}
	catch( SiteMessageException lme )
	{
		response.sendRedirect( AppPathService.getBaseUrl( request ) + AppPathService.getPortalUrl(  ) );
	}
%>