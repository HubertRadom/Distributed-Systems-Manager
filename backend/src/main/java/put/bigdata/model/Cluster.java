package put.bigdata.model;

import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Table
public class Cluster {

    @PrimaryKey
    private UUID id;

    private String name;

    private String adminEmail;

    private List<UUID> nodeIds;

    private List<String> userEmails;

    public Cluster(UUID id, String name, String adminEmail, List<UUID> nodeIds, List<String> userEmails) {
        this.id = id;
        this.name = name;
        this.adminEmail = adminEmail;
        this.nodeIds = nodeIds;
        this.userEmails = userEmails;
    }

    public void addUserEmail(String userEmail) {
        userEmails.add(userEmail);
    }

    public void removeUserEmail(String userEmail) {
        userEmails.remove(userEmail);
    }

    public void addNodeId(UUID nodeId) {
        if (nodeIds == null) {
            nodeIds = new ArrayList<UUID>();
        }
        nodeIds.add(nodeId);
    }

    public void removeNodeId(UUID nodeId) {
        nodeIds.remove(nodeId);
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

    public String getAdminEmail() {
        return adminEmail;
    }

    public void setAdminEmail(String adminEmail) {
        this.adminEmail = adminEmail;
    }

    public List<UUID> getNodeIds() {
        return nodeIds;
    }

    public void setNodeIds(List<UUID> nodeIds) {
        this.nodeIds = nodeIds;
    }

    public List<String> getUserEmails() {
        return userEmails;
    }

    public void setUserEmails(List<String> userEmails) {
        this.userEmails = userEmails;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cluster cluster = (Cluster) o;
        return Objects.equals(id, cluster.id) && Objects.equals(name, cluster.name) && Objects.equals(adminEmail, cluster.adminEmail) && Objects.equals(nodeIds, cluster.nodeIds) && Objects.equals(userEmails, cluster.userEmails);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, adminEmail, nodeIds, userEmails);
    }
}
