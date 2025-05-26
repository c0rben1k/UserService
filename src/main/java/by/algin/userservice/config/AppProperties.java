package by.algin.userservice.config;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Data
@Component
@ConfigurationProperties(prefix = "app")
@Validated
public class AppProperties {

    @Valid
    @NotNull
    private final Security security = new Security();

    @Valid
    @NotNull
    private final Confirmation confirmation = new Confirmation();

    @Valid
    @NotNull
    private final Mail mail = new Mail();

    @Valid
    @NotNull
    private final RememberMe rememberMe = new RememberMe();

    @Valid
    @NotNull
    private final RateLimit rateLimit = new RateLimit();

    @Data
    public static class Security {
        @NotNull
        private String secret;

        @NotNull
        private Long accessTokenExpiration;

        @NotNull
        private Long refreshTokenExpiration;
    }

    @Data
    public static class Confirmation {
        @NotNull
        private Integer expirationMinutes;

        @NotNull
        private String url;
    }

    @Data
    public static class Mail {
        @NotNull
        private String registrationSubject;

        @NotNull
        private String registrationBody;
    }

    @Data
    public static class RememberMe {
        @NotNull
        private String key;

        @NotNull
        private Integer tokenValiditySeconds;
    }

    @Data
    public static class RateLimit {
        @Valid
        @NotNull
        private final ResendConfirmation resendConfirmation = new ResendConfirmation();

        @Data
        public static class ResendConfirmation {
            @NotNull
            private Integer maxRequests;

            @NotNull
            private Long windowSeconds;
        }
    }
}