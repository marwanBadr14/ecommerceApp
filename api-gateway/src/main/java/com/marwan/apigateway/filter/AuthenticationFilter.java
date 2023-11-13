package com.marwan.apigateway.filter;

import com.marwan.apigateway.util.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    @Autowired
    private RouteValidator validator;

    @Autowired
    private AuthoritiesManager authoritiesManager;

    //    @Autowired
//    private RestTemplate template;
    @Autowired
    private JwtUtil jwtUtil;

    public AuthenticationFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            if (validator.isSecured.test(exchange.getRequest())) {
                //header contains token or not
                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    throw new RuntimeException("missing authorization header");
                }

                String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                    authHeader = authHeader.substring(7);
                }
                try {
//                    //REST call to AUTH service
//                    template.getForObject("http://IDENTITY-SERVICE//validate?token" + authHeader, String.class);
                    System.out.println("\n" + "\n" + "==========================================" + authHeader + "===============================================" + "\n" + "\n");
                    jwtUtil.validateToken(authHeader);

                    // TODO: Add logic to validate role

                    // decode JWT authHeader & extract all claims from token
                    Claims tokenClaims = jwtUtil.extractAllClaims(authHeader);
                    System.out.println("\n" + "\n" + tokenClaims.toString() + "\n" + "\n");

                    // get "role" claim
                    String userRole = tokenClaims.get("role", String.class);
                    System.out.println("\n" + "\n" + "Role: " + userRole + "\n" + "\n");

                    // get URL from request
                    String url = exchange.getRequest().getURI().getPath();
                    System.out.println("\n" + "\n" + "URL: " + url + "\n" + "\n");

                    // check if user has the proper role for the given URL -> implement helper class
                    if(!authoritiesManager.isUserAuthorized(url,userRole)){

                        // TODO: adjust this code to return HttpResponse.Unauthorized
                        //return new ResponseEntity<String>("Unauthorized user!!", HttpStatus.UNAUTHORIZED);
                        System.out.println("\n" + "\n" + "Reached authority check and failed!!");
//                        ServerHttpResponse response = exchange.getResponse();
//                        response.setStatusCode(HttpStatus.UNAUTHORIZED);
                        throw new RuntimeException();

                    }


                } catch (Exception e) {
                    // TODO: adjust this code to return HttpResponse.Unauthorized
                    System.out.println("invalid access...!");
                    throw new RuntimeException("un authorized access to application");
                }
            }
            return chain.filter(exchange);
        });
    }

    public static class Config {

    }
}
