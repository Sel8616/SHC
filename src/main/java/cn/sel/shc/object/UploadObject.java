/*
 * Copyright 2015-2017 Erlu Shang (sel8616@gmail.com/philshang@163.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.sel.shc.object;

import java.io.IOException;
import java.io.InputStream;

public abstract class UploadObject
{
    protected String name;
    protected String filename;

    public abstract InputStream getInputStream()
            throws IOException;

    public abstract String getContentType();

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        if(name == null || name.isEmpty())
        {
            throw new IllegalArgumentException("The name must not be null or empty!");
        }
        this.name = name;
    }

    public String getFilename()
    {
        return filename;
    }

    public void setFilename(String filename)
    {
        if(filename == null || filename.isEmpty())
        {
            throw new IllegalArgumentException("The filename must not be null or empty!");
        }
        this.filename = filename;
    }
}