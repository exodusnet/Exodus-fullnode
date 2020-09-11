#!/usr/bin/env python
# -*- coding: utf-8 -*-
'''
# @Time       : 9/5/20 8:10 PM
# @Author     : Francis(francis_xiiiv@163.com)
# @File       : miner_supr_msg.py.py
# @Description: Type whatever you want
'''


class NetState():
    def __init__(self, height, gensig):
        self.height = height
        self.gensig = gensig


class MsgSubmitResult():
    def __init__(self, nonce):
        self.nonce = nonce

class MsgGetBagResult():
    def __init__(self,bag):
        self.bag = bag
