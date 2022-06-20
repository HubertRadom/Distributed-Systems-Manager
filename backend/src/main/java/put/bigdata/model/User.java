package put.bigdata.model;

import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Table
public class User {

    @PrimaryKey
    private String email;

    private String username;

    private String password;

    private List<UUID> clusterIds;

    public User(String email, String username, String password, List<UUID> clusterIds) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.clusterIds = clusterIds;
    }

    public void addClusterId(UUID clusterId) {
        if (clusterIds == null) {
            clusterIds = new ArrayList<UUID>();
        }
        clusterIds.add(clusterId);
    }

    public void removeClusterId(UUID clusterId) {
        clusterIds.remove(clusterId);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<UUID> getClusterIds() {
        return clusterIds;
    }

    public void setClusterIds(List<UUID> clusterIds) {
        this.clusterIds = clusterIds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(email, user.email) && Objects.equals(username, user.username) && Objects.equals(password, user.password) && Objects.equals(clusterIds, user.clusterIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, username, password, clusterIds);
    }
}
