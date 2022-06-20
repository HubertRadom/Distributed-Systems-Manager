package put.bigdata.repository;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;
import put.bigdata.model.User;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends CassandraRepository<User, UUID> {

    @Query("select * from user where email = ?0;")
    Optional<User> findByEmail(String email);
}
