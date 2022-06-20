package put.bigdata.repository;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;
import put.bigdata.model.Log;
import java.util.ArrayList;
import java.util.UUID;

@Repository
public interface LogRepository extends CassandraRepository<Log, UUID> {

    @Query("select * from log where nodeId = ?0 allow filtering;")
    ArrayList<Log> findByNodeId(UUID nodeId);
}
