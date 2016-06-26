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
package cn.sel.shc.constant;

/**
 * <b>INVALID_URL</b>           Specific URL was not correct.<br/>
 * <b>INVALID_METHOD</b>        Method was not supported.<br/>
 * <b>INVALID_CONTENT_TYPE</b>  ContentType was not supported.<br/>
 * <b>INVALID_ENCODING</b>      RequestEncoding or AcceptCharset was not supported.<br/>
 * <b>INTERNAL</b>              Programming error.<br/>
 * <b>NETWORK</b>               Failed to establish connection to host due to timeout/unknown host/socket error/IO error...<br/>
 * <b>READ_FAIL</b>             Failed to read from response stream.<br/>
 * <b>SERVER</b>                The client returned 500.<br/>
 * <b>UNKNOWN</b>               All others.<br/>
 */
public enum RequestError
{
    UNKNOWN,
    INVALID_URL,
    INVALID_METHOD,
    INVALID_ENCODING,
    INTERNAL,
    NETWORK,
    READ_FAIL
}