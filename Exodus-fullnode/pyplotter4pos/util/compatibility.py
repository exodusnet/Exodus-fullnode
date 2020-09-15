#!/usr/bin/env python
# -*- coding: utf-8 -*-
'''
# @Time       : 9/10/20 8:26 PM
# @Author     : Francis(francis_xiiv@163.com)
# @File       : compatibility.py
# @Description: run into a bunch of compatibility issue,complaining syntax supporting just above kindof version
'''

def bytes_hex(bys):
    '''
    in case of python version < 3.5,otherwise bys.hex() instead
    :param bys:
    :return:
    '''
    hex = ''.join(['%02x' % b for b in bys])

    return hex