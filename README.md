# multipartfileloader

This util allows you to build and perform requests containing multipart files.

##Usage example:

```groovy
def executeMultipartRequestAndReturnResponse(url, body = null, cookie = null, auth = null, headers = null) {
    multipart = new MultipartFileUtil(url, auth, cookie)
    
    if(headers){
        headers.each{ k, v ->
            multipart.addHeaderField(k, v)
        }
    }
    
    if (body) {
        body.each { k, v ->
            if (v instanceof File) {
                multipart.addFilePart(k, v)
            } else {
                multipart.addFormField(k, v)
            }
        }
    }
    
    multipart.finish()
}
```