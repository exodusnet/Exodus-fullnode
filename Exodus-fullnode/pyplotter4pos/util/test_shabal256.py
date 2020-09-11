#!/usr/bin/env python
# -*- encoding: utf-8 -*-
'''
@File         :test_shabal256.py
@Description  :Type whatever you want
@Datatime     :2020/08/31 03:33:14
@Author       :Francis(francis_xiiiv@163.com)
@version      :1.0
'''

import unittest
# import util.shabal256 as shabal256
import ctypes
import binascii


class TestShabal256(unittest.TestCase):
    def setUp(self):
        pass

    def tearDown(self):
        pass

    # def test_howtouse(self):
    #     self.assertEqual('6563a2d36f2f541e38aaa3f5375bfae8ce1dd2811cdf0993216669d48618aa9a', shabal256.digest('hello'))
    #     self.assertEqual('d55e745e7bfb395e6cc13a5c57b2972100ce2fb18a25041ab2fea333c2e9e425', shabal256.digest('hello world'))
    #
    #     x = [124, 67, 45, 11]
    #     y = bytearray(x)
    #     print(y)
    #     print(shabal256.digestFromBytearray(y))

    def test_c(self):
        hello = ctypes.c_char_p()
        hello.value = 'hello'.encode()
        r = ctypes.create_string_buffer(32)

        libshabal = ctypes.cdll.LoadLibrary('./libshabal.linux.so')
        libshabal.shabal256(hello,r)
        rVal =r.value

        print(str(rVal))
        #print(rVal.hex())
        print(binascii.hexlify(rVal).decode()) # 6563a2d36f2f541e38aaa3f5375bfae8ce1dd2811cdf0993216669d48618aa9a


if __name__ == '__main__':
    unittest.main()
