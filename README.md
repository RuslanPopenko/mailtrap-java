# mailtrap-java
Java client for Mailtrap API https://api-docs.mailtrap.io/docs/mailtrap-api-docs/.

## Local use

To build the component on your local machine and put it into the local maven repository, run:

```bash
./gradlew clean publishToMavenLocal
```

Note: That you need to have Java 21 installed on your machine.

## Usage

First of all to use the library you need to declare the dependency in your project:

```yaml
implementation 'com.railware.mailtrap:mailtrap-java:1.0.0'
```

Then import Feign configuration:

```java
@Configuration
@Import({FeignClientsConfiguration.class, MailtrapClientConfiguration.class})
public class FeignClientConfiguration {
}
```

See the `src/test/java/com/railsware/mailtrap/config/MailtrapClientConfigurationTest.java` as an example.

Also specify configuration properties in your `application.yml`:

```yaml
client:
  mailtrap:
    url: http://localhost:8080 # Mailtrap API URL
    token: secret-api-token # Mailtrap API token to be passed at Api-Token header
    connect-timeout-millis: 10000 # Connection timeout in milliseconds
    read-timeout-millis: 5000 # Read timeout in milliseconds
```

See the `src/test/resources/application.yml` as an example.

After that you can autowire the `MaitrapApi` interface and invoke necessary methods.

```java
import com.railsware.mailtrap.client.MailtrapApi;
...
@Autowired
private MailtrapApi mailtrapApi;
...
EmailSendResponse response = mailtrapApi.send(EmailSendRequest.builder()
    .from(createAddress("sales@example.com", "Example Sales Team"))
    .to(List.of(createAddress("john_doe@example.com", "John Doe")))
    .subject("Your Example Order Confirmation")
    .text("Congratulations on your order no. 1234")
    .html("<p>Congratulations on your order no. <strong>1234</strong>.</p>")
    .attachments(List.of(createAttachment()))
    .build())
```

See the `src/test/java/com/railsware/mailtrap/client/MailtrapApiTest.java` as an example.

Pay attention that in case of any API error `MailtrapException` will be thrown with the error body.
