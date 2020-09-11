#!/usr/bin/env python
# -*- coding: utf-8 -*-
'''
# @Time       : 9/5/20 8:12 PM
# @Author     : Francis(francis_xiiiv@163.com)
# @File       : miner_msg.py.py
# @Description: Type whatever you want
'''

from util.convert import Convert


class PlotInfo():
    def __init__(self, filename):
        self.filename = filename
        parts = filename.split("_")
        self.address = Convert.parseUnsignedLong(parts[0])
        self.startnonce = int(parts[1])
        self.plots = int(parts[2])
        self.staggeramt = int(parts[3])


class MsgSendResults():
    def __init__(self):
        pass


class MsgBestResult():
    def __init__(self, bestaddress, bestnonce, proof):
        self.bestaddress = bestaddress
        self.bestnonce = bestnonce
        self.proof = proof
