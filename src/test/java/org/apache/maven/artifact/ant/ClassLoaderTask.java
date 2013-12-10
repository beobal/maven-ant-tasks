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

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;

/**
 * Task to test context classloader.
 * 
 * @author <a href="mailto:hboutemy@apache.org">Herve Boutemy</a>
 * @version $Id: ClassLoaderTask.java 667996 2008-06-15 19:03:37Z hboutemy $
 */
public class ClassLoaderTask
    extends Task
{
    private String set;

    private String check;

    public String getSet()
    {
        return set;
    }

    public void setSet( String set )
    {
        this.set = set;
    }

    public String getCheck()
    {
        return check;
    }

    public void setCheck( String check )
    {
        this.check = check;
    }

    public void execute()
    {
        if ( set != null )
        {
            getProject().addReference( set, Thread.currentThread().getContextClassLoader() );
            log( "context classloader stored in " + set, Project.MSG_INFO );
        }

        if ( check != null )
        {
            ClassLoader classLoader = (ClassLoader) getProject().getReference( check );
            ClassLoader found = Thread.currentThread().getContextClassLoader();

            if ( classLoader != found )
            {
                throw new BuildException( "found bad context classloader: " + found + " instead of " + classLoader );
            }

            log( "context classloader " + check + " correctly switched back", Project.MSG_INFO );
        }
    }
}