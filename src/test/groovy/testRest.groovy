
import wslite.http.auth.*
import wslite.rest.*

String restUrl = "http://devops:8380/pulse/api/2"
String restQuery = "/issue"
String restMethod = "post"
String restBody = '{\n' +
        '"fields": {\n' +
        '   "project":\n' +
        '   { \n' +
        '      "key": "RPO"\n' +
        '   },\n' +
        '   "summary": "REST EXAMPLE",\n' +
        '   "description": "Creating an issue via REST API",\n' +
        '   "issuetype": {\n' +
        '      "name": "Bug"\n' +
        '   }\n' +
        '  }\n' +
        '}'
String restUsername = ""
String restPassword = "admin"
String restAcceptHeader = "text/xml"
String restContentTypeHeader = "application/json"
String restCharset = "UTF-8"

def client = new RESTClient(restUrl)
if (!restUsername.empty)     {
    println "Setting basic authentication for user ${restUsername} "
    client.authorization = new HTTPBasicAuthorization(restUsername, restPassword)
}

client.defaultContentTypeHeader = restContentTypeHeader
client.defaultCharset = restCharset

def response
try {
    switch (restMethod) {
        case 'get':
            response = client.get(path:restQuery,
                    connectTimeout: 5000,
                    readTimeout: 10000,
                    followRedirects: false,
                    useCaches: false,
                    sslTrustAllCerts: true
            )
            break
        case 'post':
            response = client.post(path:restQuery,
                    connectTimeout: 5000,
                    readTimeout: 10000,
                    followRedirects: false,
                    useCaches: false,
                    sslTrustAllCerts: true
            ) {
                text restBody
            }
            break
        case 'put':
            response = client.post(path:restQuery,
                    connectTimeout: 5000,
                    readTimeout: 10000,
                    followRedirects: false,
                    useCaches: false,
                    sslTrustAllCerts: true
            ) {
                text restBody
            }
            break
        default:
            println "No REST method supplied!"
    }
    println "Received response ${response.statusMessage} with data:"
    println response.text
} catch (RESTClientException ex) {
    println "Error making REST call:"
    println ex.printStackTrace()
}



  