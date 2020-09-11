
# !/usr/bin/env python
# -*- encoding: utf-8 -*-
'''
@File         :convert.py
@Description  :Type whatever you want
@Datatime     :2020/09/03 03:39:43
@Author       :Francis(francis_xiiiv@163.com)
@version      :1.0
'''


class Convert():
    @classmethod
    def fullHashToId(cls, address):
        hash = str.encode(address)

        if hash is None or len(hash) < 8:
            raise ValueError('illegal parameters')

        ls = list(hash)
        slicedList = ls[7::-1]

        return int.from_bytes(bytes(slicedList), byteorder='big')

    @classmethod
    def parseUnsignedLong(cls, number):
        iNumber = int(number)
        if iNumber < 0:
            iNumber &= 0xffffffff

        return iNumber


# if __name__ == '__main__':
#     print(Convert.fullHashToId(bytes([0, 1, 2, 3, 4, 5, 6, 7, 8])))
