import asyncio
import aiohttp

async def init():
    async with aiohttp.ClientSession() as session:
        res = await session.delete('http://localhost:8080/api/delete/all', ssl=False)
        
        emails = ['hubert@wp.pl', 'radek@gmail.com', 'marcin@onet.pl']
        username = ['hubert', 'radek', 'marcin']
        passwords = ['hubert123', 'radek123', 'marcin123']
        for i in range(3): 
            res = await session.post('http://localhost:8080/api/user/create-user?email='+emails[i]+'&username='+username[i]+'&password='+passwords[i], ssl=False)
            # print(res)
        
        res = await session.post('http://localhost:8080/api/cluster/create?adminEmail='+emails[0]+'&name=cluster1', ssl=False)
        clusterId = str(await res.text())[1:-1]
        # print('odp to ' + clusterId)
        res = await session.post('http://localhost:8080/api/cluster/add-user?clusterId='+clusterId+'&userEmail='+emails[1], ssl=False)
        
        res = await session.post('http://localhost:8080/api/cluster/add-node?clusterId='+clusterId+'&name=node1', ssl=False)
        nodeId = str(await res.text())[1:-1]
        res = await session.post('http://localhost:8080/api/cluster/add-node?clusterId='+clusterId+'&name=node2', ssl=False)
        nodeId2 = str(await res.text())[1:-1]
        res = await session.post('http://localhost:8080/api/cluster/add-node?clusterId='+clusterId+'&name=node3', ssl=False)

        res = await session.post('http://localhost:8080/api/node/create-log?nodeId='+nodeId+'&userEmail='+emails[0], ssl=False)
        res = await session.post('http://localhost:8080/api/node/create-log?nodeId='+nodeId+'&userEmail='+emails[1], ssl=False)
        res = await session.post('http://localhost:8080/api/node/create-log?nodeId='+nodeId2+'&userEmail='+emails[0], ssl=False)

asyncio.run(init())

