#!/usr/bin/env python
# -*- coding: utf-8 -*-
'''
# @Time       : 9/8/20 1:11 AM
# @Author     : Francis(francis_xiiiv@163.com)
# @File       : test_post.py
# @Description: Type whatever you want
'''

import unittest
from util.h import Http
from km.drill.miner_supr import MinerSupr

import json


class TestHttp(unittest.TestCase):
    def setUp(self):
        pass

    def tearDown(self):
        pass

    def test_post(self):
        url = 'http://192.168.207.130:8080/reward'
        minerSupr = MinerSupr('')
        data = {}

        gensig = '1C532145697A8B6F'
        string = 'abcdefgh'
        bys = bytes(string, encoding="utf8")
        proof = str(bys, encoding="utf8")
        bag = minerSupr.bag()

        data['gensig'] = gensig
        data['proof'] = proof
        data['bag'] = bag


        newUrl = Http.newUrl(url,{'requestType':'participateinTaiSai'})
        resp = Http.post(newUrl,data)
        print('resp: %s' % resp)


if __name__ == '__main__':
    unittest.main()