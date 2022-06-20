import React from "react";
import { useState, useEffect } from "react";

function App() {
  const [newUserEmail, setNewUserEmail] = useState("");
  const [newPassword, setNewPassword] = useState("");

  const [refresh, setRefresh] = useState(false);
  const [userEmail, setUserEmail] = useState("");
  const [clusters, setClusters] = useState<{ name: string; id: string }[]>([]);
  const [chosenCluster, setChosenCluster] = useState<string>("");
  const [nodes, setNodes] = useState<
    { name: string; id: string; status: string }[]
  >([]);
  const [chosenNode, setChosenNode] = useState('');
  const [logs, setLogs] = useState<{id: string, userEmail: string, status: string, timestamp: string}[]>([]);

  const ping = (nodeId: string, userEmail: string) => {
    fetch(`http://localhost:8080/api/node/create-log?nodeId=${nodeId}&userEmail=${userEmail}`,
    {method: 'POST'})
  }

  const changeStatus = (nodeId: string, newStatus: string) => {
    fetch(`http://localhost:8080/api/node/change-status?nodeId=${nodeId}&status=${newStatus}`,
    {method: 'POST'}).then(res => setRefresh(!refresh))
  }

  const changeStatusAll = (clusterId: string, newStatus: string) => {
    fetch(`http://localhost:8080/api/cluster/change-status?clusterId=${clusterId}&status=${newStatus}`,
    {method: 'POST'}).then(res => setRefresh(!refresh))
  }

  useEffect(() => {
    if (chosenNode !== "")
      fetch("http://localhost:8080/api/node/log?nodeId=" + chosenNode).then(
        (a) => {
          a.json().then((b) => {
            setLogs(b);
            console.log(b);
          });
          console.log("fetch nodes");
        }
      ).catch(err => console.log('No logs', err));
  }, [chosenNode]);

  useEffect(() => {
    if (chosenCluster !== "")
      fetch("http://localhost:8080/api/nodes?clusterId=" + chosenCluster).then(
        (a) => {
          a.json().then((b) => {
            setNodes(b);
            console.log(b);
          });
          console.log("fetch nodes");
        }
      );
  }, [chosenCluster, refresh]);

  useEffect(() => {
    if (userEmail !== "")
      fetch("http://localhost:8080/api/clusters?userEmail=" + userEmail).then(
        (a) => {
          a.json().then((b) => {
            setClusters(b);
            console.log(b);
          });
          console.log("fetch clusters");
        }
      );
  }, [userEmail]);

  const Logs = () => {
    return (
      <div>
        {logs.map((item) => (
          <div style={{ display: "flex", margin: "10px" }} key={item.id}>
            <div style={{ marginRight: "5px" }}>{item.status}</div>
            <div style={{ marginRight: "5px" }}>{item.userEmail}</div>
            <div style={{ marginRight: "5px" }}>{item.timestamp}</div>
          </div>
        ))}
      </div>
    );
  }

  const Nodes = () => {
    return (
      <div>
        {nodes.map((item) => (
          <div style={{ display: "flex", margin: "10px" }} key={item.id}>
            <div style={{ marginRight: "5px", cursor: "pointer" }} onClick={() => setChosenNode(item.id)}>{item.name}</div>
            <div style={{ marginRight: "15px" }}>status:{item.status}</div>
            <div style={{ marginRight: "5px", cursor: "pointer" }} onClick={() => changeStatus(item.id, 'ON')}>ON</div>
            <div style={{ marginRight: "15px", cursor: "pointer" }} onClick={() => changeStatus(item.id, 'OFF')}>OFF</div>
            <div style={{ marginRight: "5px", cursor: "pointer" }} onClick={() => ping(item.id, userEmail)}>ping</div>
          </div>
        ))}
        <div style={{display: "flex"}}>
        <div>Turn all: </div>
        <div style={{ marginRight: "5px", cursor: "pointer" }} onClick={() => changeStatusAll(chosenCluster, 'ON')}>ON</div>
        <div style={{ marginRight: "5px", cursor: "pointer" }} onClick={() => changeStatusAll(chosenCluster, 'OFF')}>OFF</div>
          </div>
      </div>
    );
  };

  const Clusters = () => {
    return (
      <div>
        {clusters.map((item) => (
          <div
            key={item.id}
            style={{ cursor: 'pointer'  }}
            onClick={() => {
              setChosenCluster(item.id);
            }}
          >
            {item.name}
          </div>
        ))}
      </div>
    );
  };

  return (
    <div className="App">
      {!userEmail ? (
        <div id="auth">
          <div>EMAIL</div>
          <input
            type="text"
            onChange={(e) => {
              setNewUserEmail(e.target.value);
            }}
          />
          <div>PASSWORD</div>
          <input
            type="text"
            onChange={(e) => {
              setNewPassword(e.target.value);
            }}
          />
          <button
            onClick={() => {
              setUserEmail(newUserEmail);
            }}
          >
            login
          </button>
        </div>
      ) : (
        <>
          <Clusters />
          {nodes.length !== 0 ? <Nodes /> : <div>no nodes</div>}
          {logs.length !== 0 ? <Logs /> : <div>no logs</div>}
        </>
      )}
    </div>
  );
}

export default App;
