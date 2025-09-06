-- Lua脚本接收KEYS和ARGV参数
-- KEYS[1]: GEO Key (vehicle:locations)
-- KEYS[2]: ZSET Key (vehicle:speeds)
-- KEYS[3]: HASH Key Prefix (vehicle:)
-- ARGV[1]: vehicleId
-- ARGV[2]: latitude
-- ARGV[3]: longitude
-- ARGV[4]: speed
-- ARGV[5]: fuelLevel
-- ARGV[6]: isAlarm
-- ARGV[7]: timestamp

local vehicleId = ARGV[1]
local longitude = tonumber(ARGV[3])
local latitude = tonumber(ARGV[2])
local speed = tonumber(ARGV[4])
local fuelLevel = tonumber(ARGV[5])
local isAlarm = ARGV[6]
local timestamp = ARGV[7]

-- 1. GEOADD - 添加位置信息
redis.call('GEOADD', KEYS[1], longitude, latitude, vehicleId)

-- 2. ZADD - 添加速度信息
redis.call('ZADD', KEYS[2], speed, vehicleId)

-- 3. HSET - 添加其他属性
redis.call('HSET', KEYS[3] .. vehicleId, 'fuelLevel', fuelLevel, 'isAlarm', isAlarm, 'timestamp', timestamp)

return '"OK"'