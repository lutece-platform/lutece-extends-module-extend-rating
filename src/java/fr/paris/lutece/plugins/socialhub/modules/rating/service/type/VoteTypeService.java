/*
 * Copyright (c) 2002-2012, Mairie de Paris
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
package fr.paris.lutece.plugins.socialhub.modules.rating.service.type;

import fr.paris.lutece.plugins.socialhub.modules.rating.business.type.IVoteTypeDAO;
import fr.paris.lutece.plugins.socialhub.modules.rating.business.type.VoteType;
import fr.paris.lutece.plugins.socialhub.modules.rating.service.RatingPlugin;
import fr.paris.lutece.portal.service.template.DatabaseTemplateService;
import fr.paris.lutece.util.ReferenceList;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import javax.inject.Inject;


/**
 *
 * VoteTypeService
 *
 */
public class VoteTypeService implements IVoteTypeService
{
    /** The Constant BEAN_SERVICE. */
    public static final String BEAN_SERVICE = "socialhub-rating.voteTypeService";
    @Inject
    private IVoteTypeDAO _voteTypeDAO;

    /**
     * {@inheritDoc}
     */
    @Override
    public VoteType findByPrimaryKey( int nIdVoteType, boolean bGetTemplateContent )
    {
        VoteType voteType = _voteTypeDAO.load( nIdVoteType, RatingPlugin.getPlugin(  ) );

        // Get the template content from DatabaseTemplateService
        if ( ( voteType != null ) && bGetTemplateContent )
        {
            voteType.setTemplateContent( DatabaseTemplateService.getTemplateFromKey( voteType.getTemplateName(  ) ) );
        }

        return voteType;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<VoteType> findAll( boolean bGetTemplateContent )
    {
        List<VoteType> listVoteTypes = _voteTypeDAO.loadAll( RatingPlugin.getPlugin(  ) );

        // Get the template content from DatabaseTemplateService
        if ( ( listVoteTypes != null ) && !listVoteTypes.isEmpty(  ) && bGetTemplateContent )
        {
            for ( VoteType voteType : listVoteTypes )
            {
                voteType.setTemplateContent( DatabaseTemplateService.getTemplateFromKey( voteType.getTemplateName(  ) ) );
            }
        }

        return listVoteTypes;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ReferenceList findAll(  )
    {
        ReferenceList list = new ReferenceList(  );
        List<VoteType> listVoteTypes = findAll( false );

        if ( ( listVoteTypes != null ) && !listVoteTypes.isEmpty(  ) )
        {
            list = ReferenceList.convert( listVoteTypes, "idVoteType", "title", true );
        }

        return list;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional( RatingPlugin.TRANSACTION_MANAGER )
    public void update( VoteType voteType )
    {
        // Update the DatabaseTemplate
        DatabaseTemplateService.updateTemplate( voteType.getTemplateName(  ), voteType.getTemplateContent(  ) );
        // Update the plugin db
        _voteTypeDAO.store( voteType, RatingPlugin.getPlugin(  ) );
    }
}
