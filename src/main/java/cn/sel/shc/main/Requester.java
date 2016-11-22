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
package cn.sel.shc.main;

import cn.sel.shc.object.RequestArgs;

import java.util.Map;

/**
 * About the parameters:
 * <p>···><b>requestId</b></p>
 * The identification number of this request.
 * <p>···><b>url</b></p>
 * [NonNull] The url string of the server.
 * <p>···><b>parameters</b></p>
 * [Nullable] A {@link Map} contains some parameters.
 * <p>···><b>responseHandler</b></p>
 * [Nullable] Implementation of {@link ResponseHandler}. The client will overlook the response if null was passed.
 */
interface Requester
{
    /**
     * @see Requester
     */
    void get(int requestId, String url, RequestArgs requestArgs, ResponseHandler responseHandler);

    /**
     * @see Requester
     */
    void get(int requestId, String url, ResponseHandler handler);

    /**
     * @see Requester
     */
    void get(int requestId, String url, RequestArgs requestArgs);

    /**
     * @see Requester
     */
    void get(int requestId, String url);

    /**
     * @see Requester
     */
    void post(int requestId, String url, RequestArgs requestArgs, ResponseHandler responseHandler);

    /**
     * @see Requester
     */
    void post(int requestId, String url, ResponseHandler handler);

    /**
     * @see Requester
     */
    void post(int requestId, String url, RequestArgs requestArgs);

    /**
     * @see Requester
     */
    void post(int requestId, String url);

    /**
     * @see Requester
     */
    void put(int requestId, String url, RequestArgs requestArgs, ResponseHandler responseHandler);

    /**
     * @see Requester
     */
    void put(int requestId, String url, ResponseHandler handler);

    /**
     * @see Requester
     */
    void put(int requestId, String url, RequestArgs requestArgs);

    /**
     * @see Requester
     */
    void put(int requestId, String url);

    /**
     * @see Requester
     */
    void delete(int requestId, String url, RequestArgs requestArgs, ResponseHandler responseHandler);

    /**
     * @see Requester
     */
    void delete(int requestId, String url, ResponseHandler handler);

    /**
     * @see Requester
     */
    void delete(int requestId, String url, RequestArgs requestArgs);

    /**
     * @see Requester
     */
    void delete(int requestId, String url);
}