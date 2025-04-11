<%@page import="fr.paris.lutece.portal.service.security.UserNotSignedException"%>
<%@page import="fr.paris.lutece.portal.service.message.SiteMessageException"%>
<%@page import="fr.paris.lutece.portal.service.util.AppPathService"%>
<%@page import="fr.paris.lutece.portal.web.PortalJspBean"%>
<%@page import="jakarta.el.ELException"%>


<%
	try
	{
%>
		${ ratingJspBean.doRating( pageContext.request, pageContext.response ) }
<%
	}
	catch( ELException el )
	{
		if ( SiteMessageException.class.getCanonicalName(  ).equals( el.getCause( ).getClass( ).getCanonicalName( ) ) )
        {
	        response.sendRedirect( AppPathService.getBaseUrl( request ) + AppPathService.getPortalUrl(  ) );
        }
		else if ( UserNotSignedException.class.getCanonicalName(  ).equals( el.getCause( ).getClass( ).getCanonicalName( ) ) )
		{
			response.sendRedirect( PortalJspBean.redirectLogin( request ) );
		}
	}
%>