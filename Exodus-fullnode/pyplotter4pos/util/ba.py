
# !/usr/bin/env python
# -*- encoding: utf-8 -*-
'''
@File         :bytearray.py
@Description  :bytearray utils similar to System.arraycopy
@Datatime     :2020/09/02 05:54:06
@Author       :Francis(francis_xiiiv@163.com)
@version      :1.0
'''


def copy(src, srcPos, dest, destPos, length):
    '''
    Copies an array from the specified source array, beginning at the specified position, 
    to the specified position of the destination array
    '''
    lenOfSrc = len(src)
    lenOfDest = len(dest)
    smallestByteCount = length

    if lenOfSrc <= srcPos + length:
        smallestByteCount = lenOfSrc - srcPos

    if lenOfDest <= destPos + smallestByteCount:
        smallestByteCount = lenOfDest - destPos

    for index in range(smallestByteCount):
        dest[destPos+index] = src[srcPos+index]

        index = index + 1


def slice(src, off, length):
    '''
    slice a part off src from $off to $(off+length-1) ( or $(len(src)-1))
    '''
    sliceList = []
    LEN = len(src)
    start = off
    stop = off

    if LEN <= off+length:
        stop = LEN
    else:
        stop = off+length

    for index in range(start, stop):
        sliceList.append(src[index])

    return bytearray(sliceList)
