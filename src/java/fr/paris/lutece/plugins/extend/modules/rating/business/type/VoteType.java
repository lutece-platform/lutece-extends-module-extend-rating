/*
 * Copyright (c) 2002-2014, Mairie de Paris
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

import javax.validation.constraints.NotNull;


/**
 *
 * VoteType
 *
 */
public class VoteType
{
    private int _nIdVoteType;
    @NotNull
    private String _strTitle;
    private String _strTemplateName;
    private String _strTemplateContent;

    /**
     * Gets the id vote type.
     *
     * @return the id vote type
     */
    public int getIdVoteType(  )
    {
        return _nIdVoteType;
    }

    /**
     * Sets the id vote type.
     *
     * @param nIdVoteType the new id vote type
     */
    public void setIdVoteType( int nIdVoteType )
    {
        _nIdVoteType = nIdVoteType;
    }

    /**
     * Gets the title.
     *
     * @return the title
     */
    public String getTitle(  )
    {
        return _strTitle;
    }

    /**
     * Sets the title.
     *
     * @param strTitle the new title
     */
    public void setTitle( String strTitle )
    {
        _strTitle = strTitle;
    }

    /**
     * Gets the template file name.
     *
     * @return the template file name
     */
    public String getTemplateName(  )
    {
        return _strTemplateName;
    }

    /**
     * Sets the template file name.
     *
     * @param strTemplateName the new template file name
     */
    public void setTemplateName( String strTemplateName )
    {
        _strTemplateName = strTemplateName;
    }

    /**
     * @return the strTemplateContent
     */
    public String getTemplateContent(  )
    {
        return _strTemplateContent;
    }

    /**
     * @param strTemplateContent the strTemplateContent to set
     */
    public void setTemplateContent( String strTemplateContent )
    {
        _strTemplateContent = strTemplateContent;
    }
}
