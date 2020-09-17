redis.pcall(
    "HMSET", KEYS[1],
    "max_permits", tonumber(ARGV[1]),
    "curr_permits", tonumber(ARGV[2]),
    "last_time", tonumber(ARGV[3]),
    "permits_per_second", tonumber(ARGV[4])
)