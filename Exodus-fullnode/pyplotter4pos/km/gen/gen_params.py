
# !/usr/bin/env python
# -*- encoding: utf-8 -*-
'''
@File         :gen_params.py
@Description  :Type whatever you want
@Datatime     :2020/09/03 03:36:52
@Author       :Francis(francis_xiiiv@163.com)
@version      :1.0
'''

from util.convert import Convert


class GenParams():
    def __init__(self, address, startnonce, plots, staggeramt):
        self.address = address

        self.addr = Convert.fullHashToId(address)
        self.startnonce = startnonce
        self.plots = plots
        self.staggeramt = staggeramt
