
import wslite.http.auth.*
import wslite.rest.*

import com.urbancode.air.AirPluginTool

def apTool = new AirPluginTool(this.args[0], this.args[1])
def props = apTool.getStepProperties()

String oBaseUrl = props['baseUrl']?.trim()
String oBaseInstance = props['baseInstance']?.trim()
String oScmConn = props['scmConn']?.trim()
Boolean oUseBuildTag = props['useBuildTag']?.trim()
String oChangeSet = props['changeSet']?.trim()
String oTitle = props['title']?.trim()
String oBody = props['body']?.trim()
String oUsername = props['username']?.trim()
String oPassword = props['password']?.trim()
String oJob = props['job']?.trim()
String oType = props['type']?.trim()
String oState = props['state']?.trim()
String sraReqId = props['reqId']?.trim()
String sraURL = props['serverUrl']?.trim()

String scmUri = oScmConn + '#' + oChangeSet
String oLink = "${sraURL}/#applicationProcessRequest/${sraReqId}"

// if using build tag, we strip last number after ':'
if (oUseBuildTag) {
    scmUri = oScmConn + '#' + oChangeSet.substring(oChangeSet.lastIndexOf(":") + 1).trim()
}

// if type is "deployed-website" then set orbiter link to the passed website link
if (oJob == "deployed-website") {
    oLink = "${sraURL}"
}

def client = new RESTClient(oBaseUrl)
if (!oUsername.empty)     {
    println "Setting basic authentication for user ${oUsername}"
    client.authorization = new HTTPBasicAuthorization(oUsername, oPassword)
}

client.defaultContentTypeHeader = 'application/json'
client.defaultAcceptHeader = 'application/json'
client.defaultCharset = 'UTF-8'

try {
    def response = client.post(path:'/services/rest/event/external-event',
                    connectTimeout: 5000,
                    readTimeout: 10000,
                    followRedirects: false,
                    useCaches: false,
                    sslTrustAllCerts: true
            ) {
                json    type:"${oType}",
                        instance:"${oBaseInstance}",
                        job:"${oJob}",
                        run:"${sraReqId}",
                        scmuri:"${scmUri}",
                        state:"${oState}",
                        title:"${oTitle}",
                        body:"${oBody}",
                        url:"${oLink}"
            }
    println "Succesfully sent event ${oTitle} to ${oBaseUrl} for SCM URI ${scmUri}"
    println "Received response '${response.statusMessage}'"
    apTool.setOutputProperty("response", response.text)
    System.exit(0)
} catch (RESTClientException ex) {
    println "Error sending event to ${oBaseUrl}:"
    println ex.printStackTrace()
    System.exit(1);
}

apTool.setOutputProperties();


  