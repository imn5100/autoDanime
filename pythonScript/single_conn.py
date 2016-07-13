# -*- coding: utf-8 -*-
import redis
#下载脚本运行时间较长，无法快速返回请求的redis连接。所以这里提供 单连接操作redis
def execute_low_level(command, *args, **kwargs):
    connection = redis.Connection(**kwargs)
    try:
        connection.connect()
        connection.send_command(command, *args)
        response = connection.read_response()
        if command in redis.Redis.RESPONSE_CALLBACKS:
            return redis.Redis.RESPONSE_CALLBACKS[command](response)
        return response
    finally:
        connection.disconnect()
        del connection
