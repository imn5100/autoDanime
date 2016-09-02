# -*- coding: utf-8 -*-
import requests
import os
from BeautifulSoup import BeautifulSoup
#这里http请求存在超时问题。需要模拟浏览器进行请求
def getDMHYHtml(url):
    r = requests.get(url);
    return  r.text

def analysisHtml(text):
    soup = BeautifulSoup(text)
    body = soup.find('tbody')
    for tr in body.findAll("tr"):
        tds = tr.findAll("td")
        time = tds[0].find("span").text
        classi = tds[1].find("font").text
        title = tds[2].find("a",target="_blank")["href"]
        magnetLink = tds[3].find("a")["href"]
        size = tds[4].text
        sendNum = tds[5].find("span").text
        downNum = tds[6].find("span").text
        comNum = tds[7].text
        publisher = tds[8].find("a").text
        print("time:"+time+" classi:"+classi+" title:"+title+" magnetLink:"+magnetLink+" size:"+size+" sendNum:"+sendNum+ " downNum:" +downNum+" comNum:"+comNum+" publisher:"+publisher)

class DmhyData(object):
    def __init__(self):
        pass

if __name__ == '__main__':
    # f = open("E://download//share.dmhy.org.html","r");
    # size =  os.path.getsize("E://download//share.dmhy.org.html");
    # text =  f.read(size);
    text = getDMHYHtml("http://share.dmhy.org/topics/list/page/1")
    analysisHtml(text)






