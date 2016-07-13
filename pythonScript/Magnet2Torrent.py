# -*- coding: utf-8 -*-
import libtorrent as lt
from single_conn import execute_low_level
import time
import redis
import json

def magnet2t(link, tfile):
    sess = lt.session()
    params = {
        "save_path": './resource/',
        "storage_mode": lt.storage_mode_t.storage_mode_sparse,
        "paused": True,
        "auto_managed": True,
        "duplicate_is_error": True
    }
    try:
        handle = lt.add_magnet_uri(sess, link, params)
        state_str = ['queued', 'checking', 'downloading metadata', 'downloading',
                 'finished', 'seeding', 'allocating']
        while (not handle.has_metadata()):
            s = handle.status()
            print '%.2f%% complete (down: %.1f kb/s up: %.1f kB/s peers: %d) %s' % (
            s.progress * 100, s.download_rate / 1000, s.upload_rate / 1000, s.
            num_peers, state_str[s.state])
            time.sleep(5)
            print handle.has_metadata()

        torinfo = handle.get_torrent_info()
        torfile = lt.create_torrent(torinfo)

        t = open(tfile.decode("utf-8"), "wb")
        t.write(lt.bencode(torfile.generate()))
        t.close()
        print '%s  generated!' % tfile
    except Exception, ex:
        print Exception, ":", ex
        return False
    return True

def  end_torrent(jstr, name):
    r = redis.StrictRedis("localhost", 6379)
    execute_low_level("LPUSH", "Torrent_Queue", 'resource/%s.torrent' % name, host='localhost', port=6379)
    execute_low_level("LREM", "Magnet_Queue_Bak", 1, jstr, host='localhost', port=6379)

def main():
    jstr = execute_low_level("RPOPLPUSH", "Magnet_Queue", "Magnet_Queue_Bak", host='localhost', port=6379)
    if  jstr:
        jobj = json.loads(jstr)
        name = jobj['simpleName'];
        magnet = jobj['magnetLink']
        if  magnet2t(magnet, 'resource/%s.torrent' % name.encode("UTF-8")):
             end_torrent(jstr, name)
        else:
            return 
    
if __name__ == '__main__':
    main()
