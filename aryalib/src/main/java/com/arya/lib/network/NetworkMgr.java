package com.arya.lib.network;

/**
 * Created by ARCHANA on 19-07-2015.
 */

import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.arya.lib.logger.Logger;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.zip.GZIPInputStream;

public class NetworkMgr {
    //private static final String SPACE = " ";
    private static final String TAG = "NetworkMgr";

    //private static boolean isFROYODevice = (Build.VERSION.SDK_INT <= Build.VERSION_CODES.GINGERBREAD_MR1);
    //private static String tag = "AryaPortfolio";
    //private int count=0;


    public static synchronized NetworkResponse httpPost(String httpUrl, String fieldName, String postData) {
       Log.d(TAG, "" + httpUrl + fieldName + postData);
        NetworkResponse netResp = new NetworkResponse();
        /*if (TextUtils.isEmpty(postData)) {
            return netResp;
        }*/
//        if (Logger.IS_DEBUG) {
//            Logger.info(TAG, "url [" + httpUrl + "] field [" + fieldName + "] data [" + postData + "]");
//        }

        InputStream is = null;
        HttpURLConnection conn = null;
        try {
            String data;
            if (TextUtils.isEmpty(fieldName)) {
                if (!httpUrl.contains("QuickAddTaskByUserMobileForApp")) {
                    data = URLEncoder.encode(postData, HttpConstants.UTF8);
                } else {
                    data = postData;
                }

            } else {
                data = URLEncoder.encode(fieldName, HttpConstants.UTF8) + "=" + URLEncoder.encode(postData, HttpConstants.UTF8);
            }
            // constants
            URL url = new URL(httpUrl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty(HttpConstants.ACCEPT_CHARSET, HttpConstants.UTF8);
            conn.setRequestProperty(HttpConstants.CONNECTION, HttpConstants.KEEP_ALIVE);
            conn.setConnectTimeout(HttpConstants.HTTP_REQUEST_TIMEOUT);
            conn.setReadTimeout(HttpConstants.HTTP_READ_TIMEOUT);
            conn.setRequestProperty(HttpConstants.HEADER_ACCEPT_ENCODING, HttpConstants.ENCODING_GZIP);
            System.setProperty("http.keepAlive", "false");

            conn.setFixedLengthStreamingMode(data.getBytes().length);
            // POST the data
            OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream(), HttpConstants.UTF8);
            osw.write(data);
            osw.close();
            osw = null;
            int statusCode = conn.getResponseCode();
            if (statusCode >= 200 && statusCode < 400) {

                is = conn.getInputStream();
            } else {
                is = conn.getErrorStream();
            }
            String response = readIt(is, conn.getContentEncoding());
            Log.d(TAG, "response value is : " + response);
            if (statusCode != HttpConstants.SC_OK) {
                netResp.netRespCode = NetworkResponse.ResponseCode.EXCEPTION;
                return netResp;
            } else {
                netResp.respStr = response;
                netResp.netRespCode = NetworkResponse.ResponseCode.OK;
            }
        } catch (SocketTimeoutException e) {
            if (Logger.IS_DEBUG) {
                e.printStackTrace();
//                Logger.error(TAG, "url [" + httpUrl + "]\n data [" + postData + "]\n Response time [" + (System.currentTimeMillis() - stime) + "]");
            }
            netResp.netRespCode = NetworkResponse.ResponseCode.TIMEOUT;
            // eLogger.fatal("OutputStreamWriterNew : SocketTimeoutException : " + e);
            return netResp;
        } catch (EOFException e) {
            e.printStackTrace();
        } catch (Exception e) {
            if (Logger.IS_DEBUG) {
                e.printStackTrace();
//                Logger.error(TAG, "url [" + httpUrl + "]\n data [" + postData + "]\n Response time [" + (System.currentTimeMillis() - stime) + "]");
            }
            netResp.netRespCode = NetworkResponse.ResponseCode.EXCEPTION;
            // eLogger.fatal("httpPostNew : Network Exception" + e);
            return netResp;
        } finally {
            finallyClose(is, conn);
        }


        return netResp;
    }

    private static void finallyClose(InputStream is, HttpURLConnection conn) {
        if (is != null) {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (conn != null) {
            conn.disconnect();
        }
    }

    private static String readIt(InputStream is, String encoding)
            throws IOException, UnsupportedEncodingException {

        StringBuilder sb = new StringBuilder(
                HttpConstants.INPUT_STREAM_BUFFSIZE);
        BufferedReader r = null;
        if (HttpConstants.ENCODING_GZIP.equals(encoding)) {
            r = new BufferedReader(new InputStreamReader(
                    new GZIPInputStream(is), HttpConstants.UTF8),
                    HttpConstants.INPUT_STREAM_BUFFSIZE);
        } else {
            r = new BufferedReader(
                    new InputStreamReader(is, HttpConstants.UTF8),
                    HttpConstants.INPUT_STREAM_BUFFSIZE);
        }
        for (String line = r.readLine(); line != null; line = r.readLine()) {
            sb.append(line);
        }
        r.close();
        return sb.toString();
    }

    public static synchronized NetworkResponse httpGet(String sourceFileURI, File outputFile) {

        long stime = System.currentTimeMillis();

        NetworkResponse netResp = new NetworkResponse();
        // file should not be download if exists.
        if (outputFile.exists()) {
            netResp.netRespCode = NetworkResponse.ResponseCode.OK;
            if (Logger.IS_DEBUG) {
                Logger.info(TAG, "get(): url [" + sourceFileURI + "] outFile [" + outputFile + "] already exists");
            }
            return netResp;
        }
        if (TextUtils.isEmpty(sourceFileURI)) {
            netResp.netRespCode = NetworkResponse.ResponseCode.EMPTY_URL;
            if (Logger.IS_DEBUG) {
                Logger.info(TAG, "get(): url [" + sourceFileURI + "] outFile [" + outputFile + "] url null");
            }
            return netResp;
        }
        if (Logger.IS_DEBUG) {
            Logger.info(TAG, "get(): url [" + sourceFileURI + "] outFile [" + outputFile + "] already exists");
        }
        InputStream is = null;
        HttpURLConnection conn = null;
        try {
            // constants
            sourceFileURI = URLDecoder
                    .decode(sourceFileURI, HttpConstants.UTF8);
            URL url = new URL(sourceFileURI);
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(false);
            conn.setDoInput(true);
            conn.setRequestMethod("GET");
            conn.setRequestProperty(HttpConstants.ACCEPT_CHARSET, "UTF-8");
            conn.setRequestProperty(HttpConstants.CONNECTION,
                    HttpConstants.KEEP_ALIVE);
            conn.setConnectTimeout(HttpConstants.HTTP_REQUEST_TIMEOUT);
            conn.setReadTimeout(HttpConstants.HTTP_READ_TIMEOUT);
            // make some HTTP header nicety
            // conn.setRequestProperty("Content-Type",
            // "text/plain; charset=utf-8");
            conn.setRequestProperty(HttpConstants.HEADER_ACCEPT_ENCODING,
                    HttpConstants.ENCODING_GZIP);
            is = conn.getInputStream();
            int statusCode = conn.getResponseCode();
            if (statusCode == HttpConstants.SC_OK) {
                InputStream inputStream = null;
                try {
                    inputStream = conn.getInputStream();
                } catch (IllegalStateException e) {
                    netResp.netRespCode = NetworkResponse.ResponseCode.EXCEPTION;
                    return netResp;
                } catch (FileNotFoundException e) {
                    // No need to treat missing file on server side as an error
                    netResp.netRespCode = NetworkResponse.ResponseCode.OK;
                    return netResp;
                } catch (Exception e) {
                    netResp.netRespCode = NetworkResponse.ResponseCode.EXCEPTION;
                    return netResp;
                }
                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(outputFile);
                } catch (FileNotFoundException e) {
                    netResp.netRespCode = NetworkResponse.ResponseCode.EXCEPTION;
                    return netResp;
                }
                int bytesRead = 0;
                try {
                    String encoding = conn.getContentEncoding();
                    // NOTE: Need to check how to get encoding info from
                    // Response.
                    BufferedInputStream bis;
                    if (HttpConstants.ENCODING_GZIP.equals(encoding)) {
                        // System.out.println("unzip file");
                        bis = new BufferedInputStream(new GZIPInputStream(
                                inputStream),
                                HttpConstants.INPUT_STREAM_BUFFSIZE);
                    } else {
                        // System.out.println("no unzip file");
                        bis = new BufferedInputStream(inputStream,
                                HttpConstants.INPUT_STREAM_BUFFSIZE);
                    }
                    byte[] buffer = new byte[HttpConstants.INPUT_STREAM_BUFFSIZE];
                    while ((bytesRead = bis.read(buffer)) > 0) {
                        fos.write(buffer, 0, bytesRead);
                    }
                    fos.close();
                    // Fix for zero byte file
                    if (outputFile.length() == 0) {
                        outputFile.delete();
                        // downloading 0 byte file is not an error. System
                        // should not try it again.
                        return netResp;
                    }
                } catch (IOException e) {
                    netResp.netRespCode = NetworkResponse.ResponseCode.EXCEPTION;
                    // eLogger.fatal("downloadFile : IOException" + e);
                    return netResp;
                }
            } else if (statusCode == HttpConstants.SC_NOT_FOUND) {
                netResp.netRespCode = NetworkResponse.ResponseCode.FILE_NOT_FOUND;
                return netResp;
            }
        } catch (SocketTimeoutException e) {
            netResp.netRespCode = NetworkResponse.ResponseCode.TIMEOUT;
            if (Logger.IS_DEBUG) {
                e.printStackTrace();
                Logger.error(TAG, "get(): url [" + sourceFileURI + "] outFile [" + outputFile + "] Response time [" + (System.currentTimeMillis() - stime) + "]");
            }
            return netResp;
        } catch (FileNotFoundException e) {
            // No need to treat missing file on server side as an error
            netResp.netRespCode = NetworkResponse.ResponseCode.OK;
            if (Logger.IS_DEBUG) {
                e.printStackTrace();
                Logger.error(TAG, "get(): url [" + sourceFileURI + "] outFile [" + outputFile + "] Response time [" + (System.currentTimeMillis() - stime) + "]");
            }
            return netResp;
        } catch (Exception e) {
            netResp.netRespCode = NetworkResponse.ResponseCode.EXCEPTION;
            if (Logger.IS_DEBUG) {
                e.printStackTrace();
                Logger.error(TAG, "get(): url [" + sourceFileURI + "] outFile [" + outputFile + "] Response time [" + (System.currentTimeMillis() - stime) + "]");
            }
            return netResp;
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    // eLogger.fatal("httpPost : SocketTimeoutException : " +
                    // e);
                }
            }
            if (conn != null) {
                conn.disconnect();
            }
        }
        if (Logger.IS_DEBUG)
            Logger.info(TAG, "get(): url [" + sourceFileURI + "] outFile [" + outputFile + "] Response time [" + (System.currentTimeMillis() - stime) + "]");
        netResp.netRespCode = NetworkResponse.ResponseCode.OK;
        return netResp;
    }

    public static synchronized NetworkResponse httpGet(String sourceFileURI) {


        long stime = System.currentTimeMillis();

        NetworkResponse netResp = new NetworkResponse();

        if (TextUtils.isEmpty(sourceFileURI)) {
            netResp.netRespCode = NetworkResponse.ResponseCode.EMPTY_URL;
            return netResp;
        }
        InputStream is = null;
        HttpURLConnection conn = null;
        try {

            URL url = new URL(sourceFileURI);
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(false);
            conn.setDoInput(true);
            conn.setRequestMethod("GET");
            conn.setRequestProperty(HttpConstants.ACCEPT_CHARSET, "UTF-8");
            conn.setRequestProperty(HttpConstants.CONNECTION, HttpConstants.KEEP_ALIVE);
            conn.setConnectTimeout(HttpConstants.HTTP_REQUEST_TIMEOUT);
            conn.setReadTimeout(HttpConstants.HTTP_READ_TIMEOUT);
            conn.setRequestProperty(HttpConstants.HEADER_ACCEPT_ENCODING, HttpConstants.ENCODING_GZIP);
            int statusCode = conn.getResponseCode();
            if (statusCode == HttpConstants.SC_OK) {
                InputStream inputStream = null;
                try {
                    inputStream = conn.getInputStream();
                } catch (IllegalStateException e) {
                    netResp.netRespCode = NetworkResponse.ResponseCode.EXCEPTION;
                    return netResp;
                } catch (FileNotFoundException e) {
                    // No need to treat missing file on server side as an error
                    netResp.netRespCode = NetworkResponse.ResponseCode.OK;
                    return netResp;
                } catch (Exception e) {
                    netResp.netRespCode = NetworkResponse.ResponseCode.EXCEPTION;
                    return netResp;
                }
                try {
                    String encoding = conn.getContentEncoding();
                    // NOTE: Need to check how to get encoding info from
                    // Response.
                    BufferedInputStream bis;
                    if (HttpConstants.ENCODING_GZIP.equals(encoding)) {
                        // System.out.println("unzip file");
                        bis = new BufferedInputStream(new GZIPInputStream(
                                inputStream),
                                HttpConstants.INPUT_STREAM_BUFFSIZE);
                    } else {
                        // System.out.println("no unzip file");
                        bis = new BufferedInputStream(inputStream,
                                HttpConstants.INPUT_STREAM_BUFFSIZE);
                    }
                    String response = readIt(bis, HttpConstants.UTF8);
                    netResp.netRespCode = NetworkResponse.ResponseCode.OK;
                    netResp.respStr = response;
                } catch (IOException e) {
                    e.printStackTrace();
                    netResp.netRespCode = NetworkResponse.ResponseCode.EXCEPTION;
                    return netResp;
                }
            } else if (statusCode == HttpConstants.SC_NOT_FOUND) {
                netResp.netRespCode = NetworkResponse.ResponseCode.FILE_NOT_FOUND;
                return netResp;
            }
        } catch (SocketTimeoutException e) {
            if (Logger.IS_DEBUG) {
                e.printStackTrace();
                Logger.error(TAG, "get(): url [" + sourceFileURI + "]");
            }
            netResp.netRespCode = NetworkResponse.ResponseCode.TIMEOUT;
            return netResp;
        } catch (FileNotFoundException e) {
            if (Logger.IS_DEBUG) {
                e.printStackTrace();
                Logger.error(TAG, "get(): url [" + sourceFileURI + "]");
            }
            // No need to treat missing file on server side as an error
            netResp.netRespCode = NetworkResponse.ResponseCode.OK;
            return netResp;
        } catch (Exception e) {
            if (Logger.IS_DEBUG) {
                e.printStackTrace();
                Logger.error(TAG, "get(): url [" + sourceFileURI + "]");
            }
            netResp.netRespCode = NetworkResponse.ResponseCode.EXCEPTION;
            return netResp;
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (conn != null) {
                conn.disconnect();
            }
        }
        if (Logger.IS_DEBUG)
            Logger.error(TAG, "get(): url [" + sourceFileURI + "] response [" + netResp.respStr + "] code [" + netResp.netRespCode + "] Response time [" + (System.currentTimeMillis() - stime) + "]");
        return netResp;
    }

    public static synchronized NetworkResponse uploadFileToServer(String serverUrl, String filePath, String file_name,Handler handler) {
        NetworkResponse networkResponse = new NetworkResponse();
        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        InputStream is = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        File sourcefile = new File(filePath);
        if (sourcefile.isFile()) {
            try {
                FileInputStream fileInputStream = new FileInputStream(sourcefile);
                URL url = new URL(serverUrl);
                conn = (HttpURLConnection) url.openConnection(); // Open a HTTP  connection to  the URL
                conn.setDoInput(true); // Allow Inputs
                conn.setDoOutput(true); // Allow Outputs
                conn.setUseCaches(false); // Don't use a Cached Copy
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                conn.setRequestProperty("uploaded_file", file_name);
                dos = new DataOutputStream(conn.getOutputStream());
                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\"" + file_name + "\"" + lineEnd);
                dos.writeBytes(lineEnd);
                dos.flush();
                bytesAvailable = fileInputStream.available(); // create a buffer of  maximum size
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];
                float fileSize=sourcefile.length();
//                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                bytesRead = 0;
                float downloadedSize = 0;
                while ((bytesRead=fileInputStream.read(buffer, 0, bufferSize))>0)
                {
                    dos.write(buffer, 0, bytesRead);
                    dos.flush();
                    downloadedSize += bytesRead;
                    if(handler!=null)
                    {
                        if(downloadedSize !=0.0)
                        {
                            handler.sendEmptyMessage((int) ((downloadedSize/fileSize)*100));
                        }
                    }

                }

                // send multipart form data necesssary after file data...
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
                int statusCode = conn.getResponseCode();
                if (statusCode >= 200 && statusCode < 400) {
                    // Create an InputStream in order to extract the response object
                    is = conn.getInputStream();
                } else {
                    is = conn.getErrorStream();
                }
                String response = readIt(is, conn.getContentEncoding());
                Log.d(TAG, "response value is : " + response);
                if (statusCode != HttpConstants.SC_OK) {
                    networkResponse.netRespCode = NetworkResponse.ResponseCode.EXCEPTION;
                    return networkResponse;
                } else {
                    networkResponse.respStr = response;
                    networkResponse.netRespCode = NetworkResponse.ResponseCode.OK;
                }
                fileInputStream.close();
                dos.flush();
                dos.close();

            } catch (MalformedURLException ex) {

                ex.printStackTrace();

            } catch (Exception e) {

                e.printStackTrace();

            }
        }


        return networkResponse;
    }

    public static NetworkResponse getResponseFromServerUsingUrl(String BaseUrl, String postData) {
        NetworkResponse response = null;
        try {
            String encodedUrl = BaseUrl + URLEncoder.encode(postData, HttpConstants.UTF8);
            response = httpGet(encodedUrl.replace("+", "%20"));

        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }
    public static synchronized String downloadFile(String fileUrl,String filename,String destinationDirPath,Handler handler,String fileSize) {
        String[] FILE_URL = fileUrl.split("/");
        File destinationFile = new File(destinationDirPath);
        if (!destinationFile.exists()) {
            destinationFile.mkdirs();
        }
        File file = new File(destinationFile, filename);
        String filepath = null;
        try {
            if (!file.exists()) {
                try
                {
                    Thread.sleep(5000);
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
                HttpURLConnection conn = null;
                InputStream inputStream = null;
                try {
                    URL url = new URL(fileUrl);
                    URLConnection urlConnection = url.openConnection();
                    conn = (HttpURLConnection) urlConnection;
                    conn.setDoInput(true);
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(30000);
                    conn.setReadTimeout(30000);
                    urlConnection.connect();
                    int responseCode=conn.getResponseCode();
                    if(responseCode==HttpConstants.SC_NOT_FOUND){
                        File fdelete = new File(filepath);
                        if (fdelete.exists()) {
                            fdelete.delete();
                        }
                        filepath=null;
                    }else{
                        inputStream = conn.getInputStream();
                        float file_size = urlConnection.getContentLength();
                        filepath = destinationDirPath + "/" + filename;
                        OutputStream outputStream = new FileOutputStream(filepath);

                        float downloadedSize = 0;
                        byte[] data = new byte[1024];
                        int count;
                        while ((count = inputStream.read(data, 0, 1024)) != -1) {
                            outputStream.write(data, 0, count);
                            downloadedSize += count;
                            if (handler != null) {
                                if (file_size != 0.0) {
                                    handler.sendEmptyMessage((int) (downloadedSize * 100 / file_size));

                                }
                            }
                        }
                        outputStream.flush();
                        outputStream.close();
                        inputStream.close();
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    File fdelete = new File(filepath);
                    if (fdelete.exists()) {
                        fdelete.delete();
                    }
                    filepath=null;

                } catch (IOException e) {
                    e.printStackTrace();
                    File fdelete = new File(filepath);
                    if (fdelete.exists()) {
                        fdelete.delete();
                    }
                    filepath=null;
                }
            } else {
                filepath = destinationDirPath + "/" + filename;
            }

        }catch (Exception e){
            e.printStackTrace();
            File fdelete = new File(filepath);
            if (fdelete.exists()) {
               fdelete.delete();
            }
            filepath=null;
        }
        return filepath;

    }
}
