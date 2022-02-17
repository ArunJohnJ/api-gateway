package com.microservice.apigateway;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiGatewayConfiguration {

	@Bean
	public RouteLocator gatewayRouter(RouteLocatorBuilder builder) {
		return builder.routes()
				.route(p -> p.path("/get")
						.filters(f -> f.addRequestHeader("AddedHeader", "headervalue").addRequestParameter("AddedParam",
								"ParamValue"))
						.uri("http://httpbin.org:80"))
				.route(p -> p.path("/currency-exchange/**").uri("lb://currency-exchange"))// lb refers to load balancer
																							// and currency-exchange
																							// refers to the name of the
																							// application which is
																							// registered to Eureka
				// we basically added custom routes so that we don't have to type out long URLs
				.route(p -> p.path("/currency-conversion/**").uri("lb://CURRENCY-CONVERSION"))
				.route(p -> p.path("/currency-conversion-feign/**").uri("lb://CURRENCY-CONVERSION"))
				.route(p -> p.path("/currency-conversion-new/**")
						.filters(f -> f.rewritePath("/currency-conversion-new/(?<segment>.*)",
								"/currency-conversion-feign/${segment}"))
						.uri("lb://currency-conversion"))// will give same output as feign for this URL
															// http://localhost:8765/currency-conversion-new/from/USD/to/INR/quantity/10
				.build();
	}
}
