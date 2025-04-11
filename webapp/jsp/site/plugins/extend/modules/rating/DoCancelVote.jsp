<%@page import="fr.paris.lutece.portal.service.message.SiteMessageException"%>
<%@page import="fr.paris.lutece.portal.service.util.AppPathService"%>
<%@page errorPage="../../../../ErrorPagePortal.jsp" %>

${ ratingJspBean.init( pageContext.request, RatingJspBean.RIGHT_MANAGE_RESOURCE_EXTENDER ) }
${ pageContext.response.sendRedirect( RatingJspBean.getEnabledExtender( pageContext.request )) }

<%
	try
	{
%>
		${ ratingJspBean.doCancelRating( pageContext.request, pageContext.response ) }
<%
	}
	catch( ELException el )
	{
		if ( SiteMessageException.class.getCanonicalName(  ).equals( el.getCause( ).getClass( ).getCanonicalName( ) ) )
        {
	        response.sendRedirect( AppPathService.getSiteMessageUrl( request ) );
        }
	}
%>