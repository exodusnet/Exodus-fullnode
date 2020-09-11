#!/usr/bin/env python
# -*- coding: utf-8 -*-
'''
# @Time       : 9/8/20 12:33 AM
# @Author     : Francis(francis_xiiiv@163.com)
# @File       : test_bag.py
# @Description: Type whatever you want
'''

import unittest

from km.drill.miner_supr import MinerSupr


class TestMinerSupr(unittest.TestCase):
    def setUp(self):
        self.minerSupr = MinerSupr('')

    def tearDown(self):
        pass

    def test_bag(self):
        bag = self.minerSupr.bag()
        print(bag)


if __name__ == '__main__':
    unittest.main()