/*
 * Copyright (c) 2002-2017, Mairie de Paris
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

package fr.paris.lutece.plugins.elasticdata.modules.gru.business.demand;

import fr.paris.lutece.plugins.elasticdata.business.AbstractDataSource;
import fr.paris.lutece.plugins.elasticdata.business.DataObject;
import fr.paris.lutece.plugins.elasticdata.business.DataSource;
import fr.paris.lutece.plugins.elasticdata.modules.gru.business.BaseDemandObject;
import fr.paris.lutece.plugins.elasticdata.service.DataSourceService;
import fr.paris.lutece.plugins.grubusiness.business.demand.Demand;
import fr.paris.lutece.plugins.grubusiness.business.demand.IDemandDAO;
import fr.paris.lutece.plugins.grubusiness.business.demand.IDemandListener;
import fr.paris.lutece.plugins.libraryelastic.util.ElasticClientException;
import fr.paris.lutece.portal.service.util.AppLogService;

import java.util.ArrayList;
import java.util.Collection;

/**
 * DemandDataSource
 */
public class DemandDataSource extends AbstractDataSource implements DataSource, IDemandListener
{
    IDemandDAO _demandDAO;

    /**
     * Set the IDemandDAO to use
     * 
     * @param demandDAO
     */
    public void setDemandDAO( IDemandDAO demandDAO )
    {
        _demandDAO = demandDAO;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Collection<DataObject> getDataObjects( )
    {
        Collection<DataObject> collResult = new ArrayList<DataObject>( );
        for ( Demand demandDAO : _demandDAO.loadAllDemands( ) )
        {
            collResult.add( new BaseDemandObject( demandDAO ) );
        }
        return collResult;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCreateDemand( Demand demand )
    {
        BaseDemandObject demandObj = new BaseDemandObject( demand );
        try
        {
            DataSourceService.insertData( this, demandObj );
        }
        catch( ElasticClientException e )
        {
            AppLogService.error( "ElasticClientException occurs while DataSourceService.insertData for demand [" + demand.getId( ) + "]", e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onUpdateDemand( Demand demand )
    {
        AppLogService.info( "DemandDataSource doesn't manage onUpdateDemand method" );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDeleteDemand( String strDemandId, String strDemandTypeId )
    {
        AppLogService.info( "DemandDataSource doesn't manage onDeleteDemand method" );
    }

}