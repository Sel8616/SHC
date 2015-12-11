package cn.sel.hsharp.constant;

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
    UNKNOWN, INVALID_URL, INVALID_METHOD, INVALID_CONTENT_TYPE, INVALID_ENCODING, INTERNAL, NETWORK, READ_FAIL
}