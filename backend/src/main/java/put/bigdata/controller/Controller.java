package put.bigdata.controller;

import com.datastax.oss.driver.api.core.uuid.Uuids;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import put.bigdata.model.*;
import put.bigdata.repository.*;

import java.sql.Timestamp;
import java.util.*;

@RestController
@RequestMapping("/api")
public class Controller {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ClusterRepository clusterRepository;

    @Autowired
    NodeRepository nodeRepository;

    @Autowired
    LogRepository logRepository;

    @PostMapping(path="user/create-user")
    public void createUser(@RequestParam(name="email") String email, @RequestParam(name="username") String username, @RequestParam(name="password") String password) {
        User user = new User(email, username, password, new ArrayList<>());
        userRepository.save(user);
    }

    @PostMapping(path="cluster/create")
    public UUID createCluster(@RequestParam(name="name") String name, @RequestParam(name="adminEmail") String adminEmail) {
        Optional<User> optionalAdmin = userRepository.findByEmail(adminEmail);
        if (optionalAdmin.isPresent()) {
            User admin = optionalAdmin.get();

            UUID clusterId = Uuids.timeBased();
            ArrayList<String> userEmails = new ArrayList<>();
            userEmails.add(adminEmail);
            Cluster cluster = new Cluster(clusterId, name, adminEmail, new ArrayList<>(), userEmails);
            clusterRepository.save(cluster);

            admin.addClusterId(clusterId);
            userRepository.save(admin);
            return clusterId;
        }
        return Uuids.timeBased();
    }

    @PostMapping(path="cluster/add-user")
    public void addUserToCluster(@RequestParam(name="clusterId") UUID clusterId, @RequestParam(name="userEmail") String userEmail) {
        Optional<User> optionalUser = userRepository.findByEmail(userEmail);
        Optional<Cluster> optionalCluster = clusterRepository.findById(clusterId);
        if (optionalUser.isPresent() && optionalCluster.isPresent()) {
            User user = optionalUser.get();
            Cluster cluster = optionalCluster.get();

            user.addClusterId(clusterId);
            userRepository.save(user);

            cluster.addUserEmail(user.getEmail());
            clusterRepository.save(cluster);
        }
    }

    @DeleteMapping(path="cluster/delete-user")
    public void deleteUserFromCluster(@RequestParam(name="clusterId") UUID clusterId, @RequestParam(name="userEmail") String userEmail) {
        Optional<Cluster> optionalCluster = clusterRepository.findById(clusterId);
        Optional<User> optionalUser = userRepository.findByEmail(userEmail);
        if (optionalCluster.isPresent() && optionalUser.isPresent()) {
            Cluster cluster = optionalCluster.get();
            User user = optionalUser.get();

            user.removeClusterId(clusterId);
            userRepository.save(user);

            cluster.removeUserEmail(userEmail);
            clusterRepository.save(cluster);
        }
    }

    @PostMapping(path="cluster/add-node")
    public UUID createNode(@RequestParam(name="clusterId") UUID clusterId, @RequestParam(name="name") String name) {
        Optional<Cluster> optionalCluster = clusterRepository.findById(clusterId);
        if (optionalCluster.isPresent()) {
            Cluster cluster = optionalCluster.get();

            UUID nodeId = Uuids.timeBased();
            Node node = new Node(nodeId, name, clusterId, "UNKNOWN");
            nodeRepository.save(node);

            cluster.addNodeId(nodeId);
            clusterRepository.save(cluster);
            return nodeId;
        }
        return Uuids.timeBased();
    }

    @DeleteMapping(path="cluster/delete-node")
    public void deleteNode(@RequestParam(name="nodeId") UUID nodeId) {
        Optional<Node> optionalNode = nodeRepository.findById(nodeId);
        if (optionalNode.isPresent()) {
            Node node = optionalNode.get();
            Optional<Cluster> optionalCluster = clusterRepository.findById(node.getClusterId());
            if (optionalCluster.isPresent()) {
                Cluster cluster = optionalCluster.get();

                nodeRepository.delete(node);

                cluster.removeNodeId(nodeId);
                clusterRepository.save(cluster);
            }
        }
    }

    @PostMapping(path="node/change-status")
    public void changeNodeStatus(@RequestParam(name="nodeId") UUID nodeId, @RequestParam(name="status") String status) {
        Optional<Node> optionalNode = nodeRepository.findById(nodeId);
        if (optionalNode.isPresent()) {
            Node node = optionalNode.get();

            node.setStatus(status);
            nodeRepository.save(node);
        }
    }

    @PostMapping(path="node/create-log")
    public void createLog(@RequestParam(name="nodeId") UUID nodeId, @RequestParam(name="userEmail") String userEmail) {
        Optional<Node> optionalNode = nodeRepository.findById(nodeId);
        if (optionalNode.isPresent()) {
            Node node = optionalNode.get();

            UUID logId = Uuids.timeBased();
            String timestamp = new Timestamp(System.currentTimeMillis()).toString();
            Log log = new Log(logId, node.getId(), node.getClusterId(), userEmail, node.getStatus(), timestamp);
            logRepository.save(log);
        }
    }

    @PostMapping(path="cluster/change-status")
    public void changeClusterStatus(@RequestParam(name="clusterId") UUID clusterId, @RequestParam(name="status") String status) {
        System.out.println("STATUS:" + status);
        ArrayList<Node> nodes = nodeRepository.findByClusterId(clusterId);
        for (int i = 0; i < nodes.size(); i++) {
            nodes.get(i).setStatus(status);
        }
        nodeRepository.saveAll(nodes);

        boolean checkAgain = true;
        while (checkAgain) {
            Integer offCount = 0;
            Integer onCount = 0;
            ArrayList<Node> updatedNodes = nodeRepository.findByClusterId(clusterId);
            for (int i = 0; i < updatedNodes.size(); i++) {
                if (updatedNodes.get(i).getStatus().equals("ON")) {
                    onCount++;
                } else {
                    offCount++;
                }
            }
            if (onCount != 0 && offCount != 0) {
                System.out.println("ON COUNT:" + onCount.toString() + " OFF COUNT:" + offCount.toString());
                for (int i = 0; i < updatedNodes.size(); i++) {
                    if (onCount > offCount) {
                        nodes.get(i).setStatus("ON");
                    } else {
                        nodes.get(i).setStatus("OFF");
                    }
                }
                nodeRepository.saveAll(nodes);
            } else {
                checkAgain = false;
            }
        }
    }

    @GetMapping(path="clusters")
    public ArrayList<Cluster> getClusters(@RequestParam(name="userEmail") String userEmail) {
        return clusterRepository.findByUserEmail(userEmail);
    }

    @GetMapping(path="nodes")
    public ArrayList<Node> getNodes(@RequestParam(name="clusterId") UUID clusterId) {
        return nodeRepository.findByClusterId(clusterId);
    }
    
    @GetMapping(path="node/log")
    public ArrayList<Log> getLogs(@RequestParam(name="nodeId") UUID nodeId) {
        return logRepository.findByNodeId(nodeId);
    }  

    @DeleteMapping(path="delete/all")
    public void deleteAll() {
        System.out.println("wielki reset");
        userRepository.deleteAll();
        clusterRepository.deleteAll();
        nodeRepository.deleteAll();
        logRepository.deleteAll();
    }

    @DeleteMapping(path="delete/all-logs")
    public void deleteAllLogs() {
        System.out.println("wielki reset logów");
        logRepository.deleteAll();
    }
}
