package com.example.redis;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.core.GeoOperations;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootApplication
public class RedisApplication {
	
	private ApplicationRunner titledRunner (String title, ApplicationRunner arr) {
		return args -> {
			System.out.println(title.toUpperCase() + ":");
			arr.run(args);
		};
	}
	
	@Bean
	ApplicationRunner geography(RedisTemplate<String, String> rt) {
		return titledRunner("geography", args -> {
			GeoOperations<String, String> geo = rt.opsForGeo();
			geo.add("Sicily", new Point(13.361389, 38.1155556), "Arigento");
			geo.add("Sicily", new Point(15.087269, 37.502669), "Catania");
			geo.add("Sicily", new Point(13.583333, 37.316667), "Arigento");
			
			Circle circle = new Circle(new Point(13.583333, 37.316667),
					new Distance(100, org.springframework.data.redis.connection.RedisGeoCommands.DistanceUnit.KILOMETERS));
			
			GeoResults<org.springframework.data.redis.connection.RedisGeoCommands.GeoLocation<String>> geoResults = geo.radius("Sicily", circle);
			geoResults
				.getContent()
				.forEach(c -> System.out.println(c.toString()));
			
		});
	}
	
	public static void main(String[] args) {
		SpringApplication.run(RedisApplication.class, args);
	}
	
}
