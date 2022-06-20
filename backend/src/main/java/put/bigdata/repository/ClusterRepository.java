package put.bigdata.repository;

import java.util.ArrayList;
import java.util.UUID;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;

import put.bigdata.model.Cluster;

@Repository
public interface ClusterRepository extends CassandraRepository<Cluster, UUID> {

  @Query("select * from cluster where useremails contains ?0 allow filtering;")
  ArrayList<Cluster> findByUserEmail(String userEmail);
}
