#!/usr/bin/env python
# -*- coding: utf-8 -*-
'''
# @Time       : 9/6/20 2:00 AM
# @Author     : Francis(francis_xiiiv@163.com)
# @File       : h.py
# @Description: http utility
'''

import urllib


class Http:
    @classmethod
    def get(cls, url, data):
        """
        self.Get(url,data）
        :param url:
        :param data:
        :return:
        """
        # processedData = ''
        #
        # if data is not None and len(data)>0:
        #     pairs = '?'
        #     firstTime = True
        #     for k,v in data.items():
        #         if firstTime:
        #             pairs = pairs + k + '=' + v
        #             firstTime = False
        #         else:
        #             pairs = pairs + '&' + k + '=' + v
        #
        #     processedData = pairs
        #
        #
        # new_url = url + processedData
        new_url = Http.newUrl(url, data)
        result = urllib.request.urlopen(new_url)
        response = result.read()
        return response.decode('utf8')

    @classmethod
    def post(cls, url, data):
        """
        self.Post(url,data）
        :param url:
        :param data:
        :return:
        """
        # data = urllib.parse.encode(data)
        # data = data.encode('utf8')
        data = urllib.parse.urlencode(data).encode('utf-8')
        new_url = urllib.request.Request(url, data)
        result = urllib.request.urlopen(new_url)
        response = result.read()
        return response.decode('utf8')

    @classmethod
    def newUrl(self, url, data):
        processedData = ''

        if data is not None and len(data)>0:
            pairs = '?'
            firstTime = True
            for k,v in data.items():
                if firstTime:
                    pairs = pairs + k + '=' + v
                    firstTime = False
                else:
                    pairs = pairs + '&' + k + '=' + v

            processedData = pairs


        new_url = url + processedData

        return new_url
