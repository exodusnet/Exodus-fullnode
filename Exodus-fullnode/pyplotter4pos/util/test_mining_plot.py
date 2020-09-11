# !/usr/bin/env python
# -*- encoding: utf-8 -*-
'''
@File         :test_mining_plot.py
@Description  :Type whatever you want
@Datatime     :2020/09/03 03:02:19
@Author       :Francis(francis_xiiiv@163.com)
@version      :1.0
'''

import unittest
from util.mining_plot import MiningPlot


class TestMiningPlot(unittest.TestCase):
    def setUp(self):
        pass

    def tearDown(self):
        pass

    def test_init(self):
        miningPlot = MiningPlot(4, 8)
        miningPlot.getScoop(4)


if __name__ == '__main__':
    unittest.main()
