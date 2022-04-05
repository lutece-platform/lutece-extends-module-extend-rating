<%@page import="fr.paris.lutece.portal.service.security.UserNotSignedException"%>
<%@page import="fr.paris.lutece.portal.service.message.SiteMessageException"%>
<%@page import="fr.paris.lutece.portal.service.util.AppPathService"%>
<%@page import="fr.paris.lutece.portal.web.PortalJspBean"%>
<jsp:useBean id="ratingJspBean" scope="request" class="fr.paris.lutece.plugins.extend.modules.rating.web.RatingJspBean" />

<%
	try
	{
		ratingJspBean.doRating( request, response );
	}
	catch( SiteMessageException lme )
	{
		response.sendRedirect( AppPathService.getBaseUrl( request ) + AppPathService.getPortalUrl(  ) );
	}
	catch( UserNotSignedException unse )
	{
		response.sendRedirect( PortalJspBean.redirectLogin( request ));
	}
%>