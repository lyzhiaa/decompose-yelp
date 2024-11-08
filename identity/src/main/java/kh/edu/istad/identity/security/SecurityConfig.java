package kh.edu.istad.identity.security;

import kh.edu.istad.identity.custom.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;

import java.util.HashSet;
import java.util.Set;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final PasswordEncoder passwordEncoder;
//    @Qualifier("userDetailsServiceImpl")
    private final UserDetailsService userDetailsService;

////    @Bean
////    public PasswordEncoder passwordEncoder() {
////        return new BCryptPasswordEncoder();
////    }
//
//    @Bean
//    public DaoAuthenticationProvider daoAuthenticationProvider() {
//        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
//        provider.setUserDetailsService(userDetailsService);
//        provider.setPasswordEncoder(passwordEncoder);
//        return provider;
//    }
//
//    @Order(1)
//    @Bean
//    SecurityFilterChain oauthSecurity(HttpSecurity httpSecurity) throws Exception {
//
//        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(httpSecurity);
//
//        httpSecurity.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
//                .oidc(Customizer.withDefaults());
//
//        httpSecurity
//                .exceptionHandling(c ->
//                        c.defaultAuthenticationEntryPointFor(
//                                new LoginUrlAuthenticationEntryPoint("/login"),
//                                new MediaTypeRequestMatcher(MediaType.TEXT_HTML)
//                        ));
//
//        return httpSecurity.build();
//    }
//
//    @Bean
//    @Order(2)
//    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
//
//        httpSecurity.authorizeHttpRequests(auth -> auth
//                .requestMatchers("/api/v1/test/public").permitAll()
//                .requestMatchers("http://localhost:9000/login").permitAll()
//                .anyRequest().authenticated()
//        );
//
//        httpSecurity.formLogin(Customizer.withDefaults());
////
//////         enable jwt security
//        httpSecurity.oauth2ResourceServer(oauthSecurity -> oauthSecurity.jwt(Customizer.withDefaults()));
////
////        // disable csrf for submit form because we develop api
//        httpSecurity.csrf(token -> token.disable());
////
////        // change from statefull to stateless because api use stateless
//
//        return httpSecurity.build();
//    }
//
//    @Bean
//    public AuthorizationServerSettings authorizationServerSettings() {
//        return AuthorizationServerSettings.builder()
//                .issuer("http://localhost:9000")
//                .build();
//    }
@Bean
DaoAuthenticationProvider daoAuthenticationProvider() {
    DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
    authenticationProvider.setUserDetailsService(userDetailsService);
    authenticationProvider.setPasswordEncoder(passwordEncoder);
    return authenticationProvider;
}


    @Bean
    AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder().build();
    }


    @Bean
    @Order(1)
    SecurityFilterChain configureOAuth2(HttpSecurity http) throws Exception {

        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);

        http
                .cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .getConfigurer(OAuth2AuthorizationServerConfigurer.class)
                /*.authorizationEndpoint(endpoint -> endpoint
                        .consentPage("/oauth2/consent")
                )*/
                .oidc(Customizer.withDefaults());

        http
                .exceptionHandling(ex -> ex
                        .defaultAuthenticationEntryPointFor(
                                new LoginUrlAuthenticationEntryPoint("/oauth2/login"),
                                new MediaTypeRequestMatcher(MediaType.TEXT_HTML)
                        )
                );

        return http.build();
    }


    @Bean
    @Order(2)
    SecurityFilterChain configureHttpSecurity(HttpSecurity http) throws Exception {

        http
                /*.authorizeHttpRequests(auth -> auth
                        .anyRequest()
                        .permitAll()
                )*/

                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(Customizer.withDefaults())
                )
                .formLogin(form -> form
                        .loginPage("/oauth2/login")
                        .usernameParameter("gp_account")
                        .passwordParameter("gp_password")
                )
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }


    @Bean
    OAuth2TokenCustomizer<JwtEncodingContext> tokenCustomizer() {
        return context -> {

            // TODO: Custom JWT with authorization_code grant type and Authentication
            Authentication authentication = context.getPrincipal();
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

            if (context.getTokenType().getValue().equals("id_token")) {
                context.getClaims().claim("reksmey1", "Mom Reksmey");
            }

            if (context.getTokenType().getValue().equals("access_token")) {
                Set<String> scopes = new HashSet<>(context.getAuthorizedScopes());
                authentication
                        .getAuthorities()
                        .forEach(grantedAuthority -> scopes.add(grantedAuthority.getAuthority()));
                context.getClaims()
                        .id(authentication.getName())
                        .subject(authentication.getName())
                        .claim("scope", scopes)
                        .claim("uuid", customUserDetails.getUser().getUuid())
                        .claim("userFullName", String.format("%s %s", customUserDetails.getUser().getFamilyName(),
                                customUserDetails.getUser().getGivenName()));
            }
        };
    }


    /*@Bean
    JwtAuthenticationConverter jwtAuthenticationConverter() {

        Converter<Jwt, Collection<GrantedAuthority>> jwtGrantedAuthoritiesConverter = jwt -> {
            Collection<String> scopes = jwt.getClaimAsStringList("scope");

            scopes.forEach(s -> log.info("Scope: {}", s));

            return scopes.stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
        };

        var jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);

        return jwtAuthenticationConverter;
    }*/

}