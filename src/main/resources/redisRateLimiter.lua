local info = redis.pcall("HMGET", KEYS[1], "max_permits", "curr_permits", "last_time", "permits_per_second")

--令牌桶最大容量
local max_permits = tonumber(info[1])
--令牌桶当前容量
local curr_permits = tonumber(info[2])
local curr_permits_copy = tonumber(info[2])
--上一次更新令牌桶的时间
local last_time = tonumber(info[3])
--令牌生成速率（每秒钟生成令牌的数量）
local permits_per_second = tonumber(info[4])

--当前时间
local curr_time = tonumber(ARGV[1])
--请求的令牌个数
local acquire_permits = tonumber(ARGV[2])

--新生成的令牌数
local reserve_permits = math.floor((curr_time-last_time)*permits_per_second/1000)
--更新令牌桶当前容量
local curr_permits = math.min(max_permits, curr_permits + reserve_permits)

--判断是否够令牌
local ret = -1
if curr_permits >= acquire_permits then
    curr_permits = curr_permits - acquire_permits;
    ret = curr_permits
end

--如果令牌桶有新增令牌，更新时间到redis
if reserve_permits > 0 then
    redis.pcall("HSET", KEYS[1], "last_time", curr_time)
end
--如果令牌桶容量有变，更新到redis
if curr_permits ~= curr_permits_copy then
    redis.pcall("HSET", KEYS[1], "curr_permits", curr_permits)
end

return ret

