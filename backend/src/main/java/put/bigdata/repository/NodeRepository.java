package put.bigdata.repository;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;
import put.bigdata.model.Node;

import java.util.ArrayList;
import java.util.UUID;

@Repository
public interface NodeRepository extends CassandraRepository<Node, UUID> {

    @Query("select * from node where clusterId = ?0 allow filtering;")
    ArrayList<Node> findByClusterId(UUID clusterId);
}
