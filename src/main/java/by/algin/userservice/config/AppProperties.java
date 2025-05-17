package by.algin.userservice.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "app")
public class AppProperties {

    private final Security security = new Security();
    private final Confirmation confirmation = new Confirmation();
    private final Mail mail = new Mail();

    @Data
    public static class Security {
        private final Jwt jwt = new Jwt();

        @Data
        public static class Jwt {
            private String secret;
            private long accessTokenExpiration;
            private long refreshTokenExpiration;
        }
    }

    @Data
    public static class Confirmation {
        private final Token token = new Token();
        private String url;

        @Data
        public static class Token {
            private int expirationMinutes;
        }
    }

    @Data
    public static class Mail {
        private final Subject subject = new Subject();
        private final Body body = new Body();

        @Data
        public static class Subject {
            private String registration;
        }

        @Data
        public static class Body {
            private String registration;
        }
    }
}
