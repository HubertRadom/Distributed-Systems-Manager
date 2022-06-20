import asyncio
import aiohttp

async def get_symbols():
    async with aiohttp.ClientSession() as session:
        res = await session.delete('http://localhost:8080/api/delete/all', ssl=False)
        print(res)

asyncio.run(get_symbols())

