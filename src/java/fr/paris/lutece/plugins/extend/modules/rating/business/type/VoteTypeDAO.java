/*
 * Copyright (c) 2002-2013, Mairie de Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.extend.modules.rating.business.type;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.sql.DAOUtil;

import java.util.ArrayList;
import java.util.List;


/**
 *
 * VoteTypeDAO
 *
 */
public class VoteTypeDAO implements IVoteTypeDAO
{
    private static final String SQL_QUERY_SELECT_ALL = " SELECT id_vote_type, title, template_name FROM extend_rating_vote_type ";
    private static final String SQL_QUERY_FIND_BY_PRIMARY_KEY = SQL_QUERY_SELECT_ALL + " WHERE id_vote_type = ? ";
    private static final String SQL_QUERY_UPDATE = " UPDATE extend_rating_vote_type SET title = ? WHERE id_vote_type = ? ";

    /**
     * {@inheritDoc}
     */
    @Override
    public VoteType load( int nIdVoteType, Plugin plugin )
    {
        int nIndex = 1;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FIND_BY_PRIMARY_KEY, plugin );
        daoUtil.setInt( nIndex++, nIdVoteType );
        daoUtil.executeQuery(  );

        VoteType voteType = null;

        if ( daoUtil.next(  ) )
        {
            nIndex = 1;
            voteType = new VoteType(  );
            voteType.setIdVoteType( daoUtil.getInt( nIndex++ ) );
            voteType.setTitle( daoUtil.getString( nIndex++ ) );
            voteType.setTemplateName( daoUtil.getString( nIndex ) );
        }

        daoUtil.free(  );

        return voteType;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<VoteType> loadAll( Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_ALL, plugin );
        daoUtil.executeQuery(  );

        List<VoteType> listVoteTypes = new ArrayList<VoteType>(  );

        while ( daoUtil.next(  ) )
        {
            int nIndex = 1;
            VoteType voteType = new VoteType(  );
            voteType.setIdVoteType( daoUtil.getInt( nIndex++ ) );
            voteType.setTitle( daoUtil.getString( nIndex++ ) );
            voteType.setTemplateName( daoUtil.getString( nIndex ) );

            listVoteTypes.add( voteType );
        }

        daoUtil.free(  );

        return listVoteTypes;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void store( VoteType voteType, Plugin plugin )
    {
        int nIndex = 1;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin );
        daoUtil.setString( nIndex++, voteType.getTitle(  ) );

        daoUtil.setInt( nIndex, voteType.getIdVoteType(  ) );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }
}
