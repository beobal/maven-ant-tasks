package org.apache.maven.artifact.ant;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import org.apache.maven.wagon.events.TransferEvent;
import org.apache.maven.wagon.events.TransferListener;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectComponent;

/**
 * Log wagon events in the ant tasks
 *
 * @author <a href="mailto:brett@apache.org">Brett Porter</a>
 * @version $Id: AntDownloadMonitor.java 814436 2009-09-13 23:29:03Z bentmann $
 */
public class AntDownloadMonitor
    extends ProjectComponent
    implements TransferListener
{
    private static final int KILO = 1024;

    public void debug( String s )
    {
        log( s, Project.MSG_DEBUG );
    }

    public void transferCompleted( TransferEvent event )
    {
        long contentLength = event.getResource().getContentLength();
        if ( ( contentLength > 0 ) && ( event.getRequestType() == TransferEvent.REQUEST_PUT ) )
        {
            log( "Uploaded " + ( ( contentLength + KILO / 2 ) / KILO ) + "K" );
        }
    }

    public void transferError( TransferEvent event )
    {
        log( event.getException().getMessage(), Project.MSG_ERR );
    }

    public void transferInitiated( TransferEvent event )
    {
        String message = event.getRequestType() == TransferEvent.REQUEST_PUT ? "Uploading" : "Downloading";
        String dest = event.getRequestType() == TransferEvent.REQUEST_PUT ? " to " : " from ";

        log( message + ": " + event.getResource().getName() + dest + "repository "
            + event.getWagon().getRepository().getId() + " at " + event.getWagon().getRepository().getUrl() );
    }

    public void transferProgress( TransferEvent event, byte[] bytes, int i )
    {
    }

    public void transferStarted( TransferEvent event )
    {
        long contentLength = event.getResource().getContentLength();
        if ( contentLength > 0 )
        {
            log( "Transferring " + ( ( contentLength + KILO / 2 ) / KILO ) + "K from "
                            + event.getWagon().getRepository().getId() );
        }
    }
}
