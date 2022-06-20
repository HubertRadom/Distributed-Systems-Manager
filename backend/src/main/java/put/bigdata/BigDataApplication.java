package put.bigdata;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;

@SpringBootApplication(scanBasePackages = {"put.bigdata.controller", "put.bigdata.repository"})
@EnableCassandraRepositories(basePackages = "put.bigdata.repository")
public class BigDataApplication {

    public static void main(String[] args) {
        SpringApplication.run(BigDataApplication.class, args);
    }

}
