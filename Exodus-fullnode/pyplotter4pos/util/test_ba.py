
# !/usr/bin/env python
# -*- encoding: utf-8 -*-
'''
@File         :test_bytearray.py
@Description  :Type whatever you want
@Datatime     :2020/09/02 06:10:24
@Author       :Francis(francis_xiiiv@163.com)
@version      :1.0
'''

import unittest
import struct
import ba


class TestBytearray(unittest.TestCase):
    def setUp(self):
        pass

    def tearDown(self):
        pass

    def test_copy(self):
        print('test_copy - ')
        sz = struct.calcsize('q')
        dest = bytearray(10 + sz)
        src = struct.pack('q', 9)
        ba.copy(src, 0, dest, 10, sz)

        # print(dest)

        src = bytearray(range(1, 15))
        dest = bytearray(10)
        dest[9] = 9

        ba.copy(src, 1, dest, 3, 20)
        print('src:%s,dest:%s' % (src, dest))

    def test_slice(self):
        dbytes1 = bytearray([1, 2, 3, 4, 5])
        slicedDbytes1 = ba.slice(dbytes1, 1, 0)
        print('test_slice - [%s] off [%s]' % (slicedDbytes1, dbytes1))


if __name__ == '__main__':
    unittest.main()
