# -*- coding: utf-8 -*-
import requests
import os
from BeautifulSoup import BeautifulSoup
# 这里http请求存在超时问题。需要模拟浏览器进行请求
def getDMHYHtml(url):
    r = requests.get(url,headers=buildHeader());
    return  r.text

def buildHeader():
    headers = {'User-Agent': 'Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.102 Safari/537.36'}
    headers['Accept'] = 'text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8'
    headers['Accept-Encoding'] = 'gzip, deflate, sdch'
    headers['Content-Type'] = 'gzip'
    headers['Accept-Language'] = 'zh-CN,zh;q=0.8,en;q=0.6'
    headers['Connection'] = 'keep-alive'
    headers['Content-Type'] = 'gzip'
    headers['Host'] = 'share.dmhy.org'
    headers['referer'] = 'http://share.dmhy.org'
    headers['Cookie'] = '__cfduid=d1a9b70e02b3116c622b3747893a44ef81472616279; Hm_lvt_e4918ccc327a268ee93dac21d5a7d53c=1472616271,1472692420,1472779166,1472803144; Hm_lpvt_e4918ccc327a268ee93dac21d5a7d53c=1472803144'
    return headers

def analysisHtml(text):
    soup = BeautifulSoup(text)
    body = soup.find('tbody')
    for tr in body.findAll("tr"):
        tds = tr.findAll("td")
        time = tds[0].find("span").text
        classi = tds[1].find("font").text
        title = tds[2].find("a", target="_blank")["href"]
        magnetLink = tds[3].find("a")["href"]
        size = tds[4].text
        sendNum = tds[5].find("span").text
        downNum = tds[6].find("span").text
        comNum = tds[7].text
        publisher = tds[8].find("a").text
        print("time:" + time + " classi:" + classi + " title:" + title + " magnetLink:" + magnetLink + " size:" + size + " sendNum:" + sendNum + " downNum:" + downNum + " comNum:" + comNum + " publisher:" + publisher)

class DmhyData(object):
    def __init__(self):
        pass

if __name__ == '__main__':
    # f = open("E://download//share.dmhy.org.html","r");
    # size =  os.path.getsize("E://download//share.dmhy.org.html");
    # text =  f.read(size);
    text = getDMHYHtml("http://share.dmhy.org/topics/list/page/1")
    analysisHtml(text)






