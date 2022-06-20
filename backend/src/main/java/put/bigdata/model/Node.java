package put.bigdata.model;

import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.Objects;
import java.util.UUID;

@Table
public class Node {

    @PrimaryKey
    private UUID id;

    private String name;

    private UUID clusterId;

    private String status;

    public Node(UUID id, String name, UUID clusterId, String status) {
        this.id = id;
        this.name = name;
        this.clusterId = clusterId;
        this.status = status;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getClusterId() {
        return clusterId;
    }

    public void setClusterId(UUID clusterId) {
        this.clusterId = clusterId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return Objects.equals(id, node.id) && Objects.equals(name, node.name) && Objects.equals(clusterId, node.clusterId) && Objects.equals(status, node.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, clusterId, status);
    }
}
