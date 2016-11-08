package com.arya.lib.network;

class HttpConstants {
    final static int HTTP_REQUEST_TIMEOUT = 60000; // 180000;
    static final int HTTP_READ_TIMEOUT = 600000;
    static final String HEADER_ACCEPT_ENCODING = "Accept-Encoding";
    static final String ENCODING_GZIP = "gzip";
    static final int INPUT_STREAM_BUFFSIZE = 16 * 1024;
    static final String CONNECTION = "Connection";
    static final String KEEP_ALIVE = "Keep-Alive";
    static final int SC_OK = 200;
    static final int SC_NOT_FOUND = 404;
    static String UTF8 = "UTF-8";
    static String ACCEPT_CHARSET = "Accept-Charset";
}
