#! /usr/bin/env python
# -*- encoding: utf-8 -*-
'''
@File         :test_gen_supervisor.py
@Description  :Type whatever you want
@Datatime     :2020/09/04 05:14:41
@Author       :Francis(francis_xiiiv@163.com)
@version      :1.0
'''


import unittest

from km.gen.gen_supervisor import GenSupervisor
from km.gen.gen_params import GenParams


class TestGenSupervisor(unittest.TestCase):
    def setUp(self):
        self.genParams = GenParams('JCKNOS7KFSBZVRFYLJB7OSC35TQOEMF6', 50, 2, 1)

    def tearDown(self):
        pass

    def test_init(self):
        genSupervisor = GenSupervisor(self.genParams)
        genSupervisor.init()


if __name__ == '__main__':
    unittest.main()
