#! /usr/bin/env python
# -*- encoding: utf-8 -*-
'''
@File         :env.py
@Description  :Type whatever you want
@Datatime     :2020/09/04 20:23:05
@Author       :Francis(francis_xiiiv@163.com)
@version      :1.0
'''

import os


reward_url = 'http://c-prod-1.exodusnetwork.io/reward'
# reward_url = 'http://192.168.207.130:8080/reward'

os = 'linux'
# os = 'openwrt'

def getRoot():
    '''
    The folder is used to build "$root/plots" directory in which there are plot files

    :return:
    '''
    return os.path.dirname(__file__)
