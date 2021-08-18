package fi.bytecraft.spock

import groovy.json.JsonOutput
import groovy.json.JsonSlurper

import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.jdbc.core.JdbcTemplate
import spock.lang.Specification

@SpringBootTest(classes = SpringApp, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ITSpecBase extends Specification {

    static HOST_BASE

    @LocalServerPort
    int port
    @Autowired
    JdbcTemplate jdbcTemplate

    def setup() {
        HOST_BASE = "http://localhost:$port"
        DbTestUtils.clearDatabase(jdbcTemplate.getDataSource().getConnection())
    }

    static class Request {

        private path
        HttpResponse response

        Request(String path) {
            this.path = path
        }

        static Request ofPath(String path) {
            new Request(path)
        }

        Request withHost(String host) {
            HOST_BASE = host

            this
        }

        Request doGet() {
            response = doRequest createGetRequest(path)

            this
        }

        Request doPost(object) {
            response = doRequest createPostRequest(path, object)

            this
        }

        Request doDelete() {
            response = doRequest createDeleteRequest(path)

            this
        }

        private HttpResponse doRequest(request) {
            HttpClient.newHttpClient().send request, HttpResponse.BodyHandlers.ofString()
        }

        def body() {
            def body
            try {
                body = new JsonSlurper().parseText(response.body() as String)
            } catch (Exception e) {
                body = response.body()
            }

            body
        }

        def <T> T bodyAs(Class<T> bodyClass) {
            bodyClass.newInstance(body())
        }

        int status() {
            response.statusCode()
        }

        private createGetRequest(path) {
            HttpRequest.Builder builder = HttpRequest.newBuilder(new URI("$HOST_BASE$path"))
                    .header("Accept", "application/json")

            builder.GET().build()
        }

        private static createPostRequest(path, data) {
            HttpRequest.newBuilder(new URI("$HOST_BASE$path"))
                    .header("Accept", "application/json")
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(JsonOutput.toJson(data)))
                    .build()
        }

        private static createPutRequest(path, data) {
            HttpRequest.newBuilder(new URI("$HOST_BASE$path"))
                    .header("Accept", "application/json")
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(JsonOutput.toJson(data)))
                    .build()
        }

        private static createDeleteRequest(path) {
            HttpRequest.newBuilder(new URI("$HOST_BASE$path"))
                    .DELETE()
                    .build()
        }

    }

}