# multipartfileloader

This util allows you to build and perform requests containing multipart files.

## Usage example:

```groovy
def executeMultipartRequestAndReturnResponse(url, cookie, authToken, headers = [:], body = [:], uploadFile = null) {
    multipart = new MultipartFileUtil(url, authToken, cookie)
    
    if(headers){
        headers.each{ k, v ->
            multipart.addHeaderField(k, v)
        }
    }
    
    if (body) {
        body.each{ k, v ->
            multipart.addFormField(k, v)
        }
    }
    
    if(uploadFile){
        multipart.addFilePart(k, v)
    }
    
    multipart.finish()
}
```