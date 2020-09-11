#! /usr/bin/env python
# -*- encoding: utf-8 -*-
'''
@File         :test_miner_supr.py
@Description  :Type whatever you want
@Datatime     :2020/09/05 02:28:38
@Author       :Francis(francis_xiiiv@163.com)
@version      :1.0
'''
import unittest

from km.drill.miner_supr import MinerSupr


class TestMinerSupr(unittest.TestCase):
    def setUp(self):
        self.minerSupr = MinerSupr('')

    def tearDown(self):
        pass

    def test_tick(self):
        self.minerSupr.tick()


if __name__ == '__main__':
    unittest.main()
