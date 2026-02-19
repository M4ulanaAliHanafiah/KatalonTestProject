import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.testobject.RequestObject
import com.kms.katalon.core.testobject.RestRequestObjectBuilder
import groovy.json.JsonSlurper

// PRODUCER - POST
RequestObject postRequest = new RestRequestObjectBuilder()
    .withRestUrl("https://jsonplaceholder.typicode.com/posts")
    .withHttpHeaders([new com.kms.katalon.core.testobject.TestObjectProperty("Content-Type", com.kms.katalon.core.testobject.ConditionType.EQUALS, "application/json")])
    .withRestRequestMethod("POST")
    .withTextBodyContent('{"title": "Test Title", "body": "Test Body Content", "userId": 1}')
    .build()

def postResponse = WS.sendRequest(postRequest)
WS.verifyResponseStatusCode(postResponse, 201)
def postBody = new JsonSlurper().parseText(postResponse.getResponseBodyContent())
println "✅ Producer - Created Post ID: ${postBody.id}"
assert postBody.id != null
println "POST Test PASSED ✅"

// CONSUMER - GET
RequestObject getRequest = new RestRequestObjectBuilder()
    .withRestUrl("https://jsonplaceholder.typicode.com/posts/1")
    .withRestRequestMethod("GET")
    .build()

def getResponse = WS.sendRequest(getRequest)
WS.verifyResponseStatusCode(getResponse, 200)
def getBody = new JsonSlurper().parseText(getResponse.getResponseBodyContent())
println "✅ Consumer - Retrieved Post ID: ${getBody.id}"
assert getBody.id == 1
println "GET Test PASSED ✅"