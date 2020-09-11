#!/usr/bin/env python
# -*- coding: utf-8 -*-
'''
# @Time       : 9/6/20 2:12 AM
# @Author     : Francis(francis_xiiiv@163.com)
# @File       : test_get.py.py
# @Description: Type whatever you want
'''

import unittest
from util.h import Http

import json


class TestHttp(unittest.TestCase):
    def setUp(self):
        pass

    def tearDown(self):
        pass

    def test_get(self):
        url = 'http://192.168.207.130:8080/reward'
        data = {}

        resp = Http.get(url,{'requestType':'makeBag'})

        dic = json.loads(resp)

        print(dic);


if __name__ == '__main__':
    unittest.main()