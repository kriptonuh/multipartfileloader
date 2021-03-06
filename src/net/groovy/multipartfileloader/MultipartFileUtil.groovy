package net.groovy.multipartfileloader

/**
 * Util for uploading multipart files on the server
 */
class MultipartFileUtil {

    static final String LINE_FEED = "\r\n"
    static final charset = "UTF-8"
    def boundary
    def httpConn
    def outputStream
    def writer

    /**
     * Initializes a new HTTP POST request with content type is set to multipart/form-data
     *
     * @param requestURL
     * @param charset
     * @throws IOException
     */
    MultipartFileUtil(requestURL, accessToken, cookie = null) throws IOException {
        // creates a unique boundary based on time stamp
        boundary = "===${System.currentTimeMillis()}==="

        def url = new URL(requestURL)
        httpConn = url.openConnection() as HttpURLConnection
        httpConn.setUseCaches(false)
        httpConn.setDoOutput(true)
        httpConn.setDoInput(true)
        httpConn.setRequestProperty("Content-Type", "multipart/form-data; boundary=${boundary}")
        httpConn.setRequestProperty("User-Agent", "GroovyTestAgent")
        httpConn.setRequestProperty("Authorization", "Bearer ${accessToken}")
        httpConn.setRequestProperty("Cookie", cookie)
        outputStream = httpConn.getOutputStream()
        writer = new PrintWriter(new OutputStreamWriter(outputStream, charset), true)
    }

    /**
     * Adds a form field to the request
     *
     * @param name field name
     * @param value field value
     */
    def addFormField(name, value) {
        writer.append("--${boundary}").append(LINE_FEED)
        writer.append("Content-Disposition: form-data; name=\"${name}\"").append(LINE_FEED)
        writer.append("Content-Type: text/plain; charset=${charset}").append(LINE_FEED)
        writer.append(LINE_FEED)
        writer.append(value).append(LINE_FEED)
        writer.flush()
    }

    /**
     * Adds a upload file section to the request
     *
     * @param fieldName name attribute in <input type="file" name="..." />
     * @param uploadFile a File to be uploaded
     * @throws IOException
     */
    def addFilePart(fieldName, File uploadFile) throws IOException {
        def fileName = uploadFile.getName()
        writer.append("--${boundary}").append(LINE_FEED)
        writer.append("Content-Disposition: form-data; name=\"${fieldName}\"; filename=\"${fileName}\"").append(LINE_FEED)
        writer.append("Content-Type: ${URLConnection.guessContentTypeFromName(fileName)}").append(LINE_FEED)
        writer.append("Content-Transfer-Encoding: binary").append(LINE_FEED)
        writer.append(LINE_FEED)
        writer.flush()

        def inputStream = new FileInputStream(uploadFile)
        outputStream << inputStream
        outputStream.flush()
        inputStream.close()

        writer.append(LINE_FEED)
        writer.flush()
    }

    /**
     * Adds a header field to the request.
     *
     * @param name - name of the header field
     * @param value - value of the header field
     */
    def addHeaderField(name, value) {
        writer.append("${name}:${value}").append(LINE_FEED)
        writer.flush()
    }

    /**
     * Completes the request and receives response from the server.
     *
     * @return HttpURLConnection
     */
    def finish() {
        writer.append(LINE_FEED).flush()
        writer.append("--${boundary}--").append(LINE_FEED)
        writer.close()

        return httpConn
    }
}
