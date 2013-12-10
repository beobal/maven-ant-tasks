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

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.resolver.ResolutionListener;
import org.apache.maven.artifact.versioning.VersionRange;
import org.apache.tools.ant.Project;

/**
 * Show resolution information in Ant.
 *
 * @author <a href="mailto:brett@apache.org">Brett Porter</a>
 * @version $Id: AntResolutionListener.java 884196 2009-11-25 17:07:06Z pgier $
 */
public class AntResolutionListener
    implements ResolutionListener
{
    private String indent = "  ";

    private final Project project;
    
    public AntResolutionListener( Project project )
    {
        this.project = project;
    }

    public void testArtifact( Artifact node )
    {
    }

    public void startProcessChildren( Artifact artifact )
    {
        indent += "  ";
    }

    public void endProcessChildren( Artifact artifact )
    {
        indent = indent.substring( 2 );
    }

    public void includeArtifact( Artifact artifact )
    {
        project.log( indent + artifact + " (selected)", Project.MSG_VERBOSE );
    }

    public void omitForNearer( Artifact omitted, Artifact kept )
    {
        project.log( indent + omitted + " (removed - nearer found: " + kept.getVersion() + ")", Project.MSG_VERBOSE );
    }

    public void omitForCycle( Artifact omitted )
    {
        project.log( indent + omitted + " (removed - causes a cycle in the graph)", Project.MSG_VERBOSE );
    }

    public void updateScope( Artifact artifact, String scope )
    {
        project.log( indent + artifact + " (setting scope to: " + scope + ")", Project.MSG_VERBOSE );
    }

    public void updateScopeCurrentPom( Artifact artifact, String scope )
    {
        project.log( indent + artifact + " (not setting scope to: " + scope + "; local scope " + artifact.getScope()
                     + " wins)", Project.MSG_VERBOSE );
    }

    public void selectVersionFromRange( Artifact artifact )
    {
        project.log( indent + artifact + " (setting version to: " + artifact.getVersion() + " from range: "
                     + artifact.getVersionRange() + ")", Project.MSG_VERBOSE );
    }

    public void restrictRange( Artifact artifact, Artifact replacement, VersionRange newRange )
    {
        project.log( indent + artifact + " (range restricted from: " + artifact.getVersionRange() + " and: "
                     + replacement.getVersionRange() + " to: " + newRange + " )", Project.MSG_VERBOSE );
    }

    public void manageArtifact( Artifact artifact, Artifact replacement )
    {
        String msg = indent + artifact;
        msg += " (";
        if ( replacement.getVersion() != null )
        {
            msg += "applying version: " + replacement.getVersion() + ";";
        }
        if ( replacement.getScope() != null )
        {
            msg += "applying scope: " + replacement.getScope();
        }
        msg += ")";
        project.log( msg, Project.MSG_VERBOSE );
    }
}
