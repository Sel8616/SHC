/*
 * Copyright 2015-2016 Erlu Shang (sel8616@gmail.com/philshang@163.com)
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

public class UploadData
{
    private final String filename;
    private final byte[] data;

    public UploadData(String filename, byte[] data)
    {
        if(filename == null || filename.isEmpty())
        {
            throw new IllegalArgumentException("The filename must not be null or empty!");
        }
        if(data == null || data.length < 1)
        {
            throw new IllegalArgumentException("The data must not be null or empty!");
        }
        this.filename = filename;
        this.data = data;
    }

    public String getFilename()
    {
        return filename;
    }

    public byte[] getData()
    {
        return data;
    }
}