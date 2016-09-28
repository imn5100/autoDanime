# -*- coding: utf-8 -*-
import libtorrent as lt
from config import *

def magnet2t(link, tfile):
    sess = lt.session()
    params = {
        "save_path": '/',
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

        t = open(tfile.decode("UTF-8"), "wb")
        t.write(lt.bencode(torfile.generate()))
        t.close()
        print '%s  generated!' % tfile
    except Exception, ex:
        print Exception, ":", ex
        return False
    return True

def main(config_file_path):
    config = Config(config_file_path)
    filename = config.get('global', 'filename')
    magent = config.get('global', 'magnet')
    print "start download file %s" % filename.decode("utf-8");
    magnet2t(magent, os.getcwd() + '/resource/%s.torrent' % filename);
    return;
    
# 读取data.ini中的配置文件,获取磁链和文件名，开始下载种子文件
if __name__ == '__main__':
    main('magnetData.ini')
