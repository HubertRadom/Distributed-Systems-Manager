package put.bigdata.model;

import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.Objects;
import java.util.UUID;

@Table
public class Log {

    @PrimaryKey
    private UUID id;

    private UUID nodeId;

    private UUID clusterId;

    private String userEmail;

    private String status;

    String timestamp;

    public Log(UUID id, UUID nodeId, UUID clusterId, String userEmail, String status, String timestamp) {
        this.id = id;
        this.nodeId = nodeId;
        this.clusterId = clusterId;
        this.userEmail = userEmail;
        this.status = status;
        this.timestamp = timestamp;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getNodeId() {
        return nodeId;
    }

    public void setNodeId(UUID nodeId) {
        this.nodeId = nodeId;
    }

    public UUID getClusterId() {
        return clusterId;
    }

    public void setClusterId(UUID clusterId) {
        this.clusterId = clusterId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Log log = (Log) o;
        return Objects.equals(id, log.id) && Objects.equals(nodeId, log.nodeId) && Objects.equals(clusterId, log.clusterId) && Objects.equals(userEmail, log.userEmail) && Objects.equals(status, log.status) && Objects.equals(timestamp, log.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nodeId, clusterId, userEmail, status, timestamp);
    }
}
