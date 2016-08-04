# -*- coding: utf-8 -*-
import ConfigParser
import sys, os, time

class Config:
    def __init__(self, path):
        self.path = path
        self.cf = ConfigParser.ConfigParser()
        #self.cf.readfp(codecs.open(path, "r", "utf-8"))
        self.cf.read(self.path)
    def get(self, field, key):
        result = ""
        try:
            result = self.cf.get(field, key)
        except Exception, ex:
            print Exception, ":", ex
            result = ""
        return result;
    def set(self, field, key, value):
        try:
            self.cf.set(field, key, value)
            self.cf.write(open(self.path), 'w')
        except Exception, ex:
            print Exception, ":", ex
            return False
        return True

def read_config(config_file_path, field, key):
    cf = ConfigParser.ConfigParser()
    try:
        cf.read(config_file_path)
        result = cf.get(field, key)
    except Exception, ex:
        print Exception, ":", ex
        sys.exit()
    return result

def  write_config(config_file_path, field, key, value):
    cf = ConfigParser.ConfigParser()
    try:
        cf.read(config_file_path)
        cf.set(field, key, value)
        cf.write(open(config_file_path, 'w'))
    except Exception, ex:
        print Exception, ":", ex
        sys.exit();
    return  True;


if __name__ == '__main__':
    config = Config('data.ini')
    print config.get('global', 'filename')
    print config.get('global', 'magnet')
    
