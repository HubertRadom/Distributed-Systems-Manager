import asyncio
from asyncio.tasks import sleep
import aiohttp
import random
from timeit import default_timer as timer

emails = ['hubert@wp.pl', 'radek@gmail.com', 'marcin@onet.pl']
username = ['hubert', 'radek', 'marcin']
passwords = ['hubert123', 'radek123', 'marcin123']

# RESET AND INIT
async def init():
    async with aiohttp.ClientSession() as session:
        res = await session.delete('http://localhost:8080/api/delete/all', ssl=False)
        
        for i in range(3): 
            res = await session.post('http://localhost:8080/api/user/create-user?email='+emails[i]+'&username='+username[i]+'&password='+passwords[i], ssl=False)
        
        res = await session.post('http://localhost:8080/api/cluster/create?adminEmail='+emails[0]+'&name=cluster1', ssl=False)
        clusterId = str(await res.text())[1:-1]
        res = await session.post('http://localhost:8080/api/cluster/add-user?clusterId='+clusterId+'&userEmail='+emails[1], ssl=False)
        res = await session.post('http://localhost:8080/api/cluster/add-user?clusterId='+clusterId+'&userEmail='+emails[2], ssl=False)
        
        nodes = []
        res = await session.post('http://localhost:8080/api/cluster/add-node?clusterId='+clusterId+'&name=node1', ssl=False)
        nodes.append(str(await res.text())[1:-1])
        res = await session.post('http://localhost:8080/api/cluster/add-node?clusterId='+clusterId+'&name=node2', ssl=False)
        nodes.append(str(await res.text())[1:-1])
        res = await session.post('http://localhost:8080/api/cluster/add-node?clusterId='+clusterId+'&name=node3', ssl=False)
        nodes.append(str(await res.text())[1:-1])
        
        # STRESS TEST 1
        print('STRESS TEST 1')
        start = timer()

        tasks = []
        for _ in range(10000):
            tasks.append(session.post('http://localhost:8080/api/node/create-log?nodeId='+(nodes[0])+'&userEmail='+(emails[0]), ssl=False))
            # sleep(0.01)
        responses = await asyncio.gather(*tasks)
        
        end = timer()
        print(end - start)

        # STRESS TEST 2
        print('STRESS TEST 2')
        start = timer()

        tasks = []
        for _ in range(10000):
            tasks.append(session.post('http://localhost:8080/api/node/create-log?nodeId='+random.choice(nodes)+'&userEmail='+random.choice(emails), ssl=False))
        responses = await asyncio.gather(*tasks)

        end = timer()
        print(end - start)

        #STRESS TEST 3
        print('STRESS TEST 3')
        start = timer()

        for i in range(4,100):
            res = await session.post('http://localhost:8080/api/cluster/add-node?clusterId='+clusterId+'&name=node'+str(i), ssl=False)
            nodes.append(str(await res.text())[1:-1])
        
        tasks = []
        #for _ in range(10000):
        tasks.append(session.post('http://localhost:8080/api/cluster/change-status?clusterId='+clusterId+'&status=ON', ssl=False))
        tasks.append(session.post('http://localhost:8080/api/cluster/change-status?clusterId='+clusterId+'&status=OFF', ssl=False))
        responses = await asyncio.gather(*tasks)

        tasks = []
        await session.delete('http://localhost:8080/api/delete/all-logs', ssl=False)
        for i in range(1,100):
            # print(i)
            tasks.append(session.post('http://localhost:8080/api/node/create-log?nodeId='+nodes[i-1]+'&userEmail='+emails[0], ssl=False))
        responses = await asyncio.gather(*tasks)

        end = timer()
        print(end - start)


asyncio.run(init())
