package test.example.coffeemachineservice.configuration;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
@RequiredArgsConstructor
public class PartitionPreparation {

    private final JdbcTemplate jdbcTemplate;

    @PostConstruct
    public void preparePartitions() {
        jdbcTemplate.update("CALL prepare_partition_years(5)");
    }
}