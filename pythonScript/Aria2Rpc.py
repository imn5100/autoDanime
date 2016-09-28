# -*- coding: utf-8 -*-
import requests
import os
import time
import base64
from single_conn import execute_low_level
import json
from config import *

class Aria2JsonRpc(object):
    def __init__(self, rpc_url, arai2_path):
        self.rpc_url = rpc_url
        self.arai2_path = arai2_path

    def startAria2Rpc(self):
        file = open("startAria2Rpc.bat", "w")
        newcmd = "\"" + self.arai2_path + "aria2c.exe\"  --enable-rpc --rpc-listen-all=true --rpc-allow-origin-all -c";
        file.write(newcmd)
        file.close()
        os.startfile((os.getcwd() + "\\startAria2Rpc.bat"))

    # 构造json请求头
    def buildHeader(self):
        headers = {
            'User-Agent': 'Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.102 Safari/537.36'}
        headers['Accept'] = 'application/json, text/javascript, */*; q=0.01'
        headers['Accept-Encoding'] = 'gzip, deflate'
        headers['Content-Type'] = 'gzip'
        headers['Accept-Language'] = 'zh-CN,zh;q=0.8,en;q=0.6'
        headers['Connection'] = 'keep-alive'
        headers['Content-Type'] = 'application/x-www-form-urlencoded; charset=UTF-8'
        return headers

    def execuetJsonRpcCmd(self, method, param=None):
        payload = {"jsonrpc": "2.0", "method": method, "id": 1, "params": param}
        tm = long(time.time() * 1000)
        url = rpc_url % str(tm)
        print(payload)
        r = requests.post(url, None, payload, headers=self.buildHeader());
        print(r.json())
        return r.status_code;

    def isAlive(self):
        payload = {"jsonrpc": "2.0", "method": "aria2.tellActive", "id": 1}
        tm = long(time.time() * 1000)
        url = rpc_url % str(tm)
        try:
            r = requests.get(url, payload, headers=self.buildHeader());
            return r.status_code == 200
        except Exception, e:
            print (e.message)
            return False

    def addUri(self, url, dir=None, out=None):
        params = [];
        download_config = {}
        if dir:
            download_config["dir"] = dir
        if out:
            download_config["out"] = out
        params.append(url);
        params.append(download_config)
        print(self.execuetJsonRpcCmd("aria2.addUri", params))


    def addTorrent(self, path, dir=None, out=None):
        bits = open(path, "rb").read();
        torrent = base64.b64encode(bits)
        params = [];
        download_config = {}
        if dir:
            download_config["dir"] = dir
        if out:
            download_config["out"] = out
        params.append(torrent);
        params.append([]);
        params.append(download_config)
        print(self.execuetJsonRpcCmd("aria2.addTorrent", params))


if __name__ == '__main__':
    # 模式2 通过种子下载文件
    config = Config("config.ini")
    rpc_url = "http://localhost:6800/jsonrpc?tm=%s"
    mode = config.get("system","aria2_mode")
    aria2_path = config.get("system","aria2_path")
    rpcClient = Aria2JsonRpc(rpc_url, aria2_path)
    # 下载链接
    if not rpcClient.isAlive():
        rpcClient.startAria2Rpc()
        time.sleep(3)
    if mode == 1:
        jstr = execute_low_level("RPOPLPUSH", "Magnet_Queue", "Magnet_Queue_Bak", host='localhost', port=6379)
        if jstr:
            jobj = json.loads(jstr)
            magnet = [jobj['magnetLink'], ]
            rpcClient.addUri(magnet, os.getcwd() + "\\resource");
    else:
        torrentfile = execute_low_level("RPOPLPUSH", "Torrent_Queue_Bak", "Torrent_Queue_Bak", host='localhost', port=6379)
        if torrentfile:
            rpcClient.addTorrent(torrentfile.decode("utf-8"), (os.getcwd() + "\\resource"))
