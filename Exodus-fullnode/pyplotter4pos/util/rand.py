#!/usr/bin/env python
# -*- coding: utf-8 -*-
'''
# @Time       : 9/16/20 10:45 PM
# @Author     : Francis(francis_xiiv@163.com)
# @File       : rand.py
# @Description: Type whatever you want
'''


import random
import string

def randStr(length):
    letters = string.ascii_lowercase
    digits = string.digits
    s = ''.join(random.sample(letters+digits, length))

def randHexStr(length):
    hexdigits = string.hexdigits
    s = ''.join(random.sample(hexdigits, length))

    return s

def randInt(a,b):
    return random.randint(a, b)